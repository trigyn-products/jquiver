package com.trigyn.jws.dynarest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dynarest.dao.QueryStore;
import com.trigyn.jws.dynarest.entities.FileUpload;

public interface FileUploadRepository extends JpaRepositoryImplementation<FileUpload, String> {

	@Query(QueryStore.JPA_QUERY_TO_GET_FILE_DETAILS_ID_BY_FILE_UPLOAD_ID)
	FileUpload findFileBinIdByUploadId(@Param("fileUploadId") String fileUploadId);

	@Query(QueryStore.JPA_QUERY_TO_GET_ALL_FILE_DETAILS_BY_FILE_UPLOAD_ID)
	List<FileUpload> findAllByFileUploadIds(@Param("fileUploadIdList") List<String> fileUploadIdList);

	@Query(QueryStore.JPA_QUERY_TO_GET_FILE_DETAILS_BY_FILE_BIN_ID_AND_ASSOC_ID)
	List<FileUpload> findAllFilesByConfigId(@Param("fileBinId") String fileBinId, @Param("fileAssociationId") String fileAssociationId);

	@Query(QueryStore.JPA_QUERY_TO_GET_FILE_DETAILS_BY_FILE_ASSOC_ID)
	List<FileUpload> findAllByFileAssociationId(@Param("fileAssociationId") String fileAssociationId);

	@Query(QueryStore.JPA_QUERY_TO_GET_FILE_DETAILS_BY_FILE_BIN_ID)
	List<FileUpload> findAllByFileBinId(@Param("fileBinId") String fileBinId);

}
