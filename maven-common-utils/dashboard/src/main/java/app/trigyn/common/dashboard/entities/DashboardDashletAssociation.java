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
@Table(name = "dashboard_dashlet_association")
public class DashboardDashletAssociation implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID 	= 1L;

	@EmbeddedId
	private DashboardDashletAssociationPK id 	= null;
		
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dashboard_id", referencedColumnName = "dashboard_id", nullable = false, insertable = false, updatable = false)
	private Dashboard	dashboard				= null;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dashlet_id", referencedColumnName = "dashlet_id", nullable = false, insertable = false, updatable = false)
	private Dashlet	dashlet						= null;

	public DashboardDashletAssociation() {
		
	}
	
	public DashboardDashletAssociation(DashboardDashletAssociationPK id) {
		this.id 			= id;
	}
	
	/**
	 * @return the id
	 */
	public DashboardDashletAssociationPK getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(DashboardDashletAssociationPK id) {
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

	/**
	 * @return the dashlet
	 */
	public Dashlet getDashlet() {
		return dashlet;
	}

	/**
	 * @param dashlet the dashlet to set
	 */
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





	



	
}
