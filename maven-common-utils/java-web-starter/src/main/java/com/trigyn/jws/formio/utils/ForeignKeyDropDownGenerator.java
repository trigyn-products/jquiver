package com.trigyn.jws.formio.utils;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

public class ForeignKeyDropDownGenerator implements FormFieldGenerator {

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Object> generateField(Map<String, Object> columnDetails) throws JSONException {
		Map<String, Object> field = new HashMap<>();
		Map<String, Object> fkConfig = (Map<String, Object>) columnDetails.get("foreignKeyConfig");
		field.put("label", columnDetails.get("fieldName"));
		field.put("key", columnDetails.get("columnName"));
		field.put("type", "select");
		field.put("input", true);
		field.put("tableView", true);
		field.put("dataSrc", "values");
		Map<String, Object> data = new HashMap<>();
		data.put("values", columnDetails.get("dropdownOptions"));
		field.put("data", data);
		field.put("selectThreshold", 0);
		field.put("searchEnabled", true);
		field.put("lazyLoad", false);
		Map<String, Object> validate = new HashMap<>();
		validate.put("required", columnDetails.get("isMandatory"));
		field.put("validate", validate);
		return field;
	}
}