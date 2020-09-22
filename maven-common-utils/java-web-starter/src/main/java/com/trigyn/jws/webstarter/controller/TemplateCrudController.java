package com.trigyn.jws.webstarter.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.templating.service.DBTemplatingService;
import com.trigyn.jws.templating.utils.TemplatingUtils;
import com.trigyn.jws.templating.vo.TemplateVO;
import com.trigyn.jws.webstarter.service.TemplateCrudService;

@RestController
@RequestMapping("/cf")
public class TemplateCrudController {
	
	private final static Logger logger = LogManager.getLogger(TemplateCrudController.class);

	@Autowired
	private DBTemplatingService dbTemplatingService = null;
	
    @Autowired
	private DBTemplatingService templateService = null;
	
	@Autowired
	private TemplatingUtils templateEngine = null;
	
	@Autowired
	private TemplateCrudService templateCrudService =  null;
	
	@Autowired
	private PropertyMasterDAO propertyMasterDAO = null;

	@GetMapping(value = "/te", produces = MediaType.TEXT_HTML_VALUE)
    public String templatePage() throws Exception {
		TemplateVO templateVO = templateService.getTemplateByName("template-listing");
		Map<String,Object>  modelMap = new HashMap<>();
		String environment = propertyMasterDAO.findPropertyMasterValue("system", "system", "profile");
		modelMap.put("environment", environment);
		return templateEngine.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(), modelMap);
    }

	@GetMapping(value = "/aet", produces = MediaType.TEXT_HTML_VALUE)
	public String velocityTemplateEditor(HttpServletRequest request) throws Exception {
		TemplateVO templateVO = dbTemplatingService.getTemplateByName("template-manage-details");
		String templateId = request.getParameter("vmMasterId");
		Map<String, Object> vmTemplateData = new HashMap<>();
		if (templateId != null) {
			TemplateVO templateDetails = dbTemplatingService.getVelocityDataById(templateId);
			vmTemplateData.put("templateDetails", templateDetails);
		}
		return templateEngine.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(),
				vmTemplateData);
	}

	@RequestMapping(value = "/ctd")
	@ResponseBody
	public String checkTemplateData(HttpServletRequest request, HttpServletResponse response) {
		String templateName = request.getParameter("templateName");
		String templateId = null;
		try {
			templateId = templateCrudService.checkVelocityData(templateName);
			return templateId;
		} catch (Exception e) {
			return null;
		}
	}
	
	@PostMapping(value = "/std")
	public void saveTemplateData(HttpSession session, HttpServletRequest request) throws Exception {
		dbTemplatingService.saveTemplateData(request);
	}
	
	@PostMapping(value = "/dtl")
	public void downloadAllTemplatesToLocalDirectory(HttpSession session, HttpServletRequest request) throws Exception {
		
		templateCrudService.downloadTemplates();
	
		
	}

	@PostMapping(value = "/utd")
	public void uploadAllTemplatesToDB(HttpSession session, HttpServletRequest request) throws Exception {
		templateCrudService.uploadTemplates();
	}
}