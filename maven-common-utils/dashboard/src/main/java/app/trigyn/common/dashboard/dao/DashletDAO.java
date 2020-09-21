package app.trigyn.common.dashboard.dao;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import app.trigyn.common.dashboard.entities.Dashlet;
import app.trigyn.common.dashboard.entities.DashletConfiguration;
import app.trigyn.common.dashboard.entities.DashletProperties;
import app.trigyn.common.dashboard.entities.DashletPropertyConfiguration;
import app.trigyn.common.dashboard.entities.DashletRoleAssociation;
import app.trigyn.common.dashboard.utility.Constants;
import app.trigyn.common.dbutils.repository.DBConnection;

@Repository
public class DashletDAO extends DBConnection {

	@Autowired
	public DashletDAO(DataSource dataSource) {
		super(dataSource);
	}

	public Dashlet findById(String dashletId) throws Exception {
		return hibernateTemplate.get(Dashlet.class, dashletId);
	}

	public DashletProperties saveDashletProperty(DashletProperties dashletPropertyEntity) throws Exception {
		getCurrentSession().saveOrUpdate(dashletPropertyEntity);
		return dashletPropertyEntity;
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
		stringBuilder.append(" ORDER BY dp.id.sequence ASC");

		Query query = getCurrentSession().createQuery(stringBuilder.toString());
		query.setParameter("isDeleted", Constants.RecordStatus.INSERTED.getStatus());
		query.setParameter("dashletId", dashletId);
		if (!includeHidden) {
			query.setParameter("toDisplay", 1);
		}
		List<DashletProperties> dashletPropertiesList = (List<DashletProperties>) query.getResultList();
		return dashletPropertiesList;
	}

	public List<Object[]> getDashlets(List<String> userRoleList,String dashboardId) throws Exception {

		String hqlQuery = "FROM Dashlet AS d INNER JOIN d.roleAssociation AS ra INNER JOIN d.dashboardAssociation AS da WHERE ra.id.roleId IN ( :roleIds ) AND d.isActive = :isActive AND da.id.dashboardId =:dashboardId  GROUP BY d.dashletId";
		Query hql = getCurrentSession().createQuery(hqlQuery);
		hql.setParameter("isActive", Constants.DashletStatus.ACTIVE.getDashletStatus());
		hql.setParameter("roleIds", userRoleList);
		hql.setParameter("dashboardId",dashboardId);
		List<Object[]> resultSet = (List<Object[]>) hql.getResultList();

		return resultSet;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getDashletData(String selectionQuery) throws Exception {
		List<Map<String, String>> list = null;
		list = (List<Map<String, String>>) (Object) jdbcTemplate.queryForList(selectionQuery);
		return list;
	}

	public DashletPropertyConfiguration saveConfiguration(DashletPropertyConfiguration configuration) throws Exception {
		getCurrentSession().saveOrUpdate(configuration);
		return configuration;
	}

	@SuppressWarnings({ "unchecked" })
	public List<Object[]> getUserPreferences(String userId, String dashletId) throws Exception {
		String sql = "SELECT dp.id.propertyId AS property, COALESCE(dpc.propertyValue, dp.defaultValue) AS value "
				+ " FROM DashletProperties dp LEFT OUTER JOIN DashletPropertyConfiguration dpc ON dp.id.propertyId = dpc.id.propertyId "
				+ "AND dpc.id.userId=:userId  WHERE dp.isDeleted = 0 AND dp.dashletId = :dashletId ";

		Query query = getCurrentSession().createQuery(sql);
		query.setParameter("userId", userId);
		query.setParameter("dashletId", dashletId);
		return query.getResultList();
	}

	@SuppressWarnings({ "unchecked" })
	public List<Object[]> getUserDashletCoordinates(String userId, String dashletId)
			throws Exception {
				String sql="SELECT COALESCE(dc.xCoordinate,dsh.xCoordinate) AS xCoordinate,COALESCE(dc.yCoordinate,dsh.yCoordinate) AS yCoordinate "+
				"FROM Dashlet dsh LEFT OUTER JOIN DashletConfiguration dc ON dsh.dashletId = dc.id.dashletId "+
				"AND dc.id.userId=:userId where dsh.dashletId=:dashletId ";
	 Query query = getCurrentSession().createQuery(sql);
	 query.setParameter("userId", userId);
	 query.setParameter("dashletId", dashletId);
	 return query.getResultList();
	}

	public void saveRoleAssociation(DashletRoleAssociation dashletRoleAssociation) {
		getCurrentSession().saveOrUpdate(dashletRoleAssociation);
	}

	/**
	 * @param dashletId
	 * @return
	 */
	public List<String> findDashletRoleByDashletId(String dashletId)throws Exception {
		Query query = getCurrentSession().createQuery(QueryStore.HQL_QUERY_TO_GET_DASHLET_ROLES_ID_BY_DASHLET_ID);
		query.setParameter("dashletId", dashletId);
		List<String> dashletRoleAssociations = (List<String>) query.getResultList();
		return dashletRoleAssociations;
	}

	public List<Map<String, Object>> getDashletsByContextId(String contextId) {
		StringBuilder stringBuilder = new StringBuilder(
				"SELECT dashletId AS dashletId, dashletName AS dashletName FROM Dashlet AS d WHERE d.contextId = :contextId");
		Query query = getCurrentSession().createQuery(stringBuilder.toString());
		query.setParameter("contextId", contextId);
		return query.getResultList();
	}
	

	public List<Dashlet> getAllDashlets()throws Exception {
		Query query = getCurrentSession().createQuery("FROM Dashlet WHERE isActive =:isActive");
		query.setParameter("isActive",  Constants.DashletStatus.ACTIVE.getDashletStatus());
		List<Dashlet> dashlets = (List<Dashlet>) query.getResultList();
		return dashlets;
	}
	
	public Dashlet getDashletByName(String dashletName)throws Exception {
		Query query = getCurrentSession().createQuery("FROM Dashlet WHERE lower(dashletName) = lower(:dashletName) AND isActive =:isActive");
		query.setParameter("dashletName", dashletName);
		query.setParameter("isActive",  Constants.DashletStatus.ACTIVE.getDashletStatus());
		Dashlet dashlet = (Dashlet) query.uniqueResult();
		return dashlet;
	}
	
}
