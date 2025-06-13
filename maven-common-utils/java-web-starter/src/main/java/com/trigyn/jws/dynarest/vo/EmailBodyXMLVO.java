package com.trigyn.jws.dynarest.vo;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlValue;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "body")
@XmlAccessorType(XmlAccessType.FIELD)
public class EmailBodyXMLVO {

	@XmlAttribute(name = "contenttype")
	private String	contenttype		= null;

	@XmlAttribute(name = "content")
	private Integer	content		= null;

	@XmlValue
	@XmlJavaTypeAdapter(value = CDATAAdapter.class)
	private String	data	= null;

	public String getContenttype() {
		return contenttype;
	}

	public void setContenttype(String contenttype) {
		this.contenttype = contenttype;
	}

	public Integer getContent() {
		return content;
	}

	public void setContent(Integer content) {
		this.content = content;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
