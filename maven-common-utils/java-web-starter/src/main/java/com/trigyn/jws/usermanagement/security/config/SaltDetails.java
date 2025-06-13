package com.trigyn.jws.usermanagement.security.config;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.hibernate.annotations.GenericGenerator;

import com.trigyn.jws.dbutils.configurations.UUIDEntityListener;

import jakarta.persistence.*;


@Entity
@Table(name = "jq_salt_details")
@EntityListeners(value = { UUIDEntityListener.class })
public class SaltDetails implements Serializable {

	private final static Logger					logger							= LoggerFactory
			.getLogger(SaltDetails.class);

	private static final long					serialVersionUID				= 1L;

	@Id
	@Column(name = "request_id")
	private String								requestId				= null;

	@Column(name = "salt")
	private String								salt				= null;

	@Column(name = "request_time")
	private LocalDateTime						requestTime			= null;

	
	
	public SaltDetails() {

	}

	public SaltDetails(String requestId, String salt, LocalDateTime localDateTime) {
		this.requestId = requestId;
		this.salt = salt;
		this.requestTime = localDateTime;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public LocalDateTime getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(LocalDateTime requestTime) {
		this.requestTime = requestTime;
	}
	
}