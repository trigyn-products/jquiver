package com.trigyn.jws.resourcebundle.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import org.apache.commons.text.StringEscapeUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
@Entity
@Table(name = "jq_resource_bundle")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region = "resourceBundles") // Provide cache strategy.
public class ResourceBundle implements Serializable {

	private static final long serialVersionUID = 7131861420493139106L;

	@EmbeddedId
	private ResourceBundlePK id = null;

	@Column(name = "text")
	private String text = null;

	@Column(name = "is_custom_updated")
	private Integer isCustomUpdated = 1;

	@ManyToOne
	@JoinColumn(name = "language_id", nullable = false, insertable = false, updatable = false)
	private Language language = null;

	@Column(name = "created_by")
	private String createdBy = null;

	@JsonIgnore
	@Column(name = "created_date")
	private Date createdDate = null;

	@Column(name = "updated_by")
	private String updatedBy = null;

	@JsonIgnore
	@Column(name = "updated_date")
	private Date updatedDate = null;

	public ResourceBundle() {

	}

	public ResourceBundle(ResourceBundlePK id, String text) {
		this.id = id;
		this.text = text;
	}

	public ResourceBundlePK getId() {
		return id;
	}

	public void setId(ResourceBundlePK id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public Integer getIsCustomUpdated() {
		return isCustomUpdated;
	}

	public void setIsCustomUpdated(Integer isCustomUpdated) {
		this.isCustomUpdated = isCustomUpdated;
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

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder().append("{ id = ").append(id).append(", text = ").append(text)
				.append(", language = ").append(language).append(" }");
		return stringBuilder.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, language, text);
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
		ResourceBundle other = (ResourceBundle) obj;
		return Objects.equals(id, other.id) && Objects.equals(language, other.language)
				&& Objects.equals(text, other.text);
	}

	public ResourceBundle getObject() {
		String textStr;
		if (text != null) {
			textStr = StringEscapeUtils.unescapeXml("<![CDATA[" + text.trim() + "]]>".trim());
		} else {
			textStr = StringEscapeUtils.unescapeXml("<![CDATA[" + text + "]]>".trim());
		}
		String langNameStr;
		if (language.getLanguageName() != null) {
			langNameStr = StringEscapeUtils.unescapeXml("<![CDATA[" + language.getLanguageName().trim() + "]]>".trim());
		} else {
			langNameStr = StringEscapeUtils.unescapeXml("<![CDATA[" + language.getLanguageName() + "]]>".trim());
		}
		ResourceBundlePK rpk = new ResourceBundlePK(id.getResourceKey(), id.getLanguageId());
		ResourceBundle resourceBundle = new ResourceBundle(rpk, textStr);
		Language lang = new Language(language.getLanguageId(), langNameStr, language.getLanguageCode(),
				language.getLastUpdateTs(), language.getIsDeleted());
		resourceBundle.setLanguage(lang);
		resourceBundle.setCreatedBy(createdBy != null ? createdBy.trim() : "admin@jquiver.io");
		resourceBundle.setCreatedDate(createdDate);
		resourceBundle.setUpdatedBy(updatedBy != null ? updatedBy.trim() : updatedBy);
		resourceBundle.setUpdatedDate(updatedDate);
		return resourceBundle;
	}

}