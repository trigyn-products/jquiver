package app.trigyn.common.dashboard.vo;

import java.io.Serializable;
import java.util.Objects;

public class ContextMasterVO implements Serializable{

	private static final long 	serialVersionUID 		= 3585706877150485806L;

	private String				contextId				= null;

	private String				contextDescription		= null;

	private Integer				allowDashboardAddition	= null;

	public ContextMasterVO() {
		
	}

	public ContextMasterVO(String contextId, String contextDescription, Integer allowDashboardAddition) {
		this.contextId 					= contextId;
		this.contextDescription 		= contextDescription;
		this.allowDashboardAddition 	= allowDashboardAddition;
	}

	public ContextMasterVO(String contextId, String contextDescription) {
		this.contextId 			= contextId;
		this.contextDescription = contextDescription;
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

	@Override
	public int hashCode() {
		return Objects.hash(allowDashboardAddition, contextDescription, contextId);
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
		ContextMasterVO other = (ContextMasterVO) obj;
		return Objects.equals(allowDashboardAddition, other.allowDashboardAddition) && Objects.equals(contextDescription, other.contextDescription) && Objects.equals(contextId, other.contextId);
	}

	@Override
	public String toString() {
		return "ContextMasterVO [contextId=" + contextId + ", contextDescription=" + contextDescription + ", allowDashboardAddition=" + allowDashboardAddition + "]";
	}
	
	
}
