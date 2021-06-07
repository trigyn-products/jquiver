package com.trigyn.jws.usermanagement.security.config;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

public class CustomLogoutSuccessHandler implements LogoutHandler {

	private static Set<LogoutSuccessEventListener> logoutListener = new HashSet<LogoutSuccessEventListener>();

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		System.out.println("Logged out successfully");
		UserInformation userInformation = (UserInformation) authentication.getPrincipal();

		for (LogoutSuccessEventListener logoutSuccessEventListner : logoutListener) {
			logoutSuccessEventListner.onLogout(userInformation);
		}
	}

	public static void addLoginListener(LogoutSuccessEventListener eventListener) {
		logoutListener.add(eventListener);
	}

	public static void removeLoginListener(LogoutSuccessEventListener eventListner) {
		logoutListener.remove(eventListner);
	}

}
