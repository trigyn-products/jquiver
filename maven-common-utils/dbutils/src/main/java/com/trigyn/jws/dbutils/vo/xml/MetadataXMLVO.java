package com.trigyn.jws.dbutils.vo.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

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
