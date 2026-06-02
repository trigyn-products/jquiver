package com.trigyn.jws.usermanagement.security.config;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.trigyn.jws.usermanagement.vo.JwtRequestDetails;
import com.trigyn.jws.webstarter.utils.JQuiverProperties;

import javax.crypto.SecretKey;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.trigyn.jws.dbutils.cipher.modes.ECBCipherMode;
import com.trigyn.jws.dbutils.entities.PropertyMaster;
import com.trigyn.jws.dbutils.repository.PropertyMasterRepository;
import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.usermanagement.repository.SaltDetailsRepository;
import com.trigyn.jws.usermanagement.service.UserConfigService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Component
public class JwtUtil {

	private final Log					logger						= LogFactory.getLog(getClass());

	@Autowired
	@Lazy
	private PropertyMasterService		propertyMasterService		= null;

	@Autowired
	@Lazy
	private UserDetailsService			userDetailsService			= null;

	@Autowired
	private UserConfigService			userConfigService			= null;

	@Autowired
	private SaltDetailsRepository		saltDetailsRepository		= null;

	@Autowired
	private JQuiverProperties			jQuiverProperties			= null;

	@Autowired
	private PropertyMasterRepository	propertyMasterRepository	= null;

	public String extractUsername(String token, String sri, String uri) {
		return extractClaim(token, Claims::getSubject, sri, uri);
	}

	public Date extractExpiration(String token, String sri, String uri) {
		return extractClaim(token, Claims::getExpiration, sri, uri);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver, String sri, String uri) {
		final Claims claims = extractAllClaims(token, sri, uri);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token, String requestId,String uri) {
		String secretKey;
		try {
			PropertyMaster	propertyMaster			= propertyMasterRepository
					.findByOwnerTypeAndOwnerIdAndPropertyName("system", "system", "enable-user-management");
			boolean			isAuthenticationEnabled	= Boolean.parseBoolean(propertyMaster.getPropertyValue());
			if (Boolean.TRUE == isAuthenticationEnabled) {
				// For decrypting Token...
				String decryptedJwt = token;
				if (Boolean.TRUE == jQuiverProperties.isEnableSecuredAuthentication()) {
					if (token != null && requestId != null && !"".equals(requestId)) {
						SaltDetails saltDetails = saltDetailsRepository.findByRequestId(requestId);
						if (saltDetails != null) {
							try {
								ECBCipherMode ecbMode = new ECBCipherMode("AES", "ECB", "PKCS5Padding", 128);
								decryptedJwt = ecbMode.decrypt(token, saltDetails.getSalt(), "AES");
							} catch (Exception e) {
								logger.warn(
										"Token decryption failed for requestId: " + requestId + " expired or invalid. URI : "+uri);
								throw new ExpiredJwtException(null, null, "Invalid JWT Token. Please try again with new token !", e);
							}
						}
					}
				}
				secretKey = propertyMasterService.findPropertyMasterValue("system", "system", "jwsSecretKey");
				byte[]		keyBytes	= Decoders.BASE64.decode(secretKey);
				SecretKey	key			= Keys.hmacShaKeyFor(keyBytes);
				if (secretKey != null) {
					return Jwts.parser().setSigningKey(key).build().parseClaimsJws(decryptedJwt).getBody();
				}
			}
		} catch (ExpiredJwtException exec) {
			logger.debug("Failed : Invalid JWT Token. " + exec.getMessage() + " URI : "+uri);
			ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (sra != null) {
				HttpServletResponse response = sra.getResponse();
				clearAuthCookies(response);
				setSysLoginCookie(response);
			}
			throw new ExpiredJwtException(null, null, "Invalid JWT Token. Please try again with new token !", exec);
		} catch (Exception exec) {
			logger.debug("Failed : Invalid JWT Token " + exec.getMessage() + " URI : "+uri);
			ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (sra != null) {
				HttpServletResponse response = sra.getResponse();
				clearAuthCookies(response);
				setSysLoginCookie(response);
			}
			throw new ExpiredJwtException(null, null, "Invalid JWT Token. Please try again with new token !", exec);
		}
		return null;

	}

	private Boolean isTokenExpired(String token, String sri, String uri) {
		return extractExpiration(token, sri, uri).before(new Date());
	}

	/**
	 * Auth token will have expiry of 90 minutes
	 */
	public JwtRequestDetails generateToken(UserDetails userDetails) {
		Map<String, Object>	claims		= generateClaims(userDetails);
		long				expiryDate	= System.currentTimeMillis() + 1000 * (jQuiverProperties.getAuthTokenExpiryTime()) * 60;
		return createToken(claims, userDetails.getUsername(), expiryDate);
	}

	private Map<String, Object> generateClaims(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		if (userDetails instanceof UserInformation) {
			UserInformation userInfo = (UserInformation) userDetails;

			claims.put("fullName", userInfo.getFullName());
			claims.put("email", userInfo.getUsername()); // same as subject
			claims.put("userId", userInfo.getUserId());
			claims.put("roles", userInfo.getRoles());
			claims.put("isDefaultPassword", userInfo.isDefaultPassword());
			claims.put("active", userDetails.isEnabled());
		}
		claims.put("username", userDetails.getUsername());
		claims.put("authorities", userDetails.getAuthorities().stream().map(auth -> auth.getAuthority()).toList());
		return claims;
	}

