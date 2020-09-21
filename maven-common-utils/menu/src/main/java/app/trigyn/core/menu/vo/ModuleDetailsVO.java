package app.trigyn.core.menu.vo;

import java.io.Serializable;
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
	
	public ModuleDetailsVO() {
		
	}

	public ModuleDetailsVO(String moduleId, String moduleName, String moduleURL, String parentModuleId,
			String parentModuleName, Integer sequence, Integer targetLookupId, String targetLookupDesc, String targetTypeId) {
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
	 * @return the moduleName
	 */
	public String getModuleName() {
		return moduleName;
	}

	/**
	 * @param moduleName the moduleName to set
	 */
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	/**
	 * @return the moduleURL
	 */
	public String getModuleURL() {
		return moduleURL;
	}

	/**
	 * @param moduleURL the moduleURL to set
	 */
	public void setModuleURL(String moduleURL) {
		this.moduleURL = moduleURL;
	}

	/**
	 * @return the parentModuleId
	 */
	public String getParentModuleId() {
		return parentModuleId;
	}

	/**
	 * @param parentModuleId the parentModuleId to set
	 */
	public void setParentId(String parentModuleId) {
		this.parentModuleId = parentModuleId;
	}

	/**
	 * @return the parentModuleName
	 */
	public String getParentModuleName() {
		return parentModuleName;
	}

	/**
	 * @param parentModuleName the parentModuleName to set
	 */
	public void setParentModuleName(String parentModuleName) {
		this.parentModuleName = parentModuleName;
	}

	/**
	 * @return the sequence
	 */
	public Integer getSequence() {
		return sequence;
	}

	/**
	 * @param sequence the sequence to set
	 */
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	/**
	 * @return the targetLookupId
	 */
	public Integer getTargetLookupId() {
		return targetLookupId;
	}

	/**
	 * @param targetLookupId the targetLookupId to set
	 */
	public void setTargetLookupId(Integer targetLookupId) {
		this.targetLookupId = targetLookupId;
	}

	/**
	 * @return the targetLookupDesc
	 */
	public String getTargetLookupDesc() {
		return targetLookupDesc;
	}

	/**
	 * @param targetLookupDesc the targetLookupDesc to set
	 */
	public void setTargetLookupDesc(String targetLookupDesc) {
		this.targetLookupDesc = targetLookupDesc;
	}

	/**
	 * @return the targetTypeId
	 */
	public String getTargetTypeId() {
		return targetTypeId;
	}

	/**
	 * @param targetTypeId the targetTypeId to set
	 */
	public void setTargetTypeId(String targetTypeId) {
		this.targetTypeId = targetTypeId;
	}

	/**
	 * @return the subModuleCount
	 */
	public Long getSubModuleCount() {
		return subModuleCount;
	}

	/**
	 * @param subModuleCount the subModuleCount to set
	 */
	public void setSubModuleCount(Long subModuleCount) {
		this.subModuleCount = subModuleCount;
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
