package com.trigyn.jws.webstarter.utils;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import javax.mail.internet.InternetAddress;

import org.springframework.stereotype.Component;


@Component
public class Email implements Serializable {
	private static final long	serialVersionUID	= 1L;



	private String				mailFrom;

	private String              mailFromName;
	
	private InternetAddress	[]  internetAddressToArray;

	private String				body;

	private String				subject;

	private String				mailContent;

	private String				contentType;
	
	private Boolean             isReplyToDifferentMail;
	
	private InternetAddress     replyToDifferentMailId;
	
	private InternetAddress []  internetAddressCCArray; 
	
	private InternetAddress []  internetAddressBCCArray; 
	
	private Boolean             isMailFooterEnabled;
	
	private String              mailFooter;
	
	private List<File>    attachementsArray;
	

	public Email() {
		contentType = "text/plain";
	}


	public String getMailFrom() {
		return mailFrom;
	}


	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}


	public String getMailFromName() {
		return mailFromName;
	}


	public void setMailFromName(String mailFromName) {
		this.mailFromName = mailFromName;
	}


	public InternetAddress[] getInternetAddressToArray() {
		return internetAddressToArray;
	}


	public void setInternetAddressToArray(InternetAddress[] internetAddressToArray) {
		this.internetAddressToArray = internetAddressToArray;
	}


	public String getBody() {
		return body;
	}


	public void setBody(String body) {
		this.body = body;
	}


	public String getSubject() {
		return subject;
	}


	public void setSubject(String subject) {
		this.subject = subject;
	}


	public String getMailContent() {
		return mailContent;
	}


	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}


	public String getContentType() {
		return contentType;
	}


	public void setContentType(String contentType) {
		this.contentType = contentType;
	}


	public Boolean getIsReplyToDifferentMail() {
		return isReplyToDifferentMail;
	}


	public void setIsReplyToDifferentMail(Boolean isReplyToDifferentMail) {
		this.isReplyToDifferentMail = isReplyToDifferentMail;
	}


	public InternetAddress getReplyToDifferentMailId() {
		return replyToDifferentMailId;
	}


	public void setReplyToDifferentMailId(InternetAddress replyToDifferentMailId) {
		this.replyToDifferentMailId = replyToDifferentMailId;
	}


	public InternetAddress[] getInternetAddressCCArray() {
		return internetAddressCCArray;
	}


	public void setInternetAddressCCArray(InternetAddress[] internetAddressCCArray) {
		this.internetAddressCCArray = internetAddressCCArray;
	}


	public InternetAddress[] getInternetAddressBCCArray() {
		return internetAddressBCCArray;
	}


	public void setInternetAddressBCCArray(InternetAddress[] internetAddressBCCArray) {
		this.internetAddressBCCArray = internetAddressBCCArray;
	}


	public Boolean getIsMailFooterEnabled() {
		return isMailFooterEnabled;
	}


	public void setIsMailFooterEnabled(Boolean isMailFooterEnabled) {
		this.isMailFooterEnabled = isMailFooterEnabled;
	}


	public String getMailFooter() {
		return mailFooter;
	}


	public void setMailFooter(String mailFooter) {
		this.mailFooter = mailFooter;
	}


	public List<File> getAttachementsArray() {
		return attachementsArray;
	}


	public void setAttachementsArray(List<File> attachementsArray) {
		this.attachementsArray = attachementsArray;
	}




	
}
