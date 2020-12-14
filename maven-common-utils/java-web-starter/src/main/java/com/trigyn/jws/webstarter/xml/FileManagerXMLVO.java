package com.trigyn.jws.webstarter.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.trigyn.jws.dbutils.vo.xml.XMLVO;
import com.trigyn.jws.dynamicform.entities.FileUploadConfig;

@XmlRootElement(name = "fileUploadData")
@XmlAccessorType (XmlAccessType.FIELD)
public class FileManagerXMLVO extends XMLVO {
	
	@XmlElement(name = "fileUpload")
    private List<FileUploadConfig> fileUploadDetails = new ArrayList<>();

	public List<FileUploadConfig> getFileUploadDetails() {
		return fileUploadDetails;
	}

	public void setFileUploadDetails(List<FileUploadConfig> fileUploadDetails) {
		this.fileUploadDetails = fileUploadDetails;
	}

}
