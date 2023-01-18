package com.trigyn.jws.dbutils.utils;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomResponseEntity {

	@JsonProperty(value = "status")
	private Integer				responseStatusCode	= null;

	@JsonIgnore
	private Date				responseTimestamp	= null;

	@JsonProperty(value = "body")
	private String				responseBody		= null;

	@JsonIgnore
	private Map<String, String>	headers				= null;
	
	@JsonIgnore
	private Long responseDuration = null;

	@JsonIgnore
	private HttpStatus statusCode = null;

	@JsonIgnore
	private String statusText = null;

	@JsonIgnore
	private String message = null;
	
	@JsonIgnore
	private ErrorCode errorCode = null;

	public CustomResponseEntity() {
		super();
	}

	public CustomResponseEntity(String responseBody, HttpStatus statusCode, ErrorCode errorCode) {
		super();
		this.statusCode	= statusCode;
		this.responseTimestamp	= new Date();
		this.responseBody		= responseBody;
		this.errorCode			= errorCode;
	}

	public CustomResponseEntity(Integer responseStatusCode, Date responseTimestamp, String responseBody, Map<String, String> headers, Long responseDuration) {
		super();
		this.responseStatusCode	= responseStatusCode;
		this.responseTimestamp	= new Date();
		this.responseBody		= responseBody;
		this.headers			= headers;
		this.responseDuration = responseDuration;
	}

	public Integer getResponseStatusCode() {
		return responseStatusCode;
	}

	public void setResponseStatusCode(Integer responseStatusCode) {
		this.responseStatusCode = responseStatusCode;
	}

	public Date getResponseTimestamp() {
		return responseTimestamp;
	}

	public void setResponseTimestamp(Date responseTimestamp) {
		this.responseTimestamp = responseTimestamp;
	}

	public String getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	@Override
	public int hashCode() {
		return Objects.hash(headers, responseBody, responseStatusCode, responseTimestamp);
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
		CustomResponseEntity other = (CustomResponseEntity) obj;
		return Objects.equals(headers, other.headers) && Objects.equals(responseBody, other.responseBody)
				&& Objects.equals(responseStatusCode, other.responseStatusCode)
				&& Objects.equals(responseTimestamp, other.responseTimestamp);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CustomResponseEntity [responseStatusCode=").append(responseStatusCode).append(", responseTimestamp=")
				.append(responseTimestamp).append(", responseBody=").append(responseBody).append(", headers=").append(headers).append("]");
		return builder.toString();
	}

	public Long getResponseDuration() {
		return responseDuration;
	}

	public void setResponseDuration(Long responseDuration) {
		this.responseDuration = responseDuration;
	}

	public HttpStatus getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(HttpStatus statusCode) {
		this.statusCode = statusCode;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public String getStatusText() {
		return statusText;
	}

	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
