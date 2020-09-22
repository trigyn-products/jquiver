package com.trigyn.jws.dashboard.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class DashletConfigurationPK implements Serializable{

	private static final long serialVersionUID = 7853312994220843804L;

	@Column(name = "user_id", nullable = false)
	private String				userId				= null;

	@Column(name = "dashlet_id", nullable = false)
	private String				dashletId			= null;
	
	@Column(name = "dashboard_id")
	private String				dashboardId			= null;

	public DashletConfigurationPK() {
		
	}

	public DashletConfigurationPK(String userId, String dashletId, String dashboardId) {
		this.userId 		= userId;
		this.dashletId 		= dashletId;
		this.dashboardId 	= dashboardId;
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

	/**
	 * @return the dashletId
	 */
	public String getDashletId() {
		return dashletId;
	}

	/**
	 * @param dashletId the dashletId to set
	 */
	public void setDashletId(String dashletId) {
		this.dashletId = dashletId;
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

	@Override
	public int hashCode() {
		return Objects.hash(dashboardId, dashletId, userId);
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
		DashletConfigurationPK other = (DashletConfigurationPK) obj;
		return Objects.equals(dashboardId, other.dashboardId) && Objects.equals(dashletId, other.dashletId) && Objects.equals(userId, other.userId);
	}

	@Override
	public String toString() {
		return "DashletConfigurationPK [userId=" + userId + ", dashletId=" + dashletId + ", dashboardId=" + dashboardId + "]";
	}
	
	
	
}

