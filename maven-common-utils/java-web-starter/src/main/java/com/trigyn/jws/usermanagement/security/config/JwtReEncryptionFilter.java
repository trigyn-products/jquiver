package com.trigyn.jws.usermanagement.security.config;

import com.trigyn.jws.usermanagement.vo.JwtRequestDetails;
import com.trigyn.jws.webstarter.utils.JQuiverProperties;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtReEncryptionFilter extends OncePerRequestFilter {

	private final Log			logger				= LogFactory.getLog(getClass());

	@Autowired
	private JwtUtil				jwtUtil				= null;

	@Autowired
	private JQuiverProperties	jQuiverProperties	= null;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String	oldToken		= jwtUtil.extractJwtFromCookie(request, "auth_token");
		String	oldRequestId	= jwtUtil.extractJwtFromCookie(request, "r");
		String	uri				= request.getRequestURI();
		try {
			if (oldToken != null && jwtUtil.validateToken(oldToken, oldRequestId, uri)) {

				// --- refresh token if expiring soon ---
				JwtRequestDetails newTokenDetails = jwtUtil.refreshTokenIfExpiringSoon(oldToken, oldRequestId, uri);
				// --- End silent refresh ---
				if (newTokenDetails == null && Boolean.TRUE == jQuiverProperties.isEnableSecuredAuthentication()
						&& oldRequestId != null && !"".equals(oldRequestId)) {
					newTokenDetails = jwtUtil.reEncryptToken(oldToken, oldRequestId);
				}
				// For setting cookie either from new generation or only re-encrypted based on
				// the condition
				if (newTokenDetails != null && response != null) {
					jwtUtil.createTokenCookie(response, newTokenDetails);
				}

			} else {
				jwtUtil.clearAuthCookies(response);
			}

		} catch (Exception e) {
			logger.info("JWT re-encryption failed: " + e.getMessage());
			jwtUtil.clearAuthCookies(response);
		}

		filterChain.doFilter(request, response);

	}
}
