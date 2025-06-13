package com.trigyn.jws.formio.utils;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

public class CurrencyFieldGenerator implements FormFieldGenerator {

	@Override
	public Map<String, Object> generateField(Map<String, Object> columnDetails) throws JSONException {
		 Map<String, Object> field = new HashMap<>();
	        field.put("label", columnDetails.get("fieldName"));
	        field.put("applyMaskOn", "change");
	        field.put("mask", false);
	        field.put("tableView", false);
	        field.put("currency", "USD");
	        field.put("inputFormat", "plain");
	        field.put("truncateMultipleSpaces", false);
	        field.put("validateWhenHidden", false);
	        field.put("key", columnDetails.get("columnName"));
	        field.put("type", "currency");
	        field.put("input", true);
	        field.put("delimiter", true);

	        Map<String, Object> validate = new HashMap<>();
	        validate.put("required", columnDetails.get("isMandatory"));

	        field.put("validate", validate);
		return field;
	}

}
