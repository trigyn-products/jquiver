package com.trigyn.jws.dynarest.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.query.Query;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.trigyn.jws.dbutils.repository.DBConnection;
import com.trigyn.jws.dynamicform.utils.Constant;
import com.trigyn.jws.dynarest.entities.JqSchedulerLog;
import com.trigyn.jws.dynarest.entities.JwsDynamicRestDaoDetail;
import com.trigyn.jws.dynarest.entities.JwsDynamicRestDetail;
import com.trigyn.jws.dynarest.utils.Constants;

@Repository
public class JwsDynarestDAO extends DBConnection {

	public JwsDynarestDAO(DataSource dataSource) {
		super(dataSource);
	}

	@Transactional
	public JwsDynamicRestDetail findDynamicRestById(String dynarestDetailsId) {
		JwsDynamicRestDetail jwsDynamicRestDetail =  hibernateTemplate.get(JwsDynamicRestDetail.class, dynarestDetailsId);
		if(jwsDynamicRestDetail != null) getCurrentSession().evict(jwsDynamicRestDetail);
		return jwsDynamicRestDetail;
	}
	
	@Transactional
	public JwsDynamicRestDetail findDynamicRestByUrl(String jwsDynamicRestUrl) {
		Query query = getCurrentSession().createQuery(" FROM JwsDynamicRestDetail  WHERE lower(jwsDynamicRestUrl) = lower(:jwsDynamicRestUrl)");
		query.setParameter("jwsDynamicRestUrl", jwsDynamicRestUrl);
		JwsDynamicRestDetail data = (JwsDynamicRestDetail) query.uniqueResult();
		if(data != null) getCurrentSession().evict(data);
		return data;
	}

	public List<Map<String, Object>> executeQueries(String dataSourceId, String query, Map<String, Object> parameterMap) {
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = updateNamedParameterJdbcTemplateDataSource(dataSourceId);
		return namedParameterJdbcTemplate.queryForList(query, parameterMap);
	}

	public List<JwsDynamicRestDetail> getAllDynamicRestDetails() {
		List<JwsDynamicRestDetail> dynamicRestDetails = (List<JwsDynamicRestDetail>) getCurrentSession()
				.createNamedQuery("JwsDynamicRestDetail.findAll", JwsDynamicRestDetail.class).getResultList();
		return dynamicRestDetails;
	}

	public void saveJwsDynamicRestDetail(JwsDynamicRestDetail currentDynamicRestDetail) {
		getCurrentSession().saveOrUpdate(currentDynamicRestDetail);
	}
	
	@Transactional(readOnly = false)
	public void saveDynaRestDetail(JwsDynamicRestDetail dynarest, List<JwsDynamicRestDaoDetail>	jwsDynamicRestDaoDetails) {
		JwsDynamicRestDetail data = getDynamicRestDetailsByName(dynarest.getJwsMethodName());
		if(dynarest.getJwsDynamicRestId() == null || (findDynamicRestById(dynarest.getJwsDynamicRestId()) == null
				&& data == null)) {
			getCurrentSession().save(dynarest);			
		}else {
			if(data != null) dynarest.setJwsDynamicRestId(data.getJwsDynamicRestId());
			getCurrentSession().saveOrUpdate(dynarest);
		}
		
		deleteDAOQueries(dynarest.getJwsDynamicRestId());
		for(JwsDynamicRestDaoDetail daoDetails : jwsDynamicRestDaoDetails) {
			daoDetails.setJwsDynamicRestDetailId(dynarest.getJwsDynamicRestId());
			saveJwsDynamicRestDAO(daoDetails);
		}
	}

	public JwsDynamicRestDetail getDynamicRestDetailsByName(String jwsMethodName) {
		Query query = getCurrentSession().createQuery(" FROM JwsDynamicRestDetail  WHERE lower(jwsMethodName) = lower(:jwsMethodName)");
		query.setParameter("jwsMethodName", jwsMethodName);
		JwsDynamicRestDetail data = (JwsDynamicRestDetail) query.uniqueResult();
		if(data != null) getCurrentSession().evict(data);
		return data;
	}

