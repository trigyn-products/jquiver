package com.trigyn.jws.usermanagement.security.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.saml2.provider.service.authentication.Saml2Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.trigyn.jws.usermanagement.service.UserConfigService;
import com.trigyn.jws.usermanagement.utils.Constants;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;

public class CustomLogoutSuccessHandler implements LogoutHandler {

	private final static Logger						logger				= LoggerFactory
			.getLogger(CustomLogoutSuccessHandler.class);

	@Autowired
	private UserConfigService						userConfigService	= null;

	private static Set<LogoutSuccessEventListener>	logoutListener		= new HashSet<LogoutSuccessEventListener>();

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		UserInformation userInformation = null;
		if (authentication != null) {
			userInformation = (UserInformation) authentication.getPrincipal();
			for (LogoutSuccessEventListener logoutSuccessEventListner : logoutListener) {
				logoutSuccessEventListner.onLogout(userInformation);
			}
		}
		if (request.getAttribute("logoutHandlerExecuted") != null) {
			return;
		}
		request.setAttribute("logoutHandlerExecuted", true);
		Map<String, Object> mapDetails = new HashMap<>();
		try {
			userConfigService.getConfigurableDetails(mapDetails);
			String	logOutUrl	= null;
			String	baseUrl		= null;
			String	signOutUrl	= null;

			if (authentication instanceof OAuth2AuthenticationToken) {
				OAuth2AuthenticationToken	oauthToken				= (OAuth2AuthenticationToken) authentication;
				String						clientRegistrationId	= oauthToken.getAuthorizedClientRegistrationId();
				if (Constants.OFFICE365.equals(clientRegistrationId)) {
					logOutUrl	= mapDetails.get("Office365LogOutURL").toString();
					baseUrl		= mapDetails.get("Office365BaseURL").toString();
					signOutUrl	= logOutUrl + "?" + Constants.POSTLOGOUTREDIRECTURI + "=" + baseUrl;
				}
			} else if (authentication instanceof Saml2Authentication) {
				logOutUrl	= mapDetails.get("SAMLLogOutURL").toString();
				baseUrl		= mapDetails.get("SAMLBaseURL").toString();
				request.logout();
				signOutUrl = logOutUrl + "?" + Constants.FROMURI + "=" + baseUrl;
			} else {
				// CLEAR auth_token and JSESSIONID for non-OAuth/SAML logout
				clearAuthCookies(response);
				request.logout();
			}

			if (signOutUrl != null) {
				clearAuthCookies(response);
				response.sendRedirect(signOutUrl);
			} else {
				request.logout();
			}
		} catch (Exception exc) {
			logger.error("Error occurred.", exc);
		}
	}

	public static void addLoginListener(LogoutSuccessEventListener eventListener) {
		logoutListener.add(eventListener);
	}

	public static void removeLoginListener(LogoutSuccessEventListener eventListner) {
		logoutListener.remove(eventListner);
	}

	private void clearAuthCookies(HttpServletResponse response) {
		// Clear JWT cookie
		Cookie authToken = new Cookie("auth_token", null);
		authToken.setPath("/");
		authToken.setMaxAge(0);
		response.addCookie(authToken);
		
		//Clear salt cookie
		Cookie saltCookie = new Cookie("r", null);
		saltCookie.setPath("/");
		saltCookie.setMaxAge(0);
		response.addCookie(saltCookie);

		// Clear session cookie
		Cookie sessionCookie = new Cookie("JSESSIONID", null);
		sessionCookie.setPath("/");
		sessionCookie.setMaxAge(0);
		response.addCookie(sessionCookie);
		
		// Clear saved request url cookie
		Cookie saveRequestCookie = new Cookie("SAVED_REQUEST_URL", null);
		saveRequestCookie.setPath("/");
		saveRequestCookie.setMaxAge(0);
		response.addCookie(saveRequestCookie);
		
		// Clear sys login cookie
		Cookie sysLogin = new Cookie("SYS_LOGIN", null);
		sysLogin.setPath("/");
		sysLogin.setMaxAge(0);
		response.addCookie(sysLogin);
	}
}
