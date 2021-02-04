package com.trigyn.jws.dbutils.vo;

import java.io.Serializable;
import java.util.Objects;

public class LookupDetailsVO implements Serializable {

	private static final long	serialVersionUID	= 1L;
	private String				lookupId			= null;
	private String				lookupName			= null;
	private Integer				recordId			= null;
	private Integer				languageId			= null;
	private String				recordDescription	= null;

	public LookupDetailsVO() {

	}

	public LookupDetailsVO(String lookupName, Integer recordId, Integer languageId, String recordDescription) {
		this.lookupName			= lookupName;
		this.recordId			= recordId;
		this.languageId			= languageId;
		this.recordDescription	= recordDescription;
	}

	/**
	 * @return the lookupId
	 */
	public String getLookupId() {
		return lookupId;
	}

	/**
	 * @param lookupId the lookupId to set
	 */
	public void setLookupId(String lookupId) {
		this.lookupId = lookupId;
	}

	/**
	 * @return the lookupName
	 */
	public String getLookupName() {
		return lookupName;
	}

	/**
	 * @param lookupName the lookupName to set
	 */
	public void setLookupName(String lookupName) {
		this.lookupName = lookupName;
	}

	/**
	 * @return the recordId
	 */
	public Integer getRecordId() {
		return recordId;
	}

	/**
	 * @param recordId the recordId to set
	 */
	public void setRecordId(Integer recordId) {
		this.recordId = recordId;
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
	 * @return the recordDescription
	 */
	public String getRecordDescription() {
		return recordDescription;
	}

	/**
	 * @param recordDescription the recordDescription to set
	 */
	public void setRecordDescription(String recordDescription) {
		this.recordDescription = recordDescription;
	}

	@Override
	public int hashCode() {
		return Objects.hash(languageId, lookupId, lookupName, recordDescription, recordId);
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
		LookupDetailsVO other = (LookupDetailsVO) obj;
		return Objects.equals(languageId, other.languageId) && Objects.equals(lookupId, other.lookupId)
				&& Objects.equals(lookupName, other.lookupName)
				&& Objects.equals(recordDescription, other.recordDescription)
				&& Objects.equals(recordId, other.recordId);
	}

	@Override
	public String toString() {
		return "LookupDetailsVO [lookupId=" + lookupId + ", lookupName=" + lookupName + ", recordId=" + recordId
				+ ", languageId=" + languageId + ", recordDescription=" + recordDescription + "]";
	}

}
