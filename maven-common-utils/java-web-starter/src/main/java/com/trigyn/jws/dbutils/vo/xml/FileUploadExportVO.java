package com.trigyn.jws.dbutils.vo.xml;

import java.util.Date;

public class FileUploadExportVO {

	private String	fileUploadId		= null;

	private String	physicalFileName	= null;

	private String	originalFileName	= null;

	private String	filePath			= null;

	private String	updatedBy			= null;

	private Date	lastUpdatedTs		= null;

	private String	fileBinId			= "default";

	private String	fileAssociationId	= null;

	public FileUploadExportVO() {}
	
	public FileUploadExportVO(String fileUploadId, String physicalFileName, String originalFileName, String filePath,
			String updatedBy, Date lastUpdatedTs, String fileBinId, String fileAssociationId) {
		super();
		this.fileUploadId		= fileUploadId;
		this.physicalFileName	= physicalFileName;
		this.originalFileName	= originalFileName;
		this.filePath			= filePath;
		this.updatedBy			= updatedBy;
		this.lastUpdatedTs		= lastUpdatedTs;
		this.fileBinId			= fileBinId;
		this.fileAssociationId	= fileAssociationId;
	}

	public String getFileUploadId() {
		return fileUploadId;
	}

	public void setFileUploadId(String fileUploadId) {
		this.fileUploadId = fileUploadId;
	}

	public String getPhysicalFileName() {
		return physicalFileName;
	}

	public void setPhysicalFileName(String physicalFileName) {
		this.physicalFileName = physicalFileName;
	}

	public String getOriginalFileName() {
		return originalFileName;
	}

	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getLastUpdatedTs() {
		return lastUpdatedTs;
	}

	public void setLastUpdatedTs(Date lastUpdatedTs) {
		this.lastUpdatedTs = lastUpdatedTs;
	}

	public String getFileBinId() {
		return fileBinId;
	}

	public void setFileBinId(String fileBinId) {
		this.fileBinId = fileBinId;
	}

	public String getFileAssociationId() {
		return fileAssociationId;
	}

	public void setFileAssociationId(String fileAssociationId) {
		this.fileAssociationId = fileAssociationId;
	}
	
}
