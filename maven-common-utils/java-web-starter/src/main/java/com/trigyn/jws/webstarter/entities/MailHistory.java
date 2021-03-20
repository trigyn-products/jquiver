package com.trigyn.jws.webstarter.entities;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "jq_failed_mail_history")
public class MailHistory {

	@Id
	@Column(name = "failed_mail_id")
	private String		failedMailId	= null;

	@Column(name = "mail_sent_by")
	private String		mailSentBy		= null;

	@Column(name = "mail_sent_to")
	private String		mailSentTo		= null;

	@Column(name = "eml_file_path")
	private String		emlFilePath		= null;

	@Column(name = "mail_failed_time", nullable = false)
	private Calendar	mailFailedTime;

	public String getFailedMailId() {
		return failedMailId;
	}

	public void setFailedMailId(String failedMailId) {
		this.failedMailId = failedMailId;
	}

	public String getMailSentBy() {
		return mailSentBy;
	}

	public void setMailSentBy(String mailSentBy) {
		this.mailSentBy = mailSentBy;
	}

	public String getMailSentTo() {
		return mailSentTo;
	}

	public void setMailSentTo(String mailSentTo) {
		this.mailSentTo = mailSentTo;
	}

	public String getEmlFilePath() {
		return emlFilePath;
	}

	public void setEmlFilePath(String emlFilePath) {
		this.emlFilePath = emlFilePath;
	}

	public Calendar getMailFaliedTime() {
		return mailFailedTime;
	}

	public void setMailFaliedTime(Calendar mailFaliedTime) {
		this.mailFailedTime = mailFaliedTime;
	}

}
