package com.trigyn.jws.dashboard.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "jq_dashboard_dashlet_association")
public class DashboardDashletAssociation implements Serializable {

	private static final long				serialVersionUID	= 1L;

	@EmbeddedId
	private DashboardDashletAssociationPK	id					= null;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dashboard_id", referencedColumnName = "dashboard_id", nullable = false, insertable = false, updatable = false)
	private Dashboard						dashboard			= null;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dashlet_id", referencedColumnName = "dashlet_id", nullable = false, insertable = false, updatable = false)
	private Dashlet							dashlet				= null;

	public DashboardDashletAssociation() {

	}

	public DashboardDashletAssociation(DashboardDashletAssociationPK id) {
		this.id = id;
	}

	public DashboardDashletAssociationPK getId() {
		return id;
	}

	public void setId(DashboardDashletAssociationPK id) {
		this.id = id;
	}

	public Dashboard getDashboard() {
		return dashboard;
	}

	public void setDashboard(Dashboard dashboard) {
		this.dashboard = dashboard;
	}

	public Dashlet getDashlet() {
		return dashlet;
	}

	public void setDashlet(Dashlet dashlet) {
		this.dashlet = dashlet;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dashboard, dashlet, id);
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
		DashboardDashletAssociation other = (DashboardDashletAssociation) obj;
		return Objects.equals(dashboard, other.dashboard) && Objects.equals(dashlet, other.dashlet) && Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "DashboardDashletAssociation [id=" + id + ", dashboard=" + dashboard + ", dashlet=" + dashlet + "]";
	}

	public DashboardDashletAssociation getObject() {
		DashboardDashletAssociation dashboardDashletAssociation = new DashboardDashletAssociation();
		dashboardDashletAssociation.setId(id.getObject());

		return dashboardDashletAssociation;
	}

}
