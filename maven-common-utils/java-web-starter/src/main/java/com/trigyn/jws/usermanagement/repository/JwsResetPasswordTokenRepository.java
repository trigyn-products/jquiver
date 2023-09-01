package com.trigyn.jws.usermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.usermanagement.entities.JwsResetPasswordToken;

@Repository
public interface JwsResetPasswordTokenRepository extends JpaRepository<JwsResetPasswordToken, String> {

	JwsResetPasswordToken findByTokenId(String token);

	@Modifying
	@Transactional
	@Query("Update JwsResetPasswordToken set isResetUrlExpired=:isResetUrlExpired  WHERE userId=:userId and tokenId=:tokenId")
	void updateUrlExpired(Boolean isResetUrlExpired, String userId, String tokenId);
}
