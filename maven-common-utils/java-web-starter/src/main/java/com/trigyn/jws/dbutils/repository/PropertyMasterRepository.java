package com.trigyn.jws.dbutils.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dbutils.entities.PropertyMaster;

public interface PropertyMasterRepository extends JpaRepositoryImplementation<PropertyMaster, String> {

	@Modifying
	@Query(" UPDATE PropertyMaster SET propertyValue=:propertyValue, isCustomUpdated = 1 WHERE propertyName =:propertyName ")
	void updatePropertyValueByName(@Param("propertyValue") String propertyValue, @Param("propertyName") String propertyName);

	@Query()
	PropertyMaster findByOwnerTypeAndOwnerIdAndPropertyName(@Param("ownerType") String ownerType, @Param("ownerId") String ownerId, 
			@Param("propertyName") String propertyName);

}
