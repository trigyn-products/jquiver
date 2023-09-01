package com.trigyn.jws.resourcebundle.entities;

public class ResourceBundlePQGrid implements Comparable<ResourceBundlePQGrid>{
	
	private String	            resourceKey					= null;

	private String				resourceBundleText				= null;

	private String			languageName			= null;
	
	private Integer            languageId=null;
	
	private String				createdBy			= null;
	
	private String				createdDate				= null;

	private String				updatedBy			= null;

	private String				updatedDate			= null;
	
	private Integer              revisionCount      = null;
	private String	             max_version_id     = null;
	
	private String	            resource_type  =null;

	public String getResourceKey() {
		return resourceKey;
	}

	public void setResourceKey(String resourceKey) {
		this.resourceKey = resourceKey;
	}

	public String getResourceBundleText() {
		return resourceBundleText;
	}

	public void setResourceBundleText(String resourceBundleText) {
		this.resourceBundleText = resourceBundleText;
	}

	public String getLanguageName() {
		return languageName;
	}

	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}

	public Integer getLanguageId() {
		return languageId;
	}

	public void setLanguageId(Integer languageId) {
		this.languageId = languageId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getRevisionCount() {
		return revisionCount;
	}

	public void setRevisionCount(Integer revisionCount) {
		this.revisionCount = revisionCount;
	}

	public String getMax_version_id() {
		return max_version_id;
	}

	public void setMax_version_id(String max_version_id) {
		this.max_version_id = max_version_id;
	}

	public String getResource_type() {
		return resource_type;
	}

	public void setResource_type(String resource_type) {
		this.resource_type = resource_type;
	}

	@Override
	public int compareTo(ResourceBundlePQGrid o) {
		return this.languageId - o.getLanguageId();
	}
	

	@Override
	public String toString() {
		return "Data{"+this.languageId+"}";
	}
	
	
}



