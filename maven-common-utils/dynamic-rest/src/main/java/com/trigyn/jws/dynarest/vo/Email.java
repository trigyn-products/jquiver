package com.trigyn.jws.dynarest.vo;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.mail.internet.InternetAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.usermanagement.security.config.ApplicationSecurityDetails;

@Component
public class Email implements Serializable {
	private static final long serialVersionUID = 1L;

	private InternetAddress[] mailFrom;

	private String mailFromName;

	private InternetAddress[] internetAddressToArray;

	private String body;

	private String subject;

	private String mailContent;

	private String contentType;

	private Boolean isReplyToDifferentMail;

	private InternetAddress replyToDifferentMailId;

	private InternetAddress[] internetAddressCCArray;

	private InternetAddress[] internetAddressBCCArray;

	private Boolean isMailFooterEnabled;

	private String mailFooter;

	private List<EmailAttachedFile> attachementsArray;

	private Boolean separatemails;

	private Boolean isAuthenticationEnabled;

	private List<String> loggedInUserRole = null;

	private InternetAddress[] failedrecipient;

	public Email() {
		contentType = "text/plain";
	}

	public Boolean getIsAuthenticationEnabled() {
		return isAuthenticationEnabled;
	}

	public void setIsAuthenticationEnabled(Boolean isAuthenticationEnabled) {
		this.isAuthenticationEnabled = isAuthenticationEnabled;
	}

	public List<String> getLoggedInUserRole() {
		return loggedInUserRole;
	}

	public void setLoggedInUserRole(List<String> loggedInUserRole) {
		this.loggedInUserRole = loggedInUserRole;
	}

	public InternetAddress[] getMailFrom() {
		return mailFrom;
	}

	public void setMailFrom(InternetAddress[] mailFrom) {
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

	public List<EmailAttachedFile> getAttachementsArray() {
		return attachementsArray;
	}

	public void setAttachementsArray(List<EmailAttachedFile> attachementsArray) {
		this.attachementsArray = attachementsArray;
	}

	public Boolean getSeparatemails() {
		return separatemails;
	}

	public void setSeparatemails(Boolean separatemails) {
		this.separatemails = separatemails;
	}

	public InternetAddress[] getFailedrecipient() {
		return failedrecipient;
	}

	public void setFailedrecipient(InternetAddress[] internetAddresses) {
		this.failedrecipient = internetAddresses;
	}

}
