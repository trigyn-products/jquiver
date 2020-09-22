
package com.trigyn.jws.dashboard.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dashboard.entities.Dashboard;
import com.trigyn.jws.dashboard.entities.DashboardRoleAssociation;
import com.trigyn.jws.dashboard.vo.DashboardDashletVO;
import com.trigyn.jws.dbutils.repository.DBConnection;

@Repository
public class DashboardDaoImpl extends DBConnection {

	@Autowired
	public DashboardDaoImpl(DataSource dataSource) {
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
	 * @param dashboard
	 * @return {@link Dashboard}
	 * @throws Exception
	 */
	public Dashboard saveDashboardDetails(Dashboard dashboard)  throws Exception{
		getCurrentSession().saveOrUpdate(dashboard);
		getCurrentSession().flush();
		return dashboard;
	}

	/**
	 * @param dashboardRoleAssociation
	 * @throws Exception
	 */
	public void saveDashboardRoleAssociation(DashboardRoleAssociation dashboardRoleAssociation) throws Exception {
		getCurrentSession().saveOrUpdate(dashboardRoleAssociation);
	}

	/**
	 * @param contextId
	 * @param userRoles
	 * @param userId
	 * @return {@link List}
	 * @throws Exception
	 */
	public List<Object[]> findDashboardsByContextId(String contextId, List<String> userRoles, String userId) throws Exception {
		Query query = getCurrentSession().createQuery(QueryStore.HQL_QUERY_FIND_DASHBOARD_BY_CONTEXT_ID);
		query.setParameterList("userRoles", userRoles);
		query.setParameter("contextId", contextId);
		query.setParameter("userId", userId);
		List<Object[]> dashboards = (List<Object[]>) query.getResultList();
		return dashboards;
	}
	
	/**
	 * @param dashboardId
	 * @return {@link List}
	 * @throws Exception
	 */
	public List<Object[]> loadDashboardDashlets(String dashboardId) throws Exception {
		Query query = getCurrentSession().createQuery(QueryStore.HQL_QUERY_LOAD_DASHBOARD_DASHLETS);
		query.setParameter("dashboardId", dashboardId);
		return query.list();
	}

	/**
	 * @param contextName
	 * @return {@link String}
	 * @throws Exception
	 */
	public String getContextNameById(String contextName)  throws Exception{
		Map<String, Object> namedParams = new HashMap<>();
		namedParams.put("contextName", contextName);
		Map<String, Object> resultMap = namedParameterJdbcTemplate.queryForMap(QueryStore.SQL_QUERY_FIND_CONTEXT_NAME_BY_ID, namedParams);
		return resultMap.get("contextId").toString();
	}

	/**
	 * @param contextName
	 * @return {@link Integer}
	 * @throws Exception
	 */
	public Integer getContextBasedPermissions(String contextName) throws Exception {
		Map<String, Object> namedParams = new HashMap<>();
		namedParams.put("contextName", contextName);
		Map<String, Object> resultMap = namedParameterJdbcTemplate.queryForMap(QueryStore.SQL_QUERY_GET_CONTEXT_BASED_PERMISSION, namedParams);
		return (Integer) resultMap.get("allowDashboardAddition");
	}

	/**
	 * @param dashletId
	 * @param dashboardId
	 * @throws Exception
	 */
	public void removeDashletFromDashboard(String dashletId, String dashboardId) throws Exception {
		Map<String, Object> namedParams = new HashMap<>();
		namedParams.put("dashletId", dashletId);
		namedParams.put("dashboardId", dashboardId);
		namedParameterJdbcTemplate.update(QueryStore.SQL_QUERY_DELETE_DASHLET_FROM_DASHBOARD, namedParams);
	}
	
	
	/**
	 * @param contextId
	 * @return
	 */
	public DashboardDashletVO getDashletsByContextId(String contextId) {
		StringBuilder stringBuilder = new StringBuilder("SELECT dashletId AS dashletId, dashletName AS dashletName FROM Dashlet AS d WHERE d.contextId = :contextId");
		Query query = getCurrentSession().createQuery(stringBuilder.toString());
		query.setParameter("contextId", contextId);
		return (DashboardDashletVO) query.uniqueResult();
	}
}
