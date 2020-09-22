package com.trigyn.jws.menu.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;



@Entity
@Table(name="module_target_lookup")
@NamedQuery(name="ModuleTargetLookup.findAll", query="SELECT m FROM ModuleTargetLookup m")
public class ModuleTargetLookup implements Serializable {
	private static final long serialVersionUID 		= 1L;

	@Id
	@Column(name="lookup_id")
	private Integer lookupId						= null;

	@Column(name="description")
	private String description						= null;

	@OneToMany(mappedBy="moduleTargetLookup")
	private List<ModuleListing> moduleListings		= null;

	public ModuleTargetLookup() {
	}

	public Integer getLookupId() {
		return this.lookupId;
	}

	public void setLookupId(Integer lookupId) {
		this.lookupId = lookupId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<ModuleListing> getModuleListings() {
		return this.moduleListings;
	}

	public void setModuleListings(List<ModuleListing> moduleListings) {
		this.moduleListings = moduleListings;
	}

	public ModuleListing addModuleListing(ModuleListing moduleListing) {
		getModuleListings().add(moduleListing);
		moduleListing.setModuleTargetLookup(this);

		return moduleListing;
	}

	public ModuleListing removeModuleListing(ModuleListing moduleListing) {
		getModuleListings().remove(moduleListing);
		moduleListing.setModuleTargetLookup(null);

		return moduleListing;
	}

	@Override
	public int hashCode() {
		return Objects.hash(description, lookupId, moduleListings);
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
		ModuleTargetLookup other = (ModuleTargetLookup) obj;
		return Objects.equals(description, other.description) && Objects.equals(lookupId, other.lookupId)
				&& Objects.equals(moduleListings, other.moduleListings);
	}

	@Override
	public String toString() {
		return "ModuleTargetLookup [lookupId=" + lookupId + ", description=" + description + ", moduleListings="
				+ moduleListings + "]";
	}

}