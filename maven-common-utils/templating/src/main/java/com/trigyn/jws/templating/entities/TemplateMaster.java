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

@Entity
@Table(name = "template_master")
public class TemplateMaster implements Serializable {

    private static final long serialVersionUID  = -5210067567698574574L;

    @Id
    @GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "template_id", nullable = false)
    private String templateId                   = null;

    @Column(name = "template_name")
    private String templateName                  = null;

    @Column(name = "template")
    private String template                     = null;

    @Column(name = "created_by")
    private String createdBy                    = null;

    @Column(name = "updated_by")
    private String updatedBy                    = null;

    @Column(name = "updated_date")
    private Date updatedDate                    = null;

    @Column(name = "checksum")
    private String checksum                     = null;

    /**
     * 
     */
    public TemplateMaster() {

    }

    /**
     * @param templateId
     * @param templateName
     * @param template
     * @param createdBy
     * @param updatedBy
     * @param updatedDate
     */
    public TemplateMaster(String templateId, String templateName, String template, String createdBy, String updatedBy,
            Date updatedDate) {
        this.templateId     = templateId;
        this.templateName   = templateName;
        this.template       = template;
        this.createdBy      = createdBy;
        this.updatedBy      = updatedBy;
        this.updatedDate    = updatedDate;
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
        return Objects.hash(createdBy, updatedBy, templateId, templateName, template);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TemplateMaster other = (TemplateMaster) obj;
        return Objects.equals(createdBy, other.createdBy) 
                && Objects.equals(updatedBy, other.updatedBy)
                && Objects.equals(templateId, other.templateId)
                && Objects.equals(templateName, other.templateName) 
                && Objects.equals(template, other.template);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder().append("{ vmMasterId = ").append(templateId)
		.append(", vmName = ").append(templateName)
        .append(", vmTemplate = ").append(template)
        .append(", createdBy = ").append(createdBy)
        .append(", updatedBy = ").append(updatedBy)
        .append(", updatedDate = ").append(updatedDate)
		.append(" }");
		return stringBuilder.toString();
    }

    

    

}