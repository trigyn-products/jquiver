package app.trigyn.core.webstarter.controller;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.trigyn.common.dbutils.spi.IUserDetailsService;
import app.trigyn.common.dbutils.vo.UserDetailsVO;
import app.trigyn.common.notification.entities.GenericUserNotification;
import app.trigyn.common.notification.service.NotificationService;
import app.trigyn.common.notification.utility.Constants;
import app.trigyn.core.templating.service.DBTemplatingService;
import app.trigyn.core.templating.utils.TemplatingUtils;
import app.trigyn.core.templating.vo.TemplateVO;

@RestController
@RequestMapping("/cf")
public class NotificationCrudController {
	
	private final static Logger logger = LogManager.getLogger(NotificationCrudController.class);
    
    @Autowired
	private DBTemplatingService templatingService = null;

	@Autowired
	private TemplatingUtils templateEngine = null;

	@Autowired
	private NotificationService notificationService = null;
	
	@Autowired
	private IUserDetailsService userDetailsService = null;

	@GetMapping(value = "/nl", produces = MediaType.TEXT_HTML_VALUE)
	public String getGenericUserNotificationHome(HttpSession session, HttpServletRequest request) throws Exception {
		TemplateVO templateVO = templatingService.getTemplateByName(Constants.GENERIC_USER_NOTIFICATION);
		return templateEngine.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(), new HashMap<>());
	}

	@GetMapping(value = "/aen", produces = MediaType.TEXT_HTML_VALUE)
	public String createNewUserNotification(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws Exception {
		TemplateVO templateVO = templatingService.getTemplateByName(Constants.CREATE_NEW_USER_NOTIFICATION);
		Map<String, Object> templateDetails = new HashMap<>();
		String dateFormat = Constants.DATE_FORMAT;
		String dateFormat_JS = Constants.DATE_FORMAT_JS;
		templateDetails.put("dateFormatter", new SimpleDateFormat(dateFormat));
		templateDetails.put("dataFormat_JS", dateFormat_JS);
		String notificationId = request.getParameter("notificationId");
		if (notificationId != null) {
			GenericUserNotification genericUserNotificationDetails = notificationService.getNotification(notificationId);
			if (genericUserNotificationDetails == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid Notification Id");
				return null;
			}
			String obj = new Gson().toJson(genericUserNotificationDetails);
			
			templateDetails.put("genericUserNotificationDetails", obj);
			templateDetails.put("isEdited", true);
		}else{
			templateDetails.put("isEdited", false);
		}
		return templateEngine.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(), templateDetails);
	}

	@PostMapping(value = "/sn", produces = MediaType.APPLICATION_JSON_VALUE)
	public Boolean saveEditedNotification(HttpSession session, HttpServletRequest request) throws Exception {
		UserDetailsVO detailsVO = userDetailsService.getUserDetails();
		String updatedBy = detailsVO.getUserId();
		notificationService.saveEditedNotificationData(GenericUserNotification.createGenericUserNotification(updatedBy, Constants.DATE_FORMAT, request.getParameterMap()));
		return true;
    }
    
}
