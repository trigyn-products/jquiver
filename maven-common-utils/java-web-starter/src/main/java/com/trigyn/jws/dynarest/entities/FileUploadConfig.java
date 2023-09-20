package com.trigyn.jws.dynarest.entities;

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

	@Column(name = "upload_query_content")
	private String		uploadQueryContent	= null;

	@Column(name = "view_query_content")
	private String		viewQueryContent	= null;

	@Column(name = "delete_query_content")
	private String		deleteQueryContent	= null;

	@Column(name = "datasource_id")
	private String		datasourceId		= null;

	@Column(name = "is_deleted")
	private Integer		isDeleted			= null;

	@Column(name = "created_by")
	private String		createdBy			= null;

	@Column(name = "created_date")
	private Date		createdDate			= null;

	@Column(name = "last_updated_by")
	private String		lastUpdatedBy		= null;

	@Column(name = "last_updated_ts")
	private Date		lastUpdatedTs		= null;

	@Column(name = "upload_query_type")
	private Integer		uploadQueryType		= 1;

	@Column(name = "view_query_type")
	private Integer		viewQueryType		= 1;

	@Column(name = "delete_query_type")
	private Integer		deleteQueryType		= 1;

	@Column(name = "datasource_view_validator")
	private String		datasourceViewValidator;

	@Column(name = "datasource_upload_validator")
	private String		datasourceUploadValidator;

	@Column(name = "datasource_delete_validator")
	private String		datasourceDeleteValidator;

	@Column(name = "is_custom_updated")
	private Integer		isCustomUpdated		= 1;

	public FileUploadConfig() {

	}

	public FileUploadConfig(String fileBinId, String fileTypSupported, BigDecimal maxFileSize, Integer noOfFiles,
			String uploadQueryContent, String viewQueryContent, String deleteQueryContent, String datasourceId,
			Integer isDeleted, String createdBy, Date createdDate, String lastUpdatedBy, Date lastUpdatedTs) {
		this.fileBinId			= fileBinId;
		this.fileTypSupported	= fileTypSupported;
		this.maxFileSize		= maxFileSize;
		this.noOfFiles			= noOfFiles;
		this.uploadQueryContent	= uploadQueryContent;
		this.viewQueryContent	= viewQueryContent;
		this.deleteQueryContent	= deleteQueryContent;
		this.datasourceId		= datasourceId;
		this.isDeleted			= isDeleted;
		this.createdBy			= createdBy;
		this.createdDate		= createdDate;
		this.lastUpdatedBy		= lastUpdatedBy;
		this.lastUpdatedTs		= lastUpdatedTs;
	}


	public FileUploadConfig(String fileBinId, String fileTypSupported, BigDecimal maxFileSize, Integer noOfFiles,
			String uploadQueryContent, String viewQueryContent, String deleteQueryContent, String datasourceId,
			Integer isDeleted, String createdBy, Date createdDate, String lastUpdatedBy, Date lastUpdatedTs,
			Integer uploadQueryType, Integer viewQueryType, Integer deleteQueryType, String datasourceViewValidator,
			String datasourceUploadValidator, String datasourceDeleteValidator, Integer isCustomUpdated) {
		this.fileBinId = fileBinId;
		this.fileTypSupported = fileTypSupported;
		this.maxFileSize = maxFileSize;
		this.noOfFiles = noOfFiles;
		this.uploadQueryContent = uploadQueryContent;
		this.viewQueryContent = viewQueryContent;
		this.deleteQueryContent = deleteQueryContent;
		this.datasourceId = datasourceId;
		this.isDeleted = isDeleted;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdatedTs = lastUpdatedTs;
		this.uploadQueryType = uploadQueryType;
		this.viewQueryType = viewQueryType;
		this.deleteQueryType = deleteQueryType;
		this.datasourceViewValidator = datasourceViewValidator;
		this.datasourceUploadValidator = datasourceUploadValidator;
		this.datasourceDeleteValidator = datasourceDeleteValidator;
		this.isCustomUpdated = isCustomUpdated;
	}

	public FileUploadConfig getObject() {
		FileUploadConfig file = new FileUploadConfig();
		file.setFileTypSupported(fileTypSupported != null ? fileTypSupported.trim() : fileTypSupported);
		file.setFileBinId(fileBinId != null ? fileBinId.trim() : fileBinId);
		file.setIsDeleted(isDeleted);
		file.setLastUpdatedTs(lastUpdatedTs);
		file.setMaxFileSize(maxFileSize);
		file.setNoOfFiles(noOfFiles);
		file.setUploadQueryContent(uploadQueryContent != null ? uploadQueryContent.trim() : "");
		file.setViewQueryContent(viewQueryContent != null ? viewQueryContent.trim() : "");
		file.setDeleteQueryContent(deleteQueryContent != null ? deleteQueryContent.trim() : "");
		file.setDatasourceId(datasourceId);
		file.setCreatedBy(createdBy != null ? createdBy.trim() : createdBy);
		file.setCreatedDate(createdDate);
		return file;
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

	public String getUploadQueryContent() {
		return uploadQueryContent != null ? uploadQueryContent.trim() : "";
	}

	public void setUploadQueryContent(String uploadQueryContent) {
		this.uploadQueryContent = uploadQueryContent;
	}

	public String getViewQueryContent() {
		return viewQueryContent != null ? viewQueryContent.trim() : "";
	}

	public void setViewQueryContent(String viewQueryContent) {
		this.viewQueryContent = viewQueryContent;
	}

	public String getDeleteQueryContent() {
		return deleteQueryContent != null ? deleteQueryContent.trim() : "";
	}

	public void setDeleteQueryContent(String deleteQueryContent) {
		this.deleteQueryContent = deleteQueryContent;
	}

	public String getDatasourceId() {
		return datasourceId;
	}

	public void setDatasourceId(String datasourceId) {
		this.datasourceId = datasourceId;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
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

	public Integer getUploadQueryType() {
		return uploadQueryType != null ? uploadQueryType : 1;
	}

	public void setUploadQueryType(Integer uploadQueryType) {
		this.uploadQueryType = uploadQueryType;
	}

	public Integer getViewQueryType() {
		return viewQueryType != null ? viewQueryType : 1;
	}

	public void setViewQueryType(Integer viewQueryType) {
		this.viewQueryType = viewQueryType;
	}

	public Integer getDeleteQueryType() {
		return deleteQueryType != null ? deleteQueryType : 1;
	}

	public void setDeleteQueryType(Integer deleteQueryType) {
		this.deleteQueryType = deleteQueryType;
	}

	public String getDatasourceViewValidator() {
		return datasourceViewValidator;
	}

	public void setDatasourceViewValidator(String datasourceViewValidator) {
		this.datasourceViewValidator = datasourceViewValidator;
	}

	public String getDatasourceUploadValidator() {
		return datasourceUploadValidator;
	}

	public void setDatasourceUploadValidator(String datasourceUploadValidator) {
		this.datasourceUploadValidator = datasourceUploadValidator;
	}

	public String getDatasourceDeleteValidator() {
		return datasourceDeleteValidator;
	}

	public void setDatasourceDeleteValidator(String datasourceDeleteValidator) {
		this.datasourceDeleteValidator = datasourceDeleteValidator;
	}

	public Integer getIsCustomUpdated() {
		return isCustomUpdated;
	}

	public void setIsCustomUpdated(Integer isCustomUpdated) {
		this.isCustomUpdated = isCustomUpdated;
	}

	@Override
	public int hashCode() {
		return Objects.hash(createdBy, datasourceId, deleteQueryContent, fileBinId, fileTypSupported, isDeleted,
				maxFileSize, noOfFiles, lastUpdatedBy, uploadQueryContent, viewQueryContent);
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
		return Objects.equals(createdBy, other.createdBy) && Objects.equals(datasourceId, other.datasourceId)
				&& Objects.equals(deleteQueryContent, other.deleteQueryContent)
				&& Objects.equals(fileBinId, other.fileBinId)
				&& Objects.equals(fileTypSupported, other.fileTypSupported)
				&& Objects.equals(isDeleted, other.isDeleted) && Objects.equals(maxFileSize, other.maxFileSize)
				&& Objects.equals(noOfFiles, other.noOfFiles) && Objects.equals(lastUpdatedBy, other.lastUpdatedBy)
				&& Objects.equals(uploadQueryContent, other.uploadQueryContent)
				&& Objects.equals(viewQueryContent, other.viewQueryContent);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FileUploadConfig [fileBinId=").append(fileBinId).append(", fileTypSupported=")
				.append(fileTypSupported).append(", maxFileSize=").append(maxFileSize).append(", noOfFiles=")
				.append(noOfFiles).append(", selectQueryContent=").append(", uploadQueryContent=")
				.append(uploadQueryContent).append(", viewQueryContent=").append(viewQueryContent)
				.append(", deleteQueryContent=").append(deleteQueryContent).append(", datasourceId=")
				.append(datasourceId).append(", isDeleted=").append(isDeleted).append(", createdBy=").append(createdBy)
				.append(", createdDate=").append(createdDate).append(", lastUpdatedBy=").append(lastUpdatedBy)
				.append(", lastUpdatedTs=").append(lastUpdatedTs).append("]");
		return builder.toString();
	}

}