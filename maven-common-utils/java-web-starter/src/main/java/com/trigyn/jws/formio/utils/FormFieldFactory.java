package com.trigyn.jws.formio.utils;

import com.trigyn.jws.dynamicform.utils.Constant;

public class FormFieldFactory {
	
    public static FormFieldGenerator getFieldGenerator(String columnType) {
        if ("hidden".equalsIgnoreCase(columnType)) {
            return new HiddenFieldGenerator();
        } else if ("text".equalsIgnoreCase(columnType) || Constant.UNIQUEID.equalsIgnoreCase(columnType) || "uuid".equalsIgnoreCase(columnType)) {
            return new TextFieldGenerator();
        } else if ("boolean".equalsIgnoreCase(columnType)) {
            return new BooleanFieldGenerator();
        } else if ("datetime".equalsIgnoreCase(columnType)) {
           return new DateTimeFieldGenerator();
        }  else if ("date".equalsIgnoreCase(columnType)) {
            return new DateFieldGenerator();
        } else if ("int".equalsIgnoreCase(columnType) || Constant.DECIMAL.equalsIgnoreCase(columnType) || Constant.TINYINT.equalsIgnoreCase(columnType)) {
        	 return new NumberFieldGenerator();
        } else if ("textarea".equalsIgnoreCase(columnType) || "json".equalsIgnoreCase(columnType) || "xml".equalsIgnoreCase(columnType)) {
            return new TextAreaFieldGenerator();
        } else if (Constant.BASETYPE_TIME.toLowerCase().equalsIgnoreCase(columnType)) {
            return new TimeFieldGenerator();
        } else if ("money".equalsIgnoreCase(columnType)) {
            return new CurrencyFieldGenerator();
        } else if ("html".equalsIgnoreCase(columnType)) {
            return new HtmlFieldGenerator();
        } else if ("captchaelement".equalsIgnoreCase(columnType)) {
            return new CaptchaFieldGenerator();
		} else if (Constant.ENUM.equalsIgnoreCase(columnType) || Constant.SET.equalsIgnoreCase(columnType)) {
			return new SelectFieldGenerator();
		} else if ("filebincomponent".equalsIgnoreCase(columnType)) {
            return new FileBinFieldGenerator();
		}
        throw new IllegalArgumentException("Invalid column type: " + columnType);
    }
}

