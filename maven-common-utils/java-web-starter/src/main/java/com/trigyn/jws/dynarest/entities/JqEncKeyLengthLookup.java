package com.trigyn.jws.dynarest.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the jq_enc_key_length_lookup database table.
 * 
 */
@Entity
@Table(name="jq_enc_key_length_lookup")
@NamedQuery(name="JqEncKeyLengthLookup.findAll", query="SELECT j FROM JqEncKeyLengthLookup j")
public class JqEncKeyLengthLookup implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="key_length_id")
	private Integer keyLengthId;

	@Column(name="key_length")
	private Integer keyLength;

	//bi-directional many-to-one association to JqEncAlgModPadKeyLookup
	@OneToMany(mappedBy="jqEncKeyLengthLookup")
	private List<JqEncAlgModPadKeyLookup> jqEncAlgModPadKeyLookups;

	public JqEncKeyLengthLookup() {
	}

	public Integer getKeyLengthId() {
		return this.keyLengthId;
	}

	public void setKeyLengthId(Integer keyLengthId) {
		this.keyLengthId = keyLengthId;
	}

	public Integer getKeyLength() {
		return this.keyLength;
	}

	public void setKeyLength(Integer keyLength) {
		this.keyLength = keyLength;
	}

	public List<JqEncAlgModPadKeyLookup> getJqEncAlgModPadKeyLookups() {
		return this.jqEncAlgModPadKeyLookups;
	}

	public void setJqEncAlgModPadKeyLookups(List<JqEncAlgModPadKeyLookup> jqEncAlgModPadKeyLookups) {
		this.jqEncAlgModPadKeyLookups = jqEncAlgModPadKeyLookups;
	}

	public JqEncAlgModPadKeyLookup addJqEncAlgModPadKeyLookup(JqEncAlgModPadKeyLookup jqEncAlgModPadKeyLookup) {
		getJqEncAlgModPadKeyLookups().add(jqEncAlgModPadKeyLookup);
		jqEncAlgModPadKeyLookup.setJqEncKeyLengthLookup(this);

		return jqEncAlgModPadKeyLookup;
	}

	public JqEncAlgModPadKeyLookup removeJqEncAlgModPadKeyLookup(JqEncAlgModPadKeyLookup jqEncAlgModPadKeyLookup) {
		getJqEncAlgModPadKeyLookups().remove(jqEncAlgModPadKeyLookup);
		jqEncAlgModPadKeyLookup.setJqEncKeyLengthLookup(null);

		return jqEncAlgModPadKeyLookup;
	}

}