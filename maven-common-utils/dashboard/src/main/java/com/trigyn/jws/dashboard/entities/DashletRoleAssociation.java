package com.trigyn.jws.dashboard.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.*;

import com.trigyn.jws.dbutils.entities.UserRole;



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

	
	public DashletRoleAssociationPK getId() {
		return id;
	}

	
	public void setId(DashletRoleAssociationPK id) {
		this.id = id;
	}

	
	public Dashlet getDashlet() {
		return dashlet;
	}

	
	public void setDashlet(Dashlet dashlet) {
		this.dashlet = dashlet;
	}
	
	
	public UserRole getUserRole() {
		return userRole;
	}

	
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

	public DashletRoleAssociation getObject() {
		DashletRoleAssociation obj = new DashletRoleAssociation();
		
		obj.setId(id.getObject());
		obj.setUserRole(userRole.getObject());
		
		return obj;
	}

}