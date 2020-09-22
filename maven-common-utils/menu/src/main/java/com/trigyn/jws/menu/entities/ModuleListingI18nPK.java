package com.trigyn.jws.menu.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.*;

/**
 * The primary key class for the module_listing_i18n database table.
 * 
 */
@Embeddable
public class ModuleListingI18nPK implements Serializable {
	private static final long serialVersionUID 		= 1L;

	@Column(name="module_id", insertable=false, updatable=false)
	private String moduleId						= null;

	@Column(name="language_id", insertable=false, updatable=false)
	private Integer languageId						= null;

	public ModuleListingI18nPK() {
	}
	
	public ModuleListingI18nPK(String moduleId, Integer languageId) {
		this.moduleId 		= moduleId;
		this.languageId 	= languageId;
	}

	public String getModuleId() {
		return this.moduleId;
	}
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}
	public Integer getLanguageId() {
		return this.languageId;
	}
	public void setLanguageId(Integer languageId) {
		this.languageId = languageId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(languageId, moduleId);
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
		ModuleListingI18nPK other = (ModuleListingI18nPK) obj;
		return Objects.equals(languageId, other.languageId) && Objects.equals(moduleId, other.moduleId);
	}

	@Override
	public String toString() {
		return "ModuleListingI18nPK [moduleId=" + moduleId + ", languageId=" + languageId + "]";
	}

	
}