package app.trigyn.common.dashboard.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "dashlet_property_configuration")
public class DashletPropertyConfiguration implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	@EmbeddedId
	private DashletPropertyConfigurationPK id		= null;
	
	@Column(name = "property_value", nullable = false)
	private String				propertyValue		= null;


	public DashletPropertyConfiguration() {
		
	}

	public DashletPropertyConfiguration(DashletPropertyConfigurationPK id, String propertyValue) {
		this.id 			= id;
		this.propertyValue 	= propertyValue;
	}

	/**
	 * @return the id
	 */
	public DashletPropertyConfigurationPK getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(DashletPropertyConfigurationPK id) {
		this.id = id;
	}

	/**
	 * @return the propertyValue
	 */
	public String getPropertyValue() {
		return propertyValue;
	}

	/**
	 * @param propertyValue the propertyValue to set
	 */
	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, propertyValue);
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
		DashletPropertyConfiguration other = (DashletPropertyConfiguration) obj;
		return Objects.equals(id, other.id) && Objects.equals(propertyValue, other.propertyValue);
	}

	@Override
	public String toString() {
		return "DashletPropertyConfiguration [id=" + id + ", propertyValue=" + propertyValue + "]";
	}


}
