package com.trigyn.jws.dbutils.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.trigyn.jws.dbutils.entities.JwsModuleVersion;
import com.trigyn.jws.usermanagement.entities.JwsEntityRoleAssociation;
import com.trigyn.jws.usermanagement.repository.JwsEntityRoleAssociationRepository;
import com.trigyn.jws.usermanagement.vo.JwsEntityRoleAssociationVO;

@Repository
public class ModuleVersionDAO extends DBConnection {

	private final static Logger					logger													= LoggerFactory
			.getLogger(ModuleVersionDAO.class);

	private final static String					HQL_QUERY_TO_GET_VERSION_ID_BY_ENTITY_ID				= " SELECT MAX(versionId) FROM JwsModuleVersion jmv WHERE jmv.entityId=:entityId";

	private final static String					HQL_QUERY_TO_GET_VERSION_ID_BY_ENTITY_ID_AND_NAME		= " SELECT MAX(versionId) FROM JwsModuleVersion jmv WHERE jmv.entityId=:entityId AND jmv.entityName=:entityName";

	private final static String					HQL_QUERY_TO_GET_MODULE_JSON_BY_ID						= "SELECT jmv.moduleJson AS templateJson FROM JwsModuleVersion jmv WHERE jmv.moduleVersionId =:moduleVersionId ";

	private final static String					HQL_QUERY_TO_GET_CHECKSUM_BY_ENTITY_ID					= "SELECT jmv.moduleJsonChecksum AS moduleJsonChecksum FROM JwsModuleVersion jmv WHERE jmv.entityId=:entityId ORDER BY jmv.versionId DESC ";

	private final static String					HQL_QUERY_TO_GET_CHECKSUM_BY_ENTITY_ID_AND_ENTITY_NAME	= "SELECT jmv.moduleJsonChecksum AS moduleJsonChecksum FROM JwsModuleVersion jmv WHERE jmv.entityId=:entityId AND jmv.entityName=:entityName ORDER BY jmv.versionId DESC ";

	private final static String					HQL_QUERY_TO_DELETE_OLD_RECORDS							= "DELETE FROM jq_module_version WHERE entity_id=:entityId AND entity_name=:entityName "
			+ " ORDER BY version_id ASC LIMIT 1 ";

	private final static String					HQL_QUERY_TO_GET_VERSION_ID_COUNT						= "SELECT COUNT(jmv.moduleVersionId) FROM JwsModuleVersion AS jmv "
			+ " WHERE jmv.entityId=:entityId AND jmv.entityName=:entityName ";

	private static final String					HQL_QUERY_TO_GET_JSON_BY_ENTITY_ID_AND_NAME				= "SELECT jmv.moduleJson AS moduleJson  FROM JwsModuleVersion AS jmv "
			+ " WHERE jmv.entityId = :entityId AND jmv.entityName = :entityName ORDER BY jmv.versionId DESC ";

	private static final String					HQL_QUERY_TO_GET_JSON_BY_ENTITY_ID_AND_MODULE_ID		= "SELECT ted.moduleJson AS moduleJson  FROM TagEntityDetails AS ted "
			+ " WHERE ted.entityId = :entityId AND ted.moduleId = :moduleId ORDER BY ted.tagEntityDetailId DESC ";

	private final static String					HQL_QUERY_TO_GET_MODULE_VERSION_BY_ENITIY_ID			= "FROM JwsModuleVersion jmv WHERE jmv.entityId=:entityId  ORDER BY jmv.versionId DESC ";

	private final static String					HQL_QUERY_TO_GET_ROLE_NAME_BY_ROLE_ID					= "SELECT jr.roleName AS roleName FROM JwsRole jr WHERE jr.roleId=:roleId ORDER BY jr.roleId DESC ";

	private final static String					HQL_QUERY_TO_DELETE_OLD_PERMISSION_BY_ENTITY_ID_AND_MODULE_ID	= "DELETE FROM jq_entity_role_association WHERE entity_id=:entityId AND module_id=:moduleId ";
	
	@Autowired
	private JwsEntityRoleAssociationRepository	jwsEntityRoleAssociationRepository;
	
	public ModuleVersionDAO(DataSource dataSource) {
		super(dataSource);
	}

