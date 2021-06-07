package com.trigyn.jws.dynarest.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.trigyn.jws.dbutils.spi.PropertyMasterDetails;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.resourcebundle.repository.interfaces.IResourceBundleRepository;
import com.trigyn.jws.resourcebundle.vo.ResourceBundleVO;
import com.trigyn.jws.templating.service.MenuService;

public class DynaRest {

	private final static Logger			logger						= LogManager.getLogger(DynaRest.class);

	@Autowired
	private PropertyMasterDetails		propertyMasterDetails		= null;

	@Autowired
	private IResourceBundleRepository	iResourceBundleRepository	= null;

	@Autowired
	private MenuService					menuService					= null;

	/**
	 * 
	 * Method to get dynamic rest details
	 *
	 */
	public Map<String, Object> getDynamicRestDetails(HttpServletRequest a_httpServletRequest, Map<String, Object> dAOparameters,
		UserDetailsVO userDetails) {
		Map<String, Object> responseMap = new HashMap<>();
		responseMap.put("methodTypes", dAOparameters.get("dynarestMethodType"));
		responseMap.put("producerDetails", dAOparameters.get("dynarestProducerDetails"));
		responseMap.put("dynarestDetails", dAOparameters.get("dynarestDetails"));
		return responseMap;
	}

	/**
	 * 
	 * Method to get default template listing
	 *
	 */
	public Map<String, Object> defaultTemplates(HttpServletRequest a_httpServletRequest, Map<String, Object> dAOparameters,
		UserDetailsVO userDetails) {
		Map<String, Object> responseMap = new HashMap<>();
		responseMap.put("defaultTemplates", dAOparameters.get("defaultTemplates"));
		return responseMap;
	}

	/**
	 * 
	 * Method to get value from property master
	 *
	 */
	public String getPropertyValueFromPropertyMaster(HttpServletRequest a_httpServletRequest, Map<String, Object> daoResultSets,
		UserDetailsVO userDetails) {
		String	ownerId			= a_httpServletRequest.getParameter("ownerId");
		String	ownerType		= a_httpServletRequest.getParameter("ownerType");
		String	propertyName	= a_httpServletRequest.getParameter("propertyName");
		String	propertyValue	= propertyMasterDetails.getPropertyValueFromPropertyMaster(ownerId, ownerType, propertyName);
		if (StringUtils.isBlank(propertyValue) == false) {
			System.out.println("Welcome to Property Master: " + propertyValue);
		}
		return propertyValue;
	}

	/**
	 * 
	 * Method to get translation from resource bundle
	 *
	 */
	public Map<String, Object> getTranslationByKey(HttpServletRequest a_httpServletRequest, Map<String, Object> daoResultSets,
		UserDetailsVO userDetails) {
		Map<String, Object>		responseMap				= new HashMap<>();
		String					resourceBundleKey		= a_httpServletRequest.getParameter("resourceBundleKey");
		List<ResourceBundleVO>	resourceBundleVOList	= iResourceBundleRepository.findResourceBundleByKey(resourceBundleKey);
		if (!CollectionUtils.isEmpty(resourceBundleVOList)) {
			System.out.println("Welcome to Resource Bundle ");
			for (ResourceBundleVO resourceBundleVO : resourceBundleVOList) {
				responseMap.put(resourceBundleVO.getLanguageId().toString(), resourceBundleVO.getText());
			}
		}
		return responseMap;
	}

	/**
	 * 
	 * Method to get template content by name and process template content
	 *
	 */
	public String getTemplate(HttpServletRequest a_httpServletRequest, Map<String, Object> daoResultSets, UserDetailsVO userDetails)
			throws Exception {
		Map<String, Object>	templateMap		= new HashMap<String, Object>();
		String				templateName	= a_httpServletRequest.getParameter("templateName");
		String				templateContent	= menuService.getTemplateWithSiteLayout(templateName, templateMap);
		return templateContent;
	}

	public String getDynarestBaseUrl(HttpServletRequest a_httpServletRequest, Map<String, Object> daoResultSets,
		UserDetailsVO userDetails) {
		String			uri			= a_httpServletRequest.getRequestURI().substring(a_httpServletRequest.getContextPath().length());
		String			url			= a_httpServletRequest.getRequestURL().toString();
		StringBuilder	urlPrefix	= new StringBuilder();
		url = url.replace(uri, "");
		urlPrefix.append(url).append("/api/");
		return urlPrefix.toString();
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.menuService				= applicationContext.getBean(MenuService.class);
		this.propertyMasterDetails		= applicationContext.getBean(PropertyMasterDetails.class);
		this.iResourceBundleRepository	= applicationContext.getBean(IResourceBundleRepository.class);
	}
}
