package com.trigyn.jws.webstarter.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.notification.entities.GenericUserNotification;
import com.trigyn.jws.notification.service.NotificationService;
import com.trigyn.jws.notification.utility.Constants;
import com.trigyn.jws.templating.service.MenuService;

@RestController
@RequestMapping("/cf")
public class NotificationCrudController {
	
	private final static Logger logger 							= LogManager.getLogger(NotificationCrudController.class);
    
	@Autowired
	private NotificationService notificationService 			= null;
	
	@Autowired
	private IUserDetailsService userDetailsService 				= null;
	
	@Autowired
	private MenuService 		menuService						= null;
	

	@GetMapping(value = "/nl", produces = MediaType.TEXT_HTML_VALUE)
	public String getGenericUserNotificationHome(HttpSession session, HttpServletRequest request, HttpServletResponse httpServletResponse) throws IOException {
		try {
			return menuService.getTemplateWithSiteLayout(Constants.GENERIC_USER_NOTIFICATION, new HashMap<>());
		} catch (Exception exception) {
			logger.error("Error ", exception);
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
			return null;
		}
	}

	@GetMapping(value = "/aen", produces = MediaType.TEXT_HTML_VALUE)
	public String createNewUserNotification(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		try {
			String dateFormat 					= Constants.DATE_FORMAT;
			String dateFormat_JS 				= Constants.DATE_FORMAT_JS;
			Map<String, Object> templateDetails = new HashMap<>();
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
			return menuService.getTemplateWithSiteLayout(Constants.CREATE_NEW_USER_NOTIFICATION, templateDetails);
		} catch (Exception exception) {
			logger.error("Error ", exception);
			response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
			return null;
		}
	}

	@PostMapping(value = "/sn", produces = MediaType.APPLICATION_JSON_VALUE)
	public Boolean saveEditedNotification(HttpSession session, HttpServletRequest request) throws Exception {
		UserDetailsVO detailsVO 	= userDetailsService.getUserDetails();
		String updatedBy 			= detailsVO.getUserId();
		notificationService.saveEditedNotificationData(GenericUserNotification.createGenericUserNotification(updatedBy, Constants.DATE_FORMAT, request.getParameterMap()));
		return true;
    }
    
}
