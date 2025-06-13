package com.trigyn.jws.webstarter.xml;

import java.util.ArrayList;
import java.util.List;

import com.trigyn.jws.dbutils.entities.PropertyMaster;
import com.trigyn.jws.dbutils.vo.xml.XMLVO;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "applicationConfigurationData")
@XmlAccessorType(XmlAccessType.FIELD)
public class PropertyMasterXMLVO extends XMLVO {

	@XmlElement(name = "applicationConfiguration")
	private List<PropertyMaster> applicationConfiguration = new ArrayList<>();

	public List<PropertyMaster> getApplicationConfiguration() {
		return applicationConfiguration;
	}

	public void setApplicationConfiguration(List<PropertyMaster> applicationConfiguration) {
		this.applicationConfiguration = applicationConfiguration;
	}

}
