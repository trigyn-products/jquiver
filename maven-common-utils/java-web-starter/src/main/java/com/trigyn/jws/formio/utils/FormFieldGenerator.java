package com.trigyn.jws.formio.utils;

import java.util.Map;

import org.json.JSONException;

public interface FormFieldGenerator {
	
    /**
     * @param columnDetails
     * @return
     */
	Map<String, Object> generateField(Map<String, Object> columnDetails) throws JSONException;
}

