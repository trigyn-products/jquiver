package com.trigyn.jws.security.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.security.entities.SecurityProperties;

@Repository
public interface SecurityPropertiesRepository extends JpaRepository<SecurityProperties, String> {

	SecurityProperties findBySecurityPropertyName(String securityPropertyName);
}
