package com.trigyn.jws.webstarter.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.trigyn.jws.dbutils.vo.xml.XMLVO;
import com.trigyn.jws.dynarest.entities.ApiClientDetails;

@XmlRootElement(name = "apiClientData")
@XmlAccessorType(XmlAccessType.FIELD)
public class ApiClientDetailsXMLVO extends XMLVO {

	@XmlElement(name = "apiClientDetails")
	private List<ApiClientDetails> apiClientDetails = new ArrayList<>();

	public List<ApiClientDetails> getApiClientDetails() {
		return apiClientDetails;
	}

	public void setApiClientDetails(List<ApiClientDetails> apiClientDetails) {
		this.apiClientDetails = apiClientDetails;
	}

}
