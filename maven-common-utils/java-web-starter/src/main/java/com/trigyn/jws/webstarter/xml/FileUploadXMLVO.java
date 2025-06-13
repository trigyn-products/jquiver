package com.trigyn.jws.webstarter.xml;

import java.util.ArrayList;
import java.util.List;

import com.trigyn.jws.dbutils.vo.xml.XMLVO;
import com.trigyn.jws.dynarest.entities.FileUpload;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "FileUpload")
@XmlAccessorType(XmlAccessType.FIELD)
public class FileUploadXMLVO extends XMLVO{
	
	@XmlElement(name = "fileUpload")
	private List<FileUpload> fileUploadDetails = new ArrayList<>();
	
	public List<FileUpload> getFileUploadDetails() {
		return fileUploadDetails;
	}

	public void setFileUploadDetails(List<FileUpload> fileUploadDetails) {
		this.fileUploadDetails = fileUploadDetails;
	}

}
