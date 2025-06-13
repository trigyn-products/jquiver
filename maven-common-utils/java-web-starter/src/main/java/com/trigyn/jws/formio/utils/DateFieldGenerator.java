package com.trigyn.jws.formio.utils;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

public class DateFieldGenerator implements FormFieldGenerator {
	
    @Override
    public Map<String, Object> generateField(Map<String, Object> columnDetails) throws JSONException {
    	
    	Map<String, Object> map = new HashMap<>();
        map.put("label", columnDetails.get("fieldName"));
        map.put("format", "d-F-Y");
        map.put("tableView", false);

        Map<String, Object> datePicker = new HashMap<>();
        datePicker.put("disableWeekends", false);
        datePicker.put("disableWeekdays", false);
        map.put("datePicker", datePicker);

        map.put("enableTime", false);
        map.put("enableMinDateInput", false);
        map.put("enableMaxDateInput", false);
        map.put("validateWhenHidden", false);
        map.put("key", columnDetails.get("columnName"));
        map.put("type", "datetime");
        map.put("input", true);

        // widget sub-map
        Map<String, Object> widget = new HashMap<>();
        widget.put("type", "calendar");
        widget.put("displayInTimezone", "viewer");
        widget.put("locale", "en");
        widget.put("useLocaleSettings", false);
        widget.put("allowInput", true);
        widget.put("mode", "single");
        widget.put("enableTime", false);
        widget.put("noCalendar", false);
        widget.put("format", "d-F-Y");
        widget.put("dateFormat", "YYYY-MM-DD");
        widget.put("hourIncrement", 1);
        widget.put("minuteIncrement", 1);
        widget.put("time_12hr", false);
        widget.put("minDate", null);
        widget.put("disableWeekends", false);
        widget.put("disableWeekdays", false);
        widget.put("maxDate", null);

        map.put("widget", widget);
        
		return map;

    	
    }
}

