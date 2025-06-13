package com.trigyn.jws.dynarest.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dynarest.entities.JwsDynamicRestDetail;
import com.trigyn.jws.dynarest.vo.RestApiDetails;

import jakarta.transaction.Transactional;

@Transactional
public interface JwsDynamicRestDetailsRepository extends JpaRepositoryImplementation<JwsDynamicRestDetail, String> {

	@Query(QueryStore.QUERY_TO_API_DETAILS_BY_URL)
	RestApiDetails findByJwsDynamicRestUrl(@Param("jwsDynamicRestUrl") String jwsDynamicRestUrl);

	@Query(QueryStore.QUERY_TO_GET_DYNAMIC_REST_ID)
	String findByJwsDynamicRestId(@Param("jwsDynamicRestUrl") String jwsDynamicRestUrl, @Param("jwsMethodName") String jwsMethodName);

	@Query(QueryStore.QUERY_TO_GET_JAVA_DYNAMIC_REST_DETAILS)
	List<RestApiDetails> findAllJavaDynarests();
	
	@Query(QueryStore.QUERY_TO_API_DETAILS_BY_ID)
	RestApiDetails findAllJavaDynarestsByRestId(@Param("jwsDynamicRestId") String jwsDynamicRestId);

}