	private JwtRequestDetails createToken(Map<String, Object> claims, String subject, long expiryDate) {
		String secretKey = null;
		try {
			PropertyMaster	propertyMaster			= propertyMasterRepository
					.findByOwnerTypeAndOwnerIdAndPropertyName("system", "system", "enable-user-management");
			boolean			isAuthenticationEnabled	= Boolean.parseBoolean(propertyMaster.getPropertyValue());
			if (Boolean.TRUE == isAuthenticationEnabled) {
				secretKey = propertyMasterService.findPropertyMasterValue("system", "system", "jwsSecretKey");
				if (secretKey != null) {
					byte[]		keyBytes		= Decoders.BASE64.decode(secretKey);
					SecretKey	key				= Keys.hmacShaKeyFor(keyBytes);

					// Define expiration time (e.g., 1 hour from now)
					Instant		now				= Instant.now();
					Date		expirationDate	= new Date(expiryDate);

					String		signedJwt		= Jwts.builder().claims(claims).subject(subject)															// The
																																							// principal
																																							// about
																																							// whom
																																							// the
																																							// JWT
																																							// is
																																							// issued
							.issuedAt(Date.from(now))																										// When
																																							// the
																																							// JWT
																																							// was
																																							// issued
							.expiration(expirationDate)																										// When
																																							// the
																																							// JWT
																																							// expires
							.signWith(key, Jwts.SIG.HS256)																									// Sign
																																							// with
																																							// the
																																							// key
																																							// and
																																							// algorithm
							.compact();																														// Compacts
																																							// the
																																							// JWT
																																							// into
																																							// its
																																							// final
																																							// string
																																							// form
					String		requestId		= "";
					var			encryptedToken	= signedJwt;
					if (jQuiverProperties.isEnableSecuredAuthentication() && signedJwt != null) {
						// Salt generation.............
						SaltDetails saltDetails = userConfigService.generateAndStoreSalt();
						requestId = saltDetails.getRequestId();

						var ecbMode = new ECBCipherMode("AES", "ECB", "PKCS5Padding", 128);
						encryptedToken = ecbMode.encrypt(signedJwt, saltDetails.getSalt(), "AES");
					}
					return new JwtRequestDetails(encryptedToken, requestId);
				}
			}
		} catch (Exception exec) {
			logger.error("Failed : Error while creating token " + exec.getMessage());
		}
		return null;

	}

	public Authentication getAuthentication(String token, String requestId,String uri) {
		try {
			Claims claims = extractAllClaims(token, requestId,uri);
			if (claims == null) {
				return null;
			}
			List<String>					authorities		= claims.get("authorities", List.class);
			List<SimpleGrantedAuthority>	userAuthorities	= (authorities != null)
					? authorities.stream().map(SimpleGrantedAuthority::new).toList()
					: Collections.emptyList();

			UserInformation					userInfo		= fetchUserInfoFromToken(token, requestId, uri);

			return new UsernamePasswordAuthenticationToken(userInfo, null, userAuthorities);
		} catch (Exception ex) {
			logger.error("Error while extracting authentication from token: " + ex.getMessage());
		}
		return null;
	}

	public UserInformation fetchUserInfoFromToken(String token, String requestId, String uri) {
		try {
			Claims claims = extractAllClaims(token, requestId, uri);
			if (claims == null) {
				return null;
			}

			String							username				= claims.getSubject();
			List<String>					authorities				= claims.get("authorities", List.class);
			String							fullName				= claims.get("fullName", String.class);
			String							userId					= claims.get("userId", String.class);
			Boolean							isDefaultPassword		= claims.get("isDefaultPassword", Boolean.class);
			Boolean							active					= claims.get("active", Boolean.class);
			boolean							isDefaultPasswordValue	= (isDefaultPassword != null) ? isDefaultPassword
					: false;
			boolean							activeValue				= (active != null) ? active : false;
			List<String>					roleList				= claims.get("roles", List.class);
			List<SimpleGrantedAuthority>	userAuthorities			= (authorities != null)
					? authorities.stream().map(SimpleGrantedAuthority::new).toList()
					: Collections.emptyList();

			UserInformation					userInfo				= new UserInformation(userId, username, fullName,
					userAuthorities, roleList, isDefaultPasswordValue, activeValue);

			return userInfo;
		} catch (Exception ex) {
			logger.error("Error while extracting authentication from token: " + ex.getMessage());
		}
		return null;
	}

	public Boolean validateToken(String token, UserDetails userDetails, String sri, String uri) {
		final String username = extractUsername(token, sri, uri);
		return (username.toLowerCase().equals(userDetails.getUsername().toLowerCase()) && !isTokenExpired(token, sri, uri));
	}

