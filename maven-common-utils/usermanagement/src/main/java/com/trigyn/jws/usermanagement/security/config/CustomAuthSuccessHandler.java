package com.trigyn.jws.usermanagement.security.config;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.usermanagement.entities.JwsAuthenticationType;
import com.trigyn.jws.usermanagement.entities.JwsUser;
import com.trigyn.jws.usermanagement.repository.JwsAuthenticationTypeRepository;
import com.trigyn.jws.usermanagement.repository.UserManagementDAO;
import com.trigyn.jws.usermanagement.service.JwsUserService;
import com.trigyn.jws.usermanagement.service.UserConfigService;

@Transactional
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {

	private final static Logger						logger							= LogManager.getLogger(CustomAuthSuccessHandler.class);

	private static Set<LoginSuccessEventListener>	loginListener					= new HashSet<LoginSuccessEventListener>();

	@Autowired
	private JwsUserService							userService						= null;

	@Autowired
	private ServletContext							servletContext					= null;

	@Autowired
	private UserManagementDAO						userManagementDAO				= null;

	@Autowired
	private ApplicationSecurityDetails				applicationSecurityDetails		= null;

	@Autowired
	private UserConfigService						userConfigService				= null;

	@Autowired
	private JwsAuthenticationTypeRepository			authenticationTypeRepository	= null;

	@Autowired
	private DataSource								dataSource						= null;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		UserInformation userInformation = (UserInformation) authentication.getPrincipal();

		logger.debug("Logged in successfully: " + userInformation.getUsername() + " at - " + new Date());
		HttpSession httpSession = request.getSession(false);
		logger.debug("Before: " + httpSession.getMaxInactiveInterval());
		httpSession.setMaxInactiveInterval(0);
		logger.debug("After: " + httpSession.getMaxInactiveInterval());

		StringBuilder	defaultUrl		= new StringBuilder().append(servletContext.getContextPath()).append("/");
		String			redirectUrl		= defaultUrl.toString();
		SavedRequest	savedRequest	= (SavedRequest) httpSession.getAttribute("SPRING_SECURITY_SAVED_REQUEST");
		if (savedRequest != null) {
			if (savedRequest.getRedirectUrl().contains("/login") == false) {
				redirectUrl = savedRequest.getRedirectUrl();
			}
		}
		JwsUser user = userService.findUserByEmail(userInformation.getUsername());
		user.setFailedAttempt(0);
		userManagementDAO.updateUserData(user);

		for (LoginSuccessEventListener logInSuccessEventListner : loginListener) {
			logInSuccessEventListner.onLogin(userInformation);
		}

		if (user.getForcePasswordChange() == 1) {
			logout(request, response, authentication, user);
			redirectUrl = new StringBuilder().append(servletContext.getContextPath()).append("/cf/changePassword?token=")
					.append(user.getUserId()).toString();
		} else {
			try {
				if (applicationSecurityDetails.getAuthenticationType() != null) {
					JSONObject				jsonObjectVerificationType	= null;
					JSONObject				jsonObjectCaptcha			= null;

					Integer					authType					= Integer
							.parseInt(applicationSecurityDetails.getAuthenticationType());
					JwsAuthenticationType	authenticationType;

					authenticationType = authenticationTypeRepository.findById(authType)
							.orElseThrow(() -> new Exception("No auth type found with id : " + authType));

					if (StringUtils.isNotBlank(authenticationType.getAuthenticationProperties())) {
						JSONArray	jsonArray						= new JSONArray(authenticationType.getAuthenticationProperties());
						String		verificationStepPropertyName	= "enableVerificationStep";

						jsonObjectVerificationType = userConfigService.getJsonObjectFromPropertyValue(jsonObjectVerificationType, jsonArray,
								verificationStepPropertyName);
						if (jsonObjectVerificationType != null && jsonObjectVerificationType.getString("value").equalsIgnoreCase("true")) {
							if (jsonObjectVerificationType.get("selectedValue") != null
									&& Integer.parseInt(jsonObjectVerificationType.get("selectedValue").toString()) != 2) {

								jsonObjectCaptcha = userConfigService.getJsonObjectFromPropertyValue(jsonObjectVerificationType, jsonArray,
										"enablePasswordExpiry");
								Integer		expiryDays	= Integer.parseInt(jsonObjectCaptcha.get("selectedValue").toString());
								LocalDate	currentDate	= LocalDate.now();
								Calendar	cal			= Calendar.getInstance();
								cal.setTime(user.getLastPasswordUpdatedDate());
								LocalDate lastUpdatedDate = user.getLastPasswordUpdatedDate().toInstant().atZone(ZoneId.systemDefault())
										.toLocalDate();
								;

								if (currentDate.isAfter(lastUpdatedDate.plusDays(expiryDays))) {
									logout(request, response, authentication, user);
									redirectUrl = new StringBuilder().append(servletContext.getContextPath())
											.append("/cf/changePassword?token=").append(user.getUserId()).append("&icp=1").toString();
								}
							}
						}
					}
				}
			} catch (Exception e) {
				logger.debug("Error while redirecting to change password " + userInformation.getUsername() + " at - " + new Date());
			}
		}
		response.sendRedirect(redirectUrl);
	}

	private void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication, JwsUser user) {
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
