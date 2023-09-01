package com.trigyn.jws.dynarest.vo;

import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "failedrecepient")
@XmlAccessorType(XmlAccessType.FIELD)
public class FailedRecipientXMLVO {

	@XmlAttribute(name = "type")
	private String	failedrecepientType	= null;

	@XmlElement
	private String	name			= null;

	@XmlElement(name = "mailid")
	private String	mailId			= null;

	
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
		return Objects.hash(mailId, name, failedrecepientType);
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
		FailedRecipientXMLVO other = (FailedRecipientXMLVO) obj;
		return Objects.equals(mailId, other.mailId) && Objects.equals(name, other.name)
				&& Objects.equals(failedrecepientType, other.failedrecepientType);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FailedRecipientXMLVO [failedrecepientType=").append(failedrecepientType).append(", name=").append(name).append(", mailId=")
				.append(mailId).append("]");
		return builder.toString();
	}

	public String getFailedrecepientType() {
		return failedrecepientType;
	}

	public void setFailedrecepientType(String failedrecepientType) {
		this.failedrecepientType = failedrecepientType;
	}
}
