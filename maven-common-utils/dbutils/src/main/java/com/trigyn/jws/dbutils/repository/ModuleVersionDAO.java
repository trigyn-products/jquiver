package com.trigyn.jws.dbutils.repository;

import javax.sql.DataSource;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dbutils.entities.JwsModuleVersion;

@Repository
public class ModuleVersionDAO extends DBConnection {

	@Autowired
	public ModuleVersionDAO(DataSource dataSource) {
		super(dataSource);
	}

	private final static String	HQL_QUERY_TO_GET_VERSION_ID_BY_ENTITY_ID				= " SELECT MAX(versionId) FROM JwsModuleVersion jmv WHERE jmv.entityId=:entityId";

	private final static String	HQL_QUERY_TO_GET_VERSION_ID_BY_ENTITY_ID_AND_NAME		= " SELECT MAX(versionId) FROM JwsModuleVersion jmv WHERE jmv.entityId=:entityId AND jmv.entityName=:entityName";

	private final static String	HQL_QUERY_TO_GET_MODULE_JSON_BY_ID						= "SELECT jmv.moduleJson AS templateJson FROM JwsModuleVersion jmv WHERE jmv.moduleVersionId =:moduleVersionId ";

	private final static String	HQL_QUERY_TO_GET_CHECKSUM_BY_ENTITY_ID					= "SELECT jmv.moduleJsonChecksum AS moduleJsonChecksum FROM JwsModuleVersion jmv WHERE jmv.entityId=:entityId ORDER BY jmv.versionId DESC ";

	private final static String	HQL_QUERY_TO_GET_CHECKSUM_BY_ENTITY_ID_AND_ENTITY_NAME	= "SELECT jmv.moduleJsonChecksum AS moduleJsonChecksum FROM JwsModuleVersion jmv WHERE jmv.entityId=:entityId AND jmv.entityName=:entityName ORDER BY jmv.versionId DESC ";

	private final static String	HQL_QUERY_TO_DELETE_OLD_RECORDS							= "DELETE FROM jq_module_version WHERE entity_id=:entityId AND entity_name=:entityName "
			+ " ORDER BY version_id ASC LIMIT 1 ";

	private final static String	HQL_QUERY_TO_GET_VERSION_ID_COUNT						= "SELECT COUNT(jmv.moduleVersionId) FROM JwsModuleVersion AS jmv "
			+ " WHERE jmv.entityId=:entityId AND jmv.entityName=:entityName ";

	private static final String	HQL_QUERY_TO_GET_JSON_BY_ENTITY_ID_AND_NAME				= "SELECT jmv.moduleJson AS moduleJson  FROM JwsModuleVersion AS jmv "
			+ " WHERE jmv.entityId = :entityId AND jmv.entityName = :entityName ORDER BY jmv.versionId DESC ";

	public Double getVersionIdByEntityId(String entityId) throws Exception {
		Double	versiondId	= null;
		Query	query		= getCurrentSession().createQuery(HQL_QUERY_TO_GET_VERSION_ID_BY_ENTITY_ID);
		query.setParameter("entityId", entityId);
		Object versionIdObj = query.uniqueResult();
		if (versionIdObj != null) {
			versiondId = Double.parseDouble(versionIdObj.toString());
			return versiondId;
		}
		return null;
	}

	public Double getVersionIdByEntityIdAndName(String entityId, String entityName) throws Exception {
		Double	versiondId	= null;
		Query	query		= getCurrentSession().createQuery(HQL_QUERY_TO_GET_VERSION_ID_BY_ENTITY_ID_AND_NAME);
		query.setParameter("entityId", entityId);
		query.setParameter("entityName", entityName);
		Object versionIdObj = query.uniqueResult();
		if (versionIdObj != null) {
			versiondId = Double.parseDouble(versionIdObj.toString());
			return versiondId;
		}
		return null;
	}

	public void save(JwsModuleVersion moduleVersion) {
		getCurrentSession().saveOrUpdate(moduleVersion);
	}

	public String getModuleJsonById(String moduleVersionId) throws Exception {
		Query query = getCurrentSession().createQuery(HQL_QUERY_TO_GET_MODULE_JSON_BY_ID);
		query.setParameter("moduleVersionId", moduleVersionId);
		Object versionIdObj = query.uniqueResult();
		if (versionIdObj != null) {
			return versionIdObj.toString();
		}
		return null;
	}

	public String getLastUpdatedJsonData(String entityId, String entityName) throws Exception {
		Query query = getCurrentSession().createQuery(HQL_QUERY_TO_GET_JSON_BY_ENTITY_ID_AND_NAME);
		query.setParameter("entityId", entityId);
		query.setParameter("entityName", entityName);
		query.setMaxResults(1);
		Object versionIdObj = query.uniqueResult();
		if (versionIdObj != null) {
			return versionIdObj.toString();
		}
		return null;
	}

	public String getModuleJsonChecksum(String entityId) throws Exception {
		Query query = getCurrentSession().createQuery(HQL_QUERY_TO_GET_CHECKSUM_BY_ENTITY_ID);
		query.setParameter("entityId", entityId);
		query.setMaxResults(1);
		Object versionIdObj = query.uniqueResult();
		if (versionIdObj != null) {
			return versionIdObj.toString();
		}
		return null;
	}

	public String getModuleJsonChecksum(String entityId, String entityName) throws Exception {
		Query query = getCurrentSession().createQuery(HQL_QUERY_TO_GET_CHECKSUM_BY_ENTITY_ID_AND_ENTITY_NAME);
		query.setParameter("entityId", entityId);
		query.setParameter("entityName", entityName);
		query.setMaxResults(1);
		Object versionIdObj = query.uniqueResult();
		if (versionIdObj != null) {
			return versionIdObj.toString();
		}
		return null;
	}

	public Integer getVersionIdCount(String entityId, String entityName) throws Exception {
		Integer	versionIdCount	= null;
		Query	query			= getCurrentSession().createQuery(HQL_QUERY_TO_GET_VERSION_ID_COUNT);
		query.setParameter("entityId", entityId);
		query.setParameter("entityName", entityName);
		Object versionIdObj = query.uniqueResult();
		if (versionIdObj != null) {
			versionIdCount = Integer.parseInt(versionIdObj.toString());
		}
		return versionIdCount;
	}

	public void deleteOldRecords(String entityId, String entityName) throws Exception {
		Query query = getCurrentSession().createSQLQuery(HQL_QUERY_TO_DELETE_OLD_RECORDS);
		query.setParameter("entityId", entityId);
		query.setParameter("entityName", entityName);
		query.executeUpdate();
	}

}
