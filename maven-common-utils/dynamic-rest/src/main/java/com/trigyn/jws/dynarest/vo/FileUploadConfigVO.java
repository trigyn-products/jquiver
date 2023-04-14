package com.trigyn.jws.dynarest.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class FileUploadConfigVO {

	private String		fileBinId			= null;

	private String		fileTypSupported	= null;

	private BigDecimal	maxFileSize			= null;

	private Integer		noOfFiles			= null;

	private String		uploadQueryContent	= null;

	private String		viewQueryContent	= null;

	private String		deleteQueryContent	= null;

	private Integer		isDeleted			= null;

	private String		updatedBy			= null;
	
	private Date		updatedDate			= null;
	
	private String		createdBy			= null;

	private Date		createdDate			= null;

	private Integer		uploadQueryType		= null;
	
	private Integer		viewQueryType		= null;

	private Integer		deleteQueryType		= null;

	private String		datasourceViewValidator = null;

	private String		datasourceUploadValidator = null;

	private String		datasourceDeleteValidator = null;

	private Integer		isCustomUpdated		= null;
	
	private String		datasourceId		= null;


	public FileUploadConfigVO() {
	}

	public FileUploadConfigVO(String fileBinId, String fileTypSupported, BigDecimal maxFileSize, Integer noOfFiles,
			String uploadQueryContent, String viewQueryContent, String deleteQueryContent, Integer isDeleted) {
		this.fileBinId			= fileBinId;
		this.fileTypSupported	= fileTypSupported;
		this.maxFileSize		= maxFileSize;
		this.noOfFiles			= noOfFiles;
		this.uploadQueryContent	= uploadQueryContent;
		this.viewQueryContent	= viewQueryContent;
		this.deleteQueryContent	= deleteQueryContent;
		this.isDeleted			= isDeleted;
	}
	
	public FileUploadConfigVO(String fileBinId, String fileTypSupported, BigDecimal maxFileSize, Integer noOfFiles,
			String uploadQueryContent, String viewQueryContent, String deleteQueryContent, Integer isDeleted,
			String updatedBy, Date updatedDate, String createdBy, Date createdDate, Integer uploadQueryType,
			Integer viewQueryType, Integer deleteQueryType, String datasourceViewValidator,
			String datasourceUploadValidator, String datasourceDeleteValidator, Integer isCustomUpdated) {
		this.fileBinId = fileBinId;
		this.fileTypSupported = fileTypSupported;
		this.maxFileSize = maxFileSize;
		this.noOfFiles = noOfFiles;
		this.uploadQueryContent = uploadQueryContent;
		this.viewQueryContent = viewQueryContent;
		this.deleteQueryContent = deleteQueryContent;
		this.isDeleted = isDeleted;
		this.updatedBy = updatedBy;
		this.updatedDate = updatedDate;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.uploadQueryType = uploadQueryType;
		this.viewQueryType = viewQueryType;
		this.deleteQueryType = deleteQueryType;
		this.datasourceViewValidator = datasourceViewValidator;
		this.datasourceUploadValidator = datasourceUploadValidator;
		this.datasourceDeleteValidator = datasourceDeleteValidator;
		this.isCustomUpdated = isCustomUpdated;
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

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
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

	public Integer getUploadQueryType() {
		return uploadQueryType;
	}

	public void setUploadQueryType(Integer uploadQueryType) {
		this.uploadQueryType = uploadQueryType;
	}

	public Integer getViewQueryType() {
		return viewQueryType;
	}

	public void setViewQueryType(Integer viewQueryType) {
		this.viewQueryType = viewQueryType;
	}

	public Integer getDeleteQueryType() {
		return deleteQueryType;
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

	public String getDatasourceId() {
		return datasourceId;
	}

	public void setDatasourceId(String datasourceId) {
		this.datasourceId = datasourceId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(deleteQueryContent, fileBinId, fileTypSupported, isDeleted, maxFileSize, noOfFiles,
				updatedBy, uploadQueryContent, viewQueryContent);
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
				&& Objects.equals(noOfFiles, other.noOfFiles) && Objects.equals(updatedBy, other.updatedBy)
				&& Objects.equals(uploadQueryContent, other.uploadQueryContent)
				&& Objects.equals(viewQueryContent, other.viewQueryContent);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FileUploadConfigVO [fileBinId=").append(fileBinId).append(", fileTypSupported=")
				.append(fileTypSupported).append(", maxFileSize=").append(maxFileSize).append(", noOfFiles=")
				.append(noOfFiles).append(", selectQueryContent=").append(", uploadQueryContent=")
				.append(uploadQueryContent).append(", viewQueryContent=").append(viewQueryContent)
				.append(", deleteQueryContent=").append(deleteQueryContent).append(", isDeleted=").append(isDeleted)
				.append(", updatedBy=").append(updatedBy).append("]");
		return builder.toString();
	}

}
