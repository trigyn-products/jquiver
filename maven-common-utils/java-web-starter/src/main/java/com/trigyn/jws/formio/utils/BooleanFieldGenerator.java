package com.trigyn.jws.formio.utils;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

public class BooleanFieldGenerator implements FormFieldGenerator {
	
    @Override
    public Map<String, Object> generateField(Map<String, Object> columnDetails) throws JSONException {
    	Map<String, Object> field = new HashMap<>();
        field.put("label", columnDetails.get("fieldName"));
        field.put("tableView", false);
        field.put("validateWhenHidden", false);
        field.put("key", columnDetails.get("columnName"));
        field.put("type", "checkbox");
        field.put("input", true);

        Map<String, Object> validate = new HashMap<String, Object>();
        validate.put("required", columnDetails.get("isMandatory"));
        validate.put("minLength", 1);
        validate.put("maxLength", columnDetails.get("columnSize"));

        field.put("validate", validate);
        return field;
    }
}

