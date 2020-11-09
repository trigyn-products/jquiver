package com.trigyn.jws.applicationmetrics.entities;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpTraceEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String httpRequestDetails = null;

	private String httpResponseDetails = null;

	private String auxillaryDetails = null;

	private Long requestDuration = null;

	private Date requestTimestamp = null;

	public String getHttpRequestDetails() {
		return httpRequestDetails;
	}

	public void setHttpRequestDetails(String httpRequestDetails) {
		this.httpRequestDetails = httpRequestDetails;
	}

	public String getHttpResponseDetails() {
		return httpResponseDetails;
	}

	public void setHttpResponseDetails(String httpResponseDetails) {
		this.httpResponseDetails = httpResponseDetails;
	}

	public String getAuxillaryDetails() {
		return auxillaryDetails;
	}

	public void setAuxillaryDetails(String auxillaryDetails) {
		this.auxillaryDetails = auxillaryDetails;
	}

	public Long getRequestDuration() {
		return requestDuration;
	}

	public void setRequestDuration(Long requestDuration) {
		this.requestDuration = requestDuration;
	}

	public Date getRequestTimestamp() {
		return requestTimestamp;
	}

	public void setRequestTimestamp(Date requestTimestamp) {
		this.requestTimestamp = requestTimestamp;
	}

	public HttpTraceEntity() {

	}

	public HttpTraceEntity(String httpRequestDetails, String httpResponseDetails, String auxillaryDetails,
			Long requestDuration, Date requestTimestamp) {
		this.httpRequestDetails = httpRequestDetails;
		this.httpResponseDetails = httpResponseDetails;
		this.auxillaryDetails = auxillaryDetails;
		this.requestDuration = requestDuration;
		this.requestTimestamp = requestTimestamp;
	}

	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return this.httpRequestDetails;
		}
	}

}
