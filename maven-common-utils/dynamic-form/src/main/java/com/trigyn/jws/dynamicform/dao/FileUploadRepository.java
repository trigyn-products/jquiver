package com.trigyn.jws.dynamicform.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dynamicform.entities.FileUpload;

@Repository
public interface FileUploadRepository extends JpaRepositoryImplementation<FileUpload, String> {

	@Query(QueryStore.QUERY_TO_GET_FILE_DETAILS)
	List<FileUpload> findAllByIds(List<String> fileIdList);

}
