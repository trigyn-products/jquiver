package com.trigyn.jws.quartz.jobs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.ServletContext;
import javax.xml.bind.JAXBException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.CollectionUtils;

import com.google.common.io.ByteStreams;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.utils.CustomStopException;
import com.trigyn.jws.dynarest.service.FilesStorageService;
import com.trigyn.jws.dynarest.service.SendMailService;
import com.trigyn.jws.dynarest.utils.Constants;
import com.trigyn.jws.dynarest.vo.AttachmentXMLVO;
import com.trigyn.jws.dynarest.vo.Email;
import com.trigyn.jws.dynarest.vo.EmailAttachedFile;
import com.trigyn.jws.dynarest.vo.EmailBodyXMLVO;
import com.trigyn.jws.dynarest.vo.EmailXMLVO;
import com.trigyn.jws.dynarest.vo.FailedRecipientXMLVO;
import com.trigyn.jws.dynarest.vo.FailedRecipientsXMLVO;
import com.trigyn.jws.dynarest.vo.RecepientXMLVO;
import com.trigyn.jws.dynarest.vo.RecepientsXMLVO;
import com.trigyn.jws.quartz.jobs.dao.MailScheduleDao;
import com.trigyn.jws.quartz.jobs.dao.MailScheduleRepository;
import com.trigyn.jws.quartz.models.entities.MailSchedule;
import com.trigyn.jws.quartz.models.entities.request.EmailSchedulerRequestVo;
import com.trigyn.jws.quartz.util.JobUtil;
import com.trigyn.jws.templating.service.DBTemplatingService;
import com.trigyn.jws.templating.utils.TemplatingUtils;
import com.trigyn.jws.templating.vo.TemplateVO;
import com.trigyn.jws.usermanagement.security.config.ApplicationSecurityDetails;
import com.trigyn.jws.usermanagement.security.config.JwsUserDetailsService;
import com.trigyn.jws.usermanagement.security.config.JwtUtil;

public class JwsMailScheduleJob extends QuartzJobBean {

	private static Logger				logger						= LogManager.getLogger(JwsMailScheduleJob.class);

	@Autowired
	MailScheduleRepository				mScheduleRepository			= null;

	@Autowired
	private TemplatingUtils				templatingUtils				= null;

	@Autowired
	private IUserDetailsService			detailsService				= null;

	@Autowired
	@Lazy
	private SendMailService				sendMailService				= null;

	@Autowired
	@Qualifier("file-system-storage")
	private FilesStorageService			storageService				= null;

	@Autowired
	private DBTemplatingService			templatingService			= null;

	@Autowired
	@Lazy
	private JwtUtil						jwtUtil						= null;

	@Autowired
	@Lazy
	private JwsUserDetailsService		jwsUserDetailsService		= null;

	@Autowired
	@Lazy
	private UserDetailsService			userDetailsService			= null;

	@Autowired
	private ApplicationSecurityDetails	applicationSecurityDetails	= null;

	@Autowired
	ServletContext						servletContext				= null;

	@Autowired
	private MailScheduleDao				mailScheduleDao				= null;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

