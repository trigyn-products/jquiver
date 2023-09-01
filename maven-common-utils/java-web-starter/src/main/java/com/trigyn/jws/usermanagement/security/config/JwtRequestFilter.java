package com.trigyn.jws.usermanagement.security.config;

import java.io.IOException;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.usermanagement.utils.Constants.AuthType;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	private final static Logger			logger						= LogManager.getLogger(JwtRequestFilter.class);
	@Autowired
	@Lazy
	private UserDetailsService			userDetailsService			= null;

	@Autowired
	@Lazy
	private JwtUtil						jwtUtil						= null;

	@Autowired
	private ApplicationSecurityDetails	applicationSecurityDetails	= null;

	@Autowired
	@Lazy
	private PropertyMasterService		propertyMasterService		= null;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		Map<String, Object> authenticationDetails = applicationSecurityDetails.getAuthenticationDetails();

		try {
			String	url				= request.getRequestURI();
			String	apiUrlProperty	= propertyMasterService.findPropertyMasterValue("scheduler-url") + "-api";
			if (url != null && apiUrlProperty != null && url.contains("/" + apiUrlProperty + "/")) {
				chain.doFilter(new HttpServletRequestWrapper(request) {

					@Override
					public String getRequestURI() {
						return url.replace("/" + apiUrlProperty + "/", "/" + "api" + "/");
					}

					@Override
					public StringBuffer getRequestURL() {
						return new StringBuffer(request.getRequestURL().toString().replace("/" + apiUrlProperty + "/",
								"/" + "api" + "/"));
					}

				}, response);
				return;
			}

			if (authenticationDetails != null) {

				String	requestUri			= request.getRequestURI().substring(request.getContextPath().length());
				String	authorizationHeader	= request.getHeader("Authorization");
				String	authTypeAtHeader	= request.getHeader("at");
				String	authenticationType	= request.getParameter("enableAuthenticationType");
				String	username			= null;
				String	jwt					= null;
				if(authTypeAtHeader !=null && null == AuthType.valueOfAt(authTypeAtHeader)){
					response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, "Authentication not supported");
					return;
					
				}
				String	requestAuthType		= (authenticationType != null) ? authenticationType
						: ((authTypeAtHeader != null)
								? String.valueOf(AuthType.valueOfAt(authTypeAtHeader).getAuthType())
								: null);
				if (requestAuthType != null && authorizationHeader != null
						&& authorizationHeader.startsWith("Bearer ")) {
					jwt = authorizationHeader.substring(7);
					if (jwt != null && Integer.valueOf(requestAuthType) == AuthType.DAO.getAuthType()
							|| Integer.valueOf(requestAuthType) == Constants.AuthType.LDAP.getAuthType()) {
						username = jwtUtil.extractUsername(jwt);
					} else if (jwt != null && Integer.valueOf(requestAuthType) == AuthType.OAUTH.getAuthType()) {
						username = retrieveUsernameFromJwtToken(jwt);
						if("jq_532".equalsIgnoreCase(username)) {
							response.sendError(HttpServletResponse.SC_FORBIDDEN,
									"You do not have enough privilege to access this module due to password expiry.");
							return;
						}
						if (username == null) {
							username = jwtUtil.extractUsername(jwt);
						}
					}
				}
				if(request.getRequestURL()!=null) {
					String fullURL = request.getRequestURL().toString();
					if(fullURL !=null) {
						String lastUri = fullURL.substring(fullURL.lastIndexOf('/')+1);
						if(lastUri!=null && jwt == null && lastUri !=null &&  lastUri.equalsIgnoreCase("login") == false) {
							response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED,
									"JWT Token is not available.");
							return;
						}
					}
				}
				if (jwt != null && username == null) {
					response.sendError(HttpServletResponse.SC_FORBIDDEN,
							"You do not have enough privilege to access this module");
					return;
				}
				boolean				authTypeActive		= false;
				Map<String, Object>	activeAuthDetails	= (Map<String, Object>) authenticationDetails
						.get("activeAuthDetails");
				if (activeAuthDetails != null && authTypeAtHeader!=null) {
					authTypeActive = activeAuthDetails
							.containsKey(String.valueOf(AuthType.valueOfAt(authTypeAtHeader).getAuthType()));
				}
				if (username != null && username.equalsIgnoreCase("anonymous") == false && authTypeActive == false) {
					response.sendError(HttpServletResponse.SC_FORBIDDEN,
							"You do not have enough privilege to access this module");
					return;
				}

				if (username != null && authTypeActive && requestAuthType != null
						&& username.equalsIgnoreCase("anonymous") == false) {

					if (requestUri != null
							&& (requestUri.startsWith("/japi/") && requestUri.equals("/japi/error") == false)
							|| (authorizationHeader != null && authorizationHeader.startsWith("Bearer "))) {

						if (activeAuthDetails != null && activeAuthDetails.isEmpty() == false
								&& SecurityContextHolder.getContext().getAuthentication() != null) {
							UserDetails userDetails = userDetailsService.loadUserByUsername(username);
							if (userDetails == null || userDetails.isEnabled() == false) {
								response.sendError(HttpServletResponse.SC_FORBIDDEN,
										"You do not have enough privilege to access this module");
								return;
							}
							if (jwt != null && ((requestAuthType != null
									&& Integer.valueOf(requestAuthType) != Constants.AuthType.OAUTH.getAuthType()
									&& jwtUtil.validateToken(jwt, userDetails))
									|| Integer.valueOf(requestAuthType) == Constants.AuthType.OAUTH.getAuthType())) {

								UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
										userDetails, null, userDetails.getAuthorities());
								usernamePasswordAuthenticationToken
										.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
								SecurityContextHolder.getContext()
										.setAuthentication(usernamePasswordAuthenticationToken);
							}
						}
					}
				}
			}

			chain.doFilter(request, response);

		} catch (ExpiredJwtException expiredException) {
			logger.error(
					"Inside JwtRequestFilter - ExpiredJwtException - Error occurred while processing the request (Request URI: {}})",
					request.getRequestURI(), expiredException);
			response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, expiredException.getMessage());
			return;
		} catch (SignatureException signatureException) {
			logger.error(
					"Inside JwtRequestFilter - SignatureException - Error occurred while processing the request (Request URI: {}})",
					request.getRequestURI(), signatureException);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, signatureException.getMessage());
			return;
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			logger.error(
					"Inside JwtRequestFilter - Throwable - Error occurred while processing the request (Request URI: {}})",
					request.getRequestURI(), throwable);
			if (throwable.getCause() instanceof AccessDeniedException) {
				response.sendError(HttpServletResponse.SC_FORBIDDEN,
						"You do not have enough privilege to access this module");
			} else {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, throwable.getMessage());
			}
			return;
		}
	}
	
	private String retrieveUsernameFromJwtToken(String token) throws Exception {
		try {
			DecodedJWT jwt = JWT.decode(token);
			if (jwt != null && jwt.getKeyId() != null) {
				JwkProvider	provider	= null;
				Jwk			jwk			= null;
				Algorithm	algorithm	= null;
				String providerUrl = propertyMasterService.findPropertyMasterValue("system", "system",
						"JwkProvider");
				if(providerUrl!=null) {
					provider = new UrlJwkProvider(new URL(providerUrl));
				}
				jwk			= provider.get(jwt.getKeyId());
				algorithm	= Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);
				algorithm.verify(jwt);// if the token signature is invalid, the method will throw
										// SignatureVerificationException

				Date expiration = jwt.getClaim("exp").asDate();
				if(expiration.before(new Date())) {
					logger.error("JWT Token expired");
					return "jq_532";
				}
				
				return jwt.getClaim("upn").asString();
			}

		} catch (Exception a_exc) {
			logger.error("Error while retrieving user name from jwt token of OAUTH authentication", a_exc);
		}
		return null;
	}

}
