package com.trigyn.jws.dashboard.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.*;

/**
 * The primary key class for the dashboard_role_association database table.
 * 
 */
@Embeddable
public class DashboardRoleAssociationPK implements Serializable {

	private static final long serialVersionUID 			= 1L;

	@Column(name="dashboard_id", insertable=false, updatable=false, nullable=false)
	private String dashboardId							= null;

	@Column(name="role_id", insertable=false, updatable=false, nullable=false)
	private String roleId								= null;

	public DashboardRoleAssociationPK() {
		
	}
	
	public DashboardRoleAssociationPK(String dashboardId, String roleId) {
		this.dashboardId 	= dashboardId;
		this.roleId 		= roleId;
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
	 * @return the roleId
	 */
	public String getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dashboardId, roleId);
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
		DashboardRoleAssociationPK other = (DashboardRoleAssociationPK) obj;
		return Objects.equals(dashboardId, other.dashboardId) && Objects.equals(roleId, other.roleId);
	}

	@Override
	public String toString() {
		return "DashboardRoleAssociationPK [dashboardId=" + dashboardId + ", roleId=" + roleId + "]";
	}


}