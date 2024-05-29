package com.trigyn.jws.webstarter.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.trigyn.jws.dbutils.vo.xml.XMLVO;
import com.trigyn.jws.sciptlibrary.entities.ScriptLibrary;
import com.trigyn.jws.sciptlibrary.entities.ScriptLibraryDetails;
import com.trigyn.jws.webstarter.vo.GenericUserNotification;

@XmlRootElement(name = "scriptLibraryData")
@XmlAccessorType(XmlAccessType.FIELD)
public class ScriptLibraryXMLVO extends XMLVO {

	@XmlElement(name = "scriptLibrary")
	private List<ScriptLibraryDetails> scriptLibraryDetails = new ArrayList<>();

	public List<ScriptLibraryDetails> getScriptLibraryDetails() {
		return scriptLibraryDetails;
	}

	public void setScriptLibraryDetails(List<ScriptLibraryDetails> scriptLibraryDetails) {
		this.scriptLibraryDetails = scriptLibraryDetails;
	}

}
