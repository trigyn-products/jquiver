package com.trigyn.jws.dbutils.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dbutils.entities.JwsLookup;
import com.trigyn.jws.dbutils.vo.LookupDetailsVO;

public interface IJwsLookUpRepository extends JpaRepositoryImplementation<JwsLookup, String> {

	@Query(QueryStore.JPA_QUERY_TO_GET_LOOKUP_DETAILS)
	List<LookupDetailsVO> getLookUpDetailsByName(@Param("lookupName") String lookupName, @Param("languageId") Integer languageId, 
			@Param("defaultLanguageId") Integer defaultLanguageId, @Param("isDeleted") Integer isDeleted);
}
