package com.trigyn.jws.tags.entities;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "jq_tag_entity_details")
public class TagEntityDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "tag_entity_details_id", length = 50, nullable = false)
    private String tagEntityDetailId;

    @Column(name = "module_id", length = 100)
    private String moduleId;

    @Column(name = "entity_id", length = 255)
    private String entityId;

    @Column(name = "tag_id", length = 50)
    private String tagId;

    @Lob
    @Column(name = "module_json", columnDefinition = "LONGTEXT")
    private String moduleJson;

    @Column(name = "module_json_checksum", length = 512)
    private String moduleJsonChecksum;

    @Lob
    @Column(name = "permissions", columnDefinition = "LONGTEXT")
    private String permissions;
    
    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    @Column(name = "created_date", nullable = false, updatable = false, insertable = false,
            columnDefinition = "timestamp default current_timestamp")
    private Date createdDate;


    @Column(name = "last_updated_by", length = 100)
    private String lastUpdatedBy;

    @Column(name = "last_updated_ts", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date lastUpdatedTs;

    // ---------- Constructors ----------

    public TagEntityDetails() {
    }

    public TagEntityDetails(String tagEntityDetailId, String moduleId, String entityId, String tagId,
                              String moduleJson, String moduleJsonChecksum, String permissions,
                              String lastUpdatedBy, Date lastUpdatedTs) {
        this.tagEntityDetailId = tagEntityDetailId;
        this.moduleId = moduleId;
        this.entityId = entityId;
        this.tagId = tagId;
        this.moduleJson = moduleJson;
        this.moduleJsonChecksum = moduleJsonChecksum;
        this.permissions = permissions;
        this.lastUpdatedBy = lastUpdatedBy;
        this.lastUpdatedTs = lastUpdatedTs;
    }

    // ---------- Getters & Setters ----------

    public String getTagEntityDetailId() {
        return tagEntityDetailId;
    }

    public void setTagEntityDetailId(String tagEntityDetailId) {
        this.tagEntityDetailId = tagEntityDetailId;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getModuleJson() {
        return moduleJson;
    }

    public void setModuleJson(String moduleJson) {
        this.moduleJson = moduleJson;
    }

    public String getModuleJsonChecksum() {
        return moduleJsonChecksum;
    }

    public void setModuleJsonChecksum(String moduleJsonChecksum) {
        this.moduleJsonChecksum = moduleJsonChecksum;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Date getLastUpdatedTs() {
        return lastUpdatedTs;
    }

    public void setLastUpdatedTs(Date lastUpdatedTs) {
        this.lastUpdatedTs = lastUpdatedTs;
    }
    
    

    // ---------- Lifecycle Methods ----------

    public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@PrePersist
    @PreUpdate
    protected void onUpdate() {
        lastUpdatedTs = new Date();
    }

    @Override
    public String toString() {
        return "JqTagEntityDetails{" +
                "tagEntityDetailId='" + tagEntityDetailId + '\'' +
                ", moduleId='" + moduleId + '\'' +
                ", entityId='" + entityId + '\'' +
                ", tagId='" + tagId + '\'' +
                ", moduleJsonChecksum='" + moduleJsonChecksum + '\'' +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                ", lastUpdatedTs=" + lastUpdatedTs +
                '}';
    }
}
