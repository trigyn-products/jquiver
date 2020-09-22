package com.trigyn.jws.menu.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the module_role_association database table.
 * 
 */
@Embeddable
public class ModuleRoleAssociationPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="module_id", insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String moduleId;

	@Column(name="role_id", unique=true, nullable=false, length=100)
	private String roleId;

	public ModuleRoleAssociationPK() {
	}
	public String getModuleId() {
		return this.moduleId;
	}
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}
	public String getRoleId() {
		return this.roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ModuleRoleAssociationPK)) {
			return false;
		}
		ModuleRoleAssociationPK castOther = (ModuleRoleAssociationPK)other;
		return 
			this.moduleId.equals(castOther.moduleId)
			&& this.roleId.equals(castOther.roleId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.moduleId.hashCode();
		hash = hash * prime + this.roleId.hashCode();
		
		return hash;
	}
}