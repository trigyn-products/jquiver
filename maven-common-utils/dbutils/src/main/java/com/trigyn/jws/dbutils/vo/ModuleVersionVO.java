package com.trigyn.jws.dbutils.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class ModuleVersionVO implements Serializable {

	private static final long	serialVersionUID	= -6331574183601298560L;

	private String				moduleVersionId		= null;

	private String				entityId			= null;

	private String				entityName			= null;

	private String				parentEntityId		= null;

	private String				moduleJson			= null;

	private Double				versionId			= null;

	private Date				updatedDate			= null;

	private String				moduleJsonChecksum	= null;

	private Integer				sourceTypeId		= null;

	public ModuleVersionVO() {

	}

	public ModuleVersionVO(String moduleVersionId, String entityId, String moduleJson, Double versionId, Date updatedDate,
			String moduleJsonChecksum, Integer sourceTypeId) {
		this.moduleVersionId	= moduleVersionId;
		this.entityId			= entityId;
		this.moduleJson			= moduleJson;
		this.versionId			= versionId;
		this.updatedDate		= updatedDate;
		this.moduleJsonChecksum	= moduleJsonChecksum;
		this.sourceTypeId		= sourceTypeId;
	}

	/**
	 * @return the moduleVersionId
	 */
	public String getModuleVersionId() {
		return moduleVersionId;
	}

	/**
	 * @param moduleVersionId the moduleVersionId to set
	 */
	public void setModuleVersionId(String moduleVersionId) {
		this.moduleVersionId = moduleVersionId;
	}

	/**
	 * @return the entityId
	 */
	public String getEntityId() {
		return entityId;
	}

	/**
	 * @param entityId the entityId to set
	 */
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	/**
	 * @return the entityName
	 */
	public String getEntityName() {
		return entityName;
	}

	/**
	 * @param entityName the entityName to set
	 */
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	/**
	 * @return the parentEntityId
	 */
	public String getParentEntityId() {
		return parentEntityId;
	}

	/**
	 * @param parentEntityId the parentEntityId to set
	 */
	public void setParentEntityId(String parentEntityId) {
		this.parentEntityId = parentEntityId;
	}

	/**
	 * @return the moduleJson
	 */
	public String getModuleJson() {
		return moduleJson;
	}

	/**
	 * @param moduleJson the moduleJson to set
	 */
	public void setModuleJson(String moduleJson) {
		this.moduleJson = moduleJson;
	}

	/**
	 * @return the versionId
	 */
	public Double getVersionId() {
		return versionId;
	}

	/**
	 * @param versionId the versionId to set
	 */
	public void setVersionId(Double versionId) {
		this.versionId = versionId;
	}

	/**
	 * @return the moduleJsonChecksum
	 */
	public String getModuleJsonChecksum() {
		return moduleJsonChecksum;
	}

	/**
	 * @param moduleJsonChecksum the moduleJsonChecksum to set
	 */
	public void setModuleJsonChecksum(String moduleJsonChecksum) {
		this.moduleJsonChecksum = moduleJsonChecksum;
	}

	/**
	 * @return the updatedDate
	 */
	public Date getUpdatedDate() {
		return updatedDate;
	}

	/**
	 * @param updatedDate the updatedDate to set
	 */
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	/**
	 * @return the sourceTypeId
	 */
	public Integer getSourceTypeId() {
		return sourceTypeId;
	}

	/**
	 * @param sourceTypeId the sourceTypeId to set
	 */
	public void setSourceTypeId(Integer sourceTypeId) {
		this.sourceTypeId = sourceTypeId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(entityId, entityName, moduleJson, moduleJsonChecksum, moduleVersionId, parentEntityId, sourceTypeId,
				updatedDate, versionId);
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
		ModuleVersionVO other = (ModuleVersionVO) obj;
		return Objects.equals(entityId, other.entityId) && Objects.equals(entityName, other.entityName)
				&& Objects.equals(moduleJson, other.moduleJson) && Objects.equals(moduleJsonChecksum, other.moduleJsonChecksum)
				&& Objects.equals(moduleVersionId, other.moduleVersionId) && Objects.equals(parentEntityId, other.parentEntityId)
				&& Objects.equals(sourceTypeId, other.sourceTypeId) && Objects.equals(updatedDate, other.updatedDate)
				&& Objects.equals(versionId, other.versionId);
	}

	@Override
	public String toString() {
		return "ModuleVersionVO [moduleVersionId=" + moduleVersionId + ", entityId=" + entityId + ", entityName=" + entityName
				+ ", parentEntityId=" + parentEntityId + ", moduleJson=" + moduleJson + ", versionId=" + versionId + ", updatedDate="
				+ updatedDate + ", moduleJsonChecksum=" + moduleJsonChecksum + ", sourceTypeId=" + sourceTypeId + "]";
	}

}
