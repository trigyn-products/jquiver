package com.trigyn.jws.dashboard.utility;

public final class Constants {

	private Constants() {
		//
	}

	public static final String	SALT_KEY								= "a1b2c3d4w5x6y7z8";
	public static final String	CLOUD_STORAGE_CONTAINER_NAME_UPLOAD_PDF	= "uploadedpdffiles";
	public static final String	PRODUCT_IMAGE_DIR_NAME					= "productImages";
	public static final String	CLOUD_DIRECTORY_NAME					= "directoryName";

	public static final String	SMTP_USERNAME							= "mail.smtp.username";
	public static final String	SMTP_PASSWORD							= "mail.smtp.password";
	public static final String	MAIL_SMTP_TLS							= "mail.smtp.starttls.enable";
	public static final String	MAIL_SMTP_SSL_ENABLE					= "mail.smtp.ssl.enable";
	public static final String	MAIL_DEBUG_ON							= "mail.debug";
	public static final String	MAIL_RETRY_COUNT						= "mail.retry.count";
	public static final String	MAIL_STORE_PROTOCOL						= "mail.store.protocol";
	public static final String	MAIL_SMTP_PORT							= "mail.smtp.port";
	public static final String	MAIL_SMTP_AUTH							= "mail.authentication.required";
	public static final String	PDF_TYPE								= "application/pdf";

	public static final String	USER_ROLE_STR							= "userRoles";
	public static final String	RESOURCE_PATH_STR						= "resourcePath";
	public static final String	DASHLET_DIV								= "/vmFiles/dashletDiv.vm";
	public static final String	COMPONENT_TYPE_CATEGORY					= "COMPONENT_TYPE";
	public static final String	ADMIN_STR								= "ADMIN";
	public static final String	DEFAULT_LOCALE							= "en_US";
	public static final String	GENERIC_USER_NOTIFICATION				= "genericUserNotification";
	public static final String	CREATE_NEW_USER_NOTIFICATION			= "createNewUserNotification";
	public static final String	DATE_FORMAT								= "dd-MMM-yyyy";
	public static final String	DATE_FORMAT_JS							= "dd-M-yy";
	public static final String	TARGET_PLATFORM_TSMS					= "tsms";
	public static final String	DASHLET_CONFIGURATION					= null;
	public static final String	ANON_USER_STR							= "anonymousUser";

	public static final Integer	DEFAULT_DASHLET_TYPE_ID					= 1;

	public static final String	CUSTOM_FILE_EXTENSION					= ".tgn";
	public static final String	DASHLET_DIRECTORY_NAME					= "Dashlets";
	public static final String	DASHLET_QUERY_FILE_NAME					= "selectQuery";
	public static final String	DASHLET_HTML_FILE_NAME					= "htmlContent";
	public static final String	TEMPORARY_STORAGE_PATH					= "template-storage-path";

	public static final Integer	UPLOAD_SOURCE_VERSION_TYPE				= 4;

	public enum RecordStatus {
		DELETED(1), INSERTED(0), UPDATED(0);

		final int status;

		RecordStatus(int i) {
			status = i;
		}

		public int getStatus() {
			return status;
		}
	}

	public enum queryImplementationType {
		VIEW(1), STORED_PROCEDURE(2);

		final int type;

		queryImplementationType(int i) {
			type = i;
		}

		public int getType() {
			return type;
		}
	}

	public enum DashletStatus {
		ACTIVE(1), INACTIVE(0);

		final int status;

		DashletStatus(int i) {
			status = i;
		}

		public int getDashletStatus() {
			return status;
		}
	}
}