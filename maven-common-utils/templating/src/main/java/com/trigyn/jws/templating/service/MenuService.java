package com.trigyn.jws.templating.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.dbutils.entities.PropertyMaster;
import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.dbutils.repository.PropertyMasterRepository;
import com.trigyn.jws.dbutils.service.ModuleService;
import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dbutils.vo.ModuleDetailsVO;
import com.trigyn.jws.templating.utils.Constant;
import com.trigyn.jws.templating.utils.TemplatingUtils;
import com.trigyn.jws.templating.vo.TemplateVO;
import com.trigyn.jws.usermanagement.security.config.ApplicationSecurityDetails;

@Service
@Transactional
public class MenuService {

	private final static Logger			logger						= LogManager.getLogger(MenuService.class);

	@Autowired
	private DBTemplatingService			templatingService			= null;

	@Autowired
	private ModuleService				moduleService				= null;

	@Autowired
	private TemplatingUtils				templateEngine				= null;

	@Autowired
	private PropertyMasterDAO			propertyMasterDAO			= null;

	@Autowired
	private PropertyMasterService		propertyMasterService		= null;

	@Autowired
	private PropertyMasterRepository	propertyMasterRepository	= null;

	@Autowired
	private ApplicationSecurityDetails	applicationSecurityDetails	= null;

