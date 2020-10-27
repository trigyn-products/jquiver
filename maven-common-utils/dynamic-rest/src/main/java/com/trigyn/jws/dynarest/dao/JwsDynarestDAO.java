package com.trigyn.jws.dynarest.dao;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.trigyn.jws.dbutils.repository.DBConnection;
import com.trigyn.jws.dynarest.entities.JwsDynamicRestDaoDetail;
import com.trigyn.jws.dynarest.entities.JwsDynamicRestDetail;

@Repository
public class JwsDynarestDAO extends DBConnection {

    public JwsDynarestDAO(DataSource dataSource) {
        super(dataSource);
    }

	public JwsDynamicRestDetail findDynamicRestById(Integer dynarestDetailsId){
		return hibernateTemplate.get(JwsDynamicRestDetail.class, dynarestDetailsId);
	}
	
    public List<Map<String, Object>> executeQueries(String query, Map<String, Object> parameterMap) {
        return namedParameterJdbcTemplate.queryForList(query, parameterMap);
    }

	public List<JwsDynamicRestDetail> getAllDynamicRestDetails() {
		List<JwsDynamicRestDetail> dynamicRestDetails = (List<JwsDynamicRestDetail>) getCurrentSession().createNamedQuery("JwsDynamicRestDetail.findAll", JwsDynamicRestDetail.class).getResultList();
		return dynamicRestDetails;
	}

	public void saveJwsDynamicRestDetail(JwsDynamicRestDetail currentDynamicRestDetail) {
			getCurrentSession().saveOrUpdate(currentDynamicRestDetail);
	}
	
	public JwsDynamicRestDetail getDynamicRestDetailsByName(String jwsMethodName) {
		 Query query = getCurrentSession().createQuery(" FROM JwsDynamicRestDetail  WHERE lower(jwsMethodName) = lower(:jwsMethodName)");
	     query.setParameter("jwsMethodName", jwsMethodName);
	     JwsDynamicRestDetail data = (JwsDynamicRestDetail) query.uniqueResult();
	     return data;
	}
    
	public void deleteDAOQueries(Integer jwsDynamicRestDetailId) {
		Query query = getCurrentSession().createQuery("DELETE FROM JwsDynamicRestDaoDetail  WHERE jwsDynamicRestDetailId = :jwsDynamicRestDetailId");
		query.setParameter("jwsDynamicRestDetailId", jwsDynamicRestDetailId);
		query.executeUpdate();
		 getCurrentSession().flush();
	}

	public void deleteDAOQueriesById(String jwsDynamicRestDetailId, List<Integer> daoDetailsIdList) {
		StringBuilder deleteDaoQuery = new StringBuilder("DELETE FROM JwsDynamicRestDaoDetail AS jdrdd WHERE jdrdd.jwsDynamicRestDetailId = :jwsDynamicRestDetailId ");
		
		if(!CollectionUtils.isEmpty(daoDetailsIdList)) {
			deleteDaoQuery.append(" AND jdrdd.jwsDaoDetailsId NOT IN(:daoDetailsIdList) ");
		}
		
		Query query = getCurrentSession().createQuery(deleteDaoQuery.toString());
		query.setParameter("jwsDynamicRestDetailId", jwsDynamicRestDetailId);
		if(!CollectionUtils.isEmpty(daoDetailsIdList)) {
			query.setParameterList("daoDetailsIdList", daoDetailsIdList);
		}
		query.executeUpdate();
	}
	
	public void saveJwsDynamicRestDAO(JwsDynamicRestDaoDetail daoDetail) {
		getCurrentSession().save(daoDetail);
		
	}

	public int executeDMLQueries(String query, Map<String, Object> parameterMap) {
		return namedParameterJdbcTemplate.update(query, parameterMap);
	}
}
