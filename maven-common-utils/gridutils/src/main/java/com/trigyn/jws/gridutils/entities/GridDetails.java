package com.trigyn.jws.gridutils.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "jq_grid_details")
public class GridDetails implements Serializable {

	private static final long	serialVersionUID		= 4472368074028971649L;

	@Id
	@Column(name = "grid_id", nullable = false)
	private String				gridId					= null;

	@Column(name = "grid_name")
	private String				gridName				= null;

	@Column(name = "grid_description")
	private String				gridDescription			= null;

	@Column(name = "grid_table_name")
	private String				gridTableName			= null;

	@Column(name = "grid_column_names")
	private String				gridColumnName			= null;

	@Column(name = "query_type")
	private Integer				queryType				= null;

	@Column(name = "grid_type_id")
	private Integer				gridTypeId				= 1;

	@Column(name = "created_by")
	private String				createdBy				= null;

	@Column(name = "created_date")
	private Date				createdDate				= null;

	@Column(name = "datasource_id")
	private String				datasourceId			= null;

	@Column(name = "custom_filter_criteria")
	private String				customFilterCriteria	= null;

	@Column(name = "last_updated_by")
	private String				lastUpdatedBy			= null;

	@Column(name = "last_updated_ts")
	private Date				lastUpdatedTs			= null;

	@Column(name = "is_custom_updated")
	private Integer				isCustomUpdated			= 1;

	public GridDetails() {

	}

	public GridDetails(String gridId, String gridName, String gridDescription, String gridTableName,
			String gridColumnName, Integer queryType, Integer gridTypeId, String createdBy, Date createdDate,
			String datasourceId, String customFilterCriteria, String lastUpdatedBy, Date lastUpdatedTs) {
		this.gridId					= gridId;
		this.gridName				= gridName;
		this.gridDescription		= gridDescription;
		this.gridTableName			= gridTableName;
		this.gridColumnName			= gridColumnName;
		this.queryType				= queryType;
		this.gridTypeId				= gridTypeId;
		this.createdBy				= createdBy;
		this.createdDate			= createdDate;
		this.datasourceId			= datasourceId;
		this.customFilterCriteria	= customFilterCriteria;
		this.lastUpdatedBy			= lastUpdatedBy;
		this.lastUpdatedTs			= lastUpdatedTs;
	}

	public String getGridId() {
		return gridId;
	}

	public void setGridId(String gridId) {
		this.gridId = gridId;
	}

	public String getGridName() {
		return gridName;
	}

	public void setGridName(String gridName) {
		this.gridName = gridName;
	}

	public String getGridDescription() {
		return gridDescription;
	}

	public void setGridDescription(String gridDescription) {
		this.gridDescription = gridDescription;
	}

	public String getGridTableName() {
		return gridTableName;
	}

	public void setGridTableName(String gridTableName) {
		this.gridTableName = gridTableName;
	}

	public String getGridColumnName() {
		return gridColumnName;
	}

	public void setGridColumnName(String gridColumnName) {
		this.gridColumnName = gridColumnName;
	}

	public Integer getQueryType() {
		return this.queryType;
	}

	public void setQueryType(Integer queryType) {
		this.queryType = queryType;
	}

	public Integer getGridTypeId() {
		return gridTypeId;
	}

	public void setGridTypeId(Integer gridTypeId) {
		this.gridTypeId = gridTypeId;
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

	public String getCustomFilterCriteria() {
		return customFilterCriteria;
	}

	public void setCustomFilterCriteria(String customFilterCriteria) {
		this.customFilterCriteria = customFilterCriteria;
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

	public Integer getIsCustomUpdated() {
		return isCustomUpdated;
	}

	public void setIsCustomUpdated(Integer isCustomUpdated) {
		this.isCustomUpdated = isCustomUpdated;
	}

	public GridDetails getObject() {
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(createdBy, datasourceId, gridColumnName, gridDescription, gridId, gridName, gridTableName,
				gridTypeId, lastUpdatedBy, queryType);
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
		GridDetails other = (GridDetails) obj;
		return Objects.equals(createdBy, other.createdBy) && Objects.equals(datasourceId, other.datasourceId)
				&& Objects.equals(gridColumnName, other.gridColumnName)
				&& Objects.equals(gridDescription, other.gridDescription) && Objects.equals(gridId, other.gridId)
				&& Objects.equals(gridName, other.gridName) && Objects.equals(gridTableName, other.gridTableName)
				&& Objects.equals(gridTypeId, other.gridTypeId) && Objects.equals(lastUpdatedBy, other.lastUpdatedBy)
				&& Objects.equals(queryType, other.queryType);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GridDetails [gridId=").append(gridId).append(", gridName=").append(gridName)
				.append(", gridDescription=").append(gridDescription).append(", gridTableName=").append(gridTableName)
				.append(", gridColumnName=").append(gridColumnName).append(", queryType=").append(queryType)
				.append(", gridTypeId=").append(gridTypeId).append(", createdBy=").append(createdBy)
				.append(", createdDate=").append(createdDate).append(", datasourceId=").append(datasourceId)
				.append(", lastUpdatedBy=").append(lastUpdatedBy).append(", lastUpdatedTs=").append(lastUpdatedTs)
				.append("]");
		return builder.toString();
	}
}