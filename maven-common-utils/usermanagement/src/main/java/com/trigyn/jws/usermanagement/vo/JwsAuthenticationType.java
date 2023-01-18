
package com.trigyn.jws.usermanagement.vo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JwsAuthenticationType {

	@JsonProperty("name")
	private String				name					;

	@JsonProperty("type")
	private String				type					;

	@JsonProperty("textValue")
	private String				textValue				;

	@JsonProperty("required")
	private Boolean				required				;

	@JsonProperty("value")
	private String				value					;

	@JsonIgnore
	@JsonProperty("selected")
	private Boolean				selected				;

	@JsonIgnore
	@JsonProperty("selectedValue")
	private String				selectedValue			;

	@JsonIgnore
	@JsonProperty("defaultValue")
	private String				defaultValue			;

	@JsonIgnore
	@JsonProperty("dropDownData")
	private List<DropDownData>	dropDownData			;

	@JsonIgnore
	private Map<String, Object>	additionalProperties	= new HashMap<String, Object>();

	@JsonIgnore
	@JsonProperty("authenticationId")
	private Integer				authenticationId		;

	@JsonProperty("configurationType")
	private String				configurationType		;

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

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	@JsonProperty("selected")
	public Boolean getSelected() {
		return selected;
	}

	@JsonProperty("selected")
	public void setSelected(Boolean selected) {
		this.selected = selected;
	}

	@JsonProperty("dropDownData")
	public List<DropDownData> getDropDownData() {
		return dropDownData;
	}

	@JsonProperty("dropDownData")
	public void setDropDownData(List<DropDownData> dropDownData) {
		this.dropDownData = dropDownData;
	}

	@JsonProperty("authenticationId")
	public Integer getAuthenticationId() {
		return authenticationId;
	}

	@JsonProperty("configurationType")
	public String getConfigurationType() {
		return configurationType;
	}

}
