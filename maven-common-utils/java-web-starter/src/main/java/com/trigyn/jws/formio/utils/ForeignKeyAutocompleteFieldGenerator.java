package com.trigyn.jws.formio.utils;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

public class ForeignKeyAutocompleteFieldGenerator implements FormFieldGenerator {

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Object> generateField(Map<String, Object> columnDetails) throws JSONException {
		Map<String, Object> field = new HashMap<>();
		Map<String, Object> fkConfig = (Map<String, Object>) columnDetails.get("foreignKeyConfig");
		String autocompleteId = String.valueOf(fkConfig.get("autocompleteId"));
		field.put("label", columnDetails.get("fieldName"));
		field.put("key", columnDetails.get("columnName"));
		field.put("type", "typeautocompletecomponent");
		field.put("typeautotype", autocompleteId);
		field.put("input", true);
		field.put("tableView", true);
		return field;
	}
}