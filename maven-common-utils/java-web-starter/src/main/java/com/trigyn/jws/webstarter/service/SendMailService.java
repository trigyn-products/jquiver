package com.trigyn.jws.webstarter.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dbutils.utils.Constant;
import com.trigyn.jws.templating.service.DBTemplatingService;
import com.trigyn.jws.templating.vo.TemplateVO;
import com.trigyn.jws.webstarter.dao.SendMailDAO;
import com.trigyn.jws.webstarter.entities.MailHistory;
import com.trigyn.jws.webstarter.utils.Email;

@Service
@Transactional
public class SendMailService {

	@Autowired
	private DBTemplatingService dbTemplatingService = null;

	@Autowired
	private PropertyMasterService propertyMasterService = null;

	@Autowired
	private SendMailDAO sendMailDao = null;

	@Async
	public CompletableFuture<Boolean> sendTestMail(Email mail) throws Exception {

		String jsonString = propertyMasterService.findPropertyMasterValue("mail-configuration");
		Map<String, String> mailMap = new Gson().fromJson(jsonString, new TypeToken<Map<String, String>>() {
		}.getType());
		Authenticator authenticator = null;
		Session session = null;
		Properties prop = new Properties();
		String stmpPort = mailMap.get("smtpPort");
		String smtpSecurityProtocal = mailMap.get("smtpSecurityProtocal");
		prop.setProperty("mail.smtp.host", mailMap.get("smtpHost"));
		prop.setProperty("mail.smtp.port", stmpPort);

		if (smtpSecurityProtocal != null && smtpSecurityProtocal != "") {
			if (smtpSecurityProtocal.equals(Constant.SSL)) {
				prop.setProperty("mail.smtp.socketFactory.port", stmpPort);
				prop.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
				prop.setProperty("mail.smtp.socketFactory.fallback", "true");
				prop.setProperty("mail.smtp.ssl.trust", "*");
			} else if (smtpSecurityProtocal.equals(Constant.TLS)) {
				prop.setProperty("mail.smtp.starttls.enable", "true");
			}
		}

		String smtpAuth = mailMap.get("isAuthenticated");

		if (smtpAuth != null && smtpAuth != "" && smtpAuth.equals("true")) {
			prop.put("mail.smtp.auth", Boolean.TRUE);
			prop.setProperty("mail.smtp.user", mailMap.get("userName"));
			prop.setProperty("mail.smtp.password", mailMap.get("password"));
			class SMTPAuthenticator extends javax.mail.Authenticator {
				String username = "";
				String password = "";

				protected SMTPAuthenticator(String username, String password) {
					this.username = username;
					this.password = password;
				}

				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			}

			authenticator = new SMTPAuthenticator(mailMap.get("userName"), mailMap.get("password"));
			session = Session.getInstance(prop, authenticator);
		} else {
			session = Session.getDefaultInstance(prop, null);
			prop.put("mail.smtp.auth", Boolean.FALSE);
		}

		Boolean isSent = true;
		MimeMessage message = new MimeMessage(session);
		MimeMultipart mimeMultipart = new MimeMultipart();
		BodyPart messageBodyPart = new MimeBodyPart();
		String mailBody = "";
		String fromMailId = mail.getMailFrom();
		String fromMailName = mail.getMailFromName();
		Boolean isReplyToDifferentMail = mail.getIsReplyToDifferentMail();
		InternetAddress replyToDifferentEmailId = mail.getReplyToDifferentMailId();
		InternetAddress[] mailToList = mail.getInternetAddressToArray();
		InternetAddress[] ccList = mail.getInternetAddressCCArray();
		InternetAddress[] bccList = mail.getInternetAddressBCCArray();
		Boolean isMailFooterRequired = mail.getIsMailFooterEnabled();
		String mailFooter = mail.getMailFooter();
		try {

			if (fromMailId != null && fromMailId != "" && isValidEmailAddressFromStringValidation(fromMailId)) {
				message.setFrom(new InternetAddress(fromMailId, fromMailName));
			} else {
				message.setFrom("fromMailId@notFound.com");
				throw new Exception("Invalid/No From Emailid entered" + fromMailId);
			}

			if (isReplyToDifferentMail != null && isReplyToDifferentMail.equals(Boolean.TRUE)) {
				if (isValidEmailInternetAddress(replyToDifferentEmailId)) {
					message.setReplyTo(new javax.mail.Address[] { replyToDifferentEmailId });
				} else {
					// Added below hard coded mail id if users entered invalid email address
					message.setReplyTo(InternetAddress.parse("inVallidOrNoREPLYTOMailId@required.com"));
					throw new Exception("Invalid/No Reply to  Emailid entered " + replyToDifferentEmailId);
				}
			}

			if (mailToList != null) {
				message.addRecipients(Message.RecipientType.TO, mailToList);
			} else {
				// mail to at least one mail id required
				message.addRecipients(Message.RecipientType.TO, "atleastOneTOMailId@required.com");
				throw new Exception("To mail id missing  ");
			}

			if (ccList != null) {
				message.addRecipients(Message.RecipientType.CC, ccList);
			}

			if (bccList != null) {
				message.addRecipients(Message.RecipientType.BCC, bccList);
			}

			if (mail.getSubject() != null && mail.getSubject() != "") {
				message.setSubject(mail.getSubject());
			}

			if (mail.getBody() != null && mail.getBody() != "") {
				mailBody = mail.getBody();
			}

			if (isMailFooterRequired != null && isMailFooterRequired.equals(Boolean.TRUE) && mailFooter != "") {
				mailBody = mailBody + "	<br>" + mail.getMailFooter();
			}
			messageBodyPart.setContent(mailBody, "text/html; charset=utf-8");
			mimeMultipart.addBodyPart(messageBodyPart);

			if (mail.getAttachementsArray() != null && !mail.getAttachementsArray().isEmpty()) {
				for (File attachedFile : mail.getAttachementsArray()) {
					BodyPart atachmentBodyPart = new MimeBodyPart();
					DataSource source = new FileDataSource(attachedFile);
					atachmentBodyPart.setDataHandler(new DataHandler(source));
					atachmentBodyPart.setFileName(attachedFile.getName());
					mimeMultipart.addBodyPart(atachmentBodyPart);
				}
			}

			message.setSentDate(new Date());
			message.setContent(mimeMultipart);

			Transport.send(message);

		} catch (Exception e) {
			isSent = false;
			e.printStackTrace();
			if (mailBody != "") {
				messageBodyPart.setContent(mailBody, "text/html; charset=utf-8");
			} else if (mail.getBody() != null && mail.getBody() != "") {
				messageBodyPart.setContent(mail.getBody(), "text/html; charset=utf-8");
			} else {
				messageBodyPart.setContent("", "text/html; charset=utf-8");
			}
			mimeMultipart.addBodyPart(messageBodyPart);
			message.setContent(mimeMultipart);
			try {
				String emailEMLFile = UUID.randomUUID().toString();
				// Have to read path from property file
				String filePath = "d:/" + emailEMLFile + "_Mail.eml";
				message.writeTo(new FileOutputStream(new File(filePath)));

				MailHistory mailHistory = new MailHistory();
				String uuidGenerated = UUID.randomUUID().toString();
				mailHistory.setFailedMailId(uuidGenerated);
				if (fromMailId == null) {
					// if no from email id entered exception occured saving database with below mail
					// id.
					mailHistory.setMailSentBy("fromMailId@notFound.com");
				} else {
					mailHistory.setMailSentBy(fromMailId.toString());
				}

				if (mailToList == null) {
					mailHistory.setMailSentTo("atleastOneTOMailId@required.com");
				} else {
					mailHistory.setMailSentTo(mailToList.toString());
				}

				mailHistory.setMailFaliedTime(Calendar.getInstance());
				mailHistory.setEmlFilePath(filePath);

				sendMailDao.saveFailedMails(mailHistory);

			} catch (IOException | MessagingException e1) {
				isSent = false;
				e1.printStackTrace();

			}
		}

		return CompletableFuture.completedFuture(isSent);
	}

