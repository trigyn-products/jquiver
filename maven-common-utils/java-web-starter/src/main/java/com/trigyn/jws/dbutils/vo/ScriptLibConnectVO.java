package com.trigyn.jws.dbutils.vo;

import java.util.Date;

import com.trigyn.jws.dbutils.entities.JwsBusinessModuleEntity;
import com.trigyn.jws.sciptlibrary.entities.ScriptLibraryConnection;

public class ScriptLibConnectVO {

	private String	scriptlibConnId	= null;

	private String	scriptlibId		= null;

	private String	moduleTypeId	= null;

	private String	entityId		= null;

	private String	createdBy		= null;

	private String	updatedBy		= null;

	private Date	updatedDate		= null;

	private Integer	iscustomUpdated	= null;

	private String	formId			= null;

	private String	primaryKey		= null;

	public String getScriptlibConnId() {
		return scriptlibConnId;
	}

	public void setScriptlibConnId(String scriptlibConnId) {
		this.scriptlibConnId = scriptlibConnId;
	}

	public String getScriptlibId() {
		return scriptlibId;
	}

	public void setScriptlibId(String scriptlibId) {
		this.scriptlibId = scriptlibId;
	}

	public String getModuleTypeId() {
		return moduleTypeId;
	}

	public void setModuleTypeId(String moduleTypeId) {
		this.moduleTypeId = moduleTypeId;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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

	public Integer getIscustomUpdated() {
		return iscustomUpdated;
	}

	public void setIscustomUpdated(Integer iscustomUpdated) {
		this.iscustomUpdated = iscustomUpdated;
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
	
	public ScriptLibConnectVO convertEntityToVO(ScriptLibraryConnection scriptLibraryConnection) {
		ScriptLibConnectVO scriptLibConnectVO = new ScriptLibConnectVO();
		scriptLibConnectVO.setScriptlibConnId(scriptLibraryConnection.getScriptlibconnId());
		scriptLibConnectVO.setScriptlibId(scriptLibraryConnection.getScriptLibId());
		scriptLibConnectVO.setModuleTypeId(scriptLibraryConnection.getModuletypeId());
		scriptLibConnectVO.setEntityId(scriptLibraryConnection.getEntityId());
		scriptLibConnectVO.setCreatedBy(scriptLibraryConnection.getCreatedBy());
		scriptLibConnectVO.setUpdatedBy(scriptLibraryConnection.getUpdatedBy());
		scriptLibConnectVO.setUpdatedDate(scriptLibraryConnection.getUpdatedDate());
		scriptLibConnectVO.setIscustomUpdated(scriptLibraryConnection.getIsCustomUpdated());
		return scriptLibConnectVO;
	}

}
