package com.trigyn.jws.dbutils.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dbutils.entities.PropertyMaster;
import com.trigyn.jws.dbutils.entities.PropertyMasterPK;

@Repository
public interface PropertyMasterRepository extends JpaRepositoryImplementation<PropertyMaster, PropertyMasterPK>{
	
	@Modifying
	@Query(" UPDATE PropertyMaster SET propertyValue=:propertyValue WHERE id.propertyName =:propertyName ")
	void updatePropertyValueByName(String propertyValue,String propertyName);
}
