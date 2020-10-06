package com.trigyn.jws.webstarter.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.dbutils.service.TemplateVersionService;
import com.trigyn.jws.dynamicform.service.DynamicFormService;
import com.trigyn.jws.dynamicform.vo.DynamicFormSaveQueryVO;
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.webstarter.service.DynamicFormCrudService;

@RestController
@RequestMapping("/cf")
public class DynamicFormCrudController {
	
	private final static Logger logger = LogManager.getLogger(DynamicFormCrudController.class);

	@Autowired
	private DynamicFormCrudService dynamicFormCrudService 	= null;
	
	@Autowired
	private PropertyMasterDAO propertyMasterDAO 			= null;
	
	@Autowired
	private DynamicFormService dynamicFormService			= null;
	
	@Autowired
	private MenuService			menuService					= null;
	
	@Autowired
	private TemplateVersionService templateVersionService	= null;
	
	@PostMapping(value = "/aedf", produces = {MediaType.TEXT_HTML_VALUE})
	public String addEditForm(@RequestParam("form-id") String formId, HttpServletResponse httpServletResponse) throws IOException {
		try{
			return dynamicFormCrudService.addEditForm(formId);
		} catch (Exception exception) {
			logger.error("Error ", exception);
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
			return null;
		}
	}
	
	@PostMapping(value = "/gfsq", produces = {MediaType.APPLICATION_JSON_VALUE})
	public List<Map<String, Object>> getAllFormQueriesById(HttpServletRequest httpServletRequest) throws Exception {
		String formId = httpServletRequest.getParameter("formId");
		return dynamicFormCrudService.getAllFormQueriesById(formId);
	}
	
	@PostMapping(value="/sdfd",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public void saveDynamicFormDetails(
			@RequestBody MultiValueMap<String, String> formData) throws Exception {
		dynamicFormCrudService.saveDynamicFormDetails(formData);
	}
	
	@GetMapping(value = "/dfl", produces = MediaType.TEXT_HTML_VALUE)
	public String dynamicFormMasterListing(HttpServletResponse httpServletResponse) throws IOException {
		try{
			Map<String,Object>  modelMap = new HashMap<>();
			String environment = propertyMasterDAO.findPropertyMasterValue("system", "system", "profile");
			modelMap.put("environment", environment);
			return menuService.getTemplateWithSiteLayout("dynamic-form-listing", modelMap);
		} catch (Exception exception) {
			logger.error("Error ", exception);
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
			return null;
		}
	}
	
	@PostMapping(value="/dfte", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, String> createDefaultFormByTableName(HttpServletRequest httpServletRequest) throws Exception {
		String tableName = httpServletRequest.getParameter("tableName");
		List<Map<String, Object>> tableDetails = dynamicFormService.getTableDetailsByTableName(tableName);
		return dynamicFormService.createDefaultFormByTableName(tableName, tableDetails);
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
		dynamicFormCrudService.downloadDynamicFormsTemplate(null);
	}

	@PostMapping(value = "/udf")
	public void uploadAllFormsToDB(HttpSession session, HttpServletRequest request) throws Exception {
		dynamicFormCrudService.uploadFormsToDB(null);
	}
	
	@GetMapping(value = "/vdfd")
	@ResponseBody
	public String getTemplateDataByVersion(@RequestHeader(name = "form-id", required = true) String formId
			,@RequestHeader(name = "version-id", required = true) Double versionId) throws Exception{
		return templateVersionService.getTemplateData(formId, versionId);
	}
	
	@PostMapping(value = "/ddfbi")
	public void downloadFormByIdToLocalDirectory(HttpSession session, HttpServletRequest request) throws Exception {
		String formId = request.getParameter("formId");
		dynamicFormCrudService.downloadDynamicFormsTemplate(formId);
	}
	
	@PostMapping(value = "/udfbn")
	public void uploadFormsByNameToDB(HttpSession session, HttpServletRequest request) throws Exception {
		String formName = request.getParameter("formName");
		dynamicFormCrudService.uploadFormsToDB(formName);
	}

}
