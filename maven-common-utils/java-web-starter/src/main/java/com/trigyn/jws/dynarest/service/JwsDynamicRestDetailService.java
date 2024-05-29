package com.trigyn.jws.dynarest.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.UUID;

import javax.net.ssl.SSLException;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.IOUtils;
import org.quartz.DateBuilder;
import org.quartz.JobDataMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.utils.ActivityLog;
import com.trigyn.jws.dbutils.utils.CustomResponseEntity;
import com.trigyn.jws.dbutils.utils.CustomStopException;
import com.trigyn.jws.dbutils.vo.FileInfo;
import com.trigyn.jws.dbutils.vo.FileInfo.FileType;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.dynarest.controller.FileUploadController;
import com.trigyn.jws.dynarest.dao.JwsDynamicRestDAORepository;
import com.trigyn.jws.dynarest.dao.JwsDynamicRestDetailsRepository;
import com.trigyn.jws.dynarest.dao.JwsDynarestDAO;
import com.trigyn.jws.dynarest.entities.JwsDynamicRestDetail;
import com.trigyn.jws.dynarest.utils.Constants;
import com.trigyn.jws.dynarest.utils.Constants.Platforms;
import com.trigyn.jws.dynarest.utils.WebClientCustomException;
import com.trigyn.jws.dynarest.vo.RestApiDaoQueries;
import com.trigyn.jws.dynarest.vo.RestApiDetails;
import com.trigyn.jws.dynarest.vo.WebClientAttacmentVO;
import com.trigyn.jws.dynarest.vo.WebClientParamVO;
import com.trigyn.jws.dynarest.vo.WebClientRequestVO;
import com.trigyn.jws.dynarest.vo.WebClientXMLVO;
import com.trigyn.jws.quartz.jobs.JwsMailScheduleJob;
import com.trigyn.jws.quartz.service.impl.JwsQuartzJobService;
import com.trigyn.jws.resourcebundle.service.ResourceBundleService;
import com.trigyn.jws.templating.service.DBTemplatingService;
import com.trigyn.jws.templating.utils.TemplatingUtils;
import com.trigyn.jws.templating.vo.TemplateVO;
import com.trigyn.jws.usermanagement.security.config.ApplicationSecurityDetails;
import com.trigyn.jws.usermanagement.security.config.JwsUserDetailsService;
import com.trigyn.jws.usermanagement.security.config.JwtUtil;
import com.trigyn.jws.usermanagement.vo.JwsAuthenticationType;
import com.trigyn.jws.usermanagement.vo.MultiAuthSecurityDetailsVO;

import freemarker.core.StopException;
import io.netty.handler.ssl.SslContextBuilder;
import net.minidev.json.JSONObject;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
@Service
@Transactional
@Lazy
public class JwsDynamicRestDetailService {

	private final static Logger				logger							= LogManager.getLogger(JwsDynamicRestDetailService.class);

	@Autowired
	private TemplatingUtils					templatingUtils					= null;

	@Autowired
	private JwsDynamicRestDAORepository		dynamicRestDAORepository		= null;

	@Autowired
	private JwsDynarestDAO					dynarestDAO						= null;

	@Autowired
	private JwsDynamicRestDetailsRepository	dyanmicRestDetailsRepository	= null;

	@Autowired
	private IUserDetailsService				detailsService					= null;

	@Autowired
	@Lazy
	private SendMailService					sendMailService					= null;

	@Autowired
	private ApplicationContext				applicationContext				= null;

	@Autowired
	@Qualifier("file-system-storage")
	private FilesStorageService				storageService					= null;

	@Autowired
	private DBTemplatingService				templatingService				= null;

	@Autowired
	@Lazy
	private JwtUtil							jwtUtil							= null;

	@Autowired
	@Lazy
	private JwsUserDetailsService			jwsUserDetailsService			= null;

	@Autowired
	@Lazy
	private UserDetailsService				userDetailsService				= null;

	@Autowired
	private ApplicationSecurityDetails		applicationSecurityDetails		= null;

	@Autowired
	private PropertyMasterService			propertyMasterService			= null;

	@Autowired
	ServletContext							servletContext					= null;

	@Autowired(required = false)
	private HttpServletRequest				request							= null;

	@Autowired
	private JwsDynamicRestDetailService		jwsService						= null;

	@Autowired
	private FileUploadController			fileUploadController			= null;

	@Autowired
	private ActivityLog						activitylog						= null;

	@Autowired
	private SessionLocaleResolver			sessionLocaleResolver			= null;
	
	@Autowired
	private ResourceBundleService			resourceBundleService			= null;

	@Autowired
	private JwsQuartzJobService				jobService						= null;
	
	protected NamedParameterJdbcTemplate	namedParameterJdbcTemplate		= null;

	private String	METHOD_SIGNATURE_MESSAGE;

	private String	FILE_METHOD_SIGNATURE_MESSAGE;
	
