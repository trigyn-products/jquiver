package com.trigyn.jws.dynarest.vo;

import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "header")
@XmlAccessorType(XmlAccessType.FIELD)
public class EmailHeaderPropertyXMLVO {

	@XmlAttribute
	private String	name			= null;

	@XmlElement(name = "property")
	private String	propertyName	= null;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, propertyName);
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
		EmailHeaderPropertyXMLVO other = (EmailHeaderPropertyXMLVO) obj;
		return Objects.equals(name, other.name) && Objects.equals(propertyName, other.propertyName);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("EmailHeaderPropertyXMLVO [name=").append(name).append(", propertyName=").append(propertyName).append("]");
		return builder.toString();
	}
}
