package com.trigyn.jws.dynamicform.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "jws_file_upload")
public class FileUpload {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "file_upload_id")
	private String fileUploadId = null;

	@Column(name = "physical_file_name")
	private String physicalFileName = null;

	@Column(name = "original_file_name")
	private String originalFileName = null;

	@Column(name = "file_path")
	private String filePath = null;

	@Column(name = "updated_by")
	private String updatedBy = null;

	@Column(name = "last_update_ts")
	private Date lastUpdatedBy = null;
	
	@Column(name = "file_config_id")
	private String fileConfigId = "default";

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

	public Date getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(Date lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public String getFileConfigId() {
		return fileConfigId;
	}

	public void setFileConfigId(String fileConfigId) {
		this.fileConfigId = fileConfigId;
	}
	
}
