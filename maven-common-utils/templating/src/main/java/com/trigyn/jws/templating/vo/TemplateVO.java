package com.trigyn.jws.templating.vo;

import java.util.Objects;

public class TemplateVO {

    private String templateId = null;

    private String templateName = null;

    private String template = null;
    
    private String checksum = null; 
    
    private boolean checksumChanged = true;


    public TemplateVO() {
    }

    public TemplateVO(String templateId, String templateName, String template) {
        this.templateId = templateId;
        this.templateName = templateName;
        this.template = template;
    }
    
    
    public TemplateVO(String templateId, String templateName, String template,String checksum) {
        this.templateId = templateId;
        this.templateName = templateName;
        this.template = template;
        this.checksum = checksum;
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

	@Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof TemplateVO)) {
            return false;
        }
        TemplateVO templateVO = (TemplateVO) o;
        return Objects.equals(templateId, templateVO.templateId) && Objects.equals(templateName, templateVO.templateName) && Objects.equals(template, templateVO.template) && Objects.equals(checksum, templateVO.checksum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(templateId, templateName, template,checksum);
    }

    @Override
    public String toString() {
        return "{" +
            " templateId='" + getTemplateId() + "'" +
            ", templateName='" + getTemplateName() + "'" +
            ", template='" + getTemplate() + "'" +
            ", checksum='" + getChecksum() + "'" +
            "}";
    }

    
}