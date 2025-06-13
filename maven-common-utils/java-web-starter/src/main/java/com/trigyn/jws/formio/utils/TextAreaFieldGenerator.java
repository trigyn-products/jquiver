package com.trigyn.jws.formio.utils;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

public class TextAreaFieldGenerator implements FormFieldGenerator {

	@Override
	public Map<String, Object> generateField(Map<String, Object> columnDetails) throws JSONException {
		Map<String, Object> field = new HashMap<>();
		field.put("label", columnDetails.get("fieldName"));
        field.put("applyMaskOn", "change");
        field.put("autoExpand", false);
        field.put("tableView", true);
        field.put("validateWhenHidden", false);
        field.put("key", columnDetails.get("columnName"));
        field.put("type", "textarea");
        field.put("input", true);
		return field;
	}

}