		JobDataMap			jobDataMap		= context.getMergedJobDataMap();
		String				dynamicRestUrl	= (String) jobDataMap.get("dynamicRestUrl");
		Map<String, Object>	requestParams	= (Map<String, Object>) jobDataMap.get("requestParams");
		try {
			String groupId = (String) jobDataMap.get("mailSenderGroupId");
			if (groupId.isEmpty() == false && groupId.isBlank() == false && null != groupId) {
				List<MailSchedule> mh = mScheduleRepository.findBySendorGroupId(groupId);
				if (mh != null && mh.isEmpty() == false) {
					for (MailSchedule emailXMLVO : mh) {
						EmailSchedulerRequestVo emailVo = new EmailSchedulerRequestVo();
						emailVo.setDynamicRestUrl(dynamicRestUrl);
						emailVo.setMailSchedule(emailXMLVO);
						emailVo.setMailScheduleId(emailXMLVO.getMailScheduleId());
						emailVo.setMailSenderGroupId(groupId);
						emailVo.setRequestParams(requestParams);
						sendMail(emailVo);
					}
				}
			}

		} catch (CustomStopException custStopException) {
			logger.error("Error occured in executeSendMail.", custStopException);
			throw custStopException;
		} catch (JAXBException exception) {
			logger.error("Error occurred while unmarshalling XML string content ", exception);

		} catch (Throwable a_thr) {
			logger.error(
					"Error occurred while sending email in " + "Rest API" + " : " + requestParams.get("dynamicRestUrl"),
					a_thr);
		}

	}

	private void sendMail(EmailSchedulerRequestVo emailVo)
			throws JAXBException, Exception, FileNotFoundException, IOException, AddressException {

		EmailXMLVO					emailObj					= JobUtil
				.unMarshalEmailXMLVO(emailVo.getMailSchedule().getEmailXml());
		List<EmailAttachedFile>		attachedFileList			= new ArrayList<>();
		RecepientsXMLVO				recepientsXMLVO				= emailObj.getRecepientsXMLVO();
		List<RecepientXMLVO>		recepientXMLVOList			= recepientsXMLVO.getRecepientXMLVO();
		/* Written for Failure Mail Notification */
		FailedRecipientsXMLVO		failedrecepientsXMLVO		= emailObj.getFailedrecepientsXMLVO();
		List<FailedRecipientXMLVO>	failedRecipientXMLVOList	= new ArrayList<>();

		if (failedrecepientsXMLVO != null) {
			failedRecipientXMLVOList = failedrecepientsXMLVO.getFailedrecipientXMLVO();
		}
		List<AttachmentXMLVO> attachmentXMLVOList = emailObj.getAttachmentXMLVO();
		if (CollectionUtils.isEmpty(attachmentXMLVOList) == false) {
			for (AttachmentXMLVO attachmentXMLVO : attachmentXMLVOList) {
				EmailAttachedFile	emailAttachedFile	= new EmailAttachedFile();
				File				attachedFile		= null;
				if (attachmentXMLVO.getType().equals(Constants.FILE_ATTACHMENT_FILEBIN)) {
					String	fileUploadId	= attachmentXMLVO.getFilePath();
					Integer	isAllowed		= storageService.hasPermission(null, null, fileUploadId,
							Constants.VIEW_FILE_VALIDATOR, new HashMap<>());
					if (isAllowed > 0) {
						Map<String, Object> fileInfo = storageService.load(fileUploadId);
						if (fileInfo != null) {
							byte[] fileByte = (byte[]) fileInfo.get("file");
							attachedFile = new File(attachmentXMLVO.getFileName());
							try (FileOutputStream fos = new FileOutputStream(attachedFile)) {
								fos.write(fileByte);
							} catch (Exception exception) {
								logger.error("Error occurred while accessing file in " + "Rest API" + " : "
										+ emailVo.getDynamicRestUrl(), exception);
							}
						} else {
							logger.error("Error occurred while accessing file in " + "Rest API" + " : "
									+ emailVo.getDynamicRestUrl());
						}
					}

				} else if (attachmentXMLVO.getType().equals(Constants.FILE_ATTACHMENT_FILESYSTEM)
						|| attachmentXMLVO.getType().equals(Constants.FILE_ATTACHMENT_UPLOADEDFILE)) {
					File aFile = new File(attachmentXMLVO.getFilePath());
					if (aFile != null && aFile.exists()) {
						attachedFile = new File(attachmentXMLVO.getFileName());
						InputStream	in			= new FileInputStream(attachmentXMLVO.getFilePath());
						byte[]		byteArray	= ByteStreams.toByteArray(in);
						in.close();
						try (FileOutputStream fos = new FileOutputStream(attachedFile)) {
							fos.write(byteArray);
						} catch (Exception exception) {
							logger.error("Error occurred while accessing file in " + "Rest API" + " : "
									+ emailVo.getDynamicRestUrl(), exception);
						}
					}
				}
				if (attachedFile != null) {
					emailAttachedFile.setFile(attachedFile);
					if (attachmentXMLVO.getHasEmbeddedImage() != null && attachmentXMLVO.getHasEmbeddedImage() != "") {
						emailAttachedFile.setIsEmbeddedImage(true);
						emailAttachedFile.setEmbeddedImageValue(attachmentXMLVO.getHasEmbeddedImage());
					}
					attachedFileList.add(emailAttachedFile);
				}
			}
		}
		List<String>	toEmailIdList		= new ArrayList<>();
		List<String>	ccEmailIdList		= new ArrayList<>();
		List<String>	bccEmailIdList		= new ArrayList<>();
		List<String>	frEmailIdList		= new ArrayList<>();		// Created for Failure Mail
																		// notification
		StringJoiner	toEmailIdStrJoiner	= new StringJoiner(", ");
		StringJoiner	ccEmailIdStrJoiner	= new StringJoiner(", ");
		StringJoiner	bccEmailIdStrJoiner	= new StringJoiner(", ");
		StringJoiner	frEmailIdStrJoiner	= new StringJoiner(", ");	// Created for Failure Mail
																		// notification

		for (RecepientXMLVO recepientXMLVO : recepientXMLVOList) {
			if (recepientXMLVO.getRecepientType().equals("to") == true
					&& toEmailIdList.contains(recepientXMLVO.getMailId()) == false) {
				toEmailIdStrJoiner.add(recepientXMLVO.getMailId());
				toEmailIdList.add(recepientXMLVO.getMailId());
			}
			if (recepientXMLVO.getRecepientType().equals("cc") == true
					&& ccEmailIdList.contains(recepientXMLVO.getMailId()) == false) {
				ccEmailIdStrJoiner.add(recepientXMLVO.getMailId());
				ccEmailIdList.add(recepientXMLVO.getMailId());
			}
			if (recepientXMLVO.getRecepientType().equals("bcc") == true
					&& bccEmailIdList.contains(recepientXMLVO.getMailId()) == false) {
				bccEmailIdStrJoiner.add(recepientXMLVO.getMailId());
				bccEmailIdList.add(recepientXMLVO.getMailId());
			}
		}
		Email email = new Email();
		email.setIsAuthenticationEnabled(applicationSecurityDetails.getIsAuthenticationEnabled());
		email.setLoggedInUserRole(detailsService.getUserDetails().getRoleIdList());
		/* Written for Failure Mail Notification */
		if (failedRecipientXMLVOList != null) {
			for (FailedRecipientXMLVO failedrecepientXMLVO : failedRecipientXMLVOList) {
				frEmailIdStrJoiner.add(failedrecepientXMLVO.getMailId());
				frEmailIdList.add(failedrecepientXMLVO.getMailId());
			}
		}
		email.setInternetAddressToArray(InternetAddress.parse(toEmailIdStrJoiner.toString()));
		email.setInternetAddressCCArray(InternetAddress.parse(ccEmailIdStrJoiner.toString()));
		email.setInternetAddressBCCArray(InternetAddress.parse(bccEmailIdStrJoiner.toString()));
		email.setFailedrecipient(InternetAddress.parse(frEmailIdStrJoiner.toString()));// Added for Failure
																						// Mail Notification
		if (emailObj.getSenderXMLVO() != null) {
			email.setMailFrom(InternetAddress.parse(emailObj.getSenderXMLVO().getMailId()));
			email.setMailFromName(emailObj.getSenderXMLVO().getName());
			if (emailObj.getSenderXMLVO().getReplyTo() != null) {
				email.setReplyToaddress(
						InternetAddress.parse(emailObj.getSenderXMLVO().getReplyTo())); /** Added for ReplyTo */
			}
		}
		EmailBodyXMLVO emailBodyXMLVO = emailObj.getBody();

		if (null != emailObj.getHeader()) {
			email.setHeaderArray(emailObj.getHeader());
		}

		String body = "";
		if (emailBodyXMLVO.getContent() != null && emailBodyXMLVO.getContent() == Constants.EMAIL_CONTENT_TYPE_TWO) {
			TemplateVO templateVO = templatingService.getTemplateByName(emailBodyXMLVO.getData().trim());
			body = templatingUtils.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(),
					emailVo.getRequestParams());
		} else if (emailBodyXMLVO.getContent() != null
				&& emailBodyXMLVO.getContent() == Constants.EMAIL_CONTENT_TYPE_THREE) {
			body = templatingUtils.processTemplateContents(emailBodyXMLVO.getData(), "", emailVo.getRequestParams());
		} else {
			body = emailBodyXMLVO.getData();
		}
		email.setBody(body);
		email.setSubject(emailObj.getSubject());
		email.setAttachementsArray(attachedFileList);
		email.setSeparatemails(recepientsXMLVO.getSeparatemails());
		emailVo.setEmail(email);
		boolean mailSuccess = sendMailService.sendMail(emailVo);
		if (mailSuccess) {
			mailScheduleDao.deleteMailScheduleId(emailVo.getMailScheduleId());
		}
	}
}
