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
@Table(name="jws_template_version")
@NamedQuery(name="JwsTemplateVersion.findAll", query="SELECT j FROM JwsTemplateVersion j")
public class JwsTemplateVersion implements Serializable {
	
	private static final long serialVersionUID 		= 1L;

	@Id
    @GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name="template_version_id", unique=true, nullable=false)
	private String templateVersionId				= null;

	@Column(name="entity_id", nullable=false)
	private String entityId							= null;
	
	@Column(name="entity_name", nullable=false)
	private String entityName						= null;

	@Column(name="parent_entity_id")
	private String parentEntityId					= null;

	@Column(name="template_json")
	private String templateJson						= null;

	@Column(name="updated_by", nullable=false)
	private String updatedBy						= null;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_date")
	private Date updatedDate						= null;

	@Column(name="version_id", nullable=false)
	private Double versionId						= null;	

	public JwsTemplateVersion() {
	}

	public JwsTemplateVersion(String templateVersionId, String entityId, String entityName, String parentEntityId,
			String templateJson, String updatedBy, Date updatedDate, Double versionId) {
		this.templateVersionId 		= templateVersionId;
		this.entityId			 	= entityId;
		this.entityName 			= entityName;
		this.parentEntityId 		= parentEntityId;
		this.templateJson 			= templateJson;
		this.updatedBy 				= updatedBy;
		this.updatedDate 			= updatedDate;
		this.versionId 				= versionId;
	}

	/**
	 * @return the templateVersionId
	 */
	public String getTemplateVersionId() {
		return templateVersionId;
	}

	/**
	 * @param templateVersionId the templateVersionId to set
	 */
	public void setTemplateVersionId(String templateVersionId) {
		this.templateVersionId = templateVersionId;
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
	 * @return the templateJson
	 */
	public String getTemplateJson() {
		return templateJson;
	}

	/**
	 * @param templateJson the templateJson to set
	 */
	public void setTemplateJson(String templateJson) {
		this.templateJson = templateJson;
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

	@Override
	public int hashCode() {
		return Objects.hash(entityId, entityName, parentEntityId, templateJson, templateVersionId, updatedBy,
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
		JwsTemplateVersion other = (JwsTemplateVersion) obj;
		return Objects.equals(entityId, other.entityId) && Objects.equals(entityName, other.entityName)
				&& Objects.equals(parentEntityId, other.parentEntityId)
				&& Objects.equals(templateJson, other.templateJson)
				&& Objects.equals(templateVersionId, other.templateVersionId)
				&& Objects.equals(updatedBy, other.updatedBy) && Objects.equals(updatedDate, other.updatedDate)
				&& Objects.equals(versionId, other.versionId);
	}

	@Override
	public String toString() {
		return "JwsTemplateVersion [templateVersionId=" + templateVersionId + ", entityId=" + entityId + ", entityName="
				+ entityName + ", parentEntityId=" + parentEntityId + ", templateJson=" + templateJson + ", updatedBy="
				+ updatedBy + ", updatedDate=" + updatedDate + ", versionId=" + versionId + ", getTemplateVersionId()="
				+ getTemplateVersionId() + ", getEntityId()=" + getEntityId() + ", getEntityName()=" + getEntityName()
				+ ", getParentEntityId()=" + getParentEntityId() + ", getTemplateJson()=" + getTemplateJson()
				+ ", getUpdatedBy()=" + getUpdatedBy() + ", getUpdatedDate()=" + getUpdatedDate() + ", getVersionId()="
				+ getVersionId() + ", hashCode()=" + hashCode() + "]";
	}

	

}