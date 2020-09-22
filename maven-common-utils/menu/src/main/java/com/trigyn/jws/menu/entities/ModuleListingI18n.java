package com.trigyn.jws.menu.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.*;


/**
 * The persistent class for the module_listing_i18n database table.
 * 
 */
@Entity
@Table(name="module_listing_i18n")
@NamedQuery(name="ModuleListingI18n.findAll", query="SELECT m FROM ModuleListingI18n m")
public class ModuleListingI18n implements Serializable {
	private static final long serialVersionUID 		= 1L;

	@EmbeddedId
	private ModuleListingI18nPK id					= null;

	@Column(name="module_name")
	private String moduleName						= null;

	@ManyToOne
	@JoinColumn(name="module_id", insertable = false, updatable = false)
	private ModuleListing moduleListing				= null;

	public ModuleListingI18n() {
	}

	public ModuleListingI18n(ModuleListingI18nPK id, String moduleName, ModuleListing moduleListing) {
		this.id 				= id;
		this.moduleName 		= moduleName;
		this.moduleListing 		= moduleListing;
	}

	public ModuleListingI18nPK getId() {
		return this.id;
	}

	public void setId(ModuleListingI18nPK id) {
		this.id = id;
	}

	public String getModuleName() {
		return this.moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public ModuleListing getModuleListing() {
		return this.moduleListing;
	}

	public void setModuleListing(ModuleListing moduleListing) {
		this.moduleListing = moduleListing;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, moduleListing, moduleName);
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
		ModuleListingI18n other = (ModuleListingI18n) obj;
		return Objects.equals(id, other.id) && Objects.equals(moduleListing, other.moduleListing)
				&& Objects.equals(moduleName, other.moduleName);
	}

	@Override
	public String toString() {
		return "ModuleListingI18n [id=" + id + ", moduleName=" + moduleName + ", moduleListing=" + moduleListing + "]";
	}

}