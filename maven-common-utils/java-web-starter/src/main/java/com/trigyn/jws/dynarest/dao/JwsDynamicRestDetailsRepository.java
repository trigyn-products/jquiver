package com.trigyn.jws.dynarest.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dynarest.entities.JwsDynamicRestDetail;
import com.trigyn.jws.dynarest.vo.RestApiDetails;

@Repository
@Transactional
public interface JwsDynamicRestDetailsRepository extends JpaRepositoryImplementation<JwsDynamicRestDetail, Integer> {

	@Query(QueryStore.QUERY_TO_API_DETAILS_BY_URL)
	RestApiDetails findByJwsDynamicRestUrl(String jwsDynamicRestUrl);

	@Query(QueryStore.QUERY_TO_GET_DYNAMIC_REST_ID)
	String findByJwsDynamicRestId(String jwsDynamicRestUrl, String jwsMethodName);

	@Query(QueryStore.QUERY_TO_GET_JAVA_DYNAMIC_REST_DETAILS)
	List<RestApiDetails> findAllJavaDynarests();
	
	@Query(QueryStore.QUERY_TO_API_DETAILS_BY_ID)
	RestApiDetails findAllJavaDynarestsByRestId(String jwsDynamicRestId);

}
