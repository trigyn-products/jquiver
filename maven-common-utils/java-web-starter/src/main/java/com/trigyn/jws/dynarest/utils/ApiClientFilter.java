package com.trigyn.jws.dynarest.utils;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import com.trigyn.jws.dbutils.cipher.utils.CipherUtilFactory;
import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dbutils.utils.CustomStopException;
import com.trigyn.jws.dbutils.utils.FileUtilities;
import com.trigyn.jws.dynarest.cipher.utils.HttpServletRequestWritableWrapper;
import com.trigyn.jws.dynarest.cipher.utils.HttpServletResponseReadableWrapper;
import com.trigyn.jws.dynarest.cipher.utils.ParameterWrappedRequest;
import com.trigyn.jws.dynarest.entities.JqEncAlgModPadKeyLookup;
import com.trigyn.jws.dynarest.repository.IApiClientDetailsRepository;
import com.trigyn.jws.dynarest.repository.JqEncAlgLookupRepository;
import com.trigyn.jws.dynarest.service.JwsDynamicRestDetailService;
import com.trigyn.jws.dynarest.vo.ApiClientDetailsVO;
import com.trigyn.jws.dynarest.vo.RestApiDetails;
import com.trigyn.jws.webstarter.utils.JQuiverProperties;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

@Component
public class ApiClientFilter extends OncePerRequestFilter {

	private final static Logger			logger						= LoggerFactory.getLogger(ApiClientFilter.class);

	@Autowired
	private IApiClientDetailsRepository	apiClientDetailsRepository	= null;

	@Autowired
	private PropertyMasterService		propertyMasterService		= null;

	@Autowired
	private JwsDynamicRestDetailService	jwsService					= null;
	
	@Autowired
	private JqEncAlgLookupRepository	algLookupRepository			= null;
	
	@Autowired
	private FileUtilities 				fileUtilities 				= null;
	
