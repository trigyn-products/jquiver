package com.trigyn.jws.webstarter.controller;

import java.util.concurrent.CompletableFuture;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.webstarter.service.SendMailService;
import com.trigyn.jws.webstarter.utils.Email;


@RestController
@RequestMapping("/cf")
public class SendMailController {
	
	@Autowired
	private SendMailService sendMailService = null;
	
	

	@PostMapping(value="/stm",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public CompletableFuture<Boolean> sendTestMail(HttpServletRequest httpServletRequest) throws Exception {
		 Email email=new Email();
		String jsonString = httpServletRequest.getParameter("saveMailConfigDetailsJson");
		return sendMailService.sendTestMail(email,jsonString);
	}
	

	
}
