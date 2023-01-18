package com.trigyn.jws.usermanagement.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserManagementVo {

	@JsonProperty("isAuthenticationEnabled")
	private boolean				isAuthenticationEnabled;

	@JsonProperty("authProperties")
	private List<AuthProperty>	authProperties	= null;

	public boolean isAuthenticationEnabled() {
		return isAuthenticationEnabled;
	}

	public void setActiveCheckbox(boolean isAuthenticationEnabled) {
		this.isAuthenticationEnabled = isAuthenticationEnabled;
	}

	public List<AuthProperty> getAuthProperties() {
		return authProperties;
	}

	public void setAuthProperties(List<AuthProperty> authProperties) {
		this.authProperties = authProperties;
	}

}
