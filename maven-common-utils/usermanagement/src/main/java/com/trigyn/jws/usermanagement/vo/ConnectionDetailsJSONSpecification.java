
package com.trigyn.jws.usermanagement.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConnectionDetailsJSONSpecification {

	@JsonProperty("authenticationType")
	private JwsAuthenticationType	authenticationType		= null;
	@JsonProperty("authenticationDetail")
	private AuthenticationDetails	authenticationDetail	= null;

	@JsonProperty("authenticationType")
	public JwsAuthenticationType getAuthenticationType() {
		return authenticationType;
	}

	@JsonProperty("authenticationType")
	public void setAuthenticationType(JwsAuthenticationType authenticationType) {
		this.authenticationType = authenticationType;
	}

	@JsonProperty("authenticationDetail")
	public AuthenticationDetails getAuthenticationDetails() {
		return authenticationDetail;
	}

	@JsonProperty("authenticationDetail")
	public void setAuthenticationDetails(AuthenticationDetails authenticationDetails) {
		this.authenticationDetail = authenticationDetails;
	}

}
