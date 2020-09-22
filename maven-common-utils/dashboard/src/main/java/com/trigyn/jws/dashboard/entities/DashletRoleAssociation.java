package com.trigyn.jws.dashboard.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.*;

import com.trigyn.jws.dbutils.entities.UserRole;


/**
 * The persistent class for the dashlet_role_association database table.
 * 
 */
@Entity
@Table(name="dashlet_role_association")
@NamedQuery(name="DashletRoleAssociation.findAll", query="SELECT d FROM DashletRoleAssociation d")
public class DashletRoleAssociation implements Serializable {
	private static final long serialVersionUID 		= 1L;

	@EmbeddedId
	private DashletRoleAssociationPK id				= null;
	
	@ManyToOne
	@JoinColumn(name="dashlet_id", nullable=false, insertable=false, updatable=false)
	private Dashlet dashlet							= null;

	@ManyToOne
	@JoinColumn(name="role_id", nullable=false, insertable=false, updatable=false)
	private UserRole userRole						= null;

	public DashletRoleAssociation() {
		
	}

	public DashletRoleAssociation(DashletRoleAssociationPK id, UserRole userRole) {
		this.id 		= id;
		this.userRole 	= userRole;
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
	
	/**
	 * @return the userRole
	 */
	public UserRole getUserRole() {
		return userRole;
	}

	/**
	 * @param userRole the userRole to set
	 */
	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dashlet, id, userRole);
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
		return Objects.equals(dashlet, other.dashlet) && Objects.equals(id, other.id)
				&& Objects.equals(userRole, other.userRole);
	}

	@Override
	public String toString() {
		return "DashletRoleAssociation [id=" + id + ", dashlet=" + dashlet + ", userRole=" + userRole + "]";
	}


	
}