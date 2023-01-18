package com.trigyn.jws.typeahead.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.text.StringEscapeUtils;

@Entity
@Table(name = "jq_autocomplete_details")
public class Autocomplete implements Serializable {

	private static final long	serialVersionUID		= 1L;

	@Id
	@Column(name = "ac_id")
	private String				autocompleteId			= null;

	@Column(name = "ac_description")
	private String				autocompleteDesc		= null;

	@Column(name = "ac_select_query")
	private String				autocompleteSelectQuery	= null;

	@Column(name = "ac_type_id")
	private Integer				acTypeId				= 1;

	@Column(name = "created_by")
	private String				createdBy				= null;

	@Column(name = "created_date")
	private Date				createdDate				= null;

	@Column(name = "datasource_id")
	private String				datasourceId			= null;

	@Column(name = "last_updated_by")
	private String				lastUpdatedBy			= null;

	@Column(name = "last_updated_ts")
	private Date				lastUpdatedTs			= null;

	@Column(name = "is_custom_updated")
	private Integer				isCustomUpdated			= 1;

	public Autocomplete() {

	}

	public Autocomplete(String autocompleteId, String autocompleteDesc, String autocompleteSelectQuery,
			Integer acTypeId, String createdBy, Date createdDate, String datasourceId, String lastUpdatedBy,
			Date lastUpdatedTs) {
		this.autocompleteId				= autocompleteId;
		this.autocompleteDesc			= autocompleteDesc;
		this.autocompleteSelectQuery	= autocompleteSelectQuery;
		this.acTypeId					= acTypeId;
		this.createdBy					= createdBy;
		this.createdDate				= createdDate;
		this.datasourceId				= datasourceId;
		this.lastUpdatedBy				= lastUpdatedBy;
		this.lastUpdatedTs				= lastUpdatedTs;
	}

	public String getAutocompleteId() {
		return autocompleteId;
	}

	public void setAutocompleteId(String autocompleteId) {
		this.autocompleteId = autocompleteId;
	}

	public String getAutocompleteDesc() {
		return autocompleteDesc;
	}

	public void setAutocompleteDesc(String autocompleteDesc) {
		this.autocompleteDesc = autocompleteDesc;
	}

	public String getAutocompleteSelectQuery() {
		return autocompleteSelectQuery;
	}

	public void setAutocompleteSelectQuery(String autocompleteSelectQuery) {
		this.autocompleteSelectQuery = autocompleteSelectQuery;
	}

	public Integer getAcTypeId() {
		return acTypeId;
	}

	public void setAcTypeId(Integer acTypeId) {
		this.acTypeId = acTypeId;
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

	public Integer getIsCustomUpdated() {
		return isCustomUpdated;
	}

	public void setIsCustomUpdated(Integer isCustomUpdated) {
		this.isCustomUpdated = isCustomUpdated;
	}

	public Autocomplete getObject() {
		Autocomplete autocomplete = new Autocomplete();

		autocomplete.setAcTypeId(acTypeId);
		autocomplete.setAutocompleteDesc(autocompleteDesc != null ? autocompleteDesc.trim() : autocompleteDesc);
		autocomplete.setAutocompleteId(autocompleteId);
		autocomplete.setDatasourceId(datasourceId);
		autocomplete.setCreatedBy(createdBy);
		autocomplete.setCreatedDate(createdDate);
		autocomplete.setLastUpdatedBy(lastUpdatedBy);
		autocomplete.setLastUpdatedTs(lastUpdatedTs);
		if (autocompleteSelectQuery != null) {
			autocomplete.setAutocompleteSelectQuery(
					StringEscapeUtils.unescapeXml("<![CDATA[" + autocompleteSelectQuery.trim() + "]]>"));
		} else {
			autocomplete.setAutocompleteSelectQuery(
					StringEscapeUtils.unescapeXml("<![CDATA[" + autocompleteSelectQuery + "]]>"));
		}
		autocomplete.setAutocompleteSelectQuery(
				StringEscapeUtils.unescapeXml("<![CDATA[" + autocompleteSelectQuery + "]]>"));
		autocomplete.setAcTypeId(this.acTypeId);
		return autocomplete;
	}

	@Override
	public int hashCode() {
		return Objects.hash(acTypeId, autocompleteDesc, autocompleteId, autocompleteSelectQuery, createdBy, createdDate,
				datasourceId, lastUpdatedBy, lastUpdatedTs);
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
		Autocomplete other = (Autocomplete) obj;
		return Objects.equals(acTypeId, other.acTypeId) && Objects.equals(autocompleteDesc, other.autocompleteDesc)
				&& Objects.equals(autocompleteId, other.autocompleteId)
				&& Objects.equals(autocompleteSelectQuery, other.autocompleteSelectQuery)
				&& Objects.equals(createdBy, other.createdBy) && Objects.equals(createdDate, other.createdDate)
				&& Objects.equals(datasourceId, other.datasourceId)
				&& Objects.equals(lastUpdatedBy, other.lastUpdatedBy)
				&& Objects.equals(lastUpdatedTs, other.lastUpdatedTs);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Autocomplete [autocompleteId=").append(autocompleteId).append(", autocompleteDesc=")
				.append(autocompleteDesc).append(", autocompleteSelectQuery=").append(autocompleteSelectQuery)
				.append(", acTypeId=").append(acTypeId).append(", createdBy=").append(createdBy)
				.append(", createdDate=").append(createdDate).append(", datasourceId=").append(datasourceId)
				.append(", lastUpdatedBy=").append(lastUpdatedBy).append(", lastUpdatedTs=").append(lastUpdatedTs)
				.append("]");
		return builder.toString();
	}

}