package com.trigyn.jws.dynamicform.dao;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dynamicform.entities.FileUpload;

@Repository
public interface FileUploadRepository extends JpaRepositoryImplementation<FileUpload, String>{

}
