package com.trigyn.jws.dynarest.entities;


import java.io.Serializable;
import javax.persistence.*;
import java.util.List;
import java.util.Objects;



@Entity
@Table(name="jws_response_code_details")
@NamedQuery(name="JwsResponseCodeDetail.findAll", query="SELECT j FROM JwsResponseCodeDetail j")
public class JwsResponseCodeDetail implements Serializable {
	private static final long serialVersionUID 								= 1L;

	@Id
	@Column(name="jws_response_code_id")
	private Integer jwsResponseCodeId										= null;

	@Column(name="jws_response_code_description")
	private String jwsResponseCodeDescription								= null;

	@Column(name="jws_response_status_code")
	private Integer jwsResponseStatusCode									= null;

	@OneToMany(mappedBy="jwsResponseCodeDetail")
	private List<JwsDynamicRestResponseParam> jwsDynamicRestResponseParams	= null;

	public JwsResponseCodeDetail() {
	}

	public JwsResponseCodeDetail(Integer jwsResponseCodeId, String jwsResponseCodeDescription,
			Integer jwsResponseStatusCode, List<JwsDynamicRestResponseParam> jwsDynamicRestResponseParams) {
		this.jwsResponseCodeId 				= jwsResponseCodeId;
		this.jwsResponseCodeDescription 	= jwsResponseCodeDescription;
		this.jwsResponseStatusCode 			= jwsResponseStatusCode;
		this.jwsDynamicRestResponseParams 	= jwsDynamicRestResponseParams;
	}

	public Integer getJwsResponseCodeId() {
		return this.jwsResponseCodeId;
	}

	public void setJwsResponseCodeId(Integer jwsResponseCodeId) {
		this.jwsResponseCodeId = jwsResponseCodeId;
	}

	public String getJwsResponseCodeDescription() {
		return this.jwsResponseCodeDescription;
	}

	public void setJwsResponseCodeDescription(String jwsResponseCodeDescription) {
		this.jwsResponseCodeDescription = jwsResponseCodeDescription;
	}

	public Integer getJwsResponseStatusCode() {
		return this.jwsResponseStatusCode;
	}

	public void setJwsResponseStatusCode(Integer jwsResponseStatusCode) {
		this.jwsResponseStatusCode = jwsResponseStatusCode;
	}

	public List<JwsDynamicRestResponseParam> getJwsDynamicRestResponseParams() {
		return this.jwsDynamicRestResponseParams;
	}

	public void setJwsDynamicRestResponseParams(List<JwsDynamicRestResponseParam> jwsDynamicRestResponseParams) {
		this.jwsDynamicRestResponseParams = jwsDynamicRestResponseParams;
	}

	public JwsDynamicRestResponseParam addJwsDynamicRestResponseParam(JwsDynamicRestResponseParam jwsDynamicRestResponseParam) {
		getJwsDynamicRestResponseParams().add(jwsDynamicRestResponseParam);
		jwsDynamicRestResponseParam.setJwsResponseCodeDetail(this);

		return jwsDynamicRestResponseParam;
	}

	public JwsDynamicRestResponseParam removeJwsDynamicRestResponseParam(JwsDynamicRestResponseParam jwsDynamicRestResponseParam) {
		getJwsDynamicRestResponseParams().remove(jwsDynamicRestResponseParam);
		jwsDynamicRestResponseParam.setJwsResponseCodeDetail(null);

		return jwsDynamicRestResponseParam;
	}

	@Override
	public int hashCode() {
		return Objects.hash(jwsDynamicRestResponseParams, jwsResponseCodeDescription, jwsResponseCodeId,
				jwsResponseStatusCode);
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
		JwsResponseCodeDetail other = (JwsResponseCodeDetail) obj;
		return Objects.equals(jwsDynamicRestResponseParams, other.jwsDynamicRestResponseParams)
				&& Objects.equals(jwsResponseCodeDescription, other.jwsResponseCodeDescription)
				&& Objects.equals(jwsResponseCodeId, other.jwsResponseCodeId)
				&& Objects.equals(jwsResponseStatusCode, other.jwsResponseStatusCode);
	}

	@Override
	public String toString() {
		return "JwsResponseCodeDetail [jwsResponseCodeId=" + jwsResponseCodeId + ", jwsResponseCodeDescription="
				+ jwsResponseCodeDescription + ", jwsResponseStatusCode=" + jwsResponseStatusCode
				+ ", jwsDynamicRestResponseParams=" + jwsDynamicRestResponseParams + "]";
	}

	public JwsResponseCodeDetail getObject() {
		JwsResponseCodeDetail dynaRest = new JwsResponseCodeDetail();
		dynaRest.setJwsResponseCodeDescription(jwsResponseCodeDescription!=null?jwsResponseCodeDescription.trim():jwsResponseCodeDescription);
		dynaRest.setJwsResponseCodeId(jwsResponseCodeId);
		dynaRest.setJwsResponseStatusCode(jwsResponseStatusCode);
		return dynaRest;
	}
}