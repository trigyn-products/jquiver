package com.trigyn.jws.dynarest.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dynarest.entities.ApiClientDetails;
import com.trigyn.jws.dynarest.vo.ApiClientDetailsVO;

@Repository
public interface IApiClientDetailsRepository extends JpaRepositoryImplementation<ApiClientDetails, String> {

	@Query("SELECT new com.trigyn.jws.dynarest.vo.ApiClientDetailsVO(cd.clientId AS clientId, cd.clientName AS clientName, "
			+ "cd.clientKey AS clientKey, cd.clientSecret AS clientSecret, "
			+ "ea.encryptionAlgorithmId AS encryptionAlgorithmId, ea.encryptionAlgorithmName AS encryptionAlgorithmName, "
			+ "cd.inclusionURLPattern AS inclusionURLPattern)"
			+ "FROM ApiClientDetails cd, EncryptionAlgorithms ea WHERE "
			+ "cd.encryptionAlgoId = ea.encryptionAlgorithmId AND cd.clientKey=:clientKey ")
	ApiClientDetailsVO findClientDetailsByClientKey(String clientKey);

}
