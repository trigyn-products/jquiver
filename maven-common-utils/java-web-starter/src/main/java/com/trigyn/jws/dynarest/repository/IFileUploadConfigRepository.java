package com.trigyn.jws.dynarest.repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dynarest.entities.FileUploadConfig;

public interface IFileUploadConfigRepository extends JpaRepositoryImplementation<FileUploadConfig, String> {

}
