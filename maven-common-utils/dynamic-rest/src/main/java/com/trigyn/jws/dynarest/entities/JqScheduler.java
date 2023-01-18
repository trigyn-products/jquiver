package com.trigyn.jws.dynarest.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "jq_job_scheduler")
public class JqScheduler {

	@Id
	@GeneratedValue(generator = "inquisitive-uuid")
	@GenericGenerator(name = "inquisitive-uuid", strategy = "com.trigyn.jws.dbutils.configurations.CustomUUIDGenerator")
	@Column(name = "scheduler_id")
	private String	schedulerId					= null;

	@Column(name = "scheduler_name")
	private String	scheduler_name				= null;

	@Column(name = "cron_scheduler")
	private String	cronScheduler				= null;

	@Column(name = "is_active")
	private Integer	isActive					= null;

	@Column(name = "header_json")
	private String	headerJson					= null;

	@Column(name = "request_param_json")
	private String	requestParamJson			= null;

	@Column(name = "jws_dynamic_rest_id")
	private String	jwsDynamicRestId			= null;

	@Column(name = "failed_notification_params")
	private String	failedNotificationParamJson	= null;

	@Column(name = "scheduler_type_id")
	private Integer	schedulerTypeId				= null;

	@Column(name = "modified_by")
	private String	modifiedBy					= null;

	@JsonIgnore
	@Column(name = "last_modified_date")
	private Date	modifiedDate				= null;

	@Column(name = "is_custom_updated")
	private Integer	isCustomUpdated				= 1;

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

	public String getJwsDynamicRestId() {
		return jwsDynamicRestId;
	}

	public void setJwsDynamicRestId(String jwsDynamicRestId) {
		this.jwsDynamicRestId = jwsDynamicRestId;
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

	public Integer getIsCustomUpdated() {
		return isCustomUpdated;
	}

	public void setIsCustomUpdated(Integer isCustomUpdated) {
		this.isCustomUpdated = isCustomUpdated;
	}

	public JqScheduler getObject() {
		JqScheduler obj = new JqScheduler();
		obj.setCronScheduler(cronScheduler != null ? cronScheduler.trim() : cronScheduler);
		obj.setFailedNotificationParamJson(
				failedNotificationParamJson != null ? failedNotificationParamJson.trim() : failedNotificationParamJson);
		obj.setHeaderJson(headerJson != null ? headerJson.trim() : headerJson);
		obj.setIsActive(isActive);
		obj.setRequestParamJson(requestParamJson != null ? requestParamJson.trim() : requestParamJson);
		obj.setScheduler_name(scheduler_name != null ? scheduler_name.trim() : scheduler_name);
		obj.setSchedulerId(schedulerId != null ? schedulerId.trim() : schedulerId);
		obj.setJwsDynamicRestId(jwsDynamicRestId != null ? jwsDynamicRestId.trim() : jwsDynamicRestId);
		obj.setSchedulerTypeId(schedulerTypeId);
		obj.setModifiedBy(modifiedBy);
		obj.setModifiedDate(modifiedDate);

		return obj;
	}

}
