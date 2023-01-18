package com.trigyn.jws.usermanagement.security.config;

import java.io.IOException;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.usermanagement.vo.MultiAuthSecurityDetailsVO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)

public class JwtRequestFilter extends OncePerRequestFilter {

	private final static Logger			logger						= LogManager.getLogger(JwtRequestFilter.class);
	@Autowired
	@Lazy
	private UserDetailsService			userDetailsService			= null;

	@Autowired
	@Lazy
	private JwtUtil						jwtUtil						= null;

	@Autowired
	private ApplicationSecurityDetails	applicationSecurityDetails	= null;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		Map<String, Object> authenticationDetails = applicationSecurityDetails.getAuthenticationDetails();

		try {
			if (authenticationDetails != null) {

				@SuppressWarnings("unchecked")
				List<MultiAuthSecurityDetailsVO> multiAuthLogiVos = (List<MultiAuthSecurityDetailsVO>) authenticationDetails
						.get("authenticationDetails");
				if (multiAuthLogiVos != null && multiAuthLogiVos.isEmpty() == false) {

					String requestUri = request.getRequestURI().substring(request.getContextPath().length());
					for (MultiAuthSecurityDetailsVO multiAuthLogin : multiAuthLogiVos) {
						Integer	authType			= multiAuthLogin.getAuthenticationTypeVO().getId();
						String	authorizationHeader	= request.getHeader("Authorization");
						String	authTypeHeader		= request.getHeader("at");
						String	username			= null;
						String	jwt					= null;
						if ((requestUri.startsWith("/japi/") && !requestUri.equals("/japi/error"))
								|| (authorizationHeader != null && authorizationHeader.startsWith("Bearer "))) {
							if (requestUri.startsWith("/japi/") && !requestUri.equals("/japi/error")) {
								if ((authType == Constants.AuthType.DAO.getAuthType()
										&& "enableDatabaseAuthentication".equals(
												multiAuthLogin.getConnectionDetailsVO().getAuthenticationType().getName())
										&& "true".equals(
												multiAuthLogin.getConnectionDetailsVO().getAuthenticationType().getValue())
										&& Constants.AuthTypeHeaderKey.DAO.getAuthTypeHeaderKey().equals(authTypeHeader.trim()))
										|| (authType == Constants.AuthType.LDAP.getAuthType() && "enableLdapAuthentication".equals(
												multiAuthLogin.getConnectionDetailsVO().getAuthenticationType().getName())
												&& Constants.AuthTypeHeaderKey.LDAP.getAuthTypeHeaderKey().equals(authTypeHeader.trim()))) {
									if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
										jwt			= authorizationHeader.substring(7);
										username	= jwtUtil.extractUsername(jwt);
									}
								} else if (authType == Constants.AuthType.OAUTH.getAuthType()
										&& "oauth-clients".equals(
												multiAuthLogin.getConnectionDetailsVO().getAuthenticationType().getName())
										&& "true".equals(
												multiAuthLogin.getConnectionDetailsVO().getAuthenticationType().getValue())
										&& Constants.AuthTypeHeaderKey.OAUTH.getAuthTypeHeaderKey().equals(authTypeHeader.trim())) {
									if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
										jwt			= authorizationHeader.substring(7);
										username	= retrieveUsernameFromJwtToken(jwt);
										if (username == null) {
											username = jwtUtil.extractUsername(jwt);
										}
									}
								}
							}
							if ((requestUri.startsWith("/japi/") && !requestUri.equals("/japi/error")
									&& username != null
									&& SecurityContextHolder.getContext().getAuthentication() == null)
									|| username != null) {

								UserDetails userDetails = userDetailsService.loadUserByUsername(username);

								if (((authType == Constants.AuthType.DAO.getAuthType()
										|| authType == Constants.AuthType.LDAP.getAuthType())
										&& jwtUtil.validateToken(jwt, userDetails))
										|| authType == Constants.AuthType.OAUTH.getAuthType()) {

									UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
											userDetails, null, userDetails.getAuthorities());
									usernamePasswordAuthenticationToken
											.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
									SecurityContextHolder.getContext()
											.setAuthentication(usernamePasswordAuthenticationToken);
									break;
								}
							}
						}
					}
				}

			}

			chain.doFilter(request, response);

		} catch (ExpiredJwtException expiredException) {
			logger.error(
					"Inside JwtRequestFilter - ExpiredJwtException - Error occurred while processing the request (Request URI: {}})",
					request.getRequestURI(), expiredException);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, expiredException.getMessage());
			return;
		} catch (SignatureException signatureException) {
			logger.error(
					"Inside JwtRequestFilter - SignatureException - Error occurred while processing the request (Request URI: {}})",
					request.getRequestURI(), signatureException);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, signatureException.getMessage());
			return;
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			logger.error(
					"Inside JwtRequestFilter - Throwable - Error occurred while processing the request (Request URI: {}})",
					request.getRequestURI(), throwable);
			if (throwable.getCause() instanceof AccessDeniedException) {
				response.sendError(HttpServletResponse.SC_FORBIDDEN,
						"You do not have enough privilege to access this module");
			} else {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, throwable.getMessage());
			}
			return;
		}
	}

	private String retrieveUsernameFromJwtToken(String token) throws Exception {
		try {
			DecodedJWT	jwt			= JWT.decode(token);

			JwkProvider	provider	= null;
			Jwk			jwk			= null;
			Algorithm	algorithm	= null;

			provider = new UrlJwkProvider(new URL("https://login.microsoftonline.com/common/discovery/keys"));
			System.out.println(jwt.getKeyId());
			jwk			= provider.get(jwt.getKeyId());
			algorithm	= Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);
			algorithm.verify(jwt);// if the token signature is invalid, the method will throw
									// SignatureVerificationException

			return jwt.getClaim("upn").asString();
		} catch (Exception a_exc) {
			logger.error("Error while retrieving user name from jwt token of OAUTH authentication", a_exc);
		}
		return null;
	}

	// private String retrieveUsernameFromOauthToken(String token) throws Exception
	// {
	// DecodedJWT decodedJWT = JWT.decode(token);
	// try {
	// String tenantID = "b04f6450-2e93-4532-8e83-fc394c677028";
	// // For Azure AD V2 token
	// String providerURLV2 = "https://login.microsoftonline.com/" + tenantID +
	// "/discovery/v2.0/keys";
	// String issuerV2 = "https://login.microsoftonline.com/" + tenantID + "/v2.0";
	//
	// JwkProvider provider = new JwkProviderBuilder(
	// new URL("https://login.microsoftonline.com/common/discovery/keys")).build();
	// Jwk jwk = provider.get(decodedJWT.getKeyId());
	// Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(),
	// null);
	// JWTVerifier verifier = JWT.require(algorithm).withIssuer(issuerV2).build();
	//
	// return verifier.verify(decodedJWT).getClaim("upn").asString();
	//
	// } catch (Exception a_exc) {
	// logger.error("Error while retrieving user name from jwt token of OAUTH
	// authentication", a_exc);
	// String appID = decodedJWT.getClaims().get("appid").asString();
	//
	// System.out.println("Header = " + decodedJWT.getHeader());
	// System.out.println("Algorithm = " + decodedJWT.getAlgorithm());
	// System.out.println("Audience = " + decodedJWT.getAudience());
	// decodedJWT.getClaims().forEach((k, v) -> {
	// System.out.println("Claim " + k + " = " + v.asString());
	// });
	// System.out.println("ContentType = " + decodedJWT.getContentType());
	// System.out.println("ExpiresAt = " + decodedJWT.getExpiresAt());
	// System.out.println("Id = " + decodedJWT.getId());
	// System.out.println("Issuer = " + decodedJWT.getIssuer());
	// System.out.println("Subject = " + decodedJWT.getSubject());
	//
	// if (appID != null && appID.equals(oAuthDetails.getOAuthClientId())
	// && decodedJWT.getExpiresAt().after(new Date())) {
	// return decodedJWT.getClaims().get("upn").asString();
	// }
	//
	// }
	// return null;
	// }

}
