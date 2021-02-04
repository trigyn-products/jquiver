package com.trigyn.jws.webstarter.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.trigyn.jws.dbutils.vo.xml.XMLVO;
import com.trigyn.jws.usermanagement.entities.JwsEntityRoleAssociation;

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
