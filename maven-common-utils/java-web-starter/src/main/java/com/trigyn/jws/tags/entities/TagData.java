package com.trigyn.jws.tags.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "jq_tag_data")
public class TagData {

    @Id
    @Column(name = "tag_id", length = 50, nullable = false)
    private String tagId;

    @Column(name = "tag_name", length = 100, nullable = false)
    private String tagName;

    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    @Column(name = "created_date", nullable = false, updatable = false, insertable = false,
            columnDefinition = "timestamp default current_timestamp")
    private Date createdDate;

    @Column(name = "last_updated_by", length = 50)
    private String lastUpdatedBy;

    @Column(name = "last_updated_ts", insertable = false, updatable = false,
            columnDefinition = "timestamp default current_timestamp")
    private Date lastUpdatedTs;

    // ---- Constructors ----
    public TagData() {
    }

    public TagData(String tagId, String tagName, String createdBy, String lastUpdatedBy) {
        this.tagId = tagId;
        this.tagName = tagName;
        this.createdBy = createdBy;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    // ---- Getters and Setters ----
    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

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

    @Override
    public String toString() {
        return "TagData{" +
                "tagId='" + tagId + '\'' +
                ", tagName='" + tagName + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdDate=" + createdDate +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                ", lastUpdatedTs=" + lastUpdatedTs +
                '}';
    }
}