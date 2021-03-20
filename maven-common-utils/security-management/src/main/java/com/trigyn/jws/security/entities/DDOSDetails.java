package com.trigyn.jws.security.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "jq_ddos_details")
public class DDOSDetails implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 4351078363363100037L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "jws_ddos_details_id")
	private String				jwsDosDetailsId		= null;

	@Column(name = "ip_address")
	private String				ipAddress			= null;

	@Column(name = "session_details")
	private String				sessionDetails		= null;

	@Column(name = "is_blocked")
	private Integer				isBlocked			= null;

	@Column(name = "last_attacked_date")
	private Date				lastAttackedDate	= null;

	public DDOSDetails() {
		super();
	}

	public DDOSDetails(String jwsDosDetailsId, String ipAddress, String sessionDetails, Integer isBlocked,
			Date lastAttackedDate) {
		this.jwsDosDetailsId	= jwsDosDetailsId;
		this.ipAddress			= ipAddress;
		this.sessionDetails		= sessionDetails;
		this.isBlocked			= isBlocked;
		this.lastAttackedDate	= lastAttackedDate;
	}

	public String getJwsDosDetailsId() {
		return jwsDosDetailsId;
	}

	public void setJwsDosDetailsId(String jwsDosDetailsId) {
		this.jwsDosDetailsId = jwsDosDetailsId;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getSessionDetails() {
		return sessionDetails;
	}

	public void setSessionDetails(String sessionDetails) {
		this.sessionDetails = sessionDetails;
	}

	public Integer getIsBlocked() {
		return isBlocked;
	}

	public void setIsBlocked(Integer isBlocked) {
		this.isBlocked = isBlocked;
	}

	public Date getLastAttackedDate() {
		return lastAttackedDate;
	}

	public void setLastAttackedDate(Date lastAttackedDate) {
		this.lastAttackedDate = lastAttackedDate;
	}

	@Override
	public int hashCode() {
		return Objects.hash(ipAddress, isBlocked, jwsDosDetailsId, lastAttackedDate, sessionDetails);
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
		DDOSDetails other = (DDOSDetails) obj;
		return Objects.equals(ipAddress, other.ipAddress) && Objects.equals(isBlocked, other.isBlocked)
				&& Objects.equals(jwsDosDetailsId, other.jwsDosDetailsId)
				&& Objects.equals(lastAttackedDate, other.lastAttackedDate)
				&& Objects.equals(sessionDetails, other.sessionDetails);
	}

	@Override
	public String toString() {
		return "DDOSDetails [jwsDosDetailsId=" + jwsDosDetailsId + ", ipAddress=" + ipAddress + ", sessionDetails="
				+ sessionDetails + ", isBlocked=" + isBlocked + ", lastAttackedDate=" + lastAttackedDate + "]";
	}

}
