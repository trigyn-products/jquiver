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
		JAVA(1), FTL(2), JAVASCRIPT(3);

		final int platform;

		Platforms(int platformId) {
			platform = platformId;
		}

		public int getPlatform() {
			return platform;
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
}
