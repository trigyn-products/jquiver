package com.trigyn.jws.dynarest.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dynarest.entities.FileUploadConfig;

@Repository
public interface FileUploadConfigRepository extends JpaRepository<FileUploadConfig, String> {

	@Query(" FROM FileUploadConfig WHERE fileBinId=:fileBinId")
	FileUploadConfig getFileUploadConfig(String fileBinId);

}
