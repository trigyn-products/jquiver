package com.trigyn.jws.dynamicform.controller;

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
	public String loadDynamicForm(@RequestParam(value = "formId",required = true) String formId,
			@RequestParam(value = "primaryId",required = true) String primaryId) throws Exception {
		return dynamicFormService.loadDynamicForm(formId,primaryId,null);
	}
	
	@PostMapping(value="/sdf",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Boolean saveDynamicForm(
			@RequestBody MultiValueMap<String, String> formData) throws Exception {
		return dynamicFormService.saveDynamicForm(formData);
	}
	
	
}
