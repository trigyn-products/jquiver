package com.trigyn.jws.webstarter.vo;

import java.util.Date;

public class Settings {

	private Date timestamp = null;
	
	private String currentVersion = null;
	
	private String author = null;

	private String leastSupportedVersion = null;
	
	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getCurrentVersion() {
		return currentVersion;
	}

	public void setCurrentVersion(String currentVersion) {
		this.currentVersion = currentVersion;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getLeastSupportedVersion() {
		return leastSupportedVersion;
	}

	public void setLeastSupportedVersion(String leastSupportedVersion) {
		this.leastSupportedVersion = leastSupportedVersion;
	}
}
