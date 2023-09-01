package com.trigyn.jws.dbutils.vo;

import java.util.Date;

public class AdditionalDatasourceVO {
	private String				additionalDatasourceId	= null;

	private String				createdBy				= null;

	private Date				createdDate				= null;

	private String				datasourceConfiguration	= null;

	private String				datasourceName			= null;

	private Integer				isDeleted				= null;

	private String				lastUpdatedBy			= null;

	private Date				lastUpdatedTs			= null;

	private String				datasourceLookupId		= null;

	private DatasourceLookUpVO	datasourceLookupVO		= null;

	private String				databaseDisplayProductName	= null;

	private Double				datasourceSupportedVersion	= null;
	
	public String getAdditionalDatasourceId() {
		return additionalDatasourceId;
	}

	public void setAdditionalDatasourceId(String additionalDatasourceId) {
		this.additionalDatasourceId = additionalDatasourceId;
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

	public String getDatasourceConfiguration() {
		return datasourceConfiguration;
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
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
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

	public String getDatasourceLookupId() {
		return datasourceLookupId;
	}

	public void setDatasourceLookupId(String datasourceLookupId) {
		this.datasourceLookupId = datasourceLookupId;
	}

	public DatasourceLookUpVO getDatasourceLookupVO() {
		return datasourceLookupVO;
	}

	public void setDatasourceLookupVO(DatasourceLookUpVO datasourceLookupVO) {
		this.datasourceLookupVO = datasourceLookupVO;
	}

	public String getDatabaseDisplayProductName() {
		return databaseDisplayProductName;
	}

	public void setDatabaseDisplayProductName(String databaseDisplayProductName) {
		this.databaseDisplayProductName = databaseDisplayProductName;
	}

	public Double getDatasourceSupportedVersion() {
		return datasourceSupportedVersion;
	}

	public void setDatasourceSupportedVersion(Double datasourceSupportedVersion) {
		this.datasourceSupportedVersion = datasourceSupportedVersion;
	}

}
