package com.trigyn.jws.usermanagement.vo;

import java.util.Date;
import java.util.List;



public class JwsEntityRoleVO {

	private String entityRoleId = null;
	
	private String entityId = null;
	
	private String entityName = null;
	
	private String moduleId = null;
	
	private List<String> roleIds = null;
	
	private Date lastUpdatedDate = null;
	
	private String lastUpdatedBy = null;
	
	private Integer isActive = null;

	public String getEntityRoleId() {
		return entityRoleId;
	}

	public void setEntityRoleId(String entityRoleId) {
		this.entityRoleId = entityRoleId;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}


	public List<String> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(List<String> roleIds) {
		this.roleIds = roleIds;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}


}