	public void deleteDAOQueries(String jwsDynamicRestDetailId) {
		Query query = getCurrentSession()
				.createQuery("DELETE FROM JwsDynamicRestDaoDetail  WHERE jwsDynamicRestDetailId = :jwsDynamicRestDetailId");
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

		Query query = getCurrentSession().createQuery(deleteDaoQuery.toString());
		query.setParameter("jwsDynamicRestDetailId", jwsDynamicRestDetailId);
		if (!CollectionUtils.isEmpty(daoDetailsIdList)) {
			query.setParameterList("daoDetailsIdList", daoDetailsIdList);
		}
		query.executeUpdate();
	}

	public void saveJwsDynamicRestDAO(JwsDynamicRestDaoDetail daoDetail) {
		getCurrentSession().save(daoDetail);

	}

	public int executeDMLQueries(String dataSourceId, String query, Map<String, Object> parameterMap) {
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = updateNamedParameterJdbcTemplateDataSource(dataSourceId);
		return namedParameterJdbcTemplate.update(query, parameterMap);
	}
	
	public String getRequestTypeById(Integer requestTypeId) {
		String querySQL = "SELECT jws_request_type FROM jq_request_type_details WHERE jws_request_type_details_id ="+requestTypeId+"";
		return String.valueOf(getCurrentSession().createSQLQuery(querySQL).uniqueResult());
	}

	@Transactional
	public void saveJqSchedulerLog(JqSchedulerLog jqSchedulerLog) {
		getCurrentSession().save(jqSchedulerLog);
	}
	
	public final List<Object> scriptLibExecution(String dynarestId) {
		Query scriptLibQuery = getCurrentSession().createSQLQuery(" SELECT jqsl.template_id "
				+ "	FROM jq_script_lib_connect jqs LEFT JOIN jq_dynamic_rest_details jqd "
				+ "	ON jqd.jws_dynamic_rest_id = jqs.entity_id "
				+ "	LEFT JOIN jq_script_lib_details jqsl "
				+ "	ON jqs.script_lib_id = jqsl.script_lib_id "
				+ "	WHERE jqs.module_type_id = :moduleId AND jqd.jws_dynamic_rest_id = :dynarestId");
		
		scriptLibQuery.setParameter("moduleId", Constants.DYNAMIC_REST_MOD_ID);
		scriptLibQuery.setParameter("dynarestId", dynarestId);
		
		List<Object[]> scriptLibList = scriptLibQuery.list();
		List<Object>		resultMap	= new ArrayList<>();
		
		for(int iCounter=0;iCounter<scriptLibList.size();iCounter++){
			Query roleQuery = getCurrentSession().createSQLQuery("SELECT COUNT(*) FROM `jq_entity_role_association` WHERE `role_id` = :roleId AND `is_active` = :isActive AND entity_id = :entityId ");
			roleQuery.setParameter("roleId",   Constant.ANONYMOUS_ROLE_ID);
			roleQuery.setParameter("isActive", Constant.IS_ACTIVE);
			roleQuery.setParameter("entityId", scriptLibList.get(iCounter));
			List<Object[]> roleCount = roleQuery.list();
			if(roleQuery.list().get(0).toString().equalsIgnoreCase("0")) {
				Query templateQuery = getCurrentSession().createSQLQuery("SELECT template FROM jq_template_master WHERE template_id = :templateId ");
				templateQuery.setParameter("templateId", scriptLibList.get(iCounter));
				List<Object[]> listTemplate = templateQuery.list();
				resultMap.add(listTemplate.get(0));
			}
		}
		 
		return resultMap;
	}
	
	public List<String> getscriptLibId(String entityId) {
		Query querySQL = getCurrentSession().createSQLQuery("SELECT script_lib_id FROM jq_script_lib_connect WHERE entity_id = :entityId");
		querySQL.setParameter("entityId", entityId);
		List<String> scriptLibIdList = querySQL.list();
		return scriptLibIdList;
	}

	public void scriptLibDeleteById(String entityid) {
		Query deleteScriptLibQuery = null;
		deleteScriptLibQuery = getCurrentSession().createSQLQuery("delete from jq_script_lib_connect where entity_id=:entityid");
		deleteScriptLibQuery.setParameter("entityid", entityid);
		int resultSetDelete = deleteScriptLibQuery.executeUpdate();
	}

}
