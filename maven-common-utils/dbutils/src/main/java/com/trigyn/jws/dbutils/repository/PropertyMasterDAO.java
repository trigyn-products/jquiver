package com.trigyn.jws.dbutils.repository;

import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dbutils.entities.PropertyMaster;

@Repository
@Transactional
public class PropertyMasterDAO extends DBConnection {

	@Autowired
	public PropertyMasterDAO(DataSource dataSource) {
		super(dataSource);
	}

	private final static String QUERY_TO_GET_PROPERTY_MASTER_DETAILS = " SELECT pm.propertyValue FROM PropertyMaster pm WHERE pm.id.ownerType=:ownerType"
			+ " AND pm.id.ownerId=:ownerId AND pm.id.propertyName=:propertyName";

	public String findPropertyMasterValue(String ownerType, String ownerId, String propertyName) throws Exception {
		String propertyValue = null;
		Query query = getCurrentSession().createQuery(QUERY_TO_GET_PROPERTY_MASTER_DETAILS);
		query.setParameter("ownerType", ownerType);
		query.setParameter("ownerId", ownerId);
		query.setParameter("propertyName", propertyName);
		Object propertValueObj = query.uniqueResult();
		if (propertValueObj != null) {
			propertyValue = propertValueObj.toString();
		}
		return propertyValue;
	}
	
	public void save(PropertyMaster propertyMaster) {
		getCurrentSession().saveOrUpdate(propertyMaster);
	}
}
