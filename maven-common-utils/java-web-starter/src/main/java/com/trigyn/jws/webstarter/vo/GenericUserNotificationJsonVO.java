package com.trigyn.jws.webstarter.vo;

import java.io.Serializable;
import java.util.Date;

public class GenericUserNotificationJsonVO implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	private String				entityName			= null;

	private String				formId				= null;

	private Date				messageValidFrom			= null;

	private String				messageFormat		= null;

	private String				messageText			= null;

	private String				messageType			= null;

	private String				notificationId		= null;

	private String				primaryKey			= null;

	private String				selectionCriteria	= null;

	private String				targetPlatform		= null;

	private Date				messageValidTill	= null;
	
	private Date				updatedDate			= null;

	private String				updatedBy			= null;

	private String				createdBy			= null;

	private Date				creationDate		= null;

	private Integer				isCustomUpdated		= 1;

	private String				datasourceId		= null;
	
	private Integer				displayOnce			= null;


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

	public String getMessageFormat() {
		return messageFormat;
	}

	public void setMessageFormat(String messageFormat) {
		this.messageFormat = messageFormat;
	}

	public String getMessageText() {
		return messageText;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(String notificationId) {
		this.notificationId = notificationId;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getSelectionCriteria() {
		return selectionCriteria;
	}

	public void setSelectionCriteria(String selectionCriteria) {
		this.selectionCriteria = selectionCriteria;
	}

	public String getTargetPlatform() {
		return targetPlatform;
	}

	public void setTargetPlatform(String targetPlatform) {
		this.targetPlatform = targetPlatform;
	}

	public Date getMessageValidFrom() {
		return messageValidFrom;
	}

	public void setMessageValidFrom(Date messageValidFrom) {
		this.messageValidFrom = messageValidFrom;
	}

	public Date getMessageValidTill() {
		return messageValidTill;
	}

	public void setMessageValidTill(Date messageValidTill) {
		this.messageValidTill = messageValidTill;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Integer getIsCustomUpdated() {
		return isCustomUpdated;
	}

	public void setIsCustomUpdated(Integer isCustomUpdated) {
		this.isCustomUpdated = isCustomUpdated;
	}

	public String getDatasourceId() {
		return datasourceId;
	}

	public void setDatasourceId(String datasourceId) {
		this.datasourceId = datasourceId;
	}

	public Integer getDisplayOnce() {
		return displayOnce;
	}

	public void setDisplayOnce(Integer displayOnce) {
		this.displayOnce = displayOnce;
	}

	
}
