package com.trigyn.jws.usermanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.usermanagement.entities.JwsEntityRoleAssociation;
import com.trigyn.jws.usermanagement.vo.JwsRoleVO;

@Repository
public interface JwsEntityRoleAssociationRepository extends JpaRepository<JwsEntityRoleAssociation, String> {

	@Query(" SELECT entityRoleId FROM JwsEntityRoleAssociation WHERE entityId=:entityId AND roleId=:roleId ")
	String getEntityRoleIdByEntityAndRoleId(String entityId, String roleId);

	@Query(" FROM JwsEntityRoleAssociation WHERE entityId=:entityId AND moduleId=:moduleId ")
	List<JwsEntityRoleAssociation> getEntityRoles(String entityId, String moduleId);

	@Query(" SELECT new com.trigyn.jws.usermanagement.vo.JwsRoleVO( r.roleId,r.roleName) FROM JwsEntityRoleAssociation AS jera INNER JOIN jera.role  AS r WHERE jera.entityId=:entityId AND jera.moduleId=:moduleId  AND jera.isActive=:isActive")
	List<JwsRoleVO> getRoles(String entityId, String moduleId, Integer isActive);

	@Modifying
	@Query(" UPDATE JwsEntityRoleAssociation SET isActive=:isActive WHERE moduleTypeId =:moduleTypeId  AND roleId=:roleId")
	void updateEntityRelatedToModule(Integer moduleTypeId, String roleId, Integer isActive);

	@Query(" FROM JwsEntityRoleAssociation WHERE moduleTypeId=:commonModuleTypeId  GROUP BY entityId ")
	List<JwsEntityRoleAssociation> findEntityByModuleTypeId(Integer commonModuleTypeId);

	@Query(" FROM JwsEntityRoleAssociation WHERE entityRoleId=:entityRoleID")
	JwsEntityRoleAssociation getJwsEntityRoleAssociation(String entityRoleID);

}
