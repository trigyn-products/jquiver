package com.trigyn.jws.dynamicform.vo;

import java.math.BigDecimal;

public class FileUploadConfigVO {

	private String fileUploadConfigId = null;

	private String fileTypSupported = null;

	private BigDecimal maxFileSize = null;

	private Integer noOfFiles = null;

	private Integer isDeleted = null;

	private String updatedBy = null;

	public FileUploadConfigVO() {}
	
	public FileUploadConfigVO(String fileUploadConfigId, String fileTypSupported, BigDecimal maxFileSize,
			Integer noOfFiles, Integer isDeleted, String updatedBy) {
		this.fileUploadConfigId = fileUploadConfigId;
		this.fileTypSupported = fileTypSupported;
		this.maxFileSize = maxFileSize;
		this.noOfFiles = noOfFiles;
		this.isDeleted = isDeleted;
		this.updatedBy = updatedBy;
	}
	
	public String getFileUploadConfigId() {
		return fileUploadConfigId;
	}

	public void setFileUploadConfigId(String fileUploadConfigId) {
		this.fileUploadConfigId = fileUploadConfigId;
	}

	public String getFileTypSupported() {
		return fileTypSupported;
	}

	public void setFileTypSupported(String fileTypSupported) {
		this.fileTypSupported = fileTypSupported;
	}

	public BigDecimal getMaxFileSize() {
		return maxFileSize;
	}

	public void setMaxFileSize(BigDecimal maxFileSize) {
		this.maxFileSize = maxFileSize;
	}

	public Integer getNoOfFiles() {
		return noOfFiles;
	}

	public void setNoOfFiles(Integer noOfFiles) {
		this.noOfFiles = noOfFiles;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
}
