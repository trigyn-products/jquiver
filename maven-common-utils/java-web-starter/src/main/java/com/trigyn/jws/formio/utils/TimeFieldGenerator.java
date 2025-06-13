package com.trigyn.jws.formio.utils;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

public class TimeFieldGenerator implements FormFieldGenerator {

	@Override
	public Map<String, Object> generateField(Map<String, Object> columnDetails) throws JSONException {
		Map<String, Object> map = new HashMap<>();
        map.put("label", columnDetails.get("fieldName"));
        map.put("tableView", true);
        map.put("validateWhenHidden", false);
        map.put("key", columnDetails.get("columnName"));
        map.put("type", "time");
        map.put("input", true);
        map.put("inputMask", "99:99");
        map.put("applyMaskOn", "change");
        map.put("autoExpand", false);

        // Nested map for "validate"
        Map<String, Object> validateMap = new HashMap<>();
        validateMap.put("required", columnDetails.get("isMandatory"));
        map.put("validate", validateMap);
		return map;
	}

}
