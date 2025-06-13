package com.trigyn.jws.usermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.usermanagement.entities.JwsConfirmationToken;

@Repository
public interface JwsConfirmationTokenRepository extends JpaRepository<JwsConfirmationToken, String> {
	
	JwsConfirmationToken findByConfirmationToken(@Param("confirmationToken") String confirmationToken);
}
