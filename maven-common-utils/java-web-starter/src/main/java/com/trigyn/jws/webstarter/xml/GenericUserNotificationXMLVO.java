package com.trigyn.jws.webstarter.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.trigyn.jws.dbutils.vo.xml.XMLVO;
import com.trigyn.jws.notification.entities.GenericUserNotification;

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
