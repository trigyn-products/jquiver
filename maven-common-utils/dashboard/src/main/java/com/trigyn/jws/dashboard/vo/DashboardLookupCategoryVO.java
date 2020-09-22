package com.trigyn.jws.dashboard.vo;

import java.io.Serializable;
import java.util.Objects;

public class DashboardLookupCategoryVO implements Serializable{

	private static final long 	serialVersionUID 	= 372515073999791536L;
	
	private String				lookupCategoryId	= null;
	
	private String				lookupCategory		= null;
	
	private String				lookupDescription	= null;

	public DashboardLookupCategoryVO() {
		
	}
	
	public DashboardLookupCategoryVO(String lookupCategoryId, String lookupCategory, String lookupDescription) {
		this.lookupCategoryId 	= lookupCategoryId;
		this.lookupCategory 	= lookupCategory;
		this.lookupDescription 	= lookupDescription;
	}

	public DashboardLookupCategoryVO(String lookupCategoryId, String lookupDescription) {
		this.lookupCategoryId 	= lookupCategoryId;
		this.lookupDescription 	= lookupDescription;
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

	@Override
	public int hashCode() {
		return Objects.hash(lookupCategory, lookupCategoryId, lookupDescription);
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
		DashboardLookupCategoryVO other = (DashboardLookupCategoryVO) obj;
		return Objects.equals(lookupCategory, other.lookupCategory) && Objects.equals(lookupCategoryId, other.lookupCategoryId) && Objects.equals(lookupDescription, other.lookupDescription);
	}

	@Override
	public String toString() {
		return "DashboardLookupCategoryVO [lookupCategoryId=" + lookupCategoryId + ", lookupCategory=" + lookupCategory + ", lookupDescription=" + lookupDescription + "]";
	}


}
