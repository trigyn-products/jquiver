package com.trigyn.jws.dbutils.vo;

import java.util.Date;

import com.trigyn.jws.dbutils.entities.JwsBusinessModuleEntity;

public class JwsBusinessModuleEntityVO {

	private String	businessModuleEntityDetailsId	= null;

	private String	businessModuleId				= null;

	private String	moduleId						= null;

	private String	entityId						= null;

	private Date	createdDate						= null;

	private String	createdBy						= null;

	public String getBusinessModuleEntityDetailsId() {
		return businessModuleEntityDetailsId;
	}

	public void setBusinessModuleEntityDetailsId(String businessModuleEntityDetailsId) {
		this.businessModuleEntityDetailsId = businessModuleEntityDetailsId;
	}

	public String getBusinessModuleId() {
		return businessModuleId;
	}

	public void setBusinessModuleId(String businessModuleId) {
		this.businessModuleId = businessModuleId;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
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

	public JwsBusinessModuleEntityVO convertEntityToVO(JwsBusinessModuleEntity businessModuleEntityId) {
		JwsBusinessModuleEntityVO businessModuleVo = new JwsBusinessModuleEntityVO();
		businessModuleVo.setBusinessModuleEntityDetailsId(businessModuleEntityId.getBusinessModuleEntityDetailsId());
		businessModuleVo.setBusinessModuleId(businessModuleEntityId.getBusinessModuleId());
		businessModuleVo.setModuleId(businessModuleEntityId.getModuleId());
		businessModuleVo.setEntityId(businessModuleEntityId.getEntityId());
		businessModuleVo.setCreatedBy(businessModuleEntityId.getCreatedBy());
		businessModuleVo.setCreatedDate(businessModuleEntityId.getCreatedDate());
		return businessModuleVo;
	}

}
