package com.trigyn.jws.webstarter.dao;

public final class CrudQueryStore {

	private CrudQueryStore() {

	}

	protected static final String	HQL_QUERY_FIND_DASHBOARD_ROLE_DASHBOARD_ID			= " FROM DashboardRoleAssociation AS dra where dra.id.dashboardId = :dashboardId ";

	protected static final String	HQL_QUERY_ALL_DELETE_DASHLET_FROM_DASHBOARD			= "DELETE FROM DashboardDashletAssociation AS dda WHERE dda.id.dashboardId = :dashboardId";

	protected static final String	HQL_QUERY_TO_DELETE_ALL_DASHBOARD_ROLES				= "DELETE FROM DashboardRoleAssociation AS dra WHERE dra.id.dashboardId = :dashboardId";

	protected static final String	HQL_QUERY_TO_DELETE_ALL_DASHLET_PROPERTY			= "DELETE FROM DashletProperties AS dp WHERE dp.dashletId = :dashletId";

	protected static final String	HQL_QUERY_TO_DELETE_ALL_DASHLET_ROLES				= "DELETE FROM DashletRoleAssociation AS dra WHERE dra.id.dashletId = :dashletId";

	public static final String		HQL_QUERY_TO_FETCH_GRID_DATA_FOR_EXPORT				= "FROM GridDetails AS gd WHERE (gd.gridId NOT IN :excludeCustomConfigList AND gd.gridTypeId = :customConfigType) OR (gd.gridId IN :includeSystemConfigList AND gd.gridTypeId = :systemConfigType)";

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
			+ "ml.moduleId IN :includeSystemConfigList";

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
	

}
