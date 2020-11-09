package com.trigyn.jws.webstarter.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.trigyn.jws.dynarest.entities.JwsDynamicRestDetail;

@XmlRootElement(name = "dynaRestData")
@XmlAccessorType (XmlAccessType.FIELD)
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
