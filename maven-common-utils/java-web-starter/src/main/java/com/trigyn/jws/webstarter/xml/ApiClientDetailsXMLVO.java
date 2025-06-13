package com.trigyn.jws.webstarter.xml;

import java.util.ArrayList;
import java.util.List;

import com.trigyn.jws.dbutils.vo.xml.XMLVO;
import com.trigyn.jws.dynarest.entities.JqApiClientDetails;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;


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
