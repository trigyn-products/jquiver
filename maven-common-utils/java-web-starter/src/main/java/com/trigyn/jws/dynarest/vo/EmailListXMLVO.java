package com.trigyn.jws.dynarest.vo;

import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "emails")
@XmlAccessorType(XmlAccessType.FIELD)
public class EmailListXMLVO {

	@XmlElement(name = "email")
	private List<EmailXMLVO> emailXMLVO = null;

	public List<EmailXMLVO> getEmailXMLVO() {
		return emailXMLVO;
	}

	public void setEmailXMLVO(List<EmailXMLVO> emailXMLVO) {
		this.emailXMLVO = emailXMLVO;
	}

}
