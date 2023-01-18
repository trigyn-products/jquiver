package com.trigyn.jws.dynarest.repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dynarest.entities.FileUploadConfig;

@Repository
public interface IFileUploadConfigRepository extends JpaRepositoryImplementation<FileUploadConfig, String> {

}
