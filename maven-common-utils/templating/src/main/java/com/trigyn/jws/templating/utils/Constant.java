package com.trigyn.jws.templating.utils;

public final class Constant {

	private Constant() {

	}

	public static final Integer	DEFAULT_TEMPLATE_TYPE			= 1;
	public static final String	TEMPLATE_DIRECTORY_NAME			= "Templates";
	public static final String	CUSTOM_FILE_EXTENSION			= ".tgn";

	public static final Integer	MASTER_SOURCE_VERSION_TYPE		= 1;
	public static final Integer	REVISION_SOURCE_VERSION_TYPE	= 2;
	public static final Integer	IMPORT_SOURCE_VERSION_TYPE		= 3;
	public static final Integer	UPLOAD_SOURCE_VERSION_TYPE		= 4;

	public static final String	SYSTEM_OWNER_TYPE				= "system";
	public static final String	SYSTEM_OWNER_ID					= "system";
	public static final String	JQUIVER_VERSION_PROPERTY_NAME	= "version";
	
	
	public enum Changetype{
		SYSTEM("System"),
		CUSTOM("Custom");
		
		final String changetype;
		
		Changetype(String i) {
			changetype = i;
		} 

		public String getChangetype() {
			return changetype;
		}

		}
	public enum MasterModuleType {
		TEMPLATES("Templates"), AUTOCOMPLETE("Autocomplete"), RESOURCEBUNDLE("ResourceBundle"), DASHBOARD("Dashboard"),
		DASHLET("Dashlets"), DYNAREST("DynaRest"), DYNAMICFORM("DynamicForm"), GRID("Grid"),
		NOTIFICATION("Notification"), FILEMANAGER("FileManager"), PERMISSION("Permission"), SITELAYOUT("SiteLayout"),
		APPLICATIONCONFIGURATION("ApplicationConfiguration"), MANAGEUSERS("ManageUsers"), MANAGEROLES("ManageRoles"),
		HELPMANUAL("HelpManual"), APICLIENTDETAILS("ApiClientDetails"), ADDITIONALDATASOURCE("AdditionalDatasource"),
		SCHEDULER("Scheduler");

		final String moduleType;

		MasterModuleType(String i) {
			moduleType = i;
		}

		public String getModuleType() {
			return moduleType;
		}
	}
	public enum Action{
		ADD("Add"),
		EDIT("Edit"),
		DELETE("Delete"),
		OPEN("Open");
		
		final String action;
		
		Action(String i) {
			action = i;
		}

		public String getAction() {
			return action;
		}

		}

}
