package com.trigyn.jws.dynarest.cipher.utils;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.trigyn.jws.dbutils.service.PropertyMasterService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

@Component
@Lazy
public class SchedulerRequestFilter extends OncePerRequestFilter {

	@Autowired
	@Lazy
	private UserDetailsService		userDetailsService		= null;

	@Autowired
	@Lazy
	private PropertyMasterService	propertyMasterService	= null;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		try {
			String	schedulerId				= request.getHeader("schId");
			String	url						= request.getRequestURI();

			String	schedulerUrlProperty	= propertyMasterService.findPropertyMasterValue("scheduler-url");
			String propertyAdminEmailId = propertyMasterService.findPropertyMasterValue("system", "system", "adminEmailId");
			String adminEmail = propertyAdminEmailId == null ? "admin@jquiver.io" : propertyAdminEmailId.equals("") ? "admin@jquiver.io" : propertyAdminEmailId;
			if (url != null && schedulerUrlProperty != null && url.contains(schedulerUrlProperty) && schedulerId != null
					&& schedulerId.isEmpty() == false) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(adminEmail);

				if (userDetails == null) {
					userDetails = userDetailsService.loadUserByUsername(adminEmail);
				}

				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

				chain.doFilter(new HttpServletRequestWrapper(request) {

					@Override
					public String getRequestURI() {
						return url.replace(schedulerUrlProperty, "api");
					}

					@Override
					public StringBuffer getRequestURL() {
						return new StringBuffer(url.replace(schedulerUrlProperty, "api"));
					}

				}, response);
			} else {
				chain.doFilter(request, response);
			}

		} catch (ExpiredJwtException expiredException) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, expiredException.getMessage());
			return;
		} catch (SignatureException signatureException) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, signatureException.getMessage());
			return;
		} catch (Exception exception) {
			response.sendError(HttpStatus.FORBIDDEN.value(), "You do not have enough privilege to access this module");
			return;
		}
	}

}