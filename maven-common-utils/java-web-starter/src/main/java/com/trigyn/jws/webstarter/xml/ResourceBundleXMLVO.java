package com.trigyn.jws.webstarter.xml;

import java.util.ArrayList;
import java.util.List;

import com.trigyn.jws.dbutils.vo.xml.XMLVO;
import com.trigyn.jws.resourcebundle.entities.ResourceBundle;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "resourceBundleData")
@XmlAccessorType(XmlAccessType.FIELD)
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
