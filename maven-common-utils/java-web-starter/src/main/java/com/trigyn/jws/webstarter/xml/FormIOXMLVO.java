package com.trigyn.jws.webstarter.xml;

import java.util.ArrayList;
import java.util.List;

import com.trigyn.jws.dbutils.vo.xml.XMLVO;
import com.trigyn.jws.formio.entities.FormIO;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "FormIOData")
@XmlAccessorType(XmlAccessType.FIELD)
public class FormIOXMLVO extends XMLVO {

	@XmlElement(name = "formIO")
	private List<FormIO> formIODetails = new ArrayList<>();

	public List<FormIO> getFormIODetails() {
		return formIODetails;
	}

	public void setFormIODetails(List<FormIO> formIODetails) {
		this.formIODetails = formIODetails;
	}

}
