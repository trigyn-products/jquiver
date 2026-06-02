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
	public final static String TEMPLATES                = "Templates";
	public final static String AUTOCOMPLETE             = "Autocomplete";
	public final static String RESOURCEBUNDLE           = "ResourceBundle";
	public final static String DASHBOARD                = "Dashboard";
	public final static String DASHLET                  = "Dashlets";
	public final static String DYNAREST                 = "DynaRest";
	public final static String DYNAMICFORM              = "DynamicForm";
	public final static String GRID                     = "Grid";
	public final static String NOTIFICATION             = "Notification";
	public final static String FILEMANAGER              = "FileManager";
	public final static String PERMISSION               = "Permission";
	public final static String ROUTER               	= "Router";
	public final static String APPLICATIONCONFIGURATION = "ApplicationConfiguration";
	public final static String MANAGEUSERS              = "ManageUsers";
	public final static String MANAGEROLES              = "ManageRoles";
	public final static String HELPMANUAL               = "HelpManual";
	public final static String APICLIENTDETAILS         = "ApiClientDetails";
	public final static String ADDITIONALDATASOURCE     = "AdditionalDatasource";
	public final static String SCHEDULER                = "Scheduler";
	public final static String FILEIMPEXPDETAILS        = "Files";
	public final static String SCRIPTLIBRARY            = "ScriptLibrary";
	public final static String BUSINESSMODULE           = "BusinessModule";
	public final static String WORKFLOW				    = "Workflow";
	public final static String AUTOCOMPLETEMODID		= "91a81b68-0ece-11eb-94b2-f48e38ab9348";
	public final static String TEMPLATEMODID			= "1b0a2e40-098d-11eb-9a16-f48e38ab9348";
	public final static String DYNAMICFORMMODID			= "30a0ff61-0ecf-11eb-94b2-f48e38ab9348";
	public final static String RESTAPIMODID				= "47030ee1-0ecf-11eb-94b2-f48e38ab9348";
	public final static String RESOURCEBUNDLEMODID		= "5559212c-8c8f-11eb-8dcd-0242ac130003";
	public final static String NOTIFICATIONMODID		= "6ac6a54c-8d3f-11eb-8dcd-0242ac130003";
	public final static String FILEBINMODID				= "248ffd91-7760-11eb-94ed-f48e38ab8cd7";
	public final static String FORMIOMODID				= "1faee99c-021c-11ef-a019-7c8ae1bb24d8";
	public final static String DASHLETMODID				= "19aa8996-80a2-11eb-971b-f48e38ab8cd7";
	public final static String GRIDMODID				= "07067149-098d-11eb-9a16-f48e38ab9348";
	public final static String APPCONFIGURATIONMODID	= "76270518-8c8f-11eb-8dcd-0242ac130003";
	public final static String DASHBOARDMODID			= "b0f8646c-0ecf-11eb-94b2-f48e38ab9348";
	public final static String ROUTERMODID				= "c6cc466a-0ed3-11eb-94b2-f48e38ab9348";
	public final static String APICLIENTMODID			= "ded49cbd-ed7c-40ce-b1f8-c2e34ad33473";
	public final static String SCHEDULERMODID			= "fcd0df1f-783f-11eb-94ed-f48e38ab8cd6";
	public final static String ADDITIONALDATASOURCEMODID = "799947cc-b6cb-11eb-8529-0242ac130003";
	public final static String BUSINESSMODENTITY        = "jq_business_module";
	public final static String HELPMANUALMODID          = "fcd0df1f-783f-11eb-94ed-f48e38ab8cd7";
	public final static String ADMINROLEID          	= "ae6465b3-097f-11eb-9a16-f48e38ab9348";
	public final static String WORKFLOWID            	= "6f1a8a6e-5a6d-4f4d-b9d3-8f1e8a7c9b2a";
	
	public final static String	HHC								= "hhc.exe";
	public final static String	HHA								= "hha.dll";
	public final static String	ITIRCL							= "itircl.dll";
	public final static String	ITSS							= "itss.dll";
	public final static String	ITCC							= "itcc.dll";
	public final static String	HHCTRL							= "hhctrl.ocx";
	public final static String	HHCOUT							= "hhcout.dll";
	public final static String	HHKOUT							= "hhkout.dll";
	public final static String	CHCOMPILER						= "chmCompiler";
	
	
	public enum ModuleType {
		TEMPLATE("template"), AUTOCOMPLETE("autocomplete"), RESOURCEBUNDLE("resouceBundle"), DASHBOARD("dashboard"),
		DASHLET("dashlet"), DYNAREST("dynarest"), DYNAMICFORM("dynamicForm"),SCRIPTLIBRARY("scriptlibrary"), FORMIO("FormIO"),BUSINESSMODULE("BusinessModule"),APPLICATIONCONFIGURATION("propertyMaster");

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
		NOTIFICATION("Notification"), FILEMANAGER("FileManager"), PERMISSION("Permission"), ROUTER("Router"),
		APPLICATIONCONFIGURATION("ApplicationConfiguration"), MANAGEUSERS("ManageUsers"), MANAGEROLES("ManageRoles"),
		HELPMANUAL("HelpManual"), APICLIENTDETAILS("ApiClientDetails"), ADDITIONALDATASOURCE("AdditionalDatasource"),
		SCHEDULER("Scheduler"), FILEIMPEXPDETAILS("Files"),SCRIPTLIBRARY("ScriptLibrary"), FORMIO("FormIO"),BUSINESSMODULE("BusinessModule"),BUSINESSENTITYMODULES("BusinessEntityModules"),WORKFLOW("Workflow");

		final String moduleType;

		MasterModuleType(String i) {
			moduleType = i;
		}

		
		public String getModuleType() {
			return moduleType;
		}
		 @Override
		 public String toString() {
		       return this.moduleType;
		    }
		 
		public static MasterModuleType valueOfModuleType(String a_strModuleType) {
			if(a_strModuleType == null) {
				return null;
			}
			for(MasterModuleType moduleType : MasterModuleType.values()) {
				if(moduleType.getModuleType().equalsIgnoreCase(a_strModuleType
						)) {
					return moduleType;
				}
			}
			return null;
		}
	}
	
	public enum EntityNameModuleTypeEnumExportImport {
		GridUtils(MasterModuleType.GRID),Autocomplete(MasterModuleType.AUTOCOMPLETE),
		FileBins(MasterModuleType.FILEMANAGER),Templating(MasterModuleType.TEMPLATES),DynamicForm(MasterModuleType.DYNAMICFORM), 
		RestAPI(MasterModuleType.DYNAREST),Router(MasterModuleType.ROUTER),Internalization(MasterModuleType.RESOURCEBUNDLE),
		Dashboard(MasterModuleType.DASHBOARD),Dashlet(MasterModuleType.DASHLET),Notification(MasterModuleType.NOTIFICATION),UserManagement(MasterModuleType.MANAGEUSERS),
		ApplicationConfiguration(MasterModuleType.APPLICATIONCONFIGURATION), HelpManual(MasterModuleType.HELPMANUAL),ManagePermissions(MasterModuleType.PERMISSION),
		ManageRoles(MasterModuleType.MANAGEROLES),Files(MasterModuleType.FILEIMPEXPDETAILS),APIClientDetails(MasterModuleType.APICLIENTDETAILS),
		AdditionalDatasource(MasterModuleType.ADDITIONALDATASOURCE),Scriptlibrary(MasterModuleType.SCRIPTLIBRARY),Scheduler(MasterModuleType.SCHEDULER), FORMIO(MasterModuleType.FORMIO),BUSINESSMODULE(MasterModuleType.BUSINESSMODULE);
		final MasterModuleType _baseEnum;

		EntityNameModuleTypeEnumExportImport(MasterModuleType a_baseEnum) {
			_baseEnum = a_baseEnum;
		}

		public MasterModuleType getBaseEnum() {
			return _baseEnum;
		}
	}
	
	public enum EntityNameModuleTypeEnum {
		TEMPLATES("jq_template_master"), AUTOCOMPLETE("jq_autocomplete_details"), RESOURCEBUNDLE("jq_resource_bundle"),
		DASHBOARD("jq_dashboard"), DASHLET("jq_dashlet"), DASHLETS("jq_dashlet"), DYNAREST("jq_dynamic_rest_details"),
		DYNAMICFORM("jq_dynamic_form"), GRID("jq_grid_details"), NOTIFICATION("jq_generic_user_notification"),
		APPLICATIONCONFIGURATION("jq_property_master"), HELPMANUAL("jq_manual_type"),
		APICLIENTDETAILS("jq_api_client_details"), ADDITIONALDATASOURCE("jq_additional_datasource"),
		SCHEDULER("jq_job_scheduler"), FILEIMPEXPDETAILS("Files"),SCRIPTLIBRARY("jq_script_lib_details"), FORMIO("FormIO"), BUSINESSMODULE("jq_business_module"),BUSINESSENTITYMODULES("jq_business_module_entity_details"),WORKFLOW("jq_workflow_definition");

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
	public static final String	Captcha								= "Captcha";
	public static final String	SCRIPT_lIB_MOD_ID					= "70bd445b-ae33-45dd-98f9-0a0c49463315";
	public static final String	BUSINESS_MOD_ID						= "ba72cdd7-7ad3-4163-a98f-48847beee391";
}
