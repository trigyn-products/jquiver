package com.trigyn.jws.usermanagement.security.config;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.usermanagement.entities.JwsUser;
import com.trigyn.jws.usermanagement.repository.UserManagementDAO;
import com.trigyn.jws.usermanagement.service.JwsUserService;
import com.trigyn.jws.usermanagement.service.UserConfigService;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.usermanagement.vo.JwsUserLoginVO;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Transactional
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {

	private final static Logger						logger				= LoggerFactory
			.getLogger(CustomAuthSuccessHandler.class);

	private static Set<LoginSuccessEventListener>	loginListener		= new HashSet<LoginSuccessEventListener>();

	@Autowired
	private JwsUserService							userService			= null;

	@Autowired
	private ServletContext							servletContext		= null;

	@Autowired
	private UserManagementDAO						userManagementDAO	= null;

	@Autowired
	private JdbcTemplate							jdbcTemplate		= null;

	@Autowired
	private UserConfigService						userConfigService	= null;

	@Autowired
	private SecurityContextRepository securityContextRepository			= null;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		UserInformation userInformation = (UserInformation) authentication.getPrincipal();
		logger.debug("Logged in successfully: " + userInformation.getUsername() + " at - " + new Date());
		HttpSession httpSession = request.getSession(false);
		logger.debug("Before: " + httpSession.getMaxInactiveInterval());
		httpSession.setMaxInactiveInterval(0);
		logger.debug("After: " + httpSession.getMaxInactiveInterval());
		boolean isLoggedOut = false;

		StringBuilder		defaultUrl		= new StringBuilder().append(servletContext.getContextPath()).append("/");
		String				redirectUrl		= defaultUrl.toString();
		SavedRequest	savedRequest	= (SavedRequest) httpSession
				.getAttribute("SPRING_SECURITY_SAVED_REQUEST");
		
		if (savedRequest != null) {
			if (savedRequest.getRedirectUrl().contains("/login") == false) {
				redirectUrl = savedRequest.getRedirectUrl();
			}else if (httpSession.getAttribute("CUSTOM_REDIRECT_URL") != null) {
				if (httpSession.getAttribute("CUSTOM_REDIRECT_URL").toString().contains("/login") == false) {
					redirectUrl = httpSession.getAttribute("CUSTOM_REDIRECT_URL").toString();
				}
			}
		} else if (httpSession.getAttribute("CUSTOM_REDIRECT_URL") != null) {
			if (httpSession.getAttribute("CUSTOM_REDIRECT_URL").toString().contains("/login") == false) {
				redirectUrl = httpSession.getAttribute("CUSTOM_REDIRECT_URL").toString();
			}
		}
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
				if (authType == null || authType.isEmpty()|| authType.isBlank()) {
					authType = request.getHeader("at");
					if (authType.equals(Constants.AuthTypeHeaderKey.DAO.getAuthTypeHeaderKey())) {
						authType = Constants.DAO_ID;
					} else if (authType.equals(Constants.AuthTypeHeaderKey.LDAP.getAuthTypeHeaderKey())) {
						authType = Constants.LDAP_ID;
					} else if (authType.equals(Constants.AuthTypeHeaderKey.OAUTH.getAuthTypeHeaderKey())) {
						authType = Constants.OAUTH_ID;
					} else if (authType.equals(Constants.AuthTypeHeaderKey.SAML.getAuthTypeHeaderKey())) {
						authType = Constants.SAML_ID;
					}
				}
				if (multiAuthLoginVO != null && authType !=null && authType.equals("2")) {
					for (JwsUserLoginVO jwsUserLoginVO : multiAuthLoginVO) {
						Integer	authTypeId = jwsUserLoginVO.getAuthenticationType().intValue();
						if(authTypeId == Constants.AuthType.DAO.getAuthType() && authentication instanceof UsernamePasswordAuthenticationToken) {
							Map<String, Object> daoAuthAttributes = jwsUserLoginVO.getLoginAttributes();
							if (daoAuthAttributes != null && daoAuthAttributes.containsKey("enableVerificationStep")) {
								String enableVerificationStepValue = (String) daoAuthAttributes
										.get("enableVerificationStep");
								if (enableVerificationStepValue.equalsIgnoreCase("true")) {
									String verficationType = (String) daoAuthAttributes.get("verificationType");
									if (verficationType != null && Constants.VerificationType.PASSWORD.getVerificationType()
											.equals(verficationType)) {

										String passwordExpiry = (String) daoAuthAttributes.get("passwordExpiry");
										if (passwordExpiry != null) {
											Integer expiryDays = Integer.parseInt(passwordExpiry);
											if (expiryDays != 0) {
												LocalDate	currentDate	= LocalDate.now();
												Calendar	cal			= Calendar.getInstance();
												cal.setTime(user.getLastPasswordUpdatedDate());
												LocalDate lastUpdatedDate = user.getLastPasswordUpdatedDate().toInstant()
														.atZone(ZoneId.systemDefault()).toLocalDate();
												if (user.getForcePasswordChange() == 1
														|| currentDate.isAfter(lastUpdatedDate.plusDays(expiryDays))) {
													logout(request, response, authentication, user);
													isLoggedOut = true;
													authentication.setAuthenticated(false);
													redirectUrl = new StringBuilder()
															.append(servletContext.getContextPath())
															.append("/cf/changePassword?token=").append(user.getUserId())
															.append("&icp=1").toString();
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
			logger.debug("Error while redirecting to change password " + userInformation.getUsername() + " at - "
					+ new Date());
		}
		for (LoginSuccessEventListener logInSuccessEventListner : loginListener) {
			logInSuccessEventListner.onLogin(userInformation);
			HttpSession					session		= request.getSession();
			if(session.getAttribute("loginCaptcha")!=null) {
				session.removeAttribute("loginCaptcha");
			}
		}
		
		if(isLoggedOut == false) {
			SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
		        .getContextHolderStrategy();

		    SecurityContext context = securityContextHolderStrategy.createEmptyContext();
		    context.setAuthentication(authentication);
		    securityContextHolderStrategy.setContext(context);

		    securityContextRepository.saveContext(context, request, response);
		}
		response.sendRedirect(redirectUrl);
	}

	private void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication,
			JwsUser user) throws ServletException {
		if (user != null) {
			request.logout();
			HttpSession session = request.getSession(false);
			if (session != null) {
				// logger.debug("Invalidating session: " + session.getId());
				session.invalidate();
			}
			
			SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
			logoutHandler.setInvalidateHttpSession(true);
			logoutHandler.logout(request, response, authentication);
			SecurityContext context = SecurityContextHolder.getContext();
			context.setAuthentication(null);
			SecurityContextHolder.clearContext();

			session = request.getSession(true);
			session.setMaxInactiveInterval(-1);
			session.invalidate();
			authentication.setAuthenticated(false);

			JdbcTokenRepositoryImpl jdbcTokenRepositoryImpl = new JdbcTokenRepositoryImpl();
			jdbcTokenRepositoryImpl.setJdbcTemplate(jdbcTemplate);
			jdbcTokenRepositoryImpl.removeUserTokens(user.getEmail());
		}
	}

	public static void addLoginListener(LoginSuccessEventListener eventListener) {
		loginListener.add(eventListener);
	}

	public static void removeLoginListener(LoginSuccessEventListener eventListner) {
		loginListener.remove(eventListner);
	}

}
