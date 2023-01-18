package com.trigyn.jws.webstarter.vo;

public class UserAuthTypeDataVo {
	
	private String authenticationTypeId;
	
	private String propertyJson;
	
	private String regexObj;
	
	private String userProfileFormId; 
	
	private String userProfileTemplate;
	
	public String getAuthenticationTypeId() {
		return authenticationTypeId;
	}
	public void setAuthenticationTypeId(String authenticationTypeId) {
		this.authenticationTypeId = authenticationTypeId;
	}
	public String getPropertyJson() {
		return propertyJson;
	}
	public void setPropertyJson(String propertyJson) {
		this.propertyJson = propertyJson;
	}
	
	public String getRegexObj() {
		return regexObj;
	}
	public void setRegexObj(String regexObj) {
		this.regexObj = regexObj;
	}
	
	public String getUserProfileFormId() {
		return userProfileFormId;
	}

	public void setUserProfileFormId(String userProfileFormId) {
		this.userProfileFormId = userProfileFormId;
	}

	public String getUserProfileTemplate() {
		return userProfileTemplate;
	}

	public void setUserProfileTemplate(String userProfileTemplate) {
		this.userProfileTemplate = userProfileTemplate;
	}

}
