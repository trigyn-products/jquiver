package com.trigyn.jws.dynarest.vo;

import java.util.List;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "header")
@XmlAccessorType(XmlAccessType.FIELD)
public class EmailHeaderPropertyXMLVO {

	@XmlElement(name = "property")
	private List<PropertyListXMLVO> propertyName = null;

	public List<PropertyListXMLVO> getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(List<PropertyListXMLVO> propertyName) {
		this.propertyName = propertyName;
	}

	@Override
	public int hashCode() {
		return Objects.hash(propertyName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmailHeaderPropertyXMLVO other = (EmailHeaderPropertyXMLVO) obj;
		return Objects.equals(propertyName, other.propertyName);
	}

	@Override
	public String toString() {
		return "EmailHeaderPropertyXMLVO [propertyName=" + propertyName + "]";
	}

}
