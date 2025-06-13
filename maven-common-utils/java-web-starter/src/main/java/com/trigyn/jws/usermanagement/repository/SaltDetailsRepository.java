package com.trigyn.jws.usermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.usermanagement.entities.JwsUser;
import com.trigyn.jws.usermanagement.security.config.SaltDetails;

@Repository
@Transactional
public interface SaltDetailsRepository extends JpaRepository<SaltDetails, String> {

	SaltDetails findByRequestId(String requestId);
	
}
