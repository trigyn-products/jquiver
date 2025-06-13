package com.trigyn.jws.formio.utils;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

public class TextFieldGenerator implements FormFieldGenerator {
	
    @Override
    public Map<String, Object> generateField(Map<String, Object> columnDetails) throws JSONException {
    	
    	Map<String, Object> field = new HashMap<>();
        field.put("label", columnDetails.get("fieldName"));
        field.put("applyMaskOn", "change");
        field.put("tableView", true);
        field.put("validateWhenHidden", false);
        field.put("key", columnDetails.get("columnName"));
        field.put("type", "textfield");
        field.put("input", true);

        Map<String, Object> validate = new HashMap<>();
        validate.put("required", columnDetails.get("isMandatory"));
        validate.put("minLength", 1);
        validate.put("maxLength", columnDetails.get("columnSize"));

        field.put("validate", validate);
        return field;
    }
}

