package com.trigyn.jws.dbutils.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dbutils.entities.PropertyMaster;

@Repository
public interface PropertyMasterRepository extends JpaRepositoryImplementation<PropertyMaster, String> {

	@Modifying
	@Query(" UPDATE PropertyMaster SET propertyValue=:propertyValue, is_custom_updated = 1 WHERE propertyName =:propertyName ")
	void updatePropertyValueByName(String propertyValue, String propertyName);

	@Query()
	PropertyMaster findByOwnerTypeAndOwnerIdAndPropertyName(String ownerType, String ownerId, String propertyName);

}
