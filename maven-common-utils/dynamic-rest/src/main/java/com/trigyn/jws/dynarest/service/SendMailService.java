package com.trigyn.jws.dynarest.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dbutils.utils.Constant;
import com.trigyn.jws.dbutils.vo.FileInfo;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.dynarest.cipher.utils.ScriptUtil;
import com.trigyn.jws.dynarest.dao.SendMailDAO;
import com.trigyn.jws.dynarest.entities.FileUpload;
import com.trigyn.jws.dynarest.entities.MailHistory;
import com.trigyn.jws.dynarest.repository.FileUploadRepository;
import com.trigyn.jws.dynarest.utils.Constants;
import com.trigyn.jws.dynarest.vo.Email;
import com.trigyn.jws.dynarest.vo.EmailAttachedFile;

@Service
@Transactional
public class SendMailService {

	private final static Logger logger = LogManager.getLogger(SendMailService.class);

	@Autowired
	private PropertyMasterService propertyMasterService = null;

	@Autowired
	private SendMailDAO sendMailDao = null;

	@Autowired
	private ScriptUtil scriptUtil = null;

	@Autowired
	@Qualifier("file-system-storage")
	private FilesStorageService storageService = null;

	@Autowired
	private FileUploadRepository fileUploadRepository = null;

	@Async
	public CompletableFuture<Boolean> sendTestMail(Email mail) throws Exception {

		String jsonString = propertyMasterService.findPropertyMasterValue("mail-configuration");
		Map<String, Object> mailMap = new Gson().fromJson(jsonString, new TypeToken<Map<String, Object>>() {
		}.getType());
		Authenticator authenticator = null;
		Session session = null;
		Properties prop = new Properties();
		String stmpPort = (String) mailMap.get("smtpPort");
		String smtpSecurityProtocal = (String) mailMap.get("smtpSecurityProtocal");
		prop.setProperty("mail.smtp.host", (String) mailMap.get("smtpHost"));
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

		Boolean smtpAuth = mailMap.get("isAuthenticated") != null ? (Boolean) mailMap.get("isAuthenticated") : null;

		if (smtpAuth != null && Boolean.TRUE.equals(smtpAuth)) {
			prop.put("mail.smtp.auth", smtpAuth);
			prop.setProperty("mail.smtp.user", (String) mailMap.get("userName"));
			prop.setProperty("mail.smtp.password", (String) mailMap.get("password"));
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

			authenticator = new SMTPAuthenticator((String) mailMap.get("userName"), (String) mailMap.get("password"));
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
		String fromMailId = (mail.getMailFrom() != null && mail.getMailFrom()[0].getAddress() != null)
				? mail.getMailFrom()[0].getAddress()
				: (String) mailMap.get("mailFrom");

		String fromMailName = StringUtils.isBlank(mail.getMailFromName()) == true ? mail.getMailFromName()
				: (String) mailMap.get("mailFromName");

		Boolean isReplyToDifferentMail = mail.getIsReplyToDifferentMail() == null
				? (Boolean) mailMap.get("isReplyToDifferentMail")
				: mail.getIsReplyToDifferentMail();
		/** Added for ReplyTo tag */
		String replyTo = (mail.getReplyToaddress() != null && mail.getReplyToaddress()[0].getAddress() != null)
				? mail.getReplyToaddress()[0].getAddress()
				: (String) mailMap.get("replyToDifferentMailId");
		/** Ends Here */

		Boolean separatemails = mail.getSeparatemails() == null ? false : mail.getSeparatemails();

		InternetAddress[] mailToList = mail.getInternetAddressToArray() == null
				? (InternetAddress[]) mailMap.get("internetAddressToArray")
				: mail.getInternetAddressToArray();

		String noReplyToMailId = (mailMap.get("noReplyToMailId") != null
				&& !mailMap.get("noReplyToMailId").toString().isEmpty()) ? mailMap.get("noReplyToMailId").toString()
						: "";

		InternetAddress replyToDifferentEmailId = null;
		if (mail.getMailFrom() != null && mail.getMailFrom()[0] != null) {
			replyToDifferentEmailId = mail.getMailFrom()[0];
		} else if (mail.getReplyToDifferentMailId() != null) {
			replyToDifferentEmailId = mail.getReplyToDifferentMailId();
		} else if (mailMap.get("replyToDifferentMailId") != null) {
			replyToDifferentEmailId = InternetAddress.parse((String) mailMap.get("replyToDifferentMailId"))[0];
		}

		InternetAddress[] ccList = mail.getInternetAddressCCArray();
		InternetAddress[] bccList = mail.getInternetAddressBCCArray();

		/** Added for appending Mail Footer */
		String mailFooterString = (mailMap.get("mailFooter") != null && !mailMap.get("mailFooter").equals("")
				? (String) mailMap.get("mailFooter")
				: mail.getMailFooter());
		if (mailFooterString != null && mailFooterString.isEmpty() == false) {
			mail.setMailFooter(mailFooterString);
		}
		/** Ends Here */

		try {

			if (fromMailId != null && fromMailId != "" && isValidEmailAddressFromStringValidation(fromMailId)) {
				message.setFrom(new InternetAddress(fromMailId, fromMailName));
			} else {
				message.setFrom("fromMailId@notFound.com");
				throw new Exception("Invalid/No From Emailid entered" + fromMailId);
			}

			/** Added for replyTo */
			if (replyTo != null && replyTo != "" && isValidEmailAddressFromStringValidation(replyTo)) {
				message.setReplyTo(InternetAddress.parse(replyTo));
				InternetAddress replyToIA = InternetAddress.parse(replyTo)[0];
				message.setReplyTo(new javax.mail.Address[] { replyToIA });// Ends Here
			} else if (isReplyToDifferentMail != null && isReplyToDifferentMail.equals(Boolean.TRUE)) {
				if (isValidEmailInternetAddress(replyToDifferentEmailId)) {
					message.setReplyTo(new javax.mail.Address[] { replyToDifferentEmailId });
				} else {
					// Added below hard coded mail id if users entered invalid email address
					message.setReplyTo(InternetAddress.parse("inVallidOrNoREPLYTOMailId@required.com"));
					throw new Exception("Invalid/No Reply to  Emailid entered " + replyToDifferentEmailId);
				}
			} else {
				if (replyToDifferentEmailId != null && replyToDifferentEmailId.toString().equals(fromMailId)) {
					message.setReplyTo(new javax.mail.Address[] { replyToDifferentEmailId });
				} else {
					message.setReplyTo(
							new javax.mail.Address[] { new javax.mail.internet.InternetAddress("noreply@trigyn.com") });
				}
			}

			if (!separatemails) {
				if (mailToList != null && separatemails == false) {
					message.addRecipients(Message.RecipientType.TO, mailToList);
				} else {
					// mail to at least one mail id required
					message.addRecipients(Message.RecipientType.TO, noReplyToMailId);
					throw new Exception("To mail id missing  ");
				}
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

			messageBodyPart.setContent(mailBody, "text/html; charset=utf-8");
			mimeMultipart.addBodyPart(messageBodyPart);

			/** Added for File Bin Attachment in Mail */
			List<FileUpload> fileUploads = fileUploadRepository.findAllByFileBinId("mailAttachment");
			File attachedFile = null;
			for (FileUpload fu : fileUploads) {
				String fileUploadId = fu.getFileUploadId();
				Integer isAllowed = storageService.hasPermission(null, null, fileUploadId,
						Constants.VIEW_FILE_VALIDATOR, new HashMap<>());
				if (isAllowed > 0) {
					Map<String, Object> fileInfo = storageService.load(fileUploadId);
					if (fileInfo != null) {
						byte[] fileByte = (byte[]) fileInfo.get("file");
						attachedFile = new File(fu.getOriginalFileName());
						try (FileOutputStream fos = new FileOutputStream(attachedFile)) {
							fos.write(fileByte);
						} catch (Exception exception) {
							logger.error("Error occurred while accessing file", exception);
						}
						BodyPart atachmentBodyPart = new MimeBodyPart();
						DataSource source = new FileDataSource(attachedFile);
						atachmentBodyPart.setDataHandler(new DataHandler(source));
						atachmentBodyPart.setFileName(fu.getOriginalFileName());
						mimeMultipart.addBodyPart(atachmentBodyPart);
						if (mailFooterString != null && mailFooterString.isEmpty() == false) {
							if (mail.getMailFooter().contains("cid")) {
								String footer = mail.getMailFooter();
								String[] strArray = footer.split("cid:");
								for (int iCounter = 0; iCounter < strArray.length; iCounter++) {
									if (strArray[iCounter].contains("\"")) {
										String[] str = strArray[iCounter].split("\"");
										if (str[0].equalsIgnoreCase(fu.getOriginalFileName())) {
											atachmentBodyPart.setDisposition(MimeBodyPart.INLINE);
										}
									}
								}
							}
						}

					} else {
						throw new Exception("Could not read the file with name - " + fu.getOriginalFileName()
								+ " : fileBinId : " + fu.getFileBinId() + " : fileUploadId : " + fu.getFileUploadId());
					}
				}
			}
			/** Added for Mail Footer */
			if (mailFooterString != null && mailFooterString.isEmpty() == false) {
				if (mail.getMailFooter().contains("cid")) {
					mailBody = mailBody + "	<br>" + mail.getMailFooter();
					messageBodyPart.setContent(mailBody, "text/html; charset=utf-8");
				} else {
					mailBody = mailBody + "	<br>" + mail.getMailFooter();
					messageBodyPart.setContent(mailBody, "text/html; charset=utf-8");
				}
			}
			/** Ends Here */

			message.setSentDate(new Date());
			message.setContent(mimeMultipart);

			if (separatemails == false) {
				Transport.send(message);
			} else if (mail.getSeparatemails()) {
				for (int iRecepientCounter = 0; iRecepientCounter < mailToList.length; iRecepientCounter++) {
					String strMailTo = mailToList[iRecepientCounter].getAddress();
					InternetAddress[] address = { new InternetAddress(strMailTo) };
					message.setRecipients(Message.RecipientType.TO, address);
					Transport.send(message);
				}
			}

		} catch (Exception a_excep) {
			isSent = false;
			logger.error("Error ocurred.", a_excep);
			if (mailBody != "") {
				messageBodyPart.setContent(mailBody, "text/html; charset=utf-8");
			} else if (mail.getBody() != null && mail.getBody() != "") {
				messageBodyPart.setContent(mail.getBody(), "text/html; charset=utf-8");
			} else {
				messageBodyPart.setContent("", "text/html; charset=utf-8");
			}
			mimeMultipart.addBodyPart(messageBodyPart);
			message.setContent(mimeMultipart);
			/* Method called for configuring failed mail notification */
			failedRecipient(mail, message);
			try {
				String emailEMLFile = UUID.randomUUID().toString();
				String emlFileStoragePath = propertyMasterService.findPropertyMasterValue("emlFileStoragePath");
				if (emlFileStoragePath == null || (emlFileStoragePath != null && emlFileStoragePath.isEmpty())) {
					emlFileStoragePath = System.getProperty("java.io.tmpdir");
				}

				File emFile = new File(emlFileStoragePath);
				if (emFile.exists() == false) {
					emlFileStoragePath = System.getProperty("java.io.tmpdir");
				}
				emlFileStoragePath = emlFileStoragePath + emailEMLFile + "_Mail.eml";
				emFile = new File(emlFileStoragePath);

				message.writeTo(new FileOutputStream(emFile));

				MailHistory mailHistory = new MailHistory();
				String uuidGenerated = UUID.randomUUID().toString();
				mailHistory.setFailedMailId(uuidGenerated);
				if (fromMailId.length() == 0) {
					// if no from email id entered exception occured saving database with below mail
					// id.
					mailHistory.setMailSentBy("fromMailId@notFound.com");
				} else {
					mailHistory.setMailSentBy(fromMailId.toString());
				}

				if (mailToList.length == 0) {
					mailHistory.setMailSentTo("atleastOneTOMailId@required.com");
				} else {
					String mailStr = "";
					for (int i = 0; i < mailToList.length; i++) {
						mailStr += mailToList[i].getAddress();
					}
					mailHistory.setMailSentTo(mailStr);
				}

				mailHistory.setMailFaliedTime(Calendar.getInstance());
				mailHistory.setEmlFilePath(emlFileStoragePath);

				sendMailDao.saveFailedMails(mailHistory);

			} catch (IOException | MessagingException a_exc) {
				isSent = false;
				logger.error("Error ocurred.", a_exc);

			}
		}

		return CompletableFuture.completedFuture(isSent);
	}

	public static boolean isValidEmailAddressFromStringValidation(String email) {
		boolean result = true;
		try {
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
		} catch (AddressException a_exc) {
			logger.error("Error ocurred.", a_exc);
			result = false;
		}
		return result;
	}

	private static boolean isValidEmailInternetAddress(InternetAddress emailAddr) {
		boolean result = true;
		try {
			emailAddr.validate();
		} catch (AddressException a_exc) {
			logger.error("Error ocurred.", a_exc);
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
		String fromMailId = (mail.getMailFrom() != null && mail.getMailFrom()[0].getAddress() != null)
				? mail.getMailFrom()[0].getAddress()
				: (String) mailMap.get("mailFrom");
		String fromMailName = (mailMap.get("mailFromName") != null && mailMap.get("mailFromName") != "")
				? mail.getMailFromName()
				: mailMap.get("mailFromName");
		String isReply = mailMap.get("isReplyToDifferentMail");
		Boolean isReplyToDifferentMail = (isReply != null && !isReply.isEmpty()) ? Boolean.parseBoolean(isReply)
				: false;
		Boolean separatemails = mail.getSeparatemails() == null ? false : mail.getSeparatemails();

		if (!isReplyToDifferentMail && mail.getIsReplyToDifferentMail() != null) {
			isReplyToDifferentMail = mail.getIsReplyToDifferentMail();
		}

		/** Added for ReplyTo tag */
		String replyTo = (mail.getReplyToaddress() != null && mail.getReplyToaddress()[0].getAddress() != null)
				? mail.getReplyToaddress()[0].getAddress()
				: (String) mailMap.get("replyToDifferentMailId");
		/** Ends Here */

		String replyEmailAddress = (mailMap.get("replyToDifferentMailId") != null
				&& !mailMap.get("replyToDifferentMailId").isEmpty()) ? mailMap.get("replyToDifferentMailId") : "";

		String noReplyToMailId = (mailMap.get("noReplyToMailId") != null && !mailMap.get("noReplyToMailId").isEmpty())
				? mailMap.get("noReplyToMailId")
				: "";

		InternetAddress replyToDifferentEmailId = null;
		if (mail.getMailFrom() != null && mail.getMailFrom()[0] != null) {
			replyToDifferentEmailId = mail.getMailFrom()[0];
		} else if (isReplyToDifferentMail && replyEmailAddress != "") {
			replyToDifferentEmailId = (new InternetAddress(replyEmailAddress));
		} else {
			replyToDifferentEmailId = (mail.getReplyToDifferentMailId());
		}

		InternetAddress[] mailToList = null;
		if (mail.getInternetAddressToArray() != null) {
			mailToList = mail.getInternetAddressToArray();
		} else if (mailMap.get("internetAddressToArray") != "" && mailMap.get("internetAddressToArray") != null) {
			mailToList = InternetAddress.parse(mailMap.get("internetAddressToArray"));
		} else {
			// mail to at least one mail id required
			message.addRecipients(Message.RecipientType.TO, noReplyToMailId);
			throw new Exception("To mail id missing  ");
		}

		// if (mailMap.get("internetAddressToArray") != "" &&
		// mailMap.get("internetAddressToArray") != null) {
		// mailToList = InternetAddress.parse(mailMap.get("internetAddressToArray"));
		// } else {
		// mailToList = mail.getInternetAddressToArray();
		// }
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
		/* Added for Mail Footer String */
		String mailFooterString = (mailMap.get("mailFooter") != null && !mailMap.get("mailFooter").isEmpty())
				? mailMap.get("mailFooter")
				: mail.getMailFooter();

		if (mailFooterString != null  && mailFooterString.isEmpty() == false) {
			mail.setMailFooter(mailFooterString);
		}
		/** Ends Here */
		try {

			if (fromMailId != null && fromMailId != "" && isValidEmailAddressFromStringValidation(fromMailId)) {
				message.setFrom(new InternetAddress(fromMailId, fromMailName));
			} else {
				throw new Exception("Invalid/No From Emailid entered" + fromMailId);
			}
			/** Added for replyTo */
			if (replyTo != null && replyTo != "" && isValidEmailAddressFromStringValidation(replyTo)) {
				message.setReplyTo(InternetAddress.parse(replyTo));
				InternetAddress replyToIA = InternetAddress.parse(replyTo)[0];
				message.setReplyTo(new javax.mail.Address[] { replyToIA });// Ends Here
			} else if (isReplyToDifferentMail != null && isReplyToDifferentMail.equals(Boolean.TRUE)) {
				message.setReplyTo(new javax.mail.Address[] { replyToDifferentEmailId });
			} else {
				throw new Exception("Invalid/No Reply to  Emailid entered " + replyToDifferentEmailId);
			}
			/** Ends Here */

			if (mailToList != null && separatemails == false) {
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

			messageBodyPart.setContent(mailBody, "text/html; charset=utf-8");
			mimeMultipart.addBodyPart(messageBodyPart);

			/** Added for File Bin Attachment in Mail */
			List<FileUpload> fileUploads = fileUploadRepository.findAllByFileBinId("mailAttachment");
			File attachedFile = null;
			for (FileUpload fu : fileUploads) {
				String fileUploadId = fu.getFileUploadId();
				Integer isAllowed = storageService.hasPermission(null, null, fileUploadId,
						Constants.VIEW_FILE_VALIDATOR, new HashMap<>());
				if (isAllowed > 0) {
					Map<String, Object> fileInfo = storageService.load(fileUploadId);
					if (fileInfo != null) {
						byte[] fileByte = (byte[]) fileInfo.get("file");
						attachedFile = new File(fu.getOriginalFileName());
						try (FileOutputStream fos = new FileOutputStream(attachedFile)) {
							fos.write(fileByte);
						} catch (Exception exception) {
							logger.error("Error occurred while accessing file", exception);
						}
						BodyPart atachmentBodyPart = new MimeBodyPart();
						DataSource source = new FileDataSource(attachedFile);
						atachmentBodyPart.setDataHandler(new DataHandler(source));
						atachmentBodyPart.setFileName(fu.getOriginalFileName());
						mimeMultipart.addBodyPart(atachmentBodyPart);
						if (mailFooterString != null && mailFooterString.isEmpty() == false ) {
							if (mail.getMailFooter().contains("cid")) {
								String footer = mail.getMailFooter();
								String[] strArray = footer.split("cid:");
								for (int iCounter = 0; iCounter < strArray.length; iCounter++) {
									if (strArray[iCounter].contains("\"")) {
										String[] str = strArray[iCounter].split("\"");
										if (str[0].equalsIgnoreCase(fu.getOriginalFileName())) {
											atachmentBodyPart.setDisposition(MimeBodyPart.INLINE);
										}
									}
								}
							}
						}

					} else {
						throw new Exception("Could not read the file with name - " + fu.getOriginalFileName()
								+ " : fileBinId : " + fu.getFileBinId() + " : fileUploadId : " + fu.getFileUploadId());
					}
				}
			}
			/** Added for Mail Footer */
			if (mailFooterString != null && mailFooterString.isEmpty() == false) {
				if (mail.getMailFooter().contains("cid")) {
					mailBody = mailBody + "	<br>" + mail.getMailFooter();
					messageBodyPart.setContent(mailBody, "text/html; charset=utf-8");
				} else {
					mailBody = mailBody + "	<br>" + mail.getMailFooter();
					messageBodyPart.setContent(mailBody, "text/html; charset=utf-8");
				}
			}
			/** Ends Here */

			message.setSentDate(new Date());
			message.setContent(mimeMultipart);

			if (separatemails == false) {
				Transport.send(message);
			} else if (mail.getSeparatemails()) {
				for (int iRecepientCounter = 0; iRecepientCounter < mailToList.length; iRecepientCounter++) {
					String strMailTo = mailToList[iRecepientCounter].getAddress();
					InternetAddress[] address = { new InternetAddress(strMailTo) };
					message.setRecipients(Message.RecipientType.TO, address);
					Transport.send(message);
				}
			}

		} catch (Exception a_exc) {
			isSent = false;
			logger.error("Error ocurred.", a_exc);
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
				String emlFileStoragePath = propertyMasterService.findPropertyMasterValue("emlFileStoragePath");
				if (emlFileStoragePath == null || (emlFileStoragePath != null && emlFileStoragePath.isEmpty())) {
					emlFileStoragePath = System.getProperty("java.io.tmpdir");
				}

				File emFile = new File(emlFileStoragePath);
				if (emFile.exists() == false) {
					emlFileStoragePath = System.getProperty("java.io.tmpdir");
				}
				emlFileStoragePath = emlFileStoragePath + emailEMLFile + "_Mail.eml";
				emFile = new File(emlFileStoragePath);

				message.writeTo(new FileOutputStream(emFile));
				message.writeTo(new FileOutputStream(new File(emlFileStoragePath)));

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
					String mailStr = "";
					for (int i = 0; i < mailToList.length; i++) {
						mailStr += mailToList[i].getAddress();
					}
					mailHistory.setMailSentTo(mailStr);
				}

				mailHistory.setMailFaliedTime(Calendar.getInstance());
				mailHistory.setEmlFilePath(emlFileStoragePath);

				sendMailDao.saveFailedMails(mailHistory);

			} catch (IOException | MessagingException a_excp) {
				isSent = false;
				logger.error("Error ocurred.", a_excp);
			}
		}

		return CompletableFuture.completedFuture(isSent);
	}

	public ResponseEntity<?> downloadEmails(HttpServletRequest a_httpServletRequest, Map<String, FileInfo> files,
			Map<String, Object> daoResultSets, UserDetailsVO detailsVO) {

		try {
			String filePath = a_httpServletRequest.getParameter("emlFilePath");

			if (new File(filePath).exists() == true) {
				File file = new File(filePath);
				return downloadFile(file, a_httpServletRequest, file.getName().toString());
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File doesnot exists");
			}
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Exception occured while downloading file file.");
		}
	}

	public ResponseEntity<?> downloadFile(File file, HttpServletRequest request, String fileName) throws Exception {

		if (file.exists() == true) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ByteArrayResource arrayResource;
			try {
				byte[] bytes = FileCopyUtils.copyToByteArray(file);
				arrayResource = new ByteArrayResource(bytes);
			} finally {
				bos.close();
			}

			ResponseEntity<?> responseEntity = ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
					.contentType(MediaType.APPLICATION_OCTET_STREAM).body(arrayResource);
			return (ResponseEntity<?>) responseEntity;
		}
		return null;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
	}

	/**
	 * Method written for configuring Failed Mail Notification
	 * 
	 * @author Bibhusrita.Nayak
	 * @param mail
	 * @param message
	 * @throws Exception
	 */
	private void failedRecipient(Email mail, MimeMessage message) throws Exception {
		InternetAddress[] failedRecipientList = mail.getFailedrecipient();
		InternetAddress[] mailToList = mail.getInternetAddressToArray();
		InternetAddress[] ccList = mail.getInternetAddressCCArray();
		InternetAddress[] bccList = mail.getInternetAddressBCCArray();

		InternetAddress[] fromMailId = mail.getMailFrom();

		try {

			message.addRecipients(Message.RecipientType.TO, failedRecipientList);
			message.setSubject("Failed Mail Notification");
			MimeMultipart mimeMultipart = new MimeMultipart();
			BodyPart messageBodyPart = new MimeBodyPart();

			StringBuilder htmlBuilder = new StringBuilder();
			htmlBuilder.append("<html>");
			htmlBuilder.append("<body><p>This is a failed mail notification for</p></body>");
			htmlBuilder.append("<p><b>Subject:</b> " + mail.getSubject() + "</p>");
			htmlBuilder.append("<body><p><b>To:</b></p></body>");
			String mailStr = "";
			if (mailToList.length > 0) {
				for (int i = 0; i < mailToList.length; i++) {
					mailStr += mailToList[i].getAddress();
					htmlBuilder.append(mailStr);
				}
			} else {
				htmlBuilder.append("");
			}
			htmlBuilder.append("<body><p><b>Cc:</b></p></body>");
			if (ccList.length > 0) {
				for (int i = 0; i < ccList.length; i++) {
					mailStr += ccList[i].getAddress();
					htmlBuilder.append(mailStr);
				}
			} else {
				htmlBuilder.append("");
			}
			htmlBuilder.append("<body><p><b>Bcc:</b></p></body>");
			if (bccList.length > 0) {
				for (int i = 0; i < bccList.length; i++) {
					mailStr += bccList[i].getAddress();
					htmlBuilder.append(mailStr);
				}
			} else {
				htmlBuilder.append("");
			}
			htmlBuilder.append("</html>");
			String html = htmlBuilder.toString();
			messageBodyPart.setContent(html, "text/html; charset=utf-8");
			mimeMultipart.addBodyPart(messageBodyPart);
			message.setContent(mimeMultipart);
			Transport.send(message);

		} catch (Exception exc) {
			logger.error("Error ocurred.", exc);

			try {

				String emailEMLFile = UUID.randomUUID().toString();
				String emlFileStoragePath = propertyMasterService.findPropertyMasterValue("emlFileStoragePath");
				if (emlFileStoragePath == null || (emlFileStoragePath != null && emlFileStoragePath.isEmpty())) {
					emlFileStoragePath = System.getProperty("java.io.tmpdir");
				}

				File emFile = new File(emlFileStoragePath);
				if (emFile.exists() == false) {
					emlFileStoragePath = System.getProperty("java.io.tmpdir");
				}
				emlFileStoragePath = emlFileStoragePath + emailEMLFile + "_Mail.eml";
				emFile = new File(emlFileStoragePath);

				message.writeTo(new FileOutputStream(emFile));

				MailHistory mailHistory = new MailHistory();
				String uuidGenerated = UUID.randomUUID().toString();
				mailHistory.setFailedMailId(uuidGenerated);
				if (fromMailId.length == 0) {
					mailHistory.setMailSentBy("fromMailId@notFound.com");
				} else {
					String mailStr = "";
					for (int i = 0; i < fromMailId.length; i++) {
						mailStr += fromMailId[i].getAddress();
					}
					mailHistory.setMailSentBy(mailStr);
				}

				if (failedRecipientList.length == 0) {
					mailHistory.setMailSentTo("atleastOneFailedMailId@required.com");
				} else {
					String mailStr = "";
					for (int i = 0; i < failedRecipientList.length; i++) {
						mailStr += failedRecipientList[i].getAddress();
					}
					mailHistory.setMailSentTo(mailStr);
				}

				mailHistory.setMailFaliedTime(Calendar.getInstance());
				mailHistory.setEmlFilePath(emlFileStoragePath);

				sendMailDao.saveFailedMails(mailHistory);

				if (mail.getLoggedInUserRole().contains("ADMIN") && mail.getIsAuthenticationEnabled()) {

					addNotification();
				}

			} catch (IOException | MessagingException a_exc) {

				logger.error("Error ocurred.", a_exc);

			}
		}
	}

	/**
	 * @author Bibhusrita.Nayak For adding notification for failed mails.
	 * 
	 */
	private void addNotification() {

		Map<String, Object> requestParams = new HashMap<>();

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, 1);
		java.util.Date dt = cal.getTime();

		requestParams.put("messageValidFrom", new Date());
		requestParams.put("messageValidTill", dt);
		requestParams.put("messageText", Constant.MESSAGE_TEXT);
		requestParams.put("messageType", "0");
		requestParams.put("displayOnceTxt", "1");
		requestParams.put("selectionCriteria", "SELECT 1");
		requestParams.put("createdBy", "system_auto_generated");
		requestParams.put("updatedBy", "system_auto_generated");

		scriptUtil.addNotification(requestParams);

	}
}
