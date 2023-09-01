package com.trigyn.jws.dbutils.repository;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.dbutils.entities.PropertyMaster;

@Repository
@Transactional
public class PropertyMasterDAO extends DBConnection {

	@Autowired
	public PropertyMasterDAO(DataSource dataSource) {
		super(dataSource);
	}

	private final static String	QUERY_TO_GET_PROPERTY_MASTER_DETAILS		= " SELECT pm.propertyValue FROM PropertyMaster pm WHERE pm.ownerType=:ownerType"
			+ " AND pm.ownerId=:ownerId AND pm.propertyName=:propertyName";

	private final static String	QUERY_TO_GET_ALL_PROPERTY_MASTER_DETAILS	= " SELECT pm.ownerId AS ownerId, pm.ownerType AS ownerType, pm.propertyName AS propertyName, pm.propertyValue AS propertyValue FROM PropertyMaster AS pm ";

	private final static String	QUERY_TO_GET_PROPERTY_MASTER_ID		= " SELECT pm.propertyMasterId FROM PropertyMaster pm WHERE pm.ownerType=:ownerType"
			+ " AND pm.ownerId=:ownerId AND pm.propertyName=:propertyName";

	public String findPropertyMasterValue(String ownerType, String ownerId, String propertyName) throws Exception {
		String	propertyValue	= null;
		Query	query			= getCurrentSession().createQuery(QUERY_TO_GET_PROPERTY_MASTER_DETAILS);
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

	public List<Map<String, Object>> findAll() {
		Query query = getCurrentSession().createQuery(QUERY_TO_GET_ALL_PROPERTY_MASTER_DETAILS);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}

	public PropertyMaster findPropertyMasterById(String propertyMasterId) {
		PropertyMaster propertyMaster =  hibernateTemplate.get(PropertyMaster.class, propertyMasterId);
		if(propertyMaster != null) getCurrentSession().evict(propertyMaster);
		return propertyMaster;
	}

	public PropertyMaster findPropertyMasterByName(String propertyName) {
		Query	query			= getCurrentSession().createQuery(QUERY_TO_GET_PROPERTY_MASTER_ID);
		query.setParameter("ownerType", "system");
		query.setParameter("ownerId", "system");
		query.setParameter("propertyName", propertyName);
		Object propertValueObj = query.uniqueResult();
		if (propertValueObj != null) {
			String propertyId = propertValueObj.toString();
			PropertyMaster propertyMaster =  hibernateTemplate.get(PropertyMaster.class, propertyId);
			return propertyMaster;
		}
		return null;		
	}

	@Transactional(readOnly = false)
	public void savePropertyMaster(PropertyMaster propertyMaster) {
		PropertyMaster prop = findPropertyMasterByName(propertyMaster.getPropertyName());
		if(prop != null) getCurrentSession().evict(prop);
		if(propertyMaster.getPropertyMasterId() == null || 
				(findPropertyMasterById(propertyMaster.getPropertyMasterId()) == null && prop == null)) {
			getCurrentSession().save(propertyMaster);			
		}else {
			if(prop != null) propertyMaster.setPropertyMasterId(prop.getPropertyMasterId());
			getCurrentSession().saveOrUpdate(propertyMaster);
		}
	}

}
