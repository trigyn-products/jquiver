package com.trigyn.jws.webstarter.dao;

public final class CrudQueryStore {

    private CrudQueryStore() {

    }
    
    protected static final String HQL_QUERY_FIND_DASHBOARD_ROLE_DASHBOARD_ID = " FROM DashboardRoleAssociation AS dra where dra.id.dashboardId = :dashboardId ";

    protected static final String HQL_QUERY_ALL_DELETE_DASHLET_FROM_DASHBOARD = "DELETE FROM DashboardDashletAssociation AS dda WHERE dda.id.dashboardId = :dashboardId";
    
    protected static final String HQL_QUERY_TO_DELETE_ALL_DASHBOARD_ROLES = "DELETE FROM DashboardRoleAssociation AS dra WHERE dra.id.dashboardId = :dashboardId";
    
    protected static final String HQL_QUERY_TO_DELETE_ALL_DASHLET_PROPERTY = "DELETE FROM DashletProperties AS dp WHERE dp.dashletId = :dashletId";
    
    protected static final String HQL_QUERY_TO_DELETE_ALL_DASHLET_ROLES = "DELETE FROM DashletRoleAssociation AS dra WHERE dra.id.dashletId = :dashletId";

}
