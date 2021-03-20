package com.trigyn.jws.dynamicform.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "jq_file_upload_config")
public class FileUploadConfig {

	@Id
	@GeneratedValue(generator = "inquisitive-uuid")
	@GenericGenerator(name = "inquisitive-uuid", strategy = "com.trigyn.jws.dbutils.configurations.CustomUUIDGenerator")
	@Column(name = "file_bin_id")
	private String		fileBinId			= null;

	@Column(name = "file_type_supported")
	private String		fileTypSupported	= null;

	@Column(name = "max_file_size")
	private BigDecimal	maxFileSize			= null;

	@Column(name = "no_of_files")
	private Integer		noOfFiles			= null;

	@Column(name = "select_query_content")
	private String		selectQueryContent	= null;

	@Column(name = "upload_query_content")
	private String		uploadQueryContent	= null;

	@Column(name = "view_query_content")
	private String		viewQueryContent	= null;

	@Column(name = "delete_query_content")
	private String		deleteQueryContent	= null;

	@Column(name = "is_deleted")
	private Integer		isDeleted			= null;

	@Column(name = "updated_by")
	private String		updatedBy			= null;

	@Column(name = "updated_date")
	private Date		updatedDate			= null;

	public FileUploadConfig() {

	}

	public FileUploadConfig(String fileBinId, String fileTypSupported, BigDecimal maxFileSize, Integer noOfFiles,
			String selectQueryContent, String uploadQueryContent, String viewQueryContent, String deleteQueryContent,
			Integer isDeleted, String updatedBy, Date updatedDate) {
		this.fileBinId			= fileBinId;
		this.fileTypSupported	= fileTypSupported;
		this.maxFileSize		= maxFileSize;
		this.noOfFiles			= noOfFiles;
		this.selectQueryContent	= selectQueryContent;
		this.uploadQueryContent	= uploadQueryContent;
		this.viewQueryContent	= viewQueryContent;
		this.deleteQueryContent	= deleteQueryContent;
		this.isDeleted			= isDeleted;
		this.updatedBy			= updatedBy;
		this.updatedDate		= updatedDate;
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

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public FileUploadConfig getObject() {
		FileUploadConfig file = new FileUploadConfig();
		file.setFileTypSupported(fileTypSupported != null ? fileTypSupported.trim() : fileTypSupported);
		file.setFileBinId(fileBinId != null ? fileBinId.trim() : fileBinId);
		file.setIsDeleted(isDeleted);
		file.setUpdatedDate(updatedDate);
		file.setMaxFileSize(maxFileSize);
		file.setNoOfFiles(noOfFiles);
		file.setSelectQueryContent(selectQueryContent);
		file.setUploadQueryContent(uploadQueryContent);
		file.setViewQueryContent(viewQueryContent);
		file.setDeleteQueryContent(deleteQueryContent);
		file.setUpdatedBy(updatedBy != null ? updatedBy.trim() : updatedBy);
		return file;
	}

	@Override
	public int hashCode() {
		return Objects.hash(deleteQueryContent, fileBinId, fileTypSupported, isDeleted, updatedDate, maxFileSize,
				noOfFiles, selectQueryContent, updatedBy, uploadQueryContent, viewQueryContent);
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
		FileUploadConfig other = (FileUploadConfig) obj;
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
		builder.append("FileUploadConfig [fileBinId=").append(fileBinId).append(", fileTypSupported=")
				.append(fileTypSupported).append(", maxFileSize=").append(maxFileSize).append(", noOfFiles=")
				.append(noOfFiles).append(", selectQueryContent=").append(selectQueryContent)
				.append(", uploadQueryContent=").append(uploadQueryContent).append(", viewQueryContent=")
				.append(viewQueryContent).append(", deleteQueryContent=").append(deleteQueryContent)
				.append(", isDeleted=").append(isDeleted).append(", updatedBy=").append(updatedBy)
				.append(", lastUpdatedBy=").append(updatedDate).append("]");
		return builder.toString();
	}

}
