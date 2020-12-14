package com.trigyn.jws.security.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.security.entities.SecurityType;

@Repository
public interface SecurityTypeRepository extends JpaRepository<SecurityType, String> {

	SecurityType findBySecurityName(String securityName);
}
