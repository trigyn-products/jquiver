package com.trigyn.jws.formio.utils;

public class FormFieldFactory {
	
    public static FormFieldGenerator getFieldGenerator(String columnType) {
        if ("hidden".equalsIgnoreCase(columnType)) {
            return new HiddenFieldGenerator();
        } else if ("text".equalsIgnoreCase(columnType) || "uniqueidentifier".equalsIgnoreCase(columnType)) {
            return new TextFieldGenerator();
        } else if ("boolean".equalsIgnoreCase(columnType)) {
            return new BooleanFieldGenerator();
        } else if ("datetime".equalsIgnoreCase(columnType)) {
           return new DateTimeFieldGenerator();
        }  else if ("date".equalsIgnoreCase(columnType)) {
            return new DateFieldGenerator();
        } else if ("int".equalsIgnoreCase(columnType) || "decimal".equalsIgnoreCase(columnType)) {
        	 return new NumberFieldGenerator();
        } else if ("textarea".equalsIgnoreCase(columnType) || "json".equalsIgnoreCase(columnType) || "xml".equalsIgnoreCase(columnType)) {
            return new TextAreaFieldGenerator();
        } else if ("time".equalsIgnoreCase(columnType)) {
            return new TimeFieldGenerator();
        } else if ("money".equalsIgnoreCase(columnType)) {
            return new CurrencyFieldGenerator();
        } else if ("html".equalsIgnoreCase(columnType)) {
            return new HtmlFieldGenerator();
        } else if ("captchaelement".equalsIgnoreCase(columnType)) {
            return new CaptchaFieldGenerator();
        }
        throw new IllegalArgumentException("Invalid column type: " + columnType);
    }
}

