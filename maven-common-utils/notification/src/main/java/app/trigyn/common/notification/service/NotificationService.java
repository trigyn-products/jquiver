package app.trigyn.common.notification.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.trigyn.common.notification.dao.NotificationDAO;
import app.trigyn.common.notification.entities.GenericUserNotification;
import app.trigyn.core.templating.service.DBTemplatingService;
import app.trigyn.core.templating.utils.TemplatingUtils;
import app.trigyn.core.templating.vo.TemplateVO;

@Service
@Transactional
public class NotificationService {

	@Autowired
	private NotificationDAO notificationDao = null;
	
	@Autowired
	private DBTemplatingService templatingService = null;

	@Autowired
	private TemplatingUtils templateEngine = null;

	
	public GenericUserNotification getNotification(String notificationId) {
		GenericUserNotification genericUserNotificationDetails = notificationDao.getNotificationDetails(notificationId);
		return genericUserNotificationDetails;
	}

	public void saveEditedNotificationData(GenericUserNotification userNotification) {
		notificationDao.saveEditedNotification(userNotification);
	}
	
	public String loadNotifications(String viewName, String contextName) throws Exception {
		String template = null;
		Map<String,Object> templateMap = new HashMap<>();
		 TemplateVO templateVO = templatingService.getTemplateByName(viewName);
		 List<GenericUserNotification> notificationData =  notificationDao.getNotificationData(contextName);
		 templateMap.put("notifications", notificationData);
		 if(!notificationData.isEmpty()) {
			 template = templateEngine.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(), templateMap);
		 }
		return template;
	}
	

}