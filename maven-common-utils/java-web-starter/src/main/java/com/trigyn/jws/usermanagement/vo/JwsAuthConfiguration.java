
package com.trigyn.jws.usermanagement.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JwsAuthConfiguration {

	@JsonProperty("name")
	private String							name					= null;

	@JsonProperty("type")
	private String							type					= null;

	@JsonProperty("textValue")
	private String							textValue				= null;

	@JsonProperty("required")
	private Boolean							required				= null;

	@JsonProperty("value")
	private String							value					= null;

	@JsonIgnore
	@JsonProperty("defaultValue")
	private String							defaultValue			= null;

	@JsonIgnore
	@JsonProperty("selected")
	private Boolean							selected				= null;

	@JsonIgnore
	@JsonProperty("selectedValue")
	private String							selectedValue			= null;

	@JsonIgnore
	@JsonProperty("dropDownData")
	private List<DropDownData>				dropDownData			= null;

	@JsonProperty("additionalDetails")
	private AdditionalDetails				additionalDetails		= null;
	
	@JsonIgnore
	@JsonProperty("condition")
	private String				condition							= null;

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

	@JsonProperty("selected")
	public Boolean getSelected() {
		return selected;
	}

	@JsonProperty("selected")
	public void setSelected(Boolean selected) {
		this.selected = selected;
	}

	@JsonProperty("textValue")
	public String getTextValue() {
		return textValue;
	}

	@JsonProperty("textValue")
	public void setTextValue(String textValue) {
		this.textValue = textValue;
	}

	@JsonProperty("required")
	public Boolean getRequired() {
		return required;
	}

	@JsonProperty("required")
	public void setRequired(Boolean required) {
		this.required = required;
	}

	@JsonProperty("value")
	public String getValue() {
		return value;
	}

	@JsonProperty("value")
	public void setValue(String value) {
		this.value = value;
	}

	@JsonProperty("selectedValue")
	public String getSelectedValue() {
		return selectedValue;
	}

	@JsonProperty("selectedValue")
	public void setSelectedValue(String selectedValue) {
		this.selectedValue = selectedValue;
	}

	@JsonProperty("defaultValue")
	public String getDefaultValue() {
		return defaultValue;
	}

	@JsonProperty("defaultValue")
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	@JsonProperty("dropDownData")
	public List<DropDownData> getDropDownData() {
		return dropDownData;
	}

	@JsonProperty("dropDownData")
	public void setDropDownData(List<DropDownData> dropDownData) {
		this.dropDownData = dropDownData;
	}

	@JsonProperty("additionalDetails")
	public AdditionalDetails getAdditionalDetails() {
		return additionalDetails;
	}

	@JsonProperty("additionalDetails")
	public void setAdditionalDetails(AdditionalDetails additionalDetails) {
		this.additionalDetails = additionalDetails;
	}
	
	@JsonProperty("condition")
	public String getCondition() {
		return condition;
	}

	@JsonProperty("condition")
	public void setCondition(String condition) {
		this.condition = condition;
	}

}
