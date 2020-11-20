package com.trigyn.jws.dbutils.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;



@Entity
@Table(name="module_listing")
@NamedQuery(name="ModuleListing.findAll", query="SELECT m FROM ModuleListing m")
public class ModuleListing implements Serializable {
	private static final long serialVersionUID 		= 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name="module_id")
	private String moduleId							= null;

	@Column(name="module_url")
	private String moduleUrl						= null;

	@Column(name="parent_id")
	private String parentId							= null;

	@Column(name="sequence")
	private Integer sequence						= null;
	
	@Column(name="is_inside_menu")
	private Integer isInsideMenu					= null;
	
	@Column(name="target_lookup_id")
	private Integer targetLookupId					= null;
	
	@Column(name="target_type_id")
	private String targetTypeId						= null;

	@OneToMany(mappedBy="moduleListing")
	private List<ModuleListingI18n> moduleListingI18ns					= null;

	@OneToMany(mappedBy="moduleListing")
	private List<ModuleRoleAssociation> moduleRoleAssociations			= null;
	
	@ManyToOne
	@JoinColumn(name="target_lookup_id", insertable = false, updatable = false)
	private ModuleTargetLookup moduleTargetLookup						= null;

	public ModuleListing() {
	}



	public ModuleListing(String moduleId, String moduleUrl, String parentId,
			Integer sequence, Integer isInsideMenu, String targetTypeId, List<ModuleListingI18n> moduleListingI18ns,
			List<ModuleRoleAssociation> moduleRoleAssociations, ModuleTargetLookup moduleTargetLookup) {
		this.moduleId 					= moduleId;
		this.moduleUrl 					= moduleUrl;
		this.parentId 					= parentId;
		this.sequence 					= sequence;
		this.isInsideMenu 				= isInsideMenu;
		this.targetTypeId 				= targetTypeId;
		this.moduleListingI18ns 		= moduleListingI18ns;
		this.moduleRoleAssociations 	= moduleRoleAssociations;
		this.moduleTargetLookup 		= moduleTargetLookup;
	}



	public ModuleListing(String moduleId, String moduleUrl, String parentId,
			Integer sequence, Integer isInsideMenu, Integer targetLookupId, String targetTypeId, List<ModuleListingI18n> moduleListingI18ns,
			List<ModuleRoleAssociation> moduleRoleAssociations, ModuleTargetLookup moduleTargetLookup) {
		this.moduleId 					= moduleId;
		this.moduleUrl 					= moduleUrl;
		this.parentId 					= parentId;
		this.sequence 					= sequence;
		this.isInsideMenu 				= isInsideMenu;
		this.targetLookupId 			= targetLookupId;
		this.targetTypeId 				= targetTypeId;
		this.moduleListingI18ns 		= moduleListingI18ns;
		this.moduleRoleAssociations 	= moduleRoleAssociations;
		this.moduleTargetLookup 		= moduleTargetLookup;
	}



	public String getModuleId() {
		return this.moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public String getModuleUrl() {
		return this.moduleUrl;
	}

	public void setModuleUrl(String moduleUrl) {
		this.moduleUrl = moduleUrl;
	}

	public String getParentId() {
		return this.parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Integer getSequence() {
		return this.sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Integer getIsInsideMenu() {
		return isInsideMenu;
	}

	public void setIsInsideMenu(Integer isInsideMenu) {
		this.isInsideMenu = isInsideMenu;
	}

	public Integer getTargetLookupId() {
		return targetLookupId;
	}

	public void setTargetLookupId(Integer targetLookupId) {
		this.targetLookupId = targetLookupId;
	}

	public String getTargetTypeId() {
		return targetTypeId;
	}

	public void setTargetTypeId(String targetTypeId) {
		this.targetTypeId = targetTypeId;
	}

	public List<ModuleListingI18n> getModuleListingI18ns() {
		return this.moduleListingI18ns;
	}

	public void setModuleListingI18ns(List<ModuleListingI18n> moduleListingI18ns) {
		this.moduleListingI18ns = moduleListingI18ns;
	}

	public ModuleListingI18n addModuleListingI18n(ModuleListingI18n moduleListingI18n) {
		getModuleListingI18ns().add(moduleListingI18n);
		moduleListingI18n.setModuleListing(this);

		return moduleListingI18n;
	}

	public ModuleListingI18n removeModuleListingI18n(ModuleListingI18n moduleListingI18n) {
		getModuleListingI18ns().remove(moduleListingI18n);
		moduleListingI18n.setModuleListing(null);

		return moduleListingI18n;
	}

	public List<ModuleRoleAssociation> getModuleRoleAssociations() {
		return this.moduleRoleAssociations;
	}

	public void setModuleRoleAssociations(List<ModuleRoleAssociation> moduleRoleAssociations) {
		this.moduleRoleAssociations = moduleRoleAssociations;
	}

	public ModuleRoleAssociation addModuleRoleAssociation(ModuleRoleAssociation moduleRoleAssociation) {
		getModuleRoleAssociations().add(moduleRoleAssociation);
		moduleRoleAssociation.setModuleListing(this);

		return moduleRoleAssociation;
	}

	public ModuleRoleAssociation removeModuleRoleAssociation(ModuleRoleAssociation moduleRoleAssociation) {
		getModuleRoleAssociations().remove(moduleRoleAssociation);
		moduleRoleAssociation.setModuleListing(null);

		return moduleRoleAssociation;
	}

	public ModuleTargetLookup getModuleTargetLookup() {
		return this.moduleTargetLookup;
	}

	public void setModuleTargetLookup(ModuleTargetLookup moduleTargetLookup) {
		this.moduleTargetLookup = moduleTargetLookup;
	}



	@Override
	public int hashCode() {
		return Objects.hash(isInsideMenu, moduleId, moduleListingI18ns, moduleRoleAssociations, moduleTargetLookup, moduleUrl,
				parentId, sequence, targetLookupId, targetTypeId);
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
		ModuleListing other = (ModuleListing) obj;
		return Objects.equals(isInsideMenu, other.isInsideMenu) && Objects.equals(moduleId, other.moduleId)
				&& Objects.equals(moduleListingI18ns, other.moduleListingI18ns)
				&& Objects.equals(moduleRoleAssociations, other.moduleRoleAssociations)
				&& Objects.equals(moduleTargetLookup, other.moduleTargetLookup)
				&& Objects.equals(moduleUrl, other.moduleUrl) && Objects.equals(parentId, other.parentId)
				&& Objects.equals(sequence, other.sequence) && Objects.equals(targetLookupId, other.targetLookupId)
				&& Objects.equals(targetTypeId, other.targetTypeId);
	}



	@Override
	public String toString() {
		return "ModuleListing [moduleId=" + moduleId + ", moduleUrl=" + moduleUrl + ", parentId=" + parentId
				+ ", sequence=" + sequence + ", isInsideMenu=" + isInsideMenu + ", targetLookupId=" + targetLookupId
				+ ", targetTypeId=" + targetTypeId + ", moduleListingI18ns=" + moduleListingI18ns
				+ ", moduleRoleAssociations=" + moduleRoleAssociations + ", moduleTargetLookup=" + moduleTargetLookup
				+ "]";
	}

	public ModuleListing getObject() {
		ModuleListing moduleListing = new ModuleListing();
		moduleListing.setIsInsideMenu(isInsideMenu);
		moduleListing.setModuleId(moduleId!=null?moduleId.trim():moduleId);
		moduleListing.setModuleUrl(moduleUrl!=null?moduleUrl.trim():moduleUrl);
		moduleListing.setParentId(parentId!=null?parentId.trim():parentId);
		moduleListing.setSequence(sequence);
		moduleListing.setTargetLookupId(targetLookupId);
		moduleListing.setTargetTypeId(targetTypeId!=null?targetTypeId.trim():targetTypeId);
		
		List<ModuleListingI18n> moduleListingI18nsOtr = new ArrayList<>();
		if(moduleListingI18ns != null && !moduleListingI18ns.isEmpty()) {
			for(ModuleListingI18n otr : moduleListingI18ns) {
				moduleListingI18nsOtr.add(otr.getObject());
			}
			moduleListing.setModuleListingI18ns(moduleListingI18nsOtr);
		} else moduleListing.setModuleListingI18ns(null);
		

		List<ModuleRoleAssociation> moduleRoleAssociationsOtr = new ArrayList<>();
		if(moduleRoleAssociations != null && !moduleRoleAssociations.isEmpty()) {
			for(ModuleRoleAssociation otr : moduleRoleAssociations) {
				moduleRoleAssociationsOtr.add(otr.getObject());
			}
			 moduleListing.setModuleRoleAssociations(moduleRoleAssociationsOtr);
		} else moduleListing.setModuleRoleAssociations(null);
		
		moduleListing.setModuleTargetLookup(moduleTargetLookup.getObject());
		return moduleListing;
	}

}