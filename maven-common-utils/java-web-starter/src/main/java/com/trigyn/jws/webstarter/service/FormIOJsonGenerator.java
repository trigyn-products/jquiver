package com.trigyn.jws.webstarter.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
 
public class FormIOJsonGenerator {
	
    public static String getFormIOJson (List<Map<String, Object>> nestedComponents)throws Exception {
    	Map<String, Object> htmlElement = new HashMap<>();
    	htmlElement.put("label", "Save Buttons");
    	htmlElement.put("attrs", List.of(Map.of("attr", "", "value", "")));
    	htmlElement.put("content", "<div id=\"jqFmioSaveButton\" style=\"display: flex; justify-content: flex-end;\"><img src=\"../webjars/1.0/images/Savendsubmit.png\" style=\"float:right;\" height=\"50px\" /></div>");
    	htmlElement.put("refreshOnChange", false);
    	htmlElement.put("key", "html");
    	htmlElement.put("type", "htmlelement");
    	htmlElement.put("input", false);
    	htmlElement.put("tableView", false);
        List<Map<String, Object>> components1 = List.of(htmlElement);
        Map<String, Object> mainColumn = createColumn(components1, 12);
        Map<String, Object> outerColumn = createColumn(nestedComponents, 12);
 
        Map<String, Object> mainColumns = new HashMap<>();
        mainColumns.put("label", "Columns");
        mainColumns.put("columns", List.of(outerColumn, mainColumn));
        mainColumns.put("key", "columns1");
        mainColumns.put("type", "columns");
        mainColumns.put("input", false);
        mainColumns.put("tableView", false);
        Map<String, Object> jsonOutput = new HashMap<>();
        jsonOutput.put("components", List.of(mainColumns));
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonOutput);
        return json;
    }
    private static Map<String, Object> createColumn(List<Map<String, Object>> components, int width) {
        Map<String, Object> column = new HashMap<>();
        column.put("components", components);
        column.put("width", width);
        column.put("offset", 0);
        column.put("push", 0);
        column.put("pull", 0);
        column.put("size", "md");
        column.put("currentWidth", width);
        return column;
    }
}
