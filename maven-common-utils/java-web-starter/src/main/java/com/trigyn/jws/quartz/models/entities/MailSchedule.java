package com.trigyn.jws.quartz.models.entities;

import java.io.Serializable;

import com.trigyn.jws.dbutils.configurations.UUIDEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@EntityListeners(value = { UUIDEntityListener.class })
@Table(name = "mail_schedule")
public class MailSchedule implements Serializable {

	private static final long	serialVersionUID	= 6321323265487281667L;

	@Id
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
