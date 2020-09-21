package app.trigyn.common.dashboard.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "context_master")
public class ContextMaster implements Serializable{

	private static final long serialVersionUID 				= -7620849431138092706L;

	@Id
	@Column(name = "context_id", nullable = false)
	private String				contextId					= null;

	@Column(name = "context_description")
	private String				contextDescription			= null;

	@Column(name = "allow_dashboard_addition")
	private Integer				allowDashboardAddition		= null;

	@Column(name = "created_by")
	private String				createdBy					= null;

	@Column(name = "created_date")
	private Date				createdDate					= null;
	
	@OneToMany(mappedBy = "contextMaster", fetch = FetchType.LAZY)
	private List<Dashlet>	dashlet							= new ArrayList<>();

	public ContextMaster() {

	}
	
	public ContextMaster(String contextId, String contextDescription, Integer allowDashboardAddition, String createdBy,
			Date createdDate) {
		this.contextId 				= contextId;
		this.contextDescription 	= contextDescription;
		this.allowDashboardAddition = allowDashboardAddition;
		this.createdBy 				= createdBy;
		this.createdDate 			= createdDate;
	}
	
	
	/**
	 * @return the contextId
	 */
	public String getContextId() {
		return contextId;
	}

	/**
	 * @param contextId the contextId to set
	 */
	public void setContextId(String contextId) {
		this.contextId = contextId;
	}

	/**
	 * @return the contextDescription
	 */
	public String getContextDescription() {
		return contextDescription;
	}

	/**
	 * @param contextDescription the contextDescription to set
	 */
	public void setContextDescription(String contextDescription) {
		this.contextDescription = contextDescription;
	}

	/**
	 * @return the allowDashboardAddition
	 */
	public Integer getAllowDashboardAddition() {
		return allowDashboardAddition;
	}

	/**
	 * @param allowDashboardAddition the allowDashboardAddition to set
	 */
	public void setAllowDashboardAddition(Integer allowDashboardAddition) {
		this.allowDashboardAddition = allowDashboardAddition;
	}

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the dashlet
	 */
	public List<Dashlet> getDashlet() {
		return dashlet;
	}

	/**
	 * @param dashlet the dashlet to set
	 */
	public void setDashlet(List<Dashlet> dashlet) {
		this.dashlet = dashlet;
	}

	@Override
	public int hashCode() {
		return Objects.hash(allowDashboardAddition, contextDescription, contextId, createdBy, createdDate, dashlet);
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
				&& Objects.equals(contextDescription, other.contextDescription)
				&& Objects.equals(contextId, other.contextId) && Objects.equals(createdBy, other.createdBy)
				&& Objects.equals(createdDate, other.createdDate) && Objects.equals(dashlet, other.dashlet);
	}

	@Override
	public String toString() {
		return "ContextMaster [contextId=" + contextId + ", contextDescription=" + contextDescription
				+ ", allowDashboardAddition=" + allowDashboardAddition + ", createdBy=" + createdBy + ", createdDate="
				+ createdDate + ", dashlet=" + dashlet + "]";
	}





	
}
