package com.trigyn.jws.dashboard.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class DashboardDashletAssociationPK implements Serializable {

	private static final long	serialVersionUID	= -6922215861439332377L;

	@Column(name = "dashboard_id")
	private String				dashboardId			= null;

	@Column(name = "dashlet_id")
	private String				dashletId			= null;

	public DashboardDashletAssociationPK(String dashboardId, String dashletId) {
		this.dashboardId	= dashboardId;
		this.dashletId		= dashletId;
	}

	public DashboardDashletAssociationPK() {

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

	@Override
	public int hashCode() {
		return Objects.hash(dashboardId, dashletId);
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
		DashboardDashletAssociationPK other = (DashboardDashletAssociationPK) obj;
		return Objects.equals(dashboardId, other.dashboardId) && Objects.equals(dashletId, other.dashletId);
	}

	@Override
	public String toString() {
		return "DashboardDashletAssociationPK [dashboardId=" + dashboardId + ", dashletId=" + dashletId + "]";
	}

	public DashboardDashletAssociationPK getObject() {
		DashboardDashletAssociationPK dashboardDashletAssociationPK = new DashboardDashletAssociationPK();
		dashboardDashletAssociationPK.setDashboardId(dashboardId != null ? dashboardId.trim() : dashboardId);
		dashboardDashletAssociationPK.setDashletId(dashletId != null ? dashletId.trim() : dashletId);

		return dashboardDashletAssociationPK;
	}

}
