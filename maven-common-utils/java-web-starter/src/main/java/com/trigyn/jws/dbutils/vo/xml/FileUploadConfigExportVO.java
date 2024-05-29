package com.trigyn.jws.dbutils.vo.xml;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class FileUploadConfigExportVO {
	private String						fileBinId			= null;

	private String						fileTypSupported	= null;

	private BigDecimal					maxFileSize			= null;

	private Integer						noOfFiles			= null;

	private String						uploadQueryContent	= null;

	private String						viewQueryContent	= null;

	private String						deleteQueryContent	= null;

	private String						dataSourceId		= null;

	private Integer						isDeleted			= null;

	private String						createdBy			= null;

	private Date						createdDate			= null;

	private String						updatedBy			= null;

	private Date						updatedDate			= null;
	
	private Integer						uploadQueryType		= null;
	
	private Integer						viewQueryType		= null;

	private Integer						deleteQueryType		= null;

	private String						datasourceViewValidator = null;

	private String						datasourceUploadValidator = null;

	private String						datasourceDeleteValidator = null;

	private Integer						isCustomUpdated			  = null;
	
	private String				        uploadScriptLibraryId	  = null;
	
	private String						viewScriptLibraryId		  = null;
	
	private String						deleteScriptLibraryId	  = null;


	public FileUploadConfigExportVO() {
	}

	public FileUploadConfigExportVO(String fileBinId, String fileTypSupported, BigDecimal maxFileSize, Integer noOfFiles,
			String uploadQueryContent, String viewQueryContent, String deleteQueryContent, Integer isDeleted,
			String updatedBy, Date updatedDate) {
		this.fileBinId			= fileBinId;
		this.fileTypSupported	= fileTypSupported;
		this.maxFileSize		= maxFileSize;
		this.noOfFiles			= noOfFiles;
		this.uploadQueryContent	= uploadQueryContent;
		this.viewQueryContent	= viewQueryContent;
		this.deleteQueryContent	= deleteQueryContent;
		this.isDeleted			= isDeleted;
		this.updatedBy			= updatedBy;
		this.updatedDate		= updatedDate;
	}

	public FileUploadConfigExportVO(String fileBinId, String fileTypSupported, BigDecimal maxFileSize,
			Integer noOfFiles, String uploadQueryContent, String viewQueryContent, String deleteQueryContent,
			String dataSourceId, Integer isDeleted, String createdBy, Date createdDate, String updatedBy,
			Date updatedDate, Integer uploadQueryType, Integer viewQueryType, Integer deleteQueryType,
			String datasourceViewValidator, String datasourceUploadValidator, String datasourceDeleteValidator,
			Integer isCustomUpdated) {
		this.fileBinId = fileBinId;
		this.fileTypSupported = fileTypSupported;
		this.maxFileSize = maxFileSize;
		this.noOfFiles = noOfFiles;
		this.uploadQueryContent = uploadQueryContent;
		this.viewQueryContent = viewQueryContent;
		this.deleteQueryContent = deleteQueryContent;
		this.dataSourceId = dataSourceId;
		this.isDeleted = isDeleted;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.updatedBy = updatedBy;
		this.updatedDate = updatedDate;
		this.uploadQueryType = uploadQueryType;
		this.viewQueryType = viewQueryType;
		this.deleteQueryType = deleteQueryType;
		this.datasourceViewValidator = datasourceViewValidator;
		this.datasourceUploadValidator = datasourceUploadValidator;
		this.datasourceDeleteValidator = datasourceDeleteValidator;
		this.isCustomUpdated = isCustomUpdated;
	}

	public FileUploadConfigExportVO(String fileBinId, String fileTypSupported, BigDecimal maxFileSize,
			Integer noOfFiles, String uploadQueryContent, String viewQueryContent, String deleteQueryContent,
			String dataSourceId, Integer isDeleted, String createdBy, Date createdDate, String updatedBy,
			Date updatedDate, Integer uploadQueryType, Integer viewQueryType, Integer deleteQueryType,
			String datasourceViewValidator, String datasourceUploadValidator, String datasourceDeleteValidator,
			Integer isCustomUpdated, String uploadScriptLibraryId, String viewScriptLibraryId,
			String deleteScriptLibraryId) {
		this.fileBinId = fileBinId;
		this.fileTypSupported = fileTypSupported;
		this.maxFileSize = maxFileSize;
		this.noOfFiles = noOfFiles;
		this.uploadQueryContent = uploadQueryContent;
		this.viewQueryContent = viewQueryContent;
		this.deleteQueryContent = deleteQueryContent;
		this.dataSourceId = dataSourceId;
		this.isDeleted = isDeleted;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.updatedBy = updatedBy;
		this.updatedDate = updatedDate;
		this.uploadQueryType = uploadQueryType;
		this.viewQueryType = viewQueryType;
		this.deleteQueryType = deleteQueryType;
		this.datasourceViewValidator = datasourceViewValidator;
		this.datasourceUploadValidator = datasourceUploadValidator;
		this.datasourceDeleteValidator = datasourceDeleteValidator;
		this.isCustomUpdated = isCustomUpdated;
		this.uploadScriptLibraryId = uploadScriptLibraryId;
		this.viewScriptLibraryId = viewScriptLibraryId;
		this.deleteScriptLibraryId = deleteScriptLibraryId;
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

	public String getDataSourceId() {
		return dataSourceId;
	}

	public void setDataSourceId(String dataSourceId) {
		this.dataSourceId = dataSourceId;
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

	public String getUploadScriptLibraryId() {
		return uploadScriptLibraryId;
	}

	public void setUploadScriptLibraryId(String uploadScriptLibraryId) {
		this.uploadScriptLibraryId = uploadScriptLibraryId;
	}

	public String getViewScriptLibraryId() {
		return viewScriptLibraryId;
	}

	public void setViewScriptLibraryId(String viewScriptLibraryId) {
		this.viewScriptLibraryId = viewScriptLibraryId;
	}

	public String getDeleteScriptLibraryId() {
		return deleteScriptLibraryId;
	}

	public void setDeleteScriptLibraryId(String deleteScriptLibraryId) {
		this.deleteScriptLibraryId = deleteScriptLibraryId;
	}

}
