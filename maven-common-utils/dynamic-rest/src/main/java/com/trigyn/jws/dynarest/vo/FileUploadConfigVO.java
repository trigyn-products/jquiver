package com.trigyn.jws.dynarest.vo;

import java.math.BigDecimal;
import java.util.Objects;

public class FileUploadConfigVO {

	private String		fileBinId			= null;

	private String		fileTypSupported	= null;

	private BigDecimal	maxFileSize			= null;

	private Integer		noOfFiles			= null;

	private String		selectQueryContent	= null;

	private String		uploadQueryContent	= null;

	private String		viewQueryContent	= null;

	private String		deleteQueryContent	= null;

	private Integer		isDeleted			= null;

	private String		updatedBy			= null;

	public FileUploadConfigVO() {
	}

	public FileUploadConfigVO(String fileBinId, String fileTypSupported, BigDecimal maxFileSize, Integer noOfFiles,
			String selectQueryContent, String uploadQueryContent, String viewQueryContent, String deleteQueryContent,
			Integer isDeleted) {
		this.fileBinId			= fileBinId;
		this.fileTypSupported	= fileTypSupported;
		this.maxFileSize		= maxFileSize;
		this.noOfFiles			= noOfFiles;
		this.selectQueryContent	= selectQueryContent;
		this.uploadQueryContent	= uploadQueryContent;
		this.viewQueryContent	= viewQueryContent;
		this.deleteQueryContent	= deleteQueryContent;
		this.isDeleted			= isDeleted;
	}

	public String getFileBinId() {
		return fileBinId;
	}

	public void setFileBinId(String fileBinId) {
		this.fileBinId = fileBinId;
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

	public String getSelectQueryContent() {
		return selectQueryContent;
	}

	public void setSelectQueryContent(String selectQueryContent) {
		this.selectQueryContent = selectQueryContent;
	}

	public String getUploadQueryContent() {
		return uploadQueryContent;
	}

	public void setUploadQueryContent(String uploadQueryContent) {
		this.uploadQueryContent = uploadQueryContent;
	}

	public String getViewQueryContent() {
		return viewQueryContent;
	}

	public void setViewQueryContent(String viewQueryContent) {
		this.viewQueryContent = viewQueryContent;
	}

	public String getDeleteQueryContent() {
		return deleteQueryContent;
	}

	public void setDeleteQueryContent(String deleteQueryContent) {
		this.deleteQueryContent = deleteQueryContent;
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

	@Override
	public int hashCode() {
		return Objects.hash(deleteQueryContent, fileBinId, fileTypSupported, isDeleted, maxFileSize, noOfFiles,
				selectQueryContent, updatedBy, uploadQueryContent, viewQueryContent);
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
		FileUploadConfigVO other = (FileUploadConfigVO) obj;
		return Objects.equals(deleteQueryContent, other.deleteQueryContent)
				&& Objects.equals(fileBinId, other.fileBinId)
				&& Objects.equals(fileTypSupported, other.fileTypSupported)
				&& Objects.equals(isDeleted, other.isDeleted) && Objects.equals(maxFileSize, other.maxFileSize)
				&& Objects.equals(noOfFiles, other.noOfFiles)
				&& Objects.equals(selectQueryContent, other.selectQueryContent)
				&& Objects.equals(updatedBy, other.updatedBy)
				&& Objects.equals(uploadQueryContent, other.uploadQueryContent)
				&& Objects.equals(viewQueryContent, other.viewQueryContent);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FileUploadConfigVO [fileBinId=").append(fileBinId).append(", fileTypSupported=")
				.append(fileTypSupported).append(", maxFileSize=").append(maxFileSize).append(", noOfFiles=")
				.append(noOfFiles).append(", selectQueryContent=").append(selectQueryContent)
				.append(", uploadQueryContent=").append(uploadQueryContent).append(", viewQueryContent=")
				.append(viewQueryContent).append(", deleteQueryContent=").append(deleteQueryContent)
				.append(", isDeleted=").append(isDeleted).append(", updatedBy=").append(updatedBy).append("]");
		return builder.toString();
	}

}
