package com.trigyn.jws.dynarest.vo;

import java.util.Date;

public class ApiClientDetailsVO {

	private String	clientId				= null;

	private String	clientName				= null;

	private String	clientKey				= null;

	private String	clientSecret			= null;

	private String	clientPublicKey			= null;

	private String	createdBy				= null;

	private Integer	encryptionAlgoId		= null;

	private String	encryptionAlgorithmName	= null;

	private Integer	encLookupId				= null;

	private String	inclusionUrlPattern		= null;

	private Integer	isCustomUpdated			= 1;

	private String	updatedBy				= null;

	private Date	updatedDate				= null;

	public ApiClientDetailsVO() {
	}
	
	public ApiClientDetailsVO(String clientId, String clientName, String clientKey, String clientSecret,
			Integer encryptionAlgoId, String encryptionAlgorithmName, Integer encLookupId) {
		this.clientId					= clientId;
		this.clientName					= clientName;
		this.clientKey					= clientKey;
		this.clientSecret				= clientSecret;
		this.encryptionAlgoId			= encryptionAlgoId;
		this.encryptionAlgorithmName	= encryptionAlgorithmName;
		this.encLookupId 				= encLookupId;
		//this.inclusionURLPattern		= inclusionURLPattern;
	}

	public ApiClientDetailsVO(String clientId, String clientName, String clientKey, String clientSecret,
			String clientPublicKey, String createdBy, Integer encryptionAlgoId, String encryptionAlgorithmName,
			Integer encLookupId, String inclusionUrlPattern, Integer isCustomUpdated, String updatedBy,
			Date updatedDate) {
		this.clientId					= clientId;
		this.clientName					= clientName;
		this.clientKey					= clientKey;
		this.clientSecret				= clientSecret;
		this.clientPublicKey			= clientPublicKey;
		this.createdBy					= createdBy;
		this.encryptionAlgoId			= encryptionAlgoId;
		this.encryptionAlgorithmName	= encryptionAlgorithmName;
		this.encLookupId				= encLookupId;
		//this.inclusionUrlPattern		= inclusionUrlPattern;
		this.isCustomUpdated			= isCustomUpdated;
		this.updatedBy					= updatedBy;
		this.updatedDate				= updatedDate;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getClientKey() {
		return clientKey;
	}

	public void setClientKey(String clientKey) {
		this.clientKey = clientKey;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public Integer getEncryptionAlgoId() {
		return encryptionAlgoId;
	}

	public void setEncryptionAlgoId(Integer encryptionAlgoId) {
		this.encryptionAlgoId = encryptionAlgoId;
	}

	public String getEncryptionAlgorithmName() {
		return encryptionAlgorithmName;
	}

	public void setEncryptionAlgorithmName(String encryptionAlgorithmName) {
		this.encryptionAlgorithmName = encryptionAlgorithmName;
	}

	public Integer getEncLookupId() {
		return encLookupId;
	}

	public void setEncLookupId(Integer encLookupId) {
		this.encLookupId = encLookupId;
	}

	public String getClientPublicKey() {
		return clientPublicKey;
	}

	public void setClientPublicKey(String clientPublicKey) {
		this.clientPublicKey = clientPublicKey;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getInclusionUrlPattern() {
		return inclusionUrlPattern;
	}

	public void setInclusionUrlPattern(String inclusionUrlPattern) {
		this.inclusionUrlPattern = inclusionUrlPattern;
	}

	public Integer getIsCustomUpdated() {
		return isCustomUpdated;
	}

	public void setIsCustomUpdated(Integer isCustomUpdated) {
		this.isCustomUpdated = isCustomUpdated;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

}
