package com.trigyn.jws.dbutils.vo.xml;

import java.util.ArrayList;
import java.util.List;

public class FilesImportExportVO {
	

	public FilesImportExportVO() {
		super();
	}
	
	public FilesImportExportVO(List<FileUploadExportVO> fileUploadList) {
		super();
		this.fileUploadList = fileUploadList;
	}


	private List<FileUploadExportVO>	fileUploadList		= new ArrayList<>();

	public List<FileUploadExportVO> getFileUploadList() {
		return fileUploadList;
	}

	public void setFileUploadList(List<FileUploadExportVO> fileUploadList) {
		this.fileUploadList = fileUploadList;
	}

}
