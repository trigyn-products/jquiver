package com.trigyn.jws.webstarter.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.trigyn.jws.dbutils.vo.xml.XMLVO;
import com.trigyn.jws.dynarest.entities.JqApiClientDetails;

@XmlRootElement(name = "apiClientData")
@XmlAccessorType(XmlAccessType.FIELD)
public class ApiClientDetailsXMLVO extends XMLVO {

	@XmlElement(name = "apiClientDetails")
	private List<JqApiClientDetails> apiClientDetails = new ArrayList<>();

	public List<JqApiClientDetails> getApiClientDetails() {
		return apiClientDetails;
	}

	public void setApiClientDetails(List<JqApiClientDetails> apiClientDetails) {
		this.apiClientDetails = apiClientDetails;
	}

}
