package com.trigyn.jws.usermanagement.vo;

import java.util.Objects;

public class SiteLayoutVO {

	private String	moduleName	= null;
	private String	moduleUrl	= null;
	private Long	roleCount	= null;

	public SiteLayoutVO() {

	}

	public SiteLayoutVO(String moduleUrl, Long roleCount) {
		this.moduleUrl	= moduleUrl;
		this.roleCount	= roleCount;
	}

	public SiteLayoutVO(String moduleName, String moduleUrl) {
		this.moduleName	= moduleName;
		this.moduleUrl	= moduleUrl;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getModuleUrl() {
		return moduleUrl;
	}

	public void setModuleUrl(String moduleUrl) {
		this.moduleUrl = moduleUrl;
	}

	public Long getRoleCount() {
		return roleCount;
	}

	public void setRoleCount(Long roleCount) {
		this.roleCount = roleCount;
	}

	@Override
	public int hashCode() {
		return Objects.hash(moduleName, moduleUrl, roleCount);
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
		SiteLayoutVO other = (SiteLayoutVO) obj;
		return Objects.equals(moduleName, other.moduleName) && Objects.equals(moduleUrl, other.moduleUrl)
				&& Objects.equals(roleCount, other.roleCount);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SiteLayoutVO [moduleName=").append(moduleName).append(", moduleUrl=").append(moduleUrl).append(", roleCount=")
				.append(roleCount).append("]");
		return builder.toString();
	}

}
