package com.trigyn.jws.dynarest.vo;

import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "sender")
@XmlAccessorType(XmlAccessType.FIELD)
public class SenderXMLVO {

	@XmlElement(name = "name")
	private String	name			= null;

	@XmlElement(name = "mailid")
	private String	mailId			= null;
	
	@XmlElement(name = "replyTo")
	private String	replyTo			= null;

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
	public String getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(mailId, name, replyTo);
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
		SenderXMLVO other = (SenderXMLVO) obj;
		return Objects.equals(mailId, other.mailId) && Objects.equals(name, other.name) && Objects.equals(replyTo, other.replyTo);
	}

	@Override
	public String toString() {
		return "SenderXMLVO [name=" + name + ", mailId=" + mailId + ", replyTo=" + replyTo + "]";
	}
}
