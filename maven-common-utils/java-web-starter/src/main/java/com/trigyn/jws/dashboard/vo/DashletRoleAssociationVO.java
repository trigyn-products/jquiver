package com.trigyn.jws.dashboard.vo;

import java.io.Serializable;
import java.util.Objects;

public class DashletRoleAssociationVO implements Serializable {

	private static final long	serialVersionUID	= -3135097488170334107L;

	private String				roleId				= null;

	private String				dashletId			= null;

	public DashletRoleAssociationVO() {

	}

	public DashletRoleAssociationVO(String roleId, String dashletId) {
		this.roleId		= roleId;
		this.dashletId	= dashletId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getDashletId() {
		return dashletId;
	}

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
		DashletRoleAssociationVO other = (DashletRoleAssociationVO) obj;
		return Objects.equals(dashletId, other.dashletId) && Objects.equals(roleId, other.roleId);
	}

	@Override
	public String toString() {
		return "DashletRoleAssociationVO [roleId=" + roleId + ", dashletId=" + dashletId + "]";
	}

}