	public Object createSourceCodeAndInvokeServiceLogic(Map<String, FileInfo> files,
			HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Map<String, Object> requestParameterMap,
			Map<String, Object> daoResultSets, RestApiDetails restApiDetails) throws Exception, CustomStopException {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, Object> apiDetails = objectMapper.convertValue(restApiDetails, Map.class);
			requestParameterMap.putAll(apiDetails);
			
			ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
			ScriptEngine scriptEngine = null;
			scriptEngine = scriptEngineManager.getEngineByName(Constants.Platforms.getPlatformByID(restApiDetails.getPlatformId()).getPlatformName());
			switch(restApiDetails.getPlatformId()) {
			case Constants.JAVA:
				return invokeAndExecuteOnFileJava(files, httpServletRequest, requestParameterMap, restApiDetails);
			case Constants.FTL:
				return invokeAndExecuteFTL(files, httpServletRequest, requestParameterMap, daoResultSets,
						restApiDetails);
			case Constants.JAVASCRIPT:
				return invokeAndExecuteFileOnJavascript(files, httpServletRequest, httpServletResponse, requestParameterMap, daoResultSets,
						restApiDetails,scriptEngine);
			case Constants.PYTHON:
				return invokeAndExecuteFileOnPython(files, httpServletRequest, httpServletResponse, requestParameterMap, daoResultSets,
						restApiDetails,scriptEngine);
			case Constants.PHP:
				return invokeAndExecuteFileOnPHP(files, httpServletRequest, httpServletResponse, requestParameterMap, daoResultSets,
						restApiDetails,scriptEngine);
			 default:
				 return null;
			}

		} catch (CustomStopException custStopException) {
			logger.error("Error occured in createSourceCodeAndInvokeServiceLogic for Stop Exception.",
					custStopException);
			throw custStopException;
		}
	}

	private Object invokeAndExecuteFTL(Map<String, FileInfo> files, HttpServletRequest httpServletRequest,
			Map<String, Object> requestParameterMap, Map<String, Object> daoResultSets, RestApiDetails restApiDetails)
			throws Exception, CustomStopException {
		try {
			if (restApiDetails.getServiceLogic() != null
					|| Boolean.FALSE.equals("".equals(restApiDetails.getServiceLogic()))) {
				requestParameterMap.putAll(daoResultSets);
				if (null != files) {
					requestParameterMap.putAll(files);
				}

				Map<String, String> headerMap = new HashMap<>();
				Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
				if (headerNames != null) {
					while (headerNames.hasMoreElements()) {
						String header = headerNames.nextElement();
						headerMap.put(header, httpServletRequest.getHeader(header));
					}
				}
				requestParameterMap.put("requestHeaders", headerMap);
				requestParameterMap.put("session", httpServletRequest.getSession());
				StringBuilder resultStringBuilder = new StringBuilder();
				resultStringBuilder.append(restApiDetails.getServiceLogic());
				return templatingUtils.processTemplateContents(resultStringBuilder.toString(), "service",
						requestParameterMap);
			}
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in invokeAndExecuteFTL for Stop Exception.",
					custStopException);
			throw custStopException;
		}
		return null;
	}

	public Object invokeAndExecuteOnFileJava(Map<String, FileInfo> files, HttpServletRequest httpServletRequest,
			Map<String, Object> daoResultSets, RestApiDetails restApiDetails) throws Exception, ClassNotFoundException,
			NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, CustomStopException {
		Class<?>	serviceClass		= Class.forName(restApiDetails.getServiceLogic(), Boolean.TRUE,
				this.getClass().getClassLoader());
		Object		classInstance		= serviceClass.getDeclaredConstructor().newInstance();

		Method		serviceLogicMethod	= serviceClass.getDeclaredMethod(restApiDetails.getMethodName(),
				HttpServletRequest.class, Map.class, Map.class, UserDetailsVO.class);
		try {
			Method applicationContextMethod = serviceClass.getDeclaredMethod("setApplicationContext",
					ApplicationContext.class);
			applicationContextMethod.invoke(classInstance, applicationContext);
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in invokeAndExecuteOnFileJava for Stop Exception.",
					custStopException);
			throw custStopException;
		} catch (NoSuchMethodException a_nsme) {
			logger.warn(
					"No method found for setting application context. Create method setApplicationContext to set applicationContext",
					a_nsme);
		} catch (SecurityException a_se) {
			logger.error("Security exception occured while invoking setApplication context ", a_se);
		} catch (IllegalAccessException a_iae) {
			logger.error("IllegalAccessException occured while invoking setApplication context ", a_iae);
		} catch (IllegalArgumentException a_iae) {
			logger.error("IllegalArgumentException occured while invoking setApplication context ", a_iae);
		} catch (InvocationTargetException a_ite) {
			logger.error("InvocationTargetException occured while invoking setApplication context ", a_ite);
		}
		return serviceLogicMethod.invoke(classInstance, httpServletRequest, files, daoResultSets,
				detailsService.getUserDetails());
	}
	
	public Object invokeAndExecuteFileOnJavascript(Map<String, FileInfo> files, HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,
			Map<String, Object> requestParameterMap, Map<String, Object> daoResultSets, RestApiDetails restApiDetails,ScriptEngine scriptEngine)
			throws Exception, CustomStopException {
		
		if (scriptEngine == null) {
			logger.error("Nashorn Script Engine not found.");
			httpServletResponse.sendError(HttpStatus.PRECONDITION_FAILED.value(),
					"Nashorn Script Engine not found.");
			return null;
        }
		try {
			return scriptEngineExecution(files,httpServletRequest,requestParameterMap,daoResultSets,restApiDetails,scriptEngine);
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in invokeAndExecuteFileOnJavascript for Stop Exception.", custStopException);
			throw custStopException;
		}
	}
	
	public Object invokeAndExecuteFileOnPython(Map<String, FileInfo> files, HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,
			Map<String, Object> requestParameterMap, Map<String, Object> daoResultSets, RestApiDetails restApiDetails,ScriptEngine scriptEngine)
			throws Exception, CustomStopException {
		
		if (scriptEngine == null) {
			logger.error("Python Script Engine not found.");
			httpServletResponse.sendError(HttpStatus.PRECONDITION_FAILED.value(),
					"Python Script Engine not found.");
			return null;
        }
		try {
			return scriptEngineExecution(files,httpServletRequest,requestParameterMap,daoResultSets,restApiDetails,scriptEngine);
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in invokeAndExecuteFileOnPython for Stop Exception.", custStopException);
			throw custStopException;
		}
	}
	
	public Object invokeAndExecuteFileOnPHP(Map<String, FileInfo> files, HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,
			Map<String, Object> requestParameterMap, Map<String, Object> daoResultSets, RestApiDetails restApiDetails,ScriptEngine scriptEngine)
			throws Exception, CustomStopException {
		try {
	        if (scriptEngine == null) {
	        	logger.error("PHP Script Engine not found.");
	        	httpServletResponse.sendError(HttpStatus.PRECONDITION_FAILED.value(),
						"PHP Script Engine not found.");
				return null;
	        }
	        return scriptEngineExecution(files,httpServletRequest,requestParameterMap,daoResultSets,restApiDetails,scriptEngine);
	        
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in invokeAndExecuteFileOnPHP for Stop Exception.", custStopException);
			throw custStopException;
		}
	}
	
	public Object scriptEngineExecution(Map<String, FileInfo> files, HttpServletRequest httpServletRequest,
			Map<String, Object> requestParameterMap, Map<String, Object> daoResultSets, RestApiDetails restApiDetails,ScriptEngine scriptEngine)
			throws Exception, CustomStopException {
		
		StringBuilder resultStringBuilder = new StringBuilder();
		scriptEngine.put("requestDetails", requestParameterMap);
		scriptEngine.put("daoResults", daoResultSets);
		try {
			if ((httpServletRequest instanceof StandardMultipartHttpServletRequest) == false) {
				if (httpServletRequest != null) {
					BufferedReader reader = httpServletRequest.getReader();
					if (reader != null) {
						scriptEngine.put("requestBody", IOUtils.toString(reader));
					}
				}
			}
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in scriptEngineExecution for Stop Exception.", custStopException);
			throw custStopException;
		} catch (Exception excp) {
			logger.error("Error occured while invoking the method in" + restApiDetails.getDynamicRestUrl(), excp);
		}
		Map<String, String> headerMap = new HashMap<>();
		Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
			
		if (headerNames != null) {
			while (headerNames.hasMoreElements()) {
				String header = headerNames.nextElement();
				headerMap.put(header, httpServletRequest.getHeader(header));
			}
		}
		if(scriptEngine.getFactory().getLanguageName().equalsIgnoreCase("ECMAScript")) {
			TemplateVO		templateVO			= templatingService.getTemplateByName("script-util");
			resultStringBuilder.append(templateVO.getTemplate()).append("\n");
		}
		
		scriptEngine.put("httpRequestObject", httpServletRequest);
		scriptEngine.put("requestHeaders", headerMap);
		scriptEngine.put("session", httpServletRequest.getSession());

		if (files != null && files.size() > 0) {
			scriptEngine.put("files", files);
		}
		List<Object> objScriptLib = dynarestDAO.scriptLibExecution(restApiDetails.getDynamicId());
		for(int iCounter = 0; iCounter<objScriptLib.size(); iCounter++) {
			resultStringBuilder.append(objScriptLib.get(iCounter)).append("\n");
		}
		resultStringBuilder.append(restApiDetails.getServiceLogic()).append("\n");
		try {
		    StringWriter stringWriter = new StringWriter();
		    scriptEngine.getContext().setWriter(new PrintWriter(stringWriter));
	        Object scriptResult = scriptEngine.eval(resultStringBuilder.toString());
	        if(scriptEngine.getFactory().getLanguageName().equalsIgnoreCase("python")) {
	        	// If the method has a return statement
	        	if(scriptEngine.get("result") != null) {
	        		Object result = scriptEngine.get("result");
	        		return result;
	        	}
	        }else if(scriptEngine.getFactory().getLanguageName().equalsIgnoreCase("php")) {
	        	System.out.println("Sys Out is necessary for PHP"+scriptResult);
		        String result = stringWriter.toString();
		        return result;
	        } else {
	        	return scriptResult;
			}
	    } catch (ScriptException scrExc) {
	        logger.error("Error occured in scriptEngineExecution.", scrExc);
	    }
		return null;
	}

	public RestApiDetails getRestApiDetails(String requestUri) {
		return dyanmicRestDetailsRepository.findByJwsDynamicRestUrl(requestUri);
	}

	public RestApiDetails getRestApiDetailsById(String dynaId) {
		return dyanmicRestDetailsRepository.findAllJavaDynarestsByRestId(dynaId);
	}

	public Map<String, Object> executeDAOQueries(String dynarestId, Map<String, Object> parameterMap,
			Map<String, FileInfo> files) throws Exception, CustomStopException {
		List<RestApiDaoQueries>	apiDaoQueries	= dynamicRestDAORepository.getRestApiDaoQueriesByApiId(dynarestId);
		Map<String, Object>		resultSetMap	= new HashMap<>();
		for (RestApiDaoQueries restApiDaoQueries : apiDaoQueries) {
			String	dataSourceId	= restApiDaoQueries.getDataSourceId();
			Integer	queryType		= restApiDaoQueries.getQueryType();
			String	queryContent	= templatingUtils
					.processTemplateContents(restApiDaoQueries.getJwsDaoQueryTemplate(), "apiQuery", parameterMap);
			/* Added for Rest Client Attachment */
			if (files != null && files.size() > 0) {
				resultSetMap.put("files", files);
			}
			// Ends here
			if (Constants.QueryType.WC.getQueryType() == queryType) {
				CustomResponseEntity customResponseEntity = new CustomResponseEntity();
				try {
					JAXBContext		jaxbContext		= JAXBContext.newInstance(WebClientXMLVO.class);
					Unmarshaller	unmarshaller	= jaxbContext.createUnmarshaller();
					StringReader	reader			= new StringReader(queryContent);
					WebClientXMLVO	webClientXMLVO	= (WebClientXMLVO) unmarshaller.unmarshal(reader);
					if (webClientXMLVO.getWebClientURL() != null
							&& webClientXMLVO.getWebClientURL().equalsIgnoreCase("about:blank") == false) {
						Date	requestTime				= new Date();
						String	requestURLContextPath	= getAbsoluteContextPath();
						String	serverBaseURL			= getServerBaseURL();
						URI		requestedURL			= new URI(requestURLContextPath);
						URI		serverUrl				= new URI(serverBaseURL);
						if (requestedURL.compareTo(serverUrl) == 0) {
							String requestURL = webClientXMLVO.getWebClientURL().toString();
							if (requestURL.indexOf("/api/") > 0 || requestURL.indexOf("/japi/") > 0) {
								String schedularURL = propertyMasterService.findPropertyMasterValue("scheduler-url") + "-api";
								if (requestURL.indexOf("/api/") > 0) {
									String replaceUrl = requestURL.replaceAll("api", schedularURL);
									webClientXMLVO.setWebClientURL(replaceUrl);
								} else if (requestURL.indexOf("/japi/") > 0) {
									String replaceUrl = requestURL.replaceAll("japi", schedularURL);
									webClientXMLVO.setWebClientURL(replaceUrl);
								}
							}
						}
						Mono<ResponseEntity<String>>	responseContent	= getWebClientResponse(webClientXMLVO);
						ResponseEntity<String>			responseString	= responseContent.block();
						Date							responTime		= new Date();
						customResponseEntity = convertResponseToVO(responseString);
						customResponseEntity.setResponseDuration(responTime.getTime() - requestTime.getTime());
					}
					parameterMap.put(restApiDaoQueries.getJwsResultVariableName(), customResponseEntity);
					resultSetMap.put(restApiDaoQueries.getJwsResultVariableName(), customResponseEntity);
					
				} catch (CustomStopException custStopException) {
					logger.error("Error occured in executeDAOQueries for Stop Exception.",
							custStopException);
					throw custStopException;
				} catch (Throwable a_thr) {
					logger.error("Error occurred while establishing connection ::" + "in Rest API" + " : " + dynarestId,
							a_thr);
					String stacktrace = ExceptionUtils.getStackTrace(a_thr);
					customResponseEntity.setResponseBody(stacktrace);
					customResponseEntity.setResponseStatusCode(500);
					parameterMap.put(restApiDaoQueries.getJwsResultVariableName() + "_error",
							ExceptionUtils.getStackTrace(a_thr));
					resultSetMap.put(restApiDaoQueries.getJwsResultVariableName(), customResponseEntity);
				}
			} else {
				List<Map<String, Object>> resultSet = new ArrayList<>();
				if (Constants.QueryType.DML.getQueryType() == queryType) {
					try {
						Integer affectedRowCount = dynarestDAO.executeDMLQueries(dataSourceId, queryContent,
								parameterMap);
						resultSetMap.put(restApiDaoQueries.getJwsResultVariableName(), affectedRowCount);
						parameterMap.put(restApiDaoQueries.getJwsResultVariableName(), affectedRowCount);
					} catch (Throwable a_thr) {
						resultSetMap.put(restApiDaoQueries.getJwsResultVariableName(), -1);
						parameterMap.put(restApiDaoQueries.getJwsResultVariableName(), -1);
						resultSetMap.put(restApiDaoQueries.getJwsResultVariableName() + "_error",
								ExceptionUtils.getStackTrace(a_thr));
						parameterMap.put(restApiDaoQueries.getJwsResultVariableName() + "_error",
								ExceptionUtils.getStackTrace(a_thr));
					}
				} else {
					try {
						resultSet = dynarestDAO.executeQueries(dataSourceId, queryContent, parameterMap);
						resultSetMap.put(restApiDaoQueries.getJwsResultVariableName(), resultSet);
						parameterMap.put(restApiDaoQueries.getJwsResultVariableName(), resultSet);
					} catch (Throwable a_thr) {
						resultSetMap.put(restApiDaoQueries.getJwsResultVariableName(), new ArrayList<>());
						parameterMap.put(restApiDaoQueries.getJwsResultVariableName(), new ArrayList<>());
						resultSetMap.put(restApiDaoQueries.getJwsResultVariableName() + "_error",
								ExceptionUtils.getStackTrace(a_thr));
						parameterMap.put(restApiDaoQueries.getJwsResultVariableName() + "_error",
								ExceptionUtils.getStackTrace(a_thr));
					}
				}
			}
		}
		return resultSetMap;
	}
	public ResponseEntity<?> executeSendMail(Object emailXMLObj, Map<String, Object> requestParams)
			throws CustomStopException {
		String		emailXMLContent	= (String) emailXMLObj;
		JsonArray	jsonArray		= new JsonArray();
		String mailSenderGroupId = null;
		try {
			if(null == requestParams.get("mailSenderGroupId")) {
				mailSenderGroupId = UUID.randomUUID().toString();
			}else {
				mailSenderGroupId = (String) requestParams.get("mailSenderGroupId");
			}
			sendMailService.saveMailHistory(emailXMLContent, mailSenderGroupId);
			JobDataMap jobDataMap = new JobDataMap();
			jobDataMap.put("dynamicRestUrl", requestParams.get("dynamicRestUrl"));
			jobDataMap.put("mailSenderGroupId", mailSenderGroupId);
			//String paramJson = mapper.writeValueAsString(requestParams);
			//jobDataMap.put("requestParams", paramJson);
			String jobGroup = mailSenderGroupId;
			boolean status = jobService.scheduleOneTimeJob("sendMail", jobGroup, JwsMailScheduleJob.class,
					DateBuilder.evenMinuteDateAfterNow(), jobDataMap);
			if (status) {
				return ResponseEntity.status(HttpStatus.OK).body(jsonArray.toString());
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonArray.toString());
			}
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in executeSendMail.", custStopException);
			throw custStopException;
		} catch (JAXBException exception) {
			logger.error("Error occurred while unmarshalling XML string content ", exception);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Please provide valid XML content, received \r\n" + emailXMLContent);
		} catch (Throwable a_thr) {
			logger.error(
					"Error occurred while sending email in " + "Rest API" + " : " + requestParams.get("dynamicRestUrl"),
					a_thr);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while sending email");
		}
	}
	
	public CustomResponseEntity executeRestXML(String queryContent) throws CustomStopException{
		CustomResponseEntity customResponseEntity = new CustomResponseEntity();
		try {
			JAXBContext		jaxbContext		= JAXBContext.newInstance(WebClientXMLVO.class);
			Unmarshaller	unmarshaller	= jaxbContext.createUnmarshaller();
			StringReader	reader			= new StringReader(queryContent);

			WebClientXMLVO	webClientXMLVO	= (WebClientXMLVO) unmarshaller.unmarshal(reader);

			/** URL is mandatory. It can'be null or empty */
			if (webClientXMLVO.getWebClientURL().isEmpty() == true
					|| webClientXMLVO.getWebClientURL().equalsIgnoreCase("about:blank") == true) {
				// Handled null pointer exception
				throw new WebClientCustomException("URL is mandatory. It should not be empty or null.",
						HttpStatus.PRECONDITION_FAILED, "URL is mandatory. It should not be empty or null.");
			}
			/** Http Request Type is mandatory. It can't be null or empty */
			if (webClientXMLVO.getRequestType() == null || webClientXMLVO.getRequestType().isEmpty() == true) {
				// Handled null pointer exception
				throw new WebClientCustomException("HTTP Request Type is mandatory. It should not be empty or null.",
						HttpStatus.PRECONDITION_FAILED,
						"HTTP Request Type is mandatory. It should not be empty or null.");
			}

			if (webClientXMLVO.getWebClientURL() != null
					&& webClientXMLVO.getWebClientURL().equalsIgnoreCase("about:blank") == false) {
				Date							requestTime		= new Date();
				Mono<ResponseEntity<String>>	responseContent	= getWebClientResponse(webClientXMLVO);
				ResponseEntity<String>			responseString	= responseContent.block();
				Date							responTime		= new Date();
				customResponseEntity = convertResponseToVO(responseString);
				customResponseEntity.setResponseDuration(responTime.getTime() - requestTime.getTime());
			}
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in executeRestXML.", custStopException);
			throw custStopException;
		} catch (Throwable a_thr) {

			String stacktrace = ExceptionUtils.getStackTrace(a_thr);
			if (a_thr instanceof WebClientResponseException) {
				logger.error("Error occurred while establishing connection ", a_thr);
				WebClientResponseException wcre = (WebClientResponseException) a_thr;
				customResponseEntity.setResponseStatusCode(wcre.getStatusCode().value());
				customResponseEntity.setStatusCode(wcre.getStatusCode());
				customResponseEntity.setStatusText(wcre.getStatusText());
				customResponseEntity.setResponseBody(wcre.getResponseBodyAsString());
				customResponseEntity.setMessage(wcre.getMessage());
				customResponseEntity.setHeaders(wcre.getHeaders().toSingleValueMap());
			} else if (a_thr instanceof WebClientCustomException) {
				logger.error("Inside JwsDynamicRestDEtailService - Error occurred while processing data :: ", a_thr);
				WebClientCustomException wce = (WebClientCustomException) a_thr;
				customResponseEntity.setResponseBody(stacktrace);
				customResponseEntity.setResponseStatusCode(wce.getStatusCode().value());
				customResponseEntity.setStatusText(wce.getStatusText());
				customResponseEntity.setStatusCode(wce.getStatusCode());
				customResponseEntity.setMessage(wce.getMessage());
			} else if (a_thr instanceof UnmarshalException) {
				logger.error("Error while UnMarshalling in JwsDynamicRestDetailService :: " + a_thr);
				UnmarshalException une = (UnmarshalException) a_thr;
				customResponseEntity.setResponseBody(stacktrace);
				customResponseEntity.setResponseStatusCode(HttpStatus.PRECONDITION_FAILED.value());
				customResponseEntity.setStatusText(une.toString());
				customResponseEntity.setStatusCode(HttpStatus.PRECONDITION_FAILED);
				customResponseEntity.setMessage(une.getMessage());
			} else {
				logger.error("Error occurred while establishing connection ::", a_thr);
				customResponseEntity.setResponseBody(stacktrace);
				customResponseEntity.setResponseStatusCode(500);
				customResponseEntity.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);

			}
			customResponseEntity.setResponseTimestamp(new Date());
		}
		return customResponseEntity;
	}

	public ResponseEntity<?> loadDynamicRestDetails(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, RestApiDetails restApiDetails) throws Exception, CustomStopException {

		Map<String, FileInfo>	fileMap	= new HashMap<>();
		Map<String, Object>		requestParams;
		if (restApiDetails == null) {
			logActivity(restApiDetails, false, null);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND.getReasonPhrase(), HttpStatus.NOT_FOUND);
		}
		String requestType = httpServletRequest.getMethod();

		/*
		 * in case of all no need to check for method type match
		 * 
		 * @author: Suresh Mallisetty
		 */
		if (restApiDetails.getMethodTypeId() != null
				&& Constants.ALL_REQ_TYPE_ID.equals(restApiDetails.getMethodTypeId()) == false) {
			if (Boolean.FALSE.equals(requestType.equals(restApiDetails.getMethodType()))) {
				logActivity(restApiDetails, false, null);
				return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase(),
						HttpStatus.METHOD_NOT_ALLOWED);
			}
		}

		requestParams = validateAndProcessRequestParams(httpServletRequest, restApiDetails);
		try {
			Object		response		= null;
			FileInfo	fileInfoObject	= null;
			try {

				if (httpServletRequest instanceof StandardMultipartHttpServletRequest) {
					StandardMultipartHttpServletRequest	multipartRequest	= (StandardMultipartHttpServletRequest) httpServletRequest;
					int									iFileCounter		= 0;
					String								fileCopyPath		= propertyMasterService
							.findPropertyMasterValue("file-copy-path");
					for (Map.Entry<String, MultipartFile> uf : multipartRequest.getFileMap().entrySet()) {
						String		absolutePath	= fileCopyPath + File.separator + UUID.randomUUID().toString();
						FileInfo	fileInfo		= new FileInfo();
						fileInfo.setFileId(uf.getValue().getName());
						fileInfo.setFileName(uf.getValue().getOriginalFilename());
						fileInfo.setFileType(FileType.Physical);
						fileInfo.setSizeInBytes(uf.getValue().getSize());
						fileInfo.setAbsolutePath(absolutePath);
						fileInfo.setCreatedTime(new Date().getTime());
						fileMap.put("file" + (iFileCounter++), fileInfo);
						/**
						 * This needs to be changed in future. Below line is kept to support backward
						 * compatibility in HRS, as we have already supported code with key having
						 * uf.getKey().
						 * 
						 * uf.getKey() returns the input type file's id.
						 */
						fileMap.put(uf.getKey(), fileInfo);
						uf.getValue().transferTo(new File(absolutePath));
					}
				}

				Map<String, Object> queriesResponse = jwsService.executeDAOQueries(restApiDetails.getDynamicId(),
						requestParams, fileMap);

				response = jwsService.createSourceCodeAndInvokeServiceLogic(fileMap, httpServletRequest, httpServletResponse, requestParams,
						queriesResponse, restApiDetails);
				String responseType = restApiDetails.getReponseType();
				if (StringUtils.isBlank(responseType) == false && responseType.equals("email/xml")) {
					Map<String, Object> combParam = new HashMap<>();
					combParam.putAll(requestParams);
					combParam.putAll(queriesResponse);
					response = jwsService.executeSendMail(response, combParam);
				}

				if ((response instanceof FileInfo) == false
						&& restApiDetails.getReponseType().equalsIgnoreCase("application/octet-stream")) {
					if (response != null && "404".equalsIgnoreCase(response.toString())) {
						httpServletResponse.sendError(HttpStatus.NOT_FOUND.value(), "Invalid file information.");
						return new ResponseEntity<>(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND);
					} else {
						httpServletResponse.sendError(HttpStatus.PRECONDITION_FAILED.value(),
								"Invalid file information.");
						return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED, HttpStatus.PRECONDITION_FAILED);
					}
				}

				if (response instanceof FileInfo
						&& restApiDetails.getReponseType().equalsIgnoreCase("application/octet-stream")) {
					fileInfoObject = (FileInfo) response;
				}
				
				Map<String, String> getResourceBundledata = getResourceData(httpServletRequest);

				for (Entry<String, String> set : ((Map<String, String>) getResourceBundledata).entrySet()) {
					if (set.getKey().equalsIgnoreCase("jws.file_mtd_sign")) {
						FILE_METHOD_SIGNATURE_MESSAGE = set.getValue();
					} else if (set.getKey().equalsIgnoreCase("jws.mtd_signature")) {
						METHOD_SIGNATURE_MESSAGE = set.getValue();
					}
				}
			} catch (CustomStopException custStopException) {
				logger.error("Error occured in loadDynamicRestDetails for Stop Exception.", custStopException);
				throw custStopException;
			} catch (IllegalArgumentException a_exception) {
				logger.error("Error occured while invoking the method " + "Rest API" + " : "
						+ restApiDetails.getMethodName(), a_exception);
				httpServletResponse.sendError(HttpStatus.PRECONDITION_FAILED.value(), METHOD_SIGNATURE_MESSAGE);
			} catch (InvocationTargetException a_exception) {
				logger.error("Error occured while invoking the method " + "Rest API" + " : "
						+ restApiDetails.getMethodName(), a_exception);
			} catch (NoSuchMethodException a_exception) {
				logger.error("Error occured while invoking the method " + "Rest API" + " : "
						+ restApiDetails.getMethodName(), a_exception);
				httpServletResponse.sendError(HttpStatus.PRECONDITION_FAILED.value(), METHOD_SIGNATURE_MESSAGE);
			} catch (ClassNotFoundException a_exception) {
				logger.error("Error occured while invoking the method " + "Rest API" + " : "
						+ restApiDetails.getMethodName(), a_exception);
				httpServletResponse.sendError(HttpStatus.NOT_FOUND.value(),
						"The class was not found in the mentioned package.");
			}
			buildResponseEntity(httpServletRequest, httpServletResponse, restApiDetails);
			if (restApiDetails.getReponseType().equalsIgnoreCase("application/octet-stream")) {
				if (fileInfoObject == null) {
					httpServletResponse.sendError(HttpStatus.UNPROCESSABLE_ENTITY.value(),
							FILE_METHOD_SIGNATURE_MESSAGE);
					logActivity(restApiDetails, false, (String) requestParams.get("isFromRestAPI"));
					return new ResponseEntity<>(FILE_METHOD_SIGNATURE_MESSAGE, HttpStatus.UNPROCESSABLE_ENTITY);
				}

				String	filePathStr	= "", fileName = "", mimeType = "";
				byte[]	file;
				if (FileType.FileBin.equals(fileInfoObject.getFileType())) {
					Map<String, Object> fileBinMap = fileUploadController.getFileFromFileUploadId(
							fileInfoObject.getFileId(), httpServletRequest, httpServletResponse);
					fileName	= fileBinMap.get("fileName").toString();
					file		= (byte[]) fileBinMap.get("file");
					mimeType	= (String) fileBinMap.get("mimeType");
				} else {
					filePathStr	= fileInfoObject.getAbsolutePath();
					fileName	= fileInfoObject.getFileName();

					InputStream in = new FileInputStream(filePathStr);
					file = ByteStreams.toByteArray(in);
					in.close();
					if (fileInfoObject.getMimeType() != null) {
						mimeType = fileInfoObject.getMimeType();
					} else {
						mimeType = httpServletRequest.getSession().getServletContext().getMimeType(filePathStr);

					}
				}

				String downloadType = "attachment";
				if (fileInfoObject.getReturnAction() == 2) {
					downloadType = "inline";
				}

				httpServletResponse.setContentType(mimeType);
				httpServletResponse.setHeader("Content-Disposition", downloadType + "; filename=\"" + fileName + "\"");
				httpServletResponse.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
				httpServletResponse.setCharacterEncoding("utf-8");
				if (StringUtils.isBlank(httpServletResponse.getContentType()) == false) {
					httpServletResponse.flushBuffer();
				}

				InputStreamResource streamResource = new InputStreamResource(new ByteArrayInputStream(file));
				logActivity(restApiDetails, true, (String) requestParams.get("isFromRestAPI"));
				return new ResponseEntity<InputStreamResource>(streamResource, HttpStatus.OK);
			} else {
				if (StringUtils.isBlank(restApiDetails.getHeaderJson()) == false
						&& StringUtils.isBlank(httpServletResponse.getContentType()) == false) {
					httpServletResponse.flushBuffer();
				}
				logActivity(restApiDetails, true, (String) requestParams.get("isFromRestAPI"));
				if (response instanceof ResponseEntity<?>) {
					return (ResponseEntity<?>) response;
				}
				return new ResponseEntity<>(response, HttpStatus.OK);
			}

		} catch (CustomStopException custStopException) {
			logger.error("Error occured in loadDynamicRestDetails for Stop Exception.", custStopException);
			throw custStopException;
		} catch (Throwable a_throwable) {
			logActivity(restApiDetails, false, (String) requestParams.get("isFromRestAPI"));
			logger.error("Error occurred while processing request: " + "Rest API" + " : "
					+ restApiDetails.getDynamicRestUrl(), a_throwable);
			Objects.requireNonNull(a_throwable);
			Throwable rootCause = a_throwable;
			while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
				if (rootCause instanceof StopException) {
					return new ResponseEntity<>(((StopException) rootCause).getMessageWithoutStackTop(),
							HttpStatus.EXPECTATION_FAILED);
				}
				rootCause = rootCause.getCause();
			}
			if (a_throwable instanceof FileNotFoundException) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND.getReasonPhrase(), HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	public Map<String, String> getResourceData(HttpServletRequest httpServletRequest) {

		String localeId = sessionLocaleResolver.resolveLocale(httpServletRequest).toString();
		String keyInitials = "jws.file_mtd_sign,jws.mtd_signature";
		List<String> keyList = Arrays.asList(keyInitials.split(","));
		Object resourceKeyList;
		Map<String, String> resourceMap = new HashMap<>();
		try {
			resourceKeyList = resourceBundleService.getResourceBundleData(localeId, keyList);
			for (Entry<String, String> set : ((Map<String, String>) resourceKeyList).entrySet()) {
				resourceMap.put(set.getKey(), set.getValue());
			}
		} catch (Exception exception) {
			logger.error("Error occurred while processing request in : ", " getResourceData() : ", exception);
		}
		return resourceMap;
	}

	/**
	 * Purpose of this method is to log activities</br>
	 * in REST API EXECUTION Module.
	 * 
	 * @author                Bibhusrita.Nayak
	 * @param  restApiDetails
	 * @param  action
	 * @param  isFromRestAPI
	 * @throws Exception
	 */
	private void logActivity(RestApiDetails restApiDetails, Boolean action, String isFromRestAPI) throws Exception {
		Map<String, String>	requestParams	= new HashMap<>();
		UserDetailsVO		detailsVO		= detailsService.getUserDetails();
		requestParams.put("entityName", restApiDetails.getDynamicRestUrl());
		Date activityTimestamp = new Date();
		if (action == true) {
			requestParams.put("action", Constants.Action.APIEXECUTED.getAction());
		} else if (action == false) {
			requestParams.put("action", Constants.Action.APIEXECFAILED.getAction());
		}
		JwsDynamicRestDetail query = jwsService.getDynamicRestDetailsByName(restApiDetails.getMethodName());
		if (query.getJwsDynamicRestTypeId() == Constants.Changetype.CUSTOM.getChangeTypeInt()) {
			requestParams.put("typeSelect", Constants.Changetype.CUSTOM.getChangetype());
		} else if (query.getJwsDynamicRestTypeId() == Constants.Changetype.SYSTEM.getChangeTypeInt()) {
			requestParams.put("typeSelect", Constants.Changetype.SYSTEM.getChangetype());
		}
		requestParams.put("masterModuleType", Constants.Modules.DYNAMICREST.getModuleName());
		requestParams.put("userName", detailsVO.getUserName());
		requestParams.put("message", "");
		requestParams.put("date", activityTimestamp.toString());
		if (isFromRestAPI != null && ("false".equalsIgnoreCase(isFromRestAPI)) == false) {
			activitylog.activitylog(requestParams, isFromRestAPI);
		}
	}

	public Map<String, Object> validateAndProcessRequestParams(HttpServletRequest httpServletRequest,
			RestApiDetails restDetails) {
		Map<String, Object> requestParams = new HashMap<>();

		for (String requestParamKey : httpServletRequest.getParameterMap().keySet()) {

			requestParams.put(requestParamKey, httpServletRequest.getParameter(requestParamKey));
		}
		return requestParams;
	}

	private void buildResponseEntity(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			RestApiDetails restDetails) throws IOException {
		httpServletResponse.setHeader("Content-Type", restDetails.getReponseType());
		String localeId = sessionLocaleResolver.resolveLocale(httpServletRequest).toString();
		httpServletResponse.setHeader("Content-Language", localeId);

		Gson gson = new Gson();
		if (StringUtils.isBlank(restDetails.getHeaderJson()) == false) {
			Map<String, String> headerConfig = gson.fromJson(restDetails.getHeaderJson(), Map.class);
			headerConfig.forEach((key, value) -> {
				if ("Content-Language".equals(key) == false) {
					httpServletResponse.setHeader(key, value);
				}
			});
		}

		if (httpServletResponse.containsHeader("Powered-By") == false) {
			httpServletResponse.setHeader("Powered-By", "JQuiver");
		}

	}

	private Mono<ResponseEntity<String>> getWebClientResponse(WebClientXMLVO webClientVO) throws Exception {

		Integer	responseTimeout		= webClientVO.getResponseTimeOut() != null ? webClientVO.getResponseTimeOut()
				: Constants.DEFAULT_RESPONSE_TIMEOUT;
		Integer	sslHandshakeTimeout	= webClientVO.getSslHandshakeTimeout() != null ? webClientVO.getResponseTimeOut()
				: Constants.SSL_HANDSHAKE_TIMEOUT;
		Integer	sslFlushTimeout		= webClientVO.getSslFlushTimeout() != null ? webClientVO.getResponseTimeOut()
				: Constants.SSL_CLOSE_NOTIFY_FLUSH_TIMEOUT;
		Integer	sslReadTimeout		= webClientVO.getResponseTimeOut() != null ? webClientVO.getResponseTimeOut()
				: Constants.SSL_CLOSE_NOTIFY_READ_TIMEOUT;

		/** The value of response timeout should not be empty. */
		if (responseTimeout == 0) {
			throw new WebClientCustomException("Response Timeout value can't be empty.", HttpStatus.PRECONDITION_FAILED,
					"Response Timeout value can't be empty.");
		}

		HttpClient			httpClient			= HttpClient.create().secure(spec -> {
													try {
														spec.sslContext(SslContextBuilder.forClient().build())
																.handshakeTimeout(
																		Duration.ofSeconds(sslHandshakeTimeout))
																.closeNotifyFlushTimeout(
																		Duration.ofSeconds(sslFlushTimeout))
																.closeNotifyReadTimeout(
																		Duration.ofSeconds(sslReadTimeout));
													} catch (SSLException exception) {
														logger.debug("SSLException ", exception);
													}
												})
				.responseTimeout(Duration.ofSeconds(responseTimeout));
		ClientHttpConnector	connector			= new ReactorClientHttpConnector(httpClient.wiretap(true));

		Builder				webClientBuilder	= WebClient.builder().defaultHeader(HttpHeaders.USER_AGENT, "JQuiver");
		WebClientRequestVO	webClientRequestVO	= webClientVO.getWebClientRequestVO();
		String				rawBody				= "";
		boolean				hasHeader			= false;
		boolean				isJsonContentType	= false;
		boolean				hasAttachment		= false;

		if (webClientRequestVO != null) {
			if (CollectionUtils.isEmpty(webClientRequestVO.getHeaderParamVOList()) == false) {
				for (WebClientParamVO paramVO : webClientRequestVO.getHeaderParamVOList()) {
					if ((paramVO.getParameterName() == null || paramVO.getParameterName().isEmpty() == true)
							|| (paramVO.getParameterValue() == null )) {
						// Handled null pointer exception
						throw new WebClientCustomException("Parameter name or value can't be null",
								HttpStatus.PRECONDITION_FAILED, "Parameter name or value can't be null");
					}

					if (HttpHeaders.AUTHORIZATION.equals(paramVO.getParameterName())
							|| "NOAUTHORIZATION".equals(paramVO.getParameterName())) {
						hasHeader = true;
					} else if ("content-type".equals(paramVO.getParameterName().toLowerCase())
							&& paramVO.getParameterValue().toLowerCase().contains("json")) {
						isJsonContentType = true;
					}
				}
			}
		}

		Integer authType = getOneAuthenticationId();

		if (hasHeader == false && authType != null
				&& (authType == com.trigyn.jws.usermanagement.utils.Constants.AuthType.DAO.getAuthType()
						|| authType == com.trigyn.jws.usermanagement.utils.Constants.AuthType.LDAP.getAuthType()
						|| authType == com.trigyn.jws.usermanagement.utils.Constants.AuthType.OAUTH.getAuthType())) {

			webClientBuilder.defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwtUtil.generateToken(
					userDetailsService.loadUserByUsername(jwsUserDetailsService.getUserDetails().getUserName())));

		}

		webClientBuilder.filter(logRequest()).clientConnector(connector);
		JSONObject						json						= new JSONObject();
		MultiValueMap<String, String>	bodyMultipvalueMap			= new LinkedMultiValueMap<String, String>();
		MultiValueMap<String, String>	queryParamMultipvalueMap	= new LinkedMultiValueMap<String, String>();
		Mono<ResponseEntity<String>>	responseContent				= null;
		MultipartBodyBuilder			builder						= new MultipartBodyBuilder();

		if (webClientRequestVO != null) {
			/** Added null check for Parameter tags */
			List<WebClientParamVO> webClientParamList = webClientRequestVO.getHeaderParamVOList();
			if (CollectionUtils.isEmpty(webClientParamList) == false) {
				for (WebClientParamVO headerParam : webClientParamList) {
					if ((headerParam.getParameterName() == null || headerParam.getParameterName().isEmpty() == true)
							|| (headerParam.getParameterValue() == null)) {
						// Handled null pointer exception
						throw new WebClientCustomException("Parameter name or value can't be null",
								HttpStatus.PRECONDITION_FAILED, "Parameter name or value can't be null");
					} else {

						webClientBuilder.defaultHeader(headerParam.getParameterName(), headerParam.getParameterValue());
					}
				}
			}

			/**
			 * Added for Exception Handling when Body content-type is raw and attachment is
			 * present which ideally is not supported. So, an Exception is thrown in this
			 * situation.
			 */
			if (webClientRequestVO.getBody() != null) {
				if ("rawBody".equals(webClientRequestVO.getBody().getContentType())
						&& CollectionUtils.isEmpty(webClientRequestVO.getAttachmnetParamVOList()) == false) {
					throw new WebClientCustomException("Cannot send raw data with file attachment",
							HttpStatus.PRECONDITION_FAILED, "Raw data can't be sent with file attachment");

				} else if (("rawBody".equals(webClientRequestVO.getBody().getContentType()) == false)
						&& CollectionUtils.isEmpty(webClientRequestVO.getBody().getBodyParamVOList()) == false) {
					List<WebClientParamVO> bodyParamVOList = webClientRequestVO.getBody().getBodyParamVOList();
					for (WebClientParamVO webClientParamVO : bodyParamVOList) {
						/**
						 * Added for Exception Handling, when body content-type is not rawBody, but
						 * Parameter name is set to "data-raw". Parameter name should be set to
						 * "data-raw" only when body content-type is " rawBody".
						 */
						if ("data-raw".equals(webClientParamVO.getParameterName())) {
							throw new WebClientCustomException(
									"Cannot set parameter name as data-raw, if body content-type is keyValue or null/no-mention",
									HttpStatus.PRECONDITION_FAILED,
									"Cannot set parameter name as data-raw, if body content-type is keyValue or null/no-mention.");
						} /** Ends Here */
					}

				} else if ("rawBody".equals(webClientRequestVO.getBody().getContentType())
						&& CollectionUtils.isEmpty(webClientRequestVO.getBody().getBodyParamVOList()) == false) {
					List<WebClientParamVO> bodyParamVOList = webClientRequestVO.getBody().getBodyParamVOList();
					for (WebClientParamVO webClientParamVO : bodyParamVOList) {
						/**
						 * If Body content-type is "rawBody", and parameter name is not set to
						 * "data-raw", then an exception is thrown.
						 */
						if ("data-raw".equals(webClientParamVO.getParameterName()) == false) {
							throw new WebClientCustomException(
									"Parameter name should be set as data-raw when body content-type is rawBody",
									HttpStatus.PRECONDITION_FAILED,
									"Parameter name should be set as data-raw when body content-type is rawBody.");
						}
					}
				}
			}
			/** Ends Here */

			if (webClientRequestVO.getBody() != null) {
				if ("rawBody".equals(webClientRequestVO.getBody().getContentType())) {
					List<WebClientParamVO> bodyParamVOList = webClientRequestVO.getBody().getBodyParamVOList();
					if (bodyParamVOList != null) {
						for (WebClientParamVO webClientParamVO : bodyParamVOList) {
							rawBody = webClientParamVO.getParameterValue();
						}
					}

				} else if (webClientRequestVO.getBody().getContentType() == null
						|| webClientRequestVO.getBody().getContentType().isEmpty()
						|| "keyValue".equals(webClientRequestVO.getBody().getContentType())) {
					List<WebClientParamVO> bodyParamVOList = webClientRequestVO.getBody().getBodyParamVOList();
					if (bodyParamVOList != null) {
						for (WebClientParamVO webClientParamVO : bodyParamVOList) {
							/** In Body the parameter name or parameter value can't be null or empty */
							if ((webClientParamVO.getParameterName() == null
									|| webClientParamVO.getParameterName().isEmpty() == true)
									|| (webClientParamVO.getParameterValue() == null)) {
								// Handled null pointer exception
								throw new WebClientCustomException("Parameter name or value can't be null",
										HttpStatus.PRECONDITION_FAILED, "Parameter name or value can't be null");
							} else {
								List<String> list = new ArrayList<>();
								list.add(webClientParamVO.getParameterValue());
								bodyMultipvalueMap.put(webClientParamVO.getParameterName(), list);
								builder.part(webClientParamVO.getParameterName(), webClientParamVO.getParameterValue(),
										MediaType.TEXT_PLAIN);

								if ("json".equals(webClientParamVO.getDataType())) {
									org.json.JSONObject jsonObj = new org.json.JSONObject(
											webClientParamVO.getParameterValue());
									json.put(webClientParamVO.getParameterName(), jsonObj);
								} else {
									json.put(webClientParamVO.getParameterName(), webClientParamVO.getParameterValue());
								}
							}
						}
					}
				}
			}

			if (CollectionUtils.isEmpty(webClientRequestVO.getQueryParamVOList()) == false) {
				for (WebClientParamVO webClientParamVO : webClientRequestVO.getQueryParamVOList()) {
					/**
					 * In query-param, the parameter name or parameter value can't be null or empty
					 */
					if ((webClientParamVO.getParameterName() == null
							|| webClientParamVO.getParameterName().isEmpty() == true)
							|| (webClientParamVO.getParameterValue() == null)) {
						// Handled null pointer exception
						throw new WebClientCustomException("Parameter name or value can't be null",
								HttpStatus.PRECONDITION_FAILED, "Parameter name or value can't be null");
					} else {
						List<String> list = new ArrayList<>();
						list.add(webClientParamVO.getParameterValue());
						queryParamMultipvalueMap.put(webClientParamVO.getParameterName(), list);
					}
				}
			}
			/* Added for Rest Client Attachment */
			byte[]				fileByte		= null;
			File				attachedFile	= null;
			Map<String, Object>	fileInfo		= new HashMap<>();
			Map<String, File>	fileMap			= new HashMap<>();
			if (CollectionUtils.isEmpty(webClientRequestVO.getAttachmnetParamVOList()) == false) {
				for (WebClientAttacmentVO webClientAttachVO : webClientRequestVO.getAttachmnetParamVOList()) {
					hasAttachment = true;
					/** In file attachment, the file type can't be null or empty */
					if (null == webClientAttachVO.getType() || webClientAttachVO.getType().equals("")) {
						throw new WebClientCustomException("File Type can't be null", HttpStatus.PRECONDITION_FAILED,
								"File Type can't be null");
					}

					/** In file attachment, the file name or file path can't be null or empty */
					if ((webClientAttachVO.getFileName() == null || webClientAttachVO.getFileName().isEmpty() == true)
							|| (webClientAttachVO.getFilePath() == null
									|| webClientAttachVO.getFilePath().isEmpty() == true)) {
						throw new WebClientCustomException("File name or path can't be null",
								HttpStatus.PRECONDITION_FAILED, "File name or path can't be null");
					}

					// This is File Bin
					if (webClientAttachVO.getType().equals(Constants.FILE_ATTACHMENT_FILEBIN)) {
						String	fileUploadId	= webClientAttachVO.getFilePath();
						Integer	isAllowed		= storageService.hasPermission(null, null, fileUploadId,
								Constants.VIEW_FILE_VALIDATOR, new HashMap<>());
						if (isAllowed > 0) {
							fileInfo = storageService.load(fileUploadId);
							if (fileInfo != null) {
								fileByte		= (byte[]) fileInfo.get("file");
								attachedFile	= new File(webClientAttachVO.getFileName());
								try (FileOutputStream fos = new FileOutputStream(attachedFile)) {
									fos.write(fileByte);
									fileMap.put(webClientAttachVO.getFileName(), attachedFile);
								} catch (Exception exception) {
									logger.error("Error occurred while accessing file in : "
											+ webClientVO.getWebClientURL() + " File UploadId : " + fileUploadId,
											exception);
								}
							}
						}
					} else if (webClientAttachVO.getType().equals(Constants.FILE_ATTACHMENT_FILESYSTEM)
							|| webClientAttachVO.getType().equals(Constants.FILE_ATTACHMENT_UPLOADEDFILE)) { // This is
																												// filesystem.
						File aFile = new File(webClientAttachVO.getFilePath());
						if (aFile != null && aFile.exists()) {
							attachedFile = new File(aFile.getName());
							InputStream in = new FileInputStream(webClientAttachVO.getFilePath());
							fileByte = ByteStreams.toByteArray(in);
							in.close();
							try (FileOutputStream fos = new FileOutputStream(attachedFile)) {
								fos.write(fileByte);
								fileMap.put(webClientAttachVO.getFileName(), attachedFile);
							} catch (Exception exception) {
								logger.error(
										"Error occurred while accessing file in : " + webClientVO.getWebClientURL(),
										exception);
							}
						}
					}
				}
			}
			WebClient			webClient		= webClientBuilder.exchangeStrategies(ExchangeStrategies.builder()
					.codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize(10000000))
					.build()).build();
			RequestHeadersSpec	reqHeaderSpec	= null;
			RequestBodySpec		reqBodySpec		= webClient.method(HttpMethod.resolve(webClientVO.getRequestType()))
					.uri(webClientVO.getWebClientURL(), uri -> uri.queryParams(queryParamMultipvalueMap).build());
			if (hasAttachment == true) {
				for (Entry<String, File> entry : fileMap.entrySet()) {
					try {
						File		file	= entry.getValue();
						InputStream	in		= new FileInputStream(file);
						fileByte = ByteStreams.toByteArray(in);
						builder.part(entry.getKey(), new FileSystemResource(file.getAbsolutePath()))
								.filename(file.getPath());

					} catch (Exception exception) {
						logger.error("Error occurred while accessing file in : " + webClientVO.getWebClientURL(),
								exception);
					}
				}

				reqHeaderSpec = reqBodySpec.contentType(MediaType.MULTIPART_FORM_DATA)
						.body(BodyInserters.fromMultipartData(builder.build()));
			} else if (webClientRequestVO.getBody() != null
					&& "rawBody".equals(webClientRequestVO.getBody().getContentType())) {
				reqHeaderSpec = reqBodySpec.bodyValue(rawBody);
			} else if (isJsonContentType == true) {
				reqHeaderSpec = reqBodySpec.bodyValue(json);
			} else {
				reqHeaderSpec = reqBodySpec.bodyValue(bodyMultipvalueMap);
			}
			responseContent = reqHeaderSpec.retrieve().toEntity(String.class);
			/**
			 * Below code is commented because we are not able to handle the exception from
			 * the urls accessed through this request. Since we are trying to convert
			 * CustomRuntimeException to String.class, the system is giving us an unparsable
			 * error, from which we are not able to get the client response code. Below code
			 * should be uncommented, after discussion only.
			 */
			// .onStatus(HttpStatus::is4xxClientError, response -> {
			// return response.bodyToMono(CustomRuntimeException.class).flatMap(error -> {
			// return Mono.error(new CustomRuntimeException(error));
			// });
			// }).onStatus(HttpStatus::is5xxServerError, response -> {
			// return response.bodyToMono(CustomRuntimeException.class).flatMap(error -> {
			// return Mono.error(new CustomRuntimeException(error));
			// });
			// }).toEntity(String.class);
		}

		return responseContent;
	}

	private CustomResponseEntity convertResponseToVO(ResponseEntity<String> responseString) {
		CustomResponseEntity customResponseEntity = new CustomResponseEntity();
		try {
			if (null != responseString.getBody()) {
				String				responseBody		= responseString.getBody();
				Integer				responseStatusCode	= responseString.getStatusCode().value();
				Map<String, String>	headerMap			= responseString.getHeaders().toSingleValueMap();
				customResponseEntity.setResponseStatusCode(responseStatusCode);
				customResponseEntity.setResponseBody(responseBody);
				customResponseEntity.setHeaders(headerMap);
				customResponseEntity.setResponseTimestamp(new Date());
			}
		} catch (Throwable a_thr) {
			logger.error("Error occured in convertResponseToVO. ", a_thr);
		}
		return customResponseEntity;
	}

	private ExchangeFilterFunction logRequest() {
		return (clientRequest, next) -> {
			logger.info("Request: {} {}", clientRequest.method(), clientRequest.url());
			clientRequest.headers()
					.forEach((name, values) -> values.forEach(value -> logger.info("{}={}", name, value)));
			return next.exchange(clientRequest);
		};
	}

	private HttpServletRequest getRequest() {
		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (sra != null) {
			return sra.getRequest();
		}
		return null;
	}

	private Integer getOneAuthenticationId() {

		Map<String, Object>	authenticationDetails	= applicationSecurityDetails.getAuthenticationDetails();
		Boolean				isAuthenticationEnabled	= (Boolean) authenticationDetails.get("isAuthenticationEnabled");
		if (authenticationDetails.isEmpty() == false && isAuthenticationEnabled) {
			List<MultiAuthSecurityDetailsVO> multiAuthSecurityDetails = (List<MultiAuthSecurityDetailsVO>) authenticationDetails
					.get("authenticationDetails");
			for (MultiAuthSecurityDetailsVO multiAuthSecurityDetailsVO : multiAuthSecurityDetails) {
				JwsAuthenticationType authenticationType = multiAuthSecurityDetailsVO.getConnectionDetailsVO()
						.getAuthenticationType();
				if (authenticationType != null) {
					if (authenticationType.getValue().equalsIgnoreCase("true")) {
						return multiAuthSecurityDetailsVO.getAuthenticationTypeVO().getId();
					}
				}
			}
		}
		return null;
	}

	public JwsDynamicRestDetail getDynamicRestDetailsByName(String jwsMethodName) {
		return dynarestDAO.getDynamicRestDetailsByName(jwsMethodName);
	}

	public String getServerBaseURL() throws Exception {
		String baseURL = propertyMasterService.findPropertyMasterValue("base-url");
		if (servletContext.getContextPath().isBlank() == false) {
			baseURL = baseURL + servletContext.getContextPath();
		}
		return baseURL;
	}

	public String getAbsoluteContextPath() {
		String	scheme		= request.getScheme();
		String	serverName	= request.getServerName();
		int		serverPort	= request.getServerPort();
		String	contextPath	= request.getContextPath();
		String	resultPath	= scheme + "://" + serverName + ":" + serverPort + contextPath;
		return resultPath;

	}
}
