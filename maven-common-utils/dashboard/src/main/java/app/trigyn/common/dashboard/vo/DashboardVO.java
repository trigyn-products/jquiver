package app.trigyn.common.dashboard.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "This model is used to hold dashboard and its associated dashlet information.")
public class DashboardVO implements Serializable{
	
	/**
	 * 
	 */
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
	
	/**
	 * @return the dashboardId
	 */
	public String getDashboardId() {
		return dashboardId;
	}

	/**
	 * @param dashboardId the dashboardId to set
	 */
	public void setDashboardId(String dashboardId) {
		this.dashboardId = dashboardId;
	}

	/**
	 * @return the dashboardName
	 */
	public String getDashboardName() {
		return dashboardName;
	}

	/**
	 * @param dashboardName the dashboardName to set
	 */
	public void setDashboardName(String dashboardName) {
		this.dashboardName = dashboardName;
	}

	/**
	 * @return the dashboardType
	 */
	public String getDashboardType() {
		return dashboardType;
	}

	/**
	 * @param dashboardType the dashboardType to set
	 */
	public void setDashboardType(String dashboardType) {
		this.dashboardType = dashboardType;
	}

	/**
	 * @return the roleIdList
	 */
	public List<String> getRoleIdList() {
		return roleIdList;
	}

	/**
	 * @param roleIdList the roleIdList to set
	 */
	public void setRoleIdList(List<String> roleIdList) {
		this.roleIdList = roleIdList;
	}

	/**
	 * @return the contextId
	 */
	public String getContextId() {
		return contextId;
	}

	/**
	 * @param contextId the contextId to set
	 */
	public void setContextId(String contextId) {
		this.contextId = contextId;
	}

	/**
	 * @return the dashletIdList
	 */
	public List<String> getDashletIdList() {
		return dashletIdList;
	}

	/**
	 * @param dashletIdList the dashletIdList to set
	 */
	public void setDashletIdList(List<String> dashletIdList) {
		this.dashletIdList = dashletIdList;
	}

	/**
	 * @return the isDraggable
	 */
	public Integer getIsDraggable() {
		return isDraggable;
	}

	/**
	 * @param isDraggable the isDraggable to set
	 */
	public void setIsDraggable(Integer isDraggable) {
		this.isDraggable = isDraggable;
	}

	/**
	 * @return the isExportable
	 */
	public Integer getIsExportable() {
		return isExportable;
	}

	/**
	 * @param isExportable the isExportable to set
	 */
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
