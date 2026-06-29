package com.trigyn.jws.webstarter.dao;

public final class CrudQueryStore {

	private CrudQueryStore() {

	}

	protected static final String	HQL_QUERY_FIND_DASHBOARD_ROLE_DASHBOARD_ID			= " FROM DashboardRoleAssociation AS dra where dra.id.dashboardId = :dashboardId ";

	protected static final String	HQL_QUERY_ALL_DELETE_DASHLET_FROM_DASHBOARD			= "DELETE FROM DashboardDashletAssociation AS dda WHERE dda.id.dashboardId = :dashboardId";

	protected static final String	HQL_QUERY_TO_DELETE_ALL_DASHBOARD_ROLES				= "DELETE FROM DashboardRoleAssociation AS dra WHERE dra.id.dashboardId = :dashboardId";

	protected static final String	HQL_QUERY_TO_DELETE_ALL_DASHLET_PROPERTY			= "DELETE FROM DashletProperties AS dp WHERE dp.dashletId = :dashletId";

	protected static final String	HQL_QUERY_TO_DELETE_ALL_DASHLET_ROLES				= "DELETE FROM DashletRoleAssociation AS dra WHERE dra.id.dashletId = :dashletId";

	public static final String		HQL_QUERY_TO_FETCH_GRID_DATA_FOR_EXPORT				= "FROM GridDetails AS gd "
			+ "WHERE (gd.gridId NOT IN :excludeCustomConfigList AND gd.gridTypeId = :customConfigType) OR (gd.gridId IN :includeSystemConfigList AND gd.gridTypeId = :systemConfigType)";

	public static final String		HQL_QUERY_TO_FETCH_AUTOCOMPLETE_DATA_FOR_EXPORT		= "FROM Autocomplete AS au WHERE "
			+ "(au.autocompleteId NOT IN :excludeCustomConfigList AND au.acTypeId = :customConfigType) OR "
			+ "(au.autocompleteId IN :includeSystemConfigList AND au.acTypeId = :systemConfigType)";

	public static final String		HQL_QUERY_TO_FETCH_RESOURCE_BUNDLE_DATA_FOR_EXPORT	= "FROM ResourceBundle AS rb WHERE "
			+ "(rb.id.resourceKey NOT IN :excludeCustomConfigList AND rb.id.resourceKey not like :customConfigType) "
			+ "OR (rb.id.resourceKey IN :includeSystemConfigList AND rb.id.resourceKey like :systemConfigType)";

	public static final String		HQL_QUERY_TO_FETCH_NOTIFICATAION_DATA_FOR_EXPORT	= "FROM GenericUserNotification AS gun WHERE "
			+ "gun.notificationId NOT IN :excludeCustomConfigList";

	public static final String		HQL_QUERY_TO_FETCH_FILE_MANAGER_DATA_FOR_EXPORT		= "FROM FileUploadConfig AS fu WHERE "
			+ "fu.fileBinId NOT IN :excludeCustomConfigList";

	public static final String		HQL_QUERY_TO_FETCH_DYNA_REST_DATA_FOR_EXPORT		= "FROM JwsDynamicRestDetail AS dr WHERE "
			+ "(dr.jwsDynamicRestId NOT IN :excludeCustomConfigList AND dr.jwsDynamicRestTypeId = :customConfigType) OR "
			+ "(dr.jwsDynamicRestId IN :includeSystemConfigList AND dr.jwsDynamicRestTypeId = :systemConfigType)";

	public static final String		HQL_QUERY_TO_FETCH_PERMISSION_FOR_EXPORT			= "FROM JwsEntityRoleAssociation AS jr WHERE "
			+ "jr.entityRoleId IN :includeSystemConfigList";

	public static final String		HQL_QUERY_TO_FETCH_SITE_LAYOUT_DATA_FOR_EXPORT		= "FROM ModuleListing AS ml WHERE "
			+ "(ml.moduleId NOT IN :excludeCustomConfigList AND ml.moduleTypeId = :customConfigType) OR "
			+ "(ml.moduleId IN :includeSystemConfigList AND ml.moduleTypeId = :systemConfigType)";

