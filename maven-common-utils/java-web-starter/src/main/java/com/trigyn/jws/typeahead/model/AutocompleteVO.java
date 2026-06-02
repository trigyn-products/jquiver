package com.trigyn.jws.typeahead.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class AutocompleteVO implements Serializable {

	private static final long	serialVersionUID		= 1L;

	private String				autocompleteId			= null;

	private String				autocompleteDesc		= null;

	private String				autocompleteSelectQuery	= null;

	private String				dataSourceId			= null;

	private String				createdBy				= null;

	private Date				createdDate				= null;

	private String				lastUpdatedBy			= null;

	private Date				lastUpdatedTs			= null;

	private Integer				acTypeId				= 1;

	private Integer				isCustomUpdated			= 1;

	public AutocompleteVO() {
	}

	public AutocompleteVO(String autocompleteId, String autocompleteDesc, String autocompleteSelectQuery,
			String dataSourceId) {
		this.autocompleteId				= autocompleteId;
		this.autocompleteDesc			= autocompleteDesc;
		this.autocompleteSelectQuery	= autocompleteSelectQuery;
		this.dataSourceId				= dataSourceId;
	}

	public AutocompleteVO(String autocompleteId, String autocompleteDesc, String autocompleteSelectQuery,
			String dataSourceId, String createdBy, Date createdDate, String lastUpdatedBy, Date lastUpdatedTs) {
		this.autocompleteId				= autocompleteId;
		this.autocompleteDesc			= autocompleteDesc;
		this.autocompleteSelectQuery	= autocompleteSelectQuery;
		this.dataSourceId				= dataSourceId;
		this.createdBy					= createdBy;
		this.createdDate				= createdDate;
		this.lastUpdatedBy				= lastUpdatedBy;
		this.lastUpdatedTs				= lastUpdatedTs;
	}

	public String getAutocompleteId() {
		return this.autocompleteId;
	}

	public void setAutocompleteId(String autocompleteId) {
		this.autocompleteId = autocompleteId;
	}

	public String getAutocompleteDesc() {
		return this.autocompleteDesc;
	}

	public void setAutocompleteDesc(String autocompleteDesc) {
		this.autocompleteDesc = autocompleteDesc;
	}

	public String getDataSourceId() {
		return dataSourceId;
	}

	public void setDataSourceId(String dataSourceId) {
		this.dataSourceId = dataSourceId;
	}

	public String getAutocompleteSelectQuery() {
		return autocompleteSelectQuery;
	}

	public void setAutocompleteSelectQuery(String autocompleteSelectQuery) {
		this.autocompleteSelectQuery = autocompleteSelectQuery;
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

	public Integer getAcTypeId() {
		return acTypeId;
	}

	public void setAcTypeId(Integer acTypeId) {
		this.acTypeId = acTypeId;
	}

	public Integer getIsCustomUpdated() {
		return isCustomUpdated;
	}

	public void setIsCustomUpdated(Integer isCustomUpdated) {
		this.isCustomUpdated = isCustomUpdated;
	}

	@Override
	public String toString() {
		return "AutocompleteVO [autocompleteId=" + autocompleteId + ", autocompleteDesc=" + autocompleteDesc
				+ ", autocompleteSelectQuery=" + autocompleteSelectQuery + ", dataSourceId=" + dataSourceId
				+ ", createdBy=" + createdBy + ", createdDate=" + createdDate + ", lastUpdatedBy=" + lastUpdatedBy
				+ ", lastUpdatedTs=" + lastUpdatedTs + ", acTypeId=" + acTypeId + ", isCustomUpdated=" + isCustomUpdated
				+ "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(acTypeId, autocompleteDesc, autocompleteId, autocompleteSelectQuery, createdBy, createdDate,
				dataSourceId, isCustomUpdated, lastUpdatedBy, lastUpdatedTs);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AutocompleteVO other = (AutocompleteVO) obj;
		return Objects.equals(acTypeId, other.acTypeId) && Objects.equals(autocompleteDesc, other.autocompleteDesc)
				&& Objects.equals(autocompleteId, other.autocompleteId)
				&& Objects.equals(autocompleteSelectQuery, other.autocompleteSelectQuery)
				&& Objects.equals(createdBy, other.createdBy) && Objects.equals(createdDate, other.createdDate)
				&& Objects.equals(dataSourceId, other.dataSourceId)
				&& Objects.equals(isCustomUpdated, other.isCustomUpdated)
				&& Objects.equals(lastUpdatedBy, other.lastUpdatedBy)
				&& Objects.equals(lastUpdatedTs, other.lastUpdatedTs);
	}

}