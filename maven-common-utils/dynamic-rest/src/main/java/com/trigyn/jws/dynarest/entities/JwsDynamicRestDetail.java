package com.trigyn.jws.dynarest.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.text.StringEscapeUtils;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "jq_dynamic_rest_details")
@NamedQuery(name = "JwsDynamicRestDetail.findAll", query = "SELECT j FROM JwsDynamicRestDetail j")
public class JwsDynamicRestDetail implements Serializable {

	private static final long					serialVersionUID				= 1L;

	@Id
	@GeneratedValue(generator = "inquisitive-uuid")
	@GenericGenerator(name = "inquisitive-uuid", strategy = "com.trigyn.jws.dbutils.configurations.CustomUUIDGenerator")
	@Column(name = "jws_dynamic_rest_id")
	private String								jwsDynamicRestId				= null;

	@Column(name = "jws_dynamic_rest_url")
	private String								jwsDynamicRestUrl				= null;

	@Column(name = "jws_method_description")
	private String								jwsMethodDescription			= null;

	@Column(name = "jws_method_name")
	private String								jwsMethodName					= null;

	@Column(name = "jws_platform_id")
	private Integer								jwsPlatformId					= null;

	@Column(name = "jws_rbac_id")
	private Integer								jwsRbacId						= null;

	@Column(name = "jws_service_logic")
	private String								jwsServiceLogic					= null;

	@Column(name = "jws_request_type_id")
	private Integer								jwsRequestTypeId				= null;

	@Column(name = "jws_response_producer_type_id")
	private Integer								jwsResponseProducerTypeId		= null;

	@Column(name = "jws_allow_files")
	private Integer								jwsAllowFiles					= null;

	@Column(name = "jws_dynamic_rest_type_id")
	private Integer								jwsDynamicRestTypeId			= 1;

	@Column(name = "created_by")
	private String								createdBy						= null;

	@Column(name = "created_date")
	private Date								createdDate						= null;

	@Column(name = "datasource_id")
	private String								datasourceId					= null;

	@Column(name = "last_updated_by")
	private String								lastUpdatedBy					= null;

	@Column(name = "last_updated_ts")
	private Date								lastUpdatedTs					= null;

