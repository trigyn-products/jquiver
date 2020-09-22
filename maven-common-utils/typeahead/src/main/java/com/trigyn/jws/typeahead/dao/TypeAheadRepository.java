package com.trigyn.jws.typeahead.dao;

import com.trigyn.jws.typeahead.entities.Autocomplete;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeAheadRepository extends JpaRepositoryImplementation<Autocomplete, String>{
    
}