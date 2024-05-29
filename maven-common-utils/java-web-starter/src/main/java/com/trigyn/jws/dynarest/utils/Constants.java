package com.trigyn.jws.dynarest.utils;

import java.util.HashMap;
import java.util.Map;

public final class Constants {

	private Constants() {

	}

	public static final String			SERVICE_CLASS_NAME						= "ServiceLogic";
	public static final String			DYNAREST_CLASS_FILE_PATH				= "dynarest-class-file-path";

	public static final Integer			SELECT_FILE_VALIDATOR					= 1;
	public static final Integer			UPLOAD_FILE_VALIDATOR					= 2;
	public static final Integer			VIEW_FILE_VALIDATOR						= 3;
	public static final Integer			DELETE_FILE_VALIDATOR					= 4;

	public static final Integer			IS_NOT_ALLOWED							= 0;
	public static final Integer			IS_ALLOWED								= 1;
	public static final Integer			DEFAULT_RESPONSE_TIMEOUT				= 3;
	public static final Integer			SSL_HANDSHAKE_TIMEOUT					= 3;
	public static final Integer			SSL_CLOSE_NOTIFY_FLUSH_TIMEOUT			= 3;
	public static final Integer			SSL_CLOSE_NOTIFY_READ_TIMEOUT			= 3;
	public static final String			MODULE_NAME								= "File Bin";
	public static final String			ACTION_UPL								= "UPLOAD";
	public static final String			ACTION_DEL								= "DELETE";
	public static final String			ACTION_DOWN								= "DOWNLOAD";
	public static final String			CHANGETYPE								= "CUSTOM";

	public static final String			CUSTOM_FILE_EXTENSION					= ".tgn";
	public static final String			DYNAMIC_REST_DIRECTORY_NAME				= "DynamicRest";
	public static final String			DYNAMIC_REST_SERVICE_LOGIC_FILE_NAME	= "serviceLogic";
	public static final String			DYNAMIC_REST_SELECT_FILE_NAME			= "selectQuery-";
	public static final String			DYNAMIC_FREST_ENTITY_NAME				= "dynamic_rest_add_edit";
	public static final Integer			FILE_ATTACHMENT_FILEBIN					= 1;
	public static final Integer			FILE_ATTACHMENT_FILESYSTEM				= 2;
	public static final Integer			FILE_ATTACHMENT_UPLOADEDFILE			= 3;
	public static final Integer			EMAIL_CONTENT_TYPE_TWO					= 2;
	public static final Integer			EMAIL_CONTENT_TYPE_THREE				= 3;
	public static final Integer			IS_SECURED								= 1;
	public static final Integer			IS_NOT_SECURED							= 0;
	public static final Integer		    ALL_REQ_TYPE_ID					        = 9;
	public static final String			DYNAMIC_REST_MOD_ID						= "47030ee1-0ecf-11eb-94b2-f48e38ab9348";
	public static final String			FILE_BIN_MOD_ID							= "248ffd91-7760-11eb-94ed-f48e38ab8cd7";
	public static final int		    	JAVA					        		= 1;
	public static final int		    	FTL					        			= 2;
	public static final int		    	JAVASCRIPT					        	= 3;
	public static final int		    	PYTHON					        		= 4;
	public static final int		    	PHP					        			= 5;
	public static final int		    	SELECT					        		= 1;
	public static final int		    	JS					        			= 4;
	public static final int		    	PY					        			= 2;
	public static final int		    	FilePHP					        		= 3;

	private static Map<String, String>	SELECT_REQUIRED_COLUMN_MAP				= new HashMap<String, String>();

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

	public enum Platforms {
		JAVA(1, "java"), FTL(2, "ftl"), JAVASCRIPT(3, "nashorn") , PYTHON(4, "python") ,PHP(5, "php");

		final int platform;
		private String platformName = null;

		Platforms(int platformId, String a_platformName) {
			platform = platformId;
			platformName = a_platformName;
		}

		public int getPlatform() {
			return platform;
		}
		
		public String getPlatformName() {
			return platformName;
		}
		
		public static Platforms getPlatformByID(int a_platformId) {
			
			for(Platforms p : Platforms.values()) {
				if(p.getPlatform() == a_platformId) {
					return p;
				}
			}
			
			return null;
		}
	}

	public enum QueryType {
		DML(2), SELECT(1), SP(3), WC(4);

		final int queryType;

		QueryType(int queryType) {
			this.queryType = queryType;
		}

		public int getQueryType() {
			return queryType;
		}
	}
	
	public enum Action {
		ADD("ADD"), EDIT("EDIT"), DELETE("DELETE"), OPEN("OPEN"), PERMISSIONACTIVATED("PERMISSION ACTIVATED"),
		PERMISSIONINACTIVATED("PERMISSION INACTIVATED"), SCHEDULEREXECUTIONFAILED("SCHEDULER EXECUTION FAILED"),
		SCHEDULEREXECUTED("SCHEDULER EXECUTED SUCCESSFULLY"), APIEXECUTED("Api Executed"),
		APIEXECFAILED("Api Execution Failed");

		final String action;

		Action(String i) {
			action = i;
		}

		public String getAction() {
			return action;
		}

	}
	public enum Changetype {
		SYSTEM("System", 2), CUSTOM("Custom", 1);

		final String	changetype;
		final int		typeInt;

		Changetype(String typeName, int a_typeInt) {
			changetype	= typeName;
			typeInt		= a_typeInt;
		}

		public String getChangetype() {
			return changetype;
		}

		public int getChangeTypeInt() {
			return typeInt;
		}

	}
	public enum Modules {

		GRIDUTILS("Grid Utils"), TEMPLATING("Templating"), DYNAMICFORM("Form Builder"), DYNAMICREST("REST API Builder"),
		AUTOCOMPLETE("TypeAhead Autocomplete"), DASHBOARD("Dashboard"), SITELAYOUT("Site Layout"), FILEBIN("File Bin"),
		HELPMANUAL("Help Manual"), MULTILINGUAL("MultiLingual"), NOTIFICATION("Notification"),
		APPLICATIONCONFIGURATION("Application Configuration"), USERMANAGEMENT("User Management"),
		APICLIENTS("API Clients"), SCHEDULER("Scheduler");

		final String moduleName;

		Modules(String moduleName) {
			this.moduleName = moduleName;
		}

		public String getModuleName() {
			return moduleName;
		}
	}
	
	public enum FileQueryType {
		SELECT(1, "SELECT"), PY(2, "python"), PHP(3,"php"), JS(4,"nashorn");

		final int queryType;
		private String queryTypeName = null;


		FileQueryType(int queryType, String a_queryTypeName) {
			this.queryType = queryType;
			queryTypeName = a_queryTypeName;
		}

		public int getQueryType() {
			return queryType;
		}
		
		public String getQueryTypeName() {
			return queryTypeName;
		}
		
		public static FileQueryType getqueryTypeID(int a_queryType) {
			
			for(FileQueryType p : FileQueryType.values()) {
				if(p.getQueryType() == a_queryType) {
					return p;
				}
			}
			
			return null;
		}
	}
}
