package com.trigyn.jws.dynarest.vo;

import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "attachment")
@XmlAccessorType(XmlAccessType.FIELD)
public class WebClientAttacmentVO {

	@XmlAttribute(name = "type")
	private Integer type = null;

	@XmlAttribute(name = "hasEmbeddedImage")
	private String hasEmbeddedImage = null;
	@XmlElement(name = "name")
	private String fileName = null;

	@XmlElement(name = "path")
	private String filePath = null;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getHasEmbeddedImage() {
		return hasEmbeddedImage;
	}

	public void setHasEmbeddedImage(String hasEmbeddedImage) {
		this.hasEmbeddedImage = hasEmbeddedImage;
	}

	@Override
	public int hashCode() {
		return Objects.hash(fileName, filePath, type);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		WebClientAttacmentVO other = (WebClientAttacmentVO) obj;
		return Objects.equals(fileName, other.fileName) && Objects.equals(filePath, other.filePath)
				&& Objects.equals(type, other.type);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("WebClientAttacmentVO [type=").append(type).append(", fileName=").append(fileName)
				.append(", filePath=").append(filePath).append("]");
		return builder.toString();
	}

}
