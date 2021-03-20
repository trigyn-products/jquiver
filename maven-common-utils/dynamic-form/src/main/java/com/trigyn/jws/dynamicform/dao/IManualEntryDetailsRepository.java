package com.trigyn.jws.dynamicform.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dynamicform.entities.ManualEntryDetails;

@Repository
public interface IManualEntryDetailsRepository extends JpaRepositoryImplementation<ManualEntryDetails, String> {

	@Query(QueryStore.JPA_QUERY_TO_GET_MANUAL_ENTRY_DETAILS_BY_MANUAL_TYPE)
	List<ManualEntryDetails> findAllByManualType(String manualType);

}
