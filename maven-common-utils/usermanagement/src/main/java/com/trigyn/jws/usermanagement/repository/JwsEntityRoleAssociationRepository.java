package com.trigyn.jws.usermanagement.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.trigyn.jws.usermanagement.entities.JwsEntityRoleAssociation;

@Repository
public interface JwsEntityRoleAssociationRepository extends JpaRepository<JwsEntityRoleAssociation, String> {
	
	@Query(" SELECT entityRoleId FROM JwsEntityRoleAssociation WHERE entityId=:entityId AND roleId=:roleId ")
	String getEntityRoleIdByEntityAndRoleId(String entityId,String roleId);
}
