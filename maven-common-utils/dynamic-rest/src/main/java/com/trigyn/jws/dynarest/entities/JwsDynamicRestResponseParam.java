package com.trigyn.jws.dynarest.entities;


import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the jws_dynamic_rest_response_params database table.
 * 
 */
@Entity
@Table(name="jws_dynamic_rest_response_params")
@NamedQuery(name="JwsDynamicRestResponseParam.findAll", query="SELECT j FROM JwsDynamicRestResponseParam j")
public class JwsDynamicRestResponseParam implements Serializable {
	private static final long serialVersionUID 						= 1L;

	@Id
	@Column(name="jws_response_param_id")
	private Integer jwsResponseParamId								= null;

	@Column(name="jws_response_code_message")
	private String jwsResponseCodeMessage							= null;

	@ManyToOne
	@JoinColumn(name="jws_dynamic_rest_details_id")
	private JwsDynamicRestDetail jwsDynamicRestDetail				= null;

	@ManyToOne
	@JoinColumn(name="jws_response_code_id")
	private JwsResponseCodeDetail jwsResponseCodeDetail				= null;

	public JwsDynamicRestResponseParam() {
	}

	public JwsDynamicRestResponseParam(Integer jwsResponseParamId, String jwsResponseCodeMessage,
			JwsDynamicRestDetail jwsDynamicRestDetail, JwsResponseCodeDetail jwsResponseCodeDetail) {
		this.jwsResponseParamId 		= jwsResponseParamId;
		this.jwsResponseCodeMessage 	= jwsResponseCodeMessage;
		this.jwsDynamicRestDetail 		= jwsDynamicRestDetail;
		this.jwsResponseCodeDetail 		= jwsResponseCodeDetail;
	}

	public Integer getJwsResponseParamId() {
		return this.jwsResponseParamId;
	}

	public void setJwsResponseParamId(Integer jwsResponseParamId) {
		this.jwsResponseParamId = jwsResponseParamId;
	}

	public String getJwsResponseCodeMessage() {
		return this.jwsResponseCodeMessage;
	}

	public void setJwsResponseCodeMessage(String jwsResponseCodeMessage) {
		this.jwsResponseCodeMessage = jwsResponseCodeMessage;
	}

	public JwsDynamicRestDetail getJwsDynamicRestDetail() {
		return this.jwsDynamicRestDetail;
	}

	public void setJwsDynamicRestDetail(JwsDynamicRestDetail jwsDynamicRestDetail) {
		this.jwsDynamicRestDetail = jwsDynamicRestDetail;
	}

	public JwsResponseCodeDetail getJwsResponseCodeDetail() {
		return this.jwsResponseCodeDetail;
	}

	public void setJwsResponseCodeDetail(JwsResponseCodeDetail jwsResponseCodeDetail) {
		this.jwsResponseCodeDetail = jwsResponseCodeDetail;
	}

	@Override
	public int hashCode() {
		return Objects.hash(jwsDynamicRestDetail, jwsResponseCodeDetail, jwsResponseCodeMessage, jwsResponseParamId);
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
		JwsDynamicRestResponseParam other = (JwsDynamicRestResponseParam) obj;
		return Objects.equals(jwsDynamicRestDetail, other.jwsDynamicRestDetail)
				&& Objects.equals(jwsResponseCodeDetail, other.jwsResponseCodeDetail)
				&& Objects.equals(jwsResponseCodeMessage, other.jwsResponseCodeMessage)
				&& Objects.equals(jwsResponseParamId, other.jwsResponseParamId);
	}

	@Override
	public String toString() {
		return "JwsDynamicRestResponseParam [jwsResponseParamId=" + jwsResponseParamId + ", jwsResponseCodeMessage="
				+ jwsResponseCodeMessage + ", jwsDynamicRestDetail=" + jwsDynamicRestDetail + ", jwsResponseCodeDetail="
				+ jwsResponseCodeDetail + "]";
	}

}