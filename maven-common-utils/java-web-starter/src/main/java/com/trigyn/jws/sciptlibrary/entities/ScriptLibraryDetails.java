package com.trigyn.jws.sciptlibrary.entities;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.trigyn.jws.webstarter.vo.GenericUserNotification;

@Entity
@Table(name = "jq_script_lib_details")
public class ScriptLibraryDetails {

	@Id
	@GeneratedValue(generator = "inquisitive-uuid")
	@GenericGenerator(name = "inquisitive-uuid", strategy = "com.trigyn.jws.dbutils.configurations.CustomUUIDGenerator")
	@Column(name = "script_lib_id")
	private String						scriptLibId				= null;

	@Column(name = "template_id")
	private String						templateId			= null;

	@Column(name = "library_name")
	private String						libraryName			= null;
	
	@Column(name = "description")
	private String						description			= null;
	
	@Column(name = "script_type")
	private String						scriptType			= null;

	@Column(name = "created_by")
	private String						createdBy				= null;

	@Column(name = "updated_by")
	private String						updatedBy			= "admin@jquiver.io";

	@Column(name = "updated_date")
	private Date						updatedDate			= null;

	@Column(name = "is_custom_updated")
	private Integer						isCustomUpdated			= 1;

	public ScriptLibraryDetails() {

	}

	public ScriptLibraryDetails(String scriptLibId, String templateId, String libraryName, String description,
			String scriptType, String createdBy, String updatedBy, Date updatedDate, Integer isCustomUpdated) {
		this.scriptLibId = scriptLibId;
		this.templateId = templateId;
		this.libraryName = libraryName;
		this.description = description;
		this.scriptType = scriptType;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
		this.updatedDate = updatedDate;
		this.isCustomUpdated = isCustomUpdated;
	}


	public String getScriptLibId() {
		return scriptLibId;
	}

	public void setScriptLibId(String scriptLibId) {
		this.scriptLibId = scriptLibId;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getLibraryName() {
		return libraryName;
	}

	public void setLibraryName(String libraryName) {
		this.libraryName = libraryName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getScriptType() {
		return scriptType;
	}

	public void setScriptType(String scriptType) {
		this.scriptType = scriptType;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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

	public Integer getIsCustomUpdated() {
		return isCustomUpdated;
	}

	public void setIsCustomUpdated(Integer isCustomUpdated) {
		this.isCustomUpdated = isCustomUpdated;
	}

	public ScriptLibraryDetails getObject() {
		ScriptLibraryDetails scriptLibraryDetails = new ScriptLibraryDetails();
		scriptLibraryDetails.setCreatedBy(createdBy != null ? createdBy.trim() : createdBy);
		scriptLibraryDetails.setTemplateId(templateId != null ? templateId.trim() : templateId);
		scriptLibraryDetails.setScriptType(scriptType != null ? scriptType.trim() : scriptType);
		scriptLibraryDetails.setScriptLibId(scriptLibId != null ? scriptLibId.trim() : scriptLibId);
		scriptLibraryDetails.setLibraryName(libraryName != null ? libraryName.trim() : libraryName);
		scriptLibraryDetails.setDescription(description != null ? description.trim() : description);
		scriptLibraryDetails.setUpdatedBy(updatedBy != null ? updatedBy.trim() : updatedBy);
		scriptLibraryDetails.setUpdatedDate(updatedDate);
		scriptLibraryDetails.setIsCustomUpdated(isCustomUpdated);

		return scriptLibraryDetails;
	}
	
}
