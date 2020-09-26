package com.trigyn.jws.menu.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class ModuleDetailsVO implements Serializable{

	private static final long serialVersionUID 		= -671156919183570354L;
	
	private String moduleId							= null;
	private String moduleName						= null;
	private String moduleURL						= null;
	private String parentModuleId					= null;
	private String parentModuleName					= null;
	private Integer sequence						= null;
	private Integer targetLookupId					= null;
	private String targetLookupDesc					= null;
	private String targetTypeId						= null;
	private Long subModuleCount						= null;
	private List<String> roleIdList					= null;
	
	public ModuleDetailsVO() {
		
	}

	public ModuleDetailsVO(String moduleId, String moduleName, String moduleURL, String parentModuleId,String parentModuleName
			, Integer sequence, Integer targetLookupId, String targetLookupDesc, String targetTypeId) {
		this.moduleId 				= moduleId;
		this.moduleName 			= moduleName;
		this.moduleURL 				= moduleURL;
		this.parentModuleId 		= parentModuleId;
		this.parentModuleName 		= parentModuleName;
		this.sequence 				= sequence;
		this.targetLookupId 		= targetLookupId;
		this.targetLookupDesc 		= targetLookupDesc;
		this.targetTypeId 			= targetTypeId;
	}

	public ModuleDetailsVO(String moduleId, String moduleName) {
		this.moduleId 	= moduleId;
		this.moduleName = moduleName;
	}
	

	public ModuleDetailsVO(String moduleId, String moduleName, String moduleURL, String parentModuleId,
			String parentModuleName, Integer sequence, Long subModuleCount) {
		this.moduleId 			= moduleId;
		this.moduleName 		= moduleName;
		this.moduleURL 			= moduleURL;
		this.parentModuleId		= parentModuleId;
		this.parentModuleName 	= parentModuleName;
		this.sequence 			= sequence;
		this.subModuleCount 	= subModuleCount;
	}
	
	
	public ModuleDetailsVO(Integer targetLookupId, String targetTypeId) {
		this.targetLookupId = targetLookupId;
		this.targetTypeId = targetTypeId;
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

	@Override
	public int hashCode() {
		return Objects.hash(moduleId, moduleName, moduleURL, parentModuleId, parentModuleName, sequence, subModuleCount,
				targetLookupDesc, targetLookupId, targetTypeId);
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
		return Objects.equals(moduleId, other.moduleId) && Objects.equals(moduleName, other.moduleName)
				&& Objects.equals(moduleURL, other.moduleURL) && Objects.equals(parentModuleId, other.parentModuleId)
				&& Objects.equals(parentModuleName, other.parentModuleName) && Objects.equals(sequence, other.sequence)
				&& Objects.equals(subModuleCount, other.subModuleCount)
				&& Objects.equals(targetLookupDesc, other.targetLookupDesc)
				&& Objects.equals(targetLookupId, other.targetLookupId)
				&& Objects.equals(targetTypeId, other.targetTypeId);
	}

	@Override
	public String toString() {
		return "ModuleDetailsVO [moduleId=" + moduleId + ", moduleName=" + moduleName + ", moduleURL=" + moduleURL
				+ ", parentModuleId=" + parentModuleId + ", parentModuleName=" + parentModuleName + ", sequence=" + sequence
				+ ", targetLookupId=" + targetLookupId + ", targetLookupDesc=" + targetLookupDesc + ", targetTypeId="
				+ targetTypeId + ", subModuleCount=" + subModuleCount + "]";
	}

	
}
