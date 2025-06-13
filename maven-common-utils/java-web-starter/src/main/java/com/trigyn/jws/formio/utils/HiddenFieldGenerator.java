package com.trigyn.jws.formio.utils;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

public class HiddenFieldGenerator implements FormFieldGenerator {
	
    @Override
    public Map<String, Object> generateField(Map<String, Object> columnDetails) throws JSONException {
       // JSONObject field = new JSONObject();
        Map<String, Object>	field	= new HashMap<>();
        field.put("label", columnDetails.get("fieldName"));
        field.put("key", columnDetails.get("columnName"));
        field.put("type", "hidden");
        field.put("input", true);
        field.put("tableView", false);
        return field;
    }
    
}