	public static final String		HQL_QUERY_TO_FETCH_TEMPLATE_DATA_FOR_EXPORT			= "FROM TemplateMaster AS tm WHERE "
			+ "(tm.templateId NOT IN :excludeCustomConfigList AND tm.templateTypeId = :customConfigType) OR "
			+ "(tm.templateId IN :includeSystemConfigList AND tm.templateTypeId = :systemConfigType)";

	public static final String		HQL_QUERY_TO_FETCH_DYNAMIC_FORM_DATA_FOR_EXPORT		= "FROM DynamicForm AS df WHERE "
			+ "(df.formId NOT IN :excludeCustomConfigList AND df.formTypeId = :customConfigType) OR "
			+ "(df.formId IN :includeSystemConfigList AND df.formTypeId = :systemConfigType)";

	public static final String		HQL_QUERY_TO_FETCH_DASHLET_DATA_FOR_EXPORT			= "FROM Dashlet AS dl WHERE "
			+ "(dl.dashletId NOT IN :excludeCustomConfigList AND dl.dashletTypeId = :customConfigType) OR "
			+ "(dl.dashletId IN :includeSystemConfigList AND dl.dashletTypeId = :systemConfigType)";

	public static final String		HQL_QUERY_TO_FETCH_DASHBOARD_DATA_FOR_EXPORT		= "FROM Dashboard AS db WHERE "
			+ "(db.dashboardId NOT IN :excludeCustomConfigList AND db.dashboardType = :customConfigType) OR "
			+ "(db.dashboardId IN :includeSystemConfigList AND db.dashboardType = :systemConfigType)";

	public static final String		HQL_QUERY_TO_FETCH_APP_CONFIG_DATA_FOR_EXPORT		= "FROM PropertyMaster AS pm WHERE "
			+ "pm.propertyMasterId IN :includeSystemConfigList";

	public static final String		HQL_QUERY_TO_FETCH_MANAGE_USERS_DATA_FOR_EXPORT		= "FROM JwsUser AS ju WHERE "
			+ "ju.userId IN :includeSystemConfigList";

	public static final String		HQL_QUERY_TO_FETCH_MANAGE_ROLES_DATA_FOR_EXPORT		= "FROM JwsRole AS jr WHERE "
			+ "jr.roleName IN :includeSystemConfigList";

	public static final String		HQL_QUERY_TO_FETCH_HELP_MANUAL_DATA_FOR_EXPORT		= " FROM ManualType AS mt WHERE "
			+ "(mt.manualId NOT IN :excludeCustomConfigList AND mt.isSystemManual = :customConfigType) OR "
			+ "(mt.manualId IN :includeSystemConfigList AND mt.isSystemManual = :systemConfigType)";

	public static final String		HQL_QUERY_TO_FETCH_API_CLIENT_DETAILS_FOR_EXPORT	= "FROM JqApiClientDetails AS acd WHERE "
			+ "acd.clientId NOT IN :excludeCustomConfigList";

	public static final String		HQL_QUERY_TO_FETCH_ADDITIONAL_DATASOURCE_FOR_EXPORT	= "FROM AdditionalDatasource AS ads WHERE "
			+ "ads.additionalDatasourceId NOT IN :excludeCustomConfigList";

	public static final String		HQL_QUERY_TO_FETCH_SCHEDULER_DATA_FOR_EXPORT		= "FROM JqScheduler AS js WHERE "
			+ "(js.schedulerId NOT IN :excludeCustomConfigList AND js.schedulerTypeId = :customConfigType) OR "
			+ "(js.schedulerId IN :includeSystemConfigList AND js.schedulerTypeId = :systemConfigType)";

	public static final String		HQL_QUERY_TO_FETCH_FILES_DATA_FOR_EXPORT			= "FROM FileUpload AS fu WHERE  fu.filePath <> '/images' AND "
			+ "fu.fileUploadId IN :includeSystemConfigList";
	
