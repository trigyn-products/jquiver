package com.trigyn.jws.dynarest.dao;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dynarest.entities.JwsDynamicRestDaoDetail;
import com.trigyn.jws.dynarest.entities.JwsDynamicRestDetail;

import com.trigyn.jws.dbutils.repository.DBConnection;

@Repository
public class JwsDynarestDAO extends DBConnection {

    public JwsDynarestDAO(DataSource dataSource) {
        super(dataSource);
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

	
	public void saveJwsDynamicRestDAO(JwsDynamicRestDaoDetail daoDetail) {
		getCurrentSession().save(daoDetail);
		
	}
}
