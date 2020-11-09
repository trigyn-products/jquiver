package com.trigyn.jws.resourcebundle.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "resource_bundle")
public class ResourceBundle implements Serializable {

    private static final long serialVersionUID  = 7131861420493139106L;

    @EmbeddedId
    private ResourceBundlePK id					= null;

    @Column(name = "text")
    private String      text                    = null;

    @ManyToOne
	@JoinColumn(name="language_id", nullable=false, insertable=false, updatable=false)
	private Language language					= null;
    
    
    public ResourceBundle() {
        
    }

    
    public ResourceBundle(ResourceBundlePK id, String text) {
        this.id		= id;
        this.text	= text;
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

	@Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder().append("{ id = ").append(id)
		.append(", text = ").append(text)
		.append(", language = ").append(language)
		.append(" }");
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
		ResourceBundlePK rpk = new ResourceBundlePK(id.getResourceKey(), id.getLanguageId());
		ResourceBundle resourceBundle = new ResourceBundle(rpk, text);
		Language lang = new Language(language.getLanguageId(), language.getLanguageName(), language.getLanguageCode(), language.getLastUpdateTs(), language.getIsDeleted());
		resourceBundle.setLanguage(lang);
		return resourceBundle;
	}
  
}