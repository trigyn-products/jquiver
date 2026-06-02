package com.trigyn.jws.workflow.entities;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name =  "jq_workflow_transition")
public class WorkflowTransition implements Serializable {


	private static final long serialVersionUID = 6532621632982113569L;

	@Id
	@Column(name = "transition_id", nullable = false)
    private String transitionId;

    // --- Many transitions can start from one status ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_status_id", nullable = false)
    private WorkflowStatus fromStatus;

    // --- Many transitions can end at one status ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_status_id", nullable = false)
    private WorkflowStatus toStatus;

    @Column(name = "allowed_roles", length = 255)
    private String allowedRoles;

    @Column(name = "transition_label", length = 50)
    private String transitionLabel;
    
    // Link to workflow definition
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "definition_id", nullable = false)
    private WorkflowDefinition workflowDefinition;
    
    @Column(name = "order_no", nullable = false)
    private Integer orderNo;

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
	
	@Column(name = "action")
	private String action;  // friendly label

	@Column(name = "raw_action_expression")
	private String rawActionExpression;  // exact BPMN expression
       
    
    public String getTransitionId() {
        return transitionId;
    }

    public void setTransitionId(String transitionId) {
        this.transitionId = transitionId;
    }

    public WorkflowStatus getFromStatus() {
        return fromStatus;
    }

    public void setFromStatus(WorkflowStatus fromStatus) {
        this.fromStatus = fromStatus;
    }

    public WorkflowStatus getToStatus() {
        return toStatus;
    }

    public void setToStatus(WorkflowStatus toStatus) {
        this.toStatus = toStatus;
    }

    public String getAllowedRoles() {
        return allowedRoles;
    }

    public void setAllowedRoles(String allowedRoles) {
        this.allowedRoles = allowedRoles;
    }

    public String getTransitionLabel() {
        return transitionLabel;
    }

    public void setTransitionLabel(String transitionLabel) {
        this.transitionLabel = transitionLabel;
    }

	public WorkflowDefinition getWorkflowDefinition() {
		return workflowDefinition;
	}

	public void setWorkflowDefinition(WorkflowDefinition workflowDefinition) {
		this.workflowDefinition = workflowDefinition;
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

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getRawActionExpression() {
		return rawActionExpression;
	}

	public void setRawActionExpression(String rawActionExpression) {
		this.rawActionExpression = rawActionExpression;
	}
    
	
    
}
