package com.trigyn.jws.dynarest.entities;

import java.util.Date;
import java.util.Objects;

import com.trigyn.jws.dbutils.configurations.UUIDEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@EntityListeners(value = { UUIDEntityListener.class })
@Table(name = "jq_file_upload_tags")
public class FileUploadTags {

	@Id
	@Column(name = "file_upload_id")
	private String	fileUploadId		= null;

	@Column(name = "physical_file_name")
	private String	physicalFileName	= null;

	@Column(name = "original_file_name")
	private String	originalFileName	= null;

	@Column(name = "file_path")
	private String	filePath			= null;

	@Column(name = "updated_by")
	private String	updatedBy			= null;

	@Column(name = "last_update_ts")
	private Date	lastUpdatedTs		= null;

	@Column(name = "file_bin_id")
	private String	fileBinId			= "default";

	@Column(name = "file_association_id")
	private String	fileAssociationId	= null;

	public FileUploadTags() {

	}

	public FileUploadTags(String fileUploadId, String physicalFileName, String originalFileName, String filePath,
			String updatedBy, String fileBinId, String fileAssociationId) {
		this.fileUploadId		= fileUploadId;
		this.physicalFileName	= physicalFileName;
		this.originalFileName	= originalFileName;
		this.filePath			= filePath;
		this.updatedBy			= updatedBy;
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

	@Override
	public int hashCode() {
		return Objects.hash(fileAssociationId, fileBinId, filePath, fileUploadId, lastUpdatedTs, originalFileName,
				physicalFileName, updatedBy);
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
		FileUploadTags other = (FileUploadTags) obj;
		return Objects.equals(fileAssociationId, other.fileAssociationId) && Objects.equals(fileBinId, other.fileBinId)
				&& Objects.equals(filePath, other.filePath) && Objects.equals(fileUploadId, other.fileUploadId)
				&& Objects.equals(lastUpdatedTs, other.lastUpdatedTs)
				&& Objects.equals(originalFileName, other.originalFileName)
				&& Objects.equals(physicalFileName, other.physicalFileName)
				&& Objects.equals(updatedBy, other.updatedBy);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FileUploadTags [fileUploadId=").append(fileUploadId).append(", physicalFileName=")
				.append(physicalFileName).append(", originalFileName=").append(originalFileName).append(", filePath=")
				.append(filePath).append(", updatedBy=").append(updatedBy).append(", lastUpdatedTs=")
				.append(lastUpdatedTs).append(", fileBinId=").append(fileBinId).append(", fileAssociationId=")
				.append(fileAssociationId).append("]");
		return builder.toString();
	}

}
