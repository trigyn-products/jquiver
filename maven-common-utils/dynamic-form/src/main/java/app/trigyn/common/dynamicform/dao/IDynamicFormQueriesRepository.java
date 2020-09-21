/**
 * 
 */
package app.trigyn.common.dynamicform.dao;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import app.trigyn.common.dynamicform.entities.DynamicFormSaveQuery;

/**
 * @author Aman.Prasad
 *
 */
@Repository
public interface IDynamicFormQueriesRepository extends JpaRepositoryImplementation<DynamicFormSaveQuery, String>{

}
