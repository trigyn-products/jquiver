package com.trigyn.jws.dbutils.vo.xml;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ManualEntryDetailsExportVO {

	private String	manualEntryId	= null;

	private String	manualType		= null;

	private String	entryName		= null;

	private String	entryContent	= null;

	private Integer	sortIndex		= null;

	private String	lastUpdatedBy	= null;

	private Date	lastModifiedOn	= null;
	
	private List<FileUploadExportVO> fileUploadList = new ArrayList<>();

	public ManualEntryDetailsExportVO() {}
	
	public ManualEntryDetailsExportVO(String manualEntryId, String manualType, String entryName, String entryContent,
			Integer sortIndex, String lastUpdatedBy, Date lastModifiedOn, List<FileUploadExportVO> fileUploadList) {
		super();
		this.manualEntryId	= manualEntryId;
		this.manualType		= manualType;
		this.entryName		= entryName;
		this.entryContent	= entryContent;
		this.sortIndex		= sortIndex;
		this.lastUpdatedBy	= lastUpdatedBy;
		this.lastModifiedOn	= lastModifiedOn;
		this.fileUploadList	= fileUploadList;
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

	public List<FileUploadExportVO> getFileUploadList() {
		return fileUploadList;
	}

	public void setFileUploadList(List<FileUploadExportVO> fileUploadList) {
		this.fileUploadList = fileUploadList;
	}
}
