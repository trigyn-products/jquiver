package com.trigyn.jws.formio.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

public class SelectFieldGenerator implements FormFieldGenerator {

	@Override
	public Map<String, Object> generateField(Map<String, Object> columnDetails) throws JSONException {
		Map<String, Object> map = new HashMap<>();

		map.put("label", columnDetails.get("fieldName"));
		map.put("placeholder", "Select an option");
		map.put("key", columnDetails.get("columnName"));
		map.put("type", "select");
		map.put("input", true);
		map.put("tableView", true);
		map.put("validateWhenHidden", false);
		map.put("dataSrc", "values"); // ⬅ Required for non-choicesjs
		map.put("widget", "choicesjs");

		// Set default value if available
		List<String> dbValues = (List<String>) columnDetails.get("dbValues");
		if (dbValues != null && !dbValues.isEmpty()) {
			map.put("defaultValue", dbValues.get(0));
		}

		// Add values to the "data.values" key
		if (dbValues != null && !dbValues.isEmpty()) {
			List<Map<String, String>> valuesList = new ArrayList<>();
			for (String val : dbValues) {
				Map<String, String> valueMap = new HashMap<>();
				valueMap.put("label", val); // Label shown in dropdown
				valueMap.put("value", val); // Value submitted on select
				valuesList.add(valueMap);
			}

			Map<String, Object> dataMap = new HashMap<>();
			dataMap.put("values", valuesList); // Assign choices to 'data.values'
			map.put("data", dataMap);
		}

		// Validation
		Map<String, Object> validateMap = new HashMap<>();
		validateMap.put("required", columnDetails.get("isMandatory"));
		map.put("validate", validateMap);

		return map;
	}
}
