package com.trigyn.jws.dynarest.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.text.StringEscapeUtils;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "jq_dynamic_rest_dao_details")
@NamedQuery(name = "JwsDynamicRestDaoDetail.findAll", query = "SELECT j FROM JwsDynamicRestDaoDetail j")
public class JwsDynamicRestDaoDetail implements Serializable {
	private static final long		serialVersionUID		= 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "custom-identity-generator")
	@GenericGenerator(name = "custom-identity-generator", strategy = "com.trigyn.jws.dbutils.configurations.CustomIdentityGenerator")
	@Column(name = "jws_dao_details_id")
	private Integer					jwsDaoDetailsId			= null;

	@Column(name = "jws_dao_query_template")
	private String					jwsDaoQueryTemplate		= null;

	@Column(name = "jws_result_variable_name")
	private String					jwsResultVariableName	= null;

	@Column(name = "jws_query_sequence")
	private Integer					jwsQuerySequence		= null;

	@Column(name = "jws_dynamic_rest_details_id")
	private String					jwsDynamicRestDetailId	= null;

	@Column(name = "jws_dao_query_type")
	private Integer					queryType				= null;

	@ManyToOne
	@JoinColumn(name = "jws_dynamic_rest_details_id", referencedColumnName = "jws_dynamic_rest_id", insertable = false, updatable = false)
	private JwsDynamicRestDetail	jwsDynamicRestDetail	= null;

	@Column(name = "datasource_id")
	private String								datasourceId					= null;

	@Column(name = "dao_query_checksum")
	private String						daoQueryChecksum		= null;

	public JwsDynamicRestDaoDetail() {
	}

	public JwsDynamicRestDaoDetail(Integer jwsDaoDetailsId, String jwsDaoQueryTemplate, String jwsResultVariableName,
			Integer jwsQuerySequence) {
		this.jwsDaoDetailsId		= jwsDaoDetailsId;
		this.jwsDaoQueryTemplate	= jwsDaoQueryTemplate;
		this.jwsResultVariableName	= jwsResultVariableName;
		this.jwsQuerySequence		= jwsQuerySequence;
	}

	public Integer getJwsDaoDetailsId() {
		return this.jwsDaoDetailsId;
	}

	public void setJwsDaoDetailsId(Integer jwsDaoDetailsId) {
		this.jwsDaoDetailsId = jwsDaoDetailsId;
	}

	public String getJwsDaoQueryTemplate() {
		return this.jwsDaoQueryTemplate;
	}

	public void setJwsDaoQueryTemplate(String jwsDaoQueryTemplate) {
		this.jwsDaoQueryTemplate = jwsDaoQueryTemplate;
	}

	public String getJwsResultVariableName() {
		return this.jwsResultVariableName;
	}

	public void setJwsResultVariableName(String jwsResultVariableName) {
		this.jwsResultVariableName = jwsResultVariableName;
	}

	public JwsDynamicRestDetail getJwsDynamicRestDetail() {
		return this.jwsDynamicRestDetail;
	}

	public void setJwsDynamicRestDetail(JwsDynamicRestDetail jwsDynamicRestDetail) {
		this.jwsDynamicRestDetail = jwsDynamicRestDetail;
	}

	public Integer getJwsQuerySequence() {
		return this.jwsQuerySequence;
	}

	public void setJwsQuerySequence(Integer jwsQuerySequence) {
		this.jwsQuerySequence = jwsQuerySequence;
	}

	public Integer getQueryType() {
		return queryType;
	}

	public void setQueryType(Integer queryType) {
		this.queryType = queryType;
	}

	public String getJwsDynamicRestDetailId() {
		return jwsDynamicRestDetailId;
	}

	public void setJwsDynamicRestDetailId(String jwsDynamicRestDetailId) {
		this.jwsDynamicRestDetailId = jwsDynamicRestDetailId;
	}

	public String getDatasourceId() {
		return datasourceId;
	}

	public void setDatasourceId(String datasourceId) {
		this.datasourceId = datasourceId;
	}

	public String getDaoQueryChecksum() {
		return daoQueryChecksum;
	}

	public void setDaoQueryChecksum(String daoQueryChecksum) {
		this.daoQueryChecksum = daoQueryChecksum;
	}

	@Override
	public int hashCode() {
		return Objects.hash(jwsDaoDetailsId, jwsDaoQueryTemplate, jwsDynamicRestDetail, jwsResultVariableName, datasourceId);
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
		JwsDynamicRestDaoDetail other = (JwsDynamicRestDaoDetail) obj;
		return Objects.equals(jwsDaoDetailsId, other.jwsDaoDetailsId) && Objects.equals(jwsDaoQueryTemplate, other.jwsDaoQueryTemplate)
				&& Objects.equals(jwsDynamicRestDetail, other.jwsDynamicRestDetail)
				&& Objects.equals(jwsResultVariableName, other.jwsResultVariableName)
				&& Objects.equals(datasourceId, other.datasourceId);
	}

	@Override
	public String toString() {
		return "JwsDynamicRestDaoDetail [jwsDaoDetailsId=" + jwsDaoDetailsId + ", jwsDaoQueryTemplate=" + jwsDaoQueryTemplate
				+ ", jwsResultVariableName=" + jwsResultVariableName + ", jwsDynamicRestDetail=" + jwsDynamicRestDetail
				+ ", datasourceId=" + datasourceId + "]";
	}

	public JwsDynamicRestDaoDetail getObject() {
		JwsDynamicRestDaoDetail dynaRest = new JwsDynamicRestDaoDetail();
		dynaRest.setJwsDaoDetailsId(jwsDaoDetailsId);
		if (jwsDaoQueryTemplate != null) {
			dynaRest.setJwsDaoQueryTemplate(jwsDaoQueryTemplate.trim());
		} else {
			dynaRest.setJwsDaoQueryTemplate(jwsDaoQueryTemplate);
		}

		dynaRest.setJwsDynamicRestDetailId(jwsDynamicRestDetailId != null ? jwsDynamicRestDetailId.trim() : jwsDynamicRestDetailId);
		dynaRest.setJwsQuerySequence(jwsQuerySequence);
		dynaRest.setJwsResultVariableName(jwsResultVariableName != null ? jwsResultVariableName.trim() : jwsResultVariableName);
		dynaRest.setQueryType(queryType);
		dynaRest.setDatasourceId(datasourceId != null ? datasourceId.trim() : datasourceId);
		return dynaRest;
	}

}