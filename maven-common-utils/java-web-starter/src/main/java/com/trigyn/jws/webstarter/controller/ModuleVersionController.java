package com.trigyn.jws.webstarter.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trigyn.jws.dbutils.entities.ModuleListing;
import com.trigyn.jws.dbutils.service.ModuleService;
import com.trigyn.jws.dbutils.service.ModuleVersionService;
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.webstarter.service.ModuleRevisionService;
import com.trigyn.jws.webstarter.utils.Constant;

@RestController
@RequestMapping("/cf")
public class ModuleVersionController {
	
	@Autowired
	private ModuleVersionService moduleVersionService				= null;
	
	@Autowired
	private MenuService menuService 								= null;
	
	@Autowired
	private ModuleRevisionService revisionService					= null;

	@Autowired
	private ModuleService moduleService 					= null;

	@PostMapping(value="/smv",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public void saveModuleVersioning(@RequestBody MultiValueMap<String, String> formData) throws Exception{
		revisionService.saveModuleVersioning(formData, Constant.MASTER_SOURCE_VERSION_TYPE);
	}
	
	@PostMapping(value = "/cmv", produces = { MediaType.TEXT_HTML_VALUE })
	public String compareModuleVersion(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse) throws Exception{
		Map<String , Object> templateMap = revisionService.getModuleVersioningData(a_httpServletRequest);
		return menuService.getTemplateWithSiteLayout("revision-details", templateMap);
	}
	
	@PostMapping(value = "/mj")
	public String getModuleJsonById(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse) throws Exception{
		String moduleVersionId = a_httpServletRequest.getParameter("moduleVersionId");
		return moduleVersionService.getModuleJsonById(moduleVersionId);
	}
	
	@PostMapping(value = "/uj")
	public String getLastUpdatedJsonData(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse) throws Exception{
		String entityId = a_httpServletRequest.getParameter("entityId");
		String entityName = a_httpServletRequest.getParameter("entityName");
		return moduleVersionService.getLastUpdatedJsonData(entityId, entityName);
	}
	
	
	@PostMapping(value = "/suj")
	public void saveUpdatedContent(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse) throws Exception{
		revisionService.saveUpdatedContent(a_httpServletRequest);
	}

	@PostMapping(value = "/muj")
	public String getLastUpdatedSitelayoutJsonData(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse) throws Exception{
		String entityId = a_httpServletRequest.getParameter("entityId");
		return moduleService.getModuleListingJson(entityId);
	}

	@PostMapping(value = "/sml")
	public void saveSiteLayout(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse) throws Exception{
		String modifiedContent 				= a_httpServletRequest.getParameter("modifiedContent");
		ObjectMapper objectMapper			= new ObjectMapper();
		ModuleListing moduleListing		= objectMapper.readValue(modifiedContent, ModuleListing.class);
		moduleService.saveModuleListing(moduleListing);
	}
}
