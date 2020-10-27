package com.trigyn.jws.webstarter.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "manual_entry_file_association")
public class ManualEntryFileAssociation implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "file_upload_id")
	private String fileUploadId = null;
	
	@Id
	@Column(name = "manual_entry_id")
	private String manualEntryId = null;
	
	public String getFileUploadId() {
		return fileUploadId;
	}

	public void setFileUploadId(String fileUploadId) {
		this.fileUploadId = fileUploadId;
	}

	public String getManualEntryId() {
		return manualEntryId;
	}

	public void setManualEntryId(String manualEntryId) {
		this.manualEntryId = manualEntryId;
	}
}
