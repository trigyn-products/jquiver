package com.trigyn.jws.resourcebundle.vo;

import java.io.Serializable;
import java.util.Objects;

public class LanguageVO implements Serializable {

	private static final long	serialVersionUID	= -5296000338141499054L;

	private Integer				languageId			= null;
	private String				languageName		= null;
	private String				localeId			= null;

	public LanguageVO() {

	}

	public LanguageVO(Integer languageId, String languageName, String localeId) {
		this.languageId		= languageId;
		this.languageName	= languageName;
		this.localeId		= localeId;
	}

	public Integer getLanguageId() {
		return languageId;
	}

	public void setLanguageId(Integer languageId) {
		this.languageId = languageId;
	}

	public String getLanguageName() {
		return languageName;
	}

	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}

	public String getLocaleId() {
		return this.localeId;
	}

	public void setLocaleId(String localeId) {
		this.localeId = localeId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(languageId, languageName);
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
		LanguageVO other = (LanguageVO) obj;
		return Objects.equals(languageId, other.languageId) && Objects.equals(languageName, other.languageName);
	}

	@Override
	public String toString() {
		return "LanguageVO [languageId=" + languageId + ", languageName=" + languageName + "]";
	}

}
