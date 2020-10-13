package com.trigyn.jws.dynarest.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;



@Entity
@Table(name="jws_dynamic_rest_details")
@NamedQuery(name="JwsDynamicRestDetail.findAll", query="SELECT j FROM JwsDynamicRestDetail j")
public class JwsDynamicRestDetail implements Serializable {
	private static final long serialVersionUID 			= 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="jws_dynamic_rest_id")
	private Integer jwsDynamicRestId					= null;

	@Column(name="jws_dynamic_rest_url")
	private String jwsDynamicRestUrl					= null;

	@Column(name="jws_method_description")
	private String jwsMethodDescription					= null;

	@Column(name="jws_method_name")
	private String jwsMethodName						= null;

	@Column(name="jws_platform_id")
	private Integer jwsPlatformId						= null;

	@Column(name="jws_rbac_id")
	private Integer jwsRbacId							= null;

	@Column(name="jws_service_logic")
	private String jwsServiceLogic						= null;
	
	@Column(name="jws_request_type_id")
	private Integer jwsRequestTypeId					= null;
	
	@Column(name="jws_response_producer_type_id")
	private Integer jwsResponseProducerTypeId			= null;
	
	@OneToMany(mappedBy="jwsDynamicRestDetail", fetch = FetchType.LAZY)
	private List<JwsDynamicRestDaoDetail> jwsDynamicRestDaoDetails				= null;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="jws_request_type_id",referencedColumnName = "jws_request_type_details_id",insertable = false, updatable = false)
	private JwsRequestTypeDetail jwsRequestTypeDetail							= null;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="jws_response_producer_type_id",referencedColumnName = "jws_response_producer_type_id",insertable = false, updatable = false)
	private JwsResponseProducerDetail jwsResponseProducerDetail					= null;

	@OneToMany(mappedBy="jwsDynamicRestDetail", fetch = FetchType.LAZY)
	private List<JwsDynamicRestResponseParam> jwsDynamicRestResponseParams		= null;
	
	@OneToMany(mappedBy="jwsDynamicRestDetail", fetch = FetchType.LAZY)
	private List<JwsDynamicRestRoleAssociation> JwsDynamicRestRoleAssociation	= null;	

	public JwsDynamicRestDetail() {
	}

	public JwsDynamicRestDetail(Integer jwsDynamicRestId, String jwsDynamicRestUrl, String jwsMethodDescription,
			String jwsMethodName, Integer jwsPlatformId, Integer jwsRbacId, String jwsServiceLogic,
			List<JwsDynamicRestDaoDetail> jwsDynamicRestDaoDetails, JwsRequestTypeDetail jwsRequestTypeDetail,
			JwsResponseProducerDetail jwsResponseProducerDetail,
			List<JwsDynamicRestResponseParam> jwsDynamicRestResponseParams) {
		this.jwsDynamicRestId 				= jwsDynamicRestId;
		this.jwsDynamicRestUrl 				= jwsDynamicRestUrl;
		this.jwsMethodDescription 			= jwsMethodDescription;
		this.jwsMethodName 					= jwsMethodName;
		this.jwsPlatformId 					= jwsPlatformId;
		this.jwsRbacId 						= jwsRbacId;
		this.jwsServiceLogic 				= jwsServiceLogic;
		this.jwsDynamicRestDaoDetails 		= jwsDynamicRestDaoDetails;
		this.jwsRequestTypeDetail 			= jwsRequestTypeDetail;
		this.jwsResponseProducerDetail 		= jwsResponseProducerDetail;
		this.jwsDynamicRestResponseParams 	= jwsDynamicRestResponseParams;
	}

	public Integer getJwsDynamicRestId() {
		return this.jwsDynamicRestId;
	}

	public void setJwsDynamicRestId(Integer jwsDynamicRestId) {
		this.jwsDynamicRestId = jwsDynamicRestId;
	}

	public String getJwsDynamicRestUrl() {
		return this.jwsDynamicRestUrl;
	}

	public void setJwsDynamicRestUrl(String jwsDynamicRestUrl) {
		this.jwsDynamicRestUrl = jwsDynamicRestUrl;
	}

	public String getJwsMethodDescription() {
		return this.jwsMethodDescription;
	}

	public void setJwsMethodDescription(String jwsMethodDescription) {
		this.jwsMethodDescription = jwsMethodDescription;
	}

	public String getJwsMethodName() {
		return this.jwsMethodName;
	}

	public void setJwsMethodName(String jwsMethodName) {
		this.jwsMethodName = jwsMethodName;
	}

	public Integer getJwsPlatformId() {
		return this.jwsPlatformId;
	}

	public void setJwsPlatformId(Integer jwsPlatformId) {
		this.jwsPlatformId = jwsPlatformId;
	}

	public Integer getJwsRbacId() {
		return this.jwsRbacId;
	}

	public void setJwsRbacId(Integer jwsRbacId) {
		this.jwsRbacId = jwsRbacId;
	}

	public String getJwsServiceLogic() {
		return this.jwsServiceLogic;
	}

	public void setJwsServiceLogic(String jwsServiceLogic) {
		this.jwsServiceLogic = jwsServiceLogic;
	}

	
	public Integer getJwsRequestTypeId() {
		return jwsRequestTypeId;
	}

	public void setJwsRequestTypeId(Integer jwsRequestTypeId) {
		this.jwsRequestTypeId = jwsRequestTypeId;
	}

	public Integer getJwsResponseProducerTypeId() {
		return jwsResponseProducerTypeId;
	}

	public void setJwsResponseProducerTypeId(Integer jwsResponseProducerTypeId) {
		this.jwsResponseProducerTypeId = jwsResponseProducerTypeId;
	}

	public List<JwsDynamicRestDaoDetail> getJwsDynamicRestDaoDetails() {
		return this.jwsDynamicRestDaoDetails;
	}

	public void setJwsDynamicRestDaoDetails(List<JwsDynamicRestDaoDetail> jwsDynamicRestDaoDetails) {
		this.jwsDynamicRestDaoDetails = jwsDynamicRestDaoDetails;
	}

	public JwsDynamicRestDaoDetail addJwsDynamicRestDaoDetail(JwsDynamicRestDaoDetail jwsDynamicRestDaoDetail) {
		getJwsDynamicRestDaoDetails().add(jwsDynamicRestDaoDetail);
		jwsDynamicRestDaoDetail.setJwsDynamicRestDetail(this);

		return jwsDynamicRestDaoDetail;
	}

	public JwsDynamicRestDaoDetail removeJwsDynamicRestDaoDetail(JwsDynamicRestDaoDetail jwsDynamicRestDaoDetail) {
		getJwsDynamicRestDaoDetails().remove(jwsDynamicRestDaoDetail);
		jwsDynamicRestDaoDetail.setJwsDynamicRestDetail(null);

		return jwsDynamicRestDaoDetail;
	}

	public JwsRequestTypeDetail getJwsRequestTypeDetail() {
		return this.jwsRequestTypeDetail;
	}

	public void setJwsRequestTypeDetail(JwsRequestTypeDetail jwsRequestTypeDetail) {
		this.jwsRequestTypeDetail = jwsRequestTypeDetail;
	}

	public JwsResponseProducerDetail getJwsResponseProducerDetail() {
		return this.jwsResponseProducerDetail;
	}

	public void setJwsResponseProducerDetail(JwsResponseProducerDetail jwsResponseProducerDetail) {
		this.jwsResponseProducerDetail = jwsResponseProducerDetail;
	}

	public List<JwsDynamicRestResponseParam> getJwsDynamicRestResponseParams() {
		return this.jwsDynamicRestResponseParams;
	}

	public void setJwsDynamicRestResponseParams(List<JwsDynamicRestResponseParam> jwsDynamicRestResponseParams) {
		this.jwsDynamicRestResponseParams = jwsDynamicRestResponseParams;
	}
	
	public JwsDynamicRestResponseParam addJwsDynamicRestResponseParam(JwsDynamicRestResponseParam jwsDynamicRestResponseParam) {
		getJwsDynamicRestResponseParams().add(jwsDynamicRestResponseParam);
		jwsDynamicRestResponseParam.setJwsDynamicRestDetail(this);

		return jwsDynamicRestResponseParam;
	}

	public JwsDynamicRestResponseParam removeJwsDynamicRestResponseParam(JwsDynamicRestResponseParam jwsDynamicRestResponseParam) {
		getJwsDynamicRestResponseParams().remove(jwsDynamicRestResponseParam);
		jwsDynamicRestResponseParam.setJwsDynamicRestDetail(null);

		return jwsDynamicRestResponseParam;
	}

	@Override
	public int hashCode() {
		return Objects.hash(JwsDynamicRestRoleAssociation, jwsDynamicRestDaoDetails, jwsDynamicRestId,
				jwsDynamicRestResponseParams, jwsDynamicRestUrl, jwsMethodDescription, jwsMethodName, jwsPlatformId,
				jwsRbacId, jwsRequestTypeDetail, jwsRequestTypeId, jwsResponseProducerDetail, jwsResponseProducerTypeId,
				jwsServiceLogic);
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
		JwsDynamicRestDetail other = (JwsDynamicRestDetail) obj;
		return Objects.equals(JwsDynamicRestRoleAssociation, other.JwsDynamicRestRoleAssociation)
				&& Objects.equals(jwsDynamicRestDaoDetails, other.jwsDynamicRestDaoDetails)
				&& Objects.equals(jwsDynamicRestId, other.jwsDynamicRestId)
				&& Objects.equals(jwsDynamicRestResponseParams, other.jwsDynamicRestResponseParams)
				&& Objects.equals(jwsDynamicRestUrl, other.jwsDynamicRestUrl)
				&& Objects.equals(jwsMethodDescription, other.jwsMethodDescription)
				&& Objects.equals(jwsMethodName, other.jwsMethodName)
				&& Objects.equals(jwsPlatformId, other.jwsPlatformId) && Objects.equals(jwsRbacId, other.jwsRbacId)
				&& Objects.equals(jwsRequestTypeDetail, other.jwsRequestTypeDetail)
				&& Objects.equals(jwsRequestTypeId, other.jwsRequestTypeId)
				&& Objects.equals(jwsResponseProducerDetail, other.jwsResponseProducerDetail)
				&& Objects.equals(jwsResponseProducerTypeId, other.jwsResponseProducerTypeId)
				&& Objects.equals(jwsServiceLogic, other.jwsServiceLogic);
	}

	@Override
	public String toString() {
		return "JwsDynamicRestDetail [jwsDynamicRestId=" + jwsDynamicRestId + ", jwsDynamicRestUrl=" + jwsDynamicRestUrl
				+ ", jwsMethodDescription=" + jwsMethodDescription + ", jwsMethodName=" + jwsMethodName
				+ ", jwsPlatformId=" + jwsPlatformId + ", jwsRbacId=" + jwsRbacId + ", jwsServiceLogic="
				+ jwsServiceLogic + ", jwsRequestTypeId="
				+ jwsRequestTypeId + ", jwsResponseProducerTypeId=" + jwsResponseProducerTypeId
				+ ", jwsDynamicRestDaoDetails=" + jwsDynamicRestDaoDetails + ", jwsRequestTypeDetail="
				+ jwsRequestTypeDetail + ", jwsResponseProducerDetail=" + jwsResponseProducerDetail
				+ ", jwsDynamicRestResponseParams=" + jwsDynamicRestResponseParams + ", JwsDynamicRestRoleAssociation="
				+ JwsDynamicRestRoleAssociation + "]";
	}


}