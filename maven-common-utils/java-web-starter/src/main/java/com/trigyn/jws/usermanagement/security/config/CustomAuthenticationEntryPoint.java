package com.trigyn.jws.usermanagement.security.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/*
 * Custom AuthenticationEntryPoint implementation used to handle requests
 * from unauthenticated users trying to access secured endpoints.
 *
 * Spring Security provides a default implementation, but this class
 * customizes the behavior by adding a cookie used for login redirection.
 */
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request,
	                     HttpServletResponse response,
	                     AuthenticationException authException) throws IOException {

	    String requestUri = request.getRequestURI();
	    String contextPath = request.getContextPath();

	    // Do not mark system login if user directly opens login page
	    if (!requestUri.equals(contextPath + "/cf/login")) {

	        Cookie sysLogin = new Cookie("SYS_LOGIN", "true");
	        sysLogin.setHttpOnly(true);
	        sysLogin.setPath("/");
	        sysLogin.setMaxAge(5 * 60); //5 minuts
	        response.addCookie(sysLogin);
	    }

	    response.sendRedirect(contextPath + "/cf/login");
	}
}
