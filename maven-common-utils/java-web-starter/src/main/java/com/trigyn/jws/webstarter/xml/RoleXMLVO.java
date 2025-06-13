package com.trigyn.jws.webstarter.xml;

import java.util.ArrayList;
import java.util.List;

import com.trigyn.jws.dbutils.vo.xml.XMLVO;
import com.trigyn.jws.usermanagement.entities.JwsRole;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "roleData")
@XmlAccessorType(XmlAccessType.FIELD)
public class RoleXMLVO extends XMLVO {

	@XmlElement(name = "role")
	private List<JwsRole> roleDetails = new ArrayList<>();

	public List<JwsRole> getRoleDetails() {
		return roleDetails;
	}

	public void setRoleDetails(List<JwsRole> roleDetails) {
		this.roleDetails = roleDetails;
	}

}
