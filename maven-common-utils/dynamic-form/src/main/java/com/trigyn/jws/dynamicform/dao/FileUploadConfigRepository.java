package com.trigyn.jws.dynamicform.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dynamicform.entities.FileUploadConfig;

@Repository
public interface FileUploadConfigRepository extends JpaRepository<FileUploadConfig, String> {

	@Query(" FROM FileUploadConfig WHERE fileUploadConfigId=:fileUploadConfigId")
	FileUploadConfig getFileUploadConfig(String fileUploadConfigId);

}
