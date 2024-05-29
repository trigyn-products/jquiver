package com.trigyn.jws.dynarest.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the jq_enc_alg_mod_pad_key_lookup database table.
 * 
 */
@Entity
@Table(name="jq_enc_alg_mod_pad_key_lookup")
@NamedQuery(name="JqEncAlgModPadKeyLookup.findAll", query="SELECT j FROM JqEncAlgModPadKeyLookup j")
public class JqEncAlgModPadKeyLookup implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="enc_lookup_id")
	private int encLookupId;

	//bi-directional many-to-one association to JqApiClientDetails
	@OneToMany(mappedBy="jqEncAlgModPadKeyLookup")
	private List<JqApiClientDetails> jqApiClientDetails;

	//bi-directional many-to-one association to JqEncryptionAlgorithmsLookup
	@ManyToOne
	@JoinColumn(name="enc_algorithm_id")
	private JqEncryptionAlgorithmsLookup jqEncryptionAlgorithmsLookup;

	//bi-directional many-to-one association to JqEncKeyLengthLookup
	@ManyToOne
	@JoinColumn(name="enc_key_length")
	private JqEncKeyLengthLookup jqEncKeyLengthLookup;

	//bi-directional many-to-one association to JqEncModeLookup
	@ManyToOne
	@JoinColumn(name="enc_mode_id")
	private JqEncModeLookup jqEncModeLookup;

	//bi-directional many-to-one association to JqEncPaddLookup
	@ManyToOne
	@JoinColumn(name="enc_padding_id")
	private JqEncPaddLookup jqEncPaddLookup;

	public JqEncAlgModPadKeyLookup() {
	}

	public int getEncLookupId() {
		return this.encLookupId;
	}

	public void setEncLookupId(int encLookupId) {
		this.encLookupId = encLookupId;
	}

	public List<JqApiClientDetails> getJqApiClientDetails() {
		return this.jqApiClientDetails;
	}

	public void setJqApiClientDetails(List<JqApiClientDetails> jqApiClientDetails) {
		this.jqApiClientDetails = jqApiClientDetails;
	}

	public JqApiClientDetails addJqApiClientDetail(JqApiClientDetails jqApiClientDetail) {
		getJqApiClientDetails().add(jqApiClientDetail);
		jqApiClientDetail.setJqEncAlgModPadKeyLookup(this);

		return jqApiClientDetail;
	}

	public JqApiClientDetails removeJqApiClientDetail(JqApiClientDetails jqApiClientDetail) {
		getJqApiClientDetails().remove(jqApiClientDetail);
		jqApiClientDetail.setJqEncAlgModPadKeyLookup(null);

		return jqApiClientDetail;
	}

	public JqEncryptionAlgorithmsLookup getJqEncryptionAlgorithmsLookup() {
		return this.jqEncryptionAlgorithmsLookup;
	}

	public void setJqEncryptionAlgorithmsLookup(JqEncryptionAlgorithmsLookup jqEncryptionAlgorithmsLookup) {
		this.jqEncryptionAlgorithmsLookup = jqEncryptionAlgorithmsLookup;
	}

	public JqEncKeyLengthLookup getJqEncKeyLengthLookup() {
		return this.jqEncKeyLengthLookup;
	}

	public void setJqEncKeyLengthLookup(JqEncKeyLengthLookup jqEncKeyLengthLookup) {
		this.jqEncKeyLengthLookup = jqEncKeyLengthLookup;
	}

	public JqEncModeLookup getJqEncModeLookup() {
		return this.jqEncModeLookup;
	}

	public void setJqEncModeLookup(JqEncModeLookup jqEncModeLookup) {
		this.jqEncModeLookup = jqEncModeLookup;
	}

	public JqEncPaddLookup getJqEncPaddLookup() {
		return this.jqEncPaddLookup;
	}

	public void setJqEncPaddLookup(JqEncPaddLookup jqEncPaddLookup) {
		this.jqEncPaddLookup = jqEncPaddLookup;
	}

}