	@OneToMany(mappedBy = "jwsDynamicRestDetail", fetch = FetchType.LAZY)
	private List<JwsDynamicRestDaoDetail>		jwsDynamicRestDaoDetails		= null;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "jws_request_type_id", referencedColumnName = "jws_request_type_details_id", insertable = false, updatable = false)
	private JwsRequestTypeDetail				jwsRequestTypeDetail			= null;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "jws_response_producer_type_id", referencedColumnName = "jws_response_producer_type_id", insertable = false, updatable = false)
	private JwsResponseProducerDetail			jwsResponseProducerDetail		= null;

	@OneToMany(mappedBy = "jwsDynamicRestDetail", fetch = FetchType.LAZY)
	private List<JwsDynamicRestResponseParam>	jwsDynamicRestResponseParams	= null;

	@OneToMany(mappedBy = "jwsDynamicRestDetail", fetch = FetchType.LAZY)
	private List<JwsDynamicRestRoleAssociation>	JwsDynamicRestRoleAssociation	= null;

	public JwsDynamicRestDetail() {

	}

	public JwsDynamicRestDetail(String jwsDynamicRestId, String jwsDynamicRestUrl, String jwsMethodDescription, String jwsMethodName,
			Integer jwsPlatformId, Integer jwsRbacId, String jwsServiceLogic, Integer jwsRequestTypeId, Integer jwsResponseProducerTypeId,
			Integer jwsAllowFiles, Integer jwsDynamicRestTypeId, String createdBy, Date createdDate, String datasourceId,
			String lastUpdatedBy, Date lastUpdatedTs) {
		this.jwsDynamicRestId			= jwsDynamicRestId;
		this.jwsDynamicRestUrl			= jwsDynamicRestUrl;
		this.jwsMethodDescription		= jwsMethodDescription;
		this.jwsMethodName				= jwsMethodName;
		this.jwsPlatformId				= jwsPlatformId;
		this.jwsRbacId					= jwsRbacId;
		this.jwsServiceLogic			= jwsServiceLogic;
		this.jwsRequestTypeId			= jwsRequestTypeId;
		this.jwsResponseProducerTypeId	= jwsResponseProducerTypeId;
		this.jwsAllowFiles				= jwsAllowFiles;
		this.jwsDynamicRestTypeId		= jwsDynamicRestTypeId;
		this.createdBy					= createdBy;
		this.createdDate				= createdDate;
		this.datasourceId				= datasourceId;
		this.lastUpdatedBy				= lastUpdatedBy;
		this.lastUpdatedTs				= lastUpdatedTs;
	}

	public String getJwsDynamicRestId() {
		return this.jwsDynamicRestId;
	}

	public void setJwsDynamicRestId(String jwsDynamicRestId) {
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

	public Integer getJwsAllowFiles() {
		return jwsAllowFiles;
	}

	public void setJwsAllowFiles(Integer jwsAllowFiles) {
		this.jwsAllowFiles = jwsAllowFiles;
	}

	public Integer getJwsDynamicRestTypeId() {
		return jwsDynamicRestTypeId;
	}

	public void setJwsDynamicRestTypeId(Integer jwsDynamicRestTypeId) {
		this.jwsDynamicRestTypeId = jwsDynamicRestTypeId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getDatasourceId() {
		return datasourceId;
	}

	public void setDatasourceId(String datasourceId) {
		this.datasourceId = datasourceId;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastUpdatedTs() {
		return lastUpdatedTs;
	}

	public void setLastUpdatedTs(Date lastUpdatedTs) {
		this.lastUpdatedTs = lastUpdatedTs;
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

	public List<JwsDynamicRestRoleAssociation> getJwsDynamicRestRoleAssociation() {
		return JwsDynamicRestRoleAssociation;
	}

	public void setJwsDynamicRestRoleAssociation(List<JwsDynamicRestRoleAssociation> jwsDynamicRestRoleAssociation) {
		JwsDynamicRestRoleAssociation = jwsDynamicRestRoleAssociation;
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

	public JwsDynamicRestDetail getObject() {
		JwsDynamicRestDetail dynaRest = new JwsDynamicRestDetail();
		dynaRest.setJwsDynamicRestId(jwsDynamicRestId != null ? jwsDynamicRestId.trim() : jwsDynamicRestId);
		dynaRest.setJwsDynamicRestUrl(jwsDynamicRestUrl != null ? jwsDynamicRestUrl.trim() : jwsDynamicRestUrl);
		dynaRest.setJwsMethodDescription(jwsMethodDescription != null ? jwsMethodDescription.trim() : jwsMethodDescription);
		dynaRest.setJwsMethodName(jwsMethodName != null ? jwsMethodName.trim() : jwsMethodName);
		dynaRest.setJwsPlatformId(jwsPlatformId);
		dynaRest.setJwsRbacId(jwsRbacId);
		dynaRest.setCreatedBy(createdBy);
		dynaRest.setCreatedDate(createdDate);
		dynaRest.setDatasourceId(datasourceId);
		dynaRest.setLastUpdatedBy(lastUpdatedBy);
		dynaRest.setLastUpdatedTs(lastUpdatedTs);
		if (jwsServiceLogic != null) {
			dynaRest.setJwsServiceLogic(StringEscapeUtils.unescapeXml("<![CDATA[" + jwsServiceLogic.trim() + "]]>"));
		} else {
			dynaRest.setJwsServiceLogic(StringEscapeUtils.unescapeXml("<![CDATA[" + jwsServiceLogic + "]]>"));
		}

		dynaRest.setJwsRequestTypeId(jwsRequestTypeId);
		dynaRest.setJwsResponseProducerTypeId(jwsResponseProducerTypeId);
		dynaRest.setJwsDynamicRestTypeId(jwsDynamicRestTypeId);
		dynaRest.setJwsAllowFiles(jwsAllowFiles);

		List<JwsDynamicRestDaoDetail> jwsDynamicRestDaoDetailsOthr = new ArrayList<>();
		if (jwsDynamicRestDaoDetails != null && !jwsDynamicRestDaoDetails.isEmpty()) {
			for (JwsDynamicRestDaoDetail jdrdd : jwsDynamicRestDaoDetails) {
				jwsDynamicRestDaoDetailsOthr.add(jdrdd.getObject());
			}
			dynaRest.setJwsDynamicRestDaoDetails(jwsDynamicRestDaoDetailsOthr);
		} else
			dynaRest.setJwsDynamicRestDaoDetails(null);

		dynaRest.setJwsRequestTypeDetail(jwsRequestTypeDetail.getObject());

		List<JwsDynamicRestResponseParam> jwsDynamicRestResponseParamsOthr = new ArrayList<>();
		if (jwsDynamicRestResponseParams != null && !jwsDynamicRestResponseParams.isEmpty()) {
			for (JwsDynamicRestResponseParam jdrrp : jwsDynamicRestResponseParams) {
				jwsDynamicRestResponseParamsOthr.add(jdrrp.getObject());
			}
			dynaRest.setJwsDynamicRestResponseParams(jwsDynamicRestResponseParamsOthr);
		} else
			dynaRest.setJwsDynamicRestResponseParams(null);

		List<JwsDynamicRestRoleAssociation> jwsDynamicRestRoleAssociationOthr = new ArrayList<>();
		if (JwsDynamicRestRoleAssociation != null && !JwsDynamicRestRoleAssociation.isEmpty()) {
			for (JwsDynamicRestRoleAssociation jdrra : JwsDynamicRestRoleAssociation) {
				jwsDynamicRestRoleAssociationOthr.add(jdrra.getObject());
			}
			dynaRest.setJwsDynamicRestRoleAssociation(jwsDynamicRestRoleAssociationOthr);
		} else
			dynaRest.setJwsDynamicRestRoleAssociation(null);

		return dynaRest;
	}

	@Override
	public int hashCode() {
		return Objects.hash(JwsDynamicRestRoleAssociation, createdBy, datasourceId, jwsAllowFiles, jwsDynamicRestDaoDetails,
				jwsDynamicRestId, jwsDynamicRestResponseParams, jwsDynamicRestTypeId, jwsDynamicRestUrl, jwsMethodDescription,
				jwsMethodName, jwsPlatformId, jwsRbacId, jwsRequestTypeDetail, jwsRequestTypeId, jwsResponseProducerDetail,
				jwsResponseProducerTypeId, jwsServiceLogic, lastUpdatedBy);
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
				&& Objects.equals(createdBy, other.createdBy) && Objects.equals(datasourceId, other.datasourceId)
				&& Objects.equals(jwsAllowFiles, other.jwsAllowFiles)
				&& Objects.equals(jwsDynamicRestDaoDetails, other.jwsDynamicRestDaoDetails)
				&& Objects.equals(jwsDynamicRestId, other.jwsDynamicRestId)
				&& Objects.equals(jwsDynamicRestResponseParams, other.jwsDynamicRestResponseParams)
				&& Objects.equals(jwsDynamicRestTypeId, other.jwsDynamicRestTypeId)
				&& Objects.equals(jwsDynamicRestUrl, other.jwsDynamicRestUrl)
				&& Objects.equals(jwsMethodDescription, other.jwsMethodDescription) && Objects.equals(jwsMethodName, other.jwsMethodName)
				&& Objects.equals(jwsPlatformId, other.jwsPlatformId) && Objects.equals(jwsRbacId, other.jwsRbacId)
				&& Objects.equals(jwsRequestTypeDetail, other.jwsRequestTypeDetail)
				&& Objects.equals(jwsRequestTypeId, other.jwsRequestTypeId)
				&& Objects.equals(jwsResponseProducerDetail, other.jwsResponseProducerDetail)
				&& Objects.equals(jwsResponseProducerTypeId, other.jwsResponseProducerTypeId)
				&& Objects.equals(jwsServiceLogic, other.jwsServiceLogic) && Objects.equals(lastUpdatedBy, other.lastUpdatedBy);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("JwsDynamicRestDetail [jwsDynamicRestId=").append(jwsDynamicRestId).append(", jwsDynamicRestUrl=")
				.append(jwsDynamicRestUrl).append(", jwsMethodDescription=").append(jwsMethodDescription).append(", jwsMethodName=")
				.append(jwsMethodName).append(", jwsPlatformId=").append(jwsPlatformId).append(", jwsRbacId=").append(jwsRbacId)
				.append(", jwsServiceLogic=").append(jwsServiceLogic).append(", jwsRequestTypeId=").append(jwsRequestTypeId)
				.append(", jwsResponseProducerTypeId=").append(jwsResponseProducerTypeId).append(", jwsAllowFiles=").append(jwsAllowFiles)
				.append(", jwsDynamicRestTypeId=").append(jwsDynamicRestTypeId).append(", createdBy=").append(createdBy)
				.append(", createdDate=").append(createdDate).append(", datasourceId=").append(datasourceId).append(", lastUpdatedBy=")
				.append(lastUpdatedBy).append(", lastUpdatedTs=").append(lastUpdatedTs).append(", jwsDynamicRestDaoDetails=")
				.append(jwsDynamicRestDaoDetails).append(", jwsRequestTypeDetail=").append(jwsRequestTypeDetail)
				.append(", jwsResponseProducerDetail=").append(jwsResponseProducerDetail).append(", jwsDynamicRestResponseParams=")
				.append(jwsDynamicRestResponseParams).append(", JwsDynamicRestRoleAssociation=").append(JwsDynamicRestRoleAssociation)
				.append("]");
		return builder.toString();
	}

}