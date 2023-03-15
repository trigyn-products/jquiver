package com.trigyn.jws.dynarest.utils;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.trigyn.jws.dbutils.cipher.utils.CipherUtilFactory;
import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dynarest.cipher.utils.HttpServletRequestWritableWrapper;
import com.trigyn.jws.dynarest.cipher.utils.HttpServletResponseReadableWrapper;
import com.trigyn.jws.dynarest.repository.IApiClientDetailsRepository;
import com.trigyn.jws.dynarest.service.JwsDynamicRestDetailService;
import com.trigyn.jws.dynarest.vo.ApiClientDetailsVO;
import com.trigyn.jws.dynarest.vo.RestApiDetails;

@Component
public class ApiClientFilter extends OncePerRequestFilter {

	private final static Logger			logger						= LogManager.getLogger(ApiClientFilter.class);

	@Autowired
	private IApiClientDetailsRepository	apiClientDetailsRepository	= null;

	@Autowired
	private PropertyMasterService		propertyMasterService		= null;
	
	@Autowired
	private JwsDynamicRestDetailService jwsService 					= null;

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain chain)
			throws ServletException, IOException {

		boolean isDoFilter = true;
		try {
			
			String requestURL = httpServletRequest.getRequestURL().toString();
			
			String	schedulerUrlProperty	= propertyMasterService.findPropertyMasterValue("scheduler-url") + "-api";
			if (requestURL != null && schedulerUrlProperty != null && requestURL.contains("/"+schedulerUrlProperty+"/")) {
				chain.doFilter(new HttpServletRequestWrapper(httpServletRequest) {
					@Override
					public String getRequestURI() {
						return httpServletRequest.getRequestURI().replace("/" + schedulerUrlProperty + "/", "/" + "api" + "/");
					}

					@Override
					public StringBuffer getRequestURL() {
						return new StringBuffer(requestURL.replace("/" + schedulerUrlProperty + "/", "/" + "api" + "/"));
					}

				}, httpServletResponse);
				return;
			}
			
			//run normally if it's not an API call
			if(requestURL.indexOf("/api/") < 0 && requestURL.indexOf("/japi/") < 0) {
				chain.doFilter(httpServletRequest, httpServletResponse);
				return;
			}
			String subString = requestURL.substring(requestURL.lastIndexOf("/") + 1);
			
			RestApiDetails restAPI =  jwsService.getRestApiDetails(subString);
			//get the restapi object here, and check if not secured
			if(restAPI != null && restAPI.getIsSecured() != null && restAPI.getIsSecured() == Constants.IS_NOT_SECURED) {
				chain.doFilter(httpServletRequest, httpServletResponse);
				return;
			}
			
			//secured API without client key
			if (restAPI != null && restAPI.getIsSecured() != null && restAPI.getIsSecured() == Constants.IS_SECURED && httpServletRequest!=null && httpServletRequest.getHeader("ck") == null) {
				httpServletResponse.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, "API Client key not found.");
			}

			if (httpServletRequest.getHeader("ck") != null) {
				String				clientKey			= httpServletRequest.getHeader("ck");

				ApiClientDetailsVO	apiClientDetailsVO	= apiClientDetailsRepository
						.findClientDetailsByClientKey(clientKey);

				if ("NA".equals(apiClientDetailsVO.getEncryptionAlgorithmName()) == false) {
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
							isDoFilter = false;
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
						}
					}
				}
				
			} 
			if(isDoFilter) {
				chain.doFilter(httpServletRequest, httpServletResponse);
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
}
