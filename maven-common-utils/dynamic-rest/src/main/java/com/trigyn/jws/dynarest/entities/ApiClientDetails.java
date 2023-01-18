package com.trigyn.jws.dynarest.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.trigyn.jws.dynarest.vo.ApiClientDetailsVO;

@Entity
@Table(name = "jq_api_client_details")
public class ApiClientDetails {

	@Id
	@GeneratedValue(generator = "inquisitive-uuid")
	@GenericGenerator(name = "inquisitive-uuid", strategy = "com.trigyn.jws.dbutils.configurations.CustomUUIDGenerator")
	@Column(name = "client_id")
	private String	clientId			= null;

	@Column(name = "client_name")
	private String	clientName			= null;

	@Column(name = "client_key")
	private String	clientKey			= null;

	@Column(name = "client_secret")
	private String	clientSecret		= null;

	@Column(name = "encryption_algo_id")
	private String	encryptionAlgoId	= null;

	@Column(name = "updated_by")
	private String	updatedBy			= null;

	@Column(name = "created_by")
	private String	createdBy			= null;

	@Column(name = "updated_date")
	private Date	updatedDate			= null;

	@Column(name = "inclusion_url_pattern")
	private String	inclusionURLPattern	= null;

	@Column(name = "is_custom_updated")
	private Integer	isCustomUpdated		= 1;

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

	public String getEncryptionAlgoId() {
		return encryptionAlgoId;
	}

	public void setEncryptionAlgoId(String encryptionAlgoId) {
		this.encryptionAlgoId = encryptionAlgoId;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getInclusionURLPattern() {
		return inclusionURLPattern;
	}

	public void setInclusionURLPattern(String inclusionURLPattern) {
		this.inclusionURLPattern = inclusionURLPattern;
	}

	public Integer getIsCustomUpdated() {
		return isCustomUpdated;
	}

	public void setIsCustomUpdated(Integer isCustomUpdated) {
		this.isCustomUpdated = isCustomUpdated;
	}

	public ApiClientDetails getObject() {
		ApiClientDetails apiClientDetails = new ApiClientDetails();

		apiClientDetails.setClientId(clientId);
		apiClientDetails.setClientKey(clientKey);
		apiClientDetails.setClientName(clientName);
		apiClientDetails.setClientSecret(clientSecret);
		apiClientDetails.setCreatedBy(createdBy);
		apiClientDetails.setEncryptionAlgoId(encryptionAlgoId);
		apiClientDetails.setInclusionURLPattern(inclusionURLPattern);
		apiClientDetails.setUpdatedBy(updatedBy);
		apiClientDetails.setUpdatedDate(updatedDate);

		return apiClientDetails;
	}

	public ApiClientDetailsVO convertEntityToVO(ApiClientDetails apiClientDetails) {

		ApiClientDetailsVO vo = new ApiClientDetailsVO();
		vo.setClientId(apiClientDetails.getClientId());
		vo.setClientKey(apiClientDetails.getClientKey());
		vo.setClientName(apiClientDetails.getClientName());
		vo.setClientSecret(apiClientDetails.getClientSecret());
		vo.setEncryptionAlgoId(Integer.valueOf(apiClientDetails.getEncryptionAlgoId()));
		vo.setInclusionURLPattern(apiClientDetails.getInclusionURLPattern());

		return vo;
	}

}
