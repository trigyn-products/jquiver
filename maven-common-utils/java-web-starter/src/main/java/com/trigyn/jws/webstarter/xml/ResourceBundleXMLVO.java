package com.trigyn.jws.webstarter.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.trigyn.jws.dbutils.vo.xml.XMLVO;
import com.trigyn.jws.resourcebundle.entities.ResourceBundle;

@XmlRootElement(name = "resourceBundleData")
@XmlAccessorType (XmlAccessType.FIELD)
public class ResourceBundleXMLVO extends XMLVO {
	
	@XmlElement(name = "resourceBundle")
    private List<ResourceBundle> resourceBundleDetails = new ArrayList<>();

	public List<ResourceBundle> getResourceBundleDetails() {
		return resourceBundleDetails;
	}

	public void setResourceBundleDetails(List<ResourceBundle> resourceBundleDetails) {
		this.resourceBundleDetails = resourceBundleDetails;
	}

}
