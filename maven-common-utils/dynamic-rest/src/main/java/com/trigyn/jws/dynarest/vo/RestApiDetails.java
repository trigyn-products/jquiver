package com.trigyn.jws.dynarest.vo;

import java.util.Objects;

public class RestApiDetails {

	private String	dynamicId			= null;

	private String	dynamicRestUrl		= null;

	private Integer	rbacId				= null;

	private String	methodName			= null;

	private String	methodDescription	= null;

	private String	reponseType			= null;

	private String	serviceLogic		= null;

	private Integer	platformId			= null;

	private String	methodType			= null;

	private String	headerJson			= null;

	public RestApiDetails() {
	}

	public RestApiDetails(String dynamicId, String dynamicRestUrl, Integer rbacId, String methodName,
			String methodDescription, String reponseType, String serviceLogic, Integer platformId, String methodType,
			String headerJson) {
		this.dynamicId			= dynamicId;
		this.dynamicRestUrl		= dynamicRestUrl;
		this.rbacId				= rbacId;
		this.methodName			= methodName;
		this.methodDescription	= methodDescription;
		this.reponseType		= reponseType;
		this.serviceLogic		= serviceLogic;
		this.platformId			= platformId;
		this.methodType			= methodType;
		this.headerJson			= headerJson;
	}

	public String getDynamicId() {
		return this.dynamicId;
	}

	public void setDynamicId(String dynamicId) {
		this.dynamicId = dynamicId;
	}

	public String getDynamicRestUrl() {
		return this.dynamicRestUrl;
	}

	public void setDynamicRestUrl(String dynamicRestUrl) {
		this.dynamicRestUrl = dynamicRestUrl;
	}

	public Integer getRbacId() {
		return this.rbacId;
	}

	public void setRbacId(Integer rbacId) {
		this.rbacId = rbacId;
	}

	public String getMethodName() {
		return this.methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getMethodDescription() {
		return this.methodDescription;
	}

	public void setMethodDescription(String methodDescription) {
		this.methodDescription = methodDescription;
	}

	public String getReponseType() {
		return this.reponseType;
	}

	public void setReponseType(String reponseType) {
		this.reponseType = reponseType;
	}

	public String getServiceLogic() {
		return this.serviceLogic;
	}

	public void setServiceLogic(String serviceLogic) {
		this.serviceLogic = serviceLogic;
	}

	public Integer getPlatformId() {
		return this.platformId;
	}

	public void setPlatformId(Integer platformId) {
		this.platformId = platformId;
	}

	public String getMethodType() {
		return this.methodType;
	}

	public void setMethodType(String methodType) {
		this.methodType = methodType;
	}

	public RestApiDetails dynamicId(String dynamicId) {
		this.dynamicId = dynamicId;
		return this;
	}

	public RestApiDetails dynamicRestUrl(String dynamicRestUrl) {
		this.dynamicRestUrl = dynamicRestUrl;
		return this;
	}

	public RestApiDetails rbacId(Integer rbacId) {
		this.rbacId = rbacId;
		return this;
	}

	public RestApiDetails methodName(String methodName) {
		this.methodName = methodName;
		return this;
	}

	public RestApiDetails methodDescription(String methodDescription) {
		this.methodDescription = methodDescription;
		return this;
	}

	public RestApiDetails reponseType(String reponseType) {
		this.reponseType = reponseType;
		return this;
	}

	public RestApiDetails serviceLogic(String serviceLogic) {
		this.serviceLogic = serviceLogic;
		return this;
	}

	public RestApiDetails platformId(Integer platformId) {
		this.platformId = platformId;
		return this;
	}

	public RestApiDetails methodType(String methodType) {
		this.methodType = methodType;
		return this;
	}

	public String getHeaderJson() {
		return headerJson;
	}

	public void setHeaderJson(String headerJson) {
		this.headerJson = headerJson;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dynamicId, dynamicRestUrl, methodDescription, methodName, methodType, platformId, rbacId,
				reponseType, serviceLogic, headerJson);
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
		RestApiDetails other = (RestApiDetails) obj;
		return Objects.equals(dynamicId, other.dynamicId) && Objects.equals(dynamicRestUrl, other.dynamicRestUrl)
				&& Objects.equals(methodDescription, other.methodDescription)
				&& Objects.equals(methodName, other.methodName) && Objects.equals(methodType, other.methodType)
				&& Objects.equals(platformId, other.platformId) && Objects.equals(rbacId, other.rbacId)
				&& Objects.equals(reponseType, other.reponseType) && Objects.equals(serviceLogic, other.serviceLogic)
				&& Objects.equals(headerJson, other.headerJson);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RestApiDetails [dynamicId=").append(dynamicId).append(", dynamicRestUrl=")
				.append(dynamicRestUrl).append(", rbacId=").append(rbacId).append(", methodName=").append(methodName)
				.append(", methodDescription=").append(methodDescription).append(", reponseType=").append(reponseType)
				.append(", serviceLogic=").append(serviceLogic).append(", platformId=").append(platformId)
				.append(", methodType=").append(methodType).append(headerJson).append("]");
		return builder.toString();
	}

}
