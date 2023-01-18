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

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.usermanagement.entities.JwsUser;
import com.trigyn.jws.usermanagement.repository.UserManagementDAO;
import com.trigyn.jws.usermanagement.service.JwsUserService;
import com.trigyn.jws.usermanagement.service.UserConfigService;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.usermanagement.vo.JwsUserLoginVO;

@Transactional
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {

	private final static Logger						logger				= LogManager
			.getLogger(CustomAuthSuccessHandler.class);

	private static Set<LoginSuccessEventListener>	loginListener		= new HashSet<LoginSuccessEventListener>();

	@Autowired
	private JwsUserService							userService			= null;

	@Autowired
	private ServletContext							servletContext		= null;

	@Autowired
	private UserManagementDAO						userManagementDAO	= null;

	@Autowired
	private DataSource								dataSource			= null;

	@Autowired
	private UserConfigService						userConfigService	= null;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		UserInformation userInformation = (UserInformation) authentication.getPrincipal();
		logger.debug("Logged in successfully: " + userInformation.getUsername() + " at - " + new Date());
		HttpSession httpSession = request.getSession(false);
		logger.debug("Before: " + httpSession.getMaxInactiveInterval());
		httpSession.setMaxInactiveInterval(0);
		logger.debug("After: " + httpSession.getMaxInactiveInterval());

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
				if (multiAuthLoginVO != null && authType.equals("2")) {
					for (JwsUserLoginVO jwsUserLoginVO : multiAuthLoginVO) {
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
		} catch (Exception e) {
			logger.debug("Error while redirecting to change password " + userInformation.getUsername() + " at - "
					+ new Date());
		}
		for (LoginSuccessEventListener logInSuccessEventListner : loginListener) {
			logInSuccessEventListner.onLogin(userInformation);
		}
		response.sendRedirect(redirectUrl);
	}

	private void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication,
			JwsUser user) {
		if (user != null) {
			new SecurityContextLogoutHandler().logout(request, response, authentication);
			HttpSession session = request.getSession(false);
			if (session != null) {
				// logger.debug("Invalidating session: " + session.getId());
				session.invalidate();
			}
			SecurityContext context = SecurityContextHolder.getContext();
			context.setAuthentication(null);
			SecurityContextHolder.clearContext();

			session = request.getSession(true);
			session.setMaxInactiveInterval(-1);
			authentication.setAuthenticated(false);

			JdbcTokenRepositoryImpl jdbcTokenRepositoryImpl = new JdbcTokenRepositoryImpl();
			jdbcTokenRepositoryImpl.setDataSource(dataSource);
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
