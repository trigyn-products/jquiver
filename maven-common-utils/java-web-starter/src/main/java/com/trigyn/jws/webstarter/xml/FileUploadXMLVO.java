package com.trigyn.jws.webstarter.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.trigyn.jws.dbutils.vo.xml.XMLVO;
import com.trigyn.jws.dynarest.entities.FileUpload;

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
