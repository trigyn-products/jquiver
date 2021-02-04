package com.trigyn.jws.dashboard.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "dashboard_lookup_category")
public class DashboardLookupCategory implements Serializable {

	private static final long		serialVersionUID	= 2476117189188428307L;

	@Id
	@Column(name = "lookup_category_id", nullable = false)
	private String					lookupCategoryId	= null;

	@Column(name = "lookup_category", nullable = false)
	private String					lookupCategory		= null;

	@Column(name = "lookup_description", nullable = false)
	private String					lookupDescription	= null;

	@Column(name = "updated_by", nullable = false)
	private String					updatedBy			= null;

	@Column(name = "updated_date", nullable = false)
	private Date					updatedDate			= null;

	@OneToMany(mappedBy = "dashboardLookupCategory", fetch = FetchType.LAZY)
	private List<DashletProperties>	dashletProperties	= new ArrayList<>();

	public DashboardLookupCategory() {

	}

	public DashboardLookupCategory(String lookupCategoryId, String lookupCategory, String lookupDescription,
			String updatedBy, Date updatedDate) {
		this.lookupCategoryId	= lookupCategoryId;
		this.lookupCategory		= lookupCategory;
		this.lookupDescription	= lookupDescription;
		this.updatedBy			= updatedBy;
		this.updatedDate		= updatedDate;
	}

	public String getLookupCategoryId() {
		return lookupCategoryId;
	}

	public void setLookupCategoryId(String lookupCategoryId) {
		this.lookupCategoryId = lookupCategoryId;
	}

	public String getLookupCategory() {
		return lookupCategory;
	}

	public void setLookupCategory(String lookupCategory) {
		this.lookupCategory = lookupCategory;
	}

	public String getLookupDescription() {
		return lookupDescription;
	}

	public void setLookupDescription(String lookupDescription) {
		this.lookupDescription = lookupDescription;
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
		return Objects.equals(lookupCategory, other.lookupCategory)
				&& Objects.equals(lookupCategoryId, other.lookupCategoryId)
				&& Objects.equals(lookupDescription, other.lookupDescription)
				&& Objects.equals(updatedBy, other.updatedBy) && Objects.equals(updatedDate, other.updatedDate);
	}

	@Override
	public String toString() {
		return "DashletLookUpCategory [lookupCategoryId=" + lookupCategoryId + ", lookupCategory=" + lookupCategory
				+ ", lookupDescription=" + lookupDescription + ", updatedBy=" + updatedBy + ", updatedDate="
				+ updatedDate + "]";
	}

}
