package com.trigyn.jws.security.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="jws_security_type")
public class SecurityType implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID 	= -5288215708996881343L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name="security_type_id")
	private String securityTypeId				= null;
	
	@Column(name = "security_name")
	private String securityName 				= null;
	
	@Column(name = "is_active")
	private Integer isActive 					= null;

	public SecurityType() {

	}

	public SecurityType(String securityTypeId, String securityName, Integer isActive) {
		this.securityTypeId 		= securityTypeId;
		this.securityName 			= securityName;
		this.isActive 				= isActive;
	}

	public String getSecurityTypeId() {
		return securityTypeId;
	}

	public void setSecurityTypeId(String securityTypeId) {
		this.securityTypeId = securityTypeId;
	}

	public String getSecurityName() {
		return securityName;
	}

	public void setSecurityName(String securityName) {
		this.securityName = securityName;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActives(Integer isActive) {
		this.isActive = isActive;
	}

	@Override
	public int hashCode() {
		return Objects.hash(securityTypeId, securityName, isActive);
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
		SecurityType other = (SecurityType) obj;
		return Objects.equals(securityTypeId, other.securityTypeId) && Objects.equals(securityName, other.securityName)
				&& Objects.equals(isActive, other.isActive);
	}

	@Override
	public String toString() {
		return "SecurityManagement [securityTypeId=" + securityTypeId + ", securityName=" + securityName
				+ ", isActive=" + isActive + "]";
	}
	
	
}
