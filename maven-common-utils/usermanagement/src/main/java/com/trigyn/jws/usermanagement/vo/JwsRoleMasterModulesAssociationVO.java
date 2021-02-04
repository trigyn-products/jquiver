package com.trigyn.jws.usermanagement.vo;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.trigyn.jws.usermanagement.entities.JwsRoleMasterModulesAssociation;

public class JwsRoleMasterModulesAssociationVO implements Serializable {

	private static final long	serialVersionUID	= -3833672140237932660L;

	private String				roleModuleId		= null;

	private String				roleId				= null;

	private String				moduleId			= null;

	private Integer				isActive			= null;

	private Integer				moduleTypeId		= null;

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

	public JwsRoleMasterModulesAssociation convertVOToEntity(
			JwsRoleMasterModulesAssociationVO jwsRoleMasterModulesAssociationVO) {

		JwsRoleMasterModulesAssociation masterModuleAssociation = new JwsRoleMasterModulesAssociation();

		masterModuleAssociation
				.setRoleModuleId(StringUtils.isNotEmpty(jwsRoleMasterModulesAssociationVO.getRoleModuleId())
						? jwsRoleMasterModulesAssociationVO.getRoleModuleId()
						: null);
		masterModuleAssociation.setRoleId(jwsRoleMasterModulesAssociationVO.getRoleId());
		masterModuleAssociation.setModuleId(jwsRoleMasterModulesAssociationVO.getModuleId());
		masterModuleAssociation.setIsActive(jwsRoleMasterModulesAssociationVO.getIsActive());

		return masterModuleAssociation;
	}

	public Integer getModuleTypeId() {
		return moduleTypeId;
	}

	public void setModuleTypeId(Integer moduleTypeId) {
		this.moduleTypeId = moduleTypeId;
	}

}