	@Autowired
	private JQuiverProperties 			jQuiverPropeties 			= null;

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			FilterChain chain) throws ServletException, IOException {

		boolean isDoFilter = true;
		try {

			final String	requestURL				= httpServletRequest.getRequestURL().toString();
			String apiPath = jQuiverPropeties.getApiPath().replaceFirst("/", "");

			String			schedulerUrlProperty	= propertyMasterService.findPropertyMasterValue("scheduler-url")
					+ "-api";
			if (requestURL != null && schedulerUrlProperty != null
					&& requestURL.contains("/" + schedulerUrlProperty + "/")) {
				chain.doFilter(new HttpServletRequestWrapper(httpServletRequest) {
					@Override
					public String getRequestURI() {
					//	return httpServletRequest.getRequestURI().replace("/" + schedulerUrlProperty + "/",
					//			"/" + "api" + "/");
				return httpServletRequest.getRequestURI().replace("/" + schedulerUrlProperty + "/",
						"/" + apiPath + "/");
					}

					@Override
					public StringBuffer getRequestURL() {
						return new StringBuffer(
								requestURL.replace("/" + schedulerUrlProperty + "/", "/" + apiPath + "/"));
					}

				}, httpServletResponse);
				return;
			}

			// run normally if it's not an API call
			if (requestURL.indexOf(jQuiverPropeties.getApiPath()+"/") < 0 && requestURL.indexOf("/japi/") < 0) {
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
						return header.keySet();
					}

					@Override
					public String getHeader(String a_name) {
						if (a_name == null) {
							return null;
						}
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
				fileUtilities.customSendError(httpServletResponse,HttpServletResponse.SC_PRECONDITION_FAILED,"API Client key not found.");
				return;
			}

			if (httpServletRequest.getHeader("ck") != null) {
				String				clientKey				= httpServletRequest.getHeader("ck");

				ApiClientDetailsVO	apiClientDetailsVO		= apiClientDetailsRepository
						.findClientDetailsByClientKey(clientKey);
				
				if(null == apiClientDetailsVO) {
					fileUtilities.customSendError(httpServletResponse,HttpServletResponse.SC_PRECONDITION_FAILED,"Invalid Client Key.");
					return;
				}

				String				decryptedRequestBody	= null;
				
				Optional<JqEncAlgModPadKeyLookup> algLookupData = algLookupRepository.findById(apiClientDetailsVO.getEncLookupId());

				String				requestBody;
				if ("GET".equalsIgnoreCase(httpServletRequest.getMethod()) == false) {
					Map<String, String[]> extraParams = new TreeMap<String, String[]>();
					ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(httpServletRequest);
					requestBody = wrappedRequest.getReader().lines()
							.collect(Collectors.joining(System.lineSeparator()));

					if (requestBody != null) {
						if (requestBody.equals("") == false && apiClientDetailsVO != null) {
							JqEncAlgModPadKeyLookup algLookup = algLookupData.get();
							decryptedRequestBody = CipherUtilFactory
									.getCipherUtil(algLookup.getJqEncryptionAlgorithmsLookup().getEncryptionAlgoName(), algLookup.getJqEncModeLookup().getModeName(), algLookup.getJqEncPaddLookup().getPaddingName(), algLookup.getJqEncKeyLengthLookup().getKeyLength())
									.decrypt(requestBody, apiClientDetailsVO.getClientSecret(), algLookupData.get().getJqEncryptionAlgorithmsLookup().getEncryptionAlgoName());
						} else {
							decryptedRequestBody = requestBody;
						}
						HttpServletRequestWritableWrapper requestWrapper = new HttpServletRequestWritableWrapper(
								httpServletRequest, decryptedRequestBody, null);
						HttpServletResponseReadableWrapper responseWrapper = new HttpServletResponseReadableWrapper(
								httpServletResponse) {
							private Map<String, String> header = new HashMap<String, String>();

							{
								header.put("Powered-By", "JQuiver");
							}

							@Override
							public void setHeader(String a_name, String a_value) {
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
								return header.keySet();
							}

							@Override
							public String getHeader(String a_name) {
								if (a_name == null) {
									return null;
								}
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
						HttpServletRequest wrappedParamRequest = new ParameterWrappedRequest(requestWrapper, extraParams);
						if (httpServletRequest.getParameterMap() != null) {
							for (String requestParamKey : httpServletRequest.getParameterMap().keySet()) {
								String[] value = new String[1];
								JqEncAlgModPadKeyLookup algLookup = algLookupData.get();
								value[0] = CipherUtilFactory
										.getCipherUtil(algLookup.getJqEncryptionAlgorithmsLookup().getEncryptionAlgoName(), algLookup.getJqEncModeLookup().getModeName(), algLookup.getJqEncPaddLookup().getPaddingName(), algLookup.getJqEncKeyLengthLookup().getKeyLength())
										.decrypt(httpServletRequest.getParameter(requestParamKey),
												apiClientDetailsVO.getClientSecret(), apiClientDetailsVO.getEncryptionAlgorithmName());
								
								extraParams.put(requestParamKey, value);
								wrappedParamRequest = new ParameterWrappedRequest(httpServletRequest, extraParams);
								//requestWrapper.getParameterMap().put(requestParamKey, value);
							}
						}
						isDoFilter = false;
						chain.doFilter(wrappedParamRequest, responseWrapper);

						String encryptedResponseBody = "";
						if(apiClientDetailsVO!=null) {
							JqEncAlgModPadKeyLookup algLookup = algLookupData.get();
							if (responseWrapper.getStatus() != 200 && algLookup != null) {
								if (responseWrapper.getStatus() == 500) {
									encryptedResponseBody = CipherUtilFactory
											.getCipherUtil(algLookup.getJqEncryptionAlgorithmsLookup().getEncryptionAlgoName(), algLookup.getJqEncModeLookup().getModeName(), algLookup.getJqEncPaddLookup().getPaddingName(), algLookup.getJqEncKeyLengthLookup().getKeyLength())
											.encrypt("JQ-500", apiClientDetailsVO.getClientSecret(), apiClientDetailsVO.getEncryptionAlgorithmName());
								} else if (responseWrapper.getStatus() == 403) {
									encryptedResponseBody = CipherUtilFactory
											.getCipherUtil(algLookup.getJqEncryptionAlgorithmsLookup().getEncryptionAlgoName(), algLookup.getJqEncModeLookup().getModeName(), algLookup.getJqEncPaddLookup().getPaddingName(), algLookup.getJqEncKeyLengthLookup().getKeyLength())
											.encrypt("JQ-403", apiClientDetailsVO.getClientSecret(), apiClientDetailsVO.getEncryptionAlgorithmName());
								} else if (responseWrapper.getStatus() == 500) {
									encryptedResponseBody = CipherUtilFactory
											.getCipherUtil(algLookup.getJqEncryptionAlgorithmsLookup().getEncryptionAlgoName(), algLookup.getJqEncModeLookup().getModeName(), algLookup.getJqEncPaddLookup().getPaddingName(), algLookup.getJqEncKeyLengthLookup().getKeyLength())
											.encrypt("JQ-404", apiClientDetailsVO.getClientSecret(), apiClientDetailsVO.getEncryptionAlgorithmName());
								}else if (responseWrapper.getStatus() == 302) {
									encryptedResponseBody = CipherUtilFactory
											.getCipherUtil(algLookup.getJqEncryptionAlgorithmsLookup().getEncryptionAlgoName(), algLookup.getJqEncModeLookup().getModeName(), algLookup.getJqEncPaddLookup().getPaddingName(), algLookup.getJqEncKeyLengthLookup().getKeyLength())
											.encrypt("JQ-302", apiClientDetailsVO.getClientSecret(), apiClientDetailsVO.getEncryptionAlgorithmName());
								}
							} else {
								encryptedResponseBody = CipherUtilFactory
										.getCipherUtil(algLookup.getJqEncryptionAlgorithmsLookup().getEncryptionAlgoName(), algLookup.getJqEncModeLookup().getModeName(), algLookup.getJqEncPaddLookup().getPaddingName(), algLookup.getJqEncKeyLengthLookup().getKeyLength())
										.encrypt(responseWrapper.getResponseBody(), apiClientDetailsVO.getClientSecret(), apiClientDetailsVO.getEncryptionAlgorithmName());
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
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in evalTemplateByContent for Stop Exception.", custStopException);
	//		throw custStopException;
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			logger.error("Inside ApiClientFilter - Error occurred while processing the request (Request URI: {}})",
					httpServletRequest.getRequestURI(), throwable);
			if (throwable.getCause() instanceof AccessDeniedException) {
				fileUtilities.customSendError(httpServletResponse,HttpServletResponse.SC_FORBIDDEN,
						"You do not have enough privilege to access this module : "
								+ httpServletRequest.getRequestURI());
			} else {
				fileUtilities.customSendError(httpServletResponse,HttpServletResponse.SC_INTERNAL_SERVER_ERROR,throwable.getMessage());
			}
			return;
		}

	}
}
