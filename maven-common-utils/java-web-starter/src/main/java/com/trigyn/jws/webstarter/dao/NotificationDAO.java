package com.trigyn.jws.webstarter.dao;

import java.util.List;

import javax.persistence.Query;
import javax.sql.DataSource;

import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.dbutils.repository.DBConnection;
import com.trigyn.jws.webstarter.vo.GenericUserNotification;


@Repository
public class NotificationDAO extends DBConnection {

	@Autowired
	public NotificationDAO(DataSource dataSource) {
		super(dataSource);
	}

	public void getNotificationDetails() throws Exception {
		System.out.println();
	}

	public GenericUserNotification getNotificationDetails(String notificationId) {
		GenericUserNotification genericUserNotificationDetails =  hibernateTemplate.get(GenericUserNotification.class, notificationId);
		if(genericUserNotificationDetails != null) getCurrentSession().evict(genericUserNotificationDetails);
		return genericUserNotificationDetails;
	}

	@Transactional(readOnly = false)
	public void saveGenericUserNotification(GenericUserNotification genericUserNotificationDetails) {
		if(genericUserNotificationDetails.getNotificationId() == null || getNotificationDetails(genericUserNotificationDetails.getNotificationId()) == null) {
			getCurrentSession().save(genericUserNotificationDetails);			
		}else {
			getCurrentSession().saveOrUpdate(genericUserNotificationDetails);
		}
	}

	public void saveEditedNotification(GenericUserNotification userNotification) {
		sessionFactory.getCurrentSession().saveOrUpdate(userNotification);
	}

	public Boolean executeSelectionCriteria(String selectionQuery) throws Exception {
		NativeQuery	sqlQuery	= sessionFactory.getCurrentSession().createSQLQuery(selectionQuery);
		Boolean		data		= sqlQuery.uniqueResult().toString().equalsIgnoreCase("1");
		return data;
	}

	public List<GenericUserNotification> getNotificationData(String contextName) throws Exception {
		String	hql			= " FROM GenericUserNotification WHERE  "
				+ "CURDATE() BETWEEN messageValidFrom AND messageValidTill AND targetPlatform = :targetPlatform"
				+ " ORDER BY CASE messageType WHEN 'error' THEN 1 WHEN 'warning' THEN 2 WHEN 'informative' THEN 3 END, messageValidTill, messageText ASC ";
		Query	hqlQuery	= sessionFactory.getCurrentSession().createQuery(hql);
		hqlQuery.setParameter("targetPlatform", contextName);
		List<GenericUserNotification> data = hqlQuery.getResultList();
		return data;
	}

	public Long getNotificationDetailsCount(String notificationId) {
		StringBuilder				stringBuilder	= new StringBuilder(
				"SELECT count(*) FROM GenericUserNotification AS d WHERE d.notificationId = :notificationId");
		org.hibernate.query.Query	query			= getCurrentSession().createQuery(stringBuilder.toString());
		query.setParameter("notificationId", notificationId);
		return (Long) query.uniqueResult();
	}

}