package com.trigyn.jws.webstarter.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.trigyn.jws.dbutils.entities.AdditionalDatasource;
import com.trigyn.jws.dbutils.vo.xml.XMLVO;

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
