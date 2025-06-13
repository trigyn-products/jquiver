package com.trigyn.jws.dbutils.entities;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dbutils.utils.QueryStore;;

public interface DatasourceLookUpRepository extends JpaRepositoryImplementation<DatasourceLookUp, String> {

	@Query(QueryStore.JPA_QUERY_TO_GET_DRIVER_CLASS_NAME_BY_ID)
	String getDriverClassNameById(@Param("datasourceLookupId") String datasourceLookupId);

	@Query(QueryStore.JPA_QUERY_TO_GET_DATASOURCE_PRODUCT_NAME_BY_ID)
	String getDataSourceProductNameById(@Param("additionalDatasourceId") String additionalDatasourceId);

}
