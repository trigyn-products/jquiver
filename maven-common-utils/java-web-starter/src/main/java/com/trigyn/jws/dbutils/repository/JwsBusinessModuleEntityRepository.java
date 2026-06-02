package com.trigyn.jws.dbutils.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.trigyn.jws.dbutils.entities.JwsBusinessModuleEntity;

public interface JwsBusinessModuleEntityRepository extends JpaRepository<JwsBusinessModuleEntity, String> {
	
	@Query(" FROM JwsBusinessModuleEntity WHERE entityId=:entityId AND moduleId=:moduleId GROUP BY businessModuleId")
	List<JwsBusinessModuleEntity> getBusinessModules(@Param("entityId") String entityId, @Param("moduleId") String moduleId);
}