	public static final String		HQL_QUERY_TO_FETCH_SCRIPT_LIBRARY_DATA_FOR_EXPORT		= "FROM ScriptLibraryDetails AS sl WHERE "
			+ "sl.scriptLibId NOT IN :excludeCustomConfigList";
	
	public static final String		HQL_QUERY_TO_FETCH_BUSINESS_MODULE_DATA_FOR_EXPORT		= "FROM JwsBusinessModule AS jbm WHERE "
			+ "(jbm.businessModuleId NOT IN :excludeCustomConfigList ) OR (jbm.businessModuleId IN :includeSystemConfigList)";
	
	public static final String		HQL_QUERY_TO_FETCH_WORKFLOW_DATA_FOR_EXPORT		= "FROM WorkflowDefinition AS wd WHERE "
			+ "wd.definitionId NOT IN :excludeCustomConfigList";
	

	public static final String HQL_QUERY_TO_FETCH_GRID_DATA_FOR_AUTO_EXPORT =
		    "SELECT " +
		    "gd.grid_id, " +
		    "gd.grid_name, " +
		    "gd.grid_description, " +
		    "gd.grid_table_name, " +
		    "gd.grid_column_names AS gridColumnName, " +
		    "gd.query_type, " +
		    "gd.grid_type_id, " +
		    "gd.created_by, " +
		    "gd.created_date, " +
		    "gd.datasource_id, " +
		    "gd.custom_filter_criteria, " +
		    "gd.last_updated_by, " +
		    "gd.last_updated_ts, " +
		    "gd.is_custom_updated " +
		    "FROM jq_grid_details gd";
	
	public static final String		HQL_QUERY_TO_FETCH_SCRIPT_LIBRARY_DATA_FOR_AUTO_EXPORT		= "Select * FROM jq_script_lib_details AS sl";

	
	public static final String HQL_QUERY_TO_FETCH_AUTOCOMPLETE_DATA_FOR_AUTO_EXPORT =
		    "SELECT " +
		    "au.ac_id AS autocompleteId, " +
		    "au.ac_description AS autocompleteDesc, " +
		    "au.ac_select_query AS autocompleteSelectQuery, " +
		    "au.ac_type_id AS acTypeId, " +
		    "au.created_by AS createdBy, " +
		    "au.created_date AS createdDate, " +
		    "au.datasource_id AS datasourceId, " +
		    "au.last_updated_by AS lastUpdatedBy, " +
		    "au.last_updated_ts AS lastUpdatedTs, " +
		    "au.is_custom_updated AS isCustomUpdated " +
		    "FROM jq_autocomplete_details au ";

	public static final String		HQL_QUERY_TO_FETCH_RESOURCE_BUNDLE_DATA_FOR_AUTO_EXPORT	= "Select * FROM jq_resource_bundle AS rb ";
			
	public static final String		HQL_QUERY_TO_FETCH_NOTIFICATAION_DATA_FOR_AUTO_EXPORT	= "Select * FROM jq_generic_user_notification AS gun ";

	public static final String		HQL_QUERY_TO_FETCH_FILE_MANAGER_DATA_FOR_AUTO_EXPORT		= "Select * FROM jq_file_upload_config AS fu ";

	public static final String		HQL_QUERY_TO_FETCH_DYNA_REST_DATA_FOR_AUTO_EXPORT		= " FROM JwsDynamicRestDetail AS dr";

	public static final String		HQL_QUERY_TO_FETCH_PERMISSION_FOR_AUTO_EXPORT			= "Select * FROM jq_entity_role_association AS jr";

