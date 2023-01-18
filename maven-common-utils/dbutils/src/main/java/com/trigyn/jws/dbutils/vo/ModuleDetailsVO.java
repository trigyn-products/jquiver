package com.trigyn.jws.dbutils.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class ModuleDetailsVO implements Serializable {

	private static final long	serialVersionUID	= -671156919183570354L;

	private String				moduleId			= null;
	private String				moduleName			= null;
	private String				moduleURL			= null;
	private String				parentModuleId		= null;
	private String				parentModuleName	= null;
	private Integer				sequence			= null;
	private Integer				isInsideMenu		= null;
	private Integer				includeLayout		= null;
	private Integer				targetLookupId		= null;
	private String				targetLookupDesc	= null;
	private String				targetLookupName	= null;
	private String				targetTypeId		= null;
	private Long				subModuleCount		= null;
	private String				headerJson			= null;
	private List<String>		roleIdList			= null;
	private String				requestParamJson	= null;
	private String				externalURL			= null;
	private Integer				openInNewTab		= null;
	private String				menuStyle			= null;

	public ModuleDetailsVO() {

	}

	public ModuleDetailsVO(String moduleId, String moduleName, String moduleURL, String parentModuleId, String parentModuleName,
			Integer sequence, Integer isInsideMenu, Integer includeLayout, Integer targetLookupId, String targetLookupDesc,
			String targetTypeId, String headerJson, String requestParamJson, Integer openInNewTab, String menuStyle) {
		this.moduleId			= moduleId;
		this.moduleName			= moduleName;
		this.moduleURL			= moduleURL;
		this.parentModuleId		= parentModuleId;
		this.parentModuleName	= parentModuleName;
		this.sequence			= sequence;
		this.isInsideMenu		= isInsideMenu;
		this.includeLayout		= includeLayout;
		this.targetLookupId		= targetLookupId;
		this.targetLookupDesc	= targetLookupDesc;
		this.targetTypeId		= targetTypeId;
		this.headerJson			= headerJson;
		this.requestParamJson 	= requestParamJson;
		this.openInNewTab 		= openInNewTab;
		this.menuStyle 			= menuStyle;
	}

	public ModuleDetailsVO(String moduleId, String moduleName, String moduleURL) {
		this.moduleId	= moduleId;
		this.moduleName	= moduleName;
		this.moduleURL	= moduleURL;
	}

	public ModuleDetailsVO(String moduleId, String moduleName, String moduleURL, String parentModuleId, String parentModuleName,
			Integer sequence, Long subModuleCount) {
		this.moduleId			= moduleId;
		this.moduleName			= moduleName;
		this.moduleURL			= moduleURL;
		this.parentModuleId		= parentModuleId;
		this.parentModuleName	= parentModuleName;
		this.sequence			= sequence;
		this.subModuleCount		= subModuleCount;
	}

	public ModuleDetailsVO(String moduleURL, Integer targetLookupId, String targetTypeId, Integer includeLayout, String headerJson, String requestParamJson) {
		this.moduleURL			= moduleURL;
		this.targetLookupId		= targetLookupId;
		this.targetTypeId		= targetTypeId;
		this.includeLayout		= includeLayout;
		this.headerJson			= headerJson;
		this.requestParamJson 	= requestParamJson;
	}
	
	public ModuleDetailsVO(String moduleId, String moduleName, String moduleURL, String parentModuleId, String parentModuleName,
			Integer sequence, Long subModuleCount, String targetTypeId, Integer targetLookupId, String requestParamJson, Integer openInNewTab, String menuStyle) {
		this.moduleId			= moduleId;
		this.moduleName			= moduleName;
		this.moduleURL			= moduleURL;
		this.parentModuleId		= parentModuleId;
		this.parentModuleName	= parentModuleName;
		this.sequence			= sequence;
		this.subModuleCount		= subModuleCount;
		this.targetTypeId		= targetTypeId;
		this.targetLookupId		= targetLookupId;
		this.requestParamJson 	= requestParamJson;
		this.openInNewTab		= openInNewTab;
		this.menuStyle 			= menuStyle;
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

	public String getModuleURL() {
		return moduleURL;
	}

	public void setModuleURL(String moduleURL) {
		this.moduleURL = moduleURL;
	}

	public String getParentModuleId() {
		return parentModuleId;
	}

	public void setParentId(String parentModuleId) {
		this.parentModuleId = parentModuleId;
	}

	public String getParentModuleName() {
		return parentModuleName;
	}

	public void setParentModuleName(String parentModuleName) {
		this.parentModuleName = parentModuleName;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Integer getIsInsideMenu() {
		return isInsideMenu;
	}

	public void setIsInsideMenu(Integer isInsideMenu) {
		this.isInsideMenu = isInsideMenu;
	}

	public Integer getIncludeLayout() {
		return includeLayout;
	}

	public void setIncludeLayout(Integer includeLayout) {
		this.includeLayout = includeLayout;
	}

	public Integer getTargetLookupId() {
		return targetLookupId;
	}

	public void setTargetLookupId(Integer targetLookupId) {
		this.targetLookupId = targetLookupId;
	}

	public String getTargetLookupDesc() {
		return targetLookupDesc;
	}

	public void setTargetLookupDesc(String targetLookupDesc) {
		this.targetLookupDesc = targetLookupDesc;
	}

	public String getTargetTypeId() {
		return targetTypeId;
	}

	public void setTargetTypeId(String targetTypeId) {
		this.targetTypeId = targetTypeId;
	}

	public Long getSubModuleCount() {
		return subModuleCount;
	}

	public void setSubModuleCount(Long subModuleCount) {
		this.subModuleCount = subModuleCount;
	}

	/**
	 * @return the roleIdList
	 */
	public List<String> getRoleIdList() {
		return roleIdList;
	}

	/**
	 * @param roleIdList the roleIdList to set
	 */
	public void setRoleIdList(List<String> roleIdList) {
		this.roleIdList = roleIdList;
	}

	public String getTargetLookupName() {
		return targetLookupName;
	}

	public void setTargetLookupName(String targetLookupName) {
		this.targetLookupName = targetLookupName;
	}

	public void setParentModuleId(String parentModuleId) {
		this.parentModuleId = parentModuleId;
	}

	public String getHeaderJson() {
		return headerJson;
	}

	public void setHeaderJson(String headerJson) {
		this.headerJson = headerJson;
	}

	public String getRequestParamJson() {
		return requestParamJson;
	}

	public void setRequestParamJson(String requestParamJson) {
		this.requestParamJson = requestParamJson;
	}
	
	public String getExternalURL() {
		return externalURL;
	}

	public void setExternalURL(String externalURL) {
		this.externalURL = externalURL;
	}

	@Override
	public int hashCode() {
		return Objects.hash(headerJson, includeLayout, isInsideMenu, moduleId, moduleName, moduleURL, parentModuleId, parentModuleName,
				roleIdList, sequence, subModuleCount, targetLookupDesc, targetLookupId, targetLookupName, targetTypeId);
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
		ModuleDetailsVO other = (ModuleDetailsVO) obj;
		return Objects.equals(headerJson, other.headerJson) && Objects.equals(includeLayout, other.includeLayout)
				&& Objects.equals(isInsideMenu, other.isInsideMenu) && Objects.equals(moduleId, other.moduleId)
				&& Objects.equals(moduleName, other.moduleName) && Objects.equals(moduleURL, other.moduleURL)
				&& Objects.equals(parentModuleId, other.parentModuleId) && Objects.equals(parentModuleName, other.parentModuleName)
				&& Objects.equals(roleIdList, other.roleIdList) && Objects.equals(sequence, other.sequence)
				&& Objects.equals(subModuleCount, other.subModuleCount) && Objects.equals(targetLookupDesc, other.targetLookupDesc)
				&& Objects.equals(targetLookupId, other.targetLookupId) && Objects.equals(targetLookupName, other.targetLookupName)
				&& Objects.equals(targetTypeId, other.targetTypeId);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ModuleDetailsVO [moduleId=").append(moduleId).append(", moduleName=").append(moduleName).append(", moduleURL=")
				.append(moduleURL).append(", parentModuleId=").append(parentModuleId).append(", parentModuleName=").append(parentModuleName)
				.append(", sequence=").append(sequence).append(", isInsideMenu=").append(isInsideMenu).append(", includeLayout=")
				.append(includeLayout).append(", targetLookupId=").append(targetLookupId).append(", targetLookupDesc=")
				.append(targetLookupDesc).append(", targetLookupName=").append(targetLookupName).append(", targetTypeId=")
				.append(targetTypeId).append(", subModuleCount=").append(subModuleCount).append(", headerJson=").append(headerJson)
				.append(", roleIdList=").append(roleIdList).append("]");
		return builder.toString();
	}

	public Integer getOpenInNewTab() {
		return openInNewTab;
	}

	public void setOpenInNewTab(Integer openInNewTab) {
		this.openInNewTab = openInNewTab;
	}

	public String getMenuStyle() {
		return menuStyle;
	}

	public void setMenuStyle(String menuStyle) {
		this.menuStyle = menuStyle;
	}
}
