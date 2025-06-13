package com.trigyn.jws.formio.utils;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

public class HtmlFieldGenerator implements FormFieldGenerator {

	@Override
	public Map<String, Object> generateField(Map<String, Object> columnDetails) throws JSONException {
		
		Map<String, Object> htmlMap = new HashMap<>();
        htmlMap.put("label", columnDetails.get("fieldName"));
        htmlMap.put("content", columnDetails.get("content"));
        htmlMap.put("refreshOnChange", false);
        htmlMap.put("key", columnDetails.get("columnName"));
        htmlMap.put("type", "htmlelement");
        htmlMap.put("input", false);
        htmlMap.put("tableView", false);
        
		return htmlMap;
	}

}
