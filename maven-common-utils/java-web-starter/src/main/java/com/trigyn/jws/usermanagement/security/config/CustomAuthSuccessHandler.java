package com.trigyn.jws.usermanagement.security.config;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jakarta.servlet.http.Cookie;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.saml2.provider.service.authentication.Saml2Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.transaction.annotation.Transactional;
import com.trigyn.jws.usermanagement.security.config.JwtUtil;
import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dbutils.spi.PropertyMasterDetails;
import com.trigyn.jws.usermanagement.entities.JwsUser;
import com.trigyn.jws.usermanagement.exception.InvalidLoginException;
import com.trigyn.jws.usermanagement.repository.UserManagementDAO;
import com.trigyn.jws.usermanagement.service.JwsUserService;
import com.trigyn.jws.usermanagement.service.UserConfigService;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.usermanagement.vo.JwsUserLoginVO;
import com.trigyn.jws.usermanagement.vo.JwtRequestDetails;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Transactional
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {

	private final static Logger						logger						= LoggerFactory
			.getLogger(CustomAuthSuccessHandler.class);

	private static Set<LoginSuccessEventListener>	loginListener				= new HashSet<LoginSuccessEventListener>();

	@Autowired
	private JwsUserService							userService					= null;

	@Autowired
	private ServletContext							servletContext				= null;

	@Autowired
	private UserManagementDAO						userManagementDAO			= null;

	@Autowired
	private UserConfigService						userConfigService			= null;

	@Autowired
	private SecurityContextRepository				securityContextRepository	= null;

	@Autowired
	@Lazy
	private JwtUtil									jwtUtil 					= null;

	@Autowired
	private PropertyMasterDetails 					propertyMasterDetails 		= null;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		Object			principal		= authentication.getPrincipal();
		UserInformation	userInformation	= null;
		if (principal instanceof String) {
			// Handle anonymous or unexpected case
			throw new InvalidLoginException("Something went wrong. Please login again.");
		} else if (principal instanceof UserInformation) {
			userInformation = (UserInformation) principal;
			logger.debug("Logged in successfully: " + userInformation.getUsername() + " at - " + new Date());
			boolean	isLoggedOut	= false;
			String	redirectUrl	= getRedirectUrl(request);
			clearCookieAndRedirect(request, response, redirectUrl);
			JwsUser user = userService.findUserByEmail(userInformation.getUsername());
			user.setFailedAttempt(0);
			userManagementDAO.updateUserData(user);
			try {
				Map<String, Object> authenticationDetails = new HashMap<>();
				userConfigService.getConfigurableDetails(authenticationDetails);
				if (authenticationDetails != null) {
					@SuppressWarnings("unchecked")
					List<JwsUserLoginVO>	multiAuthLoginVO	= (List<JwsUserLoginVO>) authenticationDetails
							.get("activeAutenticationDetails");
					String					authType			= request.getParameter("enableAuthenticationType");
					if (authType == null || authType.isEmpty() || authType.isBlank()) {
						authType = request.getHeader("at");
						if (Constants.AuthTypeHeaderKey.DAO.getAuthTypeHeaderKey().equals(authType)) {
							authType = Constants.DAO_ID;
						} else if (Constants.AuthTypeHeaderKey.LDAP.getAuthTypeHeaderKey().equals(authType)) {
							authType = Constants.LDAP_ID;
						} else if (Constants.AuthTypeHeaderKey.OAUTH.getAuthTypeHeaderKey().equals(authType)) {
							authType = Constants.OAUTH_ID;
						} else if (Constants.AuthTypeHeaderKey.SAML.getAuthTypeHeaderKey().equals(authType)) {
							authType = Constants.SAML_ID;
						}
					}

					if (authType == null || authType.isBlank()) {
						if (authentication instanceof Saml2Authentication) {
							authType = Constants.SAML_ID; // Or use .getAuthType() if needed
							logger.debug("Resolved authType as SAML from Authentication instance.");
						}
					}

					if (multiAuthLoginVO != null && authType != null && authType.equals("2")) {
						for (JwsUserLoginVO jwsUserLoginVO : multiAuthLoginVO) {
							Integer authTypeId = jwsUserLoginVO.getAuthenticationType().intValue();
							if (authTypeId == Constants.AuthType.DAO.getAuthType()
									&& authentication instanceof UsernamePasswordAuthenticationToken) {
								Map<String, Object> daoAuthAttributes = jwsUserLoginVO.getLoginAttributes();
								if (daoAuthAttributes != null
										&& daoAuthAttributes.containsKey("enableVerificationStep")) {
									String enableVerificationStepValue = (String) daoAuthAttributes
											.get("enableVerificationStep");
									if (enableVerificationStepValue.equalsIgnoreCase("true")) {
										String verficationType = (String) daoAuthAttributes.get("verificationType");
										if (verficationType != null && Constants.VerificationType.PASSWORD
												.getVerificationType().equals(verficationType)) {

											String passwordExpiry = (String) daoAuthAttributes.get("passwordExpiry");
											if (passwordExpiry != null) {
												Integer expiryDays = Integer.parseInt(passwordExpiry);
												if (expiryDays != 0) {
													LocalDate	currentDate	= LocalDate.now();
													Calendar	cal			= Calendar.getInstance();
													cal.setTime(user.getLastPasswordUpdatedDate());
													LocalDate lastUpdatedDate = user.getLastPasswordUpdatedDate()
															.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
													if (user.getForcePasswordChange() == 1 || currentDate
															.isAfter(lastUpdatedDate.plusDays(expiryDays))) {
														// logout(request, response, authentication, user);
														logout(request, response, authentication, user);
														isLoggedOut = true;
														authentication.setAuthenticated(false);
														redirectUrl = new StringBuilder()
																.append(servletContext.getContextPath())
																.append("/cf/changePassword?token=")
																.append(user.getUserId()).append("&icp=1").toString();
													}
												}
											}
										} else if (verficationType != null && Constants.VerificationType.OTP
												.getVerificationType().equals(verficationType)) {
											if (user != null) {
												user.setOneTimePassword(null);
												user.setOtpRequestedTime(null);
												userManagementDAO.updateUserData(user);
											}
										}

									}
								}
							}

						}
					}
				}
			} catch (Exception e) {
				logger.error("Error while redirecting to change password " + userInformation.getUsername() + " at - "
						+ new Date(), e);
			}
			for (LoginSuccessEventListener logInSuccessEventListner : loginListener) {
				logInSuccessEventListner.onLogin(userInformation);
			}

			JwtRequestDetails jwtRequestDetails = null;

			if (isLoggedOut == false) {
				SecurityContextHolderStrategy	securityContextHolderStrategy	= SecurityContextHolder
						.getContextHolderStrategy();

				SecurityContext					context							= securityContextHolderStrategy
						.createEmptyContext();
				context.setAuthentication(authentication);
				securityContextHolderStrategy.setContext(context);

				securityContextRepository.saveContext(context, request, response);

				// Generate JWT & store in cookie
				UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
				jwtRequestDetails = jwtUtil.generateToken(userPrincipal);

				jwtUtil.createTokenCookie(response, jwtRequestDetails);
			}
			request.getSession().removeAttribute("requestUriSaved");
			response.sendRedirect(redirectUrl);
		}
	}
	
	private void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication,
			JwsUser user) {
		if (user != null) {
			SecurityContextHolder.clearContext();
			jwtUtil.clearAuthCookies(response);
		}
	}

	public static void addLoginListener(LoginSuccessEventListener eventListener) {
		loginListener.add(eventListener);
	}

	public static void removeLoginListener(LoginSuccessEventListener eventListner) {
		loginListener.remove(eventListner);
	}

	private String getRedirectUrl(HttpServletRequest request) {
		String defaultUrl = servletContext.getContextPath() + "/";
		String baseUrl = propertyMasterDetails.getSystemPropertyValue("base-url");
		if(baseUrl.endsWith("/") == false) {
			baseUrl = baseUrl.concat("/");
		}
		
		boolean sysLogin = false;		
		try {
			if (request.getCookies() != null) {
				for (Cookie cookie : request.getCookies()) {
					if ("SYS_LOGIN".equals(cookie.getName())) {
						sysLogin = Boolean.parseBoolean(cookie.getValue());
					}
				}
			}
			
			if (sysLogin && request.getCookies() != null) {
				for (Cookie cookie : request.getCookies()) {
					if ("SAVED_REQUEST_URL".equals(cookie.getName())) {
						String	decodedUrl	= URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8.name());
						// Clear the cookie so it's not reused accidentally
						Cookie	clearCookie	= new Cookie("SAVED_REQUEST_URL", null);
						clearCookie.setPath("/");
						clearCookie.setMaxAge(0);
						return decodedUrl;
					}
				}
			}
		} catch (Exception e) {
			logger.error("Error occured in getRedirectUrl.", e.getMessage());
			e.printStackTrace();
		}

		// Check for redirect URL in cookie
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if ("CUSTOM_REDIRECT_URL".equals(cookie.getName())) {
					try {
						return URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8.name());
					} catch (UnsupportedEncodingException e) {
						logger.error("Failed to decode redirect URL cookie", e);
					}
				}
			}
		}

		// Check for redirect URL in request parameter
		String redirectUrl = request.getParameter("redirect_uri");
		// Fallback to referer
		if (redirectUrl == null || redirectUrl.contains("/login") || redirectUrl.contains("/changePassword")) {
			String referer = request.getHeader("Referer");
			redirectUrl = (referer != null && !baseUrl.equals(referer) && !referer.contains("/login") 
					&& !referer.contains("/changePassword")) ? referer : defaultUrl;
		}
		return redirectUrl;
	}
	
	private void clearCookieAndRedirect(HttpServletRequest request, HttpServletResponse response, String redirectUrl)
			throws IOException {
		Cookie clearCookie = new Cookie("SAVED_REQUEST_URL", null);
		clearCookie.setPath(request.getContextPath().isEmpty() ? "/" : request.getContextPath());
		clearCookie.setMaxAge(0);
		clearCookie.setHttpOnly(true);
		response.addCookie(clearCookie);
		
		String uri = request.getRequestURI();
		if (uri != null && uri.contains("/login")) {
		Cookie clear = new Cookie("SYS_LOGIN", null);
		clear.setPath("/");
		clear.setMaxAge(0);
		response.addCookie(clear);
		}
	}


}
