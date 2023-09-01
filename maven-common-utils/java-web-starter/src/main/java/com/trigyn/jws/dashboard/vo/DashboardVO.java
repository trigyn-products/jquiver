package com.trigyn.jws.dashboard.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "This model is used to hold dashboard and its associated dashlet information.")
public class DashboardVO implements Serializable {

	private static final long	serialVersionUID	= 3073208964632498741L;

	@ApiModelProperty(position = 1, name = "dashboardId")
	private String				dashboardId			= null;

	@ApiModelProperty(position = 2, name = "dashboardName")
	private String				dashboardName		= null;

	@ApiModelProperty(position = 3, name = "roleIdList")
	private List<String>		roleIdList			= null;

	@ApiModelProperty(position = 5, name = "dashletIdList")
	private List<String>		dashletIdList		= null;

	@ApiModelProperty(position = 6, name = "isDraggable")
	private Integer				isDraggable			= null;

	@ApiModelProperty(position = 7, name = "isExportable")
	private Integer				isExportable		= null;
	
	@ApiModelProperty(position = 8, name = "dashboardBody")
	private String				dashboardBody		= null;

	public DashboardVO() {

	}

	public DashboardVO(String dashboardId, String dashboardName, List<String> roleIdList, List<String> dashletIdList,
			Integer isDraggable, Integer isExportable, String dashboardBody) {
		this.dashboardId	= dashboardId;
		this.dashboardName	= dashboardName;
		this.roleIdList		= roleIdList;
		this.dashletIdList	= dashletIdList;
		this.isDraggable	= isDraggable;
		this.isExportable	= isExportable;
		this.dashboardBody 	= dashboardBody;
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

	public List<String> getRoleIdList() {
		return roleIdList;
	}

	public void setRoleIdList(List<String> roleIdList) {
		this.roleIdList = roleIdList;
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

	public String getDashboardBody() {
		return dashboardBody;
	}

	public void setDashboardBody(String dashboardBody) {
		this.dashboardBody = dashboardBody;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dashboardId, dashboardName, dashletIdList, isDraggable, isExportable, roleIdList,dashboardBody);
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
		return Objects.equals(dashboardId, other.dashboardId)
				&& Objects.equals(dashboardName, other.dashboardName) && Objects.equals(dashletIdList, other.dashletIdList)
				&& Objects.equals(isDraggable, other.isDraggable) && Objects.equals(isExportable, other.isExportable)
				&& Objects.equals(roleIdList, other.roleIdList);
	}

	@Override
	public String toString() {
		return "DashboardVO [dashboardId=" + dashboardId + ", dashboardName=" + dashboardName + ", roleIdList=" + roleIdList
				+ ", dashletIdList=" + dashletIdList + ", isDraggable=" + isDraggable + ", isExportable="
				+ isExportable + ", dashboardBody=" + dashboardBody + "]";
	}

}
