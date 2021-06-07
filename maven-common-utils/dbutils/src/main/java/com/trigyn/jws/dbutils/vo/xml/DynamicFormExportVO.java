package com.trigyn.jws.dbutils.vo.xml;

import java.util.Map;
import java.util.Objects;

public class DynamicFormExportVO {

	private String					formId				= null;

	private String					formName			= null;

	private String					formDescription		= null;

	private Integer					formTypeId			= 1;

	private String					selectQueryFileName	= null;

	private String					htmlBodyFileName	= null;

	private String					dataSourceId		= null;

	private Map<Integer, String>	saveFileNameMap		= null;

	public DynamicFormExportVO() {

	}

	public DynamicFormExportVO(String formId, String formName, String formDescription, Integer formTypeId, String selectQueryFileName,
			String htmlBodyFileName, String dataSourceId, Map<Integer, String> saveFileNameMap) {
		this.formId					= formId;
		this.formName				= formName;
		this.formDescription		= formDescription;
		this.formTypeId				= formTypeId;
		this.selectQueryFileName	= selectQueryFileName;
		this.htmlBodyFileName		= htmlBodyFileName;
		this.dataSourceId			= dataSourceId;
		this.saveFileNameMap		= saveFileNameMap;
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

	@Override
	public int hashCode() {
		return Objects.hash(dataSourceId, formDescription, formId, formName, formTypeId, htmlBodyFileName, saveFileNameMap,
				selectQueryFileName);
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
		DynamicFormExportVO other = (DynamicFormExportVO) obj;
		return Objects.equals(dataSourceId, other.dataSourceId) && Objects.equals(formDescription, other.formDescription)
				&& Objects.equals(formId, other.formId) && Objects.equals(formName, other.formName)
				&& Objects.equals(formTypeId, other.formTypeId) && Objects.equals(htmlBodyFileName, other.htmlBodyFileName)
				&& Objects.equals(saveFileNameMap, other.saveFileNameMap) && Objects.equals(selectQueryFileName, other.selectQueryFileName);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DynamicFormExportVO [formId=").append(formId).append(", formName=").append(formName).append(", formDescription=")
				.append(formDescription).append(", formTypeId=").append(formTypeId).append(", selectQueryFileName=")
				.append(selectQueryFileName).append(", htmlBodyFileName=").append(htmlBodyFileName).append(", dataSourceId=")
				.append(dataSourceId).append(", saveFileNameMap=").append(saveFileNameMap).append("]");
		return builder.toString();
	}

}
