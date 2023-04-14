package com.trigyn.jws.webstarter.vo;

import java.util.List;

import com.trigyn.jws.dynarest.entities.FileUpload;

public class FileUploadImportEntity {

	public FileUploadImportEntity(List<FileUpload> files) {
		super();
		this.files = files;
	}

	public FileUploadImportEntity() {
		super();
	}

	private List<FileUpload> files;

	public List<FileUpload> getFiles() {
		return files;
	}

	public void setFiles(List<FileUpload> files) {
		this.files = files;
	}


	
}
