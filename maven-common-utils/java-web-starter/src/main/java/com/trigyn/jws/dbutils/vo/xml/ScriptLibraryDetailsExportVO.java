package com.trigyn.jws.dbutils.vo.xml;

import java.util.Date;

public class ScriptLibraryDetailsExportVO {

	private String	scriptLibId			= null;

	private String	templateId			= null;
	
	private String	libraryName			= null;
	
	private String	description			= null;
	
	private String	scriptType			= null;

	private String	createdBy			= null;

	private String	updatedBy			= null;

	private Date	updatedDate			= null;

	private Integer	isCustomUpdated		= 0;
	
	private String	scriptLibConnId		= null;
	
	private String	moduleTypeId		= null;
	
	private String	enityId			= null;

	public ScriptLibraryDetailsExportVO() {}

	public ScriptLibraryDetailsExportVO(String scriptLibId, String templateId, String libraryName, String description,
			String scriptType, String createdBy, String updatedBy, Date updatedDate, Integer isCustomUpdated, String scriptLibConnId, String moduleTypeId, String enityId) {
		this.scriptLibId = scriptLibId;
		this.templateId = templateId;
		this.libraryName = libraryName;
		this.description = description;
		this.scriptType = scriptType;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
		this.updatedDate = updatedDate;
		this.isCustomUpdated = isCustomUpdated;
		this.scriptLibConnId = scriptLibConnId;
		this.moduleTypeId = moduleTypeId;
		this.enityId = enityId;
	}

	public String getScriptLibId() {
		return scriptLibId;
	}

	public void setScriptLibId(String scriptLibId) {
		this.scriptLibId = scriptLibId;
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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getIsCustomUpdated() {
		return isCustomUpdated;
	}

	public void setIsCustomUpdated(Integer isCustomUpdated) {
		this.isCustomUpdated = isCustomUpdated;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getScriptLibConnId() {
		return scriptLibConnId;
	}

	public void setScriptLibConnId(String scriptLibConnId) {
		this.scriptLibConnId = scriptLibConnId;
	}

	public String getModuleTypeId() {
		return moduleTypeId;
	}

	public void setModuleTypeId(String moduleTypeId) {
		this.moduleTypeId = moduleTypeId;
	}

	public String getEnityId() {
		return enityId;
	}

	public void setEnityId(String enityId) {
		this.enityId = enityId;
	}
}
