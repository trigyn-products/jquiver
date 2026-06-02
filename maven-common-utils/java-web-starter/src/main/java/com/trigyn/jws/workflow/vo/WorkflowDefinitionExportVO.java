package com.trigyn.jws.workflow.vo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trigyn.jws.webstarter.vo.GenericUserNotification;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

public class WorkflowDefinitionExportVO implements Serializable {

	private static final long	serialVersionUID	= -6479264054891375114L;

	private String				definitionId		= null;

	private String				definitionName		= null;

	private String				bpmnXml				= null;

	private String				bpmnFile			= null;

	private Integer				version				= 1;

	private String				uploadedBy			= null;

	private Date				uploadedAt			= null;

	private String				createdBy			= null;

	private Date				createdDate			= null;

	private Integer				isActive			= null;

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

	public String getBpmnFile() {
		return bpmnFile;
	}

	public void setBpmnFile(String bpmnFile) {
		this.bpmnFile = bpmnFile;
	}

	public WorkflowDefinitionExportVO() {
	}

	public WorkflowDefinitionExportVO(String definitionId, String definitionName, String bpmnFile, Integer version,
			String uploadedBy, Date uploadedAt, String createdBy, Date createdDate, Integer isActive) {
		this.definitionId	= definitionId;
		this.definitionName	= definitionName;
		this.bpmnFile		= bpmnFile;
		this.version		= version;
		this.uploadedBy		= uploadedBy;
		this.uploadedAt		= uploadedAt;
		this.createdBy		= createdBy;
		this.createdDate	= createdDate;
		this.isActive		= isActive;
	}

	@Override
	public int hashCode() {
		return Objects.hash(bpmnFile, bpmnXml, createdBy, createdDate, definitionId, definitionName, isActive,
				uploadedAt, uploadedBy, version);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WorkflowDefinitionExportVO other = (WorkflowDefinitionExportVO) obj;
		return Objects.equals(bpmnFile, other.bpmnFile) && Objects.equals(bpmnXml, other.bpmnXml)
				&& Objects.equals(createdBy, other.createdBy) && Objects.equals(createdDate, other.createdDate)
				&& Objects.equals(definitionId, other.definitionId)
				&& Objects.equals(definitionName, other.definitionName) && Objects.equals(isActive, other.isActive)
				&& Objects.equals(uploadedAt, other.uploadedAt) && Objects.equals(uploadedBy, other.uploadedBy)
				&& Objects.equals(version, other.version);
	}

}
