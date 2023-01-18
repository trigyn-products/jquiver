
package com.trigyn.jws.usermanagement.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DropDownData {

	@JsonProperty("name")
	private String							name					= null;
	@JsonProperty("selected")
	private Boolean							selected				= null;
	@JsonIgnore
	@JsonProperty("defaultValue")
	private String							defaultValue			= null;
	@JsonProperty("value")
	private Integer							value					= null;
	@JsonProperty("type")
	private String							type					= null;

	@JsonProperty("additionalDetails")
	private AdditionalDetails				additionalDetails		= null;

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("selected")
	public Boolean getSelected() {
		return selected;
	}

	@JsonProperty("selected")
	public void setSelected(Boolean selected) {
		this.selected = selected;
	}

	@JsonProperty("defaultValue")
	public String getDefaultValue() {
		return defaultValue;
	}

	@JsonProperty("defaultValue")
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	@JsonProperty("value")
	public Integer getValue() {
		return value;
	}

	@JsonProperty("value")
	public void setValue(Integer value) {
		this.value = value;
	}

	@JsonProperty("type")
	public String getType() {
		return type;
	}

	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
	}

	@JsonProperty("additionalDetails")
	public AdditionalDetails getAdditionalDetails() {
		return additionalDetails;
	}

	@JsonProperty("additionalDetails")
	public void setAdditionalDetails(AdditionalDetails additionalDetails) {
		this.additionalDetails = additionalDetails;
	}

}
