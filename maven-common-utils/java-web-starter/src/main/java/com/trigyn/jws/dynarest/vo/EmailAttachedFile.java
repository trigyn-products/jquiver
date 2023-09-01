package com.trigyn.jws.dynarest.vo;

import java.io.File;

public class EmailAttachedFile {
	private static final long	serialVersionUID	= 1L;
	
	private File file = null;
	
	private boolean isEmbeddedImage = false;
	
	private String embeddedImageValue;

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public boolean isEmbeddedImage() {
		return isEmbeddedImage;
	}

	public void setIsEmbeddedImage(boolean isEmbeddedImage) {
		this.isEmbeddedImage = isEmbeddedImage;
	}

	public String getEmbeddedImageValue() {
		return embeddedImageValue;
	}

	public void setEmbeddedImageValue(String embeddedImageValue) {
		this.embeddedImageValue = embeddedImageValue;
	}
}
