package com.trigyn.jws.webstarter.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import com.trigyn.jws.dbutils.utils.CustomStopException;
import com.trigyn.jws.dbutils.utils.FileUtilities;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.dynamicform.dao.DynamicFormCrudDAO;
import com.trigyn.jws.dynamicform.entities.DynamicForm;
import com.trigyn.jws.dynamicform.service.DynamicFormIoService;
import com.trigyn.jws.dynamicform.service.DynamicFormService;
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.webstarter.service.DynamicFormCrudService;
import com.trigyn.jws.webstarter.utils.Constant;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/cf")
@PreAuthorize("hasPermission('module','Form Builder')")
public class DynamicFormCrudController {

	private final static Logger		logger					= LoggerFactory.getLogger(DynamicFormCrudController.class);

	@Autowired
	private DynamicFormCrudService	dynamicFormCrudService	= null;

	@Autowired
	private PropertyMasterDAO		propertyMasterDAO		= null;

	@Autowired
	private DynamicFormService		dynamicFormService		= null;

	@Autowired
	private MenuService				menuService				= null;

	@Autowired
	private IUserDetailsService		userDetailsService		= null;

	@Autowired
	private ActivityLog				activitylog				= null;

	@Autowired
	private DynamicFormCrudDAO		dynamicFormDAO			= null;

	@Autowired
	private FileUtilities			fileUtilities			= null;

	@Autowired
	private DynamicFormIoService	dynamicFormIoService	= null;

	@PostMapping(value = "/aedf", produces = { MediaType.TEXT_HTML_VALUE })
	public String addEditForm(@RequestParam("form-id") String formId, HttpServletResponse httpServletResponse)
			throws IOException, CustomStopException {
		try {
			/* Method called for implementing Activity Log */
			logActivity(formId);
			return dynamicFormCrudService.addEditForm(formId);
		} catch (CustomStopException custStopException) {
			logger.error("Error occured while loading Dynamic Form page.", custStopException);
			throw custStopException;
		} catch (Exception a_exception) {
			logger.error("Error occured in Form Builder (formId: {})",formId, a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			fileUtilities.customSendError(httpServletResponse,HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
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
	
	@PostMapping(value = "/gsld")
	public List<Map<String, Object>> getScriptLibDetails(HttpServletRequest httpServletRequest) throws Exception {
		String formQueryId = httpServletRequest.getParameter("formQueryId");
		List<Map<String, Object>> scriptLibList = dynamicFormCrudService.getScriptLibDetatils(formQueryId);
		return scriptLibList;
	}

	@PostMapping(value = "/sdfd", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String saveDynamicFormDetails(@RequestBody MultiValueMap<String, String> formData) throws Exception {
		 DynamicForm dynamicform = dynamicFormCrudService.saveDynamicFormDetails(formData, Constant.MASTER_SOURCE_VERSION_TYPE);
		 return dynamicFormCrudService.saveDynamicFormDetails2(formData,dynamicform, Constant.MASTER_SOURCE_VERSION_TYPE);
	}

	@GetMapping(value = "/dfl", produces = MediaType.TEXT_HTML_VALUE)
	public String dynamicFormMasterListing(HttpServletResponse httpServletResponse) throws IOException, CustomStopException {
		try {
			Map<String, Object> modelMap = new HashMap<>();
			String environment = propertyMasterDAO.findPropertyMasterValue("system", "system", "profile");
			modelMap.put("environment", environment);
			return menuService.getTemplateWithSiteLayout("dynamic-form-listing", modelMap);
		} catch (CustomStopException custStopException) {
			logger.error("Error occured while loading Dynamic Form Listing page.", custStopException);
			throw custStopException;
		
		} catch (Exception a_exception) {
			logger.error("Error occured while loading Dynamic Form Listing page.", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			fileUtilities.customSendError(httpServletResponse,HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}
	}

	@PostMapping(value = "/dfte", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, String> createDefaultFormByTableName(HttpServletRequest httpServletRequest) throws Exception {
		Map<String, String> response  =  new HashMap<>();
		String tableName = httpServletRequest.getParameter("tableName");
		Boolean toggleCaptcha = false;
		Boolean toggleCsrf = false;
		Boolean toggleFileBin = false;
		String additionalDataSourceId = httpServletRequest.getParameter("dbProductID");
		String dbProductName = httpServletRequest.getParameter("dbProductName");
		List<Map<String, Object>> tableDetails = dynamicFormService.getTableDetailsByTableName(tableName,
				additionalDataSourceId);
		Boolean isFormIo = Boolean.valueOf(httpServletRequest.getParameter("isFormIo"));
		String formIoId = httpServletRequest.getParameter("formIoId");
		if(isFormIo) {
			Boolean isTableValid = Boolean.FALSE;
			for (Map<String, Object> info : tableDetails) {
				String	columnKey	= info.get("columnKey").toString();
				if ("PK".equals(columnKey)) {
					isTableValid = Boolean.TRUE;
					break;
				}
			}
			if(isTableValid == Boolean.FALSE) {
				return (Map<String, String>) ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(response);
			}
			response = dynamicFormIoService.createDefaultFormByTableName(tableName, tableDetails, null, additionalDataSourceId,
					dbProductName, formIoId);
		} else {
			response = dynamicFormService.createDefaultFormByTableName(tableName, tableDetails, null, additionalDataSourceId,
					dbProductName, toggleCaptcha,toggleCsrf,toggleFileBin,null,null);
		}
		return response;
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
		String formTypeID = request.getParameter("formTypeID");
		dynamicFormCrudService.uploadFormsToDB(formTypeID,null);
	}

	@PostMapping(value = "/ddfbi")
	public void downloadFormByIdToLocalDirectory(HttpSession session, HttpServletRequest request) throws Exception {
		String formId = request.getParameter("formId");
		dynamicFormCrudService.downloadDynamicFormsTemplate(formId);
	}

	@PostMapping(value = "/udfbn")
	public void uploadFormsByNameToDB(HttpSession session, HttpServletRequest request) throws Exception {
		String formName = request.getParameter("formName");
		String formTypeID = request.getParameter("formTypeID");
		dynamicFormCrudService.uploadFormsToDB(formTypeID,formName);
	}

}
