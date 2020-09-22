package com.trigyn.jws.webstarter.dao;

import java.util.List;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dashboard.entities.Dashboard;
import com.trigyn.jws.dashboard.entities.DashboardDashletAssociation;
import com.trigyn.jws.dashboard.entities.DashboardRoleAssociation;
import com.trigyn.jws.dbutils.repository.DBConnection;
import com.trigyn.jws.webstarter.controller.DashboardCrudController;

@Repository
public class DashboardCrudDAO extends DBConnection {
	
	private final static Logger logger = LogManager.getLogger(DashboardCrudController.class);

    @Autowired
    public DashboardCrudDAO(DataSource dataSource) {
        super(dataSource);
    }

    
	public Dashboard findDashboardByDashboardId(String dashboardId)  throws Exception{
		return hibernateTemplate.get(Dashboard.class, dashboardId);
    }
    
    
	public List<DashboardRoleAssociation> findDashboardRoleByDashboardId(String dashboardId)  throws Exception{
		Query query = getCurrentSession().createQuery(CrudQueryStore.HQL_QUERY_FIND_DASHBOARD_ROLE_DASHBOARD_ID);
		query.setParameter("dashboardId", dashboardId);
		return query.list();
	}
    
    
	public void deleteAllDashletFromDashboard(String dashboardId) throws Exception {
		Query query = getCurrentSession().createQuery(CrudQueryStore.HQL_QUERY_ALL_DELETE_DASHLET_FROM_DASHBOARD.toString());
		query.setParameter("dashboardId", dashboardId);
		query.executeUpdate();
    }
    
    
	public void saveDashboardDashletAssociation(DashboardDashletAssociation dashboardDashletAssociation) throws Exception {
		getCurrentSession().saveOrUpdate(dashboardDashletAssociation);
	}
}
