package com.trigyn.jws.dbutils.vo;

public class FileInfo {
	private String fileId = null;
	private String fileName = null;
	private Long sizeInBytes = null;

	public FileInfo(String fileId, String fileName, Long sizeInBytes) {
		super();
		this.fileId = fileId;
		this.fileName = fileName;
		this.sizeInBytes = sizeInBytes;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Long getSizeInBytes() {
		return sizeInBytes;
	}

	public void setSizeInBytes(Long sizeInBytes) {
		this.sizeInBytes = sizeInBytes;
	}

}
