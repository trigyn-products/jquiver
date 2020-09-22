package com.trigyn.jws.dbutils.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="jws_property_master")
public class PropertyMaster implements Serializable{
	
	private static final long serialVersionUID = -6641076236442355786L;

	@EmbeddedId
	private PropertyMasterPK id = null;
	
	@Column(name = "property_value")
	private String propertyValue =  null;
	
	@Column(name = "is_deleted")
	private Integer isDeleted = null;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_modified_date")
	private Date lastModifiedDate = null;
	
	@Column(name = "modified_by")
	private String modifiedBy =  null;
	
	@Column(name = "app_version")
	private Double appVersion =  null;
	
	@Column(name = "comments")
	private String comments =  null;

	public PropertyMasterPK getId() {
		return id;
	}

	public void setId(PropertyMasterPK id) {
		this.id = id;
	}

	public String getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Double getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(Double appVersion) {
		this.appVersion = appVersion;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public PropertyMaster(PropertyMasterPK id, String propertyValue, Integer isDeleted,
			Date lastModifiedDate, String modifiedBy, Double appVersion, String comments) {
		super();
		this.id = id;
		this.propertyValue = propertyValue;
		this.isDeleted = isDeleted;
		this.lastModifiedDate = lastModifiedDate;
		this.modifiedBy = modifiedBy;
		this.appVersion = appVersion;
		this.comments = comments;
	}

	public PropertyMaster() {
	}

	
	
}
