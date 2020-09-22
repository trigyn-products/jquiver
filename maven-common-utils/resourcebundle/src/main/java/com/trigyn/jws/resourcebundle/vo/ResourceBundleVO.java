package com.trigyn.jws.resourcebundle.vo;

import java.io.Serializable;
import java.util.Objects;

public class ResourceBundleVO  implements Serializable {

	private static final long serialVersionUID 	= -7443059574819653335L;
	private Integer languageId					= null;
	private String resourceKey					= null;
	private String text							= null;

	/**
	 * 
	 */
	public ResourceBundleVO() {
		
	}

	/**
	 * @param languageId
	 * @param resourceKey
	 * @param text
	 * @param languageName
	 */
	public ResourceBundleVO(Integer languageId, String resourceKey, String text) {
		this.languageId 	= languageId;
		this.resourceKey 	= resourceKey;
		this.text 			= text;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(languageId, resourceKey, text);
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
		return Objects.equals(languageId, other.languageId) 
				&& Objects.equals(resourceKey, other.resourceKey) 
				&& Objects.equals(text, other.text);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder().append("{ languageId = ").append(languageId)
		.append(", text = ").append(text)
		.append(", resourceKey = ").append(resourceKey)
		.append(" }");
		return stringBuilder.toString();
	}

    
}