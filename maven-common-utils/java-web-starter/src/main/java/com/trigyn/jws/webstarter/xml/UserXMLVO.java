package com.trigyn.jws.webstarter.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.trigyn.jws.dbutils.vo.xml.XMLVO;
import com.trigyn.jws.usermanagement.entities.JwsUser;

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
