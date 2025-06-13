/**
 * 
 */
package com.trigyn.jws.usermanagement.security.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import com.trigyn.jws.dbutils.spi.PropertyMasterDetails;
import com.trigyn.jws.usermanagement.entities.JwsUser;
import com.trigyn.jws.usermanagement.exception.InvalidLoginException;
import com.trigyn.jws.usermanagement.repository.UserManagementDAO;
import com.trigyn.jws.usermanagement.service.JwsUserService;
import com.trigyn.jws.usermanagement.utils.Constants;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

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
			super.setDefaultFailureUrl("/cf/login");
			super.onAuthenticationFailure(request, response, exception);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}