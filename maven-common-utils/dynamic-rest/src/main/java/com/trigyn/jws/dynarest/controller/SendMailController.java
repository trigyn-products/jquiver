package com.trigyn.jws.dynarest.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.utils.Constant;
import com.trigyn.jws.dynarest.service.SendMailService;
import com.trigyn.jws.dynarest.vo.Email;
import com.trigyn.jws.templating.service.DBTemplatingService;
import com.trigyn.jws.templating.vo.TemplateVO;
import com.trigyn.jws.usermanagement.security.config.ApplicationSecurityDetails;

@RestController
@RequestMapping("/cf")
public class SendMailController {

	@Autowired
	private SendMailService		sendMailService		= null;

	@Autowired
	private DBTemplatingService	dbTemplatingService	= null;
	
	@Autowired
	private IUserDetailsService					userDetails						= null;
	
	@Autowired
	private ApplicationSecurityDetails			applicationSecurityDetails		= null;

	@PostMapping(value = "/stm", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Boolean sendTestMail(HttpServletRequest httpServletRequest) throws Exception {
		Email		email			= new Email();
		String		jsonString		= httpServletRequest.getParameter("saveMailConfigDetailsJson");
		TemplateVO	templateDetails	= dbTemplatingService.getTemplateByName(Constant.MAIL_TEMPLATE_NAME);
		email.setSubject(Constant.TEST_MAIL_SUBJECT);
		email.setBody(templateDetails.getTemplate());
		/*For inserting notification in case of mail failure only on access of Admin*/
		email.setIsAuthenticationEnabled(applicationSecurityDetails.getIsAuthenticationEnabled());
		email.setLoggedInUserRole(userDetails.getUserDetails().getRoleIdList());
		sendMailService.sendTestMail(email, jsonString);
		return Boolean.TRUE;
	}

}
