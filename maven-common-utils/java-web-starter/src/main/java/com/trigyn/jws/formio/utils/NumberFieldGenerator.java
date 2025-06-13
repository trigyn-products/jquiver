package com.trigyn.jws.formio.utils;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

public class NumberFieldGenerator implements FormFieldGenerator {

	 @Override
	    public Map<String, Object> generateField(Map<String, Object> columnDetails) throws JSONException {
	       // JSONObject field = new JSONObject();
	        Map<String, Object>	field	= new HashMap<>();
	        
            field.put("label", columnDetails.get("fieldName"));
            field.put("applyMaskOn", "change");
            field.put("mask", false);
            field.put("tableView", false);
            field.put("delimiter", false);
            field.put("requireDecimal", false);
            field.put("inputFormat", "plain");
            field.put("truncateMultipleSpaces", false);

            Map<String, Object> validate = new HashMap<>();
            validate.put("required", columnDetails.get("isMandatory"));
            field.put("validate", validate);

            field.put("validateWhenHidden", false);
            field.put("key",  columnDetails.get("columnName"));
            field.put("type", "number");
            field.put("input", true);
            
	        return field;
	    }

}
