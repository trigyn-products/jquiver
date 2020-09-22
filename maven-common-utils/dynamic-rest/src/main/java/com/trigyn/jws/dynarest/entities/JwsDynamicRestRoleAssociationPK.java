package com.trigyn.jws.dynarest.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.*;

/**
 * The primary key class for the jws_dynamic_rest_role_association database table.
 * 
 */
@Embeddable
public class JwsDynamicRestRoleAssociationPK implements Serializable {
	
	private static final long serialVersionUID 		= 1L;

	@Column(name="jws_dynamic_rest_id", insertable=false, updatable=false, nullable=false)
	private Integer jwsDynamicRestId				= null;

	@Column(name="role_id", insertable=false, updatable=false, nullable=false)
	private String roleId							= null;

	public JwsDynamicRestRoleAssociationPK() {
		
	}

	public JwsDynamicRestRoleAssociationPK(Integer jwsDynamicRestId, String roleId) {
		this.jwsDynamicRestId 	= jwsDynamicRestId;
		this.roleId 			= roleId;
	}

	/**
	 * @return the jwsDynamicRestId
	 */
	public Integer getJwsDynamicRestId() {
		return jwsDynamicRestId;
	}

	/**
	 * @param jwsDynamicRestId the jwsDynamicRestId to set
	 */
	public void setJwsDynamicRestId(Integer jwsDynamicRestId) {
		this.jwsDynamicRestId = jwsDynamicRestId;
	}

	/**
	 * @return the roleId
	 */
	public String getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId the roleId to set
	 */
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
	
}