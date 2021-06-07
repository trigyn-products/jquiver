package com.trigyn.jws.dbutils.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "jq_module_version")
@NamedQuery(name = "JwsModuleVersion.findAll", query = "SELECT j FROM JwsModuleVersion j")
public class JwsModuleVersion implements Serializable {

	private static final long	serialVersionUID	= 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "module_version_id", unique = true, nullable = false)
	private String				moduleVersionId		= null;

	@Column(name = "entity_id", nullable = false)
	private String				entityId			= null;

	@Column(name = "entity_name", nullable = false)
	private String				entityName			= null;

	@Column(name = "parent_entity_id")
	private String				parentEntityId		= null;

	@Column(name = "module_json")
	private String				moduleJson			= null;

	@Column(name = "updated_by", nullable = false)
	private String				updatedBy			= null;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_date")
	private Date				updatedDate			= null;

	@Column(name = "version_id", nullable = false)
	private Double				versionId			= null;

	@Column(name = "module_json_checksum", nullable = false)
	private String				moduleJsonChecksum	= null;

	@Column(name = "source_type_id")
	private Integer				sourceTypeId		= null;

	public JwsModuleVersion() {
	}

	public JwsModuleVersion(String moduleVersionId, String entityId, String entityName, String parentEntityId, String moduleJson,
			String updatedBy, Date updatedDate, Double versionId, String moduleJsonChecksum, Integer sourceTypeId) {
		this.moduleVersionId	= moduleVersionId;
		this.entityId			= entityId;
		this.entityName			= entityName;
		this.parentEntityId		= parentEntityId;
		this.moduleJson			= moduleJson;
		this.updatedBy			= updatedBy;
		this.updatedDate		= updatedDate;
		this.versionId			= versionId;
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
	 * @return the updatedBy
	 */
	public String getUpdatedBy() {
		return updatedBy;
	}

	/**
	 * @param updatedBy the updatedBy to set
	 */
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
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
		return Objects.hash(entityId, entityName, moduleJson, moduleJsonChecksum, moduleVersionId, parentEntityId, sourceTypeId, updatedBy,
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
		JwsModuleVersion other = (JwsModuleVersion) obj;
		return Objects.equals(entityId, other.entityId) && Objects.equals(entityName, other.entityName)
				&& Objects.equals(moduleJson, other.moduleJson) && Objects.equals(moduleJsonChecksum, other.moduleJsonChecksum)
				&& Objects.equals(moduleVersionId, other.moduleVersionId) && Objects.equals(parentEntityId, other.parentEntityId)
				&& Objects.equals(sourceTypeId, other.sourceTypeId) && Objects.equals(updatedBy, other.updatedBy)
				&& Objects.equals(updatedDate, other.updatedDate) && Objects.equals(versionId, other.versionId);
	}

	@Override
	public String toString() {
		return "JwsModuleVersion [moduleVersionId=" + moduleVersionId + ", entityId=" + entityId + ", entityName=" + entityName
				+ ", parentEntityId=" + parentEntityId + ", moduleJson=" + moduleJson + ", updatedBy=" + updatedBy + ", updatedDate="
				+ updatedDate + ", versionId=" + versionId + ", moduleJsonChecksum=" + moduleJsonChecksum + ", sourceTypeId=" + sourceTypeId
				+ "]";
	}

}