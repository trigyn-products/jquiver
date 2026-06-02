package com.trigyn.jws.usermanagement.security.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.security.web.context.HttpRequestResponseHolder;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JwtSecurityContextRepository implements SecurityContextRepository {

	private final JwtUtil jwtUtil;

	@Autowired
	public JwtSecurityContextRepository(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Override
	public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) { // We can add
																							// HttpServletRequest
																							// request in signature once
																							// upgrade to spring 6
		HttpServletRequest	request	= requestResponseHolder.getRequest();			// and this line would be removed
		SecurityContext		context	= SecurityContextHolder.createEmptyContext();
		
		String				uri			= request.getRequestURI();
		String				contextPath	= request.getContextPath();

		if ((contextPath + "/cf/fetchSalt").equals(uri) || (contextPath +"/logout").equals(uri)) {
			return SecurityContextHolder.createEmptyContext();
		}

		String	jwt				= extractJwtFromCookie(request, "auth_token");
		String	saltRequestId	= extractJwtFromCookie(request, "r");
		
		if (jwt != null && jwtUtil.validateToken(jwt,saltRequestId, uri)) {
			var auth = jwtUtil.getAuthentication(jwt,saltRequestId, uri);
			context.setAuthentication(auth);
		}

		return context;
	}

	@Override
	public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
	}

	@Override
	public boolean containsContext(HttpServletRequest request) {
		return extractJwtFromCookie(request, "auth_token") != null;
	}

	private String extractJwtFromCookie(HttpServletRequest request, String cookieName) {
		if (request.getCookies() == null)
			return null;
		for (Cookie cookie : request.getCookies()) {
			if (cookieName.equals(cookie.getName())) {
				return cookie.getValue();
			}
		}
		return null;
	}
}
