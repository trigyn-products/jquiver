package com.trigyn.jws.webstarter.xml;

import java.util.ArrayList;
import java.util.List;

import com.trigyn.jws.dbutils.entities.AdditionalDatasource;
import com.trigyn.jws.dbutils.vo.xml.XMLVO;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "additionalDatasourceData")
@XmlAccessorType(XmlAccessType.FIELD)
public class AdditionalDatasourceXMLVO extends XMLVO {

	@XmlElement(name = "additionalDatasource")
	private List<AdditionalDatasource> additionalDatasource = new ArrayList<>();

	public List<AdditionalDatasource> getAdditionalDatasource() {
		return additionalDatasource;
	}

	public void setAdditionalDatasource(List<AdditionalDatasource> additionalDatasource) {
		this.additionalDatasource = additionalDatasource;
	}

}
