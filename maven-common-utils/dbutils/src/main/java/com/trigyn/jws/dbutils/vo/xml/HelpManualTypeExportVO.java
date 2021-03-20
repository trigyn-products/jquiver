package com.trigyn.jws.dbutils.vo.xml;

import java.util.ArrayList;
import java.util.List;

public class HelpManualTypeExportVO {

	private String	manualId		= null;

	private String	name			= null;

	private Integer	isSystemManual	= null;
	
	private List<ManualEntryDetailsExportVO> manualEntries = new ArrayList<>();
	
	private FileUploadConfigExportVO fileUploadConfig = null;

	public HelpManualTypeExportVO() {}
	
	public HelpManualTypeExportVO(String manualId, String name, Integer isSystemManual,
			List<ManualEntryDetailsExportVO> manualEntries, FileUploadConfigExportVO fileUploadConfig) {
		super();
		this.manualId			= manualId;
		this.name				= name;
		this.isSystemManual		= isSystemManual;
		this.manualEntries		= manualEntries;
		this.fileUploadConfig	= fileUploadConfig;
	}

	public String getManualId() {
		return manualId;
	}

	public void setManualId(String manualId) {
		this.manualId = manualId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getIsSystemManual() {
		return isSystemManual;
	}

	public void setIsSystemManual(Integer isSystemManual) {
		this.isSystemManual = isSystemManual;
	}

	public List<ManualEntryDetailsExportVO> getManualEntries() {
		return manualEntries;
	}

	public void setManualEntries(List<ManualEntryDetailsExportVO> manualEntries) {
		this.manualEntries = manualEntries;
	}

	public FileUploadConfigExportVO getFileUploadConfig() {
		return fileUploadConfig;
	}

	public void setFileUploadConfig(FileUploadConfigExportVO fileUploadConfig) {
		this.fileUploadConfig = fileUploadConfig;
	}
	
	
}
