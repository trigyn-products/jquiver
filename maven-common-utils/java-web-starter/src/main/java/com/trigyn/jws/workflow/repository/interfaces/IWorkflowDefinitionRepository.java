package com.trigyn.jws.workflow.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.workflow.entities.WorkflowDefinition;

@Repository
public interface IWorkflowDefinitionRepository extends JpaRepository<WorkflowDefinition, String> {

	@Query("SELECT definitionName FROM WorkflowDefinition AS wfd  WHERE definitionName = :definitionName ")
	String checkDefinationExist(String definitionName);

}