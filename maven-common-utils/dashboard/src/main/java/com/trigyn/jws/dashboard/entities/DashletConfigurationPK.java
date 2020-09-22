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

	
	public String getUserId() {
		return userId;
	}

	
	public void setUserId(String userId) {
		this.userId = userId;
	}

	
	public String getDashletId() {
		return dashletId;
	}

	
	public void setDashletId(String dashletId) {
		this.dashletId = dashletId;
	}

	
	public String getDashboardId() {
		return dashboardId;
	}

	
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

