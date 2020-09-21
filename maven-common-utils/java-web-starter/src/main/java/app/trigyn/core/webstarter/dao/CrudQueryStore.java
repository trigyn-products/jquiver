package app.trigyn.core.webstarter.dao;

public final class CrudQueryStore {

    private CrudQueryStore() {

    }
    
    protected static final String HQL_QUERY_FIND_DASHBOARD_ROLE_DASHBOARD_ID = " FROM DashboardRoleAssociation AS dra where dra.id.dashboardId = :dashboardId ";

    protected static final String HQL_QUERY_ALL_DELETE_DASHLET_FROM_DASHBOARD = "DELETE FROM DashboardDashletAssociation AS dda WHERE dda.id.dashboardId = :dashboardId";

}
