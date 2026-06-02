package com.trigyn.jws.dynamicform.utils;

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

	// DB name constants
	public static final String			POSTGRESQL						= "postgresql";
	public static final String			MSSQLSERVER						= "sqlserver";
	public static final String			ORACLE							= "oracle:thin";

	public static final Integer			CUSTOM_MANUAL					= 1;

	public static final Integer			UPLOAD_SOURCE_VERSION_TYPE		= 4;
	public static final String			DYNAFORM_MOD_ID					= "30a0ff61-0ecf-11eb-94b2-f48e38ab9348";
	public static final String			ANONYMOUS_ROLE_ID				= "b4a0dda1-097f-11eb-9a16-f48e38ab9348";
	public static final Integer			IS_ACTIVE						= 0;
	public static final Integer			MASTER_SOURCE_VERSION_TYPE		= 1;
	public static final Integer			REVISION_SOURCE_VERSION_TYPE	= 2;
	public static final Integer			IMPORT_SOURCE_VERSION_TYPE		= 3;
	public static final int				SELECT							= 1;
	public static final int				JAVASCRIPT						= 2;
	public static final int				PYTHON							= 3;
	public static final int				PHP								= 4;
	public static final int				DML								= 2;
	public static final int				SP								= 3;
	public static final int				JS								= 4;
	public static final int				PY								= 5;
	public static final int				QPHP							= 6;

	public static final String			AES								= "AES";
	public static final String			ECB								= "ECB";
	public static final String			PKCS5PADDING					= "PKCS5PADDING";
	public static String				INVALID_CAPTCHA_MESSAGE;
	public static String				CAPTCHA_NOT_FOUND_MESSAGE;
	public static String				CAPTCHA_EXPIRED;
	public static final String			INVALID_CAPTCHA_KEY				= "jws.invalidCaptcha";
	public static final String			NO_CAPTCHA_KEY					= "jws.noCaptcha";
	public static final String			CAPTCHA_EXPIRED_KEY				= "jws.expiredCaptcha";
	public static final String			DYNA_REST_MOD_ID				= "47030ee1-0ecf-11eb-94b2-f48e38ab9348";
	public static final String			PERMISSION						= "permission";
	public static final String			SCRIPTLIBRARY					= "scriptlibrary";
	public static final CharSequence	MYSQL							= "mysql";
	public static final CharSequence	MARIADB							= "mariadb";
	public static final int				MSSQL_DATETIMEOFFSET			= -155;
	public static final CharSequence	DEFAULT							= "default";
	public static final int				XML								= -16;
	public static final int				UNIQUEIDENTIFIER				= 1;
	public static final String			UNIQUEID						= "uniqueidentifier";
	public static final String			MSSQL							= "Microsoft SQL Server";
	public static final String			UNIQUEID_REGEX					= "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
	public static final String			BASETYPE_STRING					= "STRING";
	public static final String			BASETYPE_INT					= "INTEGER";
	public static final String			BASETYPE_DECIMAL				= "DECIMAL";
	public static final String			BASETYPE_DATE					= "DATE";
	public static final String			BASETYPE_TIMESTAMP				= "TIMESTAMP";
	public static final String			BASETYPE_DATETIME				= "DATETIME";
	public static final String			BASETYPE_TIME					= "TIME";
	public static final String			BASETYPE_BOOLEAN				= "BOOLEAN";
	public static final String			DECIMAL_REGEX					= "^$|^-?\\d+(\\.\\d+)?([eE][-+]?\\d+)?$";
	public static final String			TIME_REGEX						= "^(?:([01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d|([01]\\d|2[0-3]):[0-5]\\d)?$|^$";
	public static final String			BOOLEAN_REGEX					= "(true|false|0|1|yes|no|48)";
	public static final String			ORACLEDB						= "Oracle";
	public static final String			DECIMAL							= "decimal";
	public static final String			XMLCOL							= "XML";
	public static final String			LONG							= "LONG";
	public static final String			ENUM							= "enum";
	public static final String			SET								= "set";
	public static final String			MARIA_DB						= "MariaDB";
	public static final int				ENUM_COLTYPE					= 1;
	public static final int				SET_COLTYPE						= 1;
	public static final String			JSON							= "JSON";
	public static final String			JSONB							= "JSONB";
	public static final String			UUID							= "UUID";
	public static final String			BIT								= "BIT";
	public static final int				JSON_COLTYPE					= -1;
	public static final String			DECIMAL_REGEX_POST_GRES			= "^$|^-?[0-9]+(\\.[0-9]+)?([eE][-+]?[0-9]+)?$";
	public static final String			MONEY							= "money";
	public static final String			SCHEMA_POSTGRES					= "public";
	public static final String			TINYINT							= "TINYINT";
	public static final String			TINYINT_REGEX_MARIADB			= "^-?(1(2[0-7]|[01][0-9])|[0-9]{1,2})$|^(-?128)$";
	public static final String			TINYINT_REGEX_MSSQL				= "^(?:[0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";
	public static final String			SCHEDULER_MOD_ID				= "fcd0df1f-783f-11eb-94ed-f48e38ab8cd6";
	public static final String          FILEBINMODID					= "248ffd91-7760-11eb-94ed-f48e38ab8cd7";

	public enum QueryType {
		SELECT(1, "SELECT"), DML(2, "DML"), SP(3, "SP"), JS(4, "nashorn"), PY(5, "python"), PHP(6, "php");

		final int		queryType;
		private String	queryTypeName	= null;

		QueryType(int queryType, String a_queryTypeName) {
			this.queryType	= queryType;
			queryTypeName	= a_queryTypeName;
		}

		public int getQueryType() {
			return queryType;
		}

		public String getQueryTypeName() {
			return queryTypeName;
		}

		public static QueryType getqueryTypeID(int a_queryType) {

			for (QueryType p : QueryType.values()) {
				if (p.getQueryType() == a_queryType) {
					return p;
				}
			}

			return null;
		}
	}

	public enum SelectQueryType {
		SELECT(1, "SELECT"), JAVASCRIPT(2, "nashorn"), PYTHON(3, "python"), PHP(4, "php");

		final int		querytype;
		private String	queryTypeName	= null;

		SelectQueryType(int queryType, String a_queryTypeName) {
			querytype		= queryType;
			queryTypeName	= a_queryTypeName;
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

			for (SelectQueryType p : SelectQueryType.values()) {
				if (p.getQuerytype() == a_queryType) {
					return p;
				}
			}

			return null;
		}
	}
}
