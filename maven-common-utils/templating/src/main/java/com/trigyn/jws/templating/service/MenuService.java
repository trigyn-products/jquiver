package com.trigyn.jws.templating.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.dbutils.service.ModuleService;
import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dbutils.vo.ModuleDetailsVO;
import com.trigyn.jws.templating.utils.Constant;
import com.trigyn.jws.templating.utils.TemplatingUtils;
import com.trigyn.jws.templating.vo.TemplateVO;

@Service
@Transactional
public class MenuService {

	@Autowired
	private DBTemplatingService templatingService 			= null;

	@Autowired
	private ModuleService moduleService 					= null;

	@Autowired
	private TemplatingUtils templateEngine 					= null;
	
    
	@Autowired
	private PropertyMasterDAO propertyMasterDAO				= null;
	
	@Autowired
	private PropertyMasterService propertyMasterService		= null;
	

	public String getTemplateWithSiteLayout(String templateName, Map<String,Object> templateDetails) throws Exception {
		String jquiverVersion = propertyMasterService.findPropertyMasterValue(Constant.SYSTEM_OWNER_TYPE
				, Constant.SYSTEM_OWNER_ID, Constant.JQUIVER_VERSION_PROPERTY_NAME);
		Map<String, Object> templateMap = new HashMap<>();
		List<ModuleDetailsVO> moduleDetailsVOList = moduleService.getAllMenuModules();
		templateMap.put("moduleDetailsVOList", moduleDetailsVOList);
		templateMap.put("jquiverVersion", jquiverVersion);
		templateMap.putAll(templateDetails);
		TemplateVO templateVO = templatingService.getTemplateByName("home-page");
		TemplateVO templateInnerVO = templatingService.getTemplateByName(templateName);
		String enableGoogleAnalytics = propertyMasterDAO.findPropertyMasterValue("system", "system", "enable-google-analytics");
		String googleAnalyticsKey = propertyMasterDAO.findPropertyMasterValue("system", "system", "google-analytics-key");
		templateMap.put("enableGoogleAnalytics", enableGoogleAnalytics);
		templateMap.put("googleAnalyticsKey", googleAnalyticsKey);
		templateMap.put("entityName", templateName);
		templateMap.put("entityType", "template");

		//https://stackoverflow.com/questions/17022363/track-url-change-with-google-analytics-without-reloading-the-page
		Map<String, String> childTemplateDetails = new HashMap<>();
		String innerTemplate = templateEngine.processTemplateContents(templateInnerVO.getTemplate(), templateInnerVO.getTemplateName(), templateDetails);
		childTemplateDetails.put("template-body", innerTemplate);
		if(templateDetails.get("entityType") == null) {
			Map<String, Object> entityDetails = new HashMap<String, Object>();
			entityDetails.put("entityType", "template");
			entityDetails.put("entityName", templateName);
			templateMap.putAll(entityDetails);
		}
		return templateEngine.processMultipleTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(), templateMap, childTemplateDetails);

	}
	
	public String getTemplateWithSiteLayoutWithoutProcess(String templateContent, Map<String,Object> templateDetails) throws Exception {
		String jquiverVersion = propertyMasterService.findPropertyMasterValue(Constant.SYSTEM_OWNER_TYPE
				, Constant.SYSTEM_OWNER_ID, Constant.JQUIVER_VERSION_PROPERTY_NAME);
		Map<String, Object> templateMap = new HashMap<>();
		List<ModuleDetailsVO> moduleDetailsVOList = moduleService.getAllMenuModules();
		templateMap.put("moduleDetailsVOList", moduleDetailsVOList);
		templateMap.put("jquiverVersion", jquiverVersion);
		TemplateVO templateVO = templatingService.getTemplateByName("home-page");
		Map<String, String> childTemplateDetails = new HashMap<>();
		childTemplateDetails.put("template-body", templateContent);
		
		String enableGoogleAnalytics 		= propertyMasterDAO.findPropertyMasterValue("system", "system", "enable-google-analytics");
		String googleAnalyticsKey 			= propertyMasterDAO.findPropertyMasterValue("system", "system", "google-analytics-key");
		templateMap.put("enableGoogleAnalytics", enableGoogleAnalytics);
		templateMap.put("googleAnalyticsKey", googleAnalyticsKey);
		templateMap.putAll(templateDetails);

		if(templateDetails.get("formName")!=null && templateDetails.get("formName").equals("")==false ) {
			templateMap.put("pageName", templateDetails.get("formName"));
			if(templateDetails.get("formDescription")!=null && templateDetails.get("formDescription").equals("")==false ){
				templateMap.put("title", templateDetails.get("formDescription"));
			}

		}
		return templateEngine.processMultipleTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(), templateMap, childTemplateDetails);

	}
	
	public String getDashletTemplateWithLayout(String template, Map<String, Object> templateParamMap) throws Exception{
		String jquiverVersion = propertyMasterService.findPropertyMasterValue(Constant.SYSTEM_OWNER_TYPE
				, Constant.SYSTEM_OWNER_ID, Constant.JQUIVER_VERSION_PROPERTY_NAME);
		Map<String, Object> templateMap = new HashMap<>();
		if(templateParamMap != null) {
			templateMap.putAll(templateParamMap);
		}
		List<ModuleDetailsVO> moduleDetailsVOList = moduleService.getAllMenuModules();
		templateMap.put("moduleDetailsVOList", moduleDetailsVOList);
		templateMap.put("jquiverVersion", jquiverVersion);
		TemplateVO templateVO = templatingService.getTemplateByName("home-page");
		Map<String, String> childTemplateDetails = new HashMap<>();
		childTemplateDetails.put("template-body", template);
		String enableGoogleAnalytics = propertyMasterDAO.findPropertyMasterValue("system", "system", "enable-google-analytics");
		String googleAnalyticsKey = propertyMasterDAO.findPropertyMasterValue("system", "system", "google-analytics-key");
		templateMap.put("enableGoogleAnalytics", enableGoogleAnalytics);
		templateMap.put("googleAnalyticsKey", googleAnalyticsKey);
		templateMap.put("pageName", templateVO.getTemplateName());
		if(templateParamMap.get("entityType") == null) {
			Map<String, Object> entityDetails = new HashMap<String, Object>();
			entityDetails.put("entityType", "dashboard");
			entityDetails.put("entityName", templateParamMap);
			templateMap.putAll(entityDetails);
		}
		return templateEngine.processMultipleTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(), templateMap, childTemplateDetails);
	}
	
	public String getTemplateWithoutLayout(String template, Map<String, Object> templateParamMap) throws Exception{
		Map<String, Object> templateMap = new HashMap<>();
		if(templateParamMap != null) {
			templateMap.putAll(templateParamMap);
		}
		TemplateVO templateVO = templatingService.getTemplateByName(template);
		String enableGoogleAnalytics = propertyMasterDAO.findPropertyMasterValue("system", "system", "enable-google-analytics");
		String googleAnalyticsKey = propertyMasterDAO.findPropertyMasterValue("system", "system", "google-analytics-key");
		templateMap.put("enableGoogleAnalytics", enableGoogleAnalytics);
		templateMap.put("googleAnalyticsKey", googleAnalyticsKey);
		templateMap.put("pageName", templateVO.getTemplateName());
		return templateEngine.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(), templateMap);
	}
}
