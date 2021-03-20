package com.trigyn.jws.templating.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "jq_template_master")
public class TemplateMaster implements Serializable {

	private static final long	serialVersionUID	= -5210067567698574574L;

	@Id
	@GeneratedValue(generator = "inquisitive-uuid")
	@GenericGenerator(name = "inquisitive-uuid", strategy = "com.trigyn.jws.dbutils.configurations.CustomUUIDGenerator")
	@Column(name = "template_id", nullable = false)
	private String				templateId			= null;

	@Column(name = "template_name")
	private String				templateName		= null;

	@Column(name = "template")
	private String				template			= null;

	@Column(name = "template_type_id")
	private Integer				templateTypeId		= 1;

	@Column(name = "created_by")
	private String				createdBy			= null;

	@Column(name = "updated_by")
	private String				updatedBy			= null;

	@JsonIgnore
	@Column(name = "updated_date")
	private Date				updatedDate			= null;

	@Column(name = "checksum")
	private String				checksum			= null;

	public TemplateMaster() {

	}

	public TemplateMaster(String templateId, String templateName, String template, Integer templateTypeId,
			String createdBy, String updatedBy, Date updatedDate) {
		this.templateId		= templateId;
		this.templateName	= templateName;
		this.template		= template;
		this.templateTypeId	= templateTypeId;
		this.createdBy		= createdBy;
		this.updatedBy		= updatedBy;
		this.updatedDate	= updatedDate;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String vmMasterId) {
		this.templateId = vmMasterId;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String vmName) {
		this.templateName = vmName;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String vmTemplate) {
		this.template = vmTemplate;
	}

	public Integer getTemplateTypeId() {
		return templateTypeId;
	}

	public void setTemplateTypeId(Integer templateTypeId) {
		this.templateTypeId = templateTypeId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getChecksum() {
		return this.checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	@Override
	public int hashCode() {
		return Objects.hash(checksum, createdBy, template, templateId, templateName, templateTypeId, updatedBy,
				updatedDate);
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
		TemplateMaster other = (TemplateMaster) obj;
		return Objects.equals(checksum, other.checksum) && Objects.equals(createdBy, other.createdBy)
				&& Objects.equals(template, other.template) && Objects.equals(templateId, other.templateId)
				&& Objects.equals(templateName, other.templateName)
				&& Objects.equals(templateTypeId, other.templateTypeId) && Objects.equals(updatedBy, other.updatedBy)
				&& Objects.equals(updatedDate, other.updatedDate);
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder().append("{ vmMasterId = ").append(templateId)
				.append(", vmName = ").append(templateName).append(", vmTemplate = ").append(template)
				.append(", templateTypeId = ").append(templateTypeId).append(", createdBy = ").append(createdBy)
				.append(", updatedBy = ").append(updatedBy).append(", updatedDate = ").append(updatedDate).append(" }");
		return stringBuilder.toString();
	}

	public TemplateMaster getObject() {
		TemplateMaster obj = new TemplateMaster();
		obj.setChecksum(checksum != null ? checksum.trim() : checksum);
		obj.setCreatedBy(createdBy != null ? createdBy.trim() : createdBy);
		obj.setTemplate(template != null ? template.trim() : template);
		obj.setTemplateId(templateId != null ? templateId.trim() : templateId);
		obj.setTemplateName(templateName != null ? templateName.trim() : templateName);
		obj.setTemplateTypeId(templateTypeId);
		obj.setUpdatedBy(updatedBy != null ? updatedBy.trim() : updatedBy);
		obj.setUpdatedDate(updatedDate);
		return obj;
	}

}