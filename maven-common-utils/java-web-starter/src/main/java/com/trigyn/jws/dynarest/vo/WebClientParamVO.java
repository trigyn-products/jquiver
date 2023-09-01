package com.trigyn.jws.dynarest.vo;

import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "parameter")
@XmlAccessorType(XmlAccessType.FIELD)
public class WebClientParamVO {

	@XmlElement(name = "name")
	private String	parameterName	= null;

	@XmlElement(name = "value")
	private String	parameterValue	= null;

	@XmlAttribute(name = "dataType")
	private String	dataType		= null;

	public WebClientParamVO() {
		super();
	}

	public WebClientParamVO(String parameterName, String parameterValue) {
		super();
		this.parameterName	= parameterName;
		this.parameterValue	= parameterValue;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public String getParameterValue() {
		return parameterValue;
	}

	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}

	@Override
	public int hashCode() {
		return Objects.hash(parameterName, parameterValue);
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
		WebClientParamVO other = (WebClientParamVO) obj;
		return Objects.equals(parameterName, other.parameterName) && Objects.equals(parameterValue, other.parameterValue);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("WebClientParamVO [parameterName=").append(parameterName).append(", parameterValue=").append(parameterValue)
				.append("]");
		return builder.toString();
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
}
