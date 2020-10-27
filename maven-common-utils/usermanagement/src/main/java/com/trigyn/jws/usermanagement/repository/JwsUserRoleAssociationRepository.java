package com.trigyn.jws.usermanagement.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.usermanagement.entities.JwsUserRoleAssociation;
import com.trigyn.jws.usermanagement.vo.JwsRoleModuleAssociationVO;
import com.trigyn.jws.usermanagement.vo.JwsRoleVO;

@Repository
public interface JwsUserRoleAssociationRepository extends JpaRepository<JwsUserRoleAssociation, String> {

	List<JwsUserRoleAssociation> findByUserId(String userId);
	
	@Query(" SELECT new com.trigyn.jws.usermanagement.vo.JwsRoleModuleAssociationVO (jr.roleId,jr.roleName,jm.moduleId,jm.moduleName )   FROM JwsUserRoleAssociation AS jura INNER JOIN jura.jwsRole AS jr INNER JOIN jr.jwsRoleMasterModulesAssociation AS jrmma  INNER JOIN  jrmma.module as jm WHERE jrmma.isActive =:isActive AND jura.userId =:userId ORDER BY jr.roleName ") 
	List<JwsRoleModuleAssociationVO> getRoleModuleAssociation(Integer isActive,String userId);

	
	@Query(" SELECT new com.trigyn.jws.usermanagement.vo.JwsRoleVO (jr.roleId,jr.roleName )   FROM JwsUserRoleAssociation AS jura INNER JOIN jura.jwsRole AS jr WHERE jr.isActive =:isActive AND jura.userId =:userId ORDER BY jr.roleName ") 
	List<JwsRoleVO> getUserRoles(Integer isActive,String userId);
}
