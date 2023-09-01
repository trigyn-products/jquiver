package com.trigyn.jws.dashboard.vo;

import java.io.Serializable;
import java.util.Objects;

public class DashboardDashletVO implements Serializable {

	private static final long	serialVersionUID	= 375952513225551323L;

	private String				dashboardId			= null;

	private String				dashletId			= null;

	private String				dashletName			= null;

	private Boolean				isSelected			= null;

	public DashboardDashletVO() {

	}

	public DashboardDashletVO(String dashboardId, String dashletId, String dashletName,Boolean isSelected) {
		this.dashboardId	= dashboardId;
		this.dashletId		= dashletId;
		this.dashletName	= dashletName;
		this.isSelected		= isSelected;
	}

	public DashboardDashletVO(String dashboardId, String dashletId, String dashletName) {
		this.dashboardId	= dashboardId;
		this.dashletId		= dashletId;
		this.dashletName	= dashletName;
	}

	public DashboardDashletVO(String dashletId, String dashletName) {
		this.dashletId = dashletId;
		this.dashletName = dashletName;
	}

	public String getDashboardId() {
		return dashboardId;
	}

	public void setDashboardId(String dashboardId) {
		this.dashboardId = dashboardId;
	}

	public String getDashletId() {
		return dashletId;
	}

	public void setDashletId(String dashletId) {
		this.dashletId = dashletId;
	}

	public String getDashletName() {
		return dashletName;
	}

	public void setDashletName(String dashletName) {
		this.dashletName = dashletName;
	}

	public Boolean getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(Boolean isSelected) {
		this.isSelected = isSelected;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dashboardId, dashletId, dashletName, isSelected);
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
		DashboardDashletVO other = (DashboardDashletVO) obj;
		return Objects.equals(dashboardId, other.dashboardId)
				&& Objects.equals(dashletId, other.dashletId) && Objects.equals(dashletName, other.dashletName)
				&& Objects.equals(isSelected, other.isSelected);
	}

	@Override
	public String toString() {
		return "DashboardDashletVO [dashletId=" + dashletId + ", dashboardId=" + dashboardId + ", dashletName=" + dashletName
				 + ", isSelected=" + isSelected + "]";
	}

}
