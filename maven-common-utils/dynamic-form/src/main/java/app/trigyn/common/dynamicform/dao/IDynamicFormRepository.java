package app.trigyn.common.dynamicform.dao;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import app.trigyn.common.dynamicform.entities.DynamicForm;

@Repository
public interface IDynamicFormRepository extends JpaRepositoryImplementation<DynamicForm, String>{

}
