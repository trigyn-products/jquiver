package com.trigyn.jws.workflow.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "jq_workflow_definition")
public class WorkflowDefinition implements Serializable {

	private static final long	serialVersionUID	= 7619794502605628730L;

	@Id
	@Column(name = "definition_id", nullable = false)
	private String				definitionId;

	@Column(name = "definition_name", nullable = false, length = 100)
	private String				definitionName;

	@Lob
	@Column(name = "bpmn_xml", nullable = false, columnDefinition = "TEXT")
	private String				bpmnXml;

	@Column(name = "version")
	private Integer				version				= 1;

	@Column(name = "uploaded_by")
	private String				uploadedBy;

	@Column(name = "uploaded_at")
	private Date				uploadedAt;

	@Column(name = "created_by")
	private String				createdBy			= null;

	@JsonIgnore
	@Column(name = "created_date")
	private Date				createdDate			= null;

	
	@Column(name = "is_active")
	private Integer	isActive					= null;

	public String getDefinitionId() {
		return definitionId;
	}

	public void setDefinitionId(String definitionId) {
		this.definitionId = definitionId;
	}

	public String getDefinitionName() {
		return definitionName;
	}

	public void setDefinitionName(String definitionName) {
		this.definitionName = definitionName;
	}

	public String getBpmnXml() {
		return bpmnXml;
	}

	public void setBpmnXml(String bpmnXml) {
		this.bpmnXml = bpmnXml;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getUploadedBy() {
		return uploadedBy;
	}

	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	public Date getUploadedAt() {
		return uploadedAt;
	}

	public void setUploadedAt(Date uploadedAt) {
		this.uploadedAt = uploadedAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	@Override
	public String toString() {
		return "WorkflowDefinition [definitionId=" + definitionId + ", definitionName=" + definitionName + ", bpmnXml="
				+ bpmnXml + ", version=" + version + ", uploadedBy=" + uploadedBy + ", uploadedAt=" + uploadedAt
				+ ", createdBy=" + createdBy + ", createdDate=" + createdDate + ", isActive=" + isActive + "]";
	}


	public WorkflowDefinition getObject() {
		WorkflowDefinition workflowDefinition = new WorkflowDefinition();
		workflowDefinition.setDefinitionId(definitionId);
		workflowDefinition.setDefinitionName(definitionName);
		workflowDefinition.setBpmnXml(bpmnXml);		
		workflowDefinition.setVersion(version);		
		workflowDefinition.setUploadedBy(uploadedBy != null ? uploadedBy.trim() : uploadedBy);		
		workflowDefinition.setUploadedAt(uploadedAt);	
		workflowDefinition.setCreatedBy(createdBy != null ? createdBy.trim() : createdBy);
		workflowDefinition.setCreatedDate(createdDate);
		workflowDefinition.setIsActive(isActive);		
		return workflowDefinition;
	}

	public WorkflowDefinition() {
		super();
	}

	@Override
	public int hashCode() {
		return Objects.hash(bpmnXml, createdBy, createdDate, definitionId, definitionName, isActive, uploadedAt,
				uploadedBy, version);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WorkflowDefinition other = (WorkflowDefinition) obj;
		return Objects.equals(bpmnXml, other.bpmnXml) && Objects.equals(createdBy, other.createdBy)
				&& Objects.equals(createdDate, other.createdDate) && Objects.equals(definitionId, other.definitionId)
				&& Objects.equals(definitionName, other.definitionName) && Objects.equals(isActive, other.isActive)
				&& Objects.equals(uploadedAt, other.uploadedAt) && Objects.equals(uploadedBy, other.uploadedBy)
				&& Objects.equals(version, other.version);
	}


	
	
	
	
}
