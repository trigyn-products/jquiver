package com.trigyn.jws.dbutils.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dbutils.entities.ModuleRoleAssociation;

@Repository
public interface IModuleRoleAssociationRepository extends JpaRepositoryImplementation<ModuleRoleAssociation, String> {

	@Query(QueryStore.JPA_QUERY_TO_MODULE_ROLE_BY_ID)
	String findModuleIdByRoleId(String roleId, String moduleId);

}
