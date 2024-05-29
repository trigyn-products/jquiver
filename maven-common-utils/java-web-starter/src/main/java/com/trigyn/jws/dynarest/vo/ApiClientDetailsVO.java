package com.trigyn.jws.dynarest.vo;

public class ApiClientDetailsVO {

	private String	clientId				= null;

	private String	clientName				= null;

	private String	clientKey				= null;

	private String	clientSecret			= null;

	private Integer	encryptionAlgoId		= null;

	private String	encryptionAlgorithmName	= null;
	
	private Integer encLookupId				= null;


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


}
