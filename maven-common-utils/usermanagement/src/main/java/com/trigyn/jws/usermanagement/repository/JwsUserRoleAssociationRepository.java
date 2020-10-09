package com.trigyn.jws.usermanagement.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.usermanagement.entities.JwsUserRoleAssociation;

@Repository
public interface JwsUserRoleAssociationRepository extends JpaRepository<JwsUserRoleAssociation, String> {

	List<JwsUserRoleAssociation> findByUserId(String userId);
	
	@Query("FROM JwsUserRoleAssociation AS jura INNER JOIN jura.jwsRole AS jr INNER JOIN jr.jwsRoleMasterModulesAssociation AS jrmma  INNER JOIN  jrmma.module as jm WHERE jrmma.isActive =:isActive AND jura.userId =:userId ") 
	List<JwsUserRoleAssociation> getRoleModuleAssociation(Integer isActive,String userId);

	
}
