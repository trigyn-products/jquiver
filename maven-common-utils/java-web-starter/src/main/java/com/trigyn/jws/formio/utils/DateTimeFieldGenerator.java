package com.trigyn.jws.formio.utils;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

public class DateTimeFieldGenerator implements FormFieldGenerator {

	@Override
	public Map<String, Object> generateField(Map<String, Object> columnDetails) throws JSONException {
		Map<String, Object> field = new HashMap<>();
		field.put("label", columnDetails.get("fieldName"));
		field.put("format", "d-F-Y");
		
		field.put("tableView", false);
		field.put("key", columnDetails.get("columnName"));
		field.put("type", "datetime");
		field.put("input", true);

		Map<String, Object> datePicker = new HashMap<String, Object>();
		datePicker.put("disableWeekends", false);
		datePicker.put("disableWeekdays", false);
		field.put("datePicker", datePicker);

		Map<String, Object> widget = new HashMap<String, Object>();
		widget.put("type", "calendar");
		widget.put("displayInTimezone", "viewer");
		widget.put("locale", "en");
		widget.put("allowInput", true);
		widget.put("mode", "single");
		widget.put("enableTime", true);
		widget.put("noCalendar", false);
	    widget.put("dateFormat", "YYYY-MM-DDTHH:mm:ss");
		widget.put("format", "%d-%B-%Y");
		
		field.put("widget", widget);

		Map<String, Object> validate = new HashMap<>();
		validate.put("required", columnDetails.get("isMandatory"));
		// validate.put("minLength", 1);
		// validate.put("maxLength", columnDetails.get("columnSize"));

		field.put("validate", validate);
		return field;
	}

}
