package com.trigyn.jws.dynarest.repository;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.dbutils.repository.DBConnection;
import com.trigyn.jws.dynarest.entities.ApiClientDetails;

@Repository
public class ApiClientDetailsDAO extends DBConnection {

	private final static Logger				logger							= LogManager
			.getLogger(ApiClientDetailsDAO.class);

	@Autowired
	public ApiClientDetailsDAO(DataSource dataSource) {
		super(dataSource);
	}

	public ApiClientDetails findApiClientDetailsById(String clientId) {
		ApiClientDetails additionalDatasource =  hibernateTemplate.get(ApiClientDetails.class, clientId);
		if(additionalDatasource != null) getCurrentSession().evict(additionalDatasource);
		return additionalDatasource;
	
	}

	@Transactional(readOnly = false)
	public void saveAdditionalDatasource(ApiClientDetails apiClientDetails) {
		if(apiClientDetails.getClientId() == null || findApiClientDetailsById(apiClientDetails.getClientId()) == null) {
			getCurrentSession().save(apiClientDetails);			
		}else {
			getCurrentSession().saveOrUpdate(apiClientDetails);
		}
	}

}
