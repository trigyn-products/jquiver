package com.trigyn.jws.security.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.security.entities.DDOSDetails;

@Repository
public interface DDOSDetailsRepository extends JpaRepository<DDOSDetails, String>{

	@Query("SELECT DISTINCT ddosD.ipAddress FROM DDOSDetails AS ddosD ")
	List<String> getAllBlockedIPAddr();
}
