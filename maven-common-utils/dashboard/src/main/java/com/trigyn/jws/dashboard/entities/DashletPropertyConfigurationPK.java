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
	 * @return the propertyId
	 */
	public String getPropertyId() {
		return propertyId;
	}

	/**
	 * @param propertyId the propertyId to set
	 */
	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
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
