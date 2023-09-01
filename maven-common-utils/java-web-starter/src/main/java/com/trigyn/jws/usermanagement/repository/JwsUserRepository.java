package com.trigyn.jws.usermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.usermanagement.entities.JwsUser;

@Repository
@Transactional
public interface JwsUserRepository extends JpaRepository<JwsUser, String> {

	JwsUser findByEmailIgnoreCase(String email);

	JwsUser findByUserId(String userId);

}
