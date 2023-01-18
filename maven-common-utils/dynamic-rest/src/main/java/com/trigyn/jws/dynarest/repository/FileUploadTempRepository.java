package com.trigyn.jws.dynarest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dynarest.dao.QueryStore;
import com.trigyn.jws.dynarest.entities.FileUploadTemp;

@Repository
public interface FileUploadTempRepository extends JpaRepositoryImplementation<FileUploadTemp, String> {

	@Query(QueryStore.JPA_QUERY_TO_GET_ALL_TEMP_FILE_DETAILS_BY_FILE_UPLOAD_ID)
	List<FileUploadTemp> findAllTempFileUpload(String fileUploadId);
	
	@Query(QueryStore.JPA_QUERY_TO_GET_FILE_TEMP_DETAILS_BY_FILE_BIN_ID)
	List<FileUploadTemp> findAllByFileTempBinId(String fileBinId);
	
	@Query(QueryStore.JPA_QUERY_TO_GET_FILE_TEMP_DETAILS_BY_FILE_BIN_ID_ILE_TEMP_ASSOC_ID)
	List<FileUploadTemp> getAllTempDeletedFileUploadId(String fileBinId, String fileAssociationId, String fileUploadId);

}
