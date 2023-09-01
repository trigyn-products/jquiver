package com.trigyn.jws.dbutils.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MasterModuleRoleAssociationPK implements Serializable {

	private static final long	serialVersionUID	= 1L;

	@Column(name = "master_module_id", insertable = false, updatable = false, nullable = false)
	private String				masterModuleId		= null;

	@Column(name = "role_id", insertable = false, updatable = false, nullable = false)
	private String				roleId				= null;

	public MasterModuleRoleAssociationPK() {

	}

	public MasterModuleRoleAssociationPK(String masterModuleId, String roleId) {
		this.masterModuleId	= masterModuleId;
		this.roleId			= roleId;
	}

	public String getMasterModuleId() {
		return masterModuleId;
	}

	public void setMasterModuleId(String masterModuleId) {
		this.masterModuleId = masterModuleId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(masterModuleId, roleId);
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
		MasterModuleRoleAssociationPK other = (MasterModuleRoleAssociationPK) obj;
		return Objects.equals(masterModuleId, other.masterModuleId) && Objects.equals(roleId, other.roleId);
	}

	@Override
	public String toString() {
		return "MasterModuleRoleAssociationPK [masterModuleId=" + masterModuleId + ", roleId=" + roleId + "]";
	}

}