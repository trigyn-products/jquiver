package com.trigyn.jws.dynarest.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/cf")
public class SendMailController {

	@Autowired
	private SendMailService sendMailService = null;

	@Autowired
	private DBTemplatingService dbTemplatingService = null;

	@Autowired
	private IUserDetailsService userDetails = null;

	@Autowired
	private ApplicationSecurityDetails applicationSecurityDetails = null;

	@PostMapping(value = "/stm", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Boolean sendTestMail(HttpServletRequest httpServletRequest) throws Exception {
		Email email = new Email();
		String jsonString = httpServletRequest.getParameter("saveMailConfigDetailsJson");
		TemplateVO templateDetails = dbTemplatingService.getTemplateByName(Constant.MAIL_TEMPLATE_NAME);
		email.setSubject(Constant.TEST_MAIL_SUBJECT);
		email.setBody(templateDetails.getTemplate());
		/* For inserting notification in case of mail failure only on access of Admin */
		email.setIsAuthenticationEnabled(applicationSecurityDetails.getIsAuthenticationEnabled());
		email.setLoggedInUserRole(userDetails.getUserDetails().getRoleIdList());
		sendMailService.sendTestMail(email, jsonString);
		return Boolean.TRUE;
	}

	@GetMapping("/downloadEmails")
	public void downloadEmailsPublic(HttpServletRequest request, HttpServletResponse response) {

		try {
			String filePath = request.getParameter("emlFilePath");
			File file = new File(filePath);

			if (!file.exists()) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter().write("File not found");
				return;
			}

			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
			response.setContentLengthLong(file.length());

			// 🔥 VERY IMPORTANT (prevents 412)
			response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);

			FileInputStream fis = new FileInputStream(file);
			OutputStream os = response.getOutputStream();

			byte[] buffer = new byte[8192];
			int bytesRead;

			while ((bytesRead = fis.read(buffer)) != -1) {
				os.write(buffer, 0, bytesRead);
			}

			os.flush();
			fis.close();

		} catch (Exception e) {
			e.printStackTrace();
			try {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().write("Error downloading file");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

}
