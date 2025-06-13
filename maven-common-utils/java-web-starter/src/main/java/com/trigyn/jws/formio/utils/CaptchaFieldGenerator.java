package com.trigyn.jws.formio.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

public class CaptchaFieldGenerator implements FormFieldGenerator {

	@Override
	public Map<String, Object> generateField(Map<String, Object> columnDetails) throws JSONException {
		
		// Create the main map
        Map<String, Object> captchaMap = new LinkedHashMap<>();
        captchaMap.put("label", columnDetails.get("fieldName"));
        // Create the attrs list
        List<Map<String, String>> attrsList = new ArrayList<>();
        Map<String, String> attrEntry = new HashMap<>();
        attrEntry.put("attr", "");
        attrEntry.put("value", "");
        attrsList.add(attrEntry);
        captchaMap.put("attrs", attrsList);
        // Add the HTML content
        captchaMap.put("content",
        		"<div class=\"row\">\r\n\t <div class=\"col-lg-4 col-12\" >\r\n\t\t\t<div class=\"col-inner-form full-form-fields\">\r\n                 <div class=\"captchablock\">\r\n\t\t\t\t\t <img id=\"imgCaptcha\" name=\"imgCaptcha\" src=\"\">\r\n\t\t\t\t\t  <span id=\"reloadCaptcha\" onclick=\"reloadCptcha()\">\r\n\t\t\t\t\t    <i class=\"fa fa-refresh\" aria-hidden=\"true\" onclick=\"reloadCptcha()\"> </i></span>\r\n\t\t\t\t\t<label for=\"captcha\" onclick=\"reloadCptcha()\" class=\"sr-only\">${messageSource.getMessage('jws.entercaptcha')}</label>\r\n\t\t\t\t\t<input type=\"text\" id=\"formCaptcha\" name=\"formCaptcha\" class=\"form-control\" placeholder=\"Enter Captcha\" required autofocus >\r\n                    </div>\r\n\t\t\t</div>\r\n\t\t</div>\r\n    </div>");

        captchaMap.put("refreshOnChange", false);
        captchaMap.put("key",  columnDetails.get("columnName"));
        captchaMap.put("type", "htmlelement");
        captchaMap.put("input", false);
        captchaMap.put("tableView", false);
        
		return captchaMap;
	}

}
