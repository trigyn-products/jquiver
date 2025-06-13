package com.trigyn.jws.webstarter.vo;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trigyn.jws.dbutils.configurations.UUIDEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@EntityListeners(value = { UUIDEntityListener.class })
@Table(name = "jq_captcha")
public class CaptchaDetails {

	@Id
	@Column(name = "request_id")
	private String requestId = null;

	@Column(name = "captcha")
	private String captcha = null;

	@JsonIgnore
	@Column(name = "request_time")
	private Date requestTime = null;

	public CaptchaDetails() {

	}
		
	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getCaptcha() {
		return captcha;
	}


	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	public Date getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}

	@Override
	public int hashCode() {
		return Objects.hash(captcha, requestId, requestTime);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CaptchaDetails other = (CaptchaDetails) obj;
		return Objects.equals(captcha, other.captcha) && Objects.equals(requestId, other.requestId)
				&& Objects.equals(requestTime, other.requestTime);
	}

	@Override
	public String toString() {
		return "CaptchaDetails [requestId=" + requestId + ", captcha=" + captcha + ", requestTime=" + requestTime + "]";
	}

	public CaptchaDetails(String requestId, String captcha, Date requestTime) {
		super();
		this.requestId = requestId;
		this.captcha = captcha;
		this.requestTime = requestTime;
	}



}
