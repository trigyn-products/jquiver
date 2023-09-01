package com.trigyn.jws.dynarest.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "jq_job_scheduler_log")
public class JqSchedulerLog {

	@Id
	@GeneratedValue(generator = "inquisitive-uuid")
	@GenericGenerator(name = "inquisitive-uuid", strategy = "com.trigyn.jws.dbutils.configurations.CustomUUIDGenerator")
	@Column(name = "scheduler_log_id")
	private String	schedulerLogId			= null;

	@Column(name = "scheduler_id")
	private String	schedulerId			= null;

	@Column(name = "response_code")
	private String	responseCode			= null;

	@Column(name = "response_body")
	private String	responseBody		= null;

	@Column(name = "request_time")
	private Date	requestTime	= null;

	@Column(name = "response_time")
	private Date	responseTime	= null;

	public String getSchedulerLogId() {
		return schedulerLogId;
	}

	public void setSchedulerLogId(String schedulerLogId) {
		this.schedulerLogId = schedulerLogId;
	}

	public String getSchedulerId() {
		return schedulerId;
	}

	public void setSchedulerId(String schedulerId) {
		this.schedulerId = schedulerId;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

	public Date getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}

	public Date getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(Date responseTime) {
		this.responseTime = responseTime;
	}

}
