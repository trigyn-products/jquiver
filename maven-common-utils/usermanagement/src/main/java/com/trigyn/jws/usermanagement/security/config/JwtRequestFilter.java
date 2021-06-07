package com.trigyn.jws.usermanagement.security.config;

import java.io.IOException;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;

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

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.trigyn.jws.usermanagement.utils.Constants;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

@Component
@Lazy
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	@Lazy
	private UserDetailsService			userDetailsService			= null;

	@Autowired
	@Lazy
	private JwtUtil						jwtUtil						= null;

	@Autowired
	private ApplicationSecurityDetails	applicationSecurityDetails	= null;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		try {
			if(applicationSecurityDetails.getAuthenticationType() != null) {
				Integer			authType			= Integer.parseInt(applicationSecurityDetails.getAuthenticationType());
				final String	authorizationHeader	= request.getHeader("Authorization");
	
				String			username			= null;
				String			jwt					= null;
				String			requestUri			= request.getRequestURI().substring(request.getContextPath().length());
				if ((requestUri.startsWith("/japi/") && !requestUri.equals("/japi/error")) || (authorizationHeader != null && authorizationHeader.startsWith("Bearer "))) {
	
					if (authType == Constants.AuthType.DAO.getAuthType()
							|| authType == Constants.AuthType.LDAP.getAuthType()) {
						if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
							jwt			= authorizationHeader.substring(7);
							username	= jwtUtil.extractUsername(jwt);
						}
					} else if (authType == Constants.AuthType.OAUTH.getAuthType()) {
						if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
							jwt			= authorizationHeader.substring(7);
							username	= retrieveUsernameFromOauthToken(jwt);
						}
					}
					if ((requestUri.startsWith("/japi/") && !requestUri.equals("/japi/error") && username != null && SecurityContextHolder.getContext().getAuthentication() == null)
							|| username != null) {
	
						UserDetails userDetails = userDetailsService.loadUserByUsername(username);
	
						if (((authType == Constants.AuthType.DAO.getAuthType()
								|| authType == Constants.AuthType.LDAP.getAuthType())
								&& jwtUtil.validateToken(jwt, userDetails))
								|| authType == Constants.AuthType.OAUTH.getAuthType()) {
	
							UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
									userDetails, null, userDetails.getAuthorities());
							usernamePasswordAuthenticationToken
									.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
							SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
						}
					}
				}
			}

			chain.doFilter(request, response);

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
	}

	private String retrieveUsernameFromOauthToken(String token) throws Exception {
		DecodedJWT	jwt			= JWT.decode(token);

		JwkProvider	provider	= null;
		Jwk			jwk			= null;
		Algorithm	algorithm	= null;

		provider	= new UrlJwkProvider(new URL("https://login.microsoftonline.com/common/discovery/keys"));
		jwk			= provider.get(jwt.getKeyId());
		algorithm	= Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);
		algorithm.verify(jwt);// if the token signature is invalid, the method will throw
								// SignatureVerificationException

		return jwt.getClaim("upn").asString();
	}

}