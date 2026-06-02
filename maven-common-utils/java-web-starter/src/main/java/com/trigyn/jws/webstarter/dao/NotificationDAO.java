package com.trigyn.jws.webstarter.dao;

import java.util.List;
import java.util.Objects;

import javax.sql.DataSource;

import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.dashboard.entities.DashletProperties;
import com.trigyn.jws.dbutils.repository.DBConnection;
import com.trigyn.jws.webstarter.vo.GenericUserNotification;

import jakarta.persistence.Query;

@Repository
public class NotificationDAO extends DBConnection {

	public NotificationDAO(DataSource dataSource) {
		super(dataSource);
	}

	public GenericUserNotification getNotificationDetails(String notificationId) {
		GenericUserNotification genericUserNotificationDetails =  getCurrentSession().get(GenericUserNotification.class, notificationId);
		if(genericUserNotificationDetails != null) getCurrentSession().evict(genericUserNotificationDetails);
		return genericUserNotificationDetails;
	}

	@Transactional(readOnly = false)
	public void saveGenericUserNotification(GenericUserNotification genericUserNotificationDetails) {
		if(genericUserNotificationDetails.getNotificationId() == null || getNotificationDetails(genericUserNotificationDetails.getNotificationId()) == null) {
			getCurrentSession().persist(genericUserNotificationDetails);			
		}else {
			getCurrentSession().merge(genericUserNotificationDetails);
		}
	}

	public Boolean executeSelectionCriteria(String selectionQuery) throws Exception {
		NativeQuery	sqlQuery	= sessionFactory.getCurrentSession().createNativeQuery(selectionQuery, String.class);
		Boolean		data		= sqlQuery.uniqueResult().toString().equalsIgnoreCase("1");
		return data;
	}

	public List<GenericUserNotification> getNotificationData(String contextName) throws Exception {
		String	hql			= " FROM GenericUserNotification WHERE  "
				+ "CURDATE() BETWEEN messageValidFrom AND messageValidTill AND targetPlatform = :targetPlatform"
				+ " ORDER BY CASE messageType WHEN 'error' THEN 1 WHEN 'warning' THEN 2 WHEN 'informative' THEN 3 END, messageValidTill, messageText ASC ";
		Query	hqlQuery	= sessionFactory.getCurrentSession().createQuery(hql, GenericUserNotification.class);
		hqlQuery.setParameter("targetPlatform", contextName);
		List<GenericUserNotification> data = hqlQuery.getResultList();
		return data;
	}

	public Long getNotificationDetailsCount(String notificationId) {
		StringBuilder				stringBuilder	= new StringBuilder(
				"SELECT count(*) FROM GenericUserNotification AS d WHERE d.notificationId = :notificationId");
		org.hibernate.query.Query	query			= getCurrentSession().createQuery(stringBuilder.toString(), Long.class);
		query.setParameter("notificationId", notificationId);
		return (Long) query.uniqueResult();
	}

}