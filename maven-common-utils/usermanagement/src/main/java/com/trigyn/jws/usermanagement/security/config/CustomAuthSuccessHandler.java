package com.trigyn.jws.usermanagement.security.config;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.dbutils.spi.PropertyMasterDetails;
import com.trigyn.jws.usermanagement.entities.JwsUser;
import com.trigyn.jws.usermanagement.repository.UserManagementDAO;
import com.trigyn.jws.usermanagement.service.JwsUserService;

@Transactional
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {

	private static Set<LoginSuccessEventListener>	loginListener			= new HashSet<LoginSuccessEventListener>();

	@Autowired
	private PropertyMasterDetails					propertyMasterDetails	= null;

	@Autowired
	private ServletContext							servletContext			= null;

	@Autowired
	private JwsUserService			userService						= null;

	@Autowired
	private UserManagementDAO								userManagementDAO					= null;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		UserInformation userInformation = (UserInformation) authentication.getPrincipal();

		System.out.println("Logged in successfully: " + userInformation.getUsername() + " at - " + new Date());
		HttpSession httpSession = request.getSession(false);
		System.out.println("Before: " + httpSession.getMaxInactiveInterval());
		httpSession.setMaxInactiveInterval(0);
		System.out.println("After: " + httpSession.getMaxInactiveInterval());

		StringBuilder	defaultUrl		= new StringBuilder().append(servletContext.getContextPath())
				.append(propertyMasterDetails.getSystemPropertyValue("default-redirect-url"));
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

		response.sendRedirect(redirectUrl);
	}

	public static void addLoginListener(LoginSuccessEventListener eventListener) {
		loginListener.add(eventListener);
	}

	public static void removeLoginListener(LoginSuccessEventListener eventListner) {
		loginListener.remove(eventListner);
	}

}
