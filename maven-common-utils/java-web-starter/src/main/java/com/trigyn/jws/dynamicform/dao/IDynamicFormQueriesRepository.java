
package com.trigyn.jws.dynamicform.dao;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dynamicform.entities.DynamicFormSaveQuery;

@Repository
public interface IDynamicFormQueriesRepository extends JpaRepositoryImplementation<DynamicFormSaveQuery, String> {

}
