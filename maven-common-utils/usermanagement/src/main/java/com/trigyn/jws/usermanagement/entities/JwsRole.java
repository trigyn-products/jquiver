package com.trigyn.jws.usermanagement.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import com.trigyn.jws.usermanagement.vo.JwsRoleVO;

@Entity
@Table(name = "jws_role")
public class JwsRole {

	@Id
	@GeneratedValue(generator = "inquisitive-uuid")
	@GenericGenerator(name = "inquisitive-uuid", strategy = "com.trigyn.jws.dbutils.configurations.CustomUUIDGenerator")
	@Column(name = "role_id")
	private String									roleId							= null;

	@Column(name = "role_name")
	private String									roleName						= null;

	@Column(name = "role_description")
	private String									roleDescription					= null;

	@Column(name = "is_active")
	private Integer									isActive						= null;

	@Column(name = "role_priority")
	private Integer									rolePriority					= null;

	@OneToMany(mappedBy = "role")
	private List<JwsRoleMasterModulesAssociation>	jwsRoleMasterModulesAssociation	= null;

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

	/**
	 * @return the rolePriority
	 */
	public Integer getRolePriority() {
		return rolePriority;
	}

	/**
	 * @param rolePriority the rolePriority to set
	 */
	public void setRolePriority(Integer rolePriority) {
		this.rolePriority = rolePriority;
	}

	public List<JwsRoleMasterModulesAssociation> getJwsRoleMasterModulesAssociation() {
		return jwsRoleMasterModulesAssociation;
	}

	public void setJwsRoleMasterModulesAssociation(
			List<JwsRoleMasterModulesAssociation> jwsRoleMasterModulesAssociation) {
		this.jwsRoleMasterModulesAssociation = jwsRoleMasterModulesAssociation;
	}

	public JwsRole getObject() {
		JwsRole role = new JwsRole();
		role.setRoleId(roleId != null ? roleId.trim() : roleId);
		role.setRoleName(roleName != null ? roleName.trim() : roleName);
		role.setRoleDescription(roleDescription != null ? roleDescription.trim() : roleDescription);
		role.setIsActive(isActive);

		List<JwsRoleMasterModulesAssociation> jrmmaOthr = new ArrayList<>();
		if (jwsRoleMasterModulesAssociation != null && !jwsRoleMasterModulesAssociation.isEmpty()) {
			for (JwsRoleMasterModulesAssociation jrmma : jwsRoleMasterModulesAssociation) {
				jrmmaOthr.add(jrmma.getObject());
			}
			role.setJwsRoleMasterModulesAssociation(jrmmaOthr);
		} else
			role.setJwsRoleMasterModulesAssociation(null);
		return role;
	}

	public JwsRoleVO convertEntityToVO(JwsRole jwsRole) {
		JwsRoleVO jwsRoleVO = new JwsRoleVO();
		jwsRoleVO.setRoleId(StringUtils.isNotEmpty(jwsRole.getRoleId()) ? jwsRole.getRoleId() : null);
		jwsRoleVO.setRoleName(jwsRole.getRoleName());
		jwsRoleVO.setRoleDescription(jwsRole.getRoleDescription());
		jwsRoleVO.setIsActive(jwsRole.getIsActive());
		jwsRoleVO.setRolePriority(jwsRole.getRolePriority());
		return jwsRoleVO;
	}

}
