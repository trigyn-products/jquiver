package com.trigyn.jws.webstarter.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.trigyn.jws.dbutils.vo.xml.XMLVO;
import com.trigyn.jws.usermanagement.entities.JwsRole;

@XmlRootElement(name = "roleData")
@XmlAccessorType (XmlAccessType.FIELD)
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
