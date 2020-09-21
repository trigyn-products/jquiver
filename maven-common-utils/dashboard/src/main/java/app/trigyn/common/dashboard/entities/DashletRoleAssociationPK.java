package app.trigyn.common.dashboard.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class DashletRoleAssociationPK implements Serializable {

	private static final long serialVersionUID = -6921994896941395278L;

	@Column(name = "role_id")
	private String				roleId				= null;

	@Column(name = "dashlet_id")
	private String				dashletId			= null;

	public DashletRoleAssociationPK() {
		
	}
	
	public DashletRoleAssociationPK(String roleId, String dashletId) {
		this.roleId 	= roleId;
		this.dashletId 	= dashletId;
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

	@Override
	public int hashCode() {
		return Objects.hash(dashletId, roleId);
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
		DashletRoleAssociationPK other = (DashletRoleAssociationPK) obj;
		return Objects.equals(dashletId, other.dashletId) && Objects.equals(roleId, other.roleId);
	}

	@Override
	public String toString() {
		return "DashletRoleAssociationPK [roleId=" + roleId + ", dashletId=" + dashletId + "]";
	}


	
	
}
