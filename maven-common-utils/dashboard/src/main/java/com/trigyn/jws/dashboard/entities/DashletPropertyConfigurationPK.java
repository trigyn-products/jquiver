package com.trigyn.jws.dashboard.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class DashletPropertyConfigurationPK implements Serializable{

	private static final long serialVersionUID 	= 6379109773178182080L;

	@Column(name = "user_id", nullable = false)
	private String				userId				= null;

	@Column(name = "property_id", nullable = false)
	private String				propertyId			= null;
	
	@Column(name = "dashboard_id", nullable = false)
	private String				dashboardId			= null;

	public DashletPropertyConfigurationPK() {
		
	}

	public DashletPropertyConfigurationPK(String userId, String propertyId, String dashboardId) {
		this.userId 		= userId;
		this.propertyId 	= propertyId;
		this.dashboardId 	= dashboardId;
	}

	
	public String getUserId() {
		return userId;
	}

	
	public void setUserId(String userId) {
		this.userId = userId;
	}

	
	public String getPropertyId() {
		return propertyId;
	}

	
	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	
	public String getDashboardId() {
		return dashboardId;
	}

	
	public void setDashboardId(String dashboardId) {
		this.dashboardId = dashboardId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dashboardId, propertyId, userId);
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
		DashletPropertyConfigurationPK other = (DashletPropertyConfigurationPK) obj;
		return Objects.equals(dashboardId, other.dashboardId) && Objects.equals(propertyId, other.propertyId) && Objects.equals(userId, other.userId);
	}

	@Override
	public String toString() {
		return "DashletPropertyConfigurationPK [userId=" + userId + ", propertyId=" + propertyId + ", dashboardId=" + dashboardId + "]";
	}
	
}
