package com.trigyn.jws.dbutils.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.sciptlibrary.entities.ScriptLibraryConnection;
import com.trigyn.jws.sciptlibrary.entities.ScriptLibraryDetails;

@Repository
public interface ScriptLibraryRepository extends JpaRepository<ScriptLibraryDetails, String> {

}
