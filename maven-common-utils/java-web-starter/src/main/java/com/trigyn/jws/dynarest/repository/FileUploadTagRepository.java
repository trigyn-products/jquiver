package com.trigyn.jws.dynarest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dynarest.dao.QueryStore;
import com.trigyn.jws.dynarest.entities.FileUpload;
import com.trigyn.jws.dynarest.entities.FileUploadTags;

public interface FileUploadTagRepository extends JpaRepositoryImplementation<FileUploadTags, String> {

	@Query(QueryStore.JPA_QUERY_TO_GET_FILE_TAG_DETAILS_ID_BY_FILE_UPLOAD_TAG_ID)
	FileUploadTags findFileBinIdByUploadId(@Param("fileUploadId") String fileUploadId);

	
}