	public static final String HQL_QUERY_TO_FETCH_SITE_LAYOUT_DATA_FOR_AUTO_EXPORT ="FROM ModuleListing ml";
//	public static final String HQL_QUERY_TO_FETCH_SITE_LAYOUT_DATA_FOR_AUTO_EXPORT =
//	        "SELECT "
//	        + "jml.module_id AS moduleId, "
//	        + "jml.module_url AS moduleUrl, "
//	        + "jml.parent_id AS parentId, "
//	        + "jml.sequence AS sequence, "
//	        + "jml.is_inside_menu AS isInsideMenu, "
//	        + "jml.include_layout AS includeLayout, "
//	        + "jml.is_home_page AS isHomePage, "
//	        + "jml.target_lookup_id AS targetLookupId, "
//	        + "jml.target_type_id AS targetTypeId, "
//	        + "jml.header_json AS headerJson, "
//	        + "jml.last_modified_date AS updatedDate, "
//	        + "jml.module_type_id AS moduleTypeId, "
//	        + "jml.request_param_json AS requestParamJson, "
//	        + "jml.open_in_new_tab AS openInNewTab, "
//	        + "jml.menu_style AS menuStyle, "
//	        + "jml.is_custom_updated AS isCustomUpdated, "
//	        + "jml.last_updated_by AS lastUpdatedBy "
//	        + "FROM jq_module_listing jml ";

	public static final String		HQL_QUERY_TO_FETCH_TEMPLATE_DATA_FOR_AUTO_EXPORT			= "Select * FROM jq_template_master AS tm ";

	public static final String		HQL_QUERY_TO_FETCH_DYNAMIC_FORM_DATA_FOR_AUTO_EXPORT		=  " FROM DynamicForm AS df ";

	public static final String		HQL_QUERY_TO_FETCH_DASHLET_DATA_FOR_AUTO_EXPORT			= " FROM Dashlet AS dl ";

	public static final String		HQL_QUERY_TO_FETCH_DASHBOARD_DATA_FOR_AUTO_EXPORT		= "FROM Dashboard AS db ";

	public static final String		HQL_QUERY_TO_FETCH_APP_CONFIG_DATA_FOR_AUTO_EXPORT		= "Select * FROM jq_property_master AS pm ";

	public static final String		HQL_QUERY_TO_FETCH_MANAGE_USERS_DATA_FOR_AUTO_EXPORT		= "Select * FROM jq_user AS ju ";

	public static final String		HQL_QUERY_TO_FETCH_MANAGE_ROLES_DATA_FOR_AUTO_EXPORT		= "Select * FROM jq_role AS jr ";

	public static final String		HQL_QUERY_TO_FETCH_HELP_MANUAL_DATA_FOR_AUTO_EXPORT		= "Select * FROM jq_manual_type AS mt ";

	public static final String		HQL_QUERY_TO_FETCH_API_CLIENT_DETAILS_FOR_AUTO_EXPORT	= "SELECT acd FROM JqApiClientDetails acd "
	+ "LEFT JOIN FETCH acd.jqEncAlgModPadKeyLookup ";

	public static final String		HQL_QUERY_TO_FETCH_ADDITIONAL_DATASOURCE_FOR_AUTO_EXPORT	= "FROM AdditionalDatasource ads";

	public static final String		HQL_QUERY_TO_FETCH_SCHEDULER_DATA_FOR_AUTO_EXPORT		= "Select * FROM jq_job_scheduler AS js ";

	public static final String		HQL_QUERY_TO_FETCH_FILES_DATA_FOR_AUTO_EXPORT			= "Select * FROM jq_file_upload AS fu WHERE fu.file_path <> '/images' ";
	
	public static final String		HQL_QUERY_TO_FETCH_FORM_IO_DATA_FOR_EXPORT					= "FROM FormIO AS fio WHERE "
			+ "(fio.formIoId NOT IN :excludeCustomConfigList AND fio.formIoType = :customConfigType) OR "
			+ "(fio.formIoId IN :includeSystemConfigList AND fio.formIoType = :systemConfigType)";
	
	public static final String		HQL_QUERY_TO_FETCH_BUSINESS_MODULE_DATA_FOR_AUTO_EXPORT		= "Select * FROM jq_business_module AS jbm";
	
	
	public static final String		HQL_QUERY_TO_FETCH_WORKFLOW_DATA_FOR_AUTO_EXPORT		= "Select * FROM jq_workflow_definition AS wd";
	
	public static final String		HQL_QUERY_TO_FETCH_FORM_IO_DATA_FOR_AUTO_EXPORT					= "Select * FROM jq_form_io fio " ;
}
