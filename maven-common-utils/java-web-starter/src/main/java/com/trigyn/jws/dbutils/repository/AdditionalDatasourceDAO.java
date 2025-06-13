package com.trigyn.jws.dbutils.repository;

import java.util.Objects;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.dbutils.entities.AdditionalDatasource;

@Repository
public class AdditionalDatasourceDAO extends DBConnection {

	public AdditionalDatasourceDAO(DataSource dataSource) {
		super(dataSource);
	}

	private final static Logger				logger							= LoggerFactory
			.getLogger(AdditionalDatasourceDAO.class);

	public AdditionalDatasource findAdditionalDatasourceById(String additionalDatasourceId) {
		AdditionalDatasource additionalDatasource =  getCurrentSession().get(AdditionalDatasource.class, additionalDatasourceId);
		if(additionalDatasource != null) getCurrentSession().evict(additionalDatasource);
		return additionalDatasource;
	
	}

	@Transactional(readOnly = false)
	public void saveAdditionalDatasource(AdditionalDatasource additionalDatasource) {
		if (additionalDatasource.getAdditionalDatasourceId() == null
				|| Objects.isNull(findAdditionalDatasourceById(additionalDatasource.getAdditionalDatasourceId()))) {
			getCurrentSession().persist(additionalDatasource);
		} else {
			getCurrentSession().merge(additionalDatasource);
		}
	}

}
