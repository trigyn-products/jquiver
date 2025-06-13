package com.trigyn.jws.dynamicform.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.query.Param;

import com.trigyn.jws.dynamicform.entities.ManualEntryDetails;

import jakarta.persistence.QueryHint;
public interface IManualEntryDetailsRepository extends JpaRepositoryImplementation<ManualEntryDetails, String> {

	@Query(value=QueryStore.JPA_QUERY_TO_GET_MANUAL_ENTRY_DETAILS_BY_MANUAL_TYPE, nativeQuery = true)
	List<ManualEntryDetails> findAllByManualType(@Param("manualType") String manualType);
	
	@QueryHints({
	    @QueryHint(name = "org.hibernate.cacheable", value = "true"),
	    @QueryHint(name = "org.hibernate.cacheRegion", value = "manualEntryDetailsRegion")
	})
	@Query(QueryStore.JPA_QUERY_TO_GET_HELP_MANUAL_ENTRY_PARENT_NODES)
	List<ManualEntryDetails> fetchParentNodes(@Param("manualId") String manualId);

	@Query(QueryStore.JPA_QUERY_TO_GET_HELP_MANUAL_ENTRY_DETAILS_BY_Parent_AND_MANUAL_ID)
	List<ManualEntryDetails> fetchByParentAndManualId(@Param("parentId") String parentId, @Param("manualId") String manualId);
	
	@Query(QueryStore.JPA_QUERY_TO_GET_COUNT)
	Integer hasChild(@Param("nodeId") String nodeId);
	
	@Query(QueryStore.JPA_QUERY_TO_GET_CONTENT)
	String fetchContent(@Param("nodeId") String nodeId);
	
	@Query(QueryStore.JPA_QUERY_TO_GET_FILTERED_DATA)
	List<ManualEntryDetails> searchNode(@Param("searchText") String searchText, @Param("manualId") String manualId);

}
