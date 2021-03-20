package com.trigyn.jws.dynamicform.utils;

import java.util.HashMap;
import java.util.Map;

public final class Constant {

	private Constant() {

	}

	public static final Integer			DEFAULT_FORM_TYPE				= 1;
	public static final String			CUSTOM_FILE_EXTENSION			= ".tgn";
	public static final String			DYNAMIC_FORM_DIRECTORY_NAME		= "DynamicForm";
	public static final String			DYNAMIC_FORM_SELECT_FILE_NAME	= "selectQuery";
	public static final String			DYNAMIC_FORM_HTML_FILE_NAME		= "htmlContent";
	public static final String			DYNAMIC_FORM_SAVE_FILE_NAME		= "saveQuery-";
	public static final String			DYNAMIC_FORM_ENTITY_NAME		= "dynamic_form_add_edit";

	public static final Integer			SELECT_FILE_VALIDATOR			= 1;
	public static final Integer			UPLOAD_FILE_VALIDATOR			= 2;
	public static final Integer			VIEW_FILE_VALIDATOR				= 3;
	public static final Integer			DELETE_FILE_VALIDATOR			= 4;

	public static final Integer			IS_NOT_ALLOWED					= 0;
	public static final Integer			IS_ALLOWED						= 1;

	public static final Integer			CUSTOM_MANUAL					= 1;

	private static Map<String, String>	SELECT_REQUIRED_COLUMN_MAP		= new HashMap<String, String>();

	public static final Map<String, String> getSelectRequiredColumnMap() {
		SELECT_REQUIRED_COLUMN_MAP.put("fileAssociationId", "VARCHAR");
		SELECT_REQUIRED_COLUMN_MAP.put("fileBinId", "VARCHAR");
		SELECT_REQUIRED_COLUMN_MAP.put("filePath", "VARCHAR");
		SELECT_REQUIRED_COLUMN_MAP.put("fileUploadId", "VARCHAR");
		SELECT_REQUIRED_COLUMN_MAP.put("originalFileName", "VARCHAR");
		SELECT_REQUIRED_COLUMN_MAP.put("physicalFileName", "VARCHAR");
		return SELECT_REQUIRED_COLUMN_MAP;
	}

	private static Map<String, String> FILE_VALIDATOR_COLUMN_MAP = new HashMap<String, String>();

	public static final Map<String, String> getFileValidatorColumnMap() {
		FILE_VALIDATOR_COLUMN_MAP.put("isAllowed", "INT");
		return FILE_VALIDATOR_COLUMN_MAP;
	}

	private static Map<String, String> QUERY_NAME_MAP = new HashMap<String, String>();

	public static final Map<String, String> getQueryName() {
		QUERY_NAME_MAP.put("selectValidator_query", "Select SQL");
		QUERY_NAME_MAP.put("uploadValidator_query", "Upload Validator SQL");
		QUERY_NAME_MAP.put("viewValidator_query", "View Validator SQL");
		QUERY_NAME_MAP.put("deleteValidator_query", "Delete Validator SQL ");
		return QUERY_NAME_MAP;
	}

	public static final Integer UPLOAD_SOURCE_VERSION_TYPE = 4;
}
