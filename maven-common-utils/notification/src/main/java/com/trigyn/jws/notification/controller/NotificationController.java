package com.trigyn.jws.notification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.notification.service.NotificationService;

@RestController
@RequestMapping(value = "/cf")
public class NotificationController {

	@Autowired
	private NotificationService notificationService = null;

	@GetMapping(value = "/lns", produces = MediaType.TEXT_HTML_VALUE)
	public String loadNotifications(@RequestParam(name = "contextName") String contextName) throws Exception {
		return notificationService.loadNotifications("loadNotifications", contextName);
	}

}