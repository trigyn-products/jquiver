package com.trigyn.jws.webstarter.xml;

import java.util.ArrayList;
import java.util.List;

import com.trigyn.jws.dbutils.entities.ModuleListing;
import com.trigyn.jws.dbutils.vo.xml.XMLVO;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "routerData")
@XmlAccessorType(XmlAccessType.FIELD)
public class SiteLayoutXMLVO extends XMLVO {

	@XmlElement(name = "router")
	private List<ModuleListing> moduleListingDetails = new ArrayList<>();

	public List<ModuleListing> getModuleListingDetails() {
		return moduleListingDetails;
	}

	public void setModuleListingDetails(List<ModuleListing> moduleListingDetails) {
		this.moduleListingDetails = moduleListingDetails;
	}

}
