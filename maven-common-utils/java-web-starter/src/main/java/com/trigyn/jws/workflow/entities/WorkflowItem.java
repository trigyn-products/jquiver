package com.trigyn.jws.workflow.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "workflow_item")
public class WorkflowItem {

    @Id
    @Column(name = "item_id", length = 50, nullable = false)
    private String itemId;

    @Column(name = "item_name", length = 100)
    private String itemName;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_status_id", nullable = false)
    private WorkflowStatus currentStatus;
    
    @Column(name = "created_by")
	private String				createdBy			= null;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at")
	private Date				createdDate			= null;

	@Column(name = "last_updated_by")
	private String				lastUpdatedBy		= "admin@jquiver.io";

	@Column(name = "last_updated_ts")
	private Date				lastUpdatedTs		= null;

    
    
    @Column(name = "entity_id", length = 50, nullable = false)
    private String entityId;
    

    // Getters and Setters
    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    
	public WorkflowStatus getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(WorkflowStatus currentStatus) {
		this.currentStatus = currentStatus;
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

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
    
    
    
}
