package com.trigyn.jws.usermanagement.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "jws_role")
public class JwsRole {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name="role_id")
	private String roleId = null;
	
	@Column(name="role_name")
	private String roleName = null;
	
	@Column(name="role_description")
	private String roleDescription = null;
	
	@Column(name="is_active")
	private Integer isActive = null;

	@OneToMany(mappedBy = "role")
	private List<JwsRoleMasterModulesAssociation> jwsRoleMasterModulesAssociation =  null;
	
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleDescription() {
		return roleDescription;
	}

	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public List<JwsRoleMasterModulesAssociation> getJwsRoleMasterModulesAssociation() {
		return jwsRoleMasterModulesAssociation;
	}

	public void setJwsRoleMasterModulesAssociation(List<JwsRoleMasterModulesAssociation> jwsRoleMasterModulesAssociation) {
		this.jwsRoleMasterModulesAssociation = jwsRoleMasterModulesAssociation;
	}

	public JwsRole getObject() {
		JwsRole role = new JwsRole();
		role.setRoleId(roleId);
		role.setRoleName(roleName);
		role.setRoleDescription(roleDescription);
		role.setIsActive(isActive);
		
		List<JwsRoleMasterModulesAssociation> jrmmaOthr = new ArrayList<>();
		for(JwsRoleMasterModulesAssociation jrmma : jwsRoleMasterModulesAssociation) {
			jrmmaOthr.add(jrmma.getObject());
		}
		role.setJwsRoleMasterModulesAssociation(jrmmaOthr);
		return role;
	}
}
