package com.trigyn.jws.dbutils.repository;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.dbutils.entities.AdditionalDatasource;

@Repository
public class AdditionalDatasourceDAO extends DBConnection {

	private final static Logger				logger							= LogManager
			.getLogger(AdditionalDatasourceDAO.class);

	@Autowired
	public AdditionalDatasourceDAO(DataSource dataSource) {
		super(dataSource);
	}

	public AdditionalDatasource findAdditionalDatasourceById(String additionalDatasourceId) {
		AdditionalDatasource additionalDatasource =  hibernateTemplate.get(AdditionalDatasource.class, additionalDatasourceId);
		if(additionalDatasource != null) getCurrentSession().evict(additionalDatasource);
		return additionalDatasource;
	
	}

	@Transactional(readOnly = false)
	public void saveAdditionalDatasource(AdditionalDatasource additionalDatasource) {
		if(additionalDatasource.getAdditionalDatasourceId() == null || findAdditionalDatasourceById(additionalDatasource.getAdditionalDatasourceId()) == null) {
			getCurrentSession().save(additionalDatasource);			
		}else {
			getCurrentSession().saveOrUpdate(additionalDatasource);
		}
	}

}
