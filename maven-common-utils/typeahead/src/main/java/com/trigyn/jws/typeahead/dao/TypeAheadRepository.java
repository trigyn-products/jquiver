package com.trigyn.jws.typeahead.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.typeahead.entities.Autocomplete;

@Repository
public interface TypeAheadRepository extends JpaRepositoryImplementation<Autocomplete, String> {

	@Query("SELECT ac.datasourceId FROM Autocomplete AS ac WHERE autocompleteId = :autocompleteId ")
	String getDataSourceId(String autocompleteId);
}