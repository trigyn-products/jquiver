package com.trigyn.jws.webstarter.controller;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
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
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.utils.CustomStopException;
import com.trigyn.jws.dbutils.utils.FileUtilities;
import com.trigyn.jws.dynarest.service.SendMailService;
import com.trigyn.jws.dynarest.vo.Email;
import com.trigyn.jws.templating.service.DBTemplatingService;
import com.trigyn.jws.templating.utils.TemplatingUtils;
import com.trigyn.jws.templating.vo.TemplateVO;
import com.trigyn.jws.usermanagement.entities.JwsResetPasswordToken;
import com.trigyn.jws.usermanagement.entities.JwsUser;
import com.trigyn.jws.usermanagement.exception.InvalidLoginException;
import com.trigyn.jws.usermanagement.repository.JwsResetPasswordTokenRepository;
import com.trigyn.jws.usermanagement.repository.JwsUserRepository;
import com.trigyn.jws.usermanagement.security.config.ApplicationSecurityDetails;
import com.trigyn.jws.usermanagement.security.config.JwtUtil;
import com.trigyn.jws.usermanagement.service.UserConfigService;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.usermanagement.vo.JwtRequestDetails;
import com.trigyn.jws.webstarter.dao.ICaptchRepository;
import com.trigyn.jws.webstarter.service.CaptchaService;
import com.trigyn.jws.webstarter.service.UserManagementService;
import com.trigyn.jws.webstarter.vo.CaptchaDetails;

import jakarta.mail.internet.InternetAddress;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/cf")
public class JwsResetPasswordController {

	private final static Logger				logger							= LoggerFactory
			.getLogger(JwsResetPasswordController.class);

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

	@Autowired

	private IUserDetailsService				userDetailsService				= null;

	@Autowired
	private JwtUtil							jwtUtil							= null;

	@Autowired
	@Lazy
	private UserDetailsService				userDetailsServiceImpl			= null;

	@Autowired
	private FileUtilities					fileUtilities					= null;

	@Autowired
	private ICaptchRepository				iCaptchRepository				= null;

	@Autowired
	private CaptchaService					captchaService					= null;

	@GetMapping(value = "/resetPasswordPage")
	@ResponseBody
	public String displayResetPasswordPage(ModelAndView modelAndView, HttpServletResponse response)
			throws Exception, CustomStopException {

		Map<String, Object> mapDetails = new HashMap<>();
		try {
			if (applicationSecurityDetails.getIsAuthenticationEnabled()) {
				TemplateVO templateVO = templatingService.getTemplateByName("jws-password-reset-mail");
				userConfigService.getConfigurableDetails(mapDetails);
				return templatingUtils.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(),
						mapDetails);
			} else {
				fileUtilities.customSendError(response, HttpStatus.FORBIDDEN.value(),
						"You dont have rights to access these module");
				return null;
			}
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in displayResetPasswordPage.", custStopException);
			throw custStopException;
		}
	}

	@PostMapping(value = "/sendResetPasswordMail")
	@ResponseBody

	public String sendResetPasswordMail(HttpServletRequest request, HttpSession session, HttpServletResponse response)
			throws Exception, CustomStopException {

		String				emailTo				= request.getParameter("email");
		Map<String, Object>	mapDetails			= new HashMap<>();
		String				viewName			= null;
		String				generatedCaptcha	= null;
		String				captchaRequest_Id	= request.getHeader("r");
		try {
			if (applicationSecurityDetails.getIsAuthenticationEnabled()) {
				userConfigService.getConfigurableDetails(mapDetails);
				JwsUser existingUser = userManagementService.findByEmailIgnoreCase(emailTo);
				if (existingUser != null) {

					// if (mapDetails.get("enableCaptcha").toString().equalsIgnoreCase("true")
					// && session.getAttribute("resetCaptcha") != null &&
					// !(request.getParameter("captcha")
					// .toString().equals(session.getAttribute("resetCaptcha").toString()))) {
					// mapDetails.put("invalidCaptcha", "Please verify captcha!");
					// mapDetails.put("previousMail", existingUser.getEmail());
					// viewName = "jws-password-reset-mail";
					// TemplateVO templateVO = templatingService.getTemplateByName(viewName);
					// return templatingUtils.processTemplateContents(templateVO.getTemplate(),
					// templateVO.getTemplateName(), mapDetails);
					// } else if
					// (mapDetails.get("enableCaptcha").toString().equalsIgnoreCase("true")) {
					// session.removeAttribute("resetCaptcha");
					// }
					captchaService.deleteExpiredCaptcha();
					CaptchaDetails captchaDetails = null;
					if (mapDetails.get("enableCaptcha").toString().equalsIgnoreCase("true")
							&& StringUtils.isBlank(captchaRequest_Id) == false) {
						captchaDetails = iCaptchRepository.findById(captchaRequest_Id).orElse(null);
						;
						if (null != captchaDetails) {
							generatedCaptcha = captchaDetails.getCaptcha();
						} else {
							mapDetails.put("invalidCaptcha", "Captcha has Expired!");
							mapDetails.put("previousMail", existingUser.getEmail());
							viewName = "jws-password-reset-mail";
							TemplateVO templateVO = templatingService.getTemplateByName(viewName);
							return templatingUtils.processTemplateContents(templateVO.getTemplate(),
									templateVO.getTemplateName(), mapDetails);
						}
					}
					if (mapDetails.get("enableCaptcha").toString().equalsIgnoreCase("true") && generatedCaptcha != null
							&& !(request.getParameter("captcha").toString().equals(generatedCaptcha.toString()))) {
						mapDetails.put("invalidCaptcha", "Please verify captcha!");
						if (StringUtils.isBlank(captchaRequest_Id) == false) {
							captchaService.deleteCaptcha(captchaRequest_Id);
						}
						mapDetails.put("previousMail", existingUser.getEmail());
						viewName = "jws-password-reset-mail";
						TemplateVO templateVO = templatingService.getTemplateByName(viewName);
						return templatingUtils.processTemplateContents(templateVO.getTemplate(),
								templateVO.getTemplateName(), mapDetails);
					} else if (mapDetails.get("enableCaptcha").toString().equalsIgnoreCase("true")) {
						// session.removeAttribute("resetCaptcha");
						if (StringUtils.isBlank(captchaRequest_Id) == false) {
							captchaService.deleteCaptcha(captchaRequest_Id);
						}
					}

					existingUser.setIsActive(Constants.INACTIVE);
					existingUser.setForcePasswordChange(Constants.INACTIVE);
					userRepository.save(existingUser);
					JwsResetPasswordToken	resetPassword	= new JwsResetPasswordToken();
					String					tokenId			= UUID.randomUUID().toString();
					resetPassword.setTokenId(tokenId);
					resetPassword.setPasswordResetTime(Calendar.getInstance());
					resetPassword.setUserId(existingUser.getUserId());
					String baseURL = UserManagementService.getBaseURL(propertyMasterService, servletContext);

					if (baseURL != null && baseURL.endsWith("/")) {
						baseURL = baseURL.substring(0, baseURL.length() - 1);
					}
					resetPassword.setResetPasswordUrl(baseURL + "/cf/resetPassword?token=" + tokenId);
					resetPassword.setIsResetUrlExpired(Boolean.FALSE);
					resetPasswordTokenRepository.save(resetPassword);

					Email email = new Email();
					email.setInternetAddressToArray(InternetAddress.parse(emailTo));
					/* For inserting notification in case of mail failure only on access of Admin */
					email.setIsAuthenticationEnabled(applicationSecurityDetails.getIsAuthenticationEnabled());
					email.setLoggedInUserRole(userDetailsService.getUserDetails().getRoleIdList());
					// email.setMailFrom("admin@jquiver.com");
					Map<String, Object>	mailDetails			= new HashMap<>();
					TemplateVO			subjectTemplateVO	= templatingService
							.getTemplateByName("reset-password-mail-subject");
					String				subject				= templatingUtils.processTemplateContents(
							subjectTemplateVO.getTemplate(), subjectTemplateVO.getTemplateName(), mailDetails);
					email.setSubject(subject);
					String	propertyAdminEmailId	= propertyMasterService.findPropertyMasterValue("system", "system",
							"adminEmailId");
					String	adminEmail				= propertyAdminEmailId == null ? "admin@jquiver.io"
							: propertyAdminEmailId.equals("") ? "admin@jquiver.io" : propertyAdminEmailId;

					mailDetails.put("firstName", existingUser.getFirstName() + " " + existingUser.getLastName());
					mailDetails.put("baseURL", baseURL);
					mailDetails.put("tokenId", tokenId);
					mailDetails.put("adminEmailAddress", adminEmail);
					TemplateVO	templateVO	= templatingService.getTemplateByName("reset-password-mail");
					String		mailBody	= templatingUtils.processTemplateContents(templateVO.getTemplate(),
							templateVO.getTemplateName(), mailDetails);
					email.setBody(mailBody);
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
				fileUtilities.customSendError(response, HttpStatus.FORBIDDEN.value(),
						"You dont have rights to access these module");
				return null;
			}
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in sendResetPasswordMail.", custStopException);
			throw custStopException;
		}
	}

	@GetMapping(value = "/resetPassword")
	@ResponseBody
	public String resetPasswordByURL(@RequestParam("token") String tokenId, HttpServletResponse response)
			throws Exception, CustomStopException {

		Map<String, Object>	mapDetails	= new HashMap<>();
		String				viewName	= null;
		try {
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
				fileUtilities.customSendError(response, HttpStatus.FORBIDDEN.value(),
						"You dont have rights to access these module");
				return null;
			}
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in resetPasswordByURL.", custStopException);
			throw custStopException;
		}
	}

	@PostMapping(value = "/createPassword")
	@ResponseBody
	public String createPassword(HttpServletRequest request, HttpSession session, HttpServletResponse response)
			throws Exception, CustomStopException {
		Map<String, Object>	mapDetails					= new HashMap<>();
		String				viewName					= null;
		String				decryptedPassword			= null;
		String				decryptedConfirmPassword	= null;
		String				requestId					= null;
		String				password					= null;
		String				confirmpassword				= null;
		String				generatedCaptcha			= null;
		String				captchaRequest_Id			= request.getHeader("r");
		if (request.getParameter("password") != null) {
			password = request.getParameter("password");
		}
		if (request.getParameter("confirmpassword") != null) {
			confirmpassword = request.getParameter("confirmpassword");
		}
		String	resetEmailId	= request.getParameter("resetEmailId");
		String	tokenId			= request.getParameter("token");
		if (request.getParameter("requestId") != null) {
			requestId = request.getParameter("requestId");
		}
		decryptedPassword			= userConfigService.decryptPassword(password, requestId);
		decryptedConfirmPassword	= userConfigService.decryptPassword(confirmpassword, requestId);
		try {
			mapDetails.put("token", tokenId);
			if (applicationSecurityDetails.getIsAuthenticationEnabled()) {
				userConfigService.getConfigurableDetails(mapDetails);
				if (decryptedPassword == null || (decryptedPassword != null && decryptedPassword.trim().isEmpty())
						|| decryptedConfirmPassword == null
						|| (decryptedConfirmPassword != null && decryptedConfirmPassword.trim().isEmpty())) {
					mapDetails.put("nonValidPassword", "Enter valid password and confirm password");
					viewName = "jws-password-reset-page";
				} else if (!decryptedPassword.equals(decryptedConfirmPassword)) {
					mapDetails.put("nonValidPassword", "Enter same password and confirm password");
					viewName = "jws-password-reset-page";
				} else if (decryptedPassword.equals(decryptedConfirmPassword)) {
					if (userManagementService.validatePassword(decryptedPassword)) {

						// fetch captcha for database
						captchaService.deleteExpiredCaptcha();
						CaptchaDetails captchaDetails = null;
						if (mapDetails.get("enableCaptcha").toString().equalsIgnoreCase("true")
								&& StringUtils.isBlank(captchaRequest_Id) == false) {
							captchaDetails = iCaptchRepository.findById(captchaRequest_Id).orElse(null);
							if (null != captchaDetails) {
								generatedCaptcha = captchaDetails.getCaptcha();
							} else {
								mapDetails.put("invalidCaptcha", "Captcha has Expired!");
								viewName = "jws-password-reset-page";
								TemplateVO templateVO = templatingService.getTemplateByName(viewName);
								return templatingUtils.processTemplateContents(templateVO.getTemplate(),
										templateVO.getTemplateName(), mapDetails);
							}
						}

						if (mapDetails.get("enableCaptcha").toString().equalsIgnoreCase("true")
								&& generatedCaptcha != null
								&& !(request.getParameter("captcha").toString().equals(generatedCaptcha.toString()))) {
							mapDetails.put("invalidCaptcha", "Please verify captcha!");
							viewName = "jws-password-reset-page";
						} else {
							if (mapDetails.get("enableCaptcha").toString().equalsIgnoreCase("true")) {
								if (StringUtils.isBlank(captchaRequest_Id) == false) {
									captchaService.deleteCaptcha(captchaRequest_Id);
								}
							}
							String	encodedPassword	= passwordEncoder.encode(decryptedPassword);
							JwsUser	user			= userRepository.findByEmailIgnoreCase(resetEmailId);
							user.setIsActive(Constants.ISACTIVE);
							user.setPassword(encodedPassword);
							userRepository.save(user);

							/*
							 * if (applicationSecurityDetails.getIsAuthenticationEnabled()) {
							 * mapDetails.put("authenticationType",
							 * applicationSecurityDetails.getAuthenticationType()); }
							 */
							userConfigService.getConfigurableDetails(mapDetails);
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
				fileUtilities.customSendError(response, HttpStatus.FORBIDDEN.value(),
						"You dont have rights to access these module");
				return null;
			}
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in createPassword.", custStopException);
			throw custStopException;
		}
	}

	@GetMapping(value = "/changePassword")
	@ResponseBody
	public String changePasswordPage(@RequestParam("token") String tokenId, HttpServletRequest request,
			HttpServletResponse response) throws Exception, CustomStopException {

		String				isChangePassword	= request.getParameter("icp");
		Map<String, Object>	mapDetails			= new HashMap<>();
		try {
			if (applicationSecurityDetails.getIsAuthenticationEnabled()) {
				userConfigService.getConfigurableDetails(mapDetails);
				if (StringUtils.isNotBlank(tokenId)) {

					JwsUser jwsUser = userRepository.findByUserId(tokenId);
					if (jwsUser != null && (jwsUser.getForcePasswordChange() == Constants.ISACTIVE
							|| "1".equals(isChangePassword))) {
						mapDetails.put("tokenId", tokenId);
						mapDetails.put("icp", isChangePassword);
					} else {
						fileUtilities.customSendError(response, HttpStatus.FORBIDDEN.value(),
								"You dont have rights to access these module");
						return null;
					}

				} else {
					fileUtilities.customSendError(response, HttpStatus.FORBIDDEN.value(),
							"You dont have rights to access these module");
					return null;
				}

				TemplateVO templateVO = templatingService.getTemplateByName("jws-change-password");
				return templatingUtils.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(),
						mapDetails);
			} else {
				fileUtilities.customSendError(response, HttpStatus.FORBIDDEN.value(),
						"You dont have rights to access these module");
				return null;
			}
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in changePasswordPage.", custStopException);
			throw custStopException;
		}
	}

	@PostMapping(value = "/updatePassword")
	@ResponseBody
	public String updatePasswordPage(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws Exception, CustomStopException {

		Map<String, Object>	mapDetails			= new HashMap<>();
		String				viewName			= null;
		String				isChangePassword	= request.getParameter("icp");
		try {
			if (applicationSecurityDetails.getIsAuthenticationEnabled()) {
				userConfigService.getConfigurableDetails(mapDetails);
				String	decryptedOldPassword	= null;
				String	decryptedNewPassword	= null;
				String	requestId				= null;
				String	oldPassword				= null;
				String	newPassword				= null;
				String	tokenId					= request.getParameter("tokenId");
				if (request.getParameter("password") != null) {
					oldPassword = request.getParameter("password");
				}
				if (request.getParameter("newPassword") != null) {
					newPassword = request.getParameter("newPassword");
				}
				if (request.getParameter("requestId") != null) {
					requestId = request.getParameter("requestId");
				}
				decryptedOldPassword	= userConfigService.decryptPassword(oldPassword, requestId);
				decryptedNewPassword	= userConfigService.decryptPassword(newPassword, requestId);
				mapDetails.put("tokenId", tokenId);
				JwsUser	jwsUser				= userRepository.findByUserId(tokenId);
				String	generatedCaptcha	= null;
				String	captchaRequest_Id	= request.getHeader("r");

				if (jwsUser != null
						&& (jwsUser.getForcePasswordChange() == Constants.ISACTIVE || "1".equals(isChangePassword))) {
					BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
					if (bcrypt.matches(decryptedNewPassword, jwsUser.getPassword())) {
						viewName = "jws-change-password";
						mapDetails.put("errorMessage", "Old Password and new password cannot be same");
					} else {
						if (BCrypt.checkpw(decryptedOldPassword, jwsUser.getPassword())) {
							if (userManagementService.validatePassword(decryptedNewPassword)) {

								// fetch captcha for database
								captchaService.deleteExpiredCaptcha();
								CaptchaDetails captchaDetails = null;
								if (mapDetails.get("enableCaptcha").toString().equalsIgnoreCase("true")
										&& StringUtils.isBlank(captchaRequest_Id) == false) {
									captchaDetails = iCaptchRepository.findById(captchaRequest_Id).orElse(null);
									if (null != captchaDetails) {
										generatedCaptcha = captchaDetails.getCaptcha();
									} else {
										mapDetails.put("invalidCaptcha", "Captcha has Expired!");
										mapDetails.put("icp", isChangePassword);
										viewName = "jws-change-password";
										TemplateVO templateVO = templatingService.getTemplateByName(viewName);
										return templatingUtils.processTemplateContents(templateVO.getTemplate(),
												templateVO.getTemplateName(), mapDetails);
									}
								}
								if (mapDetails.get("enableCaptcha").toString().equalsIgnoreCase("true")
										&& generatedCaptcha != null && !(request.getParameter("captcha").toString()
												.equals(generatedCaptcha.toString()))) {
									mapDetails.put("invalidCaptcha", "Please verify captcha!");
									mapDetails.put("icp", isChangePassword);
									viewName = "jws-change-password";
								} else {
									if (mapDetails.get("enableCaptcha").toString().equalsIgnoreCase("true")) {
										// session.removeAttribute("updateCaptcha");
										if (StringUtils.isBlank(captchaRequest_Id) == false) {
											captchaService.deleteCaptcha(captchaRequest_Id);
										}
									}
									jwsUser.setIsActive(Constants.ISACTIVE);
									jwsUser.setPassword(passwordEncoder.encode(decryptedNewPassword));
									jwsUser.setForcePasswordChange(Constants.INACTIVE);
									jwsUser.setLastPasswordUpdatedDate(new Date());
									userRepository.save(jwsUser);
									/*
									 * if (applicationSecurityDetails.getIsAuthenticationEnabled()) {
									 * mapDetails.put("authenticationType",
									 * applicationSecurityDetails.getAuthenticationType()); }
									 */
									userConfigService.getConfigurableDetails(mapDetails);

									Cookie clearCookie = new Cookie("SAVED_REQUEST_URL", null);
									clearCookie.setPath(
											request.getContextPath().isEmpty() ? "/" : request.getContextPath());
									clearCookie.setMaxAge(0);
									clearCookie.setHttpOnly(true);
									response.addCookie(clearCookie);
									mapDetails.put("resetPasswordSuccess",
											"Congratulations.You have successfully updated your password.");
									// viewName = "jws-login";

									// System login flag
									Cookie systemLogin = new Cookie("SYS_LOGIN", "true");
									systemLogin.setPath("/");
									systemLogin.setHttpOnly(true);
									systemLogin.setMaxAge(5 * 60); // 5 minute
									response.addCookie(systemLogin);
									
									response.sendRedirect(request.getContextPath() + "/cf/login");
									return null;
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
					fileUtilities.customSendError(response, HttpStatus.FORBIDDEN.value(),
							"You dont have rights to access these module");
					return null;
				}
				TemplateVO templateVO = templatingService.getTemplateByName(viewName);
				return templatingUtils.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(),
						mapDetails);
			} else {
				fileUtilities.customSendError(response, HttpStatus.FORBIDDEN.value(),
						"You dont have rights to access these module");
				return null;
			}
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in updatePasswordPage.", custStopException);
			throw custStopException;
		}
	}

	@GetMapping(value = "/configureTOTP")
	@ResponseBody
	public String configureTOTPPage(ModelAndView modelAndView, HttpServletResponse response)
			throws Exception, CustomStopException {
		Map<String, Object> mapDetails = new HashMap<>();
		try {
			if (applicationSecurityDetails.getIsAuthenticationEnabled()) {
				TemplateVO templateVO = templatingService.getTemplateByName("jws-configure-totp");
				userConfigService.getConfigurableDetails(mapDetails);
				return templatingUtils.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(),
						mapDetails);
			} else {
				fileUtilities.customSendError(response, HttpStatus.FORBIDDEN.value(),
						"You dont have rights to access these module");
				return null;
			}
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in configureTOTPPage.", custStopException);
			throw custStopException;
		}
	}

	@PostMapping(value = "/sendConfigureTOTPMail")
	@ResponseBody
	public String sendConfigureTOTPMail(HttpServletRequest request, HttpSession session, HttpServletResponse response)
			throws Exception, CustomStopException {

		String				emailTo		= request.getParameter("email");
		Map<String, Object>	mapDetails	= new HashMap<>();
		String				viewName	= null;
		try {
			if (applicationSecurityDetails.getIsAuthenticationEnabled()) {
				userConfigService.getConfigurableDetails(mapDetails);
				JwsUser existingUser = userManagementService.findByEmailIgnoreCase(emailTo);
				if (existingUser != null) {
					Email email = new Email();
					userManagementService.sendMailForTotpAuthentication(existingUser, email);
					mapDetails.put("successResetPasswordMsg",
							"Check your email for instructions to login through Google Authenticator. If it doesn’t appear within a few minutes, check your spam folder.");
					viewName = "jws-password-reset-mail-success";
				} else {
					mapDetails.put("nonRegisteredUser", "Could not send email to entered mail id");
					viewName = "jws-configure-totp";
				}
				TemplateVO templateVO = templatingService.getTemplateByName(viewName);
				return templatingUtils.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(),
						mapDetails);
			} else {
				fileUtilities.customSendError(response, HttpStatus.FORBIDDEN.value(),
						"You dont have rights to access these module");
				return null;
			}
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in sendConfigureTOTPMail.", custStopException);
			throw custStopException;
		}
	}

	@GetMapping(value = "/gt")
	@ResponseBody
	public String gt(HttpServletResponse httpServletResponse) throws Exception {
		JwtRequestDetails jwtRequestDetails = jwtUtil.generateToken(userDetailsServiceImpl.loadUserByUsername("anonymous"));
		jwtUtil.createTokenCookie(httpServletResponse, jwtRequestDetails);
		return jwtRequestDetails.getToken();

	}

}
