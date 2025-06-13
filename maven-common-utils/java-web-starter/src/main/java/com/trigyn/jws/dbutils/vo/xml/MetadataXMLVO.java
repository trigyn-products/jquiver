package com.trigyn.jws.dbutils.vo.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "export")
@XmlAccessorType(XmlAccessType.FIELD)
public class MetadataXMLVO extends XMLVO {

	@XmlElement(name = "settings")
	private Settings		settings		= null;

	@XmlElement(name = "modules")
	private ExportModule	exportModules	= null;

	@XmlElement(name = "info")
	private String			info			= null;

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	public ExportModule getExportModules() {
		return exportModules;
	}

	public void setExportModules(ExportModule exportModules) {
		this.exportModules = exportModules;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

}
