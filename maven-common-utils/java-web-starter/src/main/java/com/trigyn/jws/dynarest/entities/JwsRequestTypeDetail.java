package com.trigyn.jws.dynarest.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "jq_request_type_details")
@NamedQuery(name = "JwsRequestTypeDetail.findAll", query = "SELECT j FROM JwsRequestTypeDetail j")
public class JwsRequestTypeDetail implements Serializable {
	private static final long			serialVersionUID		= 1L;

	@Id
	@Column(name = "jws_request_type_details_id")
	private Integer						jwsRequestTypeDetailsId	= null;

	@Column(name = "jws_request_type")
	private String						jwsRequestType			= null;

	@OneToMany(mappedBy = "jwsRequestTypeDetail")
	private List<JwsDynamicRestDetail>	jwsDynamicRestDetails	= null;

	public JwsRequestTypeDetail() {
	}

	public JwsRequestTypeDetail(Integer jwsRequestTypeDetailsId, String jwsRequestType, List<JwsDynamicRestDetail> jwsDynamicRestDetails) {
		this.jwsRequestTypeDetailsId	= jwsRequestTypeDetailsId;
		this.jwsRequestType				= jwsRequestType;
		this.jwsDynamicRestDetails		= jwsDynamicRestDetails;
	}

	public Integer getJwsRequestTypeDetailsId() {
		return this.jwsRequestTypeDetailsId;
	}

	public void setJwsRequestTypeDetailsId(Integer jwsRequestTypeDetailsId) {
		this.jwsRequestTypeDetailsId = jwsRequestTypeDetailsId;
	}

	public String getJwsRequestType() {
		return this.jwsRequestType;
	}

	public void setJwsRequestType(String jwsRequestType) {
		this.jwsRequestType = jwsRequestType;
	}

	public List<JwsDynamicRestDetail> getJwsDynamicRestDetails() {
		return this.jwsDynamicRestDetails;
	}

	public void setJwsDynamicRestDetails(List<JwsDynamicRestDetail> jwsDynamicRestDetails) {
		this.jwsDynamicRestDetails = jwsDynamicRestDetails;
	}

	public JwsDynamicRestDetail addJwsDynamicRestDetail(JwsDynamicRestDetail jwsDynamicRestDetail) {
		getJwsDynamicRestDetails().add(jwsDynamicRestDetail);
		jwsDynamicRestDetail.setJwsRequestTypeDetail(this);

		return jwsDynamicRestDetail;
	}

	public JwsDynamicRestDetail removeJwsDynamicRestDetail(JwsDynamicRestDetail jwsDynamicRestDetail) {
		getJwsDynamicRestDetails().remove(jwsDynamicRestDetail);
		jwsDynamicRestDetail.setJwsRequestTypeDetail(null);

		return jwsDynamicRestDetail;
	}

	@Override
	public int hashCode() {
		return Objects.hash(jwsDynamicRestDetails, jwsRequestType, jwsRequestTypeDetailsId);
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
		JwsRequestTypeDetail other = (JwsRequestTypeDetail) obj;
		return Objects.equals(jwsDynamicRestDetails, other.jwsDynamicRestDetails) && Objects.equals(jwsRequestType, other.jwsRequestType)
				&& Objects.equals(jwsRequestTypeDetailsId, other.jwsRequestTypeDetailsId);
	}

	@Override
	public String toString() {
		return "JwsRequestTypeDetail [jwsRequestTypeDetailsId=" + jwsRequestTypeDetailsId + ", jwsRequestType=" + jwsRequestType
				+ ", jwsDynamicRestDetails=" + jwsDynamicRestDetails + "]";
	}

	public JwsRequestTypeDetail getObject() {
		JwsRequestTypeDetail dynaRest = new JwsRequestTypeDetail();
		dynaRest.setJwsRequestType(jwsRequestType != null ? jwsRequestType.trim() : jwsRequestType);
		dynaRest.setJwsRequestTypeDetailsId(jwsRequestTypeDetailsId);

		return dynaRest;
	}
}