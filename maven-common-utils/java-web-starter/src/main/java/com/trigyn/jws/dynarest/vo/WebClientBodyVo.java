package com.trigyn.jws.dynarest.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "body")
@XmlAccessorType(XmlAccessType.FIELD)
public class WebClientBodyVo {


	@XmlAttribute(name = "contentType")
	private String	contentType		= null;
	
	@XmlElement(name = "parameter")
	private List<WebClientParamVO> bodyParamVOList = null;

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public List<WebClientParamVO> getBodyParamVOList() {
		return bodyParamVOList;
	}

	public void setBodyParamVOList(List<WebClientParamVO> bodyParamVOList) {
		this.bodyParamVOList = bodyParamVOList;
	}
	
	
}
