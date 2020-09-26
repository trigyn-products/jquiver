package com.trigyn.jws.dynamicform.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.dynamicform.service.DynamicFormService;

@RestController
@RequestMapping("/cf")
public class DynamicFormController {

	@Autowired
	private DynamicFormService dynamicFormService = null;
	
	@PostMapping("/df")
	public String loadDynamicForm(@RequestParam(value = "formId",required = true) String formId, HttpServletRequest httpServletRequest) throws Exception {
		return dynamicFormService.loadDynamicForm(formId,processRequestParams(httpServletRequest), null);
	}
	
	@PostMapping(value="/sdf",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Boolean saveDynamicForm(
			@RequestBody MultiValueMap<String, String> formData) throws Exception {
		return dynamicFormService.saveDynamicForm(formData);
	}
	
	private Map<String, Object> processRequestParams(HttpServletRequest httpServletRequest) {
        Map<String, Object> requestParams = new HashMap<>();
        for (String requestParamKey : httpServletRequest.getParameterMap().keySet()) {
        	if(Boolean.FALSE.equals("formId".equalsIgnoreCase(requestParamKey))) {
        		requestParams.put(requestParamKey, httpServletRequest.getParameter(requestParamKey));
        	}
        }
        return requestParams;
    }
	
	
}
