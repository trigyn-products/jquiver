package app.trigyn.common.dashboard.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class DashboardRoleAssociationPK implements Serializable{

	private static final long serialVersionUID 	= -3380978399263807260L;

	@Column(name = "role_id")
	private String		roleId					= null;

	@Column(name = "dashboard_id")
	private String		dashboardId				= null;

	public DashboardRoleAssociationPK() {
		
	}
	
	public DashboardRoleAssociationPK(String roleId, String dashboardId) {
		this.roleId 		= roleId;
		this.dashboardId 	= dashboardId;
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
		return "DashboardRoleAssociationPK [roleId=" + roleId + ", dashboardId=" + dashboardId + "]";
	}


	
}
