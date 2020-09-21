package app.trigyn.common.dashboard.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "dashboard_role_association")
public class DashboardRoleAssociation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID 	= 1L;

	@EmbeddedId
	private DashboardRoleAssociationPK  id 		= null;;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "dashboard_id", referencedColumnName = "dashboard_id", nullable = false, insertable = false, updatable = false)
	private Dashboard	dashboard				= null;


	public DashboardRoleAssociation() {
		
	}


	public DashboardRoleAssociation(DashboardRoleAssociationPK id) {
		this.id = id;
	}


	/**
	 * @return the id
	 */
	public DashboardRoleAssociationPK getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(DashboardRoleAssociationPK id) {
		this.id = id;
	}


	/**
	 * @return the dashboard
	 */
	public Dashboard getDashboard() {
		return dashboard;
	}


	/**
	 * @param dashboard the dashboard to set
	 */
	public void setDashboard(Dashboard dashboard) {
		this.dashboard = dashboard;
	}


	@Override
	public int hashCode() {
		return Objects.hash(dashboard, id);
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
		return Objects.equals(dashboard, other.dashboard) && Objects.equals(id, other.id);
	}


	@Override
	public String toString() {
		return "DashboardRoleAssociation [id=" + id + ", dashboard=" + dashboard + "]";
	}

	
	
}
