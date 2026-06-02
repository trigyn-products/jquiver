package com.trigyn.jws.formio.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 * The persistent class for the jq_form_io database table.
 * 
 */
@Entity
@Table(name = "jq_form_io")
@NamedQuery(name = "FormIo.findAll", query = "SELECT f FROM FormIO f")
public class FormIO implements Serializable {

	private static final long	serialVersionUID	= 1L;

	@Id
	@Column(name = "form_io_id")
	private String				formIoId			= null;

	@Column(name = "created_by")
	private String				createdBy			= null;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_date")
	private Date				createdDate			= null;

	@Column(name = "form_description")
	private String				formDescription		= null;

	@Column(name = "form_io_checksum")
	private String				formIoChecksum		= null;

	@Lob
	@Column(name = "form_io_json")
	private String				formIoJson			= null;

	@Column(name = "form_io_type")
	private Integer				formIoType			= 1;

	@Column(name = "form_name")
	private String				formName			= null;

	@Column(name = "persistence_type")
	private String				persistenceType		= null;
	
	@Column(name = "is_custom_updated")
	private Integer				isCustomUpdated		= 1;

	@Column(name = "last_updated_by")
	private String				lastUpdatedBy		= "admin@jquiver.io";

	@Column(name = "last_updated_ts")
	private Date				lastUpdatedTs		= null;
	
	@Column(name = "multi_submit")
	private Integer	multiSubmit	= null;
	
	@Column(name = "route_name")
	private String	routeName	= null;

	public String getFormIoId() {
		return this.formIoId;
	}

	public void setFormIoId(String formIoId) {
		this.formIoId = formIoId;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getFormDescription() {
		return this.formDescription;
	}

	public void setFormDescription(String formDescription) {
		this.formDescription = formDescription;
	}

	public String getFormIoChecksum() {
		return this.formIoChecksum;
	}

	public void setFormIoChecksum(String formIoChecksum) {
		this.formIoChecksum = formIoChecksum;
	}

	public String getFormIoJson() {
		return this.formIoJson;
	}

	public void setFormIoJson(String formIoJson) {
		this.formIoJson = formIoJson;
	}

	public Integer getFormIoType() {
		return this.formIoType;
	}

	public void setFormIoType(Integer formIoType) {
		this.formIoType = formIoType;
	}

	public String getFormName() {
		return this.formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public Integer getIsCustomUpdated() {
		return this.isCustomUpdated;
	}

	public void setIsCustomUpdated(Integer isCustomUpdated) {
		this.isCustomUpdated = isCustomUpdated;
	}

	public String getLastUpdatedBy() {
		return this.lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastUpdatedTs() {
		return this.lastUpdatedTs;
	}

	public void setLastUpdatedTs(Date lastUpdatedTs) {
		this.lastUpdatedTs = lastUpdatedTs;
	}

	public String getPersistenceType() {
		return persistenceType;
	}

	public void setPersistenceType(String persistenceType) {
		this.persistenceType = persistenceType;
	}

	
	public Integer getMultiSubmit() {
		return multiSubmit;
	}

	public void setMultiSubmit(Integer multiSubmit) {
		this.multiSubmit = multiSubmit;
	}

	
	public String getRouteName() {
		return routeName;
	}

	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}

	public FormIO getObject() {
		return this;
	}
	
	

	@Override
	public int hashCode() {
		return Objects.hash(createdBy, createdDate, formDescription, formIoChecksum, formIoId, formIoJson, formIoType,
				formName, persistenceType, lastUpdatedBy, lastUpdatedTs,multiSubmit,routeName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FormIO other = (FormIO) obj;
		return Objects.equals(createdBy, other.createdBy) && Objects.equals(createdDate, other.createdDate)
				&& Objects.equals(formDescription, other.formDescription)
				&& Objects.equals(formIoChecksum, other.formIoChecksum) && Objects.equals(formIoId, other.formIoId)
				&& Objects.equals(formIoJson, other.formIoJson) && formIoType == other.formIoType
				&& Objects.equals(formName, other.formName) && isCustomUpdated == other.isCustomUpdated
				&& Objects.equals(persistenceType, other.persistenceType)
				&& Objects.equals(lastUpdatedBy, other.lastUpdatedBy)
				&& Objects.equals(lastUpdatedTs, other.lastUpdatedTs)
				&& Objects.equals(multiSubmit, other.multiSubmit)
				&& Objects.equals(routeName, other.routeName);
	}

}