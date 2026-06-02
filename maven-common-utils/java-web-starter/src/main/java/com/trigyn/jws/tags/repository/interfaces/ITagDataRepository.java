package com.trigyn.jws.tags.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.tags.entities.TagData;
import com.trigyn.jws.workflow.entities.WorkflowDefinition;

@Repository
public interface ITagDataRepository extends JpaRepository<TagData, String> {

	boolean existsByTagNameIgnoreCase(String tagName);



}