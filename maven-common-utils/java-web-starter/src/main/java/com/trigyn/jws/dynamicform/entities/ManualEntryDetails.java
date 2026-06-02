package com.trigyn.jws.dynamicform.entities;

import java.util.Date;
import java.util.Map;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trigyn.jws.dbutils.configurations.UUIDEntityListener;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@SuppressWarnings("deprecation")
@Entity
@EntityListeners(value = { UUIDEntityListener.class })
@Table(name = "jq_manual_entry")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "manualEntryDetails")
public class ManualEntryDetails {

	@Id
	@Column(name = "manual_entry_id")
	private String manualEntryId = null;

	@Column(name = "manual_id")
	private String manualId = null;

	@Column(name = "entry_name")
	private String entryName = null;

	@Column(name = "entry_content")
	private String entryContent = null;

	@Column(name = "sort_index")
	private Integer sortIndex = null;

	@Column(name = "created_by")
	private String createdBy = null;

	@JsonIgnore
	@Column(name = "created_date")
	private Date createdDate = null;
	
	@Column(name = "last_updated_by")
	private String lastUpdatedBy = null;

	@JsonIgnore
	@Column(name = "last_updated_ts")
	private Date lastModifiedOn = null;

	@Column(name = "is_custom_updated")
	private Integer isCustomUpdated = 1;

	@Column(name = "parent_id")
	private String parentId = null;

	public enum ActionType {
		ADD("add"), UPDATE("update"), DELETE("delete"), RENAME("rename");

		final String actionType;

		ActionType(String i) {
			actionType = i;
		}

		public String getActionType() {
			return actionType;
		}
	}

	@Transient
	private String action = "add";

	public ManualEntryDetails() {
	}

	public ManualEntryDetails(Map<String, Object> manualParameterMap, String userName) {
		super();
		this.manualEntryId = manualParameterMap.get("manualentryid") == null
				|| "".equals(manualParameterMap.get("manualentryid")) ? null
						: manualParameterMap.get("manualentryid").toString();
		this.manualId = manualParameterMap.get("mt").toString();
		this.entryName = manualParameterMap.get("entryname").toString();
		this.entryContent = manualParameterMap.get("entrycontent").toString();
		this.sortIndex = Integer.parseInt(manualParameterMap.get("sortindex").toString());
		this.lastUpdatedBy = userName;
		this.lastModifiedOn = new Date();
		this.createdBy = manualParameterMap.get("created_by").toString();
		this.createdDate = new Date();
		this.parentId = manualParameterMap.get("parentId").toString();
		;
	}

	public ManualEntryDetails(String manualEntryId, String manualId, String entryName, String entryContent,
			Integer sortIndex, String lastUpdatedBy, Date lastModifiedOn, String created_by, Date created_date,
			String parentId) {
		super();
		this.manualEntryId = manualEntryId;
		this.manualId = manualId;
		this.entryName = entryName;
		this.entryContent = entryContent;
		this.sortIndex = sortIndex;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastModifiedOn = lastModifiedOn;
		this.createdBy = created_by;
		this.createdDate = created_date;
		this.parentId = parentId;

	}

	public String getManualEntryId() {
		return manualEntryId;
	}

	public void setManualEntryId(String manualEntryId) {
		this.manualEntryId = manualEntryId;
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

	public Integer getIsCustomUpdated() {
		return isCustomUpdated;
	}

	public void setIsCustomUpdated(Integer isCustomUpdated) {
		this.isCustomUpdated = isCustomUpdated;
	}

	public String getManualId() {
		return manualId;
	}

	public void setManualId(String manualId) {
		this.manualId = manualId;
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

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@Override
	public String toString() {
		return "ManualEntryDetails [manualEntryId=" + manualEntryId + ", manualId=" + manualId + ", entryName="
				+ entryName + ", entryContent=" + entryContent + ", sortIndex=" + sortIndex + ", createdBy=" + createdBy
				+ ", createdDate=" + createdDate + ", lastUpdatedBy=" + lastUpdatedBy + ", lastModifiedOn="
				+ lastModifiedOn + ", isCustomUpdated=" + isCustomUpdated + ", parentId=" + parentId + ", action="
				+ action + "]";
	}

}
