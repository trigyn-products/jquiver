package com.trigyn.jws.dynamicform.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.trigyn.jws.dynamicform.service.DynamicFormService;
import com.trigyn.jws.usermanagement.security.config.Authorized;
import com.trigyn.jws.usermanagement.utils.Constants;

import freemarker.core.StopException;

@RestController
@RequestMapping("/cf")
public class DynamicFormController {

	private static final Logger	logger				= LogManager.getLogger(DynamicFormController.class);

	@Autowired
	private DynamicFormService	dynamicFormService	= null;

	@PostMapping("/df")
	@Authorized(moduleName = Constants.DYNAMICFORM)
	public String loadDynamicForm(@RequestParam(value = "formId", required = true) String formId,
			HttpServletRequest httpServletRequest) throws Exception {
		return dynamicFormService.loadDynamicForm(formId, processRequestParams(httpServletRequest), null);
	}

	@PostMapping(value = "/sdf", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@Authorized(moduleName = Constants.DYNAMICFORM)
	public Boolean saveDynamicForm(@RequestBody MultiValueMap<String, String> formData) throws Exception {
		return dynamicFormService.saveDynamicForm(formData);
	}

	@PostMapping(value = "/psdf")
	@Authorized(moduleName = Constants.DYNAMICFORM)
	public ResponseEntity<?> saveDynamicForm(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResonse) {
		try {
			List<Map<String, String>>	formData	= new Gson().fromJson(httpServletRequest.getParameter("formData"),
					List.class);
			HttpSession					httpSession	= httpServletRequest.getSession();
			dynamicFormService.saveDynamicForm(formData, httpSession, httpServletResonse);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (StopException a_exception) {
			return new ResponseEntity<>(a_exception.getMessageWithoutStackTop(), HttpStatus.EXPECTATION_FAILED);
		} catch (Throwable a_throwable) {

			logger.error("Error occurred while processing request: ", a_throwable);
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
