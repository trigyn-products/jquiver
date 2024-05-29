package com.trigyn.jws.quartz.util;

import java.io.StringReader;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

import com.trigyn.jws.dbutils.utils.Constant;
import com.trigyn.jws.dbutils.utils.CustomCharacterEscapeHandler;
import com.trigyn.jws.dynarest.vo.EmailListXMLVO;
import com.trigyn.jws.dynarest.vo.EmailXMLVO;
import com.trigyn.jws.quartz.config.PersistableCronTriggerFactoryBean;

public class JobUtil {
	
	private static Logger		LOGGER					= LogManager.getLogger(JobUtil.class);

	/**
	 * Create Quartz Job.
	 * 
	 * @param  jobClass  Class whose executeInternal() method needs to be called.
	 * @param  isDurable Job needs to be persisted even after completion. if true,
	 *                   job will be persisted, not otherwise.
	 * @param  context   Spring application context.
	 * @param  jobName   Job name.
	 * @param  jobGroup  Job group.
	 * @param  jobDataMap  Job Data Map.
	 * 
	 * @return           JobDetail object
	 */
	public static JobDetail createJob(Class<? extends Job> jobClass, boolean isDurable, ApplicationContext context,
			String jobName, String jobGroup, JobDataMap jobDataMap) {
		JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
		factoryBean.setJobClass(jobClass);
		factoryBean.setDurability(isDurable);
		factoryBean.setApplicationContext(context);
		factoryBean.setName(jobName);
		factoryBean.setGroup(jobGroup);
		// set job data map
		factoryBean.setJobDataMap(jobDataMap);
		factoryBean.afterPropertiesSet();
		return factoryBean.getObject();
	}

	/**
	 * Create cron trigger.
	 * 
	 * @param  triggerName        Trigger name.
	 * @param  startTime          Trigger start time.
	 * @param  cronExpression     Cron expression.
	 * @param  misFireInstruction Misfire instruction (what to do in case of misfire
	 *                            happens).
	 * 
	 * @return                    Trigger
	 */
	public static Trigger createCronTrigger(String triggerName, String jobGroup, Date startTime, String cronExpression,
			int misFireInstruction) {
		PersistableCronTriggerFactoryBean factoryBean = new PersistableCronTriggerFactoryBean();
		factoryBean.setName(triggerName);
		factoryBean.setGroup(jobGroup);
		factoryBean.setStartTime(startTime);
		factoryBean.setCronExpression(cronExpression);
		factoryBean.setMisfireInstruction(misFireInstruction);
		try {
			factoryBean.afterPropertiesSet();
		} catch (ParseException pe) {
			LOGGER.error("Error occured in executeSendMail.", pe.getCause());
		}
		return factoryBean.getObject();
	}

	/**
	 * Create a Single trigger.
	 * 
	 * @param  triggerName        Trigger name.
	 * @param  startTime          Trigger start time.
	 * @param  misFireInstruction Misfire instruction (what to do in case of misfire
	 *                            happens).
	 * 
	 * @return                    Trigger
	 */
	public static Trigger createSingleTrigger(String jobName, String triggerName, Date startTime, int misFireInstruction) {
		SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
		factoryBean.setName(jobName);
		factoryBean.setStartTime(startTime);
		factoryBean.setGroup(triggerName);
		factoryBean.setMisfireInstruction(misFireInstruction);
		factoryBean.setRepeatCount(0);
		factoryBean.afterPropertiesSet();
		return factoryBean.getObject();
	}
	
	public static EmailXMLVO unMarshalEmailXMLVO(String emailXMLContent) throws JAXBException {
		JAXBContext		jaxbContext;
		Unmarshaller	unmarshaller;
		EmailXMLVO		emailListObj;
		jaxbContext		= JAXBContext.newInstance(EmailXMLVO.class);
		unmarshaller	= jaxbContext.createUnmarshaller();
		StringReader reader = new StringReader(emailXMLContent);
		emailListObj = (EmailXMLVO) unmarshaller.unmarshal(reader);
		return emailListObj;
	}
	
	public static Properties getSmtpDetails(Map<String, Object> mailMap) {
		Properties			prop					= new Properties();
		String				stmpPort				= (String) mailMap.get("smtpPort");
		String				smtpSecurityProtocal	= (String) mailMap.get("smtpSecurityProtocal");
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
			
		} else {
			prop.put("mail.smtp.auth", Boolean.FALSE);
		}
		return prop;
	}
	
	public static EmailListXMLVO marshalEmailListXML(String emailsXMLContent) throws JAXBException {
		JAXBContext		jaxbContext;
		Unmarshaller	unmarshaller;
		EmailListXMLVO	emailListObj;
		jaxbContext		= JAXBContext.newInstance(EmailListXMLVO.class);
		unmarshaller	= jaxbContext.createUnmarshaller();
		StringReader reader = new StringReader(emailsXMLContent);
		emailListObj = (EmailListXMLVO) unmarshaller.unmarshal(reader);
		return emailListObj;
	}
	
	public static String emailXmlmarshaling(EmailXMLVO xmlVO) throws JAXBException {
		JAXBContext		jaxbContext		= JAXBContext.newInstance(xmlVO.getClass());
		StringWriter	sw				= new StringWriter();
		Marshaller		jaxbMarshaller	= jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "Unicode");
		jaxbMarshaller.setProperty(com.sun.xml.bind.marshaller.CharacterEscapeHandler.class.getName(),
				new CustomCharacterEscapeHandler());
		jaxbMarshaller.marshal(xmlVO, sw);
		return sw.toString();
	}


}
