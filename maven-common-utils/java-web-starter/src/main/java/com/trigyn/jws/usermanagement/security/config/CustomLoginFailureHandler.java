/**
 * 
 */
package com.trigyn.jws.usermanagement.security.config;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import com.trigyn.jws.dbutils.spi.PropertyMasterDetails;
import com.trigyn.jws.usermanagement.entities.JwsUser;
import com.trigyn.jws.usermanagement.exception.InvalidLoginException;
import com.trigyn.jws.usermanagement.repository.UserManagementDAO;
import com.trigyn.jws.usermanagement.service.JwsUserService;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.webstarter.controller.JwsUserRegistrationController;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	
	private final static Logger		logger					= LoggerFactory
			.getLogger(JwsUserRegistrationController.class);

	@Autowired
	private PropertyMasterDetails	propertyMasterDetails	= null;

	@Autowired
	private JwsUserService			userService				= null;

	@Autowired
	private UserManagementDAO		userManagementDAO		= null;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		try {
			String	emailID	= request.getParameter("email");
			JwsUser	user	= userService.findUserByEmail(emailID);
			String	authType	= request.getParameter("enableAuthenticationType");
			String authTypeHeader = request.getHeader("at");
			authType = Constants.getAuthType(authType, authTypeHeader);
			Integer	maxFailedCount;
			maxFailedCount = Integer.parseInt(propertyMasterDetails.getSystemPropertyValue("maxFailedCount"));

			if (user != null) {
				if (user.getIsActive() == 1) {
					if (user.getFailedAttempt() <= maxFailedCount) {
						user.setFailedAttempt(user.getFailedAttempt() + 1);
						if (user.getFailedAttempt() == maxFailedCount) {
							user.setIsActive(0);
						}
						userManagementDAO.updateUserData(user);
						if (user.getFailedAttempt() == maxFailedCount) {
							exception = new InvalidLoginException("Account Locked. Please contact Admin.", emailID, authType);
						} else {
							String msg = exception.getMessage();
							if (msg.startsWith("?")) {
								msg = "Login failed. Bad credentials.";
							}
							exception = new InvalidLoginException(msg, emailID, authType);
						}
					} else {
						exception = new InvalidLoginException("Account Locked. Please contact Admin.", emailID, authType);
					}
				} else {
					exception = new InvalidLoginException("Account Locked. Please contact Admin.", emailID, authType);
				}
			} else {
				exception = new InvalidLoginException("User does not exist.", emailID, authType);
			}
			// Set cookies instead of using session attributes
			addCookie(response, "loginErrorMessage", exception.getMessage());
			addCookie(response, "previousMail", emailID);
			addCookie(response, "prevAuthType", authType);
			addCookie(response, "error", "true");
			
			// System login flag
			Cookie systemLogin = new Cookie("SYS_LOGIN", "true");
			systemLogin.setPath("/");
			systemLogin.setHttpOnly(true);
			systemLogin.setMaxAge(5 * 60); // 5 minute
			response.addCookie(systemLogin);
			
			// Redirect to login with query param to indicate error
			response.sendRedirect(request.getContextPath() + "/cf/login");
		} catch (Exception e) {
			logger.error("Error occured in CustomLoginFailureHandler.", e.getMessage());
		}
	}
	
	private void addCookie(HttpServletResponse response, String name, String value) {
		if (value != null) {
			Cookie cookie = new Cookie(name, URLEncoder.encode(value, StandardCharsets.UTF_8));
			cookie.setPath("/");
			cookie.setHttpOnly(false); // Set to true if you dont need JavaScript to access it
			cookie.setMaxAge(60); // expires in 60 seconds
			response.addCookie(cookie);
		}
	}


}