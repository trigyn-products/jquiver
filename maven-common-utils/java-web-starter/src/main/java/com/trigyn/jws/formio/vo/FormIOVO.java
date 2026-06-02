package com.trigyn.jws.formio.vo;

import java.io.Serializable;
import java.util.Date;

public class FormIOVO implements Serializable{

	private static final long serialVersionUID = 5342852642756270184L;

	private String	formIoId		= null;

	private String	createdBy		= null;

	private Date	createdDate		= null;

	private String	formDescription	= null;

	private String	formIoChecksum	= null;

	private String	formIoJson		= null;

	private Integer	formIoType		= null;

	private String	formName		= null;
	
	private String	persistenceType	= null;

	private Integer	isCustomUpdated	= null;

	private String	lastUpdatedBy	= null;

	private Date	lastUpdatedTs	= null;
	
	private Integer	multiSubmit	= null;
	
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
		return createdDate;
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

	public Date getLastUpdatedTs() {
		return lastUpdatedTs;
	}

	public void setLastUpdatedTs(Date lastUpdatedTs) {
		this.lastUpdatedTs = lastUpdatedTs;
	}
	
	

}
