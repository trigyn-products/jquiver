package com.trigyn.jws.dbutils.entities;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.datasource.lookup.DataSourceLookup;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dbutils.utils.QueryStore;
import com.trigyn.jws.sciptlibrary.entities.ScriptLibraryConnection;;

public interface DatasourceLookUpRepository extends JpaRepositoryImplementation<DatasourceLookUp, String> {

	@Query(QueryStore.JPA_QUERY_TO_GET_DRIVER_CLASS_NAME_BY_ID)
	String getDriverClassNameById(@Param("datasourceLookupId") String datasourceLookupId);

	@Query(QueryStore.JPA_QUERY_TO_GET_DATASOURCE_PRODUCT_NAME_BY_ID)
	String getDataSourceProductNameById(@Param("additionalDatasourceId") String additionalDatasourceId);

//	@Query(" FROM DatasourceLookUp WHERE entityId=:entityId AND moduletypeId=:moduleId GROUP BY datasourceLookupId")
//	List<DatasourceLookUp> getAdditionalDataSourceIds(@Param("entityId") String entityId, @Param("moduleId") String moduleId);
}
