package app.trigyn.core.dynarest.dao;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import app.trigyn.common.dbutils.repository.DBConnection;
import app.trigyn.core.dynarest.entities.JwsDynamicRestDetail;

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
	}
}
