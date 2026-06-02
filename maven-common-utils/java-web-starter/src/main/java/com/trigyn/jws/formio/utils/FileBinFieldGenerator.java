package com.trigyn.jws.formio.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

public class FileBinFieldGenerator implements FormFieldGenerator {

	@Override
	public Map<String, Object> generateField(Map<String, Object> columnDetails) throws JSONException {
		Map<String, Object> fileBinMap = new HashMap<>();

		fileBinMap.put("fileBinType", columnDetails.get("fileBinType"));
		fileBinMap.put("label",  columnDetails.get("fieldName"));

		List<Map<String, String>> attrs = new ArrayList<>();
		Map<String, String> attrEntry = new HashMap<>();
		attrEntry.put("attr", "");
		attrEntry.put("value", "");
		attrs.add(attrEntry);
		fileBinMap.put("attrs", attrs);

		fileBinMap.put("content", columnDetails.get("content"));
		fileBinMap.put("refreshOnChange", false);
		fileBinMap.put("key", columnDetails.get("fieldName"));
		fileBinMap.put("type", columnDetails.get("columnType"));
		fileBinMap.put("input", false);
		fileBinMap.put("placeholder", "File Bin");
		fileBinMap.put("tableView", false);
		fileBinMap.put("customType", columnDetails.get("columnType"));

		// Trigger
		Map<String, Object> trigger = new HashMap<>();
		trigger.put("type", "javascript");
		trigger.put("javascript",
				columnDetails.get("jsContent")
		);

		// Logic
		Map<String, Object> logicEntry = new HashMap<>();
		logicEntry.put("name", columnDetails.get("fieldName"));
		logicEntry.put("trigger", trigger);
		logicEntry.put("actions", new ArrayList<>()); // empty array

		List<Map<String, Object>> logicList = new ArrayList<>();
		logicList.add(logicEntry);
		fileBinMap.put("logic", logicList);

		return fileBinMap;
	}

}