	public Double getVersionIdByEntityId(String entityId) throws Exception {
		Double	versiondId	= null;
		Query	query		= getCurrentSession().createQuery(HQL_QUERY_TO_GET_VERSION_ID_BY_ENTITY_ID, Double.class);
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
		Query	query		= getCurrentSession().createQuery(HQL_QUERY_TO_GET_VERSION_ID_BY_ENTITY_ID_AND_NAME,
				Double.class);
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
		getCurrentSession().persist(moduleVersion);
		getCurrentSession().flush();
	}

	public String getModuleJsonById(String moduleVersionId) throws Exception {
		Query query = getCurrentSession().createQuery(HQL_QUERY_TO_GET_MODULE_JSON_BY_ID, String.class);
		query.setParameter("moduleVersionId", moduleVersionId);
		Object versionIdObj = query.uniqueResult();
		if (versionIdObj != null) {
			return versionIdObj.toString();
		}
		return null;
	}

	public String getLastUpdatedJsonData(String entityId, String entityName) throws Exception {
		Query query = getCurrentSession().createQuery(HQL_QUERY_TO_GET_JSON_BY_ENTITY_ID_AND_NAME, String.class);
		query.setParameter("entityId", entityId);
		query.setParameter("entityName", entityName);
		query.setMaxResults(1);
		Object versionIdObj = query.uniqueResult();
		if (versionIdObj != null) {
			return versionIdObj.toString();
		}
		return null;
	}

	public String getLastUpdatedTagJsonData(String entityId, String moduleId) throws Exception {
		Query query = getCurrentSession().createQuery(HQL_QUERY_TO_GET_JSON_BY_ENTITY_ID_AND_MODULE_ID, String.class);
		query.setParameter("entityId", entityId);
		query.setParameter("moduleId", moduleId);
		query.setMaxResults(1);
		Object versionIdObj = query.uniqueResult();
		if (versionIdObj != null) {
			return versionIdObj.toString();
		}
		return null;
	}

	public String buildUpdatedPermissionJson(String entityId, String moduleId) {
		String jsonRoleNames;
		try {
			List<JwsEntityRoleAssociation>	permissionList	= jwsEntityRoleAssociationRepository
					.getEntityRoles(entityId, moduleId);
			List<String>					roleNames		= new ArrayList<>();

			for (JwsEntityRoleAssociation permission : permissionList) {
				String roleId = permission.getRoleId();
				Query query = getCurrentSession().createQuery(HQL_QUERY_TO_GET_ROLE_NAME_BY_ROLE_ID, String.class);
				query.setParameter("roleId", roleId);
				query.setMaxResults(1);
				Object roleNameObj = query.uniqueResult();
				if (roleNameObj != null) {
					roleNames.add(roleNameObj.toString());
				}
			}
			// Join all role names with commas
			String				roleNamesJoined	= String.join(",", roleNames);

			// Create map to hold final structure
			Map<String, String>	result			= new HashMap<>();
			result.put("roleNames", roleNamesJoined);

			// Convert to JSON
			jsonRoleNames = new Gson().toJson(result);
		} catch (Exception a_exc) {
			logger.debug("Execption occured while getting versionCOunt :: getVersionIdCount", a_exc);
			return "{}";
		}

		return jsonRoleNames;
	}

	public String getTagViewRoleNames(String roleIds) {
		String			jsonRoleNames;
		List<String>	roleNames	= new ArrayList<>();

		try {
			// Parse JSON string into a list of roleIds
			List<String> roleIdList = new Gson().fromJson(roleIds, new TypeToken<List<String>>() {
			}.getType());
			for (String roleId : roleIdList) {
				Query query = getCurrentSession().createQuery(HQL_QUERY_TO_GET_ROLE_NAME_BY_ROLE_ID, String.class);
				query.setParameter("roleId", roleId);
				query.setMaxResults(1);

				Object roleNameObj = query.uniqueResult();
				if (roleNameObj != null) {
					roleNames.add(roleNameObj.toString());
				}
			}

			// Join all role names with commas
			String				roleNamesJoined	= String.join(",", roleNames);

			// Create map to hold final structure
			Map<String, String>	result			= new HashMap<>();
			result.put("roleNames", roleNamesJoined);

			// Convert to JSON
			jsonRoleNames = new Gson().toJson(result);
			return jsonRoleNames; // Return as JSON string

		} catch (Exception a_exc) {
			logger.debug("Exception occurred while getting role names :: getTagViewRoleNames", a_exc);
			return "[]"; // return empty JSON array if error
		}
	}
	
