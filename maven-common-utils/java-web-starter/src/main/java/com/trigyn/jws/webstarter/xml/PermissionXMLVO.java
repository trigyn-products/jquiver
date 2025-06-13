package com.trigyn.jws.webstarter.xml;

import java.util.ArrayList;
import java.util.List;

import com.trigyn.jws.dbutils.vo.xml.XMLVO;
import com.trigyn.jws.usermanagement.entities.JwsEntityRoleAssociation;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "jwsEntityRoleAssociationData")
@XmlAccessorType(XmlAccessType.FIELD)
public class PermissionXMLVO extends XMLVO {

	@XmlElement(name = "jwsEntityRoleAssociation")
	private List<JwsEntityRoleAssociation> jwsRoleDetails = new ArrayList<>();

	public List<JwsEntityRoleAssociation> getJwsRoleDetails() {
		return jwsRoleDetails;
	}

	public void setJwsRoleDetails(List<JwsEntityRoleAssociation> jwsRoleDetails) {
		this.jwsRoleDetails = jwsRoleDetails;
	}

}
