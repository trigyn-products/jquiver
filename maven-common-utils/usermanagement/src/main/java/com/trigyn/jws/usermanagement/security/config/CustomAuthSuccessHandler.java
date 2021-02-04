package com.trigyn.jws.usermanagement.security.config;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {

	private static Set<LoginSuccessEventListener>	loginListener				= new HashSet<LoginSuccessEventListener>();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		System.out.println("Logged in successfully");
		UserInformation userInformation = (UserInformation) authentication.getPrincipal();

		for (LoginSuccessEventListener logInSuccessEventListner : loginListener) {
			logInSuccessEventListner.onLogin(userInformation);
		}

		response.sendRedirect("/cf/home");
	}

	public static void addLoginListener(LoginSuccessEventListener eventListener) {
		loginListener.add(eventListener);
	}

	public static void removeLoginListener(LoginSuccessEventListener eventListner) {
		loginListener.remove(eventListner);
	}

}
