package com.trigyn.jws.dbutils.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.trigyn.jws.dbutils.vo.AdditionalDatasourceVO;

@Entity
@Table(name = "jq_additional_datasource")
@NamedQuery(name = "AdditionalDatasource.findAll", query = "SELECT j FROM AdditionalDatasource j")
public class AdditionalDatasource implements Serializable {
	private static final long	serialVersionUID		= 1L;

	@Id
	@GeneratedValue(generator = "inquisitive-uuid")
	@GenericGenerator(name = "inquisitive-uuid", strategy = "com.trigyn.jws.dbutils.configurations.CustomUUIDGenerator")
	@Column(name = "additional_datasource_id")
	private String				additionalDatasourceId	= null;

	@Column(name = "created_by")
	private String				createdBy				= null;

	@Column(name = "created_date")
	private Date				createdDate				= null;

	@Column(name = "datasource_configuration")
	private String				datasourceConfiguration	= null;

	@Column(name = "datasource_name")
	private String				datasourceName			= null;

	@Column(name = "is_deleted")
	private Integer				isDeleted				= null;

	@Column(name = "last_updated_by")
	private String				lastUpdatedBy			= null;

	@Column(name = "last_updated_ts")
	private Date				lastUpdatedTs			= null;

	@Column(name = "datasource_lookup_id")
	private String				datasourceLookupId		= null;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "datasource_lookup_id", insertable = false, updatable = false)
	private DatasourceLookUp	datasourceLookup		= null;

	public AdditionalDatasource() {

	}

	public AdditionalDatasource(String additionalDatasourceId, String createdBy, Date createdDate, String datasourceConfiguration,
			String datasourceName, Integer isDeleted, String lastUpdatedBy, Date lastUpdatedTs, String datasourceLookupId,
			DatasourceLookUp datasourceLookup) {
		this.additionalDatasourceId		= additionalDatasourceId;
		this.createdBy					= createdBy;
		this.createdDate				= createdDate;
		this.datasourceConfiguration	= datasourceConfiguration;
		this.datasourceName				= datasourceName;
		this.isDeleted					= isDeleted;
		this.lastUpdatedBy				= lastUpdatedBy;
		this.lastUpdatedTs				= lastUpdatedTs;
		this.datasourceLookupId			= datasourceLookupId;
		this.datasourceLookup			= datasourceLookup;
	}

	public String getAdditionalDatasourceId() {
		return this.additionalDatasourceId;
	}

	public void setAdditionalDatasourceId(String additionalDatasourceId) {
		this.additionalDatasourceId = additionalDatasourceId;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getDatasourceConfiguration() {
		return this.datasourceConfiguration;
	}

	public void setDatasourceConfiguration(String datasourceConfiguration) {
		this.datasourceConfiguration = datasourceConfiguration;
	}

	public String getDatasourceName() {
		return datasourceName;
	}

	public void setDatasourceName(String datasourceName) {
		this.datasourceName = datasourceName;
	}

	public Integer getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getLastUpdatedBy() {
		return this.lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastUpdatedTs() {
		return this.lastUpdatedTs;
	}

	public void setLastUpdatedTs(Date lastUpdatedTs) {
		this.lastUpdatedTs = lastUpdatedTs;
	}

	public String getDatasourceLookupId() {
		return datasourceLookupId;
	}

	public void setDatasourceLookupId(String datasourceLookupId) {
		this.datasourceLookupId = datasourceLookupId;
	}

	public DatasourceLookUp getDatasourceLookUp() {
		return this.datasourceLookup;
	}

	public void setDatasourceLookUp(DatasourceLookUp datasourceLookup) {
		this.datasourceLookup = datasourceLookup;
	}

	@Override
	public int hashCode() {
		return Objects.hash(additionalDatasourceId, createdBy, createdDate, datasourceConfiguration, datasourceLookup, datasourceLookupId,
				datasourceName, isDeleted, lastUpdatedTs, lastUpdatedBy);
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
		AdditionalDatasource other = (AdditionalDatasource) obj;
		return Objects.equals(additionalDatasourceId, other.additionalDatasourceId) && Objects.equals(createdBy, other.createdBy)
				&& Objects.equals(createdDate, other.createdDate) && Objects.equals(datasourceConfiguration, other.datasourceConfiguration)
				&& Objects.equals(datasourceLookup, other.datasourceLookup) && Objects.equals(datasourceLookupId, other.datasourceLookupId)
				&& Objects.equals(datasourceName, other.datasourceName) && Objects.equals(isDeleted, other.isDeleted)
				&& Objects.equals(lastUpdatedTs, other.lastUpdatedTs) && Objects.equals(lastUpdatedBy, other.lastUpdatedBy);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AdditionalDatasource [additionalDatasourceId=").append(additionalDatasourceId).append(", createdBy=")
				.append(createdBy).append(", createdDate=").append(createdDate).append(", datasourceConfiguration=")
				.append(datasourceConfiguration).append(", datasourceName=").append(datasourceName).append(", isDeleted=").append(isDeleted)
				.append(", updatedBy=").append(lastUpdatedBy).append(", lastUpdatedTs=").append(lastUpdatedTs)
				.append(", datasourceLookupId=").append(datasourceLookupId).append(", datasourceLookup=").append(datasourceLookup)
				.append("]");
		return builder.toString();
	}

	public AdditionalDatasource getObject() {
		AdditionalDatasource additionalDatasource = new AdditionalDatasource();

		additionalDatasource.setAdditionalDatasourceId(additionalDatasourceId);
		additionalDatasource.setCreatedBy(createdBy);
		additionalDatasource.setCreatedDate(createdDate);
		additionalDatasource.setDatasourceConfiguration(datasourceConfiguration);
		additionalDatasource.setDatasourceLookUp(datasourceLookup.getObject());
		additionalDatasource.setDatasourceLookupId(datasourceLookupId);
		additionalDatasource.setDatasourceName(datasourceName);
		additionalDatasource.setIsDeleted(isDeleted);
		additionalDatasource.setLastUpdatedBy(lastUpdatedBy);
		additionalDatasource.setLastUpdatedTs(lastUpdatedTs);
		additionalDatasource.setDatasourceLookUp(datasourceLookup.getObject());
		
		return additionalDatasource;
	}

	public AdditionalDatasourceVO convertEntityToVO(AdditionalDatasource additionalDatasource) {

		AdditionalDatasourceVO additionalDatasourceVO = new AdditionalDatasourceVO();
		
		additionalDatasourceVO.setAdditionalDatasourceId(additionalDatasource.getAdditionalDatasourceId());
		additionalDatasourceVO.setCreatedBy(additionalDatasource.getCreatedBy());
		additionalDatasourceVO.setCreatedDate(additionalDatasource.getCreatedDate());
		additionalDatasourceVO.setDatasourceConfiguration(additionalDatasource.getDatasourceConfiguration());
		additionalDatasourceVO.setDatasourceLookupId(additionalDatasource.getDatasourceLookupId());
		additionalDatasourceVO.setDatasourceLookupVO(additionalDatasource.getDatasourceLookUp().convertEntityToVO(additionalDatasource.getDatasourceLookUp()));
		additionalDatasourceVO.setDatasourceName(additionalDatasource.getDatasourceName());
		additionalDatasourceVO.setIsDeleted(additionalDatasource.getIsDeleted());
		additionalDatasourceVO.setLastUpdatedBy(additionalDatasource.getLastUpdatedBy());
		additionalDatasourceVO.setLastUpdatedTs(additionalDatasource.getLastUpdatedTs());
		
		return additionalDatasourceVO;
	}
}