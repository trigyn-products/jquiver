package com.trigyn.jws.dbutils.vo;

import java.io.Serializable;
import java.util.Objects;

public class UserRoleVO implements Serializable {

	private static final long	serialVersionUID	= -5698385393824051258L;

	private String				roleId				= null;
	private String				roleName			= null;
	private String				roleDescription		= null;

	public UserRoleVO() {

	}

	public UserRoleVO(String roleId, String roleName, String roleDescription) {
		this.roleId				= roleId;
		this.roleName			= roleName;
		this.roleDescription	= roleDescription;
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
	 * @return the roleName
	 */
	public String getRoleName() {
		return roleName;
	}

	/**
	 * @param roleName the roleName to set
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/**
	 * @return the roleDescription
	 */
	public String getRoleDescription() {
		return roleDescription;
	}

	/**
	 * @param roleDescription the roleDescription to set
	 */
	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}

	@Override
	public int hashCode() {
		return Objects.hash(roleDescription, roleId, roleName);
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
		UserRoleVO other = (UserRoleVO) obj;
		return Objects.equals(roleDescription, other.roleDescription) && Objects.equals(roleId, other.roleId)
				&& Objects.equals(roleName, other.roleName);
	}

	@Override
	public String toString() {
		return "UserRoleVO [roleId=" + roleId + ", roleName=" + roleName + ", roleDescription=" + roleDescription + "]";
	}

}
