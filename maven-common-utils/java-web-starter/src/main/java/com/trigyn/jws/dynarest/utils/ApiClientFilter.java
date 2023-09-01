package com.trigyn.jws.dynarest.utils;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import com.trigyn.jws.dbutils.cipher.utils.CipherUtilFactory;
import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dynarest.cipher.utils.HttpServletRequestWritableWrapper;
import com.trigyn.jws.dynarest.cipher.utils.HttpServletResponseReadableWrapper;
import com.trigyn.jws.dynarest.cipher.utils.ParameterWrappedRequest;
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
	private JwsDynamicRestDetailService	jwsService					= null;

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			FilterChain chain) throws ServletException, IOException {

		boolean isDoFilter = true;
		try {

			final String	requestURL				= httpServletRequest.getRequestURL().toString();

			String			schedulerUrlProperty	= propertyMasterService.findPropertyMasterValue("scheduler-url")
					+ "-api";
			if (requestURL != null && schedulerUrlProperty != null
					&& requestURL.contains("/" + schedulerUrlProperty + "/")) {
				chain.doFilter(new HttpServletRequestWrapper(httpServletRequest) {
					@Override
					public String getRequestURI() {
						return httpServletRequest.getRequestURI().replace("/" + schedulerUrlProperty + "/",
								"/" + "api" + "/");
					}

					@Override
					public StringBuffer getRequestURL() {
						return new StringBuffer(
								requestURL.replace("/" + schedulerUrlProperty + "/", "/" + "api" + "/"));
					}

				}, httpServletResponse);
				return;
			}

			// run normally if it's not an API call
			if (requestURL.indexOf("/api/") < 0 && requestURL.indexOf("/japi/") < 0) {
				chain.doFilter(httpServletRequest, httpServletResponse);
				return;
			}
			String			subString	= requestURL.substring(requestURL.lastIndexOf("/") + 1);

			RestApiDetails	restAPI		= jwsService.getRestApiDetails(subString);
			// get the restapi object here, and check if not secured
			if (restAPI != null && restAPI.getIsSecured() != null
					&& restAPI.getIsSecured() == Constants.IS_NOT_SECURED) {
				chain.doFilter(httpServletRequest, new HttpServletResponseWrapper(httpServletResponse) {
					private Map<String, String> header = new HashMap<String, String>();

					{
						header.put("Powered-By", "JQuiver");
					}

					@Override
					public void setHeader(String a_name, String a_value) {
						// System.out.println("Setting header : " + a_name + " : " + a_value);
						super.setHeader(a_name, a_value);
						if (a_name == null) {
							return;
						}
						if (a_value == null) {
							header.remove(a_name);
						}
						header.put(a_name, a_value);
					}

					@Override
					public boolean containsHeader(String a_name) {
						if (a_name == null) {
							return false;
						}
						return header.containsKey(a_name);
					}

					@Override
					public Collection<String> getHeaderNames() {
						// System.out.println("ApiClientFilter.doFilterInternal(...).new
						// HttpServletResponseWrapper() {...}.getHeaderNames()");
						return header.keySet();
					}

					@Override
					public String getHeader(String a_name) {
						if (a_name == null) {
							return null;
						}
						// System.out.println(requestURL);
						// System.out.println("ApiClientFilter.doFilterInternal(...).new
						// HttpServletResponseWrapper() {...}.getHeader() " + a_name + " = " +
						// header.get(a_name));
						if (header.get(a_name) == null) {
							return super.getHeader(a_name);
						}
						return header.get(a_name);
					}

					@Override
					public void addHeader(String a_name, String a_value) {
						super.addHeader(a_name, a_value);
						if (a_name == null) {
							return;
						}
						if (a_value == null) {
							header.remove(a_name);
						}
						header.put(a_name, a_value);
					}
				});
				return;
			}

			// secured API without client key
			if (restAPI != null && restAPI.getIsSecured() != null && restAPI.getIsSecured() == Constants.IS_SECURED
					&& httpServletRequest != null && httpServletRequest.getHeader("ck") == null) {
				httpServletResponse.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, "API Client key not found.");
				return;
			}

			if (httpServletRequest.getHeader("ck") != null) {
				String				clientKey				= httpServletRequest.getHeader("ck");

				ApiClientDetailsVO	apiClientDetailsVO		= apiClientDetailsRepository
						.findClientDetailsByClientKey(clientKey);
				
				if(null == apiClientDetailsVO) {
					httpServletResponse.sendError(HttpServletResponse.SC_PRECONDITION_FAILED,
							"Invalid Client Key ");
					return;
				}

				String				decryptedRequestBody	= null;

				String				requestBody;
				if ("GET".equalsIgnoreCase(httpServletRequest.getMethod()) == false) {
					Map<String, String[]> extraParams = new TreeMap<String, String[]>();
					HttpServletRequest wrappedParamRequest = new ParameterWrappedRequest(httpServletRequest, extraParams);
					ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(httpServletRequest);
					requestBody = wrappedRequest.getReader().lines()
							.collect(Collectors.joining(System.lineSeparator()));

					if (requestBody != null) {
						if (requestBody.equals("") == false && apiClientDetailsVO != null) {
							// System.out.println(requestBody);
							decryptedRequestBody = CipherUtilFactory
									.getCipherUtil(apiClientDetailsVO.getEncryptionAlgorithmName())
									.decrypt(requestBody, apiClientDetailsVO.getClientSecret());
						} else {
							decryptedRequestBody = requestBody;
						}
						HttpServletRequestWritableWrapper requestWrapper = new HttpServletRequestWritableWrapper(
								httpServletRequest, decryptedRequestBody, null);
						IOUtils.toString(requestWrapper.getReader());
						HttpServletResponseReadableWrapper responseWrapper = new HttpServletResponseReadableWrapper(
								httpServletResponse) {
							private Map<String, String> header = new HashMap<String, String>();

							{
								header.put("Powered-By", "JQuiver");
							}

							@Override
							public void setHeader(String a_name, String a_value) {
								// System.out.println("Setting header : " + a_name + " : " + a_value);
								super.setHeader(a_name, a_value);
								if (a_name == null) {
									return;
								}
								if (a_value == null) {
									header.remove(a_name);
								}
								header.put(a_name, a_value);
							}

							@Override
							public boolean containsHeader(String a_name) {
								if (a_name == null) {
									return false;
								}
								return header.containsKey(a_name);
							}

							@Override
							public Collection<String> getHeaderNames() {
								// System.out.println("ApiClientFilter.doFilterInternal(...).new
								// HttpServletResponseWrapper() {...}.getHeaderNames()");
								return header.keySet();
							}

							@Override
							public String getHeader(String a_name) {
								if (a_name == null) {
									return null;
								}
								// System.out.println(requestURL);
								// System.out.println("ApiClientFilter.doFilterInternal(...).new
								// HttpServletResponseWrapper() {...}.getHeader() " + a_name + " = " +
								// header.get(a_name));
								if (header.get(a_name) == null) {
									return super.getHeader(a_name);
								}
								return header.get(a_name);
							}

							@Override
							public void addHeader(String a_name, String a_value) {
								super.addHeader(a_name, a_value);
								if (a_name == null) {
									return;
								}
								if (a_value == null) {
									header.remove(a_name);
								}
								header.put(a_name, a_value);
							}
						};
						if (httpServletRequest.getParameterMap() != null) {
							for (String requestParamKey : httpServletRequest.getParameterMap().keySet()) {
								String[] value = new String[1];
								value[0] = CipherUtilFactory
										.getCipherUtil(apiClientDetailsVO.getEncryptionAlgorithmName())
										.decrypt(httpServletRequest.getParameter(requestParamKey),
												apiClientDetailsVO.getClientSecret());
								
								extraParams.put(requestParamKey, value);
								wrappedParamRequest = new ParameterWrappedRequest(httpServletRequest, extraParams);
								//requestWrapper.getParameterMap().put(requestParamKey, value);
							}
						}
						isDoFilter = false;
						chain.doFilter(wrappedParamRequest, responseWrapper);

						String encryptedResponseBody = "";
						if(apiClientDetailsVO!=null) {
							if (responseWrapper.getStatus() != 200) {
								if (responseWrapper.getStatus() == 500) {
									encryptedResponseBody = CipherUtilFactory
											.getCipherUtil(apiClientDetailsVO.getEncryptionAlgorithmName())
											.encrypt("JQ-500", apiClientDetailsVO.getClientSecret());
								} else if (responseWrapper.getStatus() == 403) {
									encryptedResponseBody = CipherUtilFactory
											.getCipherUtil(apiClientDetailsVO.getEncryptionAlgorithmName())
											.encrypt("JQ-403", apiClientDetailsVO.getClientSecret());
								} else if (responseWrapper.getStatus() == 500) {
									encryptedResponseBody = CipherUtilFactory
											.getCipherUtil(apiClientDetailsVO.getEncryptionAlgorithmName())
											.encrypt("JQ-404", apiClientDetailsVO.getClientSecret());
								}else if (responseWrapper.getStatus() == 302) {
									encryptedResponseBody = CipherUtilFactory
											.getCipherUtil(apiClientDetailsVO.getEncryptionAlgorithmName())
											.encrypt("JQ-302", apiClientDetailsVO.getClientSecret());
								}
							} else {
								encryptedResponseBody = CipherUtilFactory
										.getCipherUtil(apiClientDetailsVO.getEncryptionAlgorithmName())
										.encrypt(responseWrapper.getResponseBody(), apiClientDetailsVO.getClientSecret());
							}
						}
						
						httpServletResponse.setContentType(httpServletRequest.getContentType());
						byte[] responseData = encryptedResponseBody.getBytes(responseWrapper.getCharacterEncoding());
						httpServletResponse.setContentLength(responseData.length);
						httpServletResponse.getWriter().write(encryptedResponseBody);
					}
				}
			}

			if (isDoFilter) {
				logger.debug("This should not have happened ==> ApiClientFilter.doFilterInternal() " + requestURL);
				chain.doFilter(httpServletRequest, httpServletResponse);
			}
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			logger.error("Inside ApiClientFilter - Error occurred while processing the request (Request URI: {}})",
					httpServletRequest.getRequestURI(), throwable);
			if (throwable.getCause() instanceof AccessDeniedException) {
				httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN,
						"You do not have enough privilege to access this module : "
								+ httpServletRequest.getRequestURI());
			} else {
				httpServletResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, throwable.getMessage());
			}
			return;
		}

	}
}
