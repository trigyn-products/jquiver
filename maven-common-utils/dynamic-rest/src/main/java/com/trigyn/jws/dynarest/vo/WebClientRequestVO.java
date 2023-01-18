package com.trigyn.jws.dynarest.vo;

import java.util.List;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "request")
@XmlAccessorType(XmlAccessType.FIELD)
public class WebClientRequestVO {

	@XmlElementWrapper(name = "headers")
	@XmlElement(name = "parameter")
	private List<WebClientParamVO> headerParamVOList = null;
	
	@XmlElement(name = "body")
	private WebClientBodyVo body = null;
	
	@XmlElementWrapper(name = "query-param")
	@XmlElement(name = "parameter")
	private List<WebClientParamVO> queryParamVOList = null;

	@XmlElementWrapper(name = "attachments")
	@XmlElement(name = "attachment")
	private List<WebClientAttacmentVO> attachmnetParamVOList = null;
	
	public WebClientRequestVO() {
		super();
	}

	public WebClientRequestVO(List<WebClientParamVO> headerParamVOList, List<WebClientParamVO> bodyParamVOList,
			List<WebClientParamVO> queryParamVOList,WebClientBodyVo body, List<WebClientAttacmentVO>  attachmnetParamVOList) {
		super();
		this.headerParamVOList = headerParamVOList;
		this.body = body;
		this.queryParamVOList = queryParamVOList;
		this.attachmnetParamVOList = attachmnetParamVOList;
	}


	public List<WebClientParamVO> getHeaderParamVOList() {
		return headerParamVOList;
	}

	public void setHeaderParamVOList(List<WebClientParamVO> headerParamVOList) {
		this.headerParamVOList = headerParamVOList;
	}

	public List<WebClientParamVO> getQueryParamVOList() {
		return queryParamVOList;
	}

	public void setQueryParamVOList(List<WebClientParamVO> queryParamVOList) {
		this.queryParamVOList = queryParamVOList;
	}

	public List<WebClientAttacmentVO> getAttachmnetParamVOList() {
		return attachmnetParamVOList;
	}

	public void setAttachmnetParamVOList(List<WebClientAttacmentVO> attachmnetParamVOList) {
		this.attachmnetParamVOList = attachmnetParamVOList;
	}

	public WebClientBodyVo getBody() {
		return body;
	}

	public void setBody(WebClientBodyVo body) {
		this.body = body;
	}

	@Override
	public int hashCode() {
		return Objects.hash(attachmnetParamVOList, body, headerParamVOList, queryParamVOList);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WebClientRequestVO other = (WebClientRequestVO) obj;
		return Objects.equals(attachmnetParamVOList, other.attachmnetParamVOList)
				&& Objects.equals(body, other.body)
				&& Objects.equals(headerParamVOList, other.headerParamVOList)
				&& Objects.equals(queryParamVOList, other.queryParamVOList);
	}

	@Override
	public String toString() {
		return "WebClientRequestVO [headerParamVOList=" + headerParamVOList + ", body=" + body
				+ ", queryParamVOList=" + queryParamVOList + ", attachmnetParamVOList=" + attachmnetParamVOList + "]";
	}

}
