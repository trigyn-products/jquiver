package com.trigyn.jws.webstarter.controller;

import java.io.IOException;
import java.util.Date;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.utils.ActivityLog;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.dynamicform.dao.DynamicFormCrudDAO;
import com.trigyn.jws.dynamicform.entities.DynamicForm;
import com.trigyn.jws.dynamicform.service.DynamicFormService;
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.webstarter.service.DynamicFormCrudService;
import com.trigyn.jws.webstarter.utils.Constant;

@RestController
@RequestMapping("/cf")
@PreAuthorize("hasPermission('module','Form Builder')")
public class DynamicFormCrudController {

	private final static Logger logger = LogManager.getLogger(DynamicFormCrudController.class);

	@Autowired
	private DynamicFormCrudService dynamicFormCrudService = null;

	@Autowired
	private PropertyMasterDAO propertyMasterDAO = null;

	@Autowired
	private DynamicFormService dynamicFormService = null;

	@Autowired
	private MenuService menuService = null;

	@Autowired
	private IUserDetailsService userDetailsService = null;

	@Autowired
	private ActivityLog activitylog = null;

	@Autowired
	private DynamicFormCrudDAO dynamicFormDAO = null;

	@PostMapping(value = "/aedf", produces = { MediaType.TEXT_HTML_VALUE })
	public String addEditForm(@RequestParam("form-id") String formId, HttpServletResponse httpServletResponse)
			throws IOException {
		try {
			/* Method called for implementing Activity Log */
			logActivity(formId);
			return dynamicFormCrudService.addEditForm(formId);
		} catch (Exception a_exception) {
			logger.error("Error occured in Form Builder (formId: {})",formId, a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}

			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}
	}

	/**
	 * Purpose of this method is to log activities</br>
	 * in Dynamic Forms Module.
	 * 
	 * @author Bibhusrita.Nayak
	 * @param formId
	 * @throws Exception
	 */
	private void logActivity(String formId) throws Exception {
		if (formId != "") {
			Map<String, String> requestParams = new HashMap<>();
			UserDetailsVO detailsVO = userDetailsService.getUserDetails();
			DynamicForm dynamicForm = new DynamicForm();
			dynamicForm = dynamicFormDAO.findDynamicFormById(formId);
			Integer typeSelect = dynamicForm.getFormTypeId();
			String masterModuleType = Constants.Modules.DYNAMICFORM.getModuleName();
			Date activityTimestamp = new Date();
			if (typeSelect == Constants.Changetype.CUSTOM.getChangeTypeInt()) {
				requestParams.put("typeSelect", Constants.Changetype.CUSTOM.getChangetype());
			} else {
				requestParams.put("typeSelect", Constants.Changetype.SYSTEM.getChangetype());
			}
			requestParams.put("entityName", dynamicForm.getFormName());
			requestParams.put("masterModuleType", masterModuleType);
			requestParams.put("userName", detailsVO.getUserName());
			requestParams.put("message", "");
			requestParams.put("date", activityTimestamp.toString());
			requestParams.put("action", Constants.Action.OPEN.getAction());
			activitylog.activitylog(requestParams);
		}

	}

	@PostMapping(value = "/gfhc")
	public String getFormContentById(HttpServletRequest httpServletRequest) throws Exception {
		String formId = httpServletRequest.getParameter("formId");
		return dynamicFormCrudService.getFormContentById(formId);
	}

	@PostMapping(value = "/gfsq", produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<Map<String, Object>> getAllFormQueriesById(HttpServletRequest httpServletRequest) throws Exception {
		String formId = httpServletRequest.getParameter("formId");
		return dynamicFormCrudService.getAllFormQueriesById(formId);
	}

	@PostMapping(value = "/sdfd", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String saveDynamicFormDetails(@RequestBody MultiValueMap<String, String> formData) throws Exception {
		return dynamicFormCrudService.saveDynamicFormDetails(formData, Constant.MASTER_SOURCE_VERSION_TYPE);
	}

	@GetMapping(value = "/dfl", produces = MediaType.TEXT_HTML_VALUE)
	public String dynamicFormMasterListing(HttpServletResponse httpServletResponse) throws IOException {
		try {
			Map<String, Object> modelMap = new HashMap<>();
			String environment = propertyMasterDAO.findPropertyMasterValue("system", "system", "profile");
			modelMap.put("environment", environment);
			return menuService.getTemplateWithSiteLayout("dynamic-form-listing", modelMap);
		} catch (Exception a_exception) {
			logger.error("Error occured while loading Dynamic Form Listing page.", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}
	}

	@PostMapping(value = "/dfte", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, String> createDefaultFormByTableName(HttpServletRequest httpServletRequest) throws Exception {
		String tableName = httpServletRequest.getParameter("tableName");
		String additionalDataSourceId = httpServletRequest.getParameter("dbProductID");
		String dbProductName = httpServletRequest.getParameter("dbProductName");
		List<Map<String, Object>> tableDetails = dynamicFormService.getTableDetailsByTableName(tableName,
				additionalDataSourceId);
		return dynamicFormService.createDefaultFormByTableName(tableName, tableDetails, null, additionalDataSourceId,
				dbProductName);
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
