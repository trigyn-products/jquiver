package com.trigyn.jws.webstarter.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.dynamicform.service.DynamicFormService;
import com.trigyn.jws.dynamicform.vo.DynamicFormSaveQueryVO;
import com.trigyn.jws.menu.service.MenuService;
import com.trigyn.jws.webstarter.service.DynamicFormCrudService;
import com.trigyn.jws.webstarter.service.MasterModuleService;

@RestController
@RequestMapping("/cf")
public class DynamicFormCrudController {

	@Autowired
	private DynamicFormCrudService dynamicFormCrudService 	= null;
	
	@Autowired
	private PropertyMasterDAO propertyMasterDAO 			= null;
	
	@Autowired
	private DynamicFormService dynamicFormService			= null;
	
	@Autowired
	private MenuService			menuService					= null;
	
	
	@PostMapping(value = "/aedf", produces = {MediaType.TEXT_HTML_VALUE})
	public String addEditForm(@RequestParam("form-id") String formId) throws Exception {
		return dynamicFormCrudService.addEditForm(formId);
	}
	
	@PostMapping(value = "/gfsq", produces = {MediaType.APPLICATION_JSON_VALUE})
	public List<DynamicFormSaveQueryVO> getAllFormQueriesById(HttpServletRequest httpServletRequest) throws Exception {
		String formId = httpServletRequest.getParameter("formId");
		return dynamicFormCrudService.getAllFormQueriesById(formId);
	}
	
	@PostMapping(value="/sdfd",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public void saveDynamicFormDetails(
			@RequestBody MultiValueMap<String, String> formData) throws Exception {
		dynamicFormCrudService.saveDynamicFormDetails(formData);
	}
	
	@GetMapping(value = "/dfl", produces = MediaType.TEXT_HTML_VALUE)
	public String dynamicFormMasterListing() throws Exception {
		Map<String,Object>  modelMap = new HashMap<>();
		String environment = propertyMasterDAO.findPropertyMasterValue("system", "system", "profile");
		modelMap.put("environment", environment);
		return menuService.getTemplateWithSiteLayout("dynamic-form-listing", modelMap);
	}
	
	@PostMapping(value="/dfte", produces = MediaType.TEXT_HTML_VALUE)
	public String createDefaultFormByTableName(HttpServletRequest httpServletRequest) throws Exception {
		String tableName = httpServletRequest.getParameter("tableName");
		return dynamicFormService.createDefaultFormByTableName(tableName);
	}
	
	@GetMapping(value = "/cdd")
	@ResponseBody
	public String checkDynamicFormData(HttpServletRequest request, HttpServletResponse response) {
		String formName = request.getParameter("formName");
		String formId = null;
		formId = dynamicFormCrudService.checkFormName(formName);
		return formId;
	}
	
	@PostMapping(value = "/ddf")
	public void downloadAllFormsToLocalDirectory(HttpSession session, HttpServletRequest request) throws Exception {
		dynamicFormCrudService.downloadDynamicFormTemplates();
	}

	@PostMapping(value = "/udf")
	public void uploadAllFormsToDB(HttpSession session, HttpServletRequest request) throws Exception {
		dynamicFormCrudService.uploadAllFormsToDB();
	}
}
