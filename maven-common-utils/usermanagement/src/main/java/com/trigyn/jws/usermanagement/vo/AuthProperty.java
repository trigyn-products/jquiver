package com.trigyn.jws.usermanagement.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class AuthProperty {

	@JsonProperty("authTypes")
	private List<ConnectionDetailsJSONSpecification> authTypes = null;

	public List<ConnectionDetailsJSONSpecification> getAuthTypes() {
		return authTypes;
	}

	public void setAuthTypes(List<ConnectionDetailsJSONSpecification> authTypes) {
		this.authTypes = authTypes;
	}

}
