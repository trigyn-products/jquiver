package com.trigyn.jws.dbutils.utils;

public final class Constant {

	public static final Double	INITIAL_VERSION_NUMBER				= 1.00;
	public static final Integer	MAX_DECIMAL_VERSION_NUMBER			= 99;

	public static final Integer	DEFAULT_LANGUAGE_ID					= 1;
	public static final Integer	DEFAULT_SEQUENCE_NUMBER				= 1;

	public static final Integer	IS_INSIDE_MENU						= 1;
	public static final Integer	IS_NOT_INSIDE_MENU					= 0;

	public static final Integer	INCLUDE_LAYOUT						= 1;
	public static final Integer	EXCLUDE_LAYOUT						= 0;

	public static final Integer	IS_HOME_PAGE						= 1;
	public static final Integer	IS_NOT_HOME_PAGE					= 0;

	public static final Integer	MODULE_GROUP_ID						= 6;
	public static final String	GROUP_MODULE_URL					= "#";
	public static final String	HOME_PAGE_MODULE_URL				= "home-module";

	public static final String	MODULE_VERSION_OWNER_TYPE			= "system";
	public static final String	MODULE_VERSION_OWNER_ID				= "system";
	public static final String	MODULE_VERSION_PROERTY_NAME			= "max-version-count";

	public static final Integer	NO_OF_RECORDS_TO_BE_DELETED			= 1;

	public static final String	REPLY_TO_DIFFERENT_EMAIL_ID			= "Different";
	public static final String	SSL									= "SSL";
	public static final String	TLS									= "TLS";
	public static final String	MAIL_TEMPLATE_NAME					= "mailTemplate";
	public static final String	TEST_MAIL_SUBJECT					= "Test Mail";

	public static final String	PROPERTY_MASTER_OWNER_TYPE			= "system";
	public static final String	PROPERTY_MASTER_OWNER_ID			= "system";
	public static final String	JWS_DATE_FORMAT_PROPERTY_NAME		= "jws-date-format";
	public static final String	JWS_JAVA_DATE_FORMAT_PROPERTY_NAME	= "java";

	public enum RecordStatus {
		INSERTED(0), DELETED(1);

		final int status;

		RecordStatus(int i) {
			status = i;
		}

		public int getStatus() {
			return status;
		}

	}

}
