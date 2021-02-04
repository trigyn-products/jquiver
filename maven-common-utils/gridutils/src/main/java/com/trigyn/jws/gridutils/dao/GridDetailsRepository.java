package com.trigyn.jws.gridutils.dao;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.gridutils.entities.GridDetails;

@Repository
public interface GridDetailsRepository extends JpaRepositoryImplementation<GridDetails, String> {

}
