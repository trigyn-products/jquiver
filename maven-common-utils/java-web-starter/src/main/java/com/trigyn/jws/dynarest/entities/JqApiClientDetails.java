package com.trigyn.jws.dynarest.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.trigyn.jws.dynarest.vo.ApiClientDetailsVO;


/**
 * The persistent class for the jq_api_client_details database table.
 * 
 */
@Entity
@Table(name="jq_api_client_details")
@NamedQuery(name="JqApiClientDetails.findAll", query="SELECT j FROM JqApiClientDetails j")
public class JqApiClientDetails implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="client_id")
	private String clientId;

	@Column(name="client_key")
	private String clientKey;

	@Column(name="client_name")
	private String clientName;

	@Lob
	@Column(name="client_public_key")
	private String clientPublicKey;

	@Lob
	@Column(name="client_secret")
	private String clientSecret;

	@Column(name="created_by")
	private String createdBy;

	@Lob
	@Column(name="inclusion_url_pattern")
	private String inclusionUrlPattern;

	@Column(name="is_custom_updated")
	private Integer isCustomUpdated;

	@Column(name="updated_by")
	private String updatedBy;

	@Column(name="updated_date")
	private Date updatedDate;

	//bi-directional many-to-one association to JqEncAlgModPadKeyLookup
	@ManyToOne
	@JoinColumn(name="encryption_algo_id")
	private JqEncAlgModPadKeyLookup jqEncAlgModPadKeyLookup;

	public JqApiClientDetails() {
	}

	public String getClientId() {
		return this.clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientKey() {
		return this.clientKey;
	}

	public void setClientKey(String clientKey) {
		this.clientKey = clientKey;
	}

	public String getClientName() {
		return this.clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getClientPublicKey() {
		return this.clientPublicKey;
	}

	public void setClientPublicKey(String clientPublicKey) {
		this.clientPublicKey = clientPublicKey;
	}

	public String getClientSecret() {
		return this.clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getInclusionUrlPattern() {
		return this.inclusionUrlPattern;
	}

	public void setInclusionUrlPattern(String inclusionUrlPattern) {
		this.inclusionUrlPattern = inclusionUrlPattern;
	}

	public Integer getIsCustomUpdated() {
		return this.isCustomUpdated;
	}

	public void setIsCustomUpdated(Integer isCustomUpdated) {
		this.isCustomUpdated = isCustomUpdated;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public JqEncAlgModPadKeyLookup getJqEncAlgModPadKeyLookup() {
		return this.jqEncAlgModPadKeyLookup;
	}

	public void setJqEncAlgModPadKeyLookup(JqEncAlgModPadKeyLookup jqEncAlgModPadKeyLookup) {
		this.jqEncAlgModPadKeyLookup = jqEncAlgModPadKeyLookup;
	}
	
	public JqApiClientDetails getObject() {
		JqApiClientDetails apiClientDetails = new JqApiClientDetails();

		apiClientDetails.setClientId(clientId);
		apiClientDetails.setClientKey(clientKey);
		apiClientDetails.setClientName(clientName);
		apiClientDetails.setClientSecret(clientSecret);
		apiClientDetails.setCreatedBy(createdBy);
		JqEncAlgModPadKeyLookup algModPadKeyLookup =  new JqEncAlgModPadKeyLookup();
		algModPadKeyLookup.setEncLookupId(jqEncAlgModPadKeyLookup.getEncLookupId());
		apiClientDetails.setJqEncAlgModPadKeyLookup(algModPadKeyLookup);
		apiClientDetails.setInclusionUrlPattern(inclusionUrlPattern);
		apiClientDetails.setUpdatedBy(updatedBy);
		apiClientDetails.setUpdatedDate(updatedDate);

		return apiClientDetails;
	}

	public ApiClientDetailsVO convertEntityToVO(JqApiClientDetails apiClientDetails) {

		ApiClientDetailsVO vo = new ApiClientDetailsVO();
		vo.setClientId(apiClientDetails.getClientId());
		vo.setClientKey(apiClientDetails.getClientKey());
		vo.setClientName(apiClientDetails.getClientName());
		vo.setClientSecret(apiClientDetails.getClientSecret());
		vo.setEncryptionAlgoId(Integer.valueOf(apiClientDetails.getJqEncAlgModPadKeyLookup().getEncLookupId()));
		vo.setEncLookupId(apiClientDetails.getJqEncAlgModPadKeyLookup().getEncLookupId());
		return vo;
	}

}