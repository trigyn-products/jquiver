package com.trigyn.jws.dbutils.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "user_role")
@NamedQuery(name = "UserRole.findAll", query = "SELECT u FROM UserRole u")
public class UserRole implements Serializable {
	private static final long	serialVersionUID	= 1L;

	@Id
	@GeneratedValue(generator = "inquisitive-uuid")
	@GenericGenerator(name = "inquisitive-uuid", strategy = "com.trigyn.jws.dbutils.configurations.CustomUUIDGenerator")
	@Column(name = "role_id", unique = true, nullable = false)
	private String				roleId				= null;

	@Column(name = "is_deleted")
	private Integer				isDeleted			= null;

	@Column(name = "role_description")
	private String				roleDescription		= null;

	@Column(name = "role_name", nullable = false)
	private String				roleName			= null;

	public UserRole() {

	}

	public UserRole(String roleId, Integer isDeleted, String roleDescription, String roleName) {
		this.roleId				= roleId;
		this.isDeleted			= isDeleted;
		this.roleDescription	= roleDescription;
		this.roleName			= roleName;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getRoleDescription() {
		return roleDescription;
	}

	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Override
	public int hashCode() {
		return Objects.hash(isDeleted, roleDescription, roleId, roleName);
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
		UserRole other = (UserRole) obj;
		return Objects.equals(isDeleted, other.isDeleted) && Objects.equals(roleDescription, other.roleDescription)
				&& Objects.equals(roleId, other.roleId) && Objects.equals(roleName, other.roleName);
	}

	@Override
	public String toString() {
		return "UserRole [roleId=" + roleId + ", isDeleted=" + isDeleted + ", roleDescription=" + roleDescription
				+ ", roleName=" + roleName + "]";
	}

	public UserRole getObject() {
		UserRole obj = new UserRole();

		obj.setIsDeleted(isDeleted);
		obj.setRoleDescription(roleDescription != null ? roleDescription.trim() : roleDescription);
		obj.setRoleId(roleId != null ? roleId.trim() : roleId);
		obj.setRoleName(roleName != null ? roleName.trim() : roleName);

		return obj;
	}

}