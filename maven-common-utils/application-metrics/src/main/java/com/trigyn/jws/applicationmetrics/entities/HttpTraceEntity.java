
package com.trigyn.jws.applicationmetrics.entities;

import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpTraceEntity implements Serializable {

	private final static Logger	logger					= LogManager.getLogger(HttpTraceEntity.class);

	private static final long	serialVersionUID		= 1L;

	private String				httpRequestDetails		= null;

	private String				httpResponseDetails		= null;

	private String				auxillaryDetails		= null;

	private String				requestTimestamp		= null;

	private Long				minRequestDuration		= null;

	private Long				maxRequestDuration		= null;

	private Long				averageRequestDuration	= null;

	public HttpTraceEntity() {

	}

	public HttpTraceEntity(String httpRequestDetails, String httpResponseDetails, String auxillaryDetails, String requestTimestamp) {
		this.httpRequestDetails		= httpRequestDetails;
		this.httpResponseDetails	= httpResponseDetails;
		this.auxillaryDetails		= auxillaryDetails;
		this.requestTimestamp		= requestTimestamp;
	}

	public HttpTraceEntity(String httpRequestDetails, String httpResponseDetails, String auxillaryDetails, String requestTimestamp,
			Long minRequestDuration, Long maxRequestDuration, Long averageRequestDuration) {
		this.httpRequestDetails		= httpRequestDetails;
		this.httpResponseDetails	= httpResponseDetails;
		this.auxillaryDetails		= auxillaryDetails;
		this.requestTimestamp		= requestTimestamp;
		this.minRequestDuration		= minRequestDuration;
		this.maxRequestDuration		= maxRequestDuration;
		this.averageRequestDuration	= averageRequestDuration;
	}

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

	public String getRequestTimestamp() {
		return requestTimestamp;
	}

	public void setRequestTimestamp(String requestTimestamp) {
		this.requestTimestamp = requestTimestamp;
	}

	public Long getMinRequestDuration() {
		return minRequestDuration;
	}

	public void setMinRequestDuration(Long minRequestDuration) {
		this.minRequestDuration = minRequestDuration;
	}

	public Long getMaxRequestDuration() {
		return maxRequestDuration;
	}

	public void setMaxRequestDuration(Long maxRequestDuration) {
		this.maxRequestDuration = maxRequestDuration;
	}

	public Long getAverageRequestDuration() {
		return averageRequestDuration;
	}

	public void setAverageRequestDuration(Long averageRequestDuration) {
		this.averageRequestDuration = averageRequestDuration;
	}

	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException jpe) {
			logger.error("- ERROR loading '" + jpe + "'");
			return this.httpRequestDetails;
		}
	}

}