	public static boolean isValidEmailAddressFromStringValidation(String email) {
		boolean result = true;
		try {
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
		} catch (AddressException ex) {
			result = false;
		}
		return result;
	}

	private static boolean isValidEmailInternetAddress(InternetAddress emailAddr) {
		boolean result = true;
		try {
			emailAddr.validate();
		} catch (AddressException ex) {
			result = false;
		}
		return result;
	}

	// Email and with json string of properties send test mail call
	@Async
	public CompletableFuture<Boolean> sendTestMail(Email mail, String jsonString) throws Exception {

		Map<String, String> mailMap = new Gson().fromJson(jsonString, new TypeToken<Map<String, String>>() {
		}.getType());
		Authenticator authenticator = null;
		Session session = null;
		Properties prop = new Properties();
		String stmpPort = mailMap.get("smtpPort");
		String smtpSecurityProtocal = mailMap.get("smtpSecurityProtocal");
		prop.setProperty("mail.smtp.host", mailMap.get("smtpHost"));
		prop.setProperty("mail.smtp.port", stmpPort);

		if (smtpSecurityProtocal != null && smtpSecurityProtocal != "") {
			if (smtpSecurityProtocal.equals(Constant.SSL)) {
				prop.setProperty("mail.smtp.socketFactory.port", stmpPort);
				prop.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
				prop.setProperty("mail.smtp.socketFactory.fallback", "true");
				prop.setProperty("mail.smtp.ssl.trust", "*");
			} else if (smtpSecurityProtocal.equals(Constant.TLS)) {
				prop.setProperty("mail.smtp.starttls.enable", "true");
			}
		}

		String smtpAuth = mailMap.get("isAuthenticated");

		if (smtpAuth != null && smtpAuth != "" && smtpAuth.equals("true")) {
			prop.put("mail.smtp.auth", Boolean.TRUE);
			prop.setProperty("mail.smtp.user", mailMap.get("userName"));
			prop.setProperty("mail.smtp.password", mailMap.get("password"));
			class SMTPAuthenticator extends javax.mail.Authenticator {
				String username = "";
				String password = "";

				protected SMTPAuthenticator(String username, String password) {
					this.username = username;
					this.password = password;
				}

				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			}

			authenticator = new SMTPAuthenticator(mailMap.get("userName"), mailMap.get("password"));
			session = Session.getInstance(prop, authenticator);
		} else {
			session = Session.getDefaultInstance(prop, null);
			prop.put("mail.smtp.auth", Boolean.FALSE);
		}

		Boolean isSent = true;
		MimeMessage message = new MimeMessage(session);
		MimeMultipart mimeMultipart = new MimeMultipart();
		BodyPart messageBodyPart = new MimeBodyPart();
		String mailBody = "";
		String fromMailId = (mailMap.get("mailFrom") != null && mailMap.get("mailFrom") != "") ? mailMap.get("mailFrom")
				: mail.getMailFrom();
		String fromMailName = (mailMap.get("mailFromName") != null && mailMap.get("mailFromName") != "")
				? mailMap.get("mailFromName")
				: mail.getMailFromName();
		String isReply = mailMap.get("isReplyToDifferentMail");
		Boolean isReplyToDifferentMail = (isReply != null && !isReply.isEmpty()) ? Boolean.parseBoolean(isReply)
				: false;

		if (!isReplyToDifferentMail && mail.getIsReplyToDifferentMail() != null) {
			isReplyToDifferentMail = mail.getIsReplyToDifferentMail();
		}

		String replyEmailAddress = (mailMap.get("replyToDifferentMailId") != null
				&& !mailMap.get("replyToDifferentMailId").isEmpty()) ? mailMap.get("replyToDifferentMailId") : "";
		InternetAddress replyToDifferentEmailId = null;
		if (isReplyToDifferentMail && replyEmailAddress != "") {
			replyToDifferentEmailId = (new InternetAddress(replyEmailAddress));
		} else {
			replyToDifferentEmailId = (mail.getReplyToDifferentMailId());
		}

		InternetAddress[] mailToList = null;
		if (mailMap.get("internetAddressToArray") != "" && mailMap.get("internetAddressToArray") != null) {
			mailToList = InternetAddress.parse(mailMap.get("internetAddressToArray"));
		} else {
			mailToList = mail.getInternetAddressToArray();
		}
		InternetAddress[] ccList = null;

		if (mailMap.get("internetAddressCCArray") != null && mailMap.get("internetAddressCCArray") != "") {
			ccList = InternetAddress.parse(mailMap.get("internetAddressCCArray"));
		} else {
			ccList = mail.getInternetAddressCCArray();
		}
		InternetAddress[] bccList = null;
		if (mailMap.get("internetAddressBCCArray") != null && mailMap.get("internetAddressBCCArray") != "") {
			bccList = InternetAddress.parse(mailMap.get("internetAddressBCCArray"));
		} else {
			bccList = mail.getInternetAddressBCCArray();
		}

		Boolean isMailFooterRequired;

		String footerExists = mailMap.get("isMailFooterEnabled");
		isMailFooterRequired = (footerExists != null && !footerExists.isEmpty()) ? Boolean.parseBoolean(footerExists)
				: mail.getIsMailFooterEnabled();
		mail.setIsMailFooterEnabled(isMailFooterRequired);

		String mailFooterString = (mailMap.get("mailFooter") != null && !mailMap.get("mailFooter").isEmpty())
				? mailMap.get("mailFooter")
				: mail.getMailFooter();

		if (isMailFooterRequired && mailFooterString != "") {
			mail.setMailFooter(mailFooterString);
		}

		try {

			if (fromMailId != null && fromMailId != "" && isValidEmailAddressFromStringValidation(fromMailId)) {
				message.setFrom(new InternetAddress(fromMailId, fromMailName));
			} else {
				throw new Exception("Invalid/No From Emailid entered" + fromMailId);
			}

			if (isReplyToDifferentMail != null && isReplyToDifferentMail.equals(Boolean.TRUE)) {
				if (replyToDifferentEmailId != null && isValidEmailInternetAddress((replyToDifferentEmailId))) {
					message.setReplyTo(new javax.mail.Address[] { replyToDifferentEmailId });
				} else {
					throw new Exception("Invalid/No Reply to  Emailid entered " + replyToDifferentEmailId);
				}
			}

			if (mailToList != null) {
				message.addRecipients(Message.RecipientType.TO, mailToList);
			} else {
				throw new Exception("To mail id missing  ");
			}

			if (ccList != null) {
				message.addRecipients(Message.RecipientType.CC, ccList);
			}

			if (bccList != null) {
				message.addRecipients(Message.RecipientType.BCC, bccList);
			}

			if (mail.getSubject() != null && mail.getSubject() != "") {
				message.setSubject(mail.getSubject());
			}

			if (mail.getBody() != null && mail.getBody() != "") {
				mailBody = mail.getBody();
			}

			if (mail.getIsMailFooterEnabled() != null && mail.getIsMailFooterEnabled().equals(Boolean.TRUE)) {
				mailBody = mailBody + "	<br>" + mail.getMailFooter();
			}
			messageBodyPart.setContent(mailBody, "text/html; charset=utf-8");
			mimeMultipart.addBodyPart(messageBodyPart);

			if (mail.getAttachementsArray() != null && !mail.getAttachementsArray().isEmpty()) {
				for (File attachedFile : mail.getAttachementsArray()) {
					BodyPart atachmentBodyPart = new MimeBodyPart();
					DataSource source = new FileDataSource(attachedFile);
					atachmentBodyPart.setDataHandler(new DataHandler(source));
					atachmentBodyPart.setFileName(attachedFile.getName());
					mimeMultipart.addBodyPart(atachmentBodyPart);
				}
			}

			message.setSentDate(new Date());
			message.setContent(mimeMultipart);

			Transport.send(message);

		} catch (Exception e) {
			isSent = false;
			e.printStackTrace();
			if (mailBody != "") {
				messageBodyPart.setContent(mailBody, "text/html; charset=utf-8");
			} else if (mail.getBody() != null && mail.getBody() != "") {
				messageBodyPart.setContent(mail.getBody(), "text/html; charset=utf-8");
			} else {
				messageBodyPart.setContent("", "text/html; charset=utf-8");
			}
			mimeMultipart.addBodyPart(messageBodyPart);
			message.setContent(mimeMultipart);

			try {
				String emailEMLFile = UUID.randomUUID().toString();
				// Have to read path from propert file
				String filePath = "d:/" + emailEMLFile + "_Mail.eml";
				message.writeTo(new FileOutputStream(new File(filePath)));

				MailHistory mailHistory = new MailHistory();
				String uuidGenerated = UUID.randomUUID().toString();
				mailHistory.setFailedMailId(uuidGenerated);
				if (fromMailId == null) {
					mailHistory.setMailSentBy("fromMailId@notFound.com");
				} else {
					mailHistory.setMailSentBy(fromMailId.toString());
				}

				if (mailToList == null) {
					mailHistory.setMailSentTo("atleastOneTOMailId@required.com");
				} else {
					mailHistory.setMailSentTo(mailToList.toString());
				}

				mailHistory.setMailFaliedTime(Calendar.getInstance());
				mailHistory.setEmlFilePath(filePath);

				sendMailDao.saveFailedMails(mailHistory);

			} catch (IOException | MessagingException e1) {
				isSent = false;
				e1.printStackTrace();
			}
		}

		return CompletableFuture.completedFuture(isSent);
	}

}
