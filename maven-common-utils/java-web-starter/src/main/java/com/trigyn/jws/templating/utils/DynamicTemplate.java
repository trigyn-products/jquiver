package com.trigyn.jws.templating.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trigyn.jws.templating.service.MenuService;

@Component
public class DynamicTemplate {

	@Autowired
	private MenuService menuService = null;

	public String includeTemplate(String templateName, Object requestObject) throws Exception {
		Map<String, Object> templateMap = new HashMap<>();
		templateMap.put("innerTemplateObj", requestObject);
		return menuService.getTemplateWithoutLayout(templateName, templateMap);
	}

	public String includeTemplate(String templateName) throws Exception {
		return menuService.getTemplateWithoutLayout(templateName, new HashMap<>());
	}
}
