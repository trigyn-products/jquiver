package com.trigyn.jws.dashboard.vo;

import java.io.Serializable;
import java.util.Objects;

public class DashletPropertyVO implements Serializable {

	private static final long	serialVersionUID	= -6176242812943728378L;

	private String				dashletPropertyId	= null;

	private String				placeholderName		= null;

	private String				displayName			= null;

	private String				type				= null;

	private String				value				= null;
	
	private String				validation			= null;

	private String				defaultValue		= null;

	private String				configurationScript	= null;

	private Integer				toDisplay			= null;

	private Integer				sequence			= null;

	private Integer				isDeleted			= null;

	public DashletPropertyVO() {

	}

	// Used to display dashlet property from config
	public DashletPropertyVO(String dashletPropertyId, String displayName, String type, String value, String validation, String defaultValue) {
		this.dashletPropertyId	= dashletPropertyId;
		this.displayName		= displayName;
		this.type				= type;
		this.value				= value;
		this.validation 		= validation;
		this.defaultValue		= defaultValue;
	}

	public DashletPropertyVO(String dashletPropertyId, String placeholderName, String displayName, String type, String value,
			 String validation,String defaultValue, String configurationScript, Integer toDisplay, Integer sequence, Integer isDeleted) {
		this.dashletPropertyId		= dashletPropertyId;
		this.placeholderName		= placeholderName;
		this.displayName			= displayName;
		this.type					= type;
		this.value					= value;
		this.validation 			= validation;
		this.defaultValue			= defaultValue;
		this.configurationScript	= configurationScript;
		this.toDisplay				= toDisplay;
		this.sequence				= sequence;
		this.isDeleted				= isDeleted;
	}

	public String getPropertyId() {
		return dashletPropertyId;
	}

	public void setPropertyId(String dashletPropertyId) {
		this.dashletPropertyId = dashletPropertyId;
	}

	public String getPlaceholderName() {
		return placeholderName;
	}

	public void setPlaceholderName(String placeholderName) {
		this.placeholderName = placeholderName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getConfigurationScript() {
		return configurationScript;
	}

	public void setConfigurationScript(String configurationScript) {
		this.configurationScript = configurationScript;
	}

	public Integer getToDisplay() {
		return toDisplay;
	}

	public void setToDisplay(Integer toDisplay) {
		this.toDisplay = toDisplay;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	/**
	 * @return the isDeleted
	 */
	public Integer getIsDeleted() {
		return isDeleted;
	}

	/**
	 * @param isDeleted the isDeleted to set
	 */
	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getValidation() {
		return validation;
	}

	public void setValidation(String validation) {
		this.validation = validation;
	}

	@Override
	public int hashCode() {
		return Objects.hash(configurationScript, dashletPropertyId, defaultValue, displayName, isDeleted, placeholderName, sequence,
				toDisplay, type, value, validation);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DashletPropertyVO other = (DashletPropertyVO) obj;
		return Objects.equals(configurationScript, other.configurationScript) && Objects.equals(dashletPropertyId, other.dashletPropertyId)
				&& Objects.equals(defaultValue, other.defaultValue) && Objects.equals(displayName, other.displayName)
				&& Objects.equals(isDeleted, other.isDeleted) && Objects.equals(placeholderName, other.placeholderName)
				&& Objects.equals(sequence, other.sequence) && Objects.equals(toDisplay, other.toDisplay)
				&& Objects.equals(type, other.type) && Objects.equals(value, other.value) 
				&& Objects.equals(validation, other.validation);
	}

	@Override
	public String toString() {
		return "DashletPropertyVO [dashletPropertyId=" + dashletPropertyId + ", placeholderName=" + placeholderName + ", displayName="
				+ displayName + ", type=" + type + ", value=" + value + ", validation=" + validation + ", defaultValue=" + defaultValue + ", configurationScript="
				+ configurationScript + ", toDisplay=" + toDisplay + ", sequence=" + sequence + ", isDeleted=" + isDeleted + "]";
	}

}
