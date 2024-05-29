package com.trigyn.jws.dynamicform.utils;

import com.trigyn.jws.dynarest.utils.Constants.Platforms;

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
	public static final String	DYNAFORM_MOD_ID					= "30a0ff61-0ecf-11eb-94b2-f48e38ab9348";
	public static final String	ANONYMOUS_ROLE_ID				= "b4a0dda1-097f-11eb-9a16-f48e38ab9348";
	public static final Integer	IS_ACTIVE						= 0;
	public static final Integer MASTER_SOURCE_VERSION_TYPE 		= 1;
	public static final Integer	REVISION_SOURCE_VERSION_TYPE	= 2;
	public static final Integer	IMPORT_SOURCE_VERSION_TYPE		= 3;
	public static final int		SELECT					    	= 1;
	public static final int		JAVASCRIPT					    = 2;
	public static final int		PYTHON					        = 3;
	public static final int		PHP					        	= 4;
	public static final int		DML					        	= 2;
	public static final int		SP					        	= 3;
	public static final int		JS					    		= 4;
	public static final int		PY					        	= 5;
	public static final int		QPHP					        = 6;
	

	public enum QueryType {
		SELECT(1, "SELECT"), DML(2, "DML"), SP(3,"SP"), JS(4,"nashorn"), PY(5, "python"), PHP(6, "php");

		final int queryType;
		private String queryTypeName = null;


		QueryType(int queryType, String a_queryTypeName) {
			this.queryType = queryType;
			queryTypeName = a_queryTypeName;
		}

		public int getQueryType() {
			return queryType;
		}
		
		public String getQueryTypeName() {
			return queryTypeName;
		}
		
		public static QueryType getqueryTypeID(int a_queryType) {
			
			for(QueryType p : QueryType.values()) {
				if(p.getQueryType() == a_queryType) {
					return p;
				}
			}
			
			return null;
		}
	}
	
	public enum SelectQueryType {
		SELECT(1, "SELECT"), JAVASCRIPT(2, "nashorn") , PYTHON(3, "python") ,PHP(4, "php");

		final int querytype;
		private String queryTypeName = null;

		SelectQueryType(int queryType, String a_queryTypeName) {
			querytype = queryType;
			queryTypeName = a_queryTypeName;
		}
		
		public String getQueryTypeName() {
			return queryTypeName;
		}

		public void setQueryTypeName(String queryTypeName) {
			this.queryTypeName = queryTypeName;
		}

		public int getQuerytype() {
			return querytype;
		}

		public static SelectQueryType getqueryTypeID(int a_queryType) {
			
			for(SelectQueryType p : SelectQueryType.values()) {
				if(p.getQuerytype() == a_queryType) {
					return p;
				}
			}
			
			return null;
		}
	}
}
