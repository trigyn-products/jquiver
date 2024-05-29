package com.trigyn.jws.dynarest.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dynarest.entities.JqApiClientDetails;
import com.trigyn.jws.dynarest.vo.ApiClientDetailsVO;

@Repository
public interface IApiClientDetailsRepository extends JpaRepositoryImplementation<JqApiClientDetails, String> {

	@Query("SELECT new com.trigyn.jws.dynarest.vo.ApiClientDetailsVO(cd.clientId AS clientId, cd.clientName AS clientName, "
			+ "cd.clientKey AS clientKey, cd.clientSecret AS clientSecret, "
			+ "empk.jqEncryptionAlgorithmsLookup.encryptionAlgoId AS encryptionAlgorithmId, empk.jqEncryptionAlgorithmsLookup.encryptionAlgoName AS encryptionAlgorithmName, empk.encLookupId AS encLookupId)"
			+ "FROM JqApiClientDetails cd, JqEncAlgModPadKeyLookup empk WHERE cd.jqEncAlgModPadKeyLookup.encLookupId = empk.encLookupId AND "
			+ "cd.clientKey=:clientKey ")
	ApiClientDetailsVO findClientDetailsByClientKey(String clientKey);

}
