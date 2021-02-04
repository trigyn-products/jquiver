package com.trigyn.jws.usermanagement.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "jws_authentication_type")
public class JwsAuthenticationType {

	@Id
	@Column(name = "authentication_id")
	private Integer	authenticationId			= null;

	@Column(name = "authentication_name")
	private String	authenticationName			= null;

	@Column(name = "authentication_properties")
	private String	authenticationProperties	= null;

	public Integer getAuthenticationId() {
		return authenticationId;
	}

	public void setAuthenticationId(Integer authenticationId) {
		this.authenticationId = authenticationId;
	}

	public String getAuthenticationName() {
		return authenticationName;
	}

	public void setAuthenticationName(String authenticationName) {
		this.authenticationName = authenticationName;
	}

	public String getAuthenticationProperties() {
		return authenticationProperties;
	}

	public void setAuthenticationProperties(String authenticationProperties) {
		this.authenticationProperties = authenticationProperties;
	}

}
