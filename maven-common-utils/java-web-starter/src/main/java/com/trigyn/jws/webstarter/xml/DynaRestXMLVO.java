package com.trigyn.jws.webstarter.xml;

import java.util.ArrayList;
import java.util.List;

import com.trigyn.jws.dbutils.vo.xml.XMLVO;
import com.trigyn.jws.dynarest.entities.JwsDynamicRestDetail;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "dynaRestData")
@XmlAccessorType(XmlAccessType.FIELD)
public class DynaRestXMLVO extends XMLVO {

	@XmlElement(name = "dynaRest")
	private List<JwsDynamicRestDetail> dynaRestDetails = new ArrayList<>();

	public List<JwsDynamicRestDetail> getDynaRestDetails() {
		return dynaRestDetails;
	}

	public void setDynaRestDetails(List<JwsDynamicRestDetail> dynaRestDetails) {
		this.dynaRestDetails = dynaRestDetails;
	}

}
