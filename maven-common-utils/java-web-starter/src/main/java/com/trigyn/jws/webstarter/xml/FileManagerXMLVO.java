package com.trigyn.jws.webstarter.xml;

import java.util.ArrayList;
import java.util.List;

import com.trigyn.jws.dbutils.vo.xml.XMLVO;
import com.trigyn.jws.dynarest.entities.FileUploadConfig;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "fileUploadData")
@XmlAccessorType(XmlAccessType.FIELD)
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
