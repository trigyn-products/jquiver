package com.trigyn.jws.dynarest.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the jq_encryption_algorithms_lookup database table.
 * 
 */
@Entity
@Table(name="jq_encryption_algorithms_lookup")
@NamedQuery(name="JqEncryptionAlgorithmsLookup.findAll", query="SELECT j FROM JqEncryptionAlgorithmsLookup j")
public class JqEncryptionAlgorithmsLookup implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="encryption_algo_id")
	private int encryptionAlgoId;

	@Column(name="encryption_algo_name")
	private String encryptionAlgoName;

	//bi-directional many-to-one association to JqEncAlgModPadKeyLookup
	@OneToMany(mappedBy="jqEncryptionAlgorithmsLookup")
	private List<JqEncAlgModPadKeyLookup> jqEncAlgModPadKeyLookups;

	public JqEncryptionAlgorithmsLookup() {
	}

	public int getEncryptionAlgoId() {
		return this.encryptionAlgoId;
	}

	public void setEncryptionAlgoId(int encryptionAlgoId) {
		this.encryptionAlgoId = encryptionAlgoId;
	}

	public String getEncryptionAlgoName() {
		return this.encryptionAlgoName;
	}

	public void setEncryptionAlgoName(String encryptionAlgoName) {
		this.encryptionAlgoName = encryptionAlgoName;
	}

	public List<JqEncAlgModPadKeyLookup> getJqEncAlgModPadKeyLookups() {
		return this.jqEncAlgModPadKeyLookups;
	}

	public void setJqEncAlgModPadKeyLookups(List<JqEncAlgModPadKeyLookup> jqEncAlgModPadKeyLookups) {
		this.jqEncAlgModPadKeyLookups = jqEncAlgModPadKeyLookups;
	}

	public JqEncAlgModPadKeyLookup addJqEncAlgModPadKeyLookup(JqEncAlgModPadKeyLookup jqEncAlgModPadKeyLookup) {
		getJqEncAlgModPadKeyLookups().add(jqEncAlgModPadKeyLookup);
		jqEncAlgModPadKeyLookup.setJqEncryptionAlgorithmsLookup(this);

		return jqEncAlgModPadKeyLookup;
	}

	public JqEncAlgModPadKeyLookup removeJqEncAlgModPadKeyLookup(JqEncAlgModPadKeyLookup jqEncAlgModPadKeyLookup) {
		getJqEncAlgModPadKeyLookups().remove(jqEncAlgModPadKeyLookup);
		jqEncAlgModPadKeyLookup.setJqEncryptionAlgorithmsLookup(null);

		return jqEncAlgModPadKeyLookup;
	}

}