	public void deleteExistingPermissionDetails(String moduleId, String entityId) {
		// Delete old permissions first
		MutationQuery deleteQuery = getCurrentSession()
				.createNativeMutationQuery(HQL_QUERY_TO_DELETE_OLD_PERMISSION_BY_ENTITY_ID_AND_MODULE_ID);
		deleteQuery.setParameter("entityId", entityId);
		deleteQuery.setParameter("moduleId", moduleId);
		deleteQuery.executeUpdate();
	}

	public String savePermissionDetails(String moduleId, String entityId, String permissionJson) {
		try {
			// Delete old permissions first
			deleteExistingPermissionDetails(moduleId,entityId);

			// Parse the incoming JSON into a list of JwsEntityRoleAssociation objects
			Gson							gson			= new GsonBuilder()
					.registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context) -> {
																		return new Date(
																				json.getAsJsonPrimitive().getAsLong());
																	})
					.create();
			List<JwsEntityRoleAssociation>	permissionList	= gson.fromJson(permissionJson,
					new TypeToken<List<JwsEntityRoleAssociation>>() {
																	}.getType());

			List<JwsEntityRoleAssociationVO>	voList		= permissionList.stream()
					.map(p -> convertJwsEntityRoleAssociationEntityToVO(p)).collect(Collectors.toList());

			List<JwsEntityRoleAssociation>		entityList	= voList.stream().map(vo -> convertVOtoEntity(vo))
					.collect(Collectors.toList());

			for (JwsEntityRoleAssociation entity : entityList) {
				getCurrentSession().persist(entity);
			}

			return gson.toJson(voList);

		} catch (Exception exc) {
			logger.error("Error occurred while saving permission details :: savePermissionDetails", exc);
			return "{\"status\":\"error\",\"message\":\"Failed to save permissions\"}";
		}
	}

	public JwsEntityRoleAssociationVO convertJwsEntityRoleAssociationEntityToVO(
			JwsEntityRoleAssociation entityRoleAssociation) {
		JwsEntityRoleAssociationVO vo = new JwsEntityRoleAssociationVO();
		return vo.convertEntityToVO(entityRoleAssociation);
	}

	public JwsEntityRoleAssociation convertVOtoEntity(JwsEntityRoleAssociationVO entityRoleAssociationVO) {
		JwsEntityRoleAssociationVO vo = new JwsEntityRoleAssociationVO();
		return vo.convertVOtoEntity(null, entityRoleAssociationVO);
	}

	public String getModuleJsonChecksum(String entityId) throws Exception {
		Query query = getCurrentSession().createQuery(HQL_QUERY_TO_GET_CHECKSUM_BY_ENTITY_ID, String.class);
		query.setParameter("entityId", entityId);
		query.setMaxResults(1);
		Object versionIdObj = query.uniqueResult();
		if (versionIdObj != null) {
			return versionIdObj.toString();
		}
		return null;
	}

	public String getModuleJsonChecksum(String entityId, String entityName) throws Exception {
		Query query = getCurrentSession().createQuery(HQL_QUERY_TO_GET_CHECKSUM_BY_ENTITY_ID_AND_ENTITY_NAME,
				String.class);
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
		try {

			Integer	versionIdCount	= null;
			Query	query			= getCurrentSession().createQuery(HQL_QUERY_TO_GET_VERSION_ID_COUNT, Long.class);
			query.setParameter("entityId", entityId);
			query.setParameter("entityName", entityName);
			Object versionIdObj = query.uniqueResult();
			if (versionIdObj != null) {
				versionIdCount = Integer.parseInt(versionIdObj.toString());
			}
			return versionIdCount;
		} catch (Exception a_exc) {
			logger.debug("Execption occured while getting versionCOunt :: getVersionIdCount", a_exc);
			return 1;
		}
	}

	public void deleteOldRecords(String entityId, String entityName) throws Exception {
		MutationQuery query = getCurrentSession().createNativeMutationQuery(HQL_QUERY_TO_DELETE_OLD_RECORDS);
		query.setParameter("entityId", entityId);
		query.setParameter("entityName", entityName);
		query.executeUpdate();
	}

	public JwsModuleVersion getModuleVersionDetails(String entityId) throws Exception {
		Query<JwsModuleVersion> query = getCurrentSession().createQuery(HQL_QUERY_TO_GET_MODULE_VERSION_BY_ENITIY_ID,
				JwsModuleVersion.class);
		query.setParameter("entityId", entityId);
		query.setMaxResults(1);
		return query.uniqueResult();
	}
}
