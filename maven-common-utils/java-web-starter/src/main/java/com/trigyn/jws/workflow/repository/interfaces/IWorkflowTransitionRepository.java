package com.trigyn.jws.workflow.repository.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import com.trigyn.jws.workflow.entities.WorkflowStatus;
import com.trigyn.jws.workflow.entities.WorkflowTransition;

@Repository
public interface IWorkflowTransitionRepository extends JpaRepository<WorkflowTransition, String> {
	Optional<WorkflowTransition> findByFromStatusAndToStatus(WorkflowStatus fromStatus, WorkflowStatus toStatus);

	@Query("SELECT t.toStatus FROM WorkflowTransition t WHERE t.fromStatus.statusId = :fromStatus")
	List<WorkflowStatus> findNextStatusesByCurrent(@Param("fromStatus") String fromStatus);

}
