package com.trigyn.jws.dynarest.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.*;

import com.trigyn.jws.dbutils.entities.UserRole;


/**
 * The persistent class for the jws_dynamic_rest_role_association database table.
 * 
 */
@Entity
@Table(name="jws_dynamic_rest_role_association")
@NamedQuery(name="JwsDynamicRestRoleAssociation.findAll", query="SELECT j FROM JwsDynamicRestRoleAssociation j")
public class JwsDynamicRestRoleAssociation implements Serializable {
	
	private static final long serialVersionUID 			= 1L;

	@EmbeddedId
	private JwsDynamicRestRoleAssociationPK id			= null;
	
	@ManyToOne
	@JoinColumn(name="jws_dynamic_rest_id", nullable=false, insertable=false, updatable=false)
	private JwsDynamicRestDetail jwsDynamicRestDetail	= null;

	@ManyToOne
	@JoinColumn(name="role_id", nullable=false, insertable=false, updatable=false)
	private UserRole userRole							= null;

	public JwsDynamicRestRoleAssociation() {
	}

	public JwsDynamicRestRoleAssociation(JwsDynamicRestRoleAssociationPK id, UserRole userRole) {
		this.id 		= id;
		this.userRole 	= userRole;
	}

	/**
	 * @return the id
	 */
	public JwsDynamicRestRoleAssociationPK getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(JwsDynamicRestRoleAssociationPK id) {
		this.id = id;
	}

	/**
	 * @return the jwsDynamicRestDetail
	 */
	public JwsDynamicRestDetail getJwsDynamicRestDetail() {
		return jwsDynamicRestDetail;
	}

	/**
	 * @param jwsDynamicRestDetail the jwsDynamicRestDetail to set
	 */
	public void setJwsDynamicRestDetail(JwsDynamicRestDetail jwsDynamicRestDetail) {
		this.jwsDynamicRestDetail = jwsDynamicRestDetail;
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
		return Objects.hash(id, jwsDynamicRestDetail, userRole);
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
		JwsDynamicRestRoleAssociation other = (JwsDynamicRestRoleAssociation) obj;
		return Objects.equals(id, other.id) && Objects.equals(jwsDynamicRestDetail, other.jwsDynamicRestDetail)
				&& Objects.equals(userRole, other.userRole);
	}

	@Override
	public String toString() {
		return "JwsDynamicRestRoleAssociation [id=" + id + ", jwsDynamicRestDetail=" + jwsDynamicRestDetail
				+ ", userRole=" + userRole + "]";
	}


	
}