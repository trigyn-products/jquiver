package com.trigyn.jws.webstarter.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.utils.ActivityLog;
import com.trigyn.jws.dbutils.utils.Constant;
import com.trigyn.jws.dbutils.utils.IMonacoSuggestion;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.templating.service.DBTemplatingService;
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.templating.vo.TemplateVO;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.webstarter.service.TemplateCrudService;

@RestController
@RequestMapping("/cf")
@PreAuthorize("hasPermission('module','Templating')")
public class TemplateCrudController {

	private final static Logger logger = LogManager.getLogger(TemplateCrudController.class);

	@Autowired
	private DBTemplatingService dbTemplatingService = null;

	@Autowired
	private TemplateCrudService templateCrudService = null;

	@Autowired
	private PropertyMasterDAO propertyMasterDAO = null;

	@Autowired
	private MenuService menuService = null;

	@Autowired
	private IUserDetailsService userDetailsService = null;

	@Autowired
	private ActivityLog activitylog = null;

	@Autowired
	private PropertyMasterService propertyMasterService = null;

	@GetMapping(value = "/te", produces = MediaType.TEXT_HTML_VALUE)
	public String templatePage(HttpServletRequest request, HttpServletResponse httpServletResponse) throws IOException {
		try {

			Map<String, Object> modelMap = new HashMap<>();
			String environment = propertyMasterDAO.findPropertyMasterValue("system", "system", "profile");
			modelMap.put("environment", environment);
			return menuService.getTemplateWithSiteLayout("template-listing", modelMap);
		} catch (Exception a_exception) {
			logger.error("Error occured while loading Template Listing page.", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}
	}

	@RequestMapping(value = "/gtbn")
	public String getTemplateByName(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String templateName = request.getParameter("templateName");
		return menuService.getTemplateWithoutLayout(templateName, new HashMap<>());
	}

	@GetMapping(value = "/aet", produces = MediaType.TEXT_HTML_VALUE)
	public String velocityTemplateEditor(HttpServletRequest request, HttpServletResponse httpServletResponse)
			throws IOException {
		try {
			String templateId = request.getParameter("vmMasterId");
			Map<String, Object> vmTemplateData = new HashMap<>();
			if (!StringUtils.isBlank(templateId)) {
				TemplateVO templateDetails = dbTemplatingService.getVelocityDataById(templateId);
				templateDetails.setTemplate("");
				vmTemplateData.put("templateDetails", templateDetails);
				String templateName = templateDetails.getTemplateName();
				Integer typeSelect = templateDetails.getTemplateType();
				/* Method called for implementing Activity Log */
				logActivity(templateName, typeSelect);
			}
			/* ContextPath Suggestion in Monaco Editor */
			String contextSuggestions = IMonacoSuggestion.getTemplateSuggestion();
			vmTemplateData.put("suggestions", contextSuggestions);
			return menuService.getTemplateWithSiteLayout("template-manage-details", vmTemplateData);
		} catch (Exception a_exception) {
			logger.error("Error occured in Templates : " + "Template Id : " + request.getParameter("vmMasterId"),
					a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}
	}

	/**
	 * Purpose of this method is to log activities</br>
	 * in Templating Module.
	 * 
	 * @author Bibhusrita.Nayak
	 * @param templateName
	 * @param typeSelect
	 * @throws Exception
	 */
	private void logActivity(String entityName, Integer typeSelect) throws Exception {
		Map<String, String> requestParams = new HashMap<>();
		UserDetailsVO detailsVO = userDetailsService.getUserDetails();
		String masterModuleType = Constants.Modules.TEMPLATING.getModuleName();
		Date activityTimestamp = new Date();
		if (typeSelect == Constants.Changetype.CUSTOM.getChangeTypeInt()) {
			requestParams.put("typeSelect", Constants.Changetype.CUSTOM.getChangetype());
		} else {
			requestParams.put("typeSelect", Constants.Changetype.SYSTEM.getChangetype());
		}
		requestParams.put("entityName", entityName);
		requestParams.put("masterModuleType", masterModuleType);
		requestParams.put("userName", detailsVO.getUserName());
		requestParams.put("message", "");
		requestParams.put("date", activityTimestamp.toString());// activityTimestamp
		requestParams.put("action", Constants.Action.OPEN.getAction());
		activitylog.activitylog(requestParams);
	}

	@RequestMapping(value = "/ctd")
	@ResponseBody
	public String checkTemplateData(HttpServletRequest request, HttpServletResponse response) {
		String templateName = request.getParameter("templateName");
		String templateId = null;
		try {
			templateId = templateCrudService.checkVelocityData(templateName);
			return templateId;
		} catch (Exception a_exception) {
			logger.error("Error occured in Templates :" + "Template Name :" + templateName, a_exception);
			return null;
		}
	}

	@GetMapping(value = "/gtbi")
	public String getTemplateByTemplateId(HttpServletRequest request) throws Exception {

		String templateId = request.getParameter("templateId");
		TemplateVO templateDetails = dbTemplatingService.getVelocityDataById(templateId);
		return templateDetails.getTemplate();
	}

	@PostMapping(value = "/std")
	public String saveTemplateData(HttpSession session, HttpServletRequest request) throws Exception {
		return dbTemplatingService.saveTemplateData(request);
	}

	@PostMapping(value = "/stdv")

	public void saveTemplateDataByVersion(HttpServletRequest a_httpServletRequest,
			HttpServletResponse a_httpServletResponse) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		/** Added for parsing the date */
		String dbDateFormat = propertyMasterService.getDateFormatByName(Constant.PROPERTY_MASTER_OWNER_TYPE,
				Constant.PROPERTY_MASTER_OWNER_ID, Constant.JWS_DATE_FORMAT_PROPERTY_NAME,
				Constant.JWS_JAVA_DATE_FORMAT_PROPERTY_NAME);
		DateFormat dateFormat = new SimpleDateFormat(dbDateFormat);
		objectMapper.setDateFormat(dateFormat);
		/** Ends Here */
		String modifiedContent = a_httpServletRequest.getParameter("modifiedContent");
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		TemplateVO templateVO = objectMapper.readValue(modifiedContent, TemplateVO.class);
		dbTemplatingService.saveTemplate(templateVO);
	}

	@PostMapping(value = "/dtl")
	public void downloadAllTemplatesToLocalDirectory(HttpSession session, HttpServletRequest request) throws Exception {
		templateCrudService.downloadTemplates(null);
	}

	@PostMapping(value = "/utd")
	public void uploadAllTemplatesToDB(HttpSession session, HttpServletRequest request) throws Exception {
		templateCrudService.uploadTemplates(null);
	}

	@PostMapping(value = "/dtbi")
	public void downloadTemplateByIdToLocalDirectory(HttpSession session, HttpServletRequest request) throws Exception {
		String templateId = request.getParameter("templateId");
		templateCrudService.downloadTemplates(templateId);
	}

	@PostMapping(value = "/utdbi")
	public void uploadTemplateByNameToDB(HttpSession session, HttpServletRequest request) throws Exception {
		String templateName = request.getParameter("templateName");
		templateCrudService.uploadTemplates(templateName);
	}

	@PostMapping(value = "/gadc")
	public String getAutocompleteDefaultContent(HttpSession a_httpSession, HttpServletRequest a_httpServletRequest)
			throws Exception {
		String templateName = a_httpServletRequest.getParameter("templateName");
		String selectedTab = a_httpServletRequest.getParameter("selectedTab");

		Map<String, Object> templateParamMap = new HashMap<>();
		templateParamMap.put("selectedTab", selectedTab);
		return menuService.getTemplateWithoutLayout(templateName, templateParamMap);

	}
}