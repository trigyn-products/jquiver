package com.trigyn.jws.dashboard.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import com.trigyn.jws.dashboard.vo.DashletPropertyVO;

@Entity
@Table(name = "jq_dashlet_properties")
@Where(clause = "is_deleted = 0")
public class DashletProperties implements Serializable {

	private static final long					serialVersionUID				= 1L;

	@Id
	@Column(name = "property_id", nullable = false)
	@GeneratedValue(generator = "inquisitive-uuid")
	@GenericGenerator(name = "inquisitive-uuid", strategy = "com.trigyn.jws.dbutils.configurations.CustomUUIDGenerator")
	private String								propertyId						= null;

	@Column(name = "dashlet_id")
	private String								dashletId						= null;

	@Column(name = "placeholder_name")
	private String								placeholderName					= null;

	@Column(name = "display_name")
	private String								displayName						= null;

	@Column(name = "type_id")
	private String								type							= null;

	@Column(name = "value")
	private String								value							= null;
	
	@Column(name = "validation")
	private String								validation							= null;

	@Column(name = "default_value")
	private String								defaultValue					= null;

	@Column(name = "configuration_script")
	private String								configurationScript				= null;

	@Column(name = "is_deleted")
	private Integer								isDeleted						= null;

	@Column(name = "to_display")
	private Integer								toDisplay						= null;

	@Column(name = "sequence")
	private Integer								sequence						= null;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "dashlet_id", referencedColumnName = "dashlet_id", nullable = false, insertable = false, updatable = false)
	private Dashlet								dashlet							= null;

	@OneToMany(mappedBy = "dashletProperties", fetch = FetchType.LAZY)
	private List<DashletPropertyConfiguration>	dashletPropertyConfigurations	= new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "type_id", referencedColumnName = "lookup_category_id", nullable = false, insertable = false, updatable = false)
	private DashboardLookupCategory				dashboardLookupCategory			= null;

	public DashletProperties() {

	}

	public DashletProperties(String propertyId, String dashletId, String placeholderName, String displayName, String type, String value,
			String validation, String defaultValue, String configurationScript, Integer isDeleted, Integer toDisplay, Integer sequence) {
		this.propertyId				= propertyId;
		this.dashletId				= dashletId;
		this.placeholderName		= placeholderName;
		this.displayName			= displayName;
		this.type					= type;
		this.value					= value;
		this.validation 			= validation;
		this.defaultValue			= defaultValue;
		this.configurationScript	= configurationScript;
		this.isDeleted				= isDeleted;
		this.toDisplay				= toDisplay;
		this.sequence				= sequence;
	}

	public DashletProperties(String propertyId, String dashletId, String placeholderName, String displayName, String type, String value,
			String validation, String defaultValue, String configurationScript, Integer isDeleted, Integer toDisplay, Integer sequence, Dashlet dashlet) {
		this.propertyId				= propertyId;
		this.dashletId				= dashletId;
		this.placeholderName		= placeholderName;
		this.displayName			= displayName;
		this.type					= type;
		this.value					= value;
		this.validation 			= validation;
		this.defaultValue			= defaultValue;
		this.configurationScript	= configurationScript;
		this.isDeleted				= isDeleted;
		this.toDisplay				= toDisplay;
		this.sequence				= sequence;
		this.dashlet				= dashlet;
	}
	
	public String getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	public String getDashletId() {
		return dashletId;
	}

	public void setDashletId(String dashletId) {
		this.dashletId = dashletId;
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

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
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

	public Dashlet getDashlet() {
		return dashlet;
	}

	public void setDashlet(Dashlet dashlet) {
		this.dashlet = dashlet;
	}

	public String getValidation() {
		return validation;
	}

	public void setValidation(String validation) {
		this.validation = validation;
	}

	@Override
	public int hashCode() {
		return Objects.hash(configurationScript, dashlet, dashletId, defaultValue, displayName, isDeleted, placeholderName, propertyId,
				sequence, toDisplay, type, value, validation);
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
		DashletProperties other = (DashletProperties) obj;
		return Objects.equals(configurationScript, other.configurationScript) && Objects.equals(dashlet, other.dashlet)
				&& Objects.equals(dashletId, other.dashletId) && Objects.equals(defaultValue, other.defaultValue)
				&& Objects.equals(displayName, other.displayName) && Objects.equals(isDeleted, other.isDeleted)
				&& Objects.equals(placeholderName, other.placeholderName) && Objects.equals(propertyId, other.propertyId)
				&& Objects.equals(sequence, other.sequence) && Objects.equals(toDisplay, other.toDisplay)
				&& Objects.equals(type, other.type) && Objects.equals(value, other.value)
				&& Objects.equals(validation, other.validation);
	}

	@Override
	public String toString() {
		return "DashletProperties [propertyId=" + propertyId + ", dashletId=" + dashletId + ", placeholderName=" + placeholderName
				+ ", displayName=" + displayName + ", type=" + type + ", value=" + value + ", , validation=" + validation + " , defaultValue=" + defaultValue
				+ ", configurationScript=" + configurationScript + ", isDeleted=" + isDeleted + ", toDisplay=" + toDisplay + ", sequence="
				+ sequence + ", dashlet=" + dashlet + " ]";
	}
	
	public DashletProperties getObj() {
		DashletProperties obj = new DashletProperties();

		obj.setConfigurationScript(configurationScript != null ? configurationScript.trim() : configurationScript);
		obj.setPropertyId(dashletId != null ? dashletId.trim() : dashletId);
		obj.setDefaultValue(defaultValue != null ? defaultValue.trim() : defaultValue);
		obj.setDisplayName(displayName != null ? displayName.trim() : displayName);
		obj.setIsDeleted(isDeleted);
		obj.setPlaceholderName(placeholderName != null ? placeholderName.trim() : placeholderName);
		obj.setPropertyId(propertyId != null ? propertyId.trim() : propertyId);
		obj.setSequence(sequence);
		obj.setToDisplay(toDisplay);
		obj.setType(type != null ? type.trim() : type);
		obj.setValue(value != null ? value.trim() : value);
		obj.setValidation(validation != null ? validation.trim() : validation);

		return obj;
	}
	

	public DashletPropertyVO getObject() {
		DashletPropertyVO obj = new DashletPropertyVO();

		obj.setConfigurationScript(configurationScript != null ? configurationScript.trim() : configurationScript);
		obj.setPropertyId(dashletId != null ? dashletId.trim() : dashletId);
		obj.setDefaultValue(defaultValue != null ? defaultValue.trim() : defaultValue);
		obj.setDisplayName(displayName != null ? displayName.trim() : displayName);
		obj.setIsDeleted(isDeleted);
		obj.setPlaceholderName(placeholderName != null ? placeholderName.trim() : placeholderName);
		obj.setPropertyId(propertyId != null ? propertyId.trim() : propertyId);
		obj.setSequence(sequence);
		obj.setToDisplay(toDisplay);
		obj.setType(type != null ? type.trim() : type);
		obj.setValue(value != null ? value.trim() : value);
		obj.setValidation(validation != null ? validation.trim() : validation);

		return obj;
	}
}
