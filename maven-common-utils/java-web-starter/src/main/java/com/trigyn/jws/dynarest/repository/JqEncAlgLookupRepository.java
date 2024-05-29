package com.trigyn.jws.dynarest.repository;

import com.trigyn.jws.dynarest.entities.JqEncAlgModPadKeyLookup;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

@Repository
public interface JqEncAlgLookupRepository extends JpaRepositoryImplementation<JqEncAlgModPadKeyLookup, Integer> {

}
