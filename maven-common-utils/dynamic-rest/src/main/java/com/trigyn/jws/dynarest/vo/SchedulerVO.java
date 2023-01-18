package com.trigyn.jws.dynarest.vo;

import java.util.Date;

public class SchedulerVO {
	private String	schedulerId					= null;

	private String	scheduler_name				= null;

	private String	cronScheduler				= null;

	private Integer	isActive					= null;

	private String	headerJson					= null;

	private String	requestParamJson			= null;

	private String	jwsDynamicRestId			= null;

	private String	failedNotificationParamJson	= null;

	private Integer	schedulerTypeId				= null;

	private String	modifiedBy					= null;

	private Date	modifiedDate				= null;

	public SchedulerVO(String schedulerId, String scheduler_name, String cronScheduler, Integer isActive,
			String headerJson, String requestParamJson, String jwsDynamicRestId, String failedNotificationParamJson,
			Integer schedulerTypeId, String modifiedBy, Date modifiedDate) {
		super();
		this.schedulerId					= schedulerId;
		this.scheduler_name					= scheduler_name;
		this.cronScheduler					= cronScheduler;
		this.isActive						= isActive;
		this.headerJson						= headerJson;
		this.requestParamJson				= requestParamJson;
		this.jwsDynamicRestId				= jwsDynamicRestId;
		this.failedNotificationParamJson	= failedNotificationParamJson;
		this.schedulerTypeId				= schedulerTypeId;
		this.modifiedBy						= modifiedBy;
		this.modifiedDate					= modifiedDate;
	}

	public SchedulerVO() {
	}

	public String getSchedulerId() {
		return schedulerId;
	}

	public void setSchedulerId(String schedulerId) {
		this.schedulerId = schedulerId;
	}

	public String getScheduler_name() {
		return scheduler_name;
	}

	public void setScheduler_name(String scheduler_name) {
		this.scheduler_name = scheduler_name;
	}

	public String getCronScheduler() {
		return cronScheduler;
	}

	public void setCronScheduler(String cronScheduler) {
		this.cronScheduler = cronScheduler;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public String getHeaderJson() {
		return headerJson;
	}

	public void setHeaderJson(String headerJson) {
		this.headerJson = headerJson;
	}

	public String getRequestParamJson() {
		return requestParamJson;
	}

	public void setRequestParamJson(String requestParamJson) {
		this.requestParamJson = requestParamJson;
	}

	public String getJwsDynamicRestId() {
		return jwsDynamicRestId;
	}

	public void setJwsDynamicRestId(String jwsDynamicRestId) {
		this.jwsDynamicRestId = jwsDynamicRestId;
	}

	public String getFailedNotificationParamJson() {
		return failedNotificationParamJson;
	}

	public void setFailedNotificationParamJson(String failedNotificationParamJson) {
		this.failedNotificationParamJson = failedNotificationParamJson;
	}

	public Integer getSchedulerTypeId() {
		return schedulerTypeId;
	}

	public void setSchedulerTypeId(Integer schedulerTypeId) {
		this.schedulerTypeId = schedulerTypeId;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

}
