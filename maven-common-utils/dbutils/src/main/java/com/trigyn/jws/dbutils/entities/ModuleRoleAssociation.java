package com.trigyn.jws.dbutils.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "module_role_association")
@NamedQuery(name = "ModuleRoleAssociation.findAll", query = "SELECT m FROM ModuleRoleAssociation m")
public class ModuleRoleAssociation implements Serializable {
	private static final long	serialVersionUID	= 1L;

	@Id
	@Column(name = "role_id", unique = true, nullable = false, length = 50)
	private String				roleId				= null;

	@Column(name = "module_id")
	private String				moduleId			= null;

	@Column(name = "updated_by")
	private String				updatedBy			= null;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_date")
	private Date				updatedDate			= null;

	@Column(name = "is_deleted")
	private Integer				isDeleted			= 0;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "module_id", insertable = false, updatable = false)
	private ModuleListing		moduleListing;

	public ModuleRoleAssociation() {
	}

	public ModuleRoleAssociation(String roleId, String moduleId, String updatedBy, Date updatedDate, Integer isDeleted,
			ModuleListing moduleListing) {
		super();
		this.roleId			= roleId;
		this.moduleId		= moduleId;
		this.updatedBy		= updatedBy;
		this.updatedDate	= updatedDate;
		this.isDeleted		= isDeleted;
		this.moduleListing	= moduleListing;
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
	 * @return the moduleId
	 */
	public String getModuleId() {
		return moduleId;
	}

	/**
	 * @param moduleId the moduleId to set
	 */
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	/**
	 * @return the updatedBy
	 */
	public String getUpdatedBy() {
		return updatedBy;
	}

	/**
	 * @param updatedBy the updatedBy to set
	 */
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	/**
	 * @return the updatedDate
	 */
	public Date getUpdatedDate() {
		return updatedDate;
	}

	/**
	 * @param updatedDate the updatedDate to set
	 */
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	/**
	 * @return the isDeleted
	 */
	public Integer getIsDeleted() {
		return isDeleted;
	}

	/**
	 * @param isDeleted the isDeleted to set
	 */
	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	/**
	 * @return the moduleListing
	 */
	public ModuleListing getModuleListing() {
		return moduleListing;
	}

	/**
	 * @param moduleListing the moduleListing to set
	 */
	public void setModuleListing(ModuleListing moduleListing) {
		this.moduleListing = moduleListing;
	}

	@Override
	public int hashCode() {
		return Objects.hash(isDeleted, moduleId, moduleListing, roleId, updatedBy, updatedDate);
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
		ModuleRoleAssociation other = (ModuleRoleAssociation) obj;
		return Objects.equals(isDeleted, other.isDeleted) && Objects.equals(moduleId, other.moduleId)
				&& Objects.equals(moduleListing, other.moduleListing) && Objects.equals(roleId, other.roleId)
				&& Objects.equals(updatedBy, other.updatedBy) && Objects.equals(updatedDate, other.updatedDate);
	}

	@Override
	public String toString() {
		return "ModuleRoleAssociation [roleId=" + roleId + ", moduleId=" + moduleId + ", updatedBy=" + updatedBy
				+ ", updatedDate=" + updatedDate + ", isDeleted=" + isDeleted + ", moduleListing=" + moduleListing
				+ "]";
	}

	public ModuleRoleAssociation getObject() {
		ModuleRoleAssociation moduleRoleAssociation = new ModuleRoleAssociation();
		moduleRoleAssociation.setRoleId(roleId != null ? roleId.trim() : roleId);
		moduleRoleAssociation.setModuleId(moduleId != null ? moduleId.trim() : moduleId);
		moduleRoleAssociation.setUpdatedBy(updatedBy != null ? updatedBy.trim() : updatedBy);
		moduleRoleAssociation.setUpdatedDate(updatedDate);
		moduleRoleAssociation.setIsDeleted(isDeleted);

		return moduleRoleAssociation;
	}

}