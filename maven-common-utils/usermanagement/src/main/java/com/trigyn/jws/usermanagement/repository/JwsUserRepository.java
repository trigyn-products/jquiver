package com.trigyn.jws.usermanagement.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.usermanagement.entities.JwsUser;

@Repository
public interface JwsUserRepository extends JpaRepository<JwsUser, String> {

	JwsUser findByEmailIgnoreCase(String email);
	
	JwsUser findByUserId(String userId);
}
