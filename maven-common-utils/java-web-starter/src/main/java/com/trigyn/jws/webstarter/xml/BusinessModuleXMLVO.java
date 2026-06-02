package com.trigyn.jws.webstarter.xml;

import java.util.ArrayList;
import java.util.List;

import com.trigyn.jws.dbutils.entities.JwsBusinessModule;
import com.trigyn.jws.dbutils.vo.xml.XMLVO;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "businessModuleData")
@XmlAccessorType(XmlAccessType.FIELD)
public class BusinessModuleXMLVO extends XMLVO{
	
	@XmlElement(name = "businessModuleDetails")
	private List<JwsBusinessModule> businessModuleDetails = new ArrayList<>();

	public List<JwsBusinessModule> getBusinessModuleDetails() {
		return businessModuleDetails;
	}

	public void setBusinessModuleDetails(List<JwsBusinessModule> businessModuleDetails) {
		this.businessModuleDetails = businessModuleDetails;
	}

}
