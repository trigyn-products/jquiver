package com.trigyn.jws.dbutils.vo.xml;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class HelpManualTypeExportVO {

	private String	manualId		= null;

	private String	name			= null;

	private Integer	isSystemManual	= null;
	
	private String createdBy 		= null;
	
	@JsonIgnore
	private Date createdDate 		= null;
	
	private String lastUpdatedBy 	= null;
	
	@JsonIgnore
	private Date lastUpdatedTs 		= null;
	
	private String	headerTemplate	= null;
	
	private Integer	editorName	= null;
	
	private List<ManualEntryDetailsExportVO> manualEntries = new ArrayList<>();
	
	private List<FileUploadExportVO> fileUploadList = new ArrayList<>();
	
	private FileUploadConfigExportVO fileUploadConfig = null;

	public HelpManualTypeExportVO() {}
	
	public HelpManualTypeExportVO(String manualId, String name, Integer isSystemManual, String headerTemplate, Integer editorName,
			List<ManualEntryDetailsExportVO> manualEntries, String createdBy, Date createdDate, String lastUpdatedBy, Date lastUpdatedTs, FileUploadConfigExportVO fileUploadConfig,List<FileUploadExportVO> fileUploadList) {
		super();
		this.manualId			= manualId;
		this.name				= name;
		this.isSystemManual		= isSystemManual;
		this.headerTemplate = headerTemplate;
		this.editorName = editorName;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdatedTs = lastUpdatedTs;
		this.manualEntries		= manualEntries;
		this.fileUploadConfig	= fileUploadConfig;
		this.fileUploadList = fileUploadList;
	}

	public String getManualId() {
		return manualId;
	}

	public void setManualId(String manualId) {
		this.manualId = manualId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getIsSystemManual() {
		return isSystemManual;
	}

	public void setIsSystemManual(Integer isSystemManual) {
		this.isSystemManual = isSystemManual;
	}

	public List<ManualEntryDetailsExportVO> getManualEntries() {
		return manualEntries;
	}

	public void setManualEntries(List<ManualEntryDetailsExportVO> manualEntries) {
		this.manualEntries = manualEntries;
	}

	public FileUploadConfigExportVO getFileUploadConfig() {
		return fileUploadConfig;
	}

	public void setFileUploadConfig(FileUploadConfigExportVO fileUploadConfig) {
		this.fileUploadConfig = fileUploadConfig;
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

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastUpdatedTs() {
		return lastUpdatedTs;
	}

	public void setLastUpdatedTs(Date lastUpdatedTs) {
		this.lastUpdatedTs = lastUpdatedTs;
	}	
	
	public List<FileUploadExportVO> getFileUploadList() {
	return fileUploadList;
}

public void setFileUploadList(List<FileUploadExportVO> fileUploadList) {
	this.fileUploadList = fileUploadList;
}

public String getHeaderTemplate() {
	return headerTemplate;
}

public void setHeaderTemplate(String headerTemplate) {
	this.headerTemplate = headerTemplate;
}

public Integer getEditorName() {
	return editorName;
}

public void setEditorName(Integer editorName) {
	this.editorName = editorName;
}
}
