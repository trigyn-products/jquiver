package com.trigyn.jws.quartz.models.entities.request;

import java.util.Map;

import com.trigyn.jws.dynarest.vo.Email;
import com.trigyn.jws.quartz.models.entities.MailSchedule;

public class EmailSchedulerRequestVo {
	
	private MailSchedule mailSchedule;
	
	/** Used for Mail Scheduling **/
	private String mailSenderGroupId;
	
	private String dynamicRestUrl;
	
	/** Used for Mail Scheduling **/
	private String mailScheduleId;
	
	private Email email ;
	
	private Map<String, Object>	requestParams;

	public MailSchedule getMailSchedule() {
		return mailSchedule;
	}

	public void setMailSchedule(MailSchedule mailSchedule) {
		this.mailSchedule = mailSchedule;
	}

	public String getMailSenderGroupId() {
		return mailSenderGroupId;
	}

	public void setMailSenderGroupId(String mailSenderGroupId) {
		this.mailSenderGroupId = mailSenderGroupId;
	}

	public String getDynamicRestUrl() {
		return dynamicRestUrl;
	}

	public void setDynamicRestUrl(String dynamicRestUrl) {
		this.dynamicRestUrl = dynamicRestUrl;
	}

	public String getMailScheduleId() {
		return mailScheduleId;
	}

	public void setMailScheduleId(String mailScheduleId) {
		this.mailScheduleId = mailScheduleId;
	}

	public Email getEmail() {
		return email;
	}

	public void setEmail(Email email) {
		this.email = email;
	}

	public Map<String, Object> getRequestParams() {
		return requestParams;
	}

	public void setRequestParams(Map<String, Object> requestParams) {
		this.requestParams = requestParams;
	}

	

}
