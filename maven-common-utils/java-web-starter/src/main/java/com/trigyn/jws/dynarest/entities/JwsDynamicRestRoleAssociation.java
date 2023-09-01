package com.trigyn.jws.dynarest.entities;

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
@Table(name = "jq_dynamic_rest_role_association")
@NamedQuery(name = "JwsDynamicRestRoleAssociation.findAll", query = "SELECT j FROM JwsDynamicRestRoleAssociation j")
public class JwsDynamicRestRoleAssociation implements Serializable {

	private static final long				serialVersionUID		= 1L;

	@EmbeddedId
	private JwsDynamicRestRoleAssociationPK	id						= null;

	@ManyToOne
	@JoinColumn(name = "jws_dynamic_rest_id", nullable = false, insertable = false, updatable = false)
	private JwsDynamicRestDetail			jwsDynamicRestDetail	= null;

	@ManyToOne
	@JoinColumn(name = "role_id", nullable = false, insertable = false, updatable = false)
	private JwsRole						userRole				= null;

	public JwsDynamicRestRoleAssociation() {
	}

	public JwsDynamicRestRoleAssociation(JwsDynamicRestRoleAssociationPK id, JwsRole userRole) {
		this.id			= id;
		this.userRole	= userRole;
	}

	public JwsDynamicRestRoleAssociationPK getId() {
		return id;
	}

	public void setId(JwsDynamicRestRoleAssociationPK id) {
		this.id = id;
	}

	public JwsDynamicRestDetail getJwsDynamicRestDetail() {
		return jwsDynamicRestDetail;
	}

	public void setJwsDynamicRestDetail(JwsDynamicRestDetail jwsDynamicRestDetail) {
		this.jwsDynamicRestDetail = jwsDynamicRestDetail;
	}

	public JwsRole getJwsRole() {
		return userRole;
	}

	public void setJwsRole(JwsRole userRole) {
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
		return "JwsDynamicRestRoleAssociation [id=" + id + ", jwsDynamicRestDetail=" + jwsDynamicRestDetail + ", userRole=" + userRole
				+ "]";
	}

	public JwsDynamicRestRoleAssociation getObject() {
		JwsDynamicRestRoleAssociation dynaRest = new JwsDynamicRestRoleAssociation();
		dynaRest.setId(id.getObject());
		return dynaRest;
	}

}