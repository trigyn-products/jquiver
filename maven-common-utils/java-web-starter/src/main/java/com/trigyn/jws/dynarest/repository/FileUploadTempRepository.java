package com.trigyn.jws.dynarest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dynarest.dao.QueryStore;
import com.trigyn.jws.dynarest.entities.FileUploadTemp;

public interface FileUploadTempRepository extends JpaRepositoryImplementation<FileUploadTemp, String> {

	@Query(QueryStore.JPA_QUERY_TO_GET_ALL_TEMP_FILE_DETAILS_BY_FILE_UPLOAD_ID)
	List<FileUploadTemp> findAllTempFileUpload(@Param("fileUploadId") String fileUploadId);
	
	@Query(QueryStore.JPA_QUERY_TO_GET_FILE_TEMP_DETAILS_BY_FILE_BIN_ID)
	List<FileUploadTemp> findAllByFileTempBinId(@Param("fileBinId") String fileBinId);
	
	@Query(QueryStore.JPA_QUERY_TO_GET_FILE_TEMP_DETAILS_BY_FILE_BIN_ID_FILE_TEMP_ASSOC_ID_FILE_UPLOAD_ID)
	List<FileUploadTemp> getAllTempDeletedFileUploadId(@Param("fileBinId") String fileBinId, 
			@Param("fileAssociationId") String fileAssociationId, @Param("fileUploadId") String fileUploadId);

	@Query(QueryStore.JPA_QUERY_TO_GET_FILE_TEMP_DETAILS_BY_FILE_BIN_ID_FILE_TEMP_ASSOC_ID_TEMP_ID)
	List<FileUploadTemp> getAllTempDeletedFileUploadTempId(@Param("fileBinId") String fileBinId, 
			@Param("fileAssociationId") String fileAssociationId, @Param("fileUploadTempId") String fileUploadTempId);

	@Query(QueryStore.JPA_QUERY_TO_GET_ALL_TEMP_FILE_DETAILS_BY_FILE_UPLOAD_TEMP_ID)
	List<FileUploadTemp> getAllTempFileUploadTempId(String fileUploadTempId);

}
