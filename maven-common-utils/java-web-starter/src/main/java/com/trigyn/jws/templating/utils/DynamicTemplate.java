package com.trigyn.jws.templating.utils;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.trigyn.jws.dbutils.utils.JwsCustomException;
import com.trigyn.jws.templating.service.MenuService;

@Component
public class DynamicTemplate {

	@Autowired
	private MenuService		menuService		= null;

	@Autowired
	private TemplatingUtils	templatingUtils	= null;

	public String includeTemplate(String templateName, Object requestObject) throws Exception {
		Map<String, Object> templateMap = new HashMap<>();
		templateMap.put("innerTemplateObj", requestObject);
		return menuService.getTemplateWithoutLayout(templateName, templateMap);
	}

	public String includeTemplate(String templateName) throws Exception {
		return menuService.getTemplateWithoutLayout(templateName, new HashMap<>());
	}

	public boolean validateRegex(String regexPattern, Object fieldValue, String fieldName, String dataType)
			throws JwsCustomException {

		if (null == regexPattern || null == fieldName || null == dataType)
			return false;

		String errorMessage = MessageFormat.format(Constant.REGEX_INVALID_VALUE, fieldName);

		if (dataType.equalsIgnoreCase("xml")) { // Validation required for Postgress
			if (templatingUtils.isValidXML(fieldValue.toString()) == false) {
				throw new JwsCustomException(errorMessage, HttpStatus.BAD_REQUEST, Constant.REGEXERROR);
			}
		} else if (dataType.equalsIgnoreCase("json")) { // Validation required for Postgress
			if (!templatingUtils.isValidJSON(fieldValue.toString()) == false) {
				throw new JwsCustomException(errorMessage, HttpStatus.BAD_REQUEST, Constant.REGEXERROR);
			}
		} else {
			if (Pattern.matches(regexPattern, fieldValue.toString()) == false) {
				throw new JwsCustomException(errorMessage, HttpStatus.BAD_REQUEST, Constant.REGEXERROR);
			}
			;
		}

		return true;
	}
}
