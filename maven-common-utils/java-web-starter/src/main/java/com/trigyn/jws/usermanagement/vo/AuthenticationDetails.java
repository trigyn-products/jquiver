
package com.trigyn.jws.usermanagement.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationDetails {

	@JsonProperty("name")
	private String								name			= null;
	@JsonProperty("type")
	private String								type			= null;
	@JsonProperty("textValue")
	private String								textValue		= null;
	@JsonProperty("value")
	private String								value			= null;
	@JsonProperty("configurations")
	private List<List<JwsAuthConfiguration>>	configurations	= null;

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("type")
	public String getType() {
		return type;
	}

	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
	}

	@JsonProperty("textValue")
	public String getTextValue() {
		return textValue;
	}

	@JsonProperty("textValue")
	public void setTextValue(String textValue) {
		this.textValue = textValue;
	}

	@JsonProperty("value")
	public String getValue() {
		return value;
	}

	@JsonProperty("value")
	public void setValue(String value) {
		this.value = value;
	}

	@JsonProperty("configurations")
	public List<List<JwsAuthConfiguration>> getConfigurations() {
		return configurations;
	}

	@JsonProperty("configurations")
	public void setConfigurations(List<List<JwsAuthConfiguration>> configurations) {
		this.configurations = configurations;
	}

}
