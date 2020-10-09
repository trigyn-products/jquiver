package com.trigyn.jws.usermanagement.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "jws_role_master_modules_association")
public class JwsRoleMasterModulesAssociation {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name="role_module_id")
	private String roleModuleId = null;
	
	@Column(name="role_id")
	private String roleId = null;
	
	@Column(name="module_id")
	private String moduleId = null;
	
	@Column(name="is_active")
	private Integer isActive = null;
	
	@ManyToOne
	@JoinColumn(name = "role_id", referencedColumnName = "role_id", insertable = false, updatable = false )
	private JwsRole role = null;
	
	@ManyToOne
	@JoinColumn(name = "module_id", referencedColumnName = "module_id", insertable = false, updatable = false )
	private JwsMasterModules module = null;
	
	public String getRoleModuleId() {
		return roleModuleId;
	}

	public void setRoleModuleId(String roleModuleId) {
		this.roleModuleId = roleModuleId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public JwsRole getRole() {
		return role;
	}

	public void setRole(JwsRole role) {
		this.role = role;
	}

	public JwsMasterModules getModule() {
		return module;
	}

	public void setModules(JwsMasterModules module) {
		this.module = module;
	}
	
	
	
}
