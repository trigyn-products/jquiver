package com.trigyn.jws.webstarter.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.trigyn.jws.templating.entities.TemplateMaster;

@XmlRootElement(name = "templateData")
@XmlAccessorType (XmlAccessType.FIELD)
public class TemplateXMLVO extends XMLVO {
	
	@XmlElement(name = "template")
    private List<TemplateMaster> templateDetails = new ArrayList<>();

	public List<TemplateMaster> getTemplateDetails() {
		return templateDetails;
	}

	public void setTemplateDetails(List<TemplateMaster> templateDetails) {
		this.templateDetails = templateDetails;
	}

}
