package com.trigyn.jws.dynarest.utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;

import com.trigyn.jws.dbutils.cipher.utils.CipherUtilFactory;
import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dynarest.cipher.utils.HttpServletRequestWritableWrapper;
import com.trigyn.jws.dynarest.cipher.utils.HttpServletResponseReadableWrapper;
import com.trigyn.jws.dynarest.repository.IApiClientDetailsRepository;
import com.trigyn.jws.dynarest.vo.ApiClientDetailsVO;

@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApiClientFilter implements Filter {

	private final static Logger			logger						= LogManager.getLogger(ApiClientFilter.class);

	@Autowired
	private IApiClientDetailsRepository	apiClientDetailsRepository	= null;

	@Autowired
	private PropertyMasterService		propertyMasterService		= null;

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest	httpServletRequest	= (HttpServletRequest) servletRequest;
		HttpServletResponse	httpServletResponse	= (HttpServletResponse) servletResponse;
		try {

			if (httpServletRequest.getHeader("ck") != null) {
				String				clientKey			= httpServletRequest.getHeader("ck");

				ApiClientDetailsVO	apiClientDetailsVO	= apiClientDetailsRepository
						.findClientDetailsByClientKey(clientKey);

				if ("NA".equals(apiClientDetailsVO.getEncryptionAlgorithmName()) == false && validateURLPattern(
						httpServletRequest.getRequestURL().toString(), apiClientDetailsVO.getInclusionURLPattern())) {
					String	decryptedRequestBody	= null;

					String	requestBody;
					if ("POST".equalsIgnoreCase(httpServletRequest.getMethod())) {
						requestBody = httpServletRequest.getReader().lines()
								.collect(Collectors.joining(System.lineSeparator()));
						
						if (requestBody != null) {
							if (requestBody.equals("") == false) {
								System.out.println(requestBody);
								decryptedRequestBody = CipherUtilFactory
										.getCipherUtil(apiClientDetailsVO.getEncryptionAlgorithmName())
										.decrypt(requestBody, apiClientDetailsVO.getClientSecret());
							} else {
								decryptedRequestBody = requestBody;
							}
							HttpServletRequestWritableWrapper	requestWrapper	= new HttpServletRequestWritableWrapper(
									httpServletRequest, decryptedRequestBody, null);
							IOUtils.toString(requestWrapper.getReader());
							HttpServletResponseReadableWrapper	responseWrapper	= new HttpServletResponseReadableWrapper(
									httpServletResponse);
							if(httpServletRequest.getParameterMap() != null) {
								for (String requestParamKey : httpServletRequest.getParameterMap().keySet()) {
									String[] value = new String[1];
									value[0] = CipherUtilFactory.getCipherUtil(apiClientDetailsVO.getEncryptionAlgorithmName())
											.decrypt(httpServletRequest.getParameter(requestParamKey), apiClientDetailsVO.getClientSecret());
									requestWrapper.getParameterMap().put(requestParamKey, value);
								}
							}
							
							chain.doFilter(requestWrapper, responseWrapper);
							
							String encryptedResponseBody = "";
							if(responseWrapper.getStatus() != 200) {
								if(responseWrapper.getStatus() == 500) {
									encryptedResponseBody = CipherUtilFactory
											.getCipherUtil(apiClientDetailsVO.getEncryptionAlgorithmName())
											.encrypt("JQ-500", apiClientDetailsVO.getClientSecret());
								} else if(responseWrapper.getStatus() == 403) {
									encryptedResponseBody = CipherUtilFactory
											.getCipherUtil(apiClientDetailsVO.getEncryptionAlgorithmName())
											.encrypt("JQ-403", apiClientDetailsVO.getClientSecret());
								} else if(responseWrapper.getStatus() == 500) {
									encryptedResponseBody = CipherUtilFactory
											.getCipherUtil(apiClientDetailsVO.getEncryptionAlgorithmName())
											.encrypt("JQ-404", apiClientDetailsVO.getClientSecret());
								}
							} else {
								encryptedResponseBody = CipherUtilFactory
										.getCipherUtil(apiClientDetailsVO.getEncryptionAlgorithmName())
										.encrypt(responseWrapper.getResponseBody(), apiClientDetailsVO.getClientSecret());
								
							}
							httpServletResponse.setContentType(httpServletRequest.getContentType());
							byte[] responseData = encryptedResponseBody
									.getBytes(responseWrapper.getCharacterEncoding());
							httpServletResponse.setContentLength(responseData.length);
							httpServletResponse.getWriter().write(encryptedResponseBody);
						} else {
							chain.doFilter(servletRequest, servletResponse);
						}
					} else {
						chain.doFilter(servletRequest, servletResponse);
					}
				} else {
					chain.doFilter(servletRequest, servletResponse);
				}

			} else {
				chain.doFilter(servletRequest, servletResponse);
			}
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			logger.error("Inside ApiClientFilter - Error occurred while processing the request (Request URI: {}})",
					httpServletRequest.getRequestURI(), throwable);
			if (throwable.getCause() instanceof AccessDeniedException) {
				httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN,
						"You do not have enough privilege to access this module");
			} else {
				httpServletResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, throwable.getMessage());
			}
			return;
		}
	}

	private boolean validateURLPattern(String requestURL, String urlPatterns) throws Exception {
		String contextPath = propertyMasterService.findPropertyMasterValue("base-url");
		if (contextPath.endsWith("/") == false) {
			contextPath = contextPath + "/";
		}
		if (urlPatterns != null) {
			List<String> inclusionUrlPatterns = Arrays.asList(urlPatterns.split(", ", 0));
			for (String inclusionUrlPattern : inclusionUrlPatterns) {
				if (inclusionUrlPattern.startsWith("/") == false) {
					inclusionUrlPattern = "/" + inclusionUrlPattern;
				}
				if (requestURL.startsWith(contextPath.concat("api").concat(inclusionUrlPattern))) {
					return true;
				} else if (requestURL.startsWith(contextPath.concat("japi").concat(inclusionUrlPattern))) {
					return true;
				}
			}
		}
		return true;
	}
}
