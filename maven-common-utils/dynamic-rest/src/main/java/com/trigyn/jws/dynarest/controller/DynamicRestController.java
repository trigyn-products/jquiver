package com.trigyn.jws.dynarest.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.trigyn.jws.dbutils.vo.FileInfo;
import com.trigyn.jws.dynarest.service.JwsDynamicRestDetailService;
import com.trigyn.jws.dynarest.vo.RestApiDetails;
import com.trigyn.jws.usermanagement.security.config.Authorized;
import com.trigyn.jws.usermanagement.utils.Constants;

import freemarker.core.StopException;

@RestController
public class DynamicRestController {

	private static final Logger			LOGGER							= LogManager.getLogger(DynamicRestController.class);

	private static final String			METHOD_SIGNATURE_MESSAGE		= "Make sure you have the method signature correct. Signature should be similar to : - public T methodName(HttpServletRequest request, Map<String, Object> requestParameters, Map<String, Object> resultSetParameters, UserDetailsVO, details) {}";

	private static final String			FILE_METHOD_SIGNATURE_MESSAGE	= "Make sure you have the method signature correct. Signature should be similar to : - public FileInfo methodName(MultipartFile [] files, HttpServletRequest request, Map<String, Object> requestParameters, Map<String, Object> resultSetParameters, UserDetailsVO, details) {}";

	@Autowired
	private JwsDynamicRestDetailService	jwsService						= null;

	@RequestMapping(value = { "/api/**", "/japi/**" })
	@Authorized(moduleName = Constants.DYNAMICREST)
	@ResponseBody
	public ResponseEntity<?> callDynamicEntity(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		String requestUri = httpServletRequest.getRequestURI().substring(httpServletRequest.getContextPath().length());
		if (requestUri.startsWith("/japi/")) {
			requestUri = requestUri.replaceFirst("/japi/", "");
		} else {
			requestUri = requestUri.replaceFirst("/api/", "");
		}
		RestApiDetails		restApiDetails	= jwsService.getRestApiDetails(requestUri);
		Map<String, Object>	requestParams;

		if (restApiDetails == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND.getReasonPhrase(), HttpStatus.NOT_FOUND);
		}

		String requestType = httpServletRequest.getMethod();

		if (Boolean.FALSE.equals(requestType.equals(restApiDetails.getMethodType()))) {
			return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase(), HttpStatus.METHOD_NOT_ALLOWED);
		}

		requestParams = validateAndProcessRequestParams(httpServletRequest, restApiDetails);

		try {
			Map<String, Object>	queriesResponse	= jwsService.executeDAOQueries(restApiDetails.getDataSourceId(),
					restApiDetails.getDynamicId(), requestParams);
			Object				response		= null;
			try {
				response = jwsService.createSourceCodeAndInvokeServiceLogic(httpServletRequest, requestParams, queriesResponse,
						restApiDetails);
			} catch (IllegalArgumentException a_exception) {
				LOGGER.error("Error occured while invoking the method ", a_exception);
				httpServletResponse.sendError(HttpStatus.PRECONDITION_FAILED.value(), METHOD_SIGNATURE_MESSAGE);
			} catch (InvocationTargetException a_exception) {
				LOGGER.error("Error occured while invoking the method ", a_exception);
			} catch (NoSuchMethodException a_exception) {
				LOGGER.error("Error occured while invoking the method ", a_exception);
				httpServletResponse.sendError(HttpStatus.PRECONDITION_FAILED.value(), METHOD_SIGNATURE_MESSAGE);
			} catch (ClassNotFoundException a_exception) {
				LOGGER.error("Error occured while invoking the method ", a_exception);
				httpServletResponse.sendError(HttpStatus.NOT_FOUND.value(), "The class was not found in the mentioned package.");
			}
			buildResponseEntity(httpServletResponse, restApiDetails);
			if (response instanceof ResponseEntity<?>) {
				return (ResponseEntity<?>) response;
			}
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (StopException a_exception) {
			return new ResponseEntity<>(a_exception.getMessageWithoutStackTop(), HttpStatus.EXPECTATION_FAILED);
		} catch (Throwable a_throwable) {

			LOGGER.error("Error occurred while processing request: ", a_throwable);
			Objects.requireNonNull(a_throwable);
		    Throwable rootCause = a_throwable;
		    while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
		    	if(rootCause instanceof StopException) {
		    		return new ResponseEntity<>(((StopException)rootCause).getMessageWithoutStackTop(), HttpStatus.EXPECTATION_FAILED);
		    	}
		        rootCause = rootCause.getCause();
		    }
			
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/file/api/**")
	public ResponseEntity<?> callDynamicFile(@RequestParam("files") MultipartFile[] files, HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse) {
		String requestUri = httpServletRequest.getRequestURI().substring(httpServletRequest.getContextPath().length());
		requestUri = requestUri.replaceFirst("/file/api/", "");
		RestApiDetails		restApiDetails	= jwsService.getRestApiDetails(requestUri);
		Map<String, Object>	requestParams	= new HashMap<String, Object>();

		if (restApiDetails == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND.getReasonPhrase(), HttpStatus.NOT_FOUND);
		}

		requestParams = validateAndProcessRequestParams(httpServletRequest, restApiDetails);

		try {
			Map<String, Object>	queriesResponse	= jwsService.executeDAOQueries(restApiDetails.getDataSourceId(),
					restApiDetails.getDynamicId(), requestParams);
			FileInfo			fileInfo		= null;
			try {
				Object response = jwsService.invokeAndExecuteOnFileJava(files, httpServletRequest, queriesResponse, restApiDetails);
				if (response instanceof FileInfo) {
					fileInfo = (FileInfo) response;
				} else {
					LOGGER.error("Error occured while getting the file response ");
					httpServletResponse.sendError(HttpStatus.UNPROCESSABLE_ENTITY.value(), FILE_METHOD_SIGNATURE_MESSAGE);
				}
			} catch (IllegalArgumentException a_exception) {
				LOGGER.error("Error occured while invoking the method ", a_exception);
				httpServletResponse.sendError(HttpStatus.PRECONDITION_FAILED.value(), FILE_METHOD_SIGNATURE_MESSAGE);
			} catch (InvocationTargetException a_exception) {
				LOGGER.error("Error occured while invoking the method ", a_exception);
			} catch (NoSuchMethodException a_exception) {
				LOGGER.error("Error occured while invoking the method ", a_exception);
				httpServletResponse.sendError(HttpStatus.PRECONDITION_FAILED.value(), FILE_METHOD_SIGNATURE_MESSAGE);
			} catch (ClassNotFoundException a_exception) {
				LOGGER.error("Error occured while invoking the method ", a_exception);
				httpServletResponse.sendError(HttpStatus.NOT_FOUND.value(), "The class was not found in the mentioned package.");
			}
			return new ResponseEntity<>(fileInfo, HttpStatus.OK);
		} catch (Exception a_exception) {
			LOGGER.error("Error occurred while processing request: ", a_exception);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private Map<String, Object> validateAndProcessRequestParams(HttpServletRequest httpServletRequest, RestApiDetails restDetails) {
		Map<String, Object> requestParams = new HashMap<>();
		for (String requestParamKey : httpServletRequest.getParameterMap().keySet()) {
			requestParams.put(requestParamKey, httpServletRequest.getParameter(requestParamKey));
		}
		return requestParams;
	}

	private void buildResponseEntity(HttpServletResponse httpServletResponse, RestApiDetails restDetails) {
		httpServletResponse.setHeader("content-type", restDetails.getReponseType());
	}
}
