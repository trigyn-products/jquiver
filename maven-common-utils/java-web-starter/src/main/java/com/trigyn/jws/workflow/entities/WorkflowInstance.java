package com.trigyn.jws.workflow.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "jq_workflow_instance")
public class WorkflowInstance {

    @Id
    @Column(name = "instance_id", length = 36, nullable = false)
    private String instanceId;

    @Column(name = "definition_id", length = 36)
    private String definitionId;

    @Column(name = "current_status_id", length = 36)
    private String currentStatusId;

    @Column(name = "started_by", length = 50)
    private String startedBy;

    @Column(name = "started_at")
    private Date startedAt;

    @Column(name = "last_updated_by", length = 50)
    private String lastUpdatedBy;

    @Column(name = "last_updated_ts")
    private Date lastUpdatedTs;

    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "version")
	private Integer				version				= 1;

    // ===== Getters & Setters =====

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getDefinitionId() {
        return definitionId;
    }

    public void setDefinitionId(String definitionId) {
        this.definitionId = definitionId;
    }

    public String getCurrentStatusId() {
        return currentStatusId;
    }

    public void setCurrentStatusId(String currentStatusId) {
        this.currentStatusId = currentStatusId;
    }

    public String getStartedBy() {
        return startedBy;
    }

    public void setStartedBy(String startedBy) {
        this.startedBy = startedBy;
    }

    public Date getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Date getLastUpdatedTs() {
        return lastUpdatedTs;
    }

    public void setLastUpdatedTs(Date lastUpdatedTs) {
        this.lastUpdatedTs = lastUpdatedTs;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
    
    
}
