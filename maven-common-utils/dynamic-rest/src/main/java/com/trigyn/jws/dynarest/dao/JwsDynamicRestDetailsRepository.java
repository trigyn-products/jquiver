package com.trigyn.jws.dynarest.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dynarest.entities.JwsDynamicRestDetail;
import com.trigyn.jws.dynarest.vo.RestApiDetails;

@Repository
public interface JwsDynamicRestDetailsRepository extends JpaRepositoryImplementation<JwsDynamicRestDetail, Integer> {

    @Query(QueryStore.QUERY_TO_API_DETAILS_BY_URL)
    RestApiDetails findByJwsDynamicRestUrl(String jwsDynamicRestUrl);
    
}
