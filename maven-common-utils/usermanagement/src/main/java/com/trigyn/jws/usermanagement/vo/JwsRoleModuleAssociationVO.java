package com.trigyn.jws.usermanagement.vo;

public class JwsRoleModuleAssociationVO {

	private String	roleId		= null;

	private String	roleName	= null;

	private String	moduleId	= null;

	private String	moduleName	= null;

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

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public JwsRoleModuleAssociationVO() {
	}

	public JwsRoleModuleAssociationVO(String roleId, String roleName, String moduleId, String moduleName) {
		super();
		this.roleId		= roleId;
		this.roleName	= roleName;
		this.moduleId	= moduleId;
		this.moduleName	= moduleName;
	}

}
