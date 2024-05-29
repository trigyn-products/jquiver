package com.trigyn.jws.usermanagement.vo;

import com.trigyn.jws.usermanagement.entities.JwsAuthenticationType;

public class JwsAuthenticationTypeVO {

	private Integer	id							= null;

	private String	authenticationName			= null;

	private String	authenticationProperties	= null;
	
	private String	defaultAuthProperties		= null;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public JwsAuthenticationTypeVO convertEntityToVO(JwsAuthenticationType authenticationType) {
		if(authenticationType.getAuthenticationProperties()!=null) {
			this.id							= authenticationType.getAuthenticationId();
			this.authenticationName			= authenticationType.getAuthenticationName();
			this.authenticationProperties	= authenticationType.getAuthenticationProperties();
			this.defaultAuthProperties 		= authenticationType.getDefaultAuthProperties();
			return this;
		}
		return null;
		
	}

	public String getDefaultAuthProperties() {
		return defaultAuthProperties;
	}

	public void setDefaultAuthProperties(String defaultAuthProperties) {
		this.defaultAuthProperties = defaultAuthProperties;
	}

}
