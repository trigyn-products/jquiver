package com.trigyn.jws.dbutils.repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dbutils.entities.JwsTemplateVersion;

@Repository
public interface JwsTemplateVersionRepository extends JpaRepositoryImplementation<JwsTemplateVersion, String>{

}
