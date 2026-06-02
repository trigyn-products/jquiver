package com.trigyn.jws.usermanagement.security.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class SavedRequestCookieFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String	requestUri		= request.getRequestURI();
		String	contextPath		= request.getContextPath();
		String	loginPath		= contextPath + "/cf/login";
		boolean	isLoginRequest	= loginPath.equals(requestUri);
		boolean	isGetMethod		= "GET".equalsIgnoreCase(request.getMethod());

		// Skip login path, non-GET requests, or static assets
		if (isLoginRequest == true || isGetMethod == false || shouldSkipStatic(requestUri) == true) {
			filterChain.doFilter(request, response);
			return;
		}

		String	currentUrl		= requestUri + (request.getQueryString() != null ? "?" + request.getQueryString() : "");
		String	savedRequestUrl	= null;

		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if ("SAVED_REQUEST_URL".equals(cookie.getName())) {
					savedRequestUrl = cookie.getValue();
					break;
				}
			}
		}

		// Only update cookie if it's new or changed
		if (savedRequestUrl == null
				|| !savedRequestUrl.equals(URLEncoder.encode(currentUrl, StandardCharsets.UTF_8.name()))) {
			String	encodedUrl			= URLEncoder.encode(currentUrl, StandardCharsets.UTF_8.name());

			Cookie	saveRequestCookie	= new Cookie("SAVED_REQUEST_URL", encodedUrl);
			saveRequestCookie.setHttpOnly(true);
			saveRequestCookie.setPath(contextPath.isEmpty() ? "/" : contextPath);
			saveRequestCookie.setMaxAge(5 * 60); // 5 minutes

			response.addCookie(saveRequestCookie);
		}

		filterChain.doFilter(request, response);
	}

	private boolean shouldSkipStatic(String uri) {
		return uri.matches(
				".*(\\.js|\\.css|\\.png|\\.jpg|\\.jpeg|\\.gif|\\.woff|\\.woff2|\\.svg|\\.ttf|\\.eot|\\.ico|\\.map)$")
				|| uri.contains("/webjars/") || uri.contains("/static/") || uri.contains("/resources/")
				|| uri.contains("/cf/captcha") || uri.contains("/api/") || uri.contains("/cf/files/")
				|| uri.contains("/cf/ler") || uri.contains("/view/") || uri.contains("/cf/saveOtpAndSendMail")
				|| uri.contains("/cf/fdbbi");
	}
}
