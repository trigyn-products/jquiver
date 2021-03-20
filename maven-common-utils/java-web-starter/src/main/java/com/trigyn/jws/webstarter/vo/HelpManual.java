package com.trigyn.jws.webstarter.vo;

import java.util.List;

import com.trigyn.jws.dynamicform.entities.FileUpload;
import com.trigyn.jws.dynamicform.entities.FileUploadConfig;
import com.trigyn.jws.dynamicform.entities.ManualEntryDetails;
import com.trigyn.jws.dynamicform.entities.ManualType;

public class HelpManual {

	private ManualType manualType;
	
	private List<ManualEntryDetails> manualEntryDetails;
	
	private List<FileUpload> fileUpload;
	
	private FileUploadConfig fileUploadConfig;

	public HelpManual(ManualType manualType, List<ManualEntryDetails> manualEntryDetails, List<FileUpload> fileUpload,
			FileUploadConfig fileUploadConfig) {
		super();
		this.manualType			= manualType;
		this.manualEntryDetails	= manualEntryDetails;
		this.fileUpload			= fileUpload;
		this.fileUploadConfig	= fileUploadConfig;
	}

	public ManualType getManualType() {
		return manualType;
	}

	public void setManualType(ManualType manualType) {
		this.manualType = manualType;
	}

	public List<ManualEntryDetails> getManualEntryDetails() {
		return manualEntryDetails;
	}

	public void setManualEntryDetails(List<ManualEntryDetails> manualEntryDetails) {
		this.manualEntryDetails = manualEntryDetails;
	}

	public List<FileUpload> getFileUpload() {
		return fileUpload;
	}

	public void setFileUpload(List<FileUpload> fileUpload) {
		this.fileUpload = fileUpload;
	}

	public FileUploadConfig getFileUploadConfig() {
		return fileUploadConfig;
	}

	public void setFileUploadConfig(FileUploadConfig fileUploadConfig) {
		this.fileUploadConfig = fileUploadConfig;
	}
	
	
}
