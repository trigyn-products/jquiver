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

	public enum ModuleType {
		TEMPLATE("template"), AUTOCOMPLETE("autocomplete"), RESOURCEBUNDLE("resouceBundle"), DASHBOARD("dashboard"),
		DASHLET("dashlet"), DYNAREST("dynarest"), DYNAMICFORM("dynamicForm");

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
		APPLICATIONCONFIGURATION("ApplicationConfiguration"), MANAGEUSERS("ManageUsers"), MANAGEROLES("ManageRoles");

		final String moduleType;

		MasterModuleType(String i) {
			moduleType = i;
		}

		public String getModuleType() {
			return moduleType;
		}
	}

	public enum EntityNameModuleTypeEnum {
		TEMPLATES("template_master"), AUTOCOMPLETE("autocomplete_details"), RESOURCEBUNDLE("resource_bundle"),
		DASHBOARD("dashboard"), DASHLET("dashlet"), DASHLETS("dashlet"), DYNAREST("jws_dynamic_rest_details"),
		DYNAMICFORM("dynamic_form"), GRID("grid_details"), NOTIFICATION("generic_user_notification"),
		APPLICATIONCONFIGURATION("jws_property_master");

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
}
