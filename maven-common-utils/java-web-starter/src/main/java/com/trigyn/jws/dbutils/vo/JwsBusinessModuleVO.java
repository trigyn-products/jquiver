package com.trigyn.jws.dbutils.vo;

import java.util.Date;

import com.trigyn.jws.dbutils.entities.JwsBusinessModuleEntity;

public class JwsBusinessModuleVO {
	
	private String	businessModuleId	= null;

	private String	moduleName			= null;

	private Date	createdDate			= null;

	private String	createdBy			= null;

	private Date	updatedDate			= null;

	private String	updatedBy			= null;

	private String	entityName			= null;

	private String	primaryKey			= null;

	public String getBusinessModuleId() {
		return businessModuleId;
	}

	public void setBusinessModuleId(String businessModuleId) {
		this.businessModuleId = businessModuleId;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
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

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}
	

}
