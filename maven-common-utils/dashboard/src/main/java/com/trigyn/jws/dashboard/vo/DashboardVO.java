package com.trigyn.jws.dashboard.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "This model is used to hold dashboard and its associated dashlet information.")
public class DashboardVO implements Serializable{
	
	
	private static final long serialVersionUID 						= 3073208964632498741L;

	@ApiModelProperty(position = 1, name = "dashboardId")
	private String								dashboardId			= null;

	@ApiModelProperty(position = 2, name = "dashboardName")
	private String								dashboardName		= null;
	
	@ApiModelProperty(position = 3, name = "dashboardType")
	private String								dashboardType		= null;
	
	@ApiModelProperty(position = 4, name = "roleIdList")
	private List<String>						roleIdList			= null;
	
	@ApiModelProperty(position = 5, name = "contextId")
	private String								contextId			= null;
	
	@ApiModelProperty(position = 6, name = "dashletIdList")
	private List<String>						dashletIdList		= null;
	
	@ApiModelProperty(position = 7, name = "isDraggable")
	private Integer								isDraggable			= null;
	
	@ApiModelProperty(position = 8, name = "isExportable")
	private Integer								isExportable		= null;

	public DashboardVO() {

	}
	
	public DashboardVO(String dashboardId, String dashboardName, String dashboardType, List<String> roleIdList,
			String contextId, List<String> dashletIdList, Integer isDraggable, Integer isExportable) {
		this.dashboardId 	= dashboardId;
		this.dashboardName 	= dashboardName;
		this.dashboardType 	= dashboardType;
		this.roleIdList 	= roleIdList;
		this.contextId 		= contextId;
		this.dashletIdList 	= dashletIdList;
		this.isDraggable 	= isDraggable;
		this.isExportable 	= isExportable;
	}
	
	
	public String getDashboardId() {
		return dashboardId;
	}

	
	public void setDashboardId(String dashboardId) {
		this.dashboardId = dashboardId;
	}

	
	public String getDashboardName() {
		return dashboardName;
	}

	
	public void setDashboardName(String dashboardName) {
		this.dashboardName = dashboardName;
	}

	
	public String getDashboardType() {
		return dashboardType;
	}

	
	public void setDashboardType(String dashboardType) {
		this.dashboardType = dashboardType;
	}

	
	public List<String> getRoleIdList() {
		return roleIdList;
	}

	
	public void setRoleIdList(List<String> roleIdList) {
		this.roleIdList = roleIdList;
	}

	
	public String getContextId() {
		return contextId;
	}

	
	public void setContextId(String contextId) {
		this.contextId = contextId;
	}

	
	public List<String> getDashletIdList() {
		return dashletIdList;
	}

	
	public void setDashletIdList(List<String> dashletIdList) {
		this.dashletIdList = dashletIdList;
	}

	
	public Integer getIsDraggable() {
		return isDraggable;
	}

	
	public void setIsDraggable(Integer isDraggable) {
		this.isDraggable = isDraggable;
	}

	
	public Integer getIsExportable() {
		return isExportable;
	}

	
	public void setIsExportable(Integer isExportable) {
		this.isExportable = isExportable;
	}


	@Override
	public int hashCode() {
		return Objects.hash(contextId, dashboardId, dashboardName, dashboardType, dashletIdList, isDraggable, isExportable,
				roleIdList);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DashboardVO other = (DashboardVO) obj;
		return Objects.equals(contextId, other.contextId) && Objects.equals(dashboardId, other.dashboardId)
				&& Objects.equals(dashboardName, other.dashboardName)
				&& Objects.equals(dashboardType, other.dashboardType) && Objects.equals(dashletIdList, other.dashletIdList)
				&& Objects.equals(isDraggable, other.isDraggable) && Objects.equals(isExportable, other.isExportable)
				&& Objects.equals(roleIdList, other.roleIdList);
	}

	@Override
	public String toString() {
		return "DashboardVO [dashboardId=" + dashboardId + ", dashboardName=" + dashboardName + ", dashboardType="
				+ dashboardType + ", roleIdList=" + roleIdList + ", contextId=" + contextId + ", dashletIdList=" + dashletIdList
				+ ", isDraggable=" + isDraggable + ", isExportable=" + isExportable + "]";
	}



}
