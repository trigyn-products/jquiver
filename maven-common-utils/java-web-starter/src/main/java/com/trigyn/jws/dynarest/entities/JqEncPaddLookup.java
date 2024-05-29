package com.trigyn.jws.dynarest.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the jq_enc_padd_lookup database table.
 * 
 */
@Entity
@Table(name="jq_enc_padd_lookup")
@NamedQuery(name="JqEncPaddLookup.findAll", query="SELECT j FROM JqEncPaddLookup j")
public class JqEncPaddLookup implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="padding_id")
	private int paddingId;

	@Column(name="padding_name")
	private String paddingName;

	//bi-directional many-to-one association to JqEncAlgModPadKeyLookup
	@OneToMany(mappedBy="jqEncPaddLookup")
	private List<JqEncAlgModPadKeyLookup> jqEncAlgModPadKeyLookups;

	public JqEncPaddLookup() {
	}

	public int getPaddingId() {
		return this.paddingId;
	}

	public void setPaddingId(int paddingId) {
		this.paddingId = paddingId;
	}

	public String getPaddingName() {
		return this.paddingName;
	}

	public void setPaddingName(String paddingName) {
		this.paddingName = paddingName;
	}

	public List<JqEncAlgModPadKeyLookup> getJqEncAlgModPadKeyLookups() {
		return this.jqEncAlgModPadKeyLookups;
	}

	public void setJqEncAlgModPadKeyLookups(List<JqEncAlgModPadKeyLookup> jqEncAlgModPadKeyLookups) {
		this.jqEncAlgModPadKeyLookups = jqEncAlgModPadKeyLookups;
	}

	public JqEncAlgModPadKeyLookup addJqEncAlgModPadKeyLookup(JqEncAlgModPadKeyLookup jqEncAlgModPadKeyLookup) {
		getJqEncAlgModPadKeyLookups().add(jqEncAlgModPadKeyLookup);
		jqEncAlgModPadKeyLookup.setJqEncPaddLookup(this);

		return jqEncAlgModPadKeyLookup;
	}

	public JqEncAlgModPadKeyLookup removeJqEncAlgModPadKeyLookup(JqEncAlgModPadKeyLookup jqEncAlgModPadKeyLookup) {
		getJqEncAlgModPadKeyLookups().remove(jqEncAlgModPadKeyLookup);
		jqEncAlgModPadKeyLookup.setJqEncPaddLookup(null);

		return jqEncAlgModPadKeyLookup;
	}

}