package com.trigyn.jws.workflow.entities;

import java.io.Serializable;
import java.util.Date;

import com.trigyn.jws.workflow.utils.StatusType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name =  "jq_workflow_status")
public class WorkflowStatus implements Serializable {


	private static final long serialVersionUID = 7828213756392887697L;

	@Id
	@Column(name = "status_id", nullable = false)
    private String statusId;

    @Column(name = "status_name", nullable = false, length = 50)
    private String statusName;

    @Column(name = "status_order")
    private Integer statusOrder;

    @Column(name = "is_final", nullable = false)
    private Boolean isFinal = false;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    @Column(name = "order_no", nullable = false)
    private Integer orderNo;

    //  Many statuses belong to one definition
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "definition_id")
    private WorkflowDefinition definition;


    @Column(name = "created_by")
   	private String				createdBy			= null;

   	@Temporal(TemporalType.TIMESTAMP)
   	@Column(name = "created_date")
   	private Date				createdDate			= null;

   	@Column(name = "last_updated_by")
   	private String				lastUpdatedBy		= "admin@jquiver.io";

   	@Column(name = "last_updated_ts")
   	private Date				lastUpdatedTs		= null;
   	
	@Column(name = "version")
	private Integer				version	;
	
   
    @Column(name = "is_user_task", nullable = false)
    private Boolean isUserTask = true;
    
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status_type", length = 50, nullable = false)
    private StatusType type;

    // --- getters & setters ---
    public StatusType getType() {
        return type;
    }

    public void setType(StatusType type) {
        this.type = type;
        this.isFinal = (type == StatusType.END_EVENT);
        this.isUserTask = (type == StatusType.USER_TASK);
    }

       
       
    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Integer getStatusOrder() {
        return statusOrder;
    }

    public void setStatusOrder(Integer statusOrder) {
        this.statusOrder = statusOrder;
    }

    public Boolean getIsFinal() {
        return isFinal;
    }

    public void setIsFinal(Boolean isFinal) {
        this.isFinal = isFinal;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public WorkflowDefinition getDefinition() {
        return definition;
    }

    public void setDefinition(WorkflowDefinition definition) {
        this.definition = definition;
    }

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
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

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Boolean getIsUserTask() {
		return isUserTask;
	}

	public void setIsUserTask(Boolean isUserTask) {
		this.isUserTask = isUserTask;
	}


    
}