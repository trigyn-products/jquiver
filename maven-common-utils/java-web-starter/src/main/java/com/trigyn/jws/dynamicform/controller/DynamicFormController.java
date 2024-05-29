package com.trigyn.jws.dynamicform.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.Gson;
import com.trigyn.jws.dbutils.utils.CustomStopException;
import com.trigyn.jws.dynamicform.service.DynamicFormService;
import com.trigyn.jws.dynarest.service.FilesStorageService;
import com.trigyn.jws.usermanagement.security.config.Authorized;
import com.trigyn.jws.usermanagement.utils.Constants;

import freemarker.core.StopException;

@RestController
@RequestMapping("/cf")
public class DynamicFormController {
 
	private static final Logger	logger				= LogManager.getLogger(DynamicFormController.class);

	@Autowired
	private DynamicFormService	dynamicFormService	= null;

	@Autowired
	private FilesStorageService	filesStorageService	= null;
	
	@PostMapping("/df")
	@Authorized(moduleName = Constants.DYNAMICFORM)
	public String loadDynamicForm(@RequestParam(value = "formId", required = true) String formId,
			HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception, CustomStopException {
		logger.debug("Inside DynamicFormController.loadDynamicForm(gridId: {})",
				httpServletRequest.getParameter("gridId"));
		try {
			return dynamicFormService.loadDynamicForm(formId, processRequestParams(httpServletRequest), null);
			
		} catch (CustomStopException custStopException) {
			logger.error("Error occured while loading dynamic form.", custStopException);
			throw custStopException;	
		} catch (Exception exception) {
			logger.error("Error occured while loading dynamic form (formId: {})", formId, exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
			return null;
		}
	}

	@Deprecated
	@PostMapping(value = "/sdf", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@Authorized(moduleName = Constants.DYNAMICFORM)
	public Boolean saveDynamicForm(@RequestBody MultiValueMap<String, String> formData,HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws Exception {
		logger.debug("Inside DynamicFormController.saveDynamicForm(formData: {})", formData.getFirst("formId"));
		try {
			return dynamicFormService.saveDynamicForm(formData);
		} catch (Exception exception) {
			logger.error("Error occured while saving dynamic form (formId: {})", formData.getFirst("formId"), exception);
			if(exception.getMessage().equalsIgnoreCase(HttpStatus.PRECONDITION_FAILED.toString()))
			{
				httpServletResponse.sendError(HttpStatus.PRECONDITION_FAILED.value(), "File Bin already exist");
				return null;
			}
			else if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
			return null;
		}
	}
	
	@PostMapping(value = "/lse")
	public Object listScriptEngines(HttpServletRequest httpServletRequest) throws Exception {
		logger.debug("Inside DynamicFormController.listScriptEngines");
		try {
			String platformType = httpServletRequest.getParameter("platformId");
			if(platformType.trim().equalsIgnoreCase("PHP") || platformType.trim().equalsIgnoreCase("JavaScript") || platformType.trim().equalsIgnoreCase("Python")) {
				return dynamicFormService.listScriptEngines(platformType);
			} else {
				return true;
			}
		} catch (Exception exception) {
			logger.error("Error occured while listing Script Engines", exception);
			return null;
		}
	}

	@PostMapping(value = "/psdf")
	@Authorized(moduleName = Constants.DYNAMICFORM)
	public ResponseEntity<?> saveDynamicForm(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletRepsonse) throws CustomStopException{
		logger.debug("Inside DynamicFormController.saveDynamicForm()");
		List<Map<String, String>>	formData	= null;
		try {
			formData	= new Gson().fromJson(httpServletRequest.getParameter("formData"),
					List.class);
			dynamicFormService.saveDynamicForm(formData, httpServletRequest, httpServletRepsonse);
			// validate resultMap
			for (Map<String, String> formEntry : formData) {
				if (formEntry.containsKey("valueType") && formEntry.get("valueType").equalsIgnoreCase("fileBin")) {
					filesStorageService.commitChanges(formEntry.get("FileBinID"), formEntry.get("fileAssociationID"));
				}
			}
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (CustomStopException custStopException) {
			logger.error("Error occured while loading dynamic form.", custStopException);
			throw custStopException;
		} catch (Throwable a_throwable) {

			logger.error("Error occurred while saving dynamic form(formData: {})",formData, a_throwable);
			Throwable rootCause = a_throwable;
			while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
				if (rootCause instanceof StopException) {
					return new ResponseEntity<>(((StopException) rootCause).getMessageWithoutStackTop(),
							HttpStatus.EXPECTATION_FAILED);
				}
				rootCause = rootCause.getCause();
			}

			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Authorized(moduleName = Constants.DYNAMICFORM)
	public Map<String, Object> saveDynamicFormV2(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletRepsonse, Map<String, Object> parameterMap) throws IOException, CustomStopException {
		
		logger.debug("Inside DynamicFormController.saveDynamicFormV2()");
		List<Map<String, String>> formData = null;
		try {
			 formData = new Gson().fromJson(httpServletRequest.getParameter("formData"),
					List.class);
			Map<String, Object> result = dynamicFormService.saveDynamicFormV2(formData, httpServletRequest, httpServletRepsonse);

			// validate resultMap
			for (Map<String, String> formEntry : formData) {
				if (formEntry.containsKey("valueType") && formEntry.get("valueType").equalsIgnoreCase("fileBin") && formEntry.get("fileUploadTempId")!=null) {
					filesStorageService.commitChanges(formEntry.get("FileBinID"), formEntry.get("fileAssociationID"), formEntry.get("fileUploadTempId"));
				}
			}
			return result;
			
		} catch (CustomStopException custStopException) {
			logger.error("Error occured while loading dynamic form.", custStopException);
			throw custStopException;
		} catch (Throwable a_throwable) {

			logger.error("Error occurred while saving dynamic form: "+formData.get(0), a_throwable);
			Throwable rootCause = a_throwable;
			while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
				if (rootCause instanceof StopException) {
					httpServletRepsonse.sendError(HttpStatus.EXPECTATION_FAILED.value(),
							((StopException) rootCause).getMessageWithoutStackTop());
				}
				rootCause = rootCause.getCause();
			}
			
			httpServletRepsonse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
		}
		return null;
	}

	private Map<String, Object> processRequestParams(HttpServletRequest httpServletRequest) {
		
		Map<String, Object> requestParams = new HashMap<>();
		for (String requestParamKey : httpServletRequest.getParameterMap().keySet()) {
			
			if (Boolean.FALSE.equals("formId".equalsIgnoreCase(requestParamKey))) {
				requestParams.put(requestParamKey, httpServletRequest.getParameter(requestParamKey));
				}
		}
		String			uri				= httpServletRequest.getRequestURI();
		String			url				= httpServletRequest.getRequestURL().toString();
		StringBuilder	contextPathUrl	= new StringBuilder();
		url = url.replace(uri, "");
		contextPathUrl.append(url);
		
		requestParams.put("contextPathUrl", contextPathUrl);
		return requestParams;
}
}
