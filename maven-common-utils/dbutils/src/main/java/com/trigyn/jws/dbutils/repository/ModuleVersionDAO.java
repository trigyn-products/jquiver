package com.trigyn.jws.dbutils.repository;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dbutils.entities.JwsModuleVersion;
import com.trigyn.jws.dbutils.utils.Constant;

@Repository
public class ModuleVersionDAO extends DBConnection{

	@Autowired
	public ModuleVersionDAO(DataSource dataSource) {
		super(dataSource);
	}
	
	private final static String HQL_QUERY_TO_GET_VERSION_ID_BY_ENTITY_ID = " SELECT MAX(versionId) FROM JwsModuleVersion jmv WHERE jmv.entityId=:entityId";
	
	private final static String HQL_QUERY_TO_GET_ALL_VERSIONS_BY_ENTITY_ID = " FROM JwsModuleVersion jmv WHERE jmv.entityId=:entityId ORDER BY jmv.versionId ASC ";
	
	private final static String HQL_QUERY_TO_GET_MODULE_JSON_BY_ENTITY_ID_AND_VERSION
		= "SELECT jmv.moduleJson AS templateJson FROM JwsModuleVersion jmv WHERE jmv.entityId=:entityId AND jmv.versionId = :versionId ";
	
	private final static String HQL_QUERY_TO_GET_CHECKSUM_BY_ENTITY_ID
	= "SELECT jmv.moduleJsonChecksum AS moduleJsonChecksum FROM JwsModuleVersion jmv WHERE jmv.entityId=:entityId ORDER BY jmv.versionId DESC ";

	private final static String HQL_QUERY_TO_DELETE_OLD_RECORDS = "DELETE FROM jws_module_version WHERE entity_id=:entityId "
			+ " ORDER BY version_id ASC LIMIT 1 "; 

	private final static String HQL_QUERY_TO_GET_VERSION_ID_COUNT = "SELECT COUNT(jmv.moduleVersionId) FROM JwsModuleVersion AS jmv "
			+ " WHERE jmv.entityId=:entityId "; 
	
	public Double getVersionIdByEntityId(String entityId) throws Exception{
		Double versiondId = null;
		Query query = getCurrentSession().createQuery(HQL_QUERY_TO_GET_VERSION_ID_BY_ENTITY_ID);
		query.setParameter("entityId", entityId);
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
	
	
	@SuppressWarnings("unchecked")
	public List<JwsModuleVersion> getModuleVersionByEntityId(String entityId) throws Exception{
		List<JwsModuleVersion> jwsTemplateVersions = new ArrayList<>(); 
		Query query = getCurrentSession().createQuery(HQL_QUERY_TO_GET_ALL_VERSIONS_BY_ENTITY_ID);
		query.setParameter("entityId", entityId);
		jwsTemplateVersions = (List<JwsModuleVersion>) query.list();
		return jwsTemplateVersions;
	}
	
	public String getModuleData(String entityId, Double versionId) throws Exception{
		Query query = getCurrentSession().createQuery(HQL_QUERY_TO_GET_MODULE_JSON_BY_ENTITY_ID_AND_VERSION);
		query.setParameter("entityId", entityId);
		query.setParameter("versionId", versionId);
		Object versionIdObj = query.uniqueResult();
		if (versionIdObj != null) {
			return versionIdObj.toString();
		}
		return null;
	}
	
	
	public String getModuleJsonChecksum(String entityId) throws Exception{
		Query query = getCurrentSession().createQuery(HQL_QUERY_TO_GET_CHECKSUM_BY_ENTITY_ID);
		query.setParameter("entityId", entityId);
		query.setMaxResults(1);
		Object versionIdObj = query.uniqueResult();
		if (versionIdObj != null) {
			return versionIdObj.toString();
		}
		return null;
	}
	
	public Integer getVersionIdCount(String entityId) throws Exception{
		Integer versionIdCount = null;
		Query query = getCurrentSession().createQuery(HQL_QUERY_TO_GET_VERSION_ID_COUNT);
		query.setParameter("entityId", entityId);
		Object versionIdObj = query.uniqueResult();
		if (versionIdObj != null) {
			versionIdCount = Integer.parseInt(versionIdObj.toString());
		}
		return versionIdCount;
	}
	
	
	public void deleteOldRecords(String entityId) throws Exception{
		Query query = getCurrentSession().createSQLQuery(HQL_QUERY_TO_DELETE_OLD_RECORDS);
		query.setParameter("entityId", entityId);
		query.executeUpdate();
	}

}
