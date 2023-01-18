package com.trigyn.jws.dynarest.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

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
