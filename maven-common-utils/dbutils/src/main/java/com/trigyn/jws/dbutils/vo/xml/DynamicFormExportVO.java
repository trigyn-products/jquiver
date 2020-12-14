package com.trigyn.jws.dbutils.vo.xml;

import java.util.Map;

public class DynamicFormExportVO {

	private String 						formId 					= null;

	private String 						formName				= null;

	private String 						formDescription 		= null;

	private Integer 					formTypeId 				= 1;

	private String						selectQueryFileName		= null;

	private String						htmlBodyFileName		= null;

	private Map<Integer, String>		saveFileNameMap			= null;

	public DynamicFormExportVO() {
		
	}
	
	public DynamicFormExportVO(String formId, String formName, String formDescription, Integer formTypeId,
			String selectQueryFileName, String htmlBodyFileName, Map<Integer, String> saveFileNameMap) {
		this.formId = formId;
		this.formName = formName;
		this.formDescription = formDescription;
		this.formTypeId = formTypeId;
		this.selectQueryFileName = selectQueryFileName;
		this.htmlBodyFileName = htmlBodyFileName;
		this.saveFileNameMap = saveFileNameMap;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public String getFormDescription() {
		return formDescription;
	}

	public void setFormDescription(String formDescription) {
		this.formDescription = formDescription;
	}

	public Integer getFormTypeId() {
		return formTypeId;
	}

	public void setFormTypeId(Integer formTypeId) {
		this.formTypeId = formTypeId;
	}

	public String getSelectQueryFileName() {
		return selectQueryFileName;
	}

	public void setSelectQueryFileName(String selectQueryFileName) {
		this.selectQueryFileName = selectQueryFileName;
	}

	public String getHtmlBodyFileName() {
		return htmlBodyFileName;
	}

	public void setHtmlBodyFileName(String htmlBodyFileName) {
		this.htmlBodyFileName = htmlBodyFileName;
	}

	public Map<Integer, String> getSaveFileNameMap() {
		return saveFileNameMap;
	}

	public void setSaveFileNameMap(Map<Integer, String> saveFileNameMap) {
		this.saveFileNameMap = saveFileNameMap;
	}

}
