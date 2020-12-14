package com.trigyn.jws.dbutils.vo.xml;

public class TemplateExportVO {

	private String templateId 			= null;

	private String templateName 		= null;

	private Integer templateTypeId 		= 1;

	private String templateFileName 	= null;

	public TemplateExportVO() {
		
	}
	
	public TemplateExportVO(String templateId, String templateName, Integer templateTypeId, String templateFileName) {
		super();
		this.templateId = templateId;
		this.templateName = templateName;
		this.templateTypeId = templateTypeId;
		this.templateFileName = templateFileName;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public Integer getTemplateTypeId() {
		return templateTypeId;
	}

	public void setTemplateTypeId(Integer templateTypeId) {
		this.templateTypeId = templateTypeId;
	}

	public String getTemplateFileName() {
		return templateFileName;
	}

	public void setTemplateFileName(String templateFileName) {
		this.templateFileName = templateFileName;
	}

}
