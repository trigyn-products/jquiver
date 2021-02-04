package com.trigyn.jws.dbutils.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * The persistent class for the jws_lookup database table.
 * 
 */
@Entity
@Table(name = "jws_lookup")
@NamedQuery(name = "JwsLookup.findAll", query = "SELECT j FROM JwsLookup j")
public class JwsLookup implements Serializable {
	private static final long	serialVersionUID	= 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "lookup_id", unique = true, nullable = false, length = 50)
	private String				lookupId			= null;

	@Column(name = "is_deleted")
	private Integer				isDeleted			= null;

	@Column(name = "lookup_name", length = 1000)
	private String				lookupName			= null;

	@Column(name = "record_id", nullable = false)
	private Integer				recordId			= null;

	@OneToMany(mappedBy = "jwsLookup")
	private List<JwsLookupI18n>	jwsLookupI18ns		= null;

	public JwsLookup() {
	}

	public JwsLookup(String lookupId, Integer isDeleted, String lookupName, Integer recordId,
			List<JwsLookupI18n> jwsLookupI18ns) {
		this.lookupId		= lookupId;
		this.isDeleted		= isDeleted;
		this.lookupName		= lookupName;
		this.recordId		= recordId;
		this.jwsLookupI18ns	= jwsLookupI18ns;
	}

	public String getLookupId() {
		return this.lookupId;
	}

	public void setLookupId(String lookupId) {
		this.lookupId = lookupId;
	}

	public Integer getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getLookupName() {
		return this.lookupName;
	}

	public void setLookupName(String lookupName) {
		this.lookupName = lookupName;
	}

	public Integer getRecordId() {
		return this.recordId;
	}

	public void setRecordId(Integer recordId) {
		this.recordId = recordId;
	}

	public List<JwsLookupI18n> getJwsLookupI18ns() {
		return this.jwsLookupI18ns;
	}

	public void setJwsLookupI18ns(List<JwsLookupI18n> jwsLookupI18ns) {
		this.jwsLookupI18ns = jwsLookupI18ns;
	}

	public JwsLookupI18n addJwsLookupI18n(JwsLookupI18n jwsLookupI18n) {
		getJwsLookupI18ns().add(jwsLookupI18n);
		jwsLookupI18n.setJwsLookup(this);

		return jwsLookupI18n;
	}

	public JwsLookupI18n removeJwsLookupI18n(JwsLookupI18n jwsLookupI18n) {
		getJwsLookupI18ns().remove(jwsLookupI18n);
		jwsLookupI18n.setJwsLookup(null);

		return jwsLookupI18n;
	}

	@Override
	public int hashCode() {
		return Objects.hash(isDeleted, jwsLookupI18ns, lookupId, lookupName, recordId);
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
		JwsLookup other = (JwsLookup) obj;
		return Objects.equals(isDeleted, other.isDeleted) && Objects.equals(jwsLookupI18ns, other.jwsLookupI18ns)
				&& Objects.equals(lookupId, other.lookupId) && Objects.equals(lookupName, other.lookupName)
				&& Objects.equals(recordId, other.recordId);
	}

	@Override
	public String toString() {
		return "JwsLookup [lookupId=" + lookupId + ", isDeleted=" + isDeleted + ", lookupName=" + lookupName
				+ ", recordId=" + recordId + ", jwsLookupI18ns=" + jwsLookupI18ns + "]";
	}

}