	public boolean validateToken(String token, String requestId, String uri) {
		try {
			extractAllClaims(token, requestId, uri); // throws if invalid
			return true;
		} catch (JwtException | IllegalArgumentException ex) {
			return false;
		}
	}

	public String extractTokenFromRequest(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("auth_token".equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	public String extractUserNameFromRequest(HttpServletRequest request) {
		String token = extractTokenFromRequest(request);
		if (token != null) {
			try {
				PropertyMaster	propertyMaster			= propertyMasterRepository
						.findByOwnerTypeAndOwnerIdAndPropertyName("system", "system", "enable-user-management");
				boolean			isAuthenticationEnabled	= Boolean.parseBoolean(propertyMaster.getPropertyValue());
				if (Boolean.TRUE == isAuthenticationEnabled) {
					String sri = extractJwtFromCookie(request, "r");
					return extractUsername(token, sri,request.getRequestURI() );
				}
			} catch (Exception e) {
				logger.error("Failed to extract user ID from token: " + e.getMessage());
			}
		}
		return null;
	}

	public String extractJwtFromCookie(HttpServletRequest request, String cookieName) {
		if (request.getCookies() == null)
			return null;
		for (Cookie cookie : request.getCookies()) {
			if (cookieName.equals(cookie.getName())) {
				return cookie.getValue();
			}
		}
		return null;
	}

	public JwtRequestDetails reEncryptToken(String oldToken, String oldRequestId) {
		try {
			// Decrypt Token
			String		decryptedToken	= null;
			SaltDetails	oldSaltDetails	= saltDetailsRepository.findByRequestId(oldRequestId);
			if (oldSaltDetails != null && oldToken != null) {
				try {
					ECBCipherMode ecbMode = new ECBCipherMode("AES", "ECB", "PKCS5Padding", 128);
					decryptedToken = ecbMode.decrypt(oldToken, oldSaltDetails.getSalt(), "AES");
				} catch (Exception e) {
					logger.info("Token decryption failed for requestId: " + oldRequestId + " expired or invalid.");
					return null;
				}
			}

			// Salt generation.............
			SaltDetails	saltDetails		= userConfigService.generateAndStoreSalt();
			String		requestId		= saltDetails.getRequestId();

			// Encrypt with new salt
			var			ecbMode			= new ECBCipherMode("AES", "ECB", "PKCS5Padding", 128);
			String		encryptedToken	= ecbMode.encrypt(decryptedToken, saltDetails.getSalt(), "AES");
			return new JwtRequestDetails(encryptedToken, requestId);
		} catch (Exception exec) {
			logger.error("Failed : Error while re encrypting token " + exec.getMessage());
		}

		return null;
	}

	// For Refresh Token
	public JwtRequestDetails refreshTokenIfExpiringSoon(String token, String requestId, String uri) {
		try {
			Claims claims = extractAllClaims(token, requestId, uri);
			if (claims == null)
				return null;

			Date	expiration	= claims.getExpiration();
			long	now			= System.currentTimeMillis();

			// Check if token is expiring in the next 5 minutes (5 * 60 * 1000 ms)
			if (expiration.getTime() - now <= 5 * 60 * 1000) {
				// Fetch user details from token
				UserInformation userInfo = fetchUserInfoFromToken(token, requestId,uri);
				if (userInfo != null) {
					return generateToken(userInfo);
				}
			}
		} catch (Exception e) {
			logger.error("Failed to refresh token: " + e.getMessage());
		}
		return null; // token not expiring soon or failed
	}
	// Done for Refresh Token
		
	public void createTokenCookie(HttpServletResponse response, JwtRequestDetails jwtRequestDetails) {
		Cookie cookie = new Cookie("auth_token", jwtRequestDetails.getToken());
		cookie.setSecure(false);
		cookie.setPath("/");
		cookie.setMaxAge((jQuiverProperties.getAuthTokenExpiryTime()) * 60);
		response.addCookie(cookie);

		Cookie saltCookie = new Cookie("r", jwtRequestDetails.getRequestId());
		saltCookie.setSecure(false);
		saltCookie.setPath("/");
		saltCookie.setMaxAge((jQuiverProperties.getAuthTokenExpiryTime()) * 60);
		response.addCookie(saltCookie);
	}

	public void clearAuthCookies(HttpServletResponse response) {
		Cookie authCookie = new Cookie("auth_token", null);
		authCookie.setPath("/");
		authCookie.setMaxAge(0);
		authCookie.setSecure(false);

		Cookie saltIdCookie = new Cookie("r", null);
		saltIdCookie.setPath("/");
		saltIdCookie.setMaxAge(0);
		saltIdCookie.setSecure(false);

		response.addCookie(authCookie);
		response.addCookie(saltIdCookie);
	}
	
	public void setSysLoginCookie(HttpServletResponse response) {
		Cookie forceLogin = new Cookie("SYS_LOGIN", "true");
		forceLogin.setPath("/");
		forceLogin.setHttpOnly(true);
		forceLogin.setMaxAge(5 * 60); // 5 minutes
		response.addCookie(forceLogin);
	}


}
