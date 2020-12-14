package com.trigyn.jws.webstarter.vo;

import java.io.Serializable;

public class PropertyMasterJsonVO implements Serializable{

	private static final long serialVersionUID = 1L;

	private Double appVersion = null;

    private String comment = null;

    private String entityName = null;

    private String formId = null;

    private String primaryKey = null;

    private String ownerId = null;

    private String ownerType = null;

    private String propertyMasterId = null;

    private String propertyName = null;
    
    private String propertyValue = null;

	public Double getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(Double appVersion) {
		this.appVersion = appVersion;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
