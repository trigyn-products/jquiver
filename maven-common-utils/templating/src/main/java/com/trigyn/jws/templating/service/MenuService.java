package com.trigyn.jws.templating.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.dbutils.service.ModuleService;
import com.trigyn.jws.dbutils.vo.ModuleDetailsVO;
import com.trigyn.jws.templating.utils.TemplatingUtils;
import com.trigyn.jws.templating.vo.TemplateVO;

@Service
@Transactional
public class MenuService {

	@Autowired
	private DBTemplatingService templatingService 	= null;

	@Autowired
	private ModuleService moduleService 			= null;

	@Autowired
	private TemplatingUtils templateEngine 			= null;

	public String getTemplateWithSiteLayout(String templateName, Map<String,Object> templateDetails) throws Exception {
		Map<String, Object> templateMap = new HashMap<>();
		List<ModuleDetailsVO> moduleDetailsVOList = moduleService.getAllMenuModules();
		templateMap.put("moduleDetailsVOList", moduleDetailsVOList);
		templateMap.putAll(templateDetails);
		TemplateVO templateVO = templatingService.getTemplateByName("home-page");
		TemplateVO templateInnerVO = templatingService.getTemplateByName(templateName);
		Map<String, String> childTemplateDetails = new HashMap<>();
		String innerTemplate = templateEngine.processTemplateContents(templateInnerVO.getTemplate(), templateInnerVO.getTemplateName(), templateDetails);
		childTemplateDetails.put("template-body", innerTemplate);
		return templateEngine.processMultipleTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(), templateMap, childTemplateDetails);

	}
	
	public String getTemplateWithSiteLayoutWithoutProcess(String templateContent, Map<String,Object> templateDetails) throws Exception {
		Map<String, Object> templateMap = new HashMap<>();
		List<ModuleDetailsVO> moduleDetailsVOList = moduleService.getAllMenuModules();
		templateMap.put("moduleDetailsVOList", moduleDetailsVOList);
		TemplateVO templateVO = templatingService.getTemplateByName("home-page");
		Map<String, String> childTemplateDetails = new HashMap<>();
		childTemplateDetails.put("template-body", templateContent);
		return templateEngine.processMultipleTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(), templateMap, childTemplateDetails);

	}
	
	public String getDashletTemplateWithLayout(String template, Map<String, Object> templateParamMap) throws Exception{
		Map<String, Object> templateMap = new HashMap<>();
		if(templateParamMap != null) {
			templateMap.putAll(templateParamMap);
		}
		List<ModuleDetailsVO> moduleDetailsVOList = moduleService.getAllMenuModules();
		templateMap.put("moduleDetailsVOList", moduleDetailsVOList);
		TemplateVO templateVO = templatingService.getTemplateByName("home-page");
		Map<String, String> childTemplateDetails = new HashMap<>();
		childTemplateDetails.put("template-body", template);
		return templateEngine.processMultipleTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(), templateMap, childTemplateDetails);
	}
	
}
