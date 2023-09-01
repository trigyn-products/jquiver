package com.trigyn.jws.dbutils.entities;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dbutils.vo.DataSourceVO;

@Repository
public interface AdditionalDatasourceRepository extends JpaRepositoryImplementation<AdditionalDatasource, String> {

	@Query("SELECT new com.trigyn.jws.dbutils.vo.DataSourceVO(ad.additionalDatasourceId AS additionalDataSourceId, dl.driverClassName AS driverClassName, ad.datasourceConfiguration AS dataSourceConfiguration)"
			+ " FROM AdditionalDatasource AS ad INNER JOIN ad.datasourceLookup AS dl WHERE ad.additionalDatasourceId = :additionalDatasourceId")
	DataSourceVO getDataSourceConfiguration(String additionalDatasourceId);
	

}
