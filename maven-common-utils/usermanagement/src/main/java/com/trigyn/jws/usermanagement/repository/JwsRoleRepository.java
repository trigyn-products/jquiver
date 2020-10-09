package com.trigyn.jws.usermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.usermanagement.entities.JwsRole;

@Repository
public interface JwsRoleRepository extends JpaRepository<JwsRole, String> {

}
