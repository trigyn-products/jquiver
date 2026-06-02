package com.trigyn.jws.dashboard.entities;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "jq_dashboard_dashlet_association")
public class DashboardDashletAssociation implements Serializable {

	private static final long				serialVersionUID	= 1L;

	@EmbeddedId
	private DashboardDashletAssociationPK	id					= null;

	@JsonIgnore
	@XmlTransient
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "dashboard_id", referencedColumnName = "dashboard_id", nullable = false, insertable = false, updatable = false)
	private Dashboard						dashboard			= null;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	@JoinColumn(name = "dashlet_id", referencedColumnName = "dashlet_id", nullable = false, insertable = false, updatable = false)
	@XmlTransient
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

	@XmlTransient
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

	public DashboardDashletAssociation getObject() {
		DashboardDashletAssociation dashboardDashletAssociation = new DashboardDashletAssociation();
		dashboardDashletAssociation.setId(id.getObject());

		return dashboardDashletAssociation;
	}

}
