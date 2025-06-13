package com.trigyn.jws.dynarest.vo;

import java.util.Objects;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "recepient")
@XmlAccessorType(XmlAccessType.FIELD)
public class RecepientXMLVO {

	@XmlAttribute(name = "type")
	private String	recepientType	= null;

	@XmlElement
	private String	name			= null;

	@XmlElement(name = "mailid")
	private String	mailId			= null;

	public String getRecepientType() {
		return recepientType;
	}

	public void setRecepientType(String recepientType) {
		this.recepientType = recepientType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMailId() {
		return mailId;
	}

	public void setMailId(String mailId) {
		this.mailId = mailId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(mailId, name, recepientType);
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
		RecepientXMLVO other = (RecepientXMLVO) obj;
		return Objects.equals(mailId, other.mailId) && Objects.equals(name, other.name)
				&& Objects.equals(recepientType, other.recepientType);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RecepientXMLVO [recepientType=").append(recepientType).append(", name=").append(name).append(", mailId=")
				.append(mailId).append("]");
		return builder.toString();
	}
}
