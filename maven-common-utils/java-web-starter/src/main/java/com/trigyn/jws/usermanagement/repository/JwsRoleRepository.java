package com.trigyn.jws.usermanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.usermanagement.entities.JwsRole;

@Repository
public interface JwsRoleRepository extends JpaRepository<JwsRole, String> {

	@Query("FROM JwsRole jr WHERE jr.isActive=1 ORDER BY jr.roleName  ")
	List<JwsRole> findAllRoles();

	JwsRole findByRoleName(String anonymousRoleName);

	@Query("SELECT jr.roleId AS roleId FROM JwsRole jr WHERE jr.roleName IN (:roleNameList) AND jr.isActive=1 ORDER BY jr.rolePriority DESC ")
	List<String> getRoleIdByPriority(List<String> roleNameList);

}
