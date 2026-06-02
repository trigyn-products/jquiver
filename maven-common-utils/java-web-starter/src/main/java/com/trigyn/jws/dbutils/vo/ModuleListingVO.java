package com.trigyn.jws.dbutils.vo;

import java.io.Serializable;
import java.util.Date;

public class ModuleListingVO implements Serializable {

	private static final long serialVersionUID = 6687856221695753257L;

	    private String moduleId;
	    private String moduleUrl;
	    private String parentId;
	    private Integer sequence;
	    private Integer isInsideMenu;
	    private Integer includeLayout;
	    private Integer isHomePage;
	    private Integer targetLookupId;
	    private String targetTypeId;
	    private String headerJson;
	    private Date updatedDate;
	    private Integer moduleTypeId;
	    private String requestParamJson;
	    private Integer openInNewTab;
	    private String menuStyle;
	    private Integer isCustomUpdated;
	    private String lastUpdatedBy;

//	    private List<ModuleListingI18n> moduleListingI18ns;
//	    private List<ModuleRoleAssociation>	moduleRoleAssociations	= null;
//	    private ModuleTargetLookup moduleTargetLookup;

	    public String getModuleId() {
	        return moduleId;
	    }

	    public void setModuleId(String moduleId) {
	        this.moduleId = moduleId;
	    }

	    public String getModuleUrl() {
	        return moduleUrl;
	    }

	    public void setModuleUrl(String moduleUrl) {
	        this.moduleUrl = moduleUrl;
	    }

	    public String getParentId() {
	        return parentId;
	    }

	    public void setParentId(String parentId) {
	        this.parentId = parentId;
	    }

	    public Integer getSequence() {
	        return sequence;
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

	    public Integer getIsHomePage() {
	        return isHomePage;
	    }

	    public void setIsHomePage(Integer isHomePage) {
	        this.isHomePage = isHomePage;
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

	    public Integer getIsCustomUpdated() {
	        return isCustomUpdated;
	    }

	    public void setIsCustomUpdated(Integer isCustomUpdated) {
	        this.isCustomUpdated = isCustomUpdated;
	    }

	    public String getLastUpdatedBy() {
	        return lastUpdatedBy;
	    }

	    public void setLastUpdatedBy(String lastUpdatedBy) {
	        this.lastUpdatedBy = lastUpdatedBy;
	    }

	

	   
	}


