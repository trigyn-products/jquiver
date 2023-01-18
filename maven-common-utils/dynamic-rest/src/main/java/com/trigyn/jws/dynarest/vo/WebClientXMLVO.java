package com.trigyn.jws.dynarest.vo;

import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "rest")
@XmlAccessorType(XmlAccessType.FIELD)
public class WebClientXMLVO {

	@XmlJavaTypeAdapter(value = CDATAAdapter.class)
	@XmlElement(name = "url")
	private String				webClientURL		= null;

	@XmlElement(name = "type")
	private String				requestType			= null;

	@XmlElement(name = "content-type")
	private String				contentType			= null;

	@XmlElement(name = "timeout")
	private Integer				responseTimeOut		= null;

	@XmlElement(name = "ssl-handshake-timeout")
	private Integer				sslHandshakeTimeout	= null;

	@XmlElement(name = "ssl-flush-timeout")
	private Integer				sslFlushTimeout		= null;

	@XmlElement(name = "ssl-read-timeout")
	private Integer				sslReadTimeout		= null;

	@XmlElement(name = "request")
	private WebClientRequestVO	webClientRequestVO	= null;

	public WebClientXMLVO() {
		super();
	}

	public WebClientXMLVO(String webClientURL, String requestType, String contentType, Integer responseTimeOut, Integer sslHandshakeTimeout,
			Integer sslFlushTimeout, Integer sslReadTimeout, WebClientRequestVO webClientRequestVO) {
		super();
		this.webClientURL			= webClientURL;
		this.requestType			= requestType;
		this.contentType			= contentType;
		this.responseTimeOut		= responseTimeOut;
		this.sslHandshakeTimeout	= sslHandshakeTimeout;
		this.sslFlushTimeout		= sslFlushTimeout;
		this.sslReadTimeout			= sslReadTimeout;
		this.webClientRequestVO		= webClientRequestVO;
	}

	public String getWebClientURL() {
		return webClientURL;
	}

	public void setWebClientURL(String webClientURL) {
		this.webClientURL = webClientURL;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Integer getResponseTimeOut() {
		return responseTimeOut;
	}

	public void setResponseTimeOut(Integer responseTimeOut) {
		this.responseTimeOut = responseTimeOut;
	}

	public Integer getSslHandshakeTimeout() {
		return sslHandshakeTimeout;
	}

	public void setSslHandshakeTimeout(Integer sslHandshakeTimeout) {
		this.sslHandshakeTimeout = sslHandshakeTimeout;
	}

	public Integer getSslFlushTimeout() {
		return sslFlushTimeout;
	}

	public void setSslFlushTimeout(Integer sslFlushTimeout) {
		this.sslFlushTimeout = sslFlushTimeout;
	}

	public Integer getSslReadTimeout() {
		return sslReadTimeout;
	}

	public void setSslReadTimeout(Integer sslReadTimeout) {
		this.sslReadTimeout = sslReadTimeout;
	}

	public WebClientRequestVO getWebClientRequestVO() {
		return webClientRequestVO;
	}

	public void setWebClientRequestVO(WebClientRequestVO webClientRequestVO) {
		this.webClientRequestVO = webClientRequestVO;
	}

	@Override
	public int hashCode() {
		return Objects.hash(contentType, webClientRequestVO, requestType, responseTimeOut, sslFlushTimeout, sslHandshakeTimeout,
				sslReadTimeout, webClientURL);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		WebClientXMLVO other = (WebClientXMLVO) obj;
		return Objects.equals(contentType, other.contentType) && Objects.equals(webClientRequestVO, other.webClientRequestVO)
				&& Objects.equals(requestType, other.requestType) && Objects.equals(responseTimeOut, other.responseTimeOut)
				&& Objects.equals(sslFlushTimeout, other.sslFlushTimeout) && Objects.equals(sslHandshakeTimeout, other.sslHandshakeTimeout)
				&& Objects.equals(sslReadTimeout, other.sslReadTimeout) && Objects.equals(webClientURL, other.webClientURL);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("WebClientXMLVO [webClientURL=").append(webClientURL).append(", requestType=").append(requestType)
				.append(", contentType=").append(contentType).append(", responseTimeOut=").append(responseTimeOut)
				.append(", sslHandshakeTimeout=").append(sslHandshakeTimeout).append(", sslFlushTimeout=").append(sslFlushTimeout)
				.append(", sslReadTimeout=").append(sslReadTimeout).append(", webClientRequestVO=").append(webClientRequestVO).append("]");
		return builder.toString();
	}

}
