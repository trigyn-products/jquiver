package com.trigyn.jws.dbutils.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.*;

/**
 * The persistent class for the jws_lookup_i18n database table.
 * 
 */
@Entity
@Table(name = "jws_lookup_i18n")
@NamedQuery(name = "JwsLookupI18n.findAll", query = "SELECT j FROM JwsLookupI18n j")
public class JwsLookupI18n implements Serializable {
	private static final long	serialVersionUID	= 1L;

	@Id
	@Column(unique = true, nullable = false, length = 50)
	private String				id					= null;

	@Column(name = "language_id", nullable = false)
	private Integer				languageId			= null;

	@Column(name = "record_description", nullable = false, length = 5000)
	private String				recordDescription	= null;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "jws_lookup_id", nullable = false)
	private JwsLookup			jwsLookup			= null;

	public JwsLookupI18n() {
	}

	public JwsLookupI18n(String id, Integer languageId, String recordDescription, JwsLookup jwsLookup) {
		this.id					= id;
		this.languageId			= languageId;
		this.recordDescription	= recordDescription;
		this.jwsLookup			= jwsLookup;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getLanguageId() {
		return this.languageId;
	}

	public void setLanguageId(Integer languageId) {
		this.languageId = languageId;
	}

	public String getRecordDescription() {
		return this.recordDescription;
	}

	public void setRecordDescription(String recordDescription) {
		this.recordDescription = recordDescription;
	}

	public JwsLookup getJwsLookup() {
		return this.jwsLookup;
	}

	public void setJwsLookup(JwsLookup jwsLookup) {
		this.jwsLookup = jwsLookup;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, jwsLookup, languageId, recordDescription);
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
		JwsLookupI18n other = (JwsLookupI18n) obj;
		return Objects.equals(id, other.id) && Objects.equals(jwsLookup, other.jwsLookup)
				&& Objects.equals(languageId, other.languageId)
				&& Objects.equals(recordDescription, other.recordDescription);
	}

	@Override
	public String toString() {
		return "JwsLookupI18n [id=" + id + ", languageId=" + languageId + ", recordDescription=" + recordDescription
				+ ", jwsLookup=" + jwsLookup + "]";
	}

}