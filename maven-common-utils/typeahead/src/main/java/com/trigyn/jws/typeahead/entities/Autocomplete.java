package com.trigyn.jws.typeahead.entities;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.text.StringEscapeUtils;

@Entity
@Table(name = "jq_autocomplete_details")
public class Autocomplete {

	@Id
	@Column(name = "ac_id")
	private String	autocompleteId			= null;

	@Column(name = "ac_description")
	private String	autocompleteDesc		= null;

	@Column(name = "ac_select_query")
	private String	autocompleteSelectQuery	= null;

	@Column(name = "ac_type_id")
	private Integer	acTypeId				= 1;

	public Autocomplete() {
	}

	public Autocomplete(String autocompleteId, String autocompleteDesc, String autocompleteSelectQuery) {
		this.autocompleteId				= autocompleteId;
		this.autocompleteDesc			= autocompleteDesc;
		this.autocompleteSelectQuery	= autocompleteSelectQuery;
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

	public String getAutocompleteSelectQuery() {
		return this.autocompleteSelectQuery;
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

	@Override
	public int hashCode() {
		return Objects.hash(acTypeId, autocompleteDesc, autocompleteId, autocompleteSelectQuery);
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
				&& Objects.equals(autocompleteSelectQuery, other.autocompleteSelectQuery);
	}

	public Autocomplete getObject() {
		Autocomplete autocomplete = new Autocomplete();

		autocomplete.setAcTypeId(acTypeId);
		autocomplete.setAutocompleteDesc(autocompleteDesc != null ? autocompleteDesc.trim() : autocompleteDesc);
		autocomplete.setAutocompleteId(autocompleteId);
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
	public String toString() {
		return "Autocomplete [autocompleteId=" + autocompleteId + ", autocompleteDesc=" + autocompleteDesc
				+ ", autocompleteSelectQuery=" + autocompleteSelectQuery + ", acTypeId=" + acTypeId + "]";
	}

}