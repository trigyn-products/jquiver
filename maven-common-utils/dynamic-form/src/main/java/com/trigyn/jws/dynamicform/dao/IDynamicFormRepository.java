package com.trigyn.jws.dynamicform.dao;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dynamicform.entities.DynamicForm;

@Repository
public interface IDynamicFormRepository extends JpaRepositoryImplementation<DynamicForm, String> {

}
