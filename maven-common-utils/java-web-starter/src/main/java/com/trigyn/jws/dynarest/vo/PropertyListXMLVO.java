package com.trigyn.jws.dynarest.vo;

import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "property")
@XmlAccessorType(XmlAccessType.FIELD)
public class PropertyListXMLVO {

	@XmlAttribute(name = "name")
	private String	name		= null;

	@XmlValue
	@XmlJavaTypeAdapter(value = CDATAAdapter.class)
	private String	value	= null;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PropertyListXMLVO other = (PropertyListXMLVO) obj;
		return Objects.equals(name, other.name) && Objects.equals(value, other.value);
	}

	@Override
	public String toString() {
		return "PropertyListXMLVO [name=" + name + ", value=" + value + "]";
	}
	
}
