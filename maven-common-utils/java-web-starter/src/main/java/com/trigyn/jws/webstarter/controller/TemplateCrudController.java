package com.trigyn.jws.webstarter.controller;

import java.io.IOException;
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
import com.trigyn.jws.templating.service.DBTemplatingService;
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.templating.vo.TemplateVO;
import com.trigyn.jws.webstarter.service.TemplateCrudService;

@RestController
@RequestMapping("/cf")
@PreAuthorize("hasPermission('module','Templating')")
public class TemplateCrudController {

	private final static Logger	logger				= LogManager.getLogger(TemplateCrudController.class);

	@Autowired
	private DBTemplatingService	dbTemplatingService	= null;

	@Autowired
	private TemplateCrudService	templateCrudService	= null;

	@Autowired
	private PropertyMasterDAO	propertyMasterDAO	= null;

	@Autowired
	private MenuService			menuService			= null;

	@GetMapping(value = "/te", produces = MediaType.TEXT_HTML_VALUE)
	public String templatePage(HttpServletResponse httpServletResponse) throws IOException {
		try {
			Map<String, Object>	modelMap	= new HashMap<>();
			String				environment	= propertyMasterDAO.findPropertyMasterValue("system", "system", "profile");
			modelMap.put("environment", environment);
			return menuService.getTemplateWithSiteLayout("template-listing", modelMap);
		} catch (Exception a_exception) {
			logger.error("Error ", a_exception);
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}
	}

	@GetMapping(value = "/aet", produces = MediaType.TEXT_HTML_VALUE)
	public String velocityTemplateEditor(HttpServletRequest request, HttpServletResponse httpServletResponse)
			throws IOException {
		try {
			String				templateId		= request.getParameter("vmMasterId");
			Map<String, Object>	vmTemplateData	= new HashMap<>();
			if (!StringUtils.isBlank(templateId)) {
				TemplateVO templateDetails = dbTemplatingService.getVelocityDataById(templateId);
				templateDetails.setTemplate("");
				vmTemplateData.put("templateDetails", templateDetails);
			}
			return menuService.getTemplateWithSiteLayout("template-manage-details", vmTemplateData);
		} catch (Exception a_exception) {
			logger.error("Error ", a_exception);
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}
	}

	@RequestMapping(value = "/ctd")
	@ResponseBody
	public String checkTemplateData(HttpServletRequest request, HttpServletResponse response) {
		String	templateName	= request.getParameter("templateName");
		String	templateId		= null;
		try {
			templateId = templateCrudService.checkVelocityData(templateName);
			return templateId;
		} catch (Exception a_exception) {
			logger.error("Error ", a_exception);
			return null;
		}
	}

	@GetMapping(value = "/gtbi")
	public String getTemplateByTemplateId(HttpServletRequest request) throws Exception {
		String		templateId		= request.getParameter("templateId");
		TemplateVO	templateDetails	= dbTemplatingService.getVelocityDataById(templateId);
		return templateDetails.getTemplate();
	}

	@PostMapping(value = "/std")
	public String saveTemplateData(HttpSession session, HttpServletRequest request) throws Exception {
		return dbTemplatingService.saveTemplateData(request);
	}

	@PostMapping(value = "/stdv")
	public void saveTemplateDataByVersion(HttpServletRequest a_httpServletRequest,
			HttpServletResponse a_httpServletResponse) throws Exception {
		String			modifiedContent	= a_httpServletRequest.getParameter("modifiedContent");
		ObjectMapper	objectMapper	= new ObjectMapper();
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
}