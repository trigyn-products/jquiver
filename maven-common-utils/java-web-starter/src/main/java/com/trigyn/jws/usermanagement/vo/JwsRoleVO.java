package com.trigyn.jws.usermanagement.vo;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.trigyn.jws.usermanagement.entities.JwsRole;

public class JwsRoleVO implements Serializable {

	private static final long	serialVersionUID	= -879130649799270177L;

	private String				roleId				= null;

	private String				roleName			= null;

	private String				roleDescription		= null;

	private Integer				isActive			= null;

	private Integer				rolePriority		= null;

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

	public JwsRole convertVOToEntity(JwsRoleVO jwsRoleVO) {
		JwsRole jwsRole = new JwsRole();
		jwsRole.setRoleId(StringUtils.isNotEmpty(jwsRoleVO.getRoleId()) ? jwsRoleVO.getRoleId() : null);
		jwsRole.setRoleName(jwsRoleVO.getRoleName());
		jwsRole.setRoleDescription(jwsRoleVO.getRoleDescription());
		jwsRole.setIsActive(jwsRoleVO.getIsActive());
		jwsRole.setRolePriority(jwsRoleVO.getRolePriority());
		return jwsRole;
	}

	public JwsRoleVO() {
	}

	public JwsRoleVO(String roleId, String roleName) {
		super();
		this.roleId		= roleId;
		this.roleName	= roleName;
	}

}
