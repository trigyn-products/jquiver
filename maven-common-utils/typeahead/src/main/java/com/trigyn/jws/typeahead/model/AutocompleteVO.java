package com.trigyn.jws.typeahead.model;

import java.io.Serializable;
import java.util.Objects;

public class AutocompleteVO implements Serializable {

	private static final long	serialVersionUID	= 1L;

	private String				autocompleteId		= null;

	private String				autocompleteDesc	= null;

	private String				autocompleteQuery	= null;

	private String				dataSourceId		= null;

	public AutocompleteVO() {
	}

	public AutocompleteVO(String autocompleteId, String autocompleteDesc, String autocompleteQuery, String dataSourceId) {
		this.autocompleteId		= autocompleteId;
		this.autocompleteDesc	= autocompleteDesc;
		this.autocompleteQuery	= autocompleteQuery;
		this.dataSourceId		= dataSourceId;
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

	public String getAutocompleteQuery() {
		return this.autocompleteQuery;
	}

	public void setAutocompleteQuery(String autocompleteQuery) {
		this.autocompleteQuery = autocompleteQuery;
	}

	public String getDataSourceId() {
		return dataSourceId;
	}

	public void setDataSourceId(String dataSourceId) {
		this.dataSourceId = dataSourceId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(autocompleteDesc, autocompleteId, autocompleteQuery, dataSourceId);
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
		AutocompleteVO other = (AutocompleteVO) obj;
		return Objects.equals(autocompleteDesc, other.autocompleteDesc) && Objects.equals(autocompleteId, other.autocompleteId)
				&& Objects.equals(autocompleteQuery, other.autocompleteQuery) && Objects.equals(dataSourceId, other.dataSourceId);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AutocompleteVO [autocompleteId=").append(autocompleteId).append(", autocompleteDesc=").append(autocompleteDesc)
				.append(", autocompleteQuery=").append(autocompleteQuery).append(", dataSourceId=").append(dataSourceId).append("]");
		return builder.toString();
	}

}