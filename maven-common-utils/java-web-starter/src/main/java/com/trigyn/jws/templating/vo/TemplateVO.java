package com.trigyn.jws.templating.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class TemplateVO implements Serializable {

	private static final long	serialVersionUID	= 2926106225906899844L;

	private String				templateId			= null;

	private String				templateName		= null;

	private String				template			= null;

	private String				checksum			= null;

	private boolean				checksumChanged		= true;

	private Integer				templateTypeId		= null;

	private String				createdBy			= null;

	private Date				updatedDate			= null;
	
	private String				updatedBy			= null;

	public TemplateVO() {
	}

	public TemplateVO(String templateId, String templateName, String template, Integer templateTypeId, String checksum,
			String createdBy, String updatedBy,Date updatDate) {
		this.templateId		= templateId;
		this.templateName	= templateName;
		this.template		= template;
		this.templateTypeId	= templateTypeId;
		this.checksum 		= checksum;
		this.createdBy		= createdBy;
		this.updatedBy		= updatedBy;
		this.updatedDate	= updatDate;
	}

	public String getTemplateId() {
		return this.templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getTemplateName() {
		return this.templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getTemplate() {
		return this.template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getChecksum() {
		return checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	public boolean isChecksumChanged() {
		return checksumChanged;
	}

	public void setChecksumChanged(boolean checksumChanged) {
		this.checksumChanged = checksumChanged;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Override
	public int hashCode() {
		return Objects.hash(checksum, checksumChanged, template, templateId, templateName, templateTypeId);
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
		TemplateVO other = (TemplateVO) obj;
		return Objects.equals(checksum, other.checksum) && checksumChanged == other.checksumChanged
				&& Objects.equals(template, other.template) && Objects.equals(templateId, other.templateId)
				&& Objects.equals(templateName, other.templateName)
				&& Objects.equals(templateTypeId, other.templateTypeId);
	}

	@Override
	public String toString() {
		return "TemplateVO [templateId=" + templateId + ", templateName=" + templateName + ", template=" + template
				+ ", checksum=" + checksum + ", checksumChanged=" + checksumChanged + ", templateTypeId="
				+ templateTypeId + "]";
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getTemplateTypeId() {
		return templateTypeId;
	}

	public void setTemplateTypeId(Integer templateTypeId) {
		this.templateTypeId = templateTypeId;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	
}