package com.trigyn.jws.formio.dao;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.formio.entities.FormIO;

@Repository
public interface IFormIORepository extends JpaRepositoryImplementation<FormIO, String>{
	
}
