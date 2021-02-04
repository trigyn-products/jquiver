package com.trigyn.jws.webstarter.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.dbutils.utils.Constant;
import com.trigyn.jws.templating.service.DBTemplatingService;
import com.trigyn.jws.templating.vo.TemplateVO;
import com.trigyn.jws.webstarter.service.SendMailService;
import com.trigyn.jws.webstarter.utils.Email;

@RestController
@RequestMapping("/cf")
public class SendMailController {

	@Autowired
	private SendMailService		sendMailService		= null;

	@Autowired
	private DBTemplatingService	dbTemplatingService	= null;

	@PostMapping(value = "/stm", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Boolean sendTestMail(HttpServletRequest httpServletRequest) throws Exception {
		Email		email			= new Email();
		String		jsonString		= httpServletRequest.getParameter("saveMailConfigDetailsJson");
		TemplateVO	templateDetails	= dbTemplatingService.getTemplateByName(Constant.MAIL_TEMPLATE_NAME);
		email.setBody(templateDetails.getTemplate());
		sendMailService.sendTestMail(email, jsonString);
		return Boolean.TRUE;
	}

}
