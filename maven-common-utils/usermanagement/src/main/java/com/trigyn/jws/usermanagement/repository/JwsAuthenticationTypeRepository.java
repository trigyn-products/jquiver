package com.trigyn.jws.usermanagement.repository;

import java.util.List;

import com.trigyn.jws.usermanagement.entities.JwsAuthenticationType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface JwsAuthenticationTypeRepository extends JpaRepository<JwsAuthenticationType, Integer> {

	@Modifying
	@Transactional
	@Query("UPDATE JwsAuthenticationType SET authenticationProperties=:propertyJson WHERE authenticationId=:authenticationId")
	void updatePropertyById(Integer authenticationId, String propertyJson);
	
	@Query("FROM JwsAuthenticationType WHERE authenticationProperties IS NOT NULL")
	List<JwsAuthenticationType> getAuthenticationTypes();
}
