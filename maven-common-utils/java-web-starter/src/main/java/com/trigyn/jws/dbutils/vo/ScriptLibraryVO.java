package com.trigyn.jws.dbutils.vo;

import java.util.Date;

public class ScriptLibraryVO {
	private String				scriptlibId				= null;

	private String				createdBy				= null;

	private Date				updatedDate				= null;

	private String				templateId				= null;

	private String				libraryName				= null;
	
	private String				description				= null;
	
	private String				scriptType				= null;
	
	private Integer				iscustomUpdated			= null;

	private String				updatedBy				= null;
	
	private String				formId					= null;
	
	private String				primaryKey				= null;
	
	private String				entityName				= null;
	
	private Integer 			isEdit 					= null;

	public String getScriptlibId() {
		return scriptlibId;
	}

	public void setScriptlibId(String scriptlibId) {
		this.scriptlibId = scriptlibId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getLibraryName() {
		return libraryName;
	}

	public void setLibraryName(String libraryName) {
		this.libraryName = libraryName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getScriptType() {
		return scriptType;
	}

	public void setScriptType(String scriptType) {
		this.scriptType = scriptType;
	}

	public Integer getIscustomUpdated() {
		return iscustomUpdated;
	}

	public void setIscustomUpdated(Integer iscustomUpdated) {
		this.iscustomUpdated = iscustomUpdated;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
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

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public Integer getIsEdit() {
		return isEdit;
	}

	public void setIsEdit(Integer isEdit) {
		this.isEdit = isEdit;
	}
	
}
