package com.trigyn.jws.notification.entities;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "generic_user_notification")
public class GenericUserNotification {

	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	@Column(name = "notification_id", length = 50, nullable = false)
	@Id
	private String notificationId = null;

	
	@Column(name = "target_platform", length = 100, nullable = false)
	@Basic(fetch = FetchType.LAZY)
	private String targetPlatform = null;

	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "message_valid_from", length = 100,nullable = false)
	@Basic(fetch = FetchType.LAZY)
	private Date messageValidFrom = null;

	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "message_valid_till", length = 100,nullable = false)
	@Basic(fetch = FetchType.LAZY)
	private Date messageValidTill = null;

	
	@Column(name = "message_text", length = 100, nullable = false)
	@Basic(fetch = FetchType.LAZY)
	private String messageText = null;
	
	@Column(name = "message_type", length = 100, nullable = false)
	@Basic(fetch = FetchType.LAZY)
	private String messageType = null;


	@Column(name = "message_format", length = 100,nullable = false)
	@Basic(fetch = FetchType.LAZY)
	private String messageFormat = null;

	
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "created_by", length = 100, nullable = false, updatable = false)
	private String createdBy = null;

	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "creation_date", length = 100, nullable = false, updatable = false)
	@Basic(fetch = FetchType.LAZY)
	private Date creationDate = null;

	
	@Column(name = "selection_criteria", length = 100,nullable = false)
	@Basic(fetch = FetchType.LAZY)
	private String selectionCriteria = null;

	
	@Column(name = "updated_by", length = 100,nullable = true)
	@Basic(fetch = FetchType.LAZY)
	private String updatedBy = null;

   
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_date", length = 100, nullable = true)
	@Basic(fetch = FetchType.LAZY)
	private Date updatedDate = null;
    

	public GenericUserNotification() {
		super();
	}

	public GenericUserNotification(String notificationId, String targetPlatform, Date messageValidFrom, Date messageValidTill,
			String messageText,String messageType, String messageFormat,String createdBy,  Date creationDate, String selectionCriteria,
			String updatedBy, Date updatedDate) {
		super();
		this.notificationId = notificationId;
		this.targetPlatform = targetPlatform;
		this.messageValidFrom = messageValidFrom;
		this.messageValidTill = messageValidTill;
		this.messageText = messageText;
		this.messageType = messageType;
		this.messageFormat = messageFormat;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.selectionCriteria = selectionCriteria;
		this.updatedBy = updatedBy;
		this.updatedDate = updatedDate;
	}
	
	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(String notificationId) {
		this.notificationId = notificationId;
	}

	public String getTargetPlatform() {
		return targetPlatform;
	}

	public void setTargetPlatform(String targetPlatform) {
		this.targetPlatform = targetPlatform;
	}

	public Date getMessageValidFrom() {
		return messageValidFrom;
	}

	public void setMessageValidFrom(Date messageValidFrom) {
		this.messageValidFrom = messageValidFrom;
	}

	public Date getMessageValidTill() {
		return messageValidTill;
	}

	public void setMessageValidTill(Date messageValidTill) {
		this.messageValidTill = messageValidTill;
	}

	public String getMessageText() {
		return messageText;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	public String getMessageFormat() {
		return messageFormat;
	}

	public void setMessageFormat(String messageFormat) {
		this.messageFormat = messageFormat;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getSelectionCriteria() {
		return selectionCriteria;
	}

	public void setSelectionCriteria(String selectionCriteria) {
		this.selectionCriteria = selectionCriteria;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public static GenericUserNotification createGenericUserNotification(String a_currentUser, String a_strDateFormat, Map a_properties) throws ParseException {
		GenericUserNotification userNotification = new GenericUserNotification();
		if(a_properties.containsKey("objGenericNotificationRequest[notificationId]")) {
			userNotification.setNotificationId(getSingleValue(a_properties.get("objGenericNotificationRequest[notificationId]")));	
		}
		
		userNotification.setTargetPlatform(getSingleValue(a_properties.get("objGenericNotificationRequest[targetPlatform]")));
		userNotification.setMessageValidFrom(new SimpleDateFormat(a_strDateFormat).parse(getSingleValue(a_properties.get("objGenericNotificationRequest[messageValidFrom]"))));
		
		Date dateFormat = new SimpleDateFormat(a_strDateFormat).parse(getSingleValue(a_properties.get("objGenericNotificationRequest[messageValidTill]")));

		Calendar cal = Calendar.getInstance();
		cal.setTime(dateFormat);
		cal.set(Calendar.HOUR, 11);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.AM_PM, Calendar.PM);

		Date tillDate = cal.getTime();
	        
		userNotification.setMessageValidTill(tillDate);
		userNotification.setMessageFormat(getSingleValue (a_properties.get("objGenericNotificationRequest[messageFormat]")));
		userNotification.setMessageText(getSingleValue(a_properties.get("objGenericNotificationRequest[messageText]")));
		userNotification.setMessageType(getSingleValue(a_properties.get("objGenericNotificationRequest[messageType]")));
		userNotification.setSelectionCriteria(getSingleValue(a_properties.get("objGenericNotificationRequest[selectionCriteria]")));
		
		if(a_properties.containsKey("objGenericNotificationRequest[createdBy]")) {
			userNotification.setCreatedBy(getSingleValue(a_properties.get("objGenericNotificationRequest[createdBy]")));
			userNotification.setUpdatedBy(a_currentUser);
			userNotification.setUpdatedDate(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime());
		}else {
			
			userNotification.setCreatedBy(a_currentUser);
			userNotification.setCreationDate(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime());
		}
		return userNotification;
	}
	
	private static String getSingleValue(Object a_obj) {
		if(a_obj instanceof String) {
			return a_obj.toString();
		}else {
			return ((String[])a_obj)[0];
		}
	}

	@Override
	public String toString() {
		return "GenericUserNotification [notificationId=" + notificationId + ", targetPlatform=" + targetPlatform
				+ ", messageValidFrom=" + messageValidFrom + ", messageValidTill=" + messageValidTill + ", messageText="
				+ messageText + ", messageType=" + messageType + ", messageFormat=" + messageFormat + ", createdBy="
				+ createdBy + ", creationDate=" + creationDate + ", selectionCriteria=" + selectionCriteria
				+ ", updatedBy=" + updatedBy + ", updatedDate=" + updatedDate + "]";
	}

	public GenericUserNotification getObject() {
		GenericUserNotification notification = new GenericUserNotification();
		notification.setCreatedBy(createdBy);
		notification.setCreationDate(creationDate);
		notification.setMessageFormat(messageFormat);
		notification.setMessageText(messageText);
		notification.setMessageType(messageType);
		notification.setMessageValidFrom(messageValidFrom);
		notification.setMessageValidTill(messageValidTill);
		notification.setNotificationId(notificationId);
		notification.setSelectionCriteria(selectionCriteria);
		notification.setTargetPlatform(targetPlatform);
		notification.setUpdatedBy(updatedBy);
		notification.setUpdatedDate(updatedDate);
		
		return notification;
	}

}