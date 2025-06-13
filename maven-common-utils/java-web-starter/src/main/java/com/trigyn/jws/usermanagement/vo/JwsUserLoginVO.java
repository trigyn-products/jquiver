package com.trigyn.jws.usermanagement.vo;

import java.util.Map;

public class JwsUserLoginVO {

	private Integer				authenticationType;

	private Integer				verificationType	= 0;

	private Map<String, Object>	loginAttributes;

	private String				databaseDisplayName	= null;

	private String				ldapDisplayName		= null;

	public Integer getAuthenticationType() {
		return authenticationType;
	}

	public String getDatabaseDisplayName() {
		return databaseDisplayName;
	}

	public void setDatabaseDisplayName(String databaseDisplayName) {
		this.databaseDisplayName = databaseDisplayName;
	}

	public String getLdapDisplayName() {
		return ldapDisplayName;
	}

	public void setLdapDisplayName(String ldapDisplayName) {
		this.ldapDisplayName = ldapDisplayName;
	}

	public void setAuthenticationType(Integer authenticationType) {
		this.authenticationType = authenticationType;
	}

	public Integer getVerificationType() {
		return verificationType;
	}

	public void setVerificationType(Integer verificationType) {
		if (verificationType != null)
			this.verificationType = verificationType;
		else
			this.verificationType = 0;
	}

	public Map<String, Object> getLoginAttributes() {
		return loginAttributes;
	}

	public void setLoginAttributes(Map<String, Object> loginAttributes) {
		this.loginAttributes = loginAttributes;
	}

}
