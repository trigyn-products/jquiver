package com.trigyn.jws.webstarter.utils;

public final class Constant {

	private Constant() {
		//
	}

	public enum TargetLookupId {
		DASHBOARD(1), DYANMICFORM(2), DYNAMICREST(3), MODELANDVIEW(4), TEMPLATE(5);

		final int lookupId;

		TargetLookupId(int i) {
			lookupId = i;
		}

		public int getTargetLookupId() {
			return lookupId;
		}
	}

	public static final String	TEMPLATE_TYPE_LOOKUP			= "TEMPLATE_TYPE";
	public static final String	DYNAMIC_FORM_TYPE_LOOKUP		= "DYNAMIC_FORM_TYPE";
	public static final Integer	DYNAMIC_FORM_DEFAULT_TYPE_ID	= 1;
	public static final String	DASHBOARD_TYPE_LOOKUP			= "DASHBOARD_TYPE";
	public static final String	DASHLET_TYPE_LOOKUP				= "DASHLET_TYPE";
	public static final String	XML_EXPORT_TYPE					= "XML";
	public static final String	FOLDER_EXPORT_TYPE				= "FOLDER";
	public static final String	EXPORTTEMPPATH					= "exportTempPath";
	public static final String	IMPORTTEMPPATH					= "importTempPath";
	public static final String	HELP_MANUAL_DIRECTORY_NAME		= "HelpManual";
	public static final String	FILE_BIN_UPLOAD_DIRECTORY_NAME	= "FileBin";
	public static final String	GENERIC_USER_NOTIFICATION		= "notification-listing";
	public static final String	FILES_UPLOAD_DIRECTORY_NAME		= "Files";

	public enum ModuleType {
		TEMPLATE("template"), AUTOCOMPLETE("autocomplete"), RESOURCEBUNDLE("resouceBundle"), DASHBOARD("dashboard"),
		DASHLET("dashlet"), DYNAREST("dynarest"), DYNAMICFORM("dynamicForm"),SCRIPTLIBRARY("scriptlibrary");

		final String moduleType;

		ModuleType(String i) {
			moduleType = i;
		}

		public String getModuleType() {
			return moduleType;
		}
	}

	public enum MasterModuleType {
		TEMPLATES("Templates"), AUTOCOMPLETE("Autocomplete"), RESOURCEBUNDLE("ResourceBundle"), DASHBOARD("Dashboard"),
		DASHLET("Dashlets"), DYNAREST("DynaRest"), DYNAMICFORM("DynamicForm"), GRID("Grid"),
		NOTIFICATION("Notification"), FILEMANAGER("FileManager"), PERMISSION("Permission"), SITELAYOUT("SiteLayout"),
		APPLICATIONCONFIGURATION("ApplicationConfiguration"), MANAGEUSERS("ManageUsers"), MANAGEROLES("ManageRoles"),
		HELPMANUAL("HelpManual"), APICLIENTDETAILS("ApiClientDetails"), ADDITIONALDATASOURCE("AdditionalDatasource"),
		SCHEDULER("Scheduler"), FILEIMPEXPDETAILS("Files"),SCRIPTLIBRARY("ScriptLibrary");

		final String moduleType;

		MasterModuleType(String i) {
			moduleType = i;
		}

		public String getModuleType() {
			return moduleType;
		}
	}

	public enum EntityNameModuleTypeEnum {
		TEMPLATES("jq_template_master"), AUTOCOMPLETE("jq_autocomplete_details"), RESOURCEBUNDLE("jq_resource_bundle"),
		DASHBOARD("jq_dashboard"), DASHLET("jq_dashlet"), DASHLETS("jq_dashlet"), DYNAREST("jq_dynamic_rest_details"),
		DYNAMICFORM("jq_dynamic_form"), GRID("jq_grid_details"), NOTIFICATION("jq_generic_user_notification"),
		APPLICATIONCONFIGURATION("jq_property_master"), HELPMANUAL("jq_manual_type"),
		APICLIENTDETAILS("jq_api_client_details"), ADDITIONALDATASOURCE("jq_additional_datasource"),
		SCHEDULER("jq_job_scheduler"), FILEIMPEXPDETAILS("Files"),SCRIPTLIBRARY("jq_script_lib_details");

		final String tableName;

		EntityNameModuleTypeEnum(String i) {
			tableName = i;
		}

		public String geTableName() {
			return tableName;
		}
	}

	public static final String	PROPERTY_MASTER_OWNER_TYPE			= "system";
	public static final String	PROPERTY_MASTER_OWNER_ID			= "system";
	public static final String	JWS_DATE_FORMAT_PROPERTY_NAME		= "jws-date-format";
	public static final String	JWS_DB_DATE_FORMAT_PROPERTY_NAME	= "db";

	public static final String	DYNAMIC_FORM_IS_EDIT				= "1";
	public static final Integer	MASTER_SOURCE_VERSION_TYPE			= 1;
	public static final Integer	REVISION_SOURCE_VERSION_TYPE		= 2;
	public static final Integer	IMPORT_SOURCE_VERSION_TYPE			= 3;

	public static final Integer	INCLUDE_LAYOUT						= 1;
	public static final Integer	EXCLUDE_LAYOUT						= 0;
	public static final String	DYNA_REST_MOD_ID					= "47030ee1-0ecf-11eb-94b2-f48e38ab9348";
	public static final String	DYNAFORM_MOD_ID						= "30a0ff61-0ecf-11eb-94b2-f48e38ab9348";
}
