package com.trigyn.jws.dynarest.utils;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dbutils.spi.PropertyMasterDetails;
import com.trigyn.jws.dbutils.utils.CustomStopException;
import com.trigyn.jws.dbutils.utils.FileUtilities;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author Rashmi Shirke
 * @since 07-Apr-2025
 */

@Component
public class XSSFilter extends OncePerRequestFilter {

	private final static Logger		logger					= LoggerFactory.getLogger(XSSFilter.class);

	@Autowired
	private FileUtilities fileUtilities = null;

	@Autowired
	private PropertyMasterDetails propertyMasterDetails = null;

	@Autowired
	private PropertyMasterService	propertyMasterService	= null;
	
	private static final Pattern BASE64_PATTERN = Pattern.compile("^[A-Za-z0-9+/]+={0,2}$");
	private static final Pattern HEX_PATTERN = Pattern.compile("^[0-9a-fA-F]+$");

	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String methodType = request.getMethod();

			// Only check login requests
//			if ("/cf/login".equals(request.getServletPath()) && "POST".equalsIgnoreCase(request.getMethod())) {
//				String email = request.getParameter("email");
//				String password = request.getParameter("password");
//
//				if ((email != null && containsXSS(email)) || (password != null && containsXSS(password))) {
//					// Block the request
//					HttpServletResponse resp = (HttpServletResponse) response;
//					resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
//							"There seems to be a problem with your request. Please check your input and try again.");
//					return;
//				}
//			}

			if (("GET").equalsIgnoreCase(methodType)) {
				// Loop through all parameters
				for (String paramName : request.getParameterMap().keySet()) {
					String[] values = request.getParameterValues(paramName);

					if (null != values) {
						for (String value : values) {
							String decodedValue = decode(value);

							if (containsXSS(decodedValue)) {
								fileUtilities.customSendError(response, HttpServletResponse.SC_BAD_REQUEST,
										"There seems to be a problem with your request. Please check your input and try again.");
								return;
							}
						}
					}
				}

			}
			/**
			 * Removed the below block of code as it is causing different issues at
			 * different scenarios. One of the case, is where OAuth authentication is hrsSIT
			 * is not working, and redirecting to Invalid Request page, if cache is cleared.
			 */
//			String baseUrl = propertyMasterDetails.getSystemPropertyValue("base-url");
//			String referrerUrl = request.getHeader("referer");
//			String xForwardedHost = normalizeHost(request.getHeader("x-forwarded-host"));
//			String allowedHost = normalizeHost(baseUrl);
//			String	uri						= request.getRequestURI();
//
//			String	schedulerUrlProperty	= propertyMasterService.findPropertyMasterValue("scheduler-url");
//			if (uri != null && schedulerUrlProperty != null && uri.contains("/" + schedulerUrlProperty + "/") == false
//					&& (null == baseUrl || (null != referrerUrl && referrerUrl.startsWith(baseUrl) == false)
//							|| (null != xForwardedHost && xForwardedHost.equals(allowedHost) == false))) {
//				System.out.println("baseUrl ## " + baseUrl);
//				System.out.println("referrerurl ## " + referrerUrl);
//				System.out.println("xforwarededHost ## " + xForwardedHost);
//
//				fileUtilities.customSendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid Request");
//				return;
//			}

			filterChain.doFilter(request, response);
		} catch (CustomStopException custStopException) {
			logger.error("Custom stop exception caught.", custStopException);
			throw custStopException;
		} catch (Exception excp) {
			excp.printStackTrace();
			logger.error("Error in Cross-Site Scripting Filter", excp.getMessage());
			String errorMessage = excp.getMessage();
			if (excp.getCause() != null && excp.getCause().getMessage() != null) {
				errorMessage = excp.getCause().getMessage();
			}
			fileUtilities.customSendError(response, HttpServletResponse.SC_BAD_REQUEST, errorMessage);
			return;
		}
	}

	private String decode(String value) {
		int times = 3;
		if (value == null)
			return null;
		for (int i = 0; i < times; i++) {
			value = URLDecoder.decode(value, StandardCharsets.UTF_8);
		}
		// Step 1: URL Decode

		// Step 2: Unicode Decode
		value = decodeUnicode(value);

		// Step 3: Base64 or Hex Decode
		value = tryDecodeBase64OrHex(value);

		return value;
	}

	public boolean containsXSS(String value) {
		if (null == value)
			return false;
		List<Pattern> XSS_PATTERNS = propertyMasterDetails.getXssPatternList();
		for (Pattern pattern : XSS_PATTERNS) {
			if (pattern.matcher(value).find()) {
				return true;
			}
		}
		return false;
	}

	private String decodeUnicode(String input) {
		Pattern unicode = Pattern.compile("%u([0-9A-Fa-f]{4})");
		Matcher matcher = unicode.matcher(input);
		StringBuffer decoded = new StringBuffer();

		while (matcher.find()) {
			int code = Integer.parseInt(matcher.group(1), 16);
			matcher.appendReplacement(decoded, Character.toString((char) code));
		}
		matcher.appendTail(decoded);
		return decoded.toString();
	}

	// Tries to decode Base64 and Hexadecimal values
	private String tryDecodeBase64OrHex(String value) {
		if (value == null)
			return null;

		// Try Base64 decoding
		try {

			if (isBase64(value)) {
				byte[] decoded = Base64.getDecoder().decode(value);
				return new String(decoded, StandardCharsets.UTF_8);
			}
		} catch (IllegalArgumentException e) {
			logger.error("Error while Base64 decoding: ", e);
		}

		// Try Hexadecimal decoding
		try {
			// Ensure the value consists only of hex characters and has an even length
			if (isHex(value)) {
				byte[] bytes = new byte[value.length() / 2];
				for (int i = 0; i < value.length(); i += 2) {
					bytes[i / 2] = (byte) Integer.parseInt(value.substring(i, i + 2), 16);
				}
				return new String(bytes, StandardCharsets.UTF_8);
			}
		} catch (Exception e) {
			logger.error("Error while Hexadecimal decoding: ", e);
		}

		// Return the original value if no Base64/Hex decoding is possible
		return value;
	}

	public static boolean isBase64(String value) {
		return value != null && value.length() % 4 == 0 && BASE64_PATTERN.matcher(value).matches();
	}

	private static boolean isHex(String value) {
		return value != null && value.length() % 2 == 0 && HEX_PATTERN.matcher(value).matches();
	}

	private String normalizeHost(String url) {
		if (url == null)
			return null;
		url = url.toLowerCase();
		if (url.startsWith("http://"))
			url = url.substring(7);
		else if (url.startsWith("https://"))
			url = url.substring(8);
//		int colonIndex = url.indexOf(':');
//		if (colonIndex != -1)
//			url = url.substring(0, colonIndex);
		return url.replaceAll("/+$", "");
	}
}
