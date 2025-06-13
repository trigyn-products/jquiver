package com.trigyn.jws.webstarter.xml;

import java.util.ArrayList;
import java.util.List;

import com.trigyn.jws.dbutils.vo.xml.XMLVO;
import com.trigyn.jws.webstarter.vo.GenericUserNotification;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "genericUserNotificationData")
@XmlAccessorType(XmlAccessType.FIELD)
public class GenericUserNotificationXMLVO extends XMLVO {

	@XmlElement(name = "genericUserNotification")
	private List<GenericUserNotification> genericUserNotificationDetails = new ArrayList<>();

	public List<GenericUserNotification> getGenericUserNotificationDetails() {
		return genericUserNotificationDetails;
	}

	public void setGenericUserNotificationDetails(List<GenericUserNotification> genericUserNotificationDetails) {
		this.genericUserNotificationDetails = genericUserNotificationDetails;
	}

}
