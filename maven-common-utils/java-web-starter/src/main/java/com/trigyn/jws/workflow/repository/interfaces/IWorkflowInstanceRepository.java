package com.trigyn.jws.workflow.repository.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.workflow.entities.WorkflowInstance;

@Repository
public interface IWorkflowInstanceRepository extends JpaRepository<WorkflowInstance, String> {

    // ✅ Custom queries if needed

    // Find all active workflow instances
    List<WorkflowInstance> findByIsActiveTrue();

    // Find by definition id
    List<WorkflowInstance> findByDefinitionId(String definitionId);

    // Find by current status id
    List<WorkflowInstance> findByCurrentStatusId(String currentStatusId);

    // Find workflows started by a specific user
    List<WorkflowInstance> findByStartedBy(String startedBy);

	List<WorkflowInstance> findByDefinitionIdAndIsActive(String definitionId, boolean b);
}
 