package com.trigyn.jws.dbutils.entities;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dbutils.utils.QueryStore;;

@Repository
public interface DatasourceLookUpRepository extends JpaRepositoryImplementation<DatasourceLookUp, String> {

	@Query(QueryStore.JPA_QUERY_TO_GET_DRIVER_CLASS_NAME_BY_ID)
	String getDriverClassNameById(String datasourceLookupId);

	@Query(QueryStore.JPA_QUERY_TO_GET_DATASOURCE_PRODUCT_NAME_BY_ID)
	String getDataSourceProductNameById(String additionalDatasourceId);

}