	public String getTemplateWithSiteLayout(String templateName, Map<String, Object> templateDetails) throws Exception {
		logger.debug("Inside MenuService.getTemplateWithSiteLayout(templateName{}, templateDetails{})", templateName,
				templateDetails);
		String					jquiverVersion		= propertyMasterService.findPropertyMasterValue(
				Constant.SYSTEM_OWNER_TYPE, Constant.SYSTEM_OWNER_ID, Constant.JQUIVER_VERSION_PROPERTY_NAME);
		Map<String, Object>		templateMap			= new HashMap<>();
		List<ModuleDetailsVO>	moduleDetailsVOList	= moduleService.getAllMenuModules();
		templateMap.put("moduleDetailsVOList", moduleDetailsVOList);
		templateMap.put("jquiverVersion", jquiverVersion);
		PropertyMaster propertyMaster = propertyMasterRepository.findByOwnerTypeAndOwnerIdAndPropertyName("system",
				"system", "enable-user-management");
		templateMap.put("isAuthEnabled", Boolean.parseBoolean(propertyMaster.getPropertyValue()));
		templateMap.put("isAuthEnabled", applicationSecurityDetails.getIsAuthenticationEnabled());
		templateMap.putAll(templateDetails);
		TemplateVO	templateVO				= templatingService.getTemplateByName("home-page");
		TemplateVO	templateInnerVO			= templatingService.getTemplateByName(templateName);
		String		enableGoogleAnalytics	= propertyMasterDAO.findPropertyMasterValue("system", "system",
				"enable-google-analytics");
		String		googleAnalyticsKey		= propertyMasterDAO.findPropertyMasterValue("system", "system",
				"google-analytics-key");
		templateMap.put("enableGoogleAnalytics", enableGoogleAnalytics);
		templateMap.put("googleAnalyticsKey", googleAnalyticsKey);
		templateMap.put("entityName", templateName);
		templateMap.put("entityType", "template");

		// https://stackoverflow.com/questions/17022363/track-url-change-with-google-analytics-without-reloading-the-page
		Map<String, String>	childTemplateDetails	= new HashMap<>();
		String				innerTemplate			= templateEngine.processTemplateContents(
				templateInnerVO.getTemplate(), templateInnerVO.getTemplateName(), templateDetails);
		childTemplateDetails.put("template-body", innerTemplate);
		if (templateDetails.get("entityType") == null) {
			Map<String, Object> entityDetails = new HashMap<String, Object>();
			entityDetails.put("entityType", "template");
			entityDetails.put("entityName", templateName);
			templateMap.putAll(entityDetails);
		}
		return templateEngine.processMultipleTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(),
				templateMap, childTemplateDetails);

	}

	public String getTemplateWithSiteLayoutWithoutProcess(String templateContent, Map<String, Object> templateDetails)
			throws Exception {
		logger.debug("Inside MenuService.getTemplateWithSiteLayoutWithoutProcess(templateContent{}, templateDetails{})",
				templateContent, templateDetails);
		String					jquiverVersion		= propertyMasterService.findPropertyMasterValue(
				Constant.SYSTEM_OWNER_TYPE, Constant.SYSTEM_OWNER_ID, Constant.JQUIVER_VERSION_PROPERTY_NAME);
		Map<String, Object>		templateMap			= new HashMap<>();
		List<ModuleDetailsVO>	moduleDetailsVOList	= moduleService.getAllMenuModules();
		templateMap.put("moduleDetailsVOList", moduleDetailsVOList);
		templateMap.put("jquiverVersion", jquiverVersion);
		PropertyMaster propertyMaster = propertyMasterRepository.findByOwnerTypeAndOwnerIdAndPropertyName("system",
				"system", "enable-user-management");
		templateMap.put("isAuthEnabled", Boolean.parseBoolean(propertyMaster.getPropertyValue()));
		templateMap.put("isAuthEnabled", applicationSecurityDetails.getIsAuthenticationEnabled());
		TemplateVO			templateVO				= templatingService.getTemplateByName("home-page");
		Map<String, String>	childTemplateDetails	= new HashMap<>();
		childTemplateDetails.put("template-body", templateContent);

		String	enableGoogleAnalytics	= propertyMasterDAO.findPropertyMasterValue("system", "system",
				"enable-google-analytics");
		String	googleAnalyticsKey		= propertyMasterDAO.findPropertyMasterValue("system", "system",
				"google-analytics-key");
		templateMap.put("enableGoogleAnalytics", enableGoogleAnalytics);
		templateMap.put("googleAnalyticsKey", googleAnalyticsKey);
		templateMap.putAll(templateDetails);

		if (templateDetails.get("formName") != null && templateDetails.get("formName").equals("") == false) {
			templateMap.put("pageName", templateDetails.get("formName"));
			if (templateDetails.get("formDescription") != null
					&& templateDetails.get("formDescription").equals("") == false) {
				templateMap.put("title", templateDetails.get("formDescription"));
			}

		}
		return templateEngine.processMultipleTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(),
				templateMap, childTemplateDetails);

	}

	public String getDashletTemplateWithLayout(String template, Map<String, Object> templateParamMap) throws Exception {
		logger.debug("Inside MenuService.getDashletTemplateWithLayout(template{}, templateParamMap{})", template,
				templateParamMap);
		String				jquiverVersion	= propertyMasterService.findPropertyMasterValue(Constant.SYSTEM_OWNER_TYPE,
				Constant.SYSTEM_OWNER_ID, Constant.JQUIVER_VERSION_PROPERTY_NAME);
		Map<String, Object>	templateMap		= new HashMap<>();
		if (templateParamMap != null) {
			templateMap.putAll(templateParamMap);
		}
		List<ModuleDetailsVO> moduleDetailsVOList = moduleService.getAllMenuModules();
		templateMap.put("moduleDetailsVOList", moduleDetailsVOList);
		templateMap.put("jquiverVersion", jquiverVersion);
		PropertyMaster propertyMaster = propertyMasterRepository.findByOwnerTypeAndOwnerIdAndPropertyName("system",
				"system", "enable-user-management");
		templateMap.put("isAuthEnabled", Boolean.parseBoolean(propertyMaster.getPropertyValue()));
		templateMap.put("isAuthEnabled", applicationSecurityDetails.getIsAuthenticationEnabled());
		TemplateVO			templateVO				= templatingService.getTemplateByName("home-page");
		Map<String, String>	childTemplateDetails	= new HashMap<>();
		childTemplateDetails.put("template-body", template);
		String	enableGoogleAnalytics	= propertyMasterDAO.findPropertyMasterValue("system", "system",
				"enable-google-analytics");
		String	googleAnalyticsKey		= propertyMasterDAO.findPropertyMasterValue("system", "system",
				"google-analytics-key");
		templateMap.put("enableGoogleAnalytics", enableGoogleAnalytics);
		templateMap.put("googleAnalyticsKey", googleAnalyticsKey);
		templateMap.put("pageName", templateVO.getTemplateName());
		if (templateParamMap.get("entityType") == null) {
			Map<String, Object> entityDetails = new HashMap<String, Object>();
			entityDetails.put("entityType", "dashboard");
			entityDetails.put("entityName", templateParamMap.get("templateName"));
			templateMap.putAll(entityDetails);
		}
		return templateEngine.processMultipleTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(),
				templateMap, childTemplateDetails);
	}

	public String getTemplateWithoutLayout(String template, Map<String, Object> templateParamMap) throws Exception {
		logger.debug("Inside MenuService.getTemplateWithoutLayout(template{}, templateParamMap{})", template,
				templateParamMap);
		Map<String, Object> templateMap = new HashMap<>();
		if (templateParamMap != null) {
			templateMap.putAll(templateParamMap);
		}
		TemplateVO	templateVO				= templatingService.getTemplateByName(template);
		String		enableGoogleAnalytics	= propertyMasterDAO.findPropertyMasterValue("system", "system",
				"enable-google-analytics");
		String		googleAnalyticsKey		= propertyMasterDAO.findPropertyMasterValue("system", "system",
				"google-analytics-key");
		templateMap.put("enableGoogleAnalytics", enableGoogleAnalytics);
		templateMap.put("googleAnalyticsKey", googleAnalyticsKey);
		templateMap.put("pageName", templateVO.getTemplateName());
		return templateEngine.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(),
				templateMap);
	}

	public String getDashletWithoutLayout(String templateName, String template, Map<String, Object> templateParamMap)
			throws Exception {
		logger.debug("Inside MenuService.getTemplateWithoutLayout(template{}, templateParamMap{})", template,
				templateParamMap);
		Map<String, Object> templateMap = new HashMap<>();
		if (templateParamMap != null) {
			templateMap.putAll(templateParamMap);
		}
		String	enableGoogleAnalytics	= propertyMasterDAO.findPropertyMasterValue("system", "system",
				"enable-google-analytics");
		String	googleAnalyticsKey		= propertyMasterDAO.findPropertyMasterValue("system", "system",
				"google-analytics-key");
		templateMap.put("enableGoogleAnalytics", enableGoogleAnalytics);
		templateMap.put("googleAnalyticsKey", googleAnalyticsKey);
		templateMap.put("pageName", templateName);
		return templateEngine.processTemplateContents(template, templateName, templateMap);
	}
}
