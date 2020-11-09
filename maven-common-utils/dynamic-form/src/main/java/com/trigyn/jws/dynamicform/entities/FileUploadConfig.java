package com.trigyn.jws.dynamicform.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "file_upload_config")
public class FileUploadConfig {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "file_upload_config_id")
	private String fileUploadConfigId = null;

	@Column(name = "file_type_supported")
	private String fileTypSupported = null;

	@Column(name = "max_file_size")
	private BigDecimal maxFileSize = null;

	@Column(name = "no_of_files")
	private Integer noOfFiles = null;

	@Column(name = "is_deleted")
	private Integer isDeleted = null;

	@Column(name = "updated_by")
	private String updatedBy = null;

	@Column(name = "updated_date")
	private Date lastUpdatedBy = null;

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

	public Date getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(Date lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public FileUploadConfig getObject() {
		FileUploadConfig file = new FileUploadConfig();
		file.setFileTypSupported(fileTypSupported);
		file.setFileUploadConfigId(fileUploadConfigId);
		file.setIsDeleted(isDeleted);
		file.setLastUpdatedBy(lastUpdatedBy);
		file.setMaxFileSize(maxFileSize);
		file.setNoOfFiles(noOfFiles);
		file.setUpdatedBy(updatedBy);
		return file;
	}
}
