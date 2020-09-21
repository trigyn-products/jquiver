package app.trigyn.core.webstarter.dao;

import java.util.List;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import app.trigyn.common.dashboard.entities.Dashboard;
import app.trigyn.common.dashboard.entities.DashboardDashletAssociation;
import app.trigyn.common.dashboard.entities.DashboardRoleAssociation;
import app.trigyn.common.dbutils.repository.DBConnection;
import app.trigyn.core.webstarter.controller.DashboardCrudController;

@Repository
public class DashboardCrudDAO extends DBConnection {
	
	private final static Logger logger = LogManager.getLogger(DashboardCrudController.class);

    @Autowired
    public DashboardCrudDAO(DataSource dataSource) {
        super(dataSource);
    }

    /**
	 * @param dashboardId
	 * @return {@link Dashboard}
 	 * @throws Exception
	 */
	public Dashboard findDashboardByDashboardId(String dashboardId)  throws Exception{
		return hibernateTemplate.get(Dashboard.class, dashboardId);
    }
    
    /**
	 * @param dashboardId
	 * @return {@link List}
	 * @throws Exception
	 */
	public List<DashboardRoleAssociation> findDashboardRoleByDashboardId(String dashboardId)  throws Exception{
		Query query = getCurrentSession().createQuery(CrudQueryStore.HQL_QUERY_FIND_DASHBOARD_ROLE_DASHBOARD_ID);
		query.setParameter("dashboardId", dashboardId);
		return query.list();
	}
    
    /**
	 * @param dashboardId
	 * @param dashboardId
	 * @throws Exception
	 */
	public void deleteAllDashletFromDashboard(String dashboardId) throws Exception {
		Query query = getCurrentSession().createQuery(CrudQueryStore.HQL_QUERY_ALL_DELETE_DASHLET_FROM_DASHBOARD.toString());
		query.setParameter("dashboardId", dashboardId);
		query.executeUpdate();
    }
    
    /**
	 * @param dashboardDashletAssociation
	 * @throws Exception
	 */
	public void saveDashboardDashletAssociation(DashboardDashletAssociation dashboardDashletAssociation) throws Exception {
		getCurrentSession().saveOrUpdate(dashboardDashletAssociation);
	}
}
