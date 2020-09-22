package com.trigyn.jws.dbutils.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dbutils.entities.UserRole;
import com.trigyn.jws.dbutils.utils.QueryStore;
import com.trigyn.jws.dbutils.vo.UserRoleVO;

@Repository
public interface UserRoleRepository extends JpaRepositoryImplementation<UserRole, String> {

	@Query(QueryStore.JPA_QUERY_TO_GET_ALL_ACTIVE_ROLES)
	List<UserRoleVO> getAllActiveRoles(Integer isDeleted);
}
