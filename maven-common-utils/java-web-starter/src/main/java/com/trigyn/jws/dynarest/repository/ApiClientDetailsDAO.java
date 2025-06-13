package com.trigyn.jws.dynarest.repository;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.dbutils.repository.DBConnection;
import com.trigyn.jws.dynarest.entities.JqApiClientDetails;

@Repository
public class ApiClientDetailsDAO extends DBConnection {

	public ApiClientDetailsDAO(DataSource dataSource) {
		super(dataSource);
	}

	private final static Logger				logger							= LoggerFactory
			.getLogger(ApiClientDetailsDAO.class);

	public JqApiClientDetails findApiClientDetailsById(String clientId) {
		JqApiClientDetails additionalDatasource =  getCurrentSession().get(JqApiClientDetails.class, clientId);
		if(additionalDatasource != null) getCurrentSession().evict(additionalDatasource);
		return additionalDatasource;
	
	}

	@Transactional(readOnly = false)
	public void saveAdditionalDatasource(JqApiClientDetails apiClientDetails) {
		if(apiClientDetails.getClientId() == null || findApiClientDetailsById(apiClientDetails.getClientId()) == null) {
			getCurrentSession().persist(apiClientDetails);			
		}else {
			getCurrentSession().merge(apiClientDetails);
		}
	}

}
