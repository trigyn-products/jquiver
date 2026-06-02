package com.trigyn.jws.usermanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.usermanagement.entities.JwsUser;

@Repository
@Transactional
public interface JwsUserRepository extends JpaRepository<JwsUser, String> {

	JwsUser findByEmailIgnoreCase(@Param("email") String email);

	JwsUser findByUserId(@Param("userId") String userId);
	
	@Query("SELECT ju.email FROM JwsUser ju WHERE ju.email=:email and ju.isActive=1")
	String findbyEmailAndIsActive(@Param("email") String email);
	

}
