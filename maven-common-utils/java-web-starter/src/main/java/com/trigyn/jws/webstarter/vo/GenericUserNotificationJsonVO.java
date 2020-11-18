package com.trigyn.jws.webstarter.vo;

import java.util.Date;

public class GenericUserNotificationJsonVO {

	private String entityName = null;
	
	private String formId = null;
	
	private Date fromDate = null;
	
	private String messageFormat = null;
	
	private String messageText = null;
	
	private String messageType = null;
	
	private String notificationId = null;
	
	private String primaryKey = null;
	
	private String selectionCriteria = null;

	private String targetPlatform = null;

	private Date toDate = null;

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

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
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

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
}
