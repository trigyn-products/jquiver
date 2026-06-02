package com.trigyn.jws.resourcebundle.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class ResourceBundleVO implements Serializable {

	private static final long	serialVersionUID	= -7443059574819653335L;
	private Integer				languageId			= null;
	private String				resourceKey			= null;
	private String				text				= null;
	private String				createdBy			= null;
	private Date				createdDate				= null;
	private String				updatedBy			= null;
	private Date				updatedDate				= null;
	private Integer isCustomUpdated = 1;

	public ResourceBundleVO() {

	}

	public ResourceBundleVO(Integer languageId, String resourceKey, String text,String createdBy,Date createdDate,String updatedBy,Date updatedDate) {
		this.languageId		= languageId;
		this.resourceKey	= resourceKey;
		this.text			= text;
		this.createdBy      =createdBy;
		this.createdDate    =createdDate;
		this.updatedBy  =updatedBy;
		this.updatedDate=updatedDate;
	}

	public Integer getLanguageId() {
		return languageId;
	}

	public void setLanguageId(Integer languageId) {
		this.languageId = languageId;
	}

	public String getResourceKey() {
		return resourceKey;
	}

	public void setResourceKey(String resourceKey) {
		this.resourceKey = resourceKey;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	
	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	
	public Integer getIsCustomUpdated() {
		return isCustomUpdated;
	}

	public void setIsCustomUpdated(Integer isCustomUpdated) {
		this.isCustomUpdated = isCustomUpdated;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(languageId, resourceKey, text,createdBy);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResourceBundleVO other = (ResourceBundleVO) obj;
		return Objects.equals(languageId, other.languageId) && Objects.equals(resourceKey, other.resourceKey)
				&& Objects.equals(text, other.text) && Objects.equals(createdBy, other.createdBy) && Objects.equals(createdDate, other.createdDate) && Objects.equals(updatedBy, other.updatedBy)  && Objects.equals(updatedBy, other.updatedBy) && Objects.equals(updatedDate, other.updatedDate) && Objects.equals(isCustomUpdated, other.isCustomUpdated);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ResourceBundleVO [languageId=" + languageId + ", resourceKey=" + resourceKey + ", text=" + text
				+ ", createdBy=" + createdBy + ", createdDate=" + createdDate + ", updatedBy=" + updatedBy
				+ ", updatedDate=" + updatedDate + ", isCustomUpdated=" + isCustomUpdated + "]";
	}

	


}