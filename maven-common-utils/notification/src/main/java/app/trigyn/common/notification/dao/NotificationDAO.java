package app.trigyn.common.notification.dao;

import java.util.List;

import javax.persistence.Query;
import javax.sql.DataSource;

import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import app.trigyn.common.dbutils.repository.DBConnection;
import app.trigyn.common.notification.entities.GenericUserNotification;

@Repository
public class NotificationDAO extends DBConnection {

    @Autowired
    public NotificationDAO(DataSource dataSource) {
        super(dataSource);
    }
    
    
    /** 
     * @throws Exception
     */
    public void getNotificationDetails() throws Exception{
        System.out.println();
    }

	/**
	 * @param notificationId
	 * @return
	 */
	public GenericUserNotification getNotificationDetails(String notificationId) {
		GenericUserNotification genericUserNotificationDetails = (GenericUserNotification) sessionFactory.getCurrentSession().get(GenericUserNotification.class, notificationId);
		return genericUserNotificationDetails;
	}

	/**
	 * @param userNotification
	 */
	public void saveEditedNotification(GenericUserNotification userNotification) {
		sessionFactory.getCurrentSession().saveOrUpdate(userNotification);
	}
	

	/**
	 * @param selectionQuery
	 * @return
	 * @throws Exception
	 */
	public Boolean executeSelectionCriteria(String selectionQuery) throws Exception {
		NativeQuery sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(selectionQuery);
		Boolean data = sqlQuery.uniqueResult().toString().equalsIgnoreCase("1");
		return data;
    }
    
	 /**
	 * @param contextName
	 * @return
	 * @throws Exception
	 */
	public List<GenericUserNotification> getNotificationData(String contextName) throws Exception {
			String hql = " FROM GenericUserNotification WHERE  "
					+ "CURDATE() BETWEEN messageValidFrom AND messageValidTill AND targetPlatform = :targetPlatform"
					+ " ORDER BY CASE messageType WHEN 'error' THEN 1 WHEN 'warning' THEN 2 WHEN 'informative' THEN 3 END, messageValidTill, messageText ASC ";
			Query hqlQuery = sessionFactory.getCurrentSession().createQuery(hql);
			hqlQuery.setParameter("targetPlatform", contextName);
			List<GenericUserNotification> data = hqlQuery.getResultList();
			return data;
	}

}