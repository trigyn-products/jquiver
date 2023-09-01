package com.trigyn.jws.dashboard.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "jq_context_master")
public class ContextMaster implements Serializable {

	private static final long	serialVersionUID		= -7620849431138092706L;

	@Id
	@Column(name = "context_id", nullable = false)
	private String				contextId				= null;

	@Column(name = "context_description")
	private String				contextDescription		= null;

	@Column(name = "allow_dashboard_addition")
	private Integer				allowDashboardAddition	= null;

	@Column(name = "created_by")
	private String				createdBy				= null;

	@Column(name = "created_date")
	private Date				createdDate				= null;

	public ContextMaster() {

	}

	public ContextMaster(String contextId, String contextDescription, Integer allowDashboardAddition, String createdBy, Date createdDate) {
		this.contextId				= contextId;
		this.contextDescription		= contextDescription;
		this.allowDashboardAddition	= allowDashboardAddition;
		this.createdBy				= createdBy;
		this.createdDate			= createdDate;
	}

	public String getContextId() {
		return contextId;
	}

	public void setContextId(String contextId) {
		this.contextId = contextId;
	}

	public String getContextDescription() {
		return contextDescription;
	}

	public void setContextDescription(String contextDescription) {
		this.contextDescription = contextDescription;
	}

	public Integer getAllowDashboardAddition() {
		return allowDashboardAddition;
	}

	public void setAllowDashboardAddition(Integer allowDashboardAddition) {
		this.allowDashboardAddition = allowDashboardAddition;
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

	@Override
	public int hashCode() {
		return Objects.hash(allowDashboardAddition, contextDescription, contextId, createdBy, createdDate);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ContextMaster other = (ContextMaster) obj;
		return Objects.equals(allowDashboardAddition, other.allowDashboardAddition)
				&& Objects.equals(contextDescription, other.contextDescription) && Objects.equals(contextId, other.contextId)
				&& Objects.equals(createdBy, other.createdBy) && Objects.equals(createdDate, other.createdDate)
				;
	}

	@Override
	public String toString() {
		return "ContextMaster [contextId=" + contextId + ", contextDescription=" + contextDescription + ", allowDashboardAddition="
				+ allowDashboardAddition + ", createdBy=" + createdBy + ", createdDate=" + createdDate  + "]";
	}

	public ContextMaster getObject() {
		ContextMaster obj = new ContextMaster();

		obj.setAllowDashboardAddition(allowDashboardAddition);
		obj.setContextDescription(contextDescription != null ? contextDescription.trim() : contextDescription);
		obj.setContextId(contextId != null ? contextId.trim() : contextId);
		obj.setCreatedBy(createdBy != null ? createdBy.trim() : createdBy);
		obj.setCreatedDate(createdDate);

		return obj;
	}
}
