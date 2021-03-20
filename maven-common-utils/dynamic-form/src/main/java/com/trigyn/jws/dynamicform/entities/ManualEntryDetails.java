package com.trigyn.jws.dynamicform.entities;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "jq_manual_entry")
public class ManualEntryDetails {

	@Id
	@GeneratedValue(generator = "inquisitive-uuid")
	@GenericGenerator(name = "inquisitive-uuid", strategy = "com.trigyn.jws.dbutils.configurations.CustomUUIDGenerator")
	@Column(name = "manual_entry_id")
	private String	manualEntryId	= null;

	@Column(name = "manual_type")
	private String	manualType		= null;

	@Column(name = "entry_name")
	private String	entryName		= null;

	@Column(name = "entry_content")
	private String	entryContent	= null;

	@Column(name = "sort_index")
	private Integer	sortIndex		= null;

	@Column(name = "last_updated_by")
	private String	lastUpdatedBy	= null;

	@Column(name = "last_modified_on")
	private Date	lastModifiedOn	= null;

	public ManualEntryDetails() {}
	public ManualEntryDetails(Map<String, Object> manualParameterMap, String userName) {
		super();
		this.manualEntryId	= manualParameterMap.get("manualentryid") == null
				|| "".equals(manualParameterMap.get("manualentryid")) ? null
						: manualParameterMap.get("manualentryid").toString();
		this.manualType		= manualParameterMap.get("mt").toString();
		this.entryName		= manualParameterMap.get("entryname").toString();
		this.entryContent	= manualParameterMap.get("entrycontent").toString();
		this.sortIndex		= Integer.parseInt(manualParameterMap.get("sortindex").toString());
		this.lastUpdatedBy	= userName;
		this.lastModifiedOn	= new Date();
	}
	
	

	public ManualEntryDetails(String manualEntryId, String manualType, String entryName, String entryContent,
			Integer sortIndex, String lastUpdatedBy, Date lastModifiedOn) {
		super();
		this.manualEntryId	= manualEntryId;
		this.manualType		= manualType;
		this.entryName		= entryName;
		this.entryContent	= entryContent;
		this.sortIndex		= sortIndex;
		this.lastUpdatedBy	= lastUpdatedBy;
		this.lastModifiedOn	= lastModifiedOn;
	}
	public String getManualEntryId() {
		return manualEntryId;
	}

	public void setManualEntryId(String manualEntryId) {
		this.manualEntryId = manualEntryId;
	}

	public String getManualType() {
		return manualType;
	}

	public void setManualType(String manualType) {
		this.manualType = manualType;
	}

	public String getEntryName() {
		return entryName;
	}

	public void setEntryName(String entryName) {
		this.entryName = entryName;
	}

	public String getEntryContent() {
		return entryContent;
	}

	public void setEntryContent(String entryContent) {
		this.entryContent = entryContent;
	}

	public Integer getSortIndex() {
		return sortIndex;
	}

	public void setSortIndex(Integer sortIndex) {
		this.sortIndex = sortIndex;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastModifiedOn() {
		return lastModifiedOn;
	}

	public void setLastModifiedOn(Date lastModifiedOn) {
		this.lastModifiedOn = lastModifiedOn;
	}

}
