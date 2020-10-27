package com.trigyn.jws.usermanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.usermanagement.entities.JwsRoleMasterModulesAssociation;


@Repository
public interface JwsRoleMasterModulesAssociationRepository extends JpaRepository<JwsRoleMasterModulesAssociation, String> {

	
	@Query("SELECT COUNT(*) FROM JwsRoleMasterModulesAssociation AS jrmma INNER JOIN jrmma.role AS jr  INNER JOIN  jrmma.module AS jm  WHERE jr.roleName IN (:roleNames) AND jm.moduleName=:moduleName AND jr.isActive=:isActive AND jrmma.isActive=:isActive ")
	Long checkModulePresentForCurrentRole(List<String> roleNames, String moduleName,Integer isActive);

}
