package app.trigyn.common.resourcebundle.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ResourceBundlePK implements Serializable{
	

	private static final long serialVersionUID 	= 2217428722569615565L;

	@Column(name = "resource_key", nullable = false)
    private String      resourceKey             = null;

    @Column(name = "language_id")
    private Integer     languageId              = null;

	public ResourceBundlePK() {
		
	}

	public ResourceBundlePK(String resourceKey, Integer languageId) {
		this.resourceKey 	= resourceKey;
		this.languageId 	= languageId;
	}

	/**
	 * @return the resourceKey
	 */
	public String getResourceKey() {
		return resourceKey;
	}

	/**
	 * @param resourceKey the resourceKey to set
	 */
	public void setResourceKey(String resourceKey) {
		this.resourceKey = resourceKey;
	}

	/**
	 * @return the languageId
	 */
	public Integer getLanguageId() {
		return languageId;
	}

	/**
	 * @param languageId the languageId to set
	 */
	public void setLanguageId(Integer languageId) {
		this.languageId = languageId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(languageId, resourceKey);
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
		ResourceBundlePK other = (ResourceBundlePK) obj;
		return Objects.equals(languageId, other.languageId) && Objects.equals(resourceKey, other.resourceKey);
	}

	@Override
	public String toString() {
		return "ResourceBundlePK [resourceKey=" + resourceKey + ", languageId=" + languageId + "]";
	}

}
