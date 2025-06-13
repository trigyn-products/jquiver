
package com.trigyn.jws.dynamicform.dao;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.trigyn.jws.dynamicform.entities.DynamicFormSaveQuery;

public interface IDynamicFormQueriesRepository extends JpaRepositoryImplementation<DynamicFormSaveQuery, String> {

}
