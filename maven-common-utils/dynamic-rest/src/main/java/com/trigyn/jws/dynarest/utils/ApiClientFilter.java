package com.trigyn.jws.dynarest.utils;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.trigyn.jws.dbutils.spi.PropertyMasterDetails;
import com.trigyn.jws.dynarest.cipher.utils.CipherUtilFactory;
import com.trigyn.jws.dynarest.cipher.utils.HttpServletRequestWritableWrapper;
import com.trigyn.jws.dynarest.cipher.utils.HttpServletResponseReadableWrapper;
import com.trigyn.jws.dynarest.repository.IApiClientDetailsRepository;
import com.trigyn.jws.dynarest.vo.ApiClientDetailsVO;

public class ApiClientFilter implements Filter {

	private final static Logger			logger						= LogManager.getLogger(ApiClientFilter.class);

	@Autowired
	private IApiClientDetailsRepository	apiClientDetailsRepository	= null;

	@Autowired
	private PropertyMasterDetails		propertyMasterDetails		= null;

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {

		try {
			HttpServletRequest	httpServletRequest	= (HttpServletRequest) servletRequest;
			HttpServletResponse	httpServletResponse	= (HttpServletResponse) servletResponse;

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

						if (requestBody != null && requestBody.equals("") == false) {
							decryptedRequestBody = CipherUtilFactory
									.getCipherUtil(apiClientDetailsVO.getEncryptionAlgorithmName())
									.decrypt(requestBody, apiClientDetailsVO.getClientSecret());

							HttpServletRequestWritableWrapper	requestWrapper	= new HttpServletRequestWritableWrapper(
									httpServletRequest, decryptedRequestBody);
							HttpServletResponseReadableWrapper	responseWrapper	= new HttpServletResponseReadableWrapper(
									httpServletResponse);
							chain.doFilter(requestWrapper, responseWrapper);

							String encryptedResponseBody = CipherUtilFactory
									.getCipherUtil(apiClientDetailsVO.getEncryptionAlgorithmName())
									.encrypt(responseWrapper.getResponseBody(), apiClientDetailsVO.getClientSecret());
							httpServletResponse.getWriter().write(encryptedResponseBody);

							httpServletResponse.setContentType(httpServletRequest.getContentType());
							byte[] responseData = encryptedResponseBody
									.getBytes(responseWrapper.getCharacterEncoding());
							httpServletResponse.setContentLength(responseData.length);
							ServletOutputStream out = httpServletResponse.getOutputStream();
							out.write(responseData);

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
		} catch ( Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean validateURLPattern(String requestURL, String urlPatterns) {
		String contextPath = propertyMasterDetails.getSystemPropertyValue("base-url");
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
