package com.trigyn.jws.templating.vo;

import java.io.Serializable;
import java.util.Objects;

public class TemplateVO implements Serializable{

	private static final long serialVersionUID 	= 2926106225906899844L;

	private String templateId 					= null;

    private String templateName 				= null;

    private String template 					= null;
    
    private String checksum 					= null; 
    
    private boolean checksumChanged 			= true;
    
    private Integer templateTypeId				= null;
    
    public TemplateVO() {
    }

    public TemplateVO(String templateId, String templateName, String template) {
        this.templateId 	= templateId;
        this.templateName 	= templateName;
        this.template 		= template;
    }
    
	public TemplateVO(String templateId, String templateName, String template,String checksum) {
        this.templateId 	= templateId;
        this.templateName 	= templateName;
        this.template 		= template;
        this.checksum 		= checksum;
    }
    
    public TemplateVO(String templateId, String templateName, String template, String checksum, Integer templateTypeId) {
		this.templateId 		= templateId;
		this.templateName 		= templateName;
		this.template 			= template;
		this.checksum			= checksum; 
		this.templateTypeId 	= templateTypeId;
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
	
	public Integer getTemplateType() {
		return templateTypeId;
	}

	public void setTemplateType(Integer templateTypeId) {
		this.templateTypeId = templateTypeId;
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
				&& Objects.equals(templateName, other.templateName) && Objects.equals(templateTypeId, other.templateTypeId);
	}

	@Override
	public String toString() {
		return "TemplateVO [templateId=" + templateId + ", templateName=" + templateName + ", template=" + template
				+ ", checksum=" + checksum + ", checksumChanged=" + checksumChanged + ", templateTypeId=" + templateTypeId
				+ "]";
	}


    
}