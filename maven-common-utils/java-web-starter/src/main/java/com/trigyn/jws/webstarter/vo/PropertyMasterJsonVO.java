package com.trigyn.jws.webstarter.vo;

import java.io.Serializable;
import java.util.Date;

public class PropertyMasterJsonVO implements Serializable {

	private static final long	serialVersionUID	= 1L;

	private Double				appVersion			= null;

	private String				comments				= null;

	private String				entityName			= null;

	private String				formId				= null;

	private String				primaryKey			= null;

	private String				ownerId				= null;

	private String				ownerType			= null;

	private String				propertyMasterId	= null;

	private String				propertyName		= null;

	private String				propertyValue		= null;
	
	private Integer				isDeleted			= null;
	
	private Date				lastModifiedDate	= null;

	private String				modifiedBy			= null;
	public Double getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(Double appVersion) {
		this.appVersion = appVersion;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public String getPropertyMasterId() {
		return propertyMasterId;
	}

	public void setPropertyMasterId(String propertyMasterId) {
		this.propertyMasterId = propertyMasterId;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
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

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	
	

}
