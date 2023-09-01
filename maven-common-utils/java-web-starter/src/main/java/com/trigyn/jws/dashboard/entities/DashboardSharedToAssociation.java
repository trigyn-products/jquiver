package com.trigyn.jws.dashboard.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "jq_dashboard_shared_to_association")
public class DashboardSharedToAssociation implements Serializable {

	private static final long				serialVersionUID		= 1L;

	@EmbeddedId
	private DashboardSharedToAssociationPK	id						= null;

	@Column(name = "dashboard_permission_type")
	private String							dashboardPermissionType	= null;

	public DashboardSharedToAssociation() {

	}

	public DashboardSharedToAssociation(DashboardSharedToAssociationPK id) {
		this.id = id;
	}

	public DashboardSharedToAssociationPK getId() {
		return id;
	}

	public void setId(DashboardSharedToAssociationPK id) {
		this.id = id;
	}

	public String getDashboardPermissionType() {
		return dashboardPermissionType;
	}

	public void setDashboardPermissionType(String dashboardPermissionType) {
		this.dashboardPermissionType = dashboardPermissionType;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dashboardPermissionType, id);
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
		DashboardSharedToAssociation other = (DashboardSharedToAssociation) obj;
		return Objects.equals(dashboardPermissionType, other.dashboardPermissionType) && Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "DashboardSharedToAssociation [id=" + id + ", dashboardPermissionType=" + dashboardPermissionType + "]";
	}

}
