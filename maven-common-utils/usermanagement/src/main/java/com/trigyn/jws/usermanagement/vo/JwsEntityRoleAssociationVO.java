package com.trigyn.jws.usermanagement.vo;

import java.util.Date;

import com.trigyn.jws.usermanagement.entities.JwsEntityRoleAssociation;

public class JwsEntityRoleAssociationVO {

	private String	entityRoleId	= null;

	private String	entityId		= null;

	private String	entityName		= null;

	private String	moduleId		= null;

	private String	roleId			= null;

	private Date	lastUpdatedDate	= null;

	private String	lastUpdatedBy	= null;

	private Integer	isActive		= null;

	private Integer	moduleTypeId	= 0;

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

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
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

	public Integer getModuleTypeId() {
		return moduleTypeId;
	}

	public void setModuleTypeId(Integer moduleTypeId) {
		this.moduleTypeId = moduleTypeId;
	}

	public JwsEntityRoleAssociation convertVOtoEntity(JwsEntityRoleAssociation entityRoleAssociation, JwsEntityRoleAssociationVO entityRoleAssociationVO) {
		if(entityRoleAssociation == null) {
			entityRoleAssociation = new JwsEntityRoleAssociation();
		}
		entityRoleAssociation.setEntityId(entityRoleAssociationVO.getEntityId());
		entityRoleAssociation.setEntityName(entityRoleAssociationVO.getEntityName());
		entityRoleAssociation.setEntityRoleId(entityRoleAssociationVO.getEntityRoleId());
		entityRoleAssociation.setModuleId(entityRoleAssociationVO.getModuleId());
		entityRoleAssociation.setRoleId(entityRoleAssociationVO.getRoleId());
		entityRoleAssociation.setIsActive(entityRoleAssociationVO.getIsActive());
		entityRoleAssociation.setModuleTypeId(entityRoleAssociationVO.getModuleTypeId());
		return entityRoleAssociation;
	}

	public JwsEntityRoleAssociationVO convertEntityToVO(JwsEntityRoleAssociation entityRoleAssociation) {
		JwsEntityRoleAssociationVO association = new JwsEntityRoleAssociationVO();
		association.setEntityId(entityRoleAssociation.getEntityId());
		association.setEntityName(entityRoleAssociation.getEntityName());
		association.setEntityRoleId(entityRoleAssociation.getEntityRoleId());
		association.setModuleId(entityRoleAssociation.getModuleId());
		association.setRoleId(entityRoleAssociation.getRoleId());
		association.setIsActive(entityRoleAssociation.getIsActive());
		association.setModuleTypeId(entityRoleAssociation.getModuleTypeId());
		return association;
	}

}
