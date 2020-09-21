package app.trigyn.common.resourcebundle.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "language")
public class Language implements Serializable {

    private static final long serialVersionUID 		= 4495981831880458994L;

 
    @Id
    @Column(name = "language_id", nullable = false)
    private Integer      languageId             	= null;

    @Column(name = "language_name")
    private String      languageName            	= null;

    @Column(name = "language_code")
    private String      languageCode            	= null;

    @Column(name = "last_update_ts")
    private Date      	lastUpdateTs           		= null;

    @Column(name = "is_deleted")
    private Integer    	isDeleted           		= null;
    
	@OneToMany(mappedBy="language")
	private List<ResourceBundle> resourceBundles	= null;
    

	/**
	 * 
	 */
	public Language() {
        
    }
    
    /**
     * @param languageId
     * @param languageName
     * @param languageCode
     * @param lastUpdateTs
     */
    public Language(Integer languageId, String languageName, String languageCode, Date lastUpdateTs) {
        this.languageId     = languageId;
        this.languageName   = languageName;
        this.languageCode   = languageCode;
        this.lastUpdateTs   = lastUpdateTs;
    }

    
    public Language(Integer languageId, String languageName, String languageCode, Date lastUpdateTs, Integer isDeleted) {
		this.languageId 	= languageId;
		this.languageName 	= languageName;
		this.languageCode 	= languageCode;
		this.lastUpdateTs 	= lastUpdateTs;
		this.isDeleted 		= isDeleted;
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

    /**
     * @return the languageName
     */
    public String getLanguageName() {
        return languageName;
    }

    /**
     * @param languageName the languageName to set
     */
    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    /**
     * @return the languageCode
     */
    public String getLanguageCode() {
        return languageCode;
    }

    /**
     * @param languageCode the languageCode to set
     */
    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    /**
     * @return the lastUpdateTs
     */
    public Date getLastUpdateTs() {
        return lastUpdateTs;
    }

    /**
     * @param lastUpdateTs the lastUpdateTs to set
     */
    public void setLastUpdateTs(Date lastUpdateTs) {
        this.lastUpdateTs = lastUpdateTs;
    }

	/**
	 * @return
	 */
	public Integer getIsDeleted() {
		return isDeleted;
	}

	/**
	 * @param isDeleted
	 */
	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}
	
    
	/**
	 * @return the resourceBundles
	 */
	public List<ResourceBundle> getResourceBundles() {
		return resourceBundles;
	}

	/**
	 * @param resourceBundles the resourceBundles to set
	 */
	public void setResourceBundles(List<ResourceBundle> resourceBundles) {
		this.resourceBundles = resourceBundles;
	}

	@Override
	public int hashCode() {
		return Objects.hash(isDeleted, languageCode, languageId, languageName, lastUpdateTs);
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
		Language other = (Language) obj;
		return Objects.equals(isDeleted, other.isDeleted) && Objects.equals(languageCode, other.languageCode)
				&& Objects.equals(languageId, other.languageId) && Objects.equals(languageName, other.languageName)
				&& Objects.equals(lastUpdateTs, other.lastUpdateTs);
	}

	
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder().append("{ languageId = ").append(languageId)
		.append(", languageCode = ").append(languageCode)
        .append(", languageName = ").append(languageName)
        .append(", lastUpdateTs = ").append(lastUpdateTs)
        .append(", isDeleted = ").append(isDeleted)
        .append(", resourceBundles = ").append(resourceBundles)
		.append(" }");
		return stringBuilder.toString();
    }


}