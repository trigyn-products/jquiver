package com.trigyn.jws.dynarest.cipher.utils;

import java.io.IOException;

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
import com.trigyn.jws.dbutils.utils.FileUtilities;
import com.trigyn.jws.webstarter.utils.JQuiverProperties;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SchedulerRequestFilter extends OncePerRequestFilter {

	@Autowired
	@Lazy
	private UserDetailsService		userDetailsService		= null;

	@Autowired
	@Lazy
	private PropertyMasterService	propertyMasterService	= null;
	
	@Autowired
	private FileUtilities			fileUtilities			= null;
	
	@Autowired
	private JQuiverProperties 			jQuiverPropeties 			= null;
	
	

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		try {
			String	schedulerId				= request.getHeader("schId");
			String	uri						= request.getRequestURI();

			String	schedulerUrlProperty	= propertyMasterService.findPropertyMasterValue("scheduler-url");
			String	propertyAdminEmailId	= propertyMasterService.findPropertyMasterValue("system", "system",
					"adminEmailId");
			String	adminEmail				= propertyAdminEmailId == null ? "admin@jquiver.io"
					: propertyAdminEmailId.equals("") ? "admin@jquiver.io" : propertyAdminEmailId;
			String	apiUrlProperty			= propertyMasterService.findPropertyMasterValue("scheduler-url") + "-api";
			String apiPath = jQuiverPropeties.getApiPath().replaceFirst("/", "");
			if (uri != null && schedulerUrlProperty != null && uri.contains("/" + schedulerUrlProperty + "/")
					&& schedulerId != null && schedulerId.isEmpty() == false) {
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
 						//return uri.replace("/sch-api/" + schedulerUrlProperty + "/", "/" + "api" + "/");
						
						return uri.replace("/sch-api/" + schedulerUrlProperty + "/", "/" + apiPath + "/");
					}

					@Override
					public StringBuffer getRequestURL() {
						return new StringBuffer(uri.replace("/sch-api/" + schedulerUrlProperty + "/", "/" + apiPath + "/"));
					}

				}, response);
			} else {
				chain.doFilter(new HttpServletRequestWrapper(request) {

					@Override
					public String getRequestURI() {
						return uri.replace("/" + apiUrlProperty + "/", "/" + apiPath + "/");
					}

					@Override
					public StringBuffer getRequestURL() {
						return new StringBuffer(request.getRequestURL().toString().replace("/" + apiUrlProperty + "/", "/" + apiPath + "/"));
					}

				}, response);
			}

		} catch (ExpiredJwtException expiredException) {
			fileUtilities.customSendError(response,HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					expiredException.getMessage());
			return;
		} catch (SignatureException signatureException) {
			fileUtilities.customSendError(response,HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					signatureException.getMessage());
			return;
		} catch (Exception exception) {
			fileUtilities.customSendError(response,HttpStatus.FORBIDDEN.value(), "You do not have enough privilege to access this module");
			return;
		}
	}

}