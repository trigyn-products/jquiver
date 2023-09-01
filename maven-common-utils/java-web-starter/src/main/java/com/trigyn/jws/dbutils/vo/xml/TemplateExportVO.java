package com.trigyn.jws.dbutils.vo.xml;

import java.util.Date;

public class TemplateExportVO {

	private String	templateId			= null;

	private String	templateName		= null;

	private Integer	templateTypeId		= 1;

	private String	templateFileName	= null;

	private Date	updatedDate			= null;

	public TemplateExportVO() {

	}

	public TemplateExportVO(String templateId, String templateName, Integer templateTypeId, String templateFileName,
			Date updatedDate) {
		super();
		this.templateId			= templateId;
		this.templateName		= templateName;
		this.templateTypeId		= templateTypeId;
		this.templateFileName	= templateFileName;
		this.updatedDate		= updatedDate;
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

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

}
