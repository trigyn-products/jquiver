package com.trigyn.jws.quartz.models.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "mail_schedule")
public class MailSchedule implements Serializable {

	private static final long	serialVersionUID	= 6321323265487281667L;

	@Id
	@GeneratedValue(generator = "inquisitive-uuid")
	@GenericGenerator(name = "inquisitive-uuid", strategy = "com.trigyn.jws.dbutils.configurations.CustomUUIDGenerator")
	@Column(name = "mail_schedule_id")
	private String				mailScheduleId		= null;

	@Column(name = "email_xml")
	private String				emailXml			= null;

	@Column(name = "mail_schedule_group_id")
	private String				mailScheduleGroupId	= null;

	public String getMailScheduleId() {
		return mailScheduleId;
	}

	public void setMailScheduleId(String mailScheduleId) {
		this.mailScheduleId = mailScheduleId;
	}

	public String getEmailXml() {
		return emailXml;
	}

	public void setEmailXml(String emailXml) {
		this.emailXml = emailXml;
	}

	public String getMailScheduleGroupId() {
		return mailScheduleGroupId;
	}

	public void setMailScheduleGroupId(String mailScheduleGroupId) {
		this.mailScheduleGroupId = mailScheduleGroupId;
	}

}
