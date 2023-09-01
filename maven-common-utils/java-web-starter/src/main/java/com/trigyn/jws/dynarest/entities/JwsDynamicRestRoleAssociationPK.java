package com.trigyn.jws.dynarest.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class JwsDynamicRestRoleAssociationPK implements Serializable {

	private static final long	serialVersionUID	= 1L;

	@Column(name = "jws_dynamic_rest_id", insertable = false, updatable = false, nullable = false)
	private String				jwsDynamicRestId	= null;

	@Column(name = "role_id", insertable = false, updatable = false, nullable = false)
	private String				roleId				= null;

	public JwsDynamicRestRoleAssociationPK() {

	}

	public JwsDynamicRestRoleAssociationPK(String jwsDynamicRestId, String roleId) {
		this.jwsDynamicRestId	= jwsDynamicRestId;
		this.roleId				= roleId;
	}

	public String getJwsDynamicRestId() {
		return jwsDynamicRestId;
	}

	public void setJwsDynamicRestId(String jwsDynamicRestId) {
		this.jwsDynamicRestId = jwsDynamicRestId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(jwsDynamicRestId, roleId);
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
		JwsDynamicRestRoleAssociationPK other = (JwsDynamicRestRoleAssociationPK) obj;
		return Objects.equals(jwsDynamicRestId, other.jwsDynamicRestId) && Objects.equals(roleId, other.roleId);
	}

	@Override
	public String toString() {
		return "JwsDynamicRestRoleAssociationPK [jwsDynamicRestId=" + jwsDynamicRestId + ", roleId=" + roleId + "]";
	}

	public JwsDynamicRestRoleAssociationPK getObject() {
		JwsDynamicRestRoleAssociationPK pk = new JwsDynamicRestRoleAssociationPK();
		pk.setJwsDynamicRestId(jwsDynamicRestId != null ? jwsDynamicRestId.trim() : jwsDynamicRestId);
		pk.setRoleId(roleId != null ? roleId.trim() : roleId);
		return pk;
	}
}