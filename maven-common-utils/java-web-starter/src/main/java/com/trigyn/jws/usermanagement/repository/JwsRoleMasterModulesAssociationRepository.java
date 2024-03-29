package com.trigyn.jws.usermanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.usermanagement.entities.JwsRoleMasterModulesAssociation;

@Repository
public interface JwsRoleMasterModulesAssociationRepository extends JpaRepository<JwsRoleMasterModulesAssociation, String> {

	@Query("SELECT COUNT(*) FROM JwsRoleMasterModulesAssociation AS jrmma INNER JOIN jrmma.role AS jr  INNER JOIN  jrmma.module AS jm  WHERE jr.roleName IN (:roleNames) AND jm.moduleName=:moduleName AND jr.isActive=:isActive AND jrmma.isActive=:isActive ")
	Long checkModulePresentForCurrentRole(List<String> roleNames, String moduleName, Integer isActive);
	
	@Modifying
	@Query(" UPDATE JwsRoleMasterModulesAssociation SET isActive=:isActive WHERE moduleId =:moduleTypeId  AND roleId=:roleId")
	void updateJwsRoleMasterModulesAssociation(String moduleTypeId, String roleId, Integer isActive);
	
	/**
	 * Toggles the active state for master module entities when authentication is enabled
	 * wrt anonymous access
	 * @author Anomitro.Mukherjee
	 * 
	 * @param isActive the isActive to set
	 * @param moduleIds the moduleIds list
	 * 
	 */
	@Modifying
	@Query("UPDATE JwsRoleMasterModulesAssociation" + 
			" SET isActive = :isActive" + 
			" WHERE roleId = 'b4a0dda1-097f-11eb-9a16-f48e38ab9348'" + 
			" AND moduleId IN :moduleIds")
	void toggleAnonymousUserAccessInMasterModule(Integer isActive, List<String> moduleIds);

}
