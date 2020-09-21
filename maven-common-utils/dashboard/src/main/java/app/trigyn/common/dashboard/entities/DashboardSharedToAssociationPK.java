package app.trigyn.common.dashboard.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class DashboardSharedToAssociationPK implements Serializable{

	private static final long serialVersionUID 	= -2863163969760751108L;

	@Column(name = "dashboard_id")
	private String	dashboardId					= null;

	@Column(name = "user_id")
	private String	userId						= null;

	public DashboardSharedToAssociationPK() {
		
	}

	public DashboardSharedToAssociationPK(String dashboardId, String userId) {
		this.dashboardId 	= dashboardId;
		this.userId 		= userId;
	}

	/**
	 * @return the dashboardId
	 */
	public String getDashboardId() {
		return dashboardId;
	}

	/**
	 * @param dashboardId the dashboardId to set
	 */
	public void setDashboardId(String dashboardId) {
		this.dashboardId = dashboardId;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dashboardId, userId);
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
		DashboardSharedToAssociationPK other = (DashboardSharedToAssociationPK) obj;
		return Objects.equals(dashboardId, other.dashboardId) && Objects.equals(userId, other.userId);
	}

	@Override
	public String toString() {
		return "DashboardSharedToAssociationPK [dashboardId=" + dashboardId + ", userId=" + userId + "]";
	}
	
	
}
