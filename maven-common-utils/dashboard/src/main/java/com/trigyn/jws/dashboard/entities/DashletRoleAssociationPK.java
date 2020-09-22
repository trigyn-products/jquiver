package com.trigyn.jws.dashboard.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.*;


@Embeddable
public class DashletRoleAssociationPK implements Serializable {
	
	private static final long serialVersionUID 		= 1L;

	@Column(name="dashlet_id", insertable=false, updatable=false, nullable=false)
	private String dashletId						= null;

	@Column(name="role_id", insertable=false, updatable=false, nullable=false)
	private String roleId							= null;

	public DashletRoleAssociationPK() {
		
	}

	public DashletRoleAssociationPK(String dashletId, String roleId) {
		this.dashletId 		= dashletId;
		this.roleId 		= roleId;
	}

	
	public String getDashletId() {
		return dashletId;
	}

	
	public void setDashletId(String dashletId) {
		this.dashletId = dashletId;
	}

	
	public String getRoleId() {
		return roleId;
	}

	
	public void setRoleId(String roleId) {
		this.roleId = roleId;
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
		return "DashletRoleAssociationPK [dashletId=" + dashletId + ", roleId=" + roleId + "]";
	}
	
	
}