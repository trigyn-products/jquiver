package com.trigyn.jws.usermanagement.security.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.usermanagement.entities.JwsUser;
import com.trigyn.jws.usermanagement.repository.UserManagementDAO;
import com.trigyn.jws.usermanagement.service.JwsUserService;
import com.trigyn.jws.usermanagement.utils.Constants;

public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {

	
	@Autowired
	private JwsUserService jwsUserService = null;
	
	@Autowired
	private ApplicationSecurityDetails applicationSecurityDetails = null;
	
	@Autowired
	private UserManagementDAO userManagementDAO = null;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		System.out.println("Logged in successfully");
		UserInformation userInformation = (UserInformation) authentication.getPrincipal();
		
		
		
		response.sendRedirect("/cf/home");
	}

}
