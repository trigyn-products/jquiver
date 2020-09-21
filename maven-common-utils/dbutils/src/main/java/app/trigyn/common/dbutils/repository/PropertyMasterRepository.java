package app.trigyn.common.dbutils.repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import app.trigyn.common.dbutils.entities.PropertyMaster;
import app.trigyn.common.dbutils.entities.PropertyMasterPK;

@Repository
public interface PropertyMasterRepository extends JpaRepositoryImplementation<PropertyMaster, PropertyMasterPK>{

}
