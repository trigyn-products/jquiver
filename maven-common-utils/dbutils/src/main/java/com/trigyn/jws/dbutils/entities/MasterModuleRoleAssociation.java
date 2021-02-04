package com.trigyn.jws.dbutils.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "master_module_role_association")
@NamedQuery(name = "MasterModuleRoleAssociation.findAll", query = "SELECT m FROM MasterModuleRoleAssociation m")
public class MasterModuleRoleAssociation implements Serializable {

	private static final long				serialVersionUID	= 1L;

	@EmbeddedId
	private MasterModuleRoleAssociationPK	id					= null;

	@ManyToOne
	@JoinColumn(name = "module_id", nullable = false, insertable = false, updatable = false)
	private ModuleListing					moduleListing		= null;

	@ManyToOne
	@JoinColumn(name = "role_id", nullable = false, insertable = false, updatable = false)
	private UserRole						userRole			= null;

	public MasterModuleRoleAssociation() {
	}

	public MasterModuleRoleAssociation(MasterModuleRoleAssociationPK id, UserRole userRole) {
		this.id			= id;
		this.userRole	= userRole;
	}

	public MasterModuleRoleAssociationPK getId() {
		return id;
	}

	public void setId(MasterModuleRoleAssociationPK id) {
		this.id = id;
	}

	public ModuleListing getModuleListing() {
		return moduleListing;
	}

	public void setModuleListing(ModuleListing moduleListing) {
		this.moduleListing = moduleListing;
	}

	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, moduleListing, userRole);
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
		MasterModuleRoleAssociation other = (MasterModuleRoleAssociation) obj;
		return Objects.equals(id, other.id) && Objects.equals(moduleListing, other.moduleListing)
				&& Objects.equals(userRole, other.userRole);
	}

	@Override
	public String toString() {
		return "MasterModuleRoleAssociation [id=" + id + ", moduleListing=" + moduleListing + ", userRole=" + userRole
				+ "]";
	}

}