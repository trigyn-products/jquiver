package com.trigyn.jws.usermanagement.repository;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.dbutils.repository.DBConnection;
import com.trigyn.jws.usermanagement.entities.JwsEntityRoleAssociation;

@Repository
public class JwsEntityRoleAssociationDAO extends DBConnection {

	private final static String QUERY_TO_GET_ENTITY_ROLE_ASSOC_ID = " SELECT jera.entityRoleId FROM JwsEntityRoleAssociation jera WHERE jera.entityId=:entityId"
			+ " AND jera.entityName=:entityName AND jera.moduleId=:moduleId AND jera.roleId=:roleId";

	@Autowired
	public JwsEntityRoleAssociationDAO(DataSource dataSource) {
		super(dataSource);
	}

	public JwsEntityRoleAssociation findPermissionById(String entityRoleId) {
		JwsEntityRoleAssociation entityRole = hibernateTemplate.get(JwsEntityRoleAssociation.class, entityRoleId);
		if (entityRole != null)
			getCurrentSession().evict(entityRole);
		return entityRole;
	}

	public JwsEntityRoleAssociation findJERAByName(String entityId, String entityName, String moduleId, String roleId) {
		Query query = getCurrentSession().createQuery(QUERY_TO_GET_ENTITY_ROLE_ASSOC_ID);
		query.setParameter("entityId", entityId);
		query.setParameter("entityName", entityName);
		query.setParameter("moduleId", moduleId);
		query.setParameter("roleId", roleId);
		Object jeraValueObj = query.uniqueResult();
		if (jeraValueObj != null) {
			String jeraId = jeraValueObj.toString();
			JwsEntityRoleAssociation entityRole = hibernateTemplate.get(JwsEntityRoleAssociation.class, jeraId);
			return entityRole;
		} else {
			String queryStr = " SELECT jera.entityRoleId FROM JwsEntityRoleAssociation jera WHERE jera.entityId=:entityId"
					+ " AND jera.moduleId=:moduleId AND jera.roleId=:roleId";
			query = getCurrentSession().createQuery(queryStr);
			query.setParameter("entityId", entityId);
			query.setParameter("moduleId", moduleId);
			query.setParameter("roleId", roleId);
			jeraValueObj = query.uniqueResult();
			if (jeraValueObj != null) {
				String jeraId = jeraValueObj.toString();
				JwsEntityRoleAssociation entityRole = hibernateTemplate.get(JwsEntityRoleAssociation.class, jeraId);
				return entityRole;
			}

		}
		return null;
	}

	@Transactional(readOnly = false)
	public void saveJwsEntityRoleAssociation(JwsEntityRoleAssociation entityRole) {
		try {
			JwsEntityRoleAssociation jera = findJERAByName(entityRole.getEntityId(), entityRole.getEntityName(),
					entityRole.getModuleId(), entityRole.getRoleId());
			if (jera != null)
				getCurrentSession().evict(jera);

			if (entityRole.getEntityRoleId() == null
					|| (findPermissionById(entityRole.getEntityRoleId()) == null && jera == null)) {
				getCurrentSession().save(entityRole);
			} else {
				if (jera != null)
					entityRole.setEntityRoleId(jera.getEntityRoleId());
				getCurrentSession().saveOrUpdate(entityRole);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createPermission(String roleId, Integer isActive, String updatedBy) {
		String insertQuery = "INSERT INTO jq_entity_role_association(entity_role_id, entity_id, entity_name, module_id, role_id, last_updated_date, last_updated_by, is_active, module_type_id, is_custom_updated, role_type_id) "
				+ " SELECT UUID(), entity_id, entity_name, module_id, :roleId, NOW(), :updatedBy, :isActive, module_type_id, 1, role_type_id FROM jq_entity_role_association WHERE role_id= :adminRoleId ";

		Map<String, Object>	paramMap	= new HashMap<>();
		paramMap.put("roleId", roleId);
		paramMap.put("updatedBy", updatedBy);
		paramMap.put("isActive", isActive);
		paramMap.put("adminRoleId", "ae6465b3-097f-11eb-9a16-f48e38ab9348");
		namedParameterJdbcTemplate.update(insertQuery, paramMap);
	}

}
