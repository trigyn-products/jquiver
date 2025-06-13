package com.trigyn.jws.dynamicform.dao;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.trigyn.jws.dynamicform.entities.DynamicForm;

public interface IDynamicFormRepository extends JpaRepositoryImplementation<DynamicForm, String> {

}
