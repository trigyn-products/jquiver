package com.trigyn.jws.dashboard.dao;

public final class QueryStore {

	protected static final String	HQL_QUERY_FIND_DASHBOARD_BY_CONTEXT_ID				= " FROM Dashboard AS d INNER JOIN d.dashboardRoles AS dr where dr.roleId IN ( :userRoles ) AND d.createdBy IN ( :adminStr, :userId ) AND d.isDeleted = 0 ORDER BY d.lastUpdatedDate DESC";

	public static final String	HQL_QUERY_LOAD_DASHBOARD_DASHLETS					= " FROM DashboardDashletAssociation AS dda INNER JOIN dda.dashlet AS d WITH d.isActive = 1 where dda.dashboardId = :dashboardId ";

	protected static final String	SQL_QUERY_FIND_CONTEXT_NAME_BY_ID					= "SELECT context_id AS contextId from jq_context_master where context_description LIKE :contextName";

	protected static final String	SQL_QUERY_GET_CONTEXT_BASED_PERMISSION				= "SELECT allow_dashboard_addition AS allowDashboardAddition from jq_context_master where context_description LIKE :contextName";

	protected static final String	SQL_QUERY_DELETE_DASHLET_FROM_DASHBOARD				= "delete from jq_dashboard_dashlet_association where dashboard_id = :dashboardId AND dashlet_id = :dashletId";

	public static final String		JPA_QUERY_TO_GET_ALL_CONTEXT						= "SELECT new com.trigyn.jws.dashboard.vo.ContextMasterVO "
			+ " (cm.contextId AS contextId, cm.contextDescription AS contextDescription) " + " FROM ContextMaster AS cm ";

	public static final String		JPA_QUERY_TO_GET_DASHLET_BY_CONTEXT_ID				= "SELECT new  com.trigyn.jws.dashboard.vo.DashboardDashletVO "
			+ " (da.id.dashboardId AS dashboardId, dl.dashletId AS dashletId, dl.dashletName AS dashletName) "
			+ " FROM Dashlet AS dl " 
			+ " LEFT OUTER JOIN dl.dashboardAssociation AS da ON da.id.dashboardId = :dashboardId "
			+ " ORDER BY dl.dashletName ";

	public static final String		JPA_QUERY_TO_GET_DAHSLET_DETAILS_BY_ID				= "SELECT new com.trigyn.jws.dashboard.vo.DashletVO "
			+ " (dl.dashletId AS dashletId, dl.dashletTitle AS dashletTitle, dl.dashletName AS dashletName "
			+ " , dl.dashletBody AS dashletBody, dl.dashletQuery AS dashletQuery"
			+ " , dl.xCoordinate AS xCoordinate, dl.yCoordinate AS yCoordinate"
			+ " , dl.width AS width, dl.height AS height,dl.datasourceId AS dataSourceId "
			+ " , dl.resultVariableName, dl.daoQueryType "
			+ " , dl.showHeader AS showHeader, dl.isActive AS isActive) " + " FROM Dashlet AS dl WHERE dl.dashletId = :dashletId ";

	public static final String		JPQ_QUERY_TO_GET_DAHSLET_PROPERTIES_BY_ID			= "SELECT new com.trigyn.jws.dashboard.vo.DashletPropertyVO "
			+ " (dp.propertyId AS dashletPropertyId, dp.placeholderName AS placeholderName, dp.displayName AS displayName "
			+ " , dp.type AS type, dp.value AS value, dp.defaultValue AS defaultValue, dp.configurationScript AS configurationScript "
			+ " , dp.toDisplay AS toDisplay, dp.sequence AS sequence , dp.isDeleted AS isDeleted) " + " FROM DashletProperties AS dp "
			+ " WHERE dp.dashletId = :dashletId AND dp.isDeleted = :isDeleted ORDER BY dp.id.sequence ASC";

	public static final String		JPQ_QUERY_TO_GET_DASHLET_ROLES_ASSOCIATION_BY_ID	= "SELECT new com.trigyn.jws.dashboard.vo.DashletRoleAssociationVO "
			+ " (dra.id.roleId AS roleId, dra.id.dashletId AS dashletId) " + " FROM DashletRoleAssociation AS dra "
			+ " WHERE dra.id.dashletId = :dashletId ";

	public static final String		HQL_QUERY_TO_GET_DASHLET_ROLES_ID_BY_DASHLET_ID		= "SELECT dra.id.roleId AS roleId "
			+ "FROM DashletRoleAssociation AS dra WHERE dra.id.dashletId = :dashletId";

	public static final String		JPA_QUERY_TO_GET_LOOKUP_DETAILS_BY_CATEOGRY_NAME	= "SELECT new com.trigyn.jws.dashboard.vo.DashboardLookupCategoryVO "
			+ " (dlc.lookupCategoryId AS lookupCategoryId, dlc.lookupDescription AS lookupDescription) "
			+ " FROM DashboardLookupCategory AS dlc WHERE dlc.lookupCategory = :lookupCategoryName ";

	public static final String		JPA_QUERY_TO_GET_LOOKUP_DETAILS_BY_CATEOGRY_ID		= "SELECT new com.trigyn.jws.dashboard.vo.DashboardLookupCategoryVO "
			+ " (dlc.lookupCategoryId AS lookupCategoryId, dlc.lookupDescription AS lookupDescription) "
			+ " FROM DashboardLookupCategory AS dlc WHERE dlc.lookupCategoryId IN (:lookupCategoryId) ";

	public static final String		JPQ_QUERY_TO_GET_DAHSLET_PROPERTIES_DETAILS_BY_ID	= "SELECT new com.trigyn.jws.dashboard.vo.DashletPropertyVO "
			+ " (dp.propertyId AS dashletPropertyId, dp.displayName AS displayName "
			+ " , dlc.lookupDescription AS type, dp.value AS value, COALESCE(dpc.propertyValue, dp.defaultValue) AS defaultValue) "
			+ " FROM DashletProperties AS dp " + " LEFT OUTER JOIN dp.dashletPropertyConfigurations AS dpc ON dpc.id.userId = :userId "
			+ " LEFT OUTER JOIN dp.dashboardLookupCategory AS dlc "
			+ " WHERE dp.dashletId = :dashletId AND dp.isDeleted = :isDeleted ORDER BY dp.id.sequence ASC";
	
}