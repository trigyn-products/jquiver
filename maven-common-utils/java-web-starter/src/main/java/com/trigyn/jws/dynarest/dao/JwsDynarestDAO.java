package com.trigyn.jws.dynarest.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.trigyn.jws.dbutils.entities.JwsBusinessModule;
import com.trigyn.jws.dbutils.entities.JwsBusinessModuleEntity;
import com.trigyn.jws.dbutils.repository.DBConnection;
import com.trigyn.jws.dynamicform.utils.Constant;
import com.trigyn.jws.dynarest.entities.JqSchedulerLog;
import com.trigyn.jws.dynarest.entities.JwsDynamicRestDaoDetail;
import com.trigyn.jws.dynarest.entities.JwsDynamicRestDetail;
import com.trigyn.jws.dynarest.utils.Constants;
import com.trigyn.jws.sciptlibrary.entities.ScriptLibraryConnection;
import com.trigyn.jws.sciptlibrary.entities.ScriptLibraryDetails;
import com.trigyn.jws.webstarter.utils.RedissonQueryCacheManagerUtil;

@Repository
public class JwsDynarestDAO extends DBConnection {

	@Autowired
	private RedissonQueryCacheManagerUtil cacheManager = null;

	public JwsDynarestDAO(DataSource dataSource) {
		super(dataSource);
	}

	@Transactional
	public JwsDynamicRestDetail findDynamicRestById(String dynarestDetailsId) {
		JwsDynamicRestDetail jwsDynamicRestDetail = getCurrentSession().get(JwsDynamicRestDetail.class,
				dynarestDetailsId);
		if (jwsDynamicRestDetail != null)
			getCurrentSession().evict(jwsDynamicRestDetail);
		return jwsDynamicRestDetail;
	}

	@Transactional
	public JwsDynamicRestDetail findDynamicRestByUrl(String jwsDynamicRestUrl) {
		Query query = getCurrentSession().createQuery(
				" FROM JwsDynamicRestDetail " + " WHERE lower(jwsDynamicRestUrl) = lower(:jwsDynamicRestUrl)",
				JwsDynamicRestDetail.class);
		query.setParameter("jwsDynamicRestUrl", jwsDynamicRestUrl);
		JwsDynamicRestDetail data = (JwsDynamicRestDetail) query.uniqueResult();
		if (data != null)
			getCurrentSession().evict(data);
		return data;
	}

	public List<Map<String, Object>> executeQueries(String dataSourceId, String query,
			Map<String, Object> parameterMap) {
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = updateNamedParameterJdbcTemplateDataSource(
				dataSourceId);
		return namedParameterJdbcTemplate.queryForList(query, parameterMap);
	}

	public List<JwsDynamicRestDetail> getAllDynamicRestDetails() {
		List<JwsDynamicRestDetail> dynamicRestDetails = (List<JwsDynamicRestDetail>) getCurrentSession()
				.createNamedQuery("JwsDynamicRestDetail.findAll", JwsDynamicRestDetail.class).getResultList();
		return dynamicRestDetails;
	}

	public void saveJwsDynamicRestDetail(JwsDynamicRestDetail currentDynamicRestDetail) {
		// getCurrentSession().saveOrUpdate(currentDynamicRestDetail);
		JwsDynamicRestDetail data = getDynamicRestDetailsByName(currentDynamicRestDetail.getJwsMethodName());
		if (currentDynamicRestDetail.getJwsDynamicRestId() == null
				|| (findDynamicRestById(currentDynamicRestDetail.getJwsDynamicRestId()) == null && data == null)) {
			getCurrentSession().persist(currentDynamicRestDetail);
		} else {
			if (data != null)
				currentDynamicRestDetail.setJwsDynamicRestId(data.getJwsDynamicRestId());
			getCurrentSession().merge(currentDynamicRestDetail);
		}
		String cacheKey = com.trigyn.jws.webstarter.utils.Constant.TargetLookupId.DYNAMICREST.getTargetLookupId() + "::" + currentDynamicRestDetail.getJwsDynamicRestId();
		cacheManager.invalidateDtoORScalarValues("targetTypeDetailsCache", cacheKey);
	}

	@Transactional(readOnly = false)
	public void saveDynaRestDetail(JwsDynamicRestDetail dynarest,
			List<JwsDynamicRestDaoDetail> jwsDynamicRestDaoDetails) {
		JwsDynamicRestDetail data = getDynamicRestDetailsByName(dynarest.getJwsMethodName());
		if (dynarest.getJwsDynamicRestId() == null
				|| (findDynamicRestById(dynarest.getJwsDynamicRestId()) == null && data == null)) {
			getCurrentSession().persist(dynarest);
		} else {
			if (data != null)
				dynarest.setJwsDynamicRestId(data.getJwsDynamicRestId());
			getCurrentSession().merge(dynarest);

		}

		deleteDAOQueries(dynarest.getJwsDynamicRestId());
		for (JwsDynamicRestDaoDetail daoDetails : jwsDynamicRestDaoDetails) {
			daoDetails.setJwsDynamicRestDetailId(dynarest.getJwsDynamicRestId());
			saveJwsDynamicRestDAO(daoDetails);

		}
		String cacheKey = com.trigyn.jws.webstarter.utils.Constant.TargetLookupId.DYNAMICREST.getTargetLookupId() + "::" + dynarest.getJwsDynamicRestId();
		cacheManager.invalidateDtoORScalarValues("targetTypeDetailsCache", cacheKey);
	}

	public JwsDynamicRestDetail getDynamicRestDetailsByName(String jwsMethodName) {
		Query query = getCurrentSession().createQuery(
				" FROM JwsDynamicRestDetail  WHERE lower(jwsMethodName) = lower(:jwsMethodName)",
				JwsDynamicRestDetail.class);
		query.setParameter("jwsMethodName", jwsMethodName);
		JwsDynamicRestDetail data = (JwsDynamicRestDetail) query.uniqueResult();
		if (data != null)
			getCurrentSession().evict(data);
		return data;
	}

	public void deleteDAOQueries(String jwsDynamicRestDetailId) {
		MutationQuery query = getCurrentSession().createMutationQuery(
				"DELETE FROM JwsDynamicRestDaoDetail  WHERE jwsDynamicRestDetailId = :jwsDynamicRestDetailId");
		query.setParameter("jwsDynamicRestDetailId", jwsDynamicRestDetailId);
		query.executeUpdate();
		getCurrentSession().flush();
	}

	public void deleteDAOQueriesById(String jwsDynamicRestDetailId, List<Integer> daoDetailsIdList) {
		StringBuilder deleteDaoQuery = new StringBuilder(
				"DELETE FROM JwsDynamicRestDaoDetail AS jdrdd WHERE jdrdd.jwsDynamicRestDetailId = :jwsDynamicRestDetailId ");

		if (!CollectionUtils.isEmpty(daoDetailsIdList)) {
			deleteDaoQuery.append(" AND jdrdd.jwsDaoDetailsId NOT IN(:daoDetailsIdList) ");
		}

		MutationQuery query = getCurrentSession().createMutationQuery(deleteDaoQuery.toString());
		query.setParameter("jwsDynamicRestDetailId", jwsDynamicRestDetailId);
		if (!CollectionUtils.isEmpty(daoDetailsIdList)) {
			query.setParameterList("daoDetailsIdList", daoDetailsIdList);
		}
		query.executeUpdate();
	}

	public void saveJwsDynamicRestDAO(JwsDynamicRestDaoDetail daoDetail) {
		getCurrentSession().persist(daoDetail);
	}

	public int executeDMLQueries(String dataSourceId, String query, Map<String, Object> parameterMap) {
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = updateNamedParameterJdbcTemplateDataSource(
				dataSourceId);
		return namedParameterJdbcTemplate.update(query, parameterMap);
	}

	public String getRequestTypeById(Integer requestTypeId) {
		String querySQL = "SELECT jws_request_type FROM jq_request_type_details WHERE jws_request_type_details_id ="
				+ requestTypeId + "";
		return String.valueOf(getCurrentSession().createNativeQuery(querySQL, String.class).uniqueResult());
	}

	@Transactional
	public void saveJqSchedulerLog(JqSchedulerLog jqSchedulerLog) {
		getCurrentSession().persist(jqSchedulerLog);
	}

	public final List<Object> scriptLibExecution(String dynarestId) {
		Query scriptLibQuery = getCurrentSession().createNativeQuery(
				" SELECT jqsl.template_id " + "	FROM jq_script_lib_connect jqs LEFT JOIN jq_dynamic_rest_details jqd "
						+ "	ON jqd.jws_dynamic_rest_id = jqs.entity_id " + "	LEFT JOIN jq_script_lib_details jqsl "
						+ "	ON jqs.script_lib_id = jqsl.script_lib_id "
						+ "	WHERE jqs.module_type_id = :moduleId AND jqd.jws_dynamic_rest_id = :dynarestId",
				Object.class);

		scriptLibQuery.setParameter("moduleId", Constants.DYNAMIC_REST_MOD_ID);
		scriptLibQuery.setParameter("dynarestId", dynarestId);

		List<Object[]>	scriptLibList	= scriptLibQuery.list();
		List<Object>	resultMap		= new ArrayList<>();

		for (int iCounter = 0; iCounter < scriptLibList.size(); iCounter++) {
			Query roleQuery = getCurrentSession().createNativeQuery(
					"SELECT COUNT(*) FROM `jq_entity_role_association`"
							+ " WHERE `role_id` = :roleId AND `is_active` = :isActive AND entity_id = :entityId ",
					Object.class);
			roleQuery.setParameter("roleId", Constant.ANONYMOUS_ROLE_ID);
			roleQuery.setParameter("isActive", Constant.IS_ACTIVE);
			roleQuery.setParameter("entityId", scriptLibList.get(iCounter));
			List<Object[]> roleCount = roleQuery.list();
			if (roleQuery.list().get(0).toString().equalsIgnoreCase("0")) {
				Query templateQuery = getCurrentSession().createNativeQuery(
						"SELECT template FROM jq_template_master" + " WHERE template_id = :templateId ", Object.class);
				templateQuery.setParameter("templateId", scriptLibList.get(iCounter));
				List<Object[]> listTemplate = templateQuery.list();
				resultMap.add(listTemplate.get(0));
			}
		}

		return resultMap;
	}
	
	public ScriptLibraryDetails getScriptLibDetails(String scriptLibId) {
	    Query querySQL = getCurrentSession().createNativeQuery(
	        "SELECT * FROM jq_script_lib_details WHERE script_lib_id = :scriptLibId",
	        ScriptLibraryDetails.class);

	    querySQL.setParameter("scriptLibId", scriptLibId);

	    // Return single result safely
	    List<ScriptLibraryDetails> results = querySQL.getResultList();
	    return results.isEmpty() ? null : results.get(0);
	}
	
	public List<ScriptLibraryConnection> getscriptLibraryConnDetails(String scriptLibId, String entityId, String moduleId) {
	    if (moduleId.equals(Constant.FILEBINMODID)) {
	        List<String> entityIds = Arrays.asList(
	            "upload_" + entityId,
	            "view_" + entityId,
	            "delete_" + entityId
	        );

	        Query querySQL = getCurrentSession().createNativeQuery(
	            "SELECT * FROM jq_script_lib_connect " +
	            "WHERE script_lib_id = :scriptLibId " +
	            "AND entity_id IN (:entityIds) " +
	            "AND module_type_id = :moduleId",
	            ScriptLibraryConnection.class
	        );

	        querySQL.setParameter("scriptLibId", scriptLibId);
	        querySQL.setParameterList("entityIds", entityIds);
	        querySQL.setParameter("moduleId", moduleId);

	        return querySQL.list();
	    }

	    // fallback if not FILEBINMODID
	    Query querySQL = getCurrentSession().createNativeQuery(
	        "SELECT * FROM jq_script_lib_connect " +
	        "WHERE script_lib_id = :scriptLibId " +
	        "AND entity_id = :entityId " +
	        "AND module_type_id = :moduleId",
	        ScriptLibraryConnection.class
	    );
	    querySQL.setParameter("scriptLibId", scriptLibId);
	    querySQL.setParameter("entityId", entityId);
	    querySQL.setParameter("moduleId", moduleId);

	    return querySQL.list();
	}

	
	public JwsBusinessModule getBusinessModuleDetails(String businessModuleId) {
	    Query querySQL = getCurrentSession().createNativeQuery(
	        "SELECT * FROM jq_business_module WHERE business_module_id = :businessModuleId",
	        JwsBusinessModule.class);

	    querySQL.setParameter("businessModuleId", businessModuleId);

	    // Return single result safely
	    List<JwsBusinessModule> results = querySQL.getResultList();
	    return results.isEmpty() ? null : results.get(0);
	}
	
	public List<String> getdynamicFormQueryID(String dynamicformId) {
		Query querySQL = getCurrentSession().createNativeQuery(
				"SELECT dynamic_form_query_id FROM jq_dynamic_form_save_queries WHERE dynamic_form_id = :dynamicformId", String.class);
		querySQL.setParameter("dynamicformId", dynamicformId);
		List<String> dynamicFormSaveQueryList = querySQL.list();
		return dynamicFormSaveQueryList;
	}

	
	public List<String> getmoduleDetailsId(String entityId, String moduleId) {
		Query querySQL = getCurrentSession().createNativeQuery(
				"SELECT business_module_id FROM jq_business_module_entity_details" + " WHERE entity_id = :entityId AND module_id = :moduleId", String.class);
		querySQL.setParameter("entityId", entityId);
		querySQL.setParameter("moduleId", moduleId);
		List<String> businessModuleIdList = querySQL.list();
		return businessModuleIdList;
	}
	
	public List<JwsBusinessModuleEntity> getBusinessModuleEntityDetails(String businessModuleId,String entityId, String moduleId) {
		Query querySQL = getCurrentSession().createNativeQuery(
				"SELECT * FROM jq_business_module_entity_details" + " WHERE business_module_id = :businessModuleId AND entity_id = :entityId AND module_id = :moduleId", JwsBusinessModuleEntity.class);
		querySQL.setParameter("businessModuleId", businessModuleId);
		querySQL.setParameter("entityId", entityId);
		querySQL.setParameter("moduleId", moduleId);
		List<JwsBusinessModuleEntity> businessModuleIdList = querySQL.list();
		return businessModuleIdList;
	}

	public List<String> getscriptLibId(String entityId) {
		Query querySQL = getCurrentSession().createNativeQuery(
				"SELECT script_lib_id FROM jq_script_lib_connect" + " WHERE entity_id = :entityId", String.class);
		querySQL.setParameter("entityId", entityId);
		List<String> scriptLibIdList = querySQL.list();
		return scriptLibIdList;
	}

	public List<String> getscriptLibConnDetails(String scriptLibId) {
		Query querySQL = getCurrentSession().createNativeQuery(
				"SELECT * FROM jq_script_lib_connect" + " WHERE script_lib_id = :scriptLibId", String.class);
		querySQL.setParameter("scriptLibId", scriptLibId);
		List<String> scriptLibIdList = querySQL.list();
		return scriptLibIdList;
	}

	public void scriptLibDeleteById(String entityid) {
		MutationQuery deleteScriptLibQuery = getCurrentSession()
				.createNativeMutationQuery("delete from jq_script_lib_connect" + " where entity_id=:entityid");
		deleteScriptLibQuery.setParameter("entityid", entityid);
		int resultSetDelete = deleteScriptLibQuery.executeUpdate();
	}
	
	@Transactional
	public void businessModEntityDeleteById(String entityid) {
		MutationQuery deleteBusinessModEntities = getCurrentSession()
				.createNativeMutationQuery("delete from jq_business_module_entity_details" + " where entity_id=:entityid AND module_id=:moduleid");
		deleteBusinessModEntities.setParameter("entityid", entityid);
		deleteBusinessModEntities.setParameter("moduleid", Constant.SCHEDULER_MOD_ID);
		int resultSetDelete = deleteBusinessModEntities.executeUpdate();
	}

	public List<JwsDynamicRestDetail> getAllDynamicRestDetails(Integer dynamicRestTypeId) {
		Query query = getCurrentSession().createQuery(
				"FROM JwsDynamicRestDetail AS dr WHERE dr.jwsDynamicRestTypeId = :dynamicRestTypeId",
				JwsDynamicRestDetail.class);
		query.setParameter("dynamicRestTypeId", dynamicRestTypeId);
		return query.list();
	}

	@Transactional
	public ScriptLibraryDetails findScriptLibDetailConnectById(String scriptLibraryId) {
		ScriptLibraryDetails scriptLibrary = getCurrentSession().get(ScriptLibraryDetails.class, scriptLibraryId);
		if (scriptLibrary != null)
			getCurrentSession().evict(scriptLibrary);
		return scriptLibrary;
	}
	
	public String matchDynaRestUrl(String requestUri) {
		Query<String> restUrlQuery = getCurrentSession().createQuery(
				"SELECT DISTINCT jdrd.jwsDynamicRestUrl FROM JwsEntityRoleAssociation jera "
						+ "INNER JOIN JwsDynamicRestDetail jdrd ON jera.entityId = jdrd.jwsDynamicRestId "
						+ "INNER JOIN JwsRole jr ON jera.roleId = jr.roleId " + "WHERE jera.isActive = :isActive ",
				String.class);

		restUrlQuery.setParameter("isActive", Constants.ISACTIVE);
		List<String> dynamicUrls = restUrlQuery.getResultList();

		for (String dynamicUrl : dynamicUrls) {
			String regex = convertToRegex(dynamicUrl);
			if (requestUri.matches(regex)) {
				return dynamicUrl;
			}
		}
		return null;
	}

	private String convertToRegex(String dynamicUrl) {
		String regex = dynamicUrl.replace(".", "\\\\.");
		if (regex.endsWith("/**")) {
			regex = regex.substring(0, regex.length() - 3) + "(?:/.*)?";
		} else {
			String tempDoubleStarPlaceholder = "__DOUBLE_STAR_PLACEHOLDER__";
			regex	= regex.replace("**", tempDoubleStarPlaceholder);
			regex	= regex.replace("*", "[^/]+");
			regex	= regex.replace(tempDoubleStarPlaceholder, "(?:/.*)?");
		}
		return "^" + regex + "$";
	}

}
