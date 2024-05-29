package com.trigyn.jws.dynarest.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the jq_enc_mode_lookup database table.
 * 
 */
@Entity
@Table(name="jq_enc_mode_lookup")
@NamedQuery(name="JqEncModeLookup.findAll", query="SELECT j FROM JqEncModeLookup j")
public class JqEncModeLookup implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="mode_id")
	private int modeId;

	@Column(name="mode_name")
	private String modeName;

	//bi-directional many-to-one association to JqEncAlgModPadKeyLookup
	@OneToMany(mappedBy="jqEncModeLookup")
	private List<JqEncAlgModPadKeyLookup> jqEncAlgModPadKeyLookups;

	public JqEncModeLookup() {
	}

	public int getModeId() {
		return this.modeId;
	}

	public void setModeId(int modeId) {
		this.modeId = modeId;
	}

	public String getModeName() {
		return this.modeName;
	}

	public void setModeName(String modeName) {
		this.modeName = modeName;
	}

	public List<JqEncAlgModPadKeyLookup> getJqEncAlgModPadKeyLookups() {
		return this.jqEncAlgModPadKeyLookups;
	}

	public void setJqEncAlgModPadKeyLookups(List<JqEncAlgModPadKeyLookup> jqEncAlgModPadKeyLookups) {
		this.jqEncAlgModPadKeyLookups = jqEncAlgModPadKeyLookups;
	}

	public JqEncAlgModPadKeyLookup addJqEncAlgModPadKeyLookup(JqEncAlgModPadKeyLookup jqEncAlgModPadKeyLookup) {
		getJqEncAlgModPadKeyLookups().add(jqEncAlgModPadKeyLookup);
		jqEncAlgModPadKeyLookup.setJqEncModeLookup(this);

		return jqEncAlgModPadKeyLookup;
	}

	public JqEncAlgModPadKeyLookup removeJqEncAlgModPadKeyLookup(JqEncAlgModPadKeyLookup jqEncAlgModPadKeyLookup) {
		getJqEncAlgModPadKeyLookups().remove(jqEncAlgModPadKeyLookup);
		jqEncAlgModPadKeyLookup.setJqEncModeLookup(null);

		return jqEncAlgModPadKeyLookup;
	}

}