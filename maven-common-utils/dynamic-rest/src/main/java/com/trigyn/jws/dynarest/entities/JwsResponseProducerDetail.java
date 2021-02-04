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
@Table(name = "jws_response_producer_details")
@NamedQuery(name = "JwsResponseProducerDetail.findAll", query = "SELECT j FROM JwsResponseProducerDetail j")
public class JwsResponseProducerDetail implements Serializable {
	private static final long			serialVersionUID			= 1L;

	@Id
	@Column(name = "jws_response_producer_type_id")
	private Integer						jwsResponseProducerTypeId	= null;

	@Column(name = "jws_response_producer_type")
	private String						jwsResponseProducerType		= null;

	@OneToMany(mappedBy = "jwsResponseProducerDetail")
	private List<JwsDynamicRestDetail>	jwsDynamicRestDetails		= null;

	public JwsResponseProducerDetail() {
	}

	public JwsResponseProducerDetail(Integer jwsResponseProducerTypeId, String jwsResponseProducerType,
			List<JwsDynamicRestDetail> jwsDynamicRestDetails) {
		this.jwsResponseProducerTypeId	= jwsResponseProducerTypeId;
		this.jwsResponseProducerType	= jwsResponseProducerType;
		this.jwsDynamicRestDetails		= jwsDynamicRestDetails;
	}

	public Integer getJwsResponseProducerTypeId() {
		return this.jwsResponseProducerTypeId;
	}

	public void setJwsResponseProducerTypeId(Integer jwsResponseProducerTypeId) {
		this.jwsResponseProducerTypeId = jwsResponseProducerTypeId;
	}

	public String getJwsResponseProducerType() {
		return this.jwsResponseProducerType;
	}

	public void setJwsResponseProducerType(String jwsResponseProducerType) {
		this.jwsResponseProducerType = jwsResponseProducerType;
	}

	public List<JwsDynamicRestDetail> getJwsDynamicRestDetails() {
		return this.jwsDynamicRestDetails;
	}

	public void setJwsDynamicRestDetails(List<JwsDynamicRestDetail> jwsDynamicRestDetails) {
		this.jwsDynamicRestDetails = jwsDynamicRestDetails;
	}

	public JwsDynamicRestDetail addJwsDynamicRestDetail(JwsDynamicRestDetail jwsDynamicRestDetail) {
		getJwsDynamicRestDetails().add(jwsDynamicRestDetail);
		jwsDynamicRestDetail.setJwsResponseProducerDetail(this);

		return jwsDynamicRestDetail;
	}

	public JwsDynamicRestDetail removeJwsDynamicRestDetail(JwsDynamicRestDetail jwsDynamicRestDetail) {
		getJwsDynamicRestDetails().remove(jwsDynamicRestDetail);
		jwsDynamicRestDetail.setJwsResponseProducerDetail(null);

		return jwsDynamicRestDetail;
	}

	@Override
	public int hashCode() {
		return Objects.hash(jwsDynamicRestDetails, jwsResponseProducerType, jwsResponseProducerTypeId);
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
		JwsResponseProducerDetail other = (JwsResponseProducerDetail) obj;
		return Objects.equals(jwsDynamicRestDetails, other.jwsDynamicRestDetails)
				&& Objects.equals(jwsResponseProducerType, other.jwsResponseProducerType)
				&& Objects.equals(jwsResponseProducerTypeId, other.jwsResponseProducerTypeId);
	}

	@Override
	public String toString() {
		return "JwsResponseProducerDetail [jwsResponseProducerTypeId=" + jwsResponseProducerTypeId
				+ ", jwsResponseProducerType=" + jwsResponseProducerType + ", jwsDynamicRestDetails="
				+ jwsDynamicRestDetails + "]";
	}

}