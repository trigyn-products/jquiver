package com.trigyn.jws.webstarter.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.dbutils.service.TemplateVersionService;
import com.trigyn.jws.templating.service.DBTemplatingService;
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.templating.vo.TemplateVO;
import com.trigyn.jws.webstarter.service.TemplateCrudService;

@RestController
@RequestMapping("/cf")
public class TemplateCrudController {
	
	private final static Logger logger 						= LogManager.getLogger(TemplateCrudController.class);

	@Autowired
	private DBTemplatingService dbTemplatingService 		= null;
	
	@Autowired
	private TemplateCrudService templateCrudService			= null;
	
	@Autowired
	private PropertyMasterDAO propertyMasterDAO 			= null;
	
	@Autowired
	private MenuService 				menuService			= null;
	
	@Autowired
	private TemplateVersionService templateVersionService	= null;

	@GetMapping(value = "/te", produces = MediaType.TEXT_HTML_VALUE)
    public String templatePage(HttpServletResponse httpServletResponse) throws IOException {
		try {
			Map<String,Object>  modelMap 	= new HashMap<>();
			String environment 				= propertyMasterDAO.findPropertyMasterValue("system", "system", "profile");
			modelMap.put("environment", environment);
			return menuService.getTemplateWithSiteLayout("template-listing", modelMap);
		} catch (Exception exception) {
			logger.error("Error ", exception);
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
			return null;
		}
    }

	@GetMapping(value = "/aet", produces = MediaType.TEXT_HTML_VALUE)
	public String velocityTemplateEditor(HttpServletRequest request, HttpServletResponse httpServletResponse) throws IOException  {
		try {
			String templateId 					= request.getParameter("vmMasterId");
			Map<String, Object> vmTemplateData 	= new HashMap<>();
			if (templateId != null) {
				TemplateVO templateDetails = dbTemplatingService.getVelocityDataById(templateId);
				Map<Double, String> versionDetailsMap = templateVersionService.getVersionDetails(templateId);
				templateDetails.setTemplate("");
				vmTemplateData.put("templateDetails", templateDetails);
				vmTemplateData.put("versionDetailsMap", versionDetailsMap);
			}
			return menuService.getTemplateWithSiteLayout("template-manage-details", vmTemplateData);
		} catch (Exception exception) {
			logger.error("Error ", exception);
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
			return null;
		}
	}
	
	@RequestMapping(value = "/ctd")
	@ResponseBody
	public String checkTemplateData(HttpServletRequest request, HttpServletResponse response) {
		String templateName 	= request.getParameter("templateName");
		String templateId 		= null;
		try {
			templateId = templateCrudService.checkVelocityData(templateName);
			return templateId;
		} catch (Exception exception) {
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
	public void saveTemplateData(HttpSession session, HttpServletRequest request) throws Exception {
		dbTemplatingService.saveTemplateData(request);
	}
	
	@PostMapping(value = "/dtl")
	public void downloadAllTemplatesToLocalDirectory(HttpSession session, HttpServletRequest request) throws Exception {
		templateCrudService.downloadTemplates(null);
	}

	@PostMapping(value = "/utd")
	public void uploadAllTemplatesToDB(HttpSession session, HttpServletRequest request) throws Exception {
		templateCrudService.uploadTemplates(null);
	}
	
	@GetMapping(value = "/vtd")
	@ResponseBody
	public String getTemplateDatabByVersion(@RequestHeader(name = "template-id", required = true) String templateId
			,@RequestHeader(name = "version-id", required = true) Double versionId) throws Exception{
		return templateVersionService.getTemplateData(templateId, versionId);
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