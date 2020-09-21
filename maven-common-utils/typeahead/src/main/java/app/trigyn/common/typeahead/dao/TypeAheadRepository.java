package app.trigyn.common.typeahead.dao;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import app.trigyn.common.typeahead.entities.Autocomplete;

@Repository
public interface TypeAheadRepository extends JpaRepositoryImplementation<Autocomplete, String>{
    
}