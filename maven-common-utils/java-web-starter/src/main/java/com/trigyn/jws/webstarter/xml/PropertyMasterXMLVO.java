package com.trigyn.jws.webstarter.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.trigyn.jws.dbutils.entities.PropertyMaster;
import com.trigyn.jws.dbutils.vo.xml.XMLVO;

@XmlRootElement(name = "applicationConfigurationData")
@XmlAccessorType (XmlAccessType.FIELD)
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
