
package com.trigyn.jws.usermanagement.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdditionalDetails {

	@JsonProperty("additionalProperties")
	private List<List<JwsAuthAdditionalProperty>> additionalProperties = null;

	@JsonProperty("additionalProperties")
	public List<List<JwsAuthAdditionalProperty>> getAdditionalProperties() {
		return additionalProperties;
	}

	@JsonProperty("additionalProperties")
	public void setAdditionalProperties(List<List<JwsAuthAdditionalProperty>> additionalProperties) {
		this.additionalProperties = additionalProperties;
	}

}
