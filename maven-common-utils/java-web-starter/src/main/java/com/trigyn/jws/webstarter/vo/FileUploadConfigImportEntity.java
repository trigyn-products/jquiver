package com.trigyn.jws.webstarter.vo;

import java.util.List;

import com.trigyn.jws.dynamicform.entities.FileUpload;
import com.trigyn.jws.dynamicform.entities.FileUploadConfig;

public class FileUploadConfigImportEntity {

	private FileUploadConfig fileUploadConfig;
	
	private List<FileUpload> fileUpload;

	public FileUploadConfigImportEntity() {}
	
	public FileUploadConfigImportEntity(FileUploadConfig fileUploadConfig, List<FileUpload> fileUpload) {
		super();
		this.fileUploadConfig	= fileUploadConfig;
		this.fileUpload			= fileUpload;
	}

	public FileUploadConfig getFileUploadConfig() {
		return fileUploadConfig;
	}

	public void setFileUploadConfig(FileUploadConfig fileUploadConfig) {
		this.fileUploadConfig = fileUploadConfig;
	}

	public List<FileUpload> getFileUpload() {
		return fileUpload;
	}

	public void setFileUpload(List<FileUpload> fileUpload) {
		this.fileUpload = fileUpload;
	}
	

}
