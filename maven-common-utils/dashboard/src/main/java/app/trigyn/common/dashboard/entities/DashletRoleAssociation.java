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
@Table(name = "dashlet_role_association")
public class DashletRoleAssociation implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	@EmbeddedId
	private DashletRoleAssociationPK id				= null;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dashlet_id", referencedColumnName = "dashlet_id", nullable = false, insertable = false, updatable = false)
	private Dashlet				dashlet				= null;


	public DashletRoleAssociation() {
		
	}

	public DashletRoleAssociation(DashletRoleAssociationPK id) {
		this.id 		= id;
	}

	/**
	 * @return the id
	 */
	public DashletRoleAssociationPK getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(DashletRoleAssociationPK id) {
		this.id = id;
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
		return Objects.hash(dashlet, id);
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
		DashletRoleAssociation other = (DashletRoleAssociation) obj;
		return Objects.equals(dashlet, other.dashlet) && Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "DashletRoleAssociation [id=" + id + ", dashlet=" + dashlet + "]";
	}


}
