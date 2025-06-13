package com.trigyn.jws.webstarter.xml;

import java.util.ArrayList;
import java.util.List;

import com.trigyn.jws.dbutils.vo.xml.XMLVO;
import com.trigyn.jws.sciptlibrary.entities.ScriptLibraryDetails;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "scriptLibraryData")
@XmlAccessorType(XmlAccessType.FIELD)
public class ScriptLibraryXMLVO extends XMLVO {

	@XmlElement(name = "scriptLibraryDetails")
	private List<ScriptLibraryDetails> scriptLibraryDetails = new ArrayList<>();

	public List<ScriptLibraryDetails> getScriptLibraryDetails() {
		return scriptLibraryDetails;
	}

	public void setScriptLibraryDetails(List<ScriptLibraryDetails> scriptLibraryDetails) {
		this.scriptLibraryDetails = scriptLibraryDetails;
	}

}
