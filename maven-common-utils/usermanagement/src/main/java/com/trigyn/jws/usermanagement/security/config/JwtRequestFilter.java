package com.trigyn.jws.usermanagement.security.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

@Component
@Lazy
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	@Lazy
	private UserDetailsService	userDetailsService	= null;

	@Autowired
	@Lazy
	private JwtUtil				jwtUtil				= null;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		final String	authorizationHeader	= request.getHeader("Authorization");

		String			username			= null;
		String			jwt					= null;
		String			requestUri			= request.getRequestURI().substring(request.getContextPath().length());
		if (requestUri.startsWith("/japi/") && !requestUri.equals("/japi/error")) {

			try {
				if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
					jwt			= authorizationHeader.substring(7);
					username	= jwtUtil.extractUsername(jwt);
				}
			} catch (ExpiredJwtException expiredException) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, expiredException.getMessage());
				return;
			} catch (SignatureException signatureException) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, signatureException.getMessage());
				return;
			} catch (Exception exception) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Invalid JWT Token");
				return;
			}
			// Cookie[] cookies = request.getCookies();
			// if(cookies != null && cookies.length!=0) {
			// for (Cookie cookie : cookies) {
			// if(cookie.getName().equalsIgnoreCase("token")) {
			// jwt = cookie.getValue();
			// username = jwtUtil.extractUsername(jwt);
			// }
			// }
			// }

			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

				UserDetails userDetails = userDetailsService.loadUserByUsername(username);

				if (jwtUtil.validateToken(jwt, userDetails)) {

					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					usernamePasswordAuthenticationToken
							.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}
			}
		}

		chain.doFilter(request, response);
	}

}