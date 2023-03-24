package com.trigyn.jws.dashboard.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.trigyn.jws.usermanagement.entities.JwsRole;


@Entity
@Table(name = "jq_dashboard_role_association")
@NamedQuery(name = "DashboardRoleAssociation.findAll", query = "SELECT d FROM DashboardRoleAssociation d")
public class DashboardRoleAssociation implements Serializable {

	private static final long			serialVersionUID	= 1L;

	@EmbeddedId
	private DashboardRoleAssociationPK	id					= null;

	@ManyToOne
	@JoinColumn(name = "dashboard_id", nullable = false, insertable = false, updatable = false)
	private Dashboard					dashboard			= null;

	@ManyToOne
	@JoinColumn(name = "role_id", nullable = false, insertable = false, updatable = false)
	private JwsRole					userRole			= null;

	public DashboardRoleAssociation() {
	}

	public DashboardRoleAssociation(DashboardRoleAssociationPK id, JwsRole userRole) {
		this.id			= id;
		this.userRole	= userRole;
	}

	public DashboardRoleAssociationPK getId() {
		return id;
	}

	public void setId(DashboardRoleAssociationPK id) {
		this.id = id;
	}

	public JwsRole getJwsRole() {
		return userRole;
	}

	public void setJwsRole(JwsRole userRole) {
		this.userRole = userRole;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dashboard, id, userRole);
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
		DashboardRoleAssociation other = (DashboardRoleAssociation) obj;
		return Objects.equals(dashboard, other.dashboard) && Objects.equals(id, other.id) && Objects.equals(userRole, other.userRole);
	}

	@Override
	public String toString() {
		return "DashboardRoleAssociation [id=" + id + ", dashboard=" + dashboard + ", userRole=" + userRole + "]";
	}

	public DashboardRoleAssociation getObject() {
		DashboardRoleAssociation dashboardRoleAssociation = new DashboardRoleAssociation();
		dashboardRoleAssociation.setId(id.getObject());
		return dashboardRoleAssociation;
	}

}