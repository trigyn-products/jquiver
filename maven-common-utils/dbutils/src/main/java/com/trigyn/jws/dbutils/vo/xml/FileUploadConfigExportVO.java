package com.trigyn.jws.dbutils.vo.xml;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileUploadConfigExportVO {
	private String						fileBinId			= null;

	private String						fileTypSupported	= null;

	private BigDecimal					maxFileSize			= null;

	private Integer						noOfFiles			= null;

	private String						selectQueryContent	= null;

	private String						uploadQueryContent	= null;

	private String						viewQueryContent	= null;

	private String						deleteQueryContent	= null;

	private String						dataSourceId		= null;

	private Integer						isDeleted			= null;

	private String						createdBy			= null;

	private Date						createdDate			= null;

	private String						updatedBy			= null;

	private Date						updatedDate			= null;

	private List<FileUploadExportVO>	fileUploadList		= new ArrayList<>();

	public FileUploadConfigExportVO() {
	}

	public FileUploadConfigExportVO(String fileBinId, String fileTypSupported, BigDecimal maxFileSize, Integer noOfFiles,
			String selectQueryContent, String uploadQueryContent, String viewQueryContent, String deleteQueryContent, Integer isDeleted,
			String updatedBy, Date updatedDate) {
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

	public FileUploadConfigExportVO(String fileBinId, String fileTypSupported, BigDecimal maxFileSize, Integer noOfFiles,
			String selectQueryContent, String uploadQueryContent, String viewQueryContent, String deleteQueryContent, Integer isDeleted,
			String updatedBy, Date updatedDate, List<FileUploadExportVO> fileUploadList) {
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
		this.fileUploadList		= fileUploadList;
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

	public List<FileUploadExportVO> getFileUploadList() {
		return fileUploadList;
	}

	public void setFileUploadList(List<FileUploadExportVO> fileUploadList) {
		this.fileUploadList = fileUploadList;
	}

}
