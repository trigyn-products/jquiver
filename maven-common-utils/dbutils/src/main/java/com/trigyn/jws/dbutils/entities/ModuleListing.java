
package com.trigyn.jws.dbutils.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "jq_module_listing")
@NamedQuery(name = "ModuleListing.findAll", query = "SELECT m FROM ModuleListing m")
public class ModuleListing implements Serializable {
	private static final long			serialVersionUID		= 1L;

	@Id
	@GeneratedValue(generator = "inquisitive-uuid")
	@GenericGenerator(name = "inquisitive-uuid", strategy = "com.trigyn.jws.dbutils.configurations.CustomUUIDGenerator")
	@Column(name = "module_id")
	private String						moduleId				= null;

	@Column(name = "module_url")
	private String						moduleUrl				= null;

	@Column(name = "parent_id")
	private String						parentId				= null;

	@Column(name = "sequence")
	private Integer						sequence				= null;

	@Column(name = "is_inside_menu")
	private Integer						isInsideMenu			= null;

	@Column(name = "include_layout")
	private Integer						includeLayout			= null;

	@Column(name = "is_home_page")
	private Integer						isHomePage				= null;

	@Column(name = "target_lookup_id")
	private Integer						targetLookupId			= null;

	@Column(name = "target_type_id")
	private String						targetTypeId			= null;

	@Column(name = "header_json")
	private String						headerJson				= null;

	@OneToMany(mappedBy = "moduleListing")
	private List<ModuleListingI18n>		moduleListingI18ns		= null;

	@OneToMany(mappedBy = "moduleListing")
	private List<ModuleRoleAssociation>	moduleRoleAssociations	= null;

	@ManyToOne
	@JoinColumn(name = "target_lookup_id", insertable = false, updatable = false)
	private ModuleTargetLookup			moduleTargetLookup		= null;

	@JsonIgnore
	@Column(name = "last_modified_date")
	private Date						updatedDate				= null;

	@Column(name = "module_type_id ")
	private Integer						moduleTypeId			= 1;

	@Column(name = "request_param_json")
	private String						requestParamJson		= null;

	@Column(name = "open_in_new_tab ")
	private Integer						openInNewTab			= null;

	@Column(name = "menu_style")
	private String						menuStyle				= null;//New Column Added for displaying Menu Style

	@Column(name = "is_custom_updated")
	private Integer						isCustomUpdated			= 1;

	public ModuleListing() {
	}

	public ModuleListing(String moduleId, String moduleUrl, String parentId, Integer sequence, Integer isInsideMenu,
			String targetTypeId, List<ModuleListingI18n> moduleListingI18ns,
			List<ModuleRoleAssociation> moduleRoleAssociations, ModuleTargetLookup moduleTargetLookup,
			Integer openInNewTab, String menuStyle) {
		this.moduleId				= moduleId;
		this.moduleUrl				= moduleUrl;
		this.parentId				= parentId;
		this.sequence				= sequence;
		this.isInsideMenu			= isInsideMenu;
		this.targetTypeId			= targetTypeId;
		this.moduleListingI18ns		= moduleListingI18ns;
		this.moduleRoleAssociations	= moduleRoleAssociations;
		this.moduleTargetLookup		= moduleTargetLookup;
		this.openInNewTab			= openInNewTab;
		this.menuStyle				= menuStyle;//New Column Added for displaying Menu Style
	}

	public ModuleListing(String moduleId, String moduleUrl, String parentId, Integer sequence, Integer isInsideMenu,
			Integer targetLookupId, String targetTypeId, List<ModuleListingI18n> moduleListingI18ns,
			List<ModuleRoleAssociation> moduleRoleAssociations, ModuleTargetLookup moduleTargetLookup,
			Integer openInNewTab, String menuStyle) {
		this.moduleId				= moduleId;
		this.moduleUrl				= moduleUrl;
		this.parentId				= parentId;
		this.sequence				= sequence;
		this.isInsideMenu			= isInsideMenu;
		this.targetLookupId			= targetLookupId;
		this.targetTypeId			= targetTypeId;
		this.moduleListingI18ns		= moduleListingI18ns;
		this.moduleRoleAssociations	= moduleRoleAssociations;
		this.moduleTargetLookup		= moduleTargetLookup;
		this.openInNewTab			= openInNewTab;
		this.menuStyle				= menuStyle;//New Column Added for displaying Menu Style
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

	public Integer getIncludeLayout() {
		return includeLayout;
	}

	public void setIncludeLayout(Integer includeLayout) {
		this.includeLayout = includeLayout;
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

	/**
	 * @return the isHomePage
	 */
	public Integer getIsHomePage() {
		return isHomePage;
	}

	/**
	 * @param isHomePage the isHomePage to set
	 */
	public void setIsHomePage(Integer isHomePage) {
		this.isHomePage = isHomePage;
	}

	public String getHeaderJson() {
		return headerJson;
	}

	public void setHeaderJson(String headerJson) {
		this.headerJson = headerJson;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getModuleTypeId() {
		return moduleTypeId;
	}

	public void setModuleTypeId(Integer moduleTypeId) {
		this.moduleTypeId = moduleTypeId;
	}

	public String getRequestParamJson() {
		return requestParamJson;
	}

	public void setRequestParamJson(String requestParamJson) {
		this.requestParamJson = requestParamJson;
	}

	public Integer getOpenInNewTab() {
		return openInNewTab;
	}

	public void setOpenInNewTab(Integer openInNewTab) {
		this.openInNewTab = openInNewTab;
	}

	public String getMenuStyle() {
		return menuStyle;
	}

	public void setMenuStyle(String menuStyle) {
		this.menuStyle = menuStyle;
	}

	public ModuleListing getObject() {
		ModuleListing moduleListing = new ModuleListing();
		moduleListing.setIsInsideMenu(isInsideMenu);
		moduleListing.setIncludeLayout(includeLayout);
		moduleListing.setIsHomePage(isHomePage);
		moduleListing.setModuleId(moduleId != null ? moduleId.trim() : moduleId);
		moduleListing.setModuleUrl(moduleUrl != null ? moduleUrl.trim() : moduleUrl);
		moduleListing.setParentId(parentId != null ? parentId.trim() : parentId);
		moduleListing.setSequence(sequence);
		moduleListing.setTargetLookupId(targetLookupId);
		moduleListing.setTargetTypeId(targetTypeId != null ? targetTypeId.trim() : targetTypeId);

		List<ModuleListingI18n> moduleListingI18nsOtr = new ArrayList<>();
		if (moduleListingI18ns != null && !moduleListingI18ns.isEmpty()) {
			for (ModuleListingI18n otr : moduleListingI18ns) {
				moduleListingI18nsOtr.add(otr.getObject());
			}
			moduleListing.setModuleListingI18ns(moduleListingI18nsOtr);
		} else
			moduleListing.setModuleListingI18ns(null);

		List<ModuleRoleAssociation> moduleRoleAssociationsOtr = new ArrayList<>();
		if (moduleRoleAssociations != null && !moduleRoleAssociations.isEmpty()) {
			for (ModuleRoleAssociation otr : moduleRoleAssociations) {
				moduleRoleAssociationsOtr.add(otr.getObject());
			}
			moduleListing.setModuleRoleAssociations(moduleRoleAssociationsOtr);
		} else
			moduleListing.setModuleRoleAssociations(null);

		moduleListing.setModuleTargetLookup(moduleTargetLookup.getObject());
		moduleListing.setUpdatedDate(updatedDate);
		moduleListing.setModuleTypeId(moduleTypeId);
		moduleListing.setHeaderJson(headerJson);
		moduleListing.setRequestParamJson(requestParamJson);
		moduleListing.setOpenInNewTab(openInNewTab);//Added for Open New Tab Column
		moduleListing.setMenuStyle(menuStyle); //Added for Menu Style Column
		return moduleListing;
	}

	@Override
	public int hashCode() {
		return Objects.hash(headerJson, includeLayout, isHomePage, isInsideMenu, moduleId, moduleListingI18ns,
				moduleRoleAssociations, moduleTargetLookup, moduleTypeId, moduleUrl, parentId, sequence, targetLookupId,
				targetTypeId, updatedDate, headerJson, requestParamJson);
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
		return Objects.equals(headerJson, other.headerJson) && Objects.equals(includeLayout, other.includeLayout)
				&& Objects.equals(isHomePage, other.isHomePage) && Objects.equals(isInsideMenu, other.isInsideMenu)
				&& Objects.equals(moduleId, other.moduleId)
				&& Objects.equals(moduleListingI18ns, other.moduleListingI18ns)
				&& Objects.equals(moduleRoleAssociations, other.moduleRoleAssociations)
				&& Objects.equals(moduleTargetLookup, other.moduleTargetLookup)
				&& Objects.equals(moduleTypeId, other.moduleTypeId) && Objects.equals(moduleUrl, other.moduleUrl)
				&& Objects.equals(parentId, other.parentId) && Objects.equals(sequence, other.sequence)
				&& Objects.equals(targetLookupId, other.targetLookupId)
				&& Objects.equals(targetTypeId, other.targetTypeId) && Objects.equals(updatedDate, other.updatedDate)
				&& Objects.equals(headerJson, other.headerJson)
				&& Objects.equals(requestParamJson, other.requestParamJson);
	}
	//
	// @Override
	// public String toString() {
	// StringBuilder builder = new StringBuilder();
	// builder.append("ModuleListing [moduleId=").append(moduleId).append(",
	// moduleUrl=").append(moduleUrl).append(", parentId=")
	// .append(parentId).append(", sequence=").append(sequence).append(",
	// isInsideMenu=").append(isInsideMenu)
	// .append(", includeLayout=").append(includeLayout).append(",
	// isHomePage=").append(isHomePage).append(", targetLookupId=")
	// .append(targetLookupId).append(",
	// targetTypeId=").append(targetTypeId).append(",
	// headerJson=").append(headerJson)
	// .append(", moduleListingI18ns=").append(moduleListingI18ns).append(",
	// moduleRoleAssociations=")
	// .append(moduleRoleAssociations).append(",
	// moduleTargetLookup=").append(moduleTargetLookup).append(", updatedDate=")
	// .append(updatedDate).append(", moduleTypeId=").append(moduleTypeId).append(",
	// headerJson=").append(headerJson)
	// .append(", requestParamJson=").append(requestParamJson).append("]");
	// return builder.toString();
	// }

	public Integer getIsCustomUpdated() {
		return isCustomUpdated;
	}

	public void setIsCustomUpdated(Integer isCustomUpdated) {
		this.isCustomUpdated = isCustomUpdated;
	}

}