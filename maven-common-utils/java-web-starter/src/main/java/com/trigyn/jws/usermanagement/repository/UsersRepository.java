package com.trigyn.jws.usermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.usermanagement.entities.JwsUser;

@Repository
@Transactional
public interface UsersRepository extends JpaRepository<JwsUser, String> {

	JwsUser findByEmailIgnoreCase(@Param("email") String email);

	JwsUser findByUserId(@Param("userId") String userId);

}
