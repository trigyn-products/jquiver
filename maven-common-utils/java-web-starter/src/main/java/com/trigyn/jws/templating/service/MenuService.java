package com.trigyn.jws.templating.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.dbutils.entities.PropertyMaster;
import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.dbutils.repository.PropertyMasterRepository;
import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dbutils.spi.PropertyMasterDetails;
import com.trigyn.jws.dbutils.utils.ApplicationContextUtils;
import com.trigyn.jws.dbutils.utils.CustomStopException;
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

	@Autowired
	private PropertyMasterDetails				propertyMasterDetails				= null;
	
	public String getTemplateWithSiteLayout(String templateName, Map<String, Object> templateDetails)
			throws Exception, CustomStopException {
		logger.debug("Inside MenuService.getTemplateWithSiteLayout(templateName: {}, templateDetails: {})",
				templateName, templateDetails);
		try {
			String jquiverVersion = propertyMasterService.findPropertyMasterValue(Constant.SYSTEM_OWNER_TYPE,
					Constant.SYSTEM_OWNER_ID, Constant.JQUIVER_VERSION_PROPERTY_NAME);
			Map<String, Object> templateMap = new HashMap<>();
			List<ModuleDetailsVO> moduleDetailsVOList = moduleService.getAllMenuModules();
			templateMap.put("moduleDetailsVOList", moduleDetailsVOList);
			templateMap.put("jquiverVersion", jquiverVersion);
			PropertyMaster propertyMaster = propertyMasterRepository.findByOwnerTypeAndOwnerIdAndPropertyName("system",
					"system", "enable-user-management");
			templateMap.put("isAuthEnabled", Boolean.parseBoolean(propertyMaster.getPropertyValue()));
			templateMap.put("isAuthEnabled", applicationSecurityDetails.getIsAuthenticationEnabled());
			templateMap.putAll(templateDetails);
			TemplateVO templateVO = templatingService.getTemplateByName("home-page");
			String suffix = propertyMasterDetails.getAllProperties().get("template_suffix");
			if (suffix == null)
				suffix = "";
			if (templatingService.getTemplateByNameWithoutAuthorization(templateName) != null) {
				templateName = templateName + suffix;
			}
			TemplateVO templateInnerVO = templatingService.getTemplateByName(templateName);
			if (templateInnerVO == null) {

				Integer statusCode = ApplicationContextUtils.getThreadLocal().get();
				if (statusCode == null) {
					templateDetails.put("statusCode", 500);
				} else {
					templateDetails.put("statusCode", statusCode);
					if (statusCode == 403) {
						templateDetails.put("errorMessage", "You do not have enough privilege to access this module");
					}
				}
				if (templatingService.getTemplateByNameWithoutAuthorization("error-page") != null) {
					templateInnerVO = templatingService.getTemplateByName("error-page" + suffix);
					;
				} else {
					templateInnerVO = templatingService.getTemplateByName("error-page");
				}
			}

			String enableGoogleAnalytics = propertyMasterDAO.findPropertyMasterValue("system", "system",
					"enable-google-analytics");
			String googleAnalyticsKey = propertyMasterDAO.findPropertyMasterValue("system", "system",
					"google-analytics-key");
			templateMap.put("enableGoogleAnalytics", enableGoogleAnalytics);
			templateMap.put("googleAnalyticsKey", googleAnalyticsKey);
			templateMap.put("entityName", templateName);
			templateMap.put("entityType", "template");

			// https://stackoverflow.com/questions/17022363/track-url-change-with-google-analytics-without-reloading-the-page
			Map<String, String> childTemplateDetails = new HashMap<>();
//		String				innerTemplate			= templateEngine.processTemplateContents(templateInnerVO.getTemplate(),
//				templateInnerVO.getTemplateName(), templateDetails);
//		childTemplateDetails.put("template-body", innerTemplate);
			childTemplateDetails = templateDetails.entrySet().stream()
					.collect(Collectors.toMap(Map.Entry::getKey, e -> String.valueOf(e.getValue())));
			childTemplateDetails.put("template-body", templateInnerVO.getTemplate());
			if (templateDetails.get("entityType") == null) {
				Map<String, Object> entityDetails = new HashMap<String, Object>();
				entityDetails.put("entityType", "template");
				entityDetails.put("entityName", templateName);
				templateMap.putAll(entityDetails);
			}
			return templateEngine.processMultipleTemplateContents(templateVO.getTemplate(),
					templateVO.getTemplateName(), templateMap, childTemplateDetails);
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in getTemplateWithSiteLayout.", custStopException);
			throw custStopException;
		}
	}

	public String getDashletWithLayout(String template, Map<String, Object> templateParamMap) throws Exception, CustomStopException {
		logger.debug("Inside MenuService.getDashletTemplateWithLayout(template: {}, templateParamMap: {})", template, templateParamMap);
		try {
		String				jquiverVersion	= propertyMasterService.findPropertyMasterValue(Constant.SYSTEM_OWNER_TYPE,
				Constant.SYSTEM_OWNER_ID, Constant.JQUIVER_VERSION_PROPERTY_NAME);
		Map<String, Object>	templateMap		= new HashMap<>();
		if (templateParamMap != null) {
			templateMap.putAll(templateParamMap);
		}
		List<ModuleDetailsVO> moduleDetailsVOList = moduleService.getAllMenuModules();
		templateMap.put("moduleDetailsVOList", moduleDetailsVOList);
		templateMap.put("jquiverVersion", jquiverVersion);
		PropertyMaster propertyMaster = propertyMasterRepository.findByOwnerTypeAndOwnerIdAndPropertyName("system", "system",
				"enable-user-management");
		templateMap.put("isAuthEnabled", Boolean.parseBoolean(propertyMaster.getPropertyValue()));
		templateMap.put("isAuthEnabled", applicationSecurityDetails.getIsAuthenticationEnabled());
		TemplateVO			templateVO				= templatingService.getTemplateByName("home-page");
		//childTemplateDetails.put("template-body", template);
		String	enableGoogleAnalytics	= propertyMasterDAO.findPropertyMasterValue("system", "system", "enable-google-analytics");
		String	googleAnalyticsKey		= propertyMasterDAO.findPropertyMasterValue("system", "system", "google-analytics-key");
		templateMap.put("enableGoogleAnalytics", enableGoogleAnalytics);
		templateMap.put("googleAnalyticsKey", googleAnalyticsKey);
		templateMap.put("pageName", templateVO.getTemplateName());
		templateMap.put("entityName", templateParamMap.get("templateName"));
		templateMap.put("entityType", "dashboard");
		
		TemplateVO templateInnerVO = new TemplateVO();
		String suffix = propertyMasterDetails.getAllProperties().get("template_suffix");
		if (suffix == null)
			suffix = "";
		
		if (templatingService.getTemplateByNameWithoutAuthorization("error-page") != null) {
			templateInnerVO = templatingService.getTemplateByName("error-page" + suffix);
			;
		} else {
			templateInnerVO = templatingService.getTemplateByName("error-page");
		}
		
		Map<String, String> childTemplateDetails = new HashMap<>();
			childTemplateDetails = templateParamMap.entrySet().stream()
					.collect(Collectors.toMap(Map.Entry::getKey, e -> String.valueOf(e.getValue())));
			if(template != null) {
				childTemplateDetails.put("template-body", template);
			} else {
				childTemplateDetails.put("template-body", templateInnerVO.getTemplate());
			}
		if (templateParamMap.get("entityType") == null) {
			Map<String, Object> entityDetails = new HashMap<String, Object>();
			entityDetails.put("entityType", "dashboard");
			entityDetails.put("entityName", templateParamMap.get("templateName"));
			templateMap.putAll(entityDetails);
		}
		return templateEngine.processMultipleTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(), templateMap,
				childTemplateDetails);
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in getDashletWithLayout.", custStopException);
			throw custStopException;
		}
	}
	
	public String getDashletWithoutLayout(String templateName, String template, Map<String, Object> templateParamMap) throws Exception,CustomStopException {
		logger.debug("Inside MenuService.getTemplateWithoutLayout(template: {}, templateParamMap: {})", template, templateParamMap);
		try {
		Map<String, Object> templateMap = new HashMap<>();
		if (templateParamMap != null) {
			templateMap.putAll(templateParamMap);
		}
		TemplateVO templateInnerVO = new TemplateVO();
		String suffix = propertyMasterDetails.getAllProperties().get("template_suffix");
		if (suffix == null)
			suffix = "";
		
		if (templatingService.getTemplateByNameWithoutAuthorization("error-page") != null) {
			templateInnerVO = templatingService.getTemplateByName("error-page" + suffix);
			;
		} else {
			templateInnerVO = templatingService.getTemplateByName("error-page");
		}
		if(null == template)
		{
			template = templateInnerVO.getTemplate();
		}
		String	enableGoogleAnalytics	= propertyMasterDAO.findPropertyMasterValue("system", "system", "enable-google-analytics");
		String	googleAnalyticsKey		= propertyMasterDAO.findPropertyMasterValue("system", "system", "google-analytics-key");
		templateMap.put("enableGoogleAnalytics", enableGoogleAnalytics);
		templateMap.put("googleAnalyticsKey", googleAnalyticsKey);
		templateMap.put("pageName", templateName);
		return templateEngine.processTemplateContents(template, templateName, templateMap);
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in getDashletWithoutLayout.", custStopException);
			throw custStopException;
		}
	}
	
	public String getTemplateWithSiteLayoutWithoutProcess(String templateContent, Map<String, Object> templateDetails) throws Exception, CustomStopException {
		logger.debug("Inside MenuService.getTemplateWithSiteLayoutWithoutProcess(templateContent: {}, templateDetails: {})",
				templateContent, templateDetails);
		try {
		String					jquiverVersion		= propertyMasterService.findPropertyMasterValue(Constant.SYSTEM_OWNER_TYPE,
				Constant.SYSTEM_OWNER_ID, Constant.JQUIVER_VERSION_PROPERTY_NAME);
		Map<String, Object>		templateMap			= new HashMap<>();
		List<ModuleDetailsVO>	moduleDetailsVOList	= moduleService.getAllMenuModules();
		templateMap.put("moduleDetailsVOList", moduleDetailsVOList);
		templateMap.put("jquiverVersion", jquiverVersion);
		PropertyMaster propertyMaster = propertyMasterRepository.findByOwnerTypeAndOwnerIdAndPropertyName("system", "system",
				"enable-user-management");
		templateMap.put("isAuthEnabled", Boolean.parseBoolean(propertyMaster.getPropertyValue()));
		templateMap.put("isAuthEnabled", applicationSecurityDetails.getIsAuthenticationEnabled());
		String suffix = propertyMasterDetails.getAllProperties().get("template_suffix");
		if(suffix == null) suffix = "";
		TemplateVO			templateVO				= null;
		if(templatingService.getTemplateByNameWithoutAuthorization("home-page") != null) {
			templateVO				= templatingService.getTemplateByName("home-page"+suffix);;
		} else {
			templateVO				= templatingService.getTemplateByName("home-page");
		}
		Map<String, String>	childTemplateDetails	= new HashMap<>();
		childTemplateDetails.put("template-body", templateContent);

		String	enableGoogleAnalytics	= propertyMasterDAO.findPropertyMasterValue("system", "system", "enable-google-analytics");
		String	googleAnalyticsKey		= propertyMasterDAO.findPropertyMasterValue("system", "system", "google-analytics-key");
		templateMap.put("enableGoogleAnalytics", enableGoogleAnalytics);
		templateMap.put("googleAnalyticsKey", googleAnalyticsKey);
		templateMap.putAll(templateDetails);

		if (templateDetails.get("formName") != null && templateDetails.get("formName").equals("") == false) {
			templateMap.put("pageName", templateDetails.get("formName"));
			if (templateDetails.get("formDescription") != null && templateDetails.get("formDescription").equals("") == false) {
				templateMap.put("title", templateDetails.get("formDescription"));
			}

		}
		return templateEngine.processMultipleTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(), templateMap,
				childTemplateDetails);
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in getTemplateWithSiteLayoutWithoutProcess.", custStopException);
			throw custStopException;
		}
	}

	public String getTemplateWithoutLayout(String template, Map<String, Object> templateParamMap) throws Exception, CustomStopException {
		logger.debug("Inside MenuService.getTemplateWithoutLayout(template: {}, templateParamMap: {})", template, templateParamMap);
		try {
		Map<String, Object> templateMap = new HashMap<>();
		if (templateParamMap != null) {
			templateMap.putAll(templateParamMap);
		}
		String suffix = propertyMasterDetails.getAllProperties().get("template_suffix");
		if(suffix == null) suffix = "";
		if(templatingService.getTemplateByNameWithoutAuthorization(template) != null) {
			template = template+suffix;
		}
		TemplateVO	templateVO				= templatingService.getTemplateByName(template);
		if(templateVO == null) {
			Integer statusCode = ApplicationContextUtils.getThreadLocal().get();
			if(statusCode == null) {
				templateMap.put("statusCode", 500);
			} else {
				templateMap.put("statusCode", statusCode);
				if(statusCode == 403) {
					templateMap.put("errorMessage", "You do not have enough privilege to access this module ");
				} 
			}
			templateVO = templatingService.getTemplateByName("error-page");
		}
		String		enableGoogleAnalytics	= propertyMasterDAO.findPropertyMasterValue("system", "system", "enable-google-analytics");
		String		googleAnalyticsKey		= propertyMasterDAO.findPropertyMasterValue("system", "system", "google-analytics-key");
		templateMap.put("enableGoogleAnalytics", enableGoogleAnalytics);
		templateMap.put("googleAnalyticsKey", googleAnalyticsKey);
		templateMap.put("pageName", templateVO.getTemplateName());
		return templateEngine.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(), templateMap);
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in getTemplateWithoutLayout.", custStopException);
			throw custStopException;
		}
		}
}
