package com.trigyn.jws.gridutils.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "grid_details")
public class GridDetails implements Serializable {

	private static final long	serialVersionUID	= 4472368074028971649L;

	@Id
	@Column(name = "grid_id", nullable = false)
	private String				gridId				= null;

	@Column(name = "grid_name")
	private String				gridName			= null;

	@Column(name = "grid_description")
	private String				gridDescription		= null;

	@Column(name = "grid_table_name")
	private String				gridTableName		= null;

	@Column(name = "grid_column_names")
	private String				gridColumnName		= null;

	@Column(name = "query_type")
	private Integer				queryType			= null;

	@Column(name = "grid_type_id")
	private Integer				gridTypeId			= 1;

	public GridDetails() {

	}

	public GridDetails(String gridId, String gridName, String gridDescription, String gridTableName,
			String gridColumnNames, Integer queryType) {
		this.gridId				= gridId;
		this.gridName			= gridName;
		this.gridDescription	= gridDescription;
		this.gridTableName		= gridTableName;
		this.gridColumnName		= gridColumnNames;
		this.queryType			= queryType;
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

	@Override
	public int hashCode() {
		return Objects.hash(gridColumnName, gridDescription, gridId, gridName, gridTableName, gridTypeId, queryType);
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
		return Objects.equals(gridColumnName, other.gridColumnName)
				&& Objects.equals(gridDescription, other.gridDescription) && Objects.equals(gridId, other.gridId)
				&& Objects.equals(gridName, other.gridName) && Objects.equals(gridTableName, other.gridTableName)
				&& Objects.equals(gridTypeId, other.gridTypeId) && Objects.equals(queryType, other.queryType);
	}

	@Override
	public String toString() {
		return "GridDetails [gridId=" + gridId + ", gridName=" + gridName + ", gridDescription=" + gridDescription
				+ ", gridTableName=" + gridTableName + ", gridColumnNames=" + gridColumnName + ", queryType="
				+ queryType + ", gridTypeId=" + gridTypeId + "]";
	}

	public GridDetails getObject() {
		return this;
	}

}