package com.trigyn.jws.dbutils.repository;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dbutils.entities.JwsTemplateVersion;

@Repository
public class TemplateVersionDAO extends DBConnection{

	@Autowired
	public TemplateVersionDAO(DataSource dataSource) {
		super(dataSource);
	}
	
	private final static String HQL_QUERY_TO_GET_VERSION_ID_BY_ENTITY_ID = " SELECT MAX(versionId) FROM JwsTemplateVersion jtv WHERE jtv.entityId=:entityId";
	
	private final static String HQL_QUERY_TO_GET_ALL_VERSIONS_BY_ENTITY_ID = " FROM JwsTemplateVersion jtv WHERE jtv.entityId=:entityId ORDER BY jtv.versionId ASC ";
	
	private final static String HQL_QUERY_TO_GET_TEMPLATE_JSON_BY_ENTITY_ID_AND_VERSION
		= "SELECT jtv.templateJson AS templateJson FROM JwsTemplateVersion jtv WHERE jtv.entityId=:entityId AND jtv.versionId = :versionId ";
	
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
	
	public void save(JwsTemplateVersion templateVersion) {
		getCurrentSession().saveOrUpdate(templateVersion);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<JwsTemplateVersion> getTemplateVersionByEntityId(String entityId) throws Exception{
		List<JwsTemplateVersion> jwsTemplateVersions = new ArrayList<>(); 
		Query query = getCurrentSession().createQuery(HQL_QUERY_TO_GET_ALL_VERSIONS_BY_ENTITY_ID);
		query.setParameter("entityId", entityId);
		jwsTemplateVersions = (List<JwsTemplateVersion>) query.list();
		return jwsTemplateVersions;
	}
	
	public String getTemplateData(String entityId, Double versionId) throws Exception{
		Query query = getCurrentSession().createQuery(HQL_QUERY_TO_GET_TEMPLATE_JSON_BY_ENTITY_ID_AND_VERSION);
		query.setParameter("entityId", entityId);
		query.setParameter("versionId", versionId);
		Object versionIdObj = query.uniqueResult();
		if (versionIdObj != null) {
			return versionIdObj.toString();
		}
		return null;
	}
}
