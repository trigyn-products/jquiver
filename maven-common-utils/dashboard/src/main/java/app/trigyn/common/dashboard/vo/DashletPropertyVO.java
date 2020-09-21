package app.trigyn.common.dashboard.vo;

import java.io.Serializable;
import java.util.Objects;

public class DashletPropertyVO implements Serializable{

	private static final long serialVersionUID 		= -6176242812943728378L;

	private String				dashletPropertyId	= null;
	
	private String				placeholderName		= null;

	private String				displayName			= null;

	private String				type				= null;

	private String				value				= null;

	private String				defaultValue		= null;

	private String				configurationScript	= null;

	private Integer				toDisplay			= null;
	
	private Integer				sequence			= null;

	public DashletPropertyVO() {
		
	}

	public DashletPropertyVO(String dashletPropertyId, String placeholderName, String displayName, String type, String value, String defaultValue, String configurationScript, Integer toDisplay, Integer sequence) {
		this.dashletPropertyId 		= dashletPropertyId;
		this.placeholderName 		= placeholderName;
		this.displayName 			= displayName;
		this.type 					= type;
		this.value 					= value;
		this.defaultValue 			= defaultValue;
		this.configurationScript 	= configurationScript;
		this.toDisplay 				= toDisplay;
		this.sequence 				= sequence;
	}

	/**
	 * @return the dashletPropertyId
	 */
	public String getPropertyId() {
		return dashletPropertyId;
	}

	/**
	 * @param dashletPropertyId the dashletPropertyId to set
	 */
	public void setPropertyId(String dashletPropertyId) {
		this.dashletPropertyId = dashletPropertyId;
	}

	/**
	 * @return the placeholderName
	 */
	public String getPlaceholderName() {
		return placeholderName;
	}

	/**
	 * @param placeholderName the placeholderName to set
	 */
	public void setPlaceholderName(String placeholderName) {
		this.placeholderName = placeholderName;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * @param defaultValue the defaultValue to set
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * @return the configurationScript
	 */
	public String getConfigurationScript() {
		return configurationScript;
	}

	/**
	 * @param configurationScript the configurationScript to set
	 */
	public void setConfigurationScript(String configurationScript) {
		this.configurationScript = configurationScript;
	}

	/**
	 * @return the toDisplay
	 */
	public Integer getToDisplay() {
		return toDisplay;
	}

	/**
	 * @param toDisplay the toDisplay to set
	 */
	public void setToDisplay(Integer toDisplay) {
		this.toDisplay = toDisplay;
	}

	/**
	 * @return the sequence
	 */
	public Integer getSequence() {
		return sequence;
	}

	/**
	 * @param sequence the sequence to set
	 */
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	@Override
	public int hashCode() {
		return Objects.hash(configurationScript, dashletPropertyId, defaultValue, displayName, placeholderName, sequence, toDisplay, type, value);
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
		return Objects.equals(configurationScript, other.configurationScript) && Objects.equals(dashletPropertyId, other.dashletPropertyId) && Objects.equals(defaultValue, other.defaultValue)
		        && Objects.equals(displayName, other.displayName) && Objects.equals(placeholderName, other.placeholderName) && Objects.equals(sequence, other.sequence) && Objects.equals(toDisplay, other.toDisplay)
		        && Objects.equals(type, other.type) && Objects.equals(value, other.value);
	}

	@Override
	public String toString() {
		return "DashletPropertyVO [dashletPropertyId=" + dashletPropertyId + ", placeholderName=" + placeholderName + ", displayName=" + displayName + ", type=" + type + ", value=" + value + ", defaultValue=" + defaultValue
		        + ", configurationScript=" + configurationScript + ", toDisplay=" + toDisplay + ", sequence=" + sequence + "]";
	}

	
	
	
}
