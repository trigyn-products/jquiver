
package com.trigyn.jws.dashboard.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.dashboard.entities.Dashboard;
import com.trigyn.jws.dashboard.entities.DashboardDashletAssociation;
import com.trigyn.jws.dashboard.entities.DashboardRoleAssociation;
import com.trigyn.jws.dashboard.entities.Dashlet;
import com.trigyn.jws.dashboard.vo.DashboardDashletVO;
import com.trigyn.jws.dbutils.repository.DBConnection;

@Repository
public class DashboardDaoImpl extends DBConnection {

	@Autowired
	public DashboardDaoImpl(DataSource dataSource) {
		super(dataSource);
	}

	public Dashboard findDashboardByDashboardId(String dashboardId) throws Exception {
		Dashboard dashboard =  hibernateTemplate.get(Dashboard.class, dashboardId);
		if(dashboard != null) getCurrentSession().evict(dashboard);
		return dashboard;
	}

	public Object findDashletByDashletId(String dashletId) {
		Dashlet dashlet =  hibernateTemplate.get(Dashlet.class, dashletId);
		if(dashlet != null) getCurrentSession().evict(dashlet);
		return dashlet;
	}

	public Dashboard saveDashboardDetails(Dashboard dashboard) throws Exception {
		getCurrentSession().saveOrUpdate(dashboard);
		getCurrentSession().flush();
		return dashboard;
	}

	public void saveDashboardRoleAssociation(DashboardRoleAssociation dashboardRoleAssociation) throws Exception {
		getCurrentSession().saveOrUpdate(dashboardRoleAssociation);
	}

	public List<Object[]> findDashboardsByContextId(String contextId, List<String> userRoles, String userId) throws Exception {
		Query query = getCurrentSession().createQuery(QueryStore.HQL_QUERY_FIND_DASHBOARD_BY_CONTEXT_ID);
		query.setParameterList("userRoles", userRoles);
		query.setParameter("contextId", contextId);
		query.setParameter("userId", userId);
		List<Object[]> dashboards = (List<Object[]>) query.getResultList();
		return dashboards;
	}

	public List<Object[]> loadDashboardDashlets(String dashboardId) throws Exception {
		Query query = getCurrentSession().createQuery(QueryStore.HQL_QUERY_LOAD_DASHBOARD_DASHLETS);
		query.setParameter("dashboardId", dashboardId);
		return query.list();
	}

	public String getContextNameById(String contextName) throws Exception {
		Map<String, Object> namedParams = new HashMap<>();
		namedParams.put("contextName", contextName);
		Map<String, Object> resultMap = namedParameterJdbcTemplate.queryForMap(QueryStore.SQL_QUERY_FIND_CONTEXT_NAME_BY_ID, namedParams);
		return resultMap.get("contextId").toString();
	}

	public Integer getContextBasedPermissions(String contextName) throws Exception {
		Map<String, Object> namedParams = new HashMap<>();
		namedParams.put("contextName", contextName);
		Map<String, Object> resultMap = namedParameterJdbcTemplate.queryForMap(QueryStore.SQL_QUERY_GET_CONTEXT_BASED_PERMISSION,
				namedParams);
		return (Integer) resultMap.get("allowDashboardAddition");
	}

	public void removeDashletFromDashboard(String dashletId, String dashboardId) throws Exception {
		Map<String, Object> namedParams = new HashMap<>();
		namedParams.put("dashletId", dashletId);
		namedParams.put("dashboardId", dashboardId);
		namedParameterJdbcTemplate.update(QueryStore.SQL_QUERY_DELETE_DASHLET_FROM_DASHBOARD, namedParams);
	}

	public DashboardDashletVO getDashletsByContextId(String contextId) {
		StringBuilder	stringBuilder	= new StringBuilder(
				"SELECT dashletId AS dashletId, dashletName AS dashletName FROM Dashlet AS d WHERE d.contextId = :contextId");
		Query			query			= getCurrentSession().createQuery(stringBuilder.toString());
		query.setParameter("contextId", contextId);
		return (DashboardDashletVO) query.uniqueResult();
	}

	public Long getDashboardCount(String dashboardId) {
		StringBuilder	stringBuilder	= new StringBuilder("SELECT count(*) FROM Dashboard AS d WHERE d.dashboardId = :dashboardId");
		Query			query			= getCurrentSession().createQuery(stringBuilder.toString());
		query.setParameter("dashboardId", dashboardId);
		return (Long) query.uniqueResult();
	}

	public Long getDashletsCount(String dashletId) {
		StringBuilder	stringBuilder	= new StringBuilder("SELECT count(*) FROM Dashlet AS d WHERE d.dashletId = :dashletId");
		Query			query			= getCurrentSession().createQuery(stringBuilder.toString());
		query.setParameter("dashletId", dashletId);
		return (Long) query.uniqueResult();
	}

}
