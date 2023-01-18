package com.trigyn.jws.dbutils.vo;

public class FileInfo {
	
	public static enum FileType{
		Physical,FileBin
	}

	private String		fileId			= null;
	private String		absolutePath	= null;
	private String		fileName		= null;
	private Long		sizeInBytes		= null;
	private FileType	fileType		= FileType.FileBin;
	private Long		createdTime		= null;
	private Integer 	returnAction 	= null;
	private String 		mimeType 		= null;

	public FileInfo() {
		
	}
	
	public FileInfo(String fileId, String fileName, Long sizeInBytes) {
		super();
		this.fileId			= fileId;
		this.fileName		= fileName;
		this.sizeInBytes	= sizeInBytes;
	}
	
	public String getAbsolutePath() {
		return absolutePath;
	}
	
	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}
	
	public FileType getFileType() {
		return fileType;
	}
	
	public void setFileType(FileType fileType) {
		this.fileType = fileType;
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

	public Long getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Long createdTime) {
		this.createdTime = createdTime;
	}

	public Integer getReturnAction() {
		return returnAction;
	}

	public void setReturnAction(Integer returnAction) {
		this.returnAction = returnAction;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

}
