package com.trigyn.jws.dynamicform.utils;

public final class Constant {

	private Constant() {

	}

	public static final Integer	DEFAULT_FORM_TYPE				= 1;
	public static final String	CUSTOM_FILE_EXTENSION			= ".tgn";
	public static final String	DYNAMIC_FORM_DIRECTORY_NAME		= "DynamicForm";
	public static final String	DYNAMIC_FORM_SELECT_FILE_NAME	= "selectQuery";
	public static final String	DYNAMIC_FORM_HTML_FILE_NAME		= "htmlContent";
	public static final String	DYNAMIC_FORM_SAVE_FILE_NAME		= "saveQuery-";
	public static final String	DYNAMIC_FORM_ENTITY_NAME		= "dynamic_form_add_edit";
	
	//DB name constants
	public static final String POSTGRESQL = "postgresql";
	public static final String MSSQLSERVER = "sqlserver";
	public static final String ORACLE= "oracle:thin";

	public static final Integer	CUSTOM_MANUAL					= 1;

	public static final Integer	UPLOAD_SOURCE_VERSION_TYPE		= 4;

	public enum QueryType {
		SELECT(1), DML(2), SP(3), JS(4);

		final int queryType;

		QueryType(int queryType) {
			this.queryType = queryType;
		}

		public int getQueryType() {
			return queryType;
		}
	}
}
