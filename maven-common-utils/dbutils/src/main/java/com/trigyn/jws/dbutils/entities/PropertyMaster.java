package com.trigyn.jws.dbutils.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "jq_property_master")
public class PropertyMaster implements Serializable {

	private static final long	serialVersionUID	= -6641076236442355786L;

	@Id
	@GeneratedValue(generator = "inquisitive-uuid")
	@GenericGenerator(name = "inquisitive-uuid", strategy = "com.trigyn.jws.dbutils.configurations.CustomUUIDGenerator")
	@Column(name = "property_master_id")
	private String				propertyMasterId	= null;

	@Column(name = "owner_type")
	private String				ownerType			= null;

	@Column(name = "owner_id")
	private String				ownerId				= null;

	@Column(name = "property_name")
	private String				propertyName		= null;

	@Column(name = "property_value")
	private String				propertyValue		= null;

	@Column(name = "is_deleted")
	private Integer				isDeleted			= null;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_modified_date")
	private Date				lastModifiedDate	= null;

	@Column(name = "modified_by")
	private String				modifiedBy			= null;

	@Column(name = "app_version")
	private Double				appVersion			= null;

	@Column(name = "comments")
	private String				comments			= null;

	@Column(name = "is_custom_updated")
	private Integer				isCustomUpdated		= 1;

	public PropertyMaster(String propertyMasterId, String ownerType, String ownerId, String propertyName,
			String propertyValue, Integer isDeleted, Date lastModifiedDate, String modifiedBy, Double appVersion,
			String comments) {
		this.propertyMasterId	= propertyMasterId;
		this.ownerType			= ownerType;
		this.ownerId			= ownerId;
		this.propertyName		= propertyName;
		this.propertyValue		= propertyValue;
		this.isDeleted			= isDeleted;
		this.lastModifiedDate	= lastModifiedDate;
		this.modifiedBy			= modifiedBy;
		this.appVersion			= appVersion;
		this.comments			= comments;
	}

	public PropertyMaster() {

	}

	public String getPropertyMasterId() {
		return propertyMasterId;
	}

	public void setPropertyMasterId(String propertyMasterId) {
		this.propertyMasterId = propertyMasterId;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Double getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(Double appVersion) {
		this.appVersion = appVersion;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Integer getIsCustomUpdated() {
		return isCustomUpdated;
	}

	public void setIsCustomUpdated(Integer isCustomUpdated) {
		this.isCustomUpdated = isCustomUpdated;
	}

	@Override
	public int hashCode() {
		return Objects.hash(appVersion, comments, isDeleted, lastModifiedDate, modifiedBy, ownerId, ownerType,
				propertyMasterId, propertyName, propertyValue);
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
		PropertyMaster other = (PropertyMaster) obj;
		return Objects.equals(appVersion, other.appVersion) && Objects.equals(comments, other.comments)
				&& Objects.equals(isDeleted, other.isDeleted)
				&& Objects.equals(lastModifiedDate, other.lastModifiedDate)
				&& Objects.equals(modifiedBy, other.modifiedBy) && Objects.equals(ownerId, other.ownerId)
				&& Objects.equals(ownerType, other.ownerType)
				&& Objects.equals(propertyMasterId, other.propertyMasterId)
				&& Objects.equals(propertyName, other.propertyName)
				&& Objects.equals(propertyValue, other.propertyValue);
	}

	@Override
	public String toString() {
		return "PropertyMaster [propertyMasterId=" + propertyMasterId + ", ownerType=" + ownerType + ", ownerId="
				+ ownerId + ", propertyName=" + propertyName + ", propertyValue=" + propertyValue + ", isDeleted="
				+ isDeleted + ", lastModifiedDate=" + lastModifiedDate + ", modifiedBy=" + modifiedBy + ", appVersion="
				+ appVersion + ", comments=" + comments + "]";
	}

	public PropertyMaster getObject() {
		PropertyMaster propertyMaster = new PropertyMaster();

		propertyMaster.setAppVersion(appVersion);
		propertyMaster.setComments(comments);
		propertyMaster.setIsDeleted(isDeleted);
		propertyMaster.setLastModifiedDate(lastModifiedDate);
		propertyMaster.setModifiedBy(modifiedBy);
		propertyMaster.setOwnerId(ownerId);
		propertyMaster.setOwnerType(ownerType);
		propertyMaster.setPropertyMasterId(propertyMasterId);
		propertyMaster.setPropertyName(propertyName);
		propertyMaster.setPropertyValue(propertyValue);

		return propertyMaster;
	}

}
