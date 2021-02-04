package com.trigyn.jws.usermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.usermanagement.entities.JwsAuthenticationType;

@Repository
public interface JwsAuthenticationTypeRepository extends JpaRepository<JwsAuthenticationType, Integer> {

	@Modifying
	@Transactional
	@Query("UPDATE JwsAuthenticationType SET authenticationProperties=:propertyJson WHERE authenticationId=:authenticationId")
	void updatePropertyById(Integer authenticationId, String propertyJson);
}
