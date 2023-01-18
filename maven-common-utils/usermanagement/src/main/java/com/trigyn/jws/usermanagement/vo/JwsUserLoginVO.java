package com.trigyn.jws.usermanagement.vo;

import java.util.Map;

public class JwsUserLoginVO {

	private Integer				authenticationType;

	private Integer				verificationType	= 0;

	private Map<String, Object>	loginAttributes;

	public Integer getAuthenticationType() {
		return authenticationType;
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
