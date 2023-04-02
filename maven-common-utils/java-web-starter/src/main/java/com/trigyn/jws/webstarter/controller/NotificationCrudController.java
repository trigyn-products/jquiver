package com.trigyn.jws.webstarter.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.webstarter.utils.Constant;

@RestController
@RequestMapping("/cf")
@PreAuthorize("hasPermission('module','Notification')")
public class NotificationCrudController {

	private final static Logger	logger				= LogManager.getLogger(NotificationCrudController.class);

	@Autowired
	private MenuService			menuService			= null;

	@GetMapping(value = "/nl", produces = MediaType.TEXT_HTML_VALUE)
	public String getGenericUserNotificationHome(HttpSession session, HttpServletRequest request, HttpServletResponse httpServletResponse)
			throws IOException {
		try {
			return menuService.getTemplateWithSiteLayout(Constant.GENERIC_USER_NOTIFICATION, new HashMap<>());
		} catch (Exception a_exception) {
			logger.error("Error occured while loading Notification Listing Page.", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}
	}

}
