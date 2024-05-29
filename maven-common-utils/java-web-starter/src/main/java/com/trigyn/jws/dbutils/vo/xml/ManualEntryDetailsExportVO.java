package com.trigyn.jws.dbutils.vo.xml;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;

public class ManualEntryDetailsExportVO {

	private String manualEntryId = null;

	private String manualId = null;

	private String entryName = null;

	private String entryContent = null;	

	private Integer sortIndex = null;
	
	private String createdBy = null;

	private Date createdDate = null;

	private String lastUpdatedBy = null;

	private Date lastModifiedOn = null;	

	private String parentId = null;

	//private List<FileUploadExportVO> fileUploadList = new ArrayList<>();

	public ManualEntryDetailsExportVO() {
	}

	public ManualEntryDetailsExportVO(String manualEntryId, String manualId, String entryName, String entryContent,
			Integer sortIndex, String lastUpdatedBy, Date lastModifiedOn, /*List<FileUploadExportVO> fileUploadList,*/
			String createdBy, Date createdDate, String parentId) {
		super();
		this.manualEntryId = manualEntryId;
		this.manualId = manualId;
		this.entryName = entryName;
		this.entryContent = entryContent;
		this.sortIndex = sortIndex;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastModifiedOn = lastModifiedOn;
		//this.fileUploadList = fileUploadList;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.parentId = parentId;
	}

	public String getManualEntryId() {
		return manualEntryId;
	}

	public void setManualEntryId(String manualEntryId) {
		this.manualEntryId = manualEntryId;
	}

	public String getManualId() {
		return manualId;
	}

	public void setManualId(String manualId) {
		this.manualId = manualId;
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

//	public List<FileUploadExportVO> getFileUploadList() {
//		return fileUploadList;
//	}
//
//	public void setFileUploadList(List<FileUploadExportVO> fileUploadList) {
//		this.fileUploadList = fileUploadList;
//	}

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

}
