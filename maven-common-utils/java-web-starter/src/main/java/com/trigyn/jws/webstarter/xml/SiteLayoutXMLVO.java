package com.trigyn.jws.webstarter.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.trigyn.jws.dbutils.entities.ModuleListing;
import com.trigyn.jws.dbutils.vo.xml.XMLVO;

@XmlRootElement(name = "siteLayoutData")
@XmlAccessorType (XmlAccessType.FIELD)
public class SiteLayoutXMLVO extends XMLVO {
	
	@XmlElement(name = "siteLayout")
    private List<ModuleListing> moduleListingDetails = new ArrayList<>();

	public List<ModuleListing> getModuleListingDetails() {
		return moduleListingDetails;
	}

	public void setModuleListingDetails(List<ModuleListing> moduleListingDetails) {
		this.moduleListingDetails = moduleListingDetails;
	}

}
