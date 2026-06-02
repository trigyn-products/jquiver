package com.trigyn.jws.tags.repository.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dbutils.entities.JwsBusinessModuleEntity;
import com.trigyn.jws.tags.entities.TagEntityDetails;
import com.trigyn.jws.usermanagement.entities.JwsRole;
import com.trigyn.jws.workflow.entities.WorkflowDefinition;

@Repository
public interface ITagEntityDetailsRepository extends JpaRepository<TagEntityDetails, String> {

	@Query(" FROM TagEntityDetails WHERE tagId=:tagId ")
	List<TagEntityDetails> getTagEntityDetailsByTagId(@Param("tagId") String tagId);
}