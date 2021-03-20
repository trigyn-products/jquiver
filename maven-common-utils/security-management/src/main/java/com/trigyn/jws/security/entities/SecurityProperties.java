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
@Table(name = "jq_security_properties")
public class SecurityProperties implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID		= -1236888556975800761L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "security_properties_id")
	private String				securityPropertiesId	= null;

	@Column(name = "security_type_id")
	private String				securityTypeId			= null;

	@Column(name = "security_property_name")
	private String				securityPropertyName	= null;

	@Column(name = "security_property_value")
	private String				securityPropertyValue	= null;

	public SecurityProperties() {

	}

	public SecurityProperties(String securityPropertiesId, String securityTypeId, String securityPropertyName,
			String securityPropertyValue) {
		this.securityPropertiesId	= securityPropertiesId;
		this.securityTypeId			= securityTypeId;
		this.securityPropertyName	= securityPropertyName;
		this.securityPropertyValue	= securityPropertyValue;
	}

	public String getSecurityPropertiesId() {
		return securityPropertiesId;
	}

	public void setSecurityPropertiesId(String securityPropertiesId) {
		this.securityPropertiesId = securityPropertiesId;
	}

	public String getSecurityTypeId() {
		return securityTypeId;
	}

	public void setSecurityTypeId(String securityTypeId) {
		this.securityTypeId = securityTypeId;
	}

	public String getSecurityPropertyName() {
		return securityPropertyName;
	}

	public void setSecurityPropertyName(String securityPropertyName) {
		this.securityPropertyName = securityPropertyName;
	}

	public String getSecurityPropertyValue() {
		return securityPropertyValue;
	}

	public void setSecurityPropertyValue(String securityPropertyValue) {
		this.securityPropertyValue = securityPropertyValue;
	}

	@Override
	public int hashCode() {
		return Objects.hash(securityPropertiesId, securityPropertyName, securityPropertyValue, securityTypeId);
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
		SecurityProperties other = (SecurityProperties) obj;
		return Objects.equals(securityPropertiesId, other.securityPropertiesId)
				&& Objects.equals(securityPropertyName, other.securityPropertyName)
				&& Objects.equals(securityPropertyValue, other.securityPropertyValue)
				&& Objects.equals(securityTypeId, other.securityTypeId);
	}

	@Override
	public String toString() {
		return "SecurityProperties [securityPropertiesId=" + securityPropertiesId + ", securityTypeId=" + securityTypeId
				+ ", securityPropertyName=" + securityPropertyName + ", securityPropertyValue=" + securityPropertyValue
				+ "]";
	}

}
