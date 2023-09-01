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

	public ResourceBundleVO() {

	}

	public ResourceBundleVO(Integer languageId, String resourceKey, String text,String createdBy,Date createdDate) {
		this.languageId		= languageId;
		this.resourceKey	= resourceKey;
		this.text			= text;
		this.createdBy      =createdBy;
		this.createdDate    =createdDate;
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
				&& Objects.equals(text, other.text) && Objects.equals(createdBy, other.createdBy) && Objects.equals(createdDate, other.createdDate);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder().append("{ languageId = ").append(languageId).append(", text = ").append(text)
				.append(", resourceKey = ").append(resourceKey).append(" , createdBy = ").append(createdBy).append(", createdDate = ").append(createdDate).append(" }");
		return stringBuilder.toString();
	}

}