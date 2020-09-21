package app.trigyn.common.dashboard.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "dashboard_lookup_category")
public class DashboardLookupCategory implements Serializable{

	private static final long serialVersionUID 					= 2476117189188428307L;

	@Id
	@Column(name = "lookup_category_id", nullable = false)
	private String						lookupCategoryId		= null;
	
	@Column(name = "lookup_category", nullable = false)
	private String						lookupCategory			= null;
	
	@Column(name = "lookup_description", nullable = false)
	private String						lookupDescription		= null;
	
	@Column(name = "updated_by", nullable = false)
	private String						updatedBy				= null;
	
	@Column(name = "updated_date", nullable = false)
	private Date						updatedDate				= null;

	public DashboardLookupCategory() {
		
	}

	public DashboardLookupCategory(String lookupCategoryId, String lookupCategory, String lookupDescription
			, String updatedBy, Date updatedDate) {
		this.lookupCategoryId 	= lookupCategoryId;
		this.lookupCategory 	= lookupCategory;
		this.lookupDescription 	= lookupDescription;
		this.updatedBy			= updatedBy;
		this.updatedDate 		= updatedDate;
	}

	/**
	 * @return the lookupCategoryId
	 */
	public String getLookupCategoryId() {
		return lookupCategoryId;
	}

	/**
	 * @param lookupCategoryId the lookupCategoryId to set
	 */
	public void setLookupCategoryId(String lookupCategoryId) {
		this.lookupCategoryId = lookupCategoryId;
	}

	/**
	 * @return the lookupCategory
	 */
	public String getLookupCategory() {
		return lookupCategory;
	}

	/**
	 * @param lookupCategory the lookupCategory to set
	 */
	public void setLookupCategory(String lookupCategory) {
		this.lookupCategory = lookupCategory;
	}

	/**
	 * @return the lookupDescription
	 */
	public String getLookupDescription() {
		return lookupDescription;
	}

	/**
	 * @param lookupDescription the lookupDescription to set
	 */
	public void setLookupDescription(String lookupDescription) {
		this.lookupDescription = lookupDescription;
	}

	/**
	 * @return the updatedBy
	 */
	public String getUpdatedBy() {
		return updatedBy;
	}

	/**
	 * @param updatedBy the updatedBy to set
	 */
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	/**
	 * @return the updatedDate
	 */
	public Date getUpdatedDate() {
		return updatedDate;
	}

	/**
	 * @param updatedDate the updatedDate to set
	 */
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	@Override
	public int hashCode() {
		return Objects.hash(lookupCategory, lookupCategoryId, lookupDescription, updatedBy, updatedDate);
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
		DashboardLookupCategory other = (DashboardLookupCategory) obj;
		return Objects.equals(lookupCategory, other.lookupCategory) && Objects.equals(lookupCategoryId, other.lookupCategoryId) && Objects.equals(lookupDescription, other.lookupDescription) && Objects.equals(updatedBy, other.updatedBy)
		        && Objects.equals(updatedDate, other.updatedDate);
	}

	@Override
	public String toString() {
		return "DashletLookUpCategory [lookupCategoryId=" + lookupCategoryId + ", lookupCategory=" + lookupCategory + ", lookupDescription=" + lookupDescription + ", updatedBy=" + updatedBy + ", updatedDate=" + updatedDate + "]";
	}
	
	
	
	
}
