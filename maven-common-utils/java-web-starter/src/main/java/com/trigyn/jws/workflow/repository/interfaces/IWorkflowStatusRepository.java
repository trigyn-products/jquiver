package com.trigyn.jws.workflow.repository.interfaces;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.workflow.entities.WorkflowStatus;

@Repository 
public interface IWorkflowStatusRepository extends JpaRepository<WorkflowStatus, String> {

	
	@Query("SELECT ws FROM WorkflowStatus ws WHERE ws.definition.id = :definitionId")
	Optional<WorkflowStatus> findByDefinitionId(@Param("definitionId") String definitionId);

} 
