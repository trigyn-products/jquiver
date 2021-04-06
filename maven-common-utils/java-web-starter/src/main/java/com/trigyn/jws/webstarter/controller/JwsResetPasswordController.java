package com.trigyn.jws.webstarter.controller;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.mail.internet.InternetAddress;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.templating.service.DBTemplatingService;
import com.trigyn.jws.templating.utils.TemplatingUtils;
import com.trigyn.jws.templating.vo.TemplateVO;
import com.trigyn.jws.usermanagement.entities.JwsResetPasswordToken;
import com.trigyn.jws.usermanagement.entities.JwsUser;
import com.trigyn.jws.usermanagement.repository.JwsResetPasswordTokenRepository;
import com.trigyn.jws.usermanagement.repository.JwsUserRepository;
import com.trigyn.jws.usermanagement.security.config.ApplicationSecurityDetails;
import com.trigyn.jws.usermanagement.service.UserConfigService;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.webstarter.service.SendMailService;
import com.trigyn.jws.webstarter.service.UserManagementService;
import com.trigyn.jws.webstarter.utils.Email;

@RestController
@RequestMapping("/cf")
public class JwsResetPasswordController {

	@Autowired
	private JwsUserRepository				userRepository					= null;

	@Autowired
	private JwsResetPasswordTokenRepository	resetPasswordTokenRepository	= null;

	@Autowired
	private PasswordEncoder					passwordEncoder					= null;

	@Autowired
	private UserManagementService			userManagementService			= null;

	@Autowired
	private DBTemplatingService				templatingService				= null;

	@Autowired
	private TemplatingUtils					templatingUtils					= null;

	@Autowired
	private ApplicationSecurityDetails		applicationSecurityDetails		= null;

	@Autowired
	private SendMailService					sendMailService					= null;

	@Autowired
	private UserConfigService				userConfigService				= null;

	@Autowired
	private PropertyMasterService			propertyMasterService			= null;

	@Autowired
	private ServletContext					servletContext					= null;

	@GetMapping(value = "/resetPasswordPage")
	@ResponseBody
	public String displayResetPasswordPage(ModelAndView modelAndView, HttpServletResponse response) throws Exception {
		Map<String, Object> mapDetails = new HashMap<>();
		if (applicationSecurityDetails.getIsAuthenticationEnabled()) {
			TemplateVO templateVO = templatingService.getTemplateByName("jws-password-reset-mail");
			userConfigService.getConfigurableDetails(mapDetails);
			return templatingUtils.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(),
					mapDetails);
		} else {
			response.sendError(HttpStatus.FORBIDDEN.value(), "You dont have rights to access these module");
			return null;
		}
	}

	@PostMapping(value = "/sendResetPasswordMail")
	@ResponseBody
	public String sendResetPasswordMail(HttpServletRequest request, HttpSession session, HttpServletResponse response)
			throws Exception {

		String				emailTo		= request.getParameter("email");
		Map<String, Object>	mapDetails	= new HashMap<>();
		String				viewName	= null;

		if (applicationSecurityDetails.getIsAuthenticationEnabled()) {
			userConfigService.getConfigurableDetails(mapDetails);
			JwsUser existingUser = userManagementService.findByEmailIgnoreCase(emailTo);
			if (existingUser != null) {

				if (mapDetails.get("enableCaptcha").toString().equalsIgnoreCase("true")
						&& session.getAttribute("resetCaptcha") != null && !(request.getParameter("captcha").toString()
								.equals(session.getAttribute("resetCaptcha").toString()))) {
					mapDetails.put("invalidCaptcha", "Please verify captcha!");
					viewName = "jws-password-reset-mail";
					TemplateVO templateVO = templatingService.getTemplateByName(viewName);
					return templatingUtils.processTemplateContents(templateVO.getTemplate(),
							templateVO.getTemplateName(), mapDetails);
				} else if (mapDetails.get("enableCaptcha").toString().equalsIgnoreCase("true")) {
					session.removeAttribute("resetCaptcha");
				}
				existingUser.setIsActive(Constants.INACTIVE);
				userRepository.save(existingUser);
				JwsResetPasswordToken	resetPassword	= new JwsResetPasswordToken();
				String					tokenId			= UUID.randomUUID().toString();
				resetPassword.setTokenId(tokenId);
				resetPassword.setPasswordResetTime(Calendar.getInstance());
				resetPassword.setUserId(existingUser.getUserId());
				String baseURL = UserManagementService.getBaseURL(propertyMasterService, servletContext);
				resetPassword.setResetPasswordUrl(baseURL + "/cf/resetPassword?token=" + tokenId);
				resetPassword.setIsResetUrlExpired(Boolean.FALSE);
				resetPasswordTokenRepository.save(resetPassword);

				Email email = new Email();
				email.setInternetAddressToArray(InternetAddress.parse(emailTo));

				// email.setMailFrom("admin@jquiver.com");
				Map<String, Object>	mailDetails			= new HashMap<>();
				TemplateVO			subjectTemplateVO	= templatingService
						.getTemplateByName("reset-password-mail-subject");
				String				subject				= templatingUtils.processTemplateContents(
						subjectTemplateVO.getTemplate(), subjectTemplateVO.getTemplateName(), mailDetails);
				email.setSubject(subject);

				mailDetails.put("baseURL", baseURL);
				mailDetails.put("tokenId", tokenId);
				TemplateVO	templateVO	= templatingService.getTemplateByName("reset-password-mail");
				String		mailBody	= templatingUtils.processTemplateContents(templateVO.getTemplate(),
						templateVO.getTemplateName(), mailDetails);
				email.setBody(mailBody);
				System.out.println(mailBody);
				sendMailService.sendTestMail(email);

				mapDetails.put("successResetPasswordMsg",
						"Check your email for a link to reset your password. If it doesn’t appear within a few minutes, check your spam folder.");
				viewName = "jws-password-reset-mail-success";
			} else {
				mapDetails.put("nonRegisteredUser", "Could not send email to entered mail id");
				viewName = "jws-password-reset-mail";
			}
			TemplateVO templateVO = templatingService.getTemplateByName(viewName);
			return templatingUtils.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(),
					mapDetails);
		} else {
			response.sendError(HttpStatus.FORBIDDEN.value(), "You dont have rights to access these module");
			return null;
		}
	}

	@GetMapping(value = "/resetPassword")
	@ResponseBody
	public String resetPasswordByURL(@RequestParam("token") String tokenId, HttpServletResponse response)
			throws Exception {

		Map<String, Object>	mapDetails	= new HashMap<>();
		String				viewName	= null;
		if (applicationSecurityDetails.getIsAuthenticationEnabled()) {
			userConfigService.getConfigurableDetails(mapDetails);
			JwsResetPasswordToken	tokenDetails	= resetPasswordTokenRepository.findByTokenId(tokenId);
			Boolean					isInvalidLink	= false;
			if (tokenDetails != null && tokenDetails.getPasswordResetTime() != null
					&& tokenDetails.getIsResetUrlExpired() != Boolean.TRUE) {
				long		linkSendTimestamp	= tokenDetails.getPasswordResetTime().getTimeInMillis();
				long		maxLinkActiveTime	= TimeUnit.MINUTES.toMillis(20);
				Timestamp	currentTimestamp	= new Timestamp(System.currentTimeMillis());
				long		milliseconds		= currentTimestamp.getTime() - linkSendTimestamp;

				if (milliseconds > maxLinkActiveTime) {
					isInvalidLink = Boolean.TRUE;
				} else {

					JwsUser userDetails = userRepository.findByUserId(tokenDetails.getUserId());
					if (userDetails != null && userDetails.getEmail() != null) {
						mapDetails.put("resetEmailId", userDetails.getEmail());
					}
					mapDetails.put("token", tokenId);
					viewName = "jws-password-reset-page";
				}
			} else {
				isInvalidLink = Boolean.TRUE;
			}

			if (isInvalidLink) {
				mapDetails.put("inValidLink",
						"The link is expired/invalid/broken.Please enter mail id again to get reset password link!");
				viewName = "jws-password-reset-mail";
			}

			TemplateVO templateVO = templatingService.getTemplateByName(viewName);
			return templatingUtils.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(),
					mapDetails);
		} else {
			response.sendError(HttpStatus.FORBIDDEN.value(), "You dont have rights to access these module");
			return null;
		}
	}

	@PostMapping(value = "/createPassword")
	@ResponseBody
	public String createPassword(HttpServletRequest request, HttpSession session, HttpServletResponse response)
			throws Exception {
		Map<String, Object>	mapDetails		= new HashMap<>();
		String				viewName		= null;

		String				password		= request.getParameter("password");
		String				confirmpassword	= request.getParameter("confirmpassword");
		String				resetEmailId	= request.getParameter("resetEmailId");
		String				tokenId			= request.getParameter("token");
		mapDetails.put("token", tokenId);
		if (applicationSecurityDetails.getIsAuthenticationEnabled()) {
			userConfigService.getConfigurableDetails(mapDetails);
			if (password == null || (password != null && password.trim().isEmpty()) || confirmpassword == null
					|| (confirmpassword != null && confirmpassword.trim().isEmpty())) {
				mapDetails.put("nonValidPassword", "Enter valid password and confirm password");
				viewName = "jws-password-reset-page";
			} else if (!password.equals(confirmpassword)) {
				mapDetails.put("nonValidPassword", "Enter same password and confirm password");
				viewName = "jws-password-reset-page";
			} else if (password.equals(confirmpassword)) {
				if (userManagementService.validatePassword(password)) {
					if (mapDetails.get("enableCaptcha").toString().equalsIgnoreCase("true")
							&& session.getAttribute("createCaptcha") != null && !(request.getParameter("captcha")
									.toString().equals(session.getAttribute("createCaptcha").toString()))) {
						mapDetails.put("invalidCaptcha", "Please verify captcha!");
						viewName = "jws-password-reset-page";
					} else {
						if (mapDetails.get("enableCaptcha").toString().equalsIgnoreCase("true")) {
							session.removeAttribute("createCaptcha");
						}
						String	encodedPassword	= passwordEncoder.encode(password);
						JwsUser	user			= userRepository.findByEmailIgnoreCase(resetEmailId);
						user.setIsActive(Constants.ISACTIVE);
						user.setPassword(encodedPassword);
						userRepository.save(user);

						if (applicationSecurityDetails.getIsAuthenticationEnabled()) {
							mapDetails.put("authenticationType", applicationSecurityDetails.getAuthenticationType());
						}

						resetPasswordTokenRepository.updateUrlExpired(Boolean.TRUE, user.getUserId(), tokenId);
						mapDetails.put("resetPasswordSuccess",
								"Congratulations.You have successfully changed your password.");
						viewName = "jws-login";
					}
				} else {
					viewName = "jws-password-reset-page";
					mapDetails.put("nonValidPassword",
							"Password must contain atleast 6 characters including UPPER/lowercase/Special charcters and numbers ");
				}

			}
			mapDetails.put("resetEmailId", resetEmailId);
			TemplateVO templateVO = templatingService.getTemplateByName(viewName);
			return templatingUtils.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(),
					mapDetails);
		} else {
			response.sendError(HttpStatus.FORBIDDEN.value(), "You dont have rights to access these module");
			return null;
		}

	}

	@GetMapping(value = "/changePassword")
	@ResponseBody
	public String changePasswordPage(@RequestParam("token") String tokenId, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String				isChangePassword	= request.getParameter("icp");
		Map<String, Object>	mapDetails			= new HashMap<>();
		if (applicationSecurityDetails.getIsAuthenticationEnabled()) {
			userConfigService.getConfigurableDetails(mapDetails);
			if (StringUtils.isNotBlank(tokenId)) {

				JwsUser jwsUser = userRepository.findByUserId(tokenId);
				if (jwsUser != null
						&& (jwsUser.getForcePasswordChange() == Constants.ISACTIVE || "1".equals(isChangePassword))) {
					mapDetails.put("tokenId", tokenId);
					mapDetails.put("icp", isChangePassword);
				} else {
					response.sendError(HttpStatus.FORBIDDEN.value(), "You dont have rights to access these page");
					return null;
				}

			} else {
				response.sendError(HttpStatus.FORBIDDEN.value(), "You dont have rights to access these page");
				return null;
			}

			TemplateVO templateVO = templatingService.getTemplateByName("jws-change-password");
			return templatingUtils.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(),
					mapDetails);
		} else {
			response.sendError(HttpStatus.FORBIDDEN.value(), "You dont have rights to access these module");
			return null;
		}
	}

	@PostMapping(value = "/updatePassword")
	@ResponseBody
	public String updatePasswordPage(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws Exception {

		Map<String, Object>	mapDetails			= new HashMap<>();
		String				viewName			= null;
		String				isChangePassword	= request.getParameter("icp");

		if (applicationSecurityDetails.getIsAuthenticationEnabled()) {
			userConfigService.getConfigurableDetails(mapDetails);
			String	tokenId		= request.getParameter("tokenId");
			String	oldPassword	= request.getParameter("password");
			String	newPassword	= request.getParameter("newPassword");
			mapDetails.put("tokenId", tokenId);
			JwsUser jwsUser = userRepository.findByUserId(tokenId);

			if (jwsUser != null
					&& (jwsUser.getForcePasswordChange() == Constants.ISACTIVE || "1".equals(isChangePassword))) {
				BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
				if (bcrypt.matches(newPassword, jwsUser.getPassword())) {
					viewName = "jws-change-password";
					mapDetails.put("errorMessage", "Old Password and new password cannot be same");
				} else {
					if (BCrypt.checkpw(oldPassword, jwsUser.getPassword())) {
						if (userManagementService.validatePassword(newPassword)) {

							if (mapDetails.get("enableCaptcha").toString().equalsIgnoreCase("true")
									&& session.getAttribute("updateCaptcha") != null
									&& !(request.getParameter("captcha").toString()
											.equals(session.getAttribute("updateCaptcha").toString()))) {
								mapDetails.put("invalidCaptcha", "Please verify captcha!");
								mapDetails.put("icp", isChangePassword);
								viewName = "jws-change-password";
							} else {
								if (mapDetails.get("enableCaptcha").toString().equalsIgnoreCase("true")) {
									session.removeAttribute("updateCaptcha");
								}
								jwsUser.setIsActive(Constants.ISACTIVE);
								jwsUser.setPassword(passwordEncoder.encode(newPassword));
								jwsUser.setForcePasswordChange(Constants.INACTIVE);
								jwsUser.setLastPasswordUpdatedDate(new Date());
								userRepository.save(jwsUser);

								if (applicationSecurityDetails.getIsAuthenticationEnabled()) {
									mapDetails.put("authenticationType",
											applicationSecurityDetails.getAuthenticationType());
								}

								mapDetails.put("resetPasswordSuccess",
										"Congratulations.You have successfully updated your password.");
								viewName = "jws-login";
							}
						} else {
							viewName = "jws-change-password";
							mapDetails.put("icp", isChangePassword);
							mapDetails.put("errorMessage",
									"Password must contain atleast 6 characters including UPPER/lowercase/Special charcters and numbers ");
						}
					} else {
						viewName = "jws-change-password";
						mapDetails.put("icp", isChangePassword);
						mapDetails.put("errorMessage",
								"Check System generated Password or ask admin to change the password");
					}
				}
			} else {
				response.sendError(HttpStatus.FORBIDDEN.value(), "You dont have rights to access these page");
				return null;
			}

			TemplateVO templateVO = templatingService.getTemplateByName(viewName);
			return templatingUtils.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(),
					mapDetails);
		} else {
			response.sendError(HttpStatus.FORBIDDEN.value(), "You dont have rights to access these module");
			return null;
		}

	}

	@GetMapping(value = "/configureTOTP")
	@ResponseBody
	public String configureTOTPPage(ModelAndView modelAndView, HttpServletResponse response) throws Exception {
		Map<String, Object> mapDetails = new HashMap<>();
		if (applicationSecurityDetails.getIsAuthenticationEnabled()) {
			TemplateVO templateVO = templatingService.getTemplateByName("jws-configure-totp");
			userConfigService.getConfigurableDetails(mapDetails);
			return templatingUtils.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(),
					mapDetails);
		} else {
			response.sendError(HttpStatus.FORBIDDEN.value(), "You dont have rights to access these module");
			return null;
		}
	}

	@PostMapping(value = "/sendConfigureTOTPMail")
	@ResponseBody
	public String sendConfigureTOTPMail(HttpServletRequest request, HttpSession session, HttpServletResponse response)
			throws Exception {

		String				emailTo		= request.getParameter("email");
		Map<String, Object>	mapDetails	= new HashMap<>();
		String				viewName	= null;

		if (applicationSecurityDetails.getIsAuthenticationEnabled()) {
			userConfigService.getConfigurableDetails(mapDetails);
			JwsUser existingUser = userManagementService.findByEmailIgnoreCase(emailTo);
			if (existingUser != null) {
				Email email = new Email();
				userManagementService.sendMailForTotpAuthentication(existingUser, email);

				mapDetails.put("successResetPasswordMsg",
						"Check your email for a instructions to login through Google AUtheticator. If it doesn’t appear within a few minutes, check your spam folder.");
				viewName = "jws-password-reset-mail-success";
			} else {
				mapDetails.put("nonRegisteredUser", "Could not send email to entered mail id");
				viewName = "jws-configure-totp";
			}
			TemplateVO templateVO = templatingService.getTemplateByName(viewName);
			return templatingUtils.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(),
					mapDetails);
		} else {
			response.sendError(HttpStatus.FORBIDDEN.value(), "You dont have rights to access these module");
			return null;
		}
	}

}
