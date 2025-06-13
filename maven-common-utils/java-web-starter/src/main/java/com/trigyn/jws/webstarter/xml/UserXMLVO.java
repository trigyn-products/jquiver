package com.trigyn.jws.webstarter.xml;

import java.util.ArrayList;
import java.util.List;

import com.trigyn.jws.dbutils.vo.xml.XMLVO;
import com.trigyn.jws.usermanagement.entities.JwsUser;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "userData")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserXMLVO extends XMLVO {

	@XmlElement(name = "user")
	private List<JwsUser> userDetails = new ArrayList<>();

	public List<JwsUser> getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(List<JwsUser> userDetails) {
		this.userDetails = userDetails;
	}

}
