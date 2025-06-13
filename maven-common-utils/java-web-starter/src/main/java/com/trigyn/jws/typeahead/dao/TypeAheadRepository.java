package com.trigyn.jws.typeahead.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.query.Param;

import com.trigyn.jws.typeahead.entities.Autocomplete;

public interface TypeAheadRepository extends JpaRepositoryImplementation<Autocomplete, String> {

	@Query("SELECT ac.datasourceId FROM Autocomplete AS ac WHERE autocompleteId = :autocompleteId ")
	String getDataSourceId(@Param("autocompleteId") String autocompleteId);
}