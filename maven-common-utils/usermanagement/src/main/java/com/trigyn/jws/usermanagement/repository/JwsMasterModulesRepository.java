package com.trigyn.jws.usermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.usermanagement.entities.JwsMasterModules;

@Repository
public interface JwsMasterModulesRepository extends JpaRepository<JwsMasterModules, String> {

}
