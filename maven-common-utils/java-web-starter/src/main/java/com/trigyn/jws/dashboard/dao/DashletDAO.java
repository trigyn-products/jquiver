package com.trigyn.jws.dashboard.dao;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.query.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.dashboard.entities.Dashlet;
import com.trigyn.jws.dashboard.entities.DashletConfiguration;
import com.trigyn.jws.dashboard.entities.DashletProperties;
import com.trigyn.jws.dashboard.entities.DashletPropertyConfiguration;
import com.trigyn.jws.dashboard.utility.Constants;
import com.trigyn.jws.dbutils.repository.DBConnection;

@Repository
public class DashletDAO extends DBConnection {

	public DashletDAO(DataSource dataSource) {
		super(dataSource);
	}

	public Dashlet findById(String dashletId) throws Exception {
		return getCurrentSession().get(Dashlet.class, dashletId);
	}

	public DashletConfiguration saveDashletConfiguration(DashletConfiguration dashletConfigurationEntity) throws Exception {
		getCurrentSession().saveOrUpdate(dashletConfigurationEntity);
		return dashletConfigurationEntity;
	}

	public List<DashletProperties> findDashletPropertyByDashletId(String dashletId, boolean includeHidden) throws Exception {
		StringBuilder stringBuilder = new StringBuilder(
				"FROM DashletProperties AS dp WHERE dp.dashletId = :dashletId AND dp.isDeleted = :isDeleted");
		if (!includeHidden) {
			stringBuilder.append(" AND dp.toDisplay = :toDisplay ");
		}
		stringBuilder.append(" ORDER BY dp.sequence ASC");

		Query query = getCurrentSession().createQuery(stringBuilder.toString(), DashletProperties.class);
		query.setParameter("isDeleted", Constants.RecordStatus.INSERTED.getStatus());
		query.setParameter("dashletId", dashletId);
		if (!includeHidden) {
			query.setParameter("toDisplay", 1);
		}
		List<DashletProperties> dashletPropertiesList = (List<DashletProperties>) query.getResultList();
		return dashletPropertiesList;
	}

	public List<Object[]> getDashlets(List<String> userRoleList, String dashboardId) throws Exception {

		String	hqlQuery	= "FROM Dashlet AS dl INNER JOIN dl.dashboardAssociation AS da "
				+ " WHERE da.id.dashboardId =:dashboardId AND dl.isActive = :isActive GROUP BY dl.dashletId ";
		Query	hql			= getCurrentSession().createQuery(hqlQuery, Object[].class);
		hql.setParameter("isActive", Constants.DashletStatus.ACTIVE.getDashletStatus());
		hql.setParameter("dashboardId", dashboardId);
		List<Object[]> resultSet = (List<Object[]>) hql.getResultList();

		return resultSet;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getDashletData(String dataSourceId, String selectionQuery) throws Exception {
		List<Map<String, String>>	list			= null;
		JdbcTemplate				jdbcTemplate	= updateJdbcTemplateDataSource(dataSourceId);
		list = (List<Map<String, String>>) (Object) jdbcTemplate.queryForList(selectionQuery);
		return list;
	}

	public DashletPropertyConfiguration saveConfiguration(DashletPropertyConfiguration configuration) throws Exception {
		getCurrentSession().saveOrUpdate(configuration);
		return configuration;
	}

	@SuppressWarnings({ "unchecked" })
	public List<Object[]> getUserPreferences(String userId, String dashletId, String dashboardId) throws Exception {
		String	sql		= "SELECT dp.propertyId AS property, COALESCE(dpc.propertyValue, dp.defaultValue) AS value, dp.validation AS validation "
				+ " FROM DashletProperties dp LEFT OUTER JOIN DashletPropertyConfiguration dpc ON dp.propertyId = dpc.id.propertyId "
				+ "AND dpc.id.userId=:userId  WHERE dp.isDeleted = 0 AND dp.dashletId = :dashletId AND dpc.id.dashboardId =:dashboardId ";

		Query	query	= getCurrentSession().createQuery(sql, Object[].class);
		query.setParameter("userId", userId);
		query.setParameter("dashletId", dashletId);
		query.setParameter("dashboardId", dashboardId);
		return query.getResultList();
	}
	
	@SuppressWarnings({ "unchecked" })
	public List<Object[]> getUserDashletCoordinates(String userId, String dashletId, String dashboardId) throws Exception {
		String	sql		= "SELECT COALESCE(dc.xCoordinate,dsh.xCoordinate) AS xCoordinate,COALESCE(dc.yCoordinate,dsh.yCoordinate) AS yCoordinate "
				+ "FROM Dashlet dsh LEFT OUTER JOIN DashletConfiguration dc ON dsh.dashletId = dc.id.dashletId "
				+ "AND dc.id.userId=:userId where dsh.dashletId=:dashletId AND dc.id.dashboardId=:dashboardId ";
		Query	query	= getCurrentSession().createQuery(sql, Object[].class);
		query.setParameter("userId", userId);
		query.setParameter("dashletId", dashletId);
		query.setParameter("dashboardId", dashboardId);
		return query.getResultList();
	}

	public List<String> findDashletRoleByDashletId(String dashletId) throws Exception {
		Query query = getCurrentSession().createQuery(QueryStore.HQL_QUERY_TO_GET_DASHLET_ROLES_ID_BY_DASHLET_ID, String.class);
		query.setParameter("dashletId", dashletId);
		List<String> dashletRoleAssociations = (List<String>) query.getResultList();
		return dashletRoleAssociations;
	}

	public List<Map<String, Object>> getDashletsByContextId(String contextId) {
		StringBuilder	stringBuilder	= new StringBuilder(
				"SELECT dashletId AS dashletId, dashletName AS dashletName FROM Dashlet AS d WHERE d.contextId = :contextId");
		Query			query			= getCurrentSession().createQuery(stringBuilder.toString(), Map.class);
		query.setParameter("contextId", contextId);
		return query.getResultList();
	}

	public List<Dashlet> getAllDashlets(Integer dashletTypeId) throws Exception {
		Query query = getCurrentSession().createQuery("FROM Dashlet WHERE isActive =:isActive AND dashletTypeId = :dashletTypeId ", Dashlet.class);
		query.setParameter("isActive", Constants.DashletStatus.ACTIVE.getDashletStatus());
		query.setParameter("dashletTypeId", dashletTypeId);
		List<Dashlet> dashlets = (List<Dashlet>) query.getResultList();
		return dashlets;
	}

	public Dashlet getDashletByName(String dashletName) throws Exception {
		Query query = getCurrentSession()
				.createQuery("FROM Dashlet WHERE lower(dashletName) = lower(:dashletName) AND isActive =:isActive", List.class);
		query.setParameter("dashletName", dashletName);
		query.setParameter("isActive", Constants.DashletStatus.ACTIVE.getDashletStatus());
		Dashlet dashlet = (Dashlet) query.uniqueResult();
		return dashlet;
	}

	@Transactional(readOnly = false)
	public Dashlet saveDashet(Dashlet dashlet) {
		getCurrentSession().merge(dashlet);
		return dashlet;
	}

}
