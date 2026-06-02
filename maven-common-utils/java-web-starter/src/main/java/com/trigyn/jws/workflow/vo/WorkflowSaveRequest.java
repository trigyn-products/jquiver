package com.trigyn.jws.workflow.vo;

import com.trigyn.jws.workflow.entities.WorkflowDefinition;

public class WorkflowSaveRequest {
	private WorkflowDefinition workflowDefinition;
    private String workflowFileName;
    private String workflowFileContent;
	public WorkflowDefinition getWorkflowDefinition() {
		return workflowDefinition;
	}
	public void setWorkflowDefinition(WorkflowDefinition workflowDefinition) {
		this.workflowDefinition = workflowDefinition;
	}
	public String getWorkflowFileName() {
		return workflowFileName;
	}
	public void setWorkflowFileName(String workflowFileName) {
		this.workflowFileName = workflowFileName;
	}
	public String getWorkflowFileContent() {
		return workflowFileContent;
	}
	public void setWorkflowFileContent(String workflowFileContent) {
		this.workflowFileContent = workflowFileContent;
	}
    
    
}
