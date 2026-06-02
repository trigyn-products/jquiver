package com.trigyn.jws.webstarter.controller;

import java.awt.Dimension;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.utils.CustomStopException;
import com.trigyn.jws.dbutils.utils.FileUtilities;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.dynarest.service.FilesStorageService;
import com.trigyn.jws.templating.service.DBTemplatingService;
import com.trigyn.jws.templating.utils.TemplatingUtils;
import com.trigyn.jws.templating.vo.TemplateVO;
import com.trigyn.jws.usermanagement.entities.JwsConfirmationToken;
import com.trigyn.jws.usermanagement.entities.JwsUser;
import com.trigyn.jws.usermanagement.entities.JwsUserRoleAssociation;
import com.trigyn.jws.usermanagement.repository.JwsConfirmationTokenRepository;
import com.trigyn.jws.usermanagement.repository.JwsUserRepository;
import com.trigyn.jws.usermanagement.repository.JwsUserRoleAssociationRepository;
import com.trigyn.jws.usermanagement.security.config.ApplicationSecurityDetails;
import com.trigyn.jws.usermanagement.security.config.CaptchaUtil;
import com.trigyn.jws.usermanagement.service.UserConfigService;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.usermanagement.utils.Constants.VerificationType;
import com.trigyn.jws.usermanagement.vo.JwsUserLoginVO;
import com.trigyn.jws.usermanagement.vo.JwsUserVO;
import com.trigyn.jws.webstarter.service.CaptchaService;
import com.trigyn.jws.webstarter.service.OtpService;
import com.trigyn.jws.webstarter.service.UserManagementService;
import com.trigyn.jws.webstarter.vo.CaptchaDetails;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/cf")
public class JwsUserRegistrationController {

	private final static Logger			logger						= LoggerFactory.getLogger(JwsUserRegistrationController.class);

	@Autowired
	private JwsUserRepository					userRepository					= null;

	@Autowired
	private JwsConfirmationTokenRepository		confirmationTokenRepository		= null;

	@Autowired
	private JwsUserRoleAssociationRepository	userRoleAssociationRepository	= null;

	@Autowired
	private ApplicationSecurityDetails			applicationSecurityDetails		= null;

	@Autowired
	private UserManagementService				userManagementService			= null;

	@Autowired
	private DBTemplatingService					templatingService				= null;

	@Autowired
	private TemplatingUtils						templatingUtils					= null;

	@Autowired
	private UserConfigService					userConfigService				= null;

	@Autowired
	private ServletContext						servletContext					= null;

	@Autowired
	private IUserDetailsService					userDetails						= null;
	
	@Autowired
	private OtpService							otpService						= null;
	
	@Autowired
	private PropertyMasterService				propertyMasterService			= null;
	
	@Autowired
	private FileUtilities 						fileUtilities 					= null;
	
	@Autowired
	private CaptchaService 						captchaService 					= null;

	@Autowired
	private FilesStorageService			filesStorageService			= null;
	
	@GetMapping("/login")
	@ResponseBody
	public String userLoginPage(HttpServletRequest request, HttpServletResponse response) throws Exception {

		UserDetailsVO userDetailsVO = userDetails.getUserDetails();
		if (userDetailsVO != null && !userDetailsVO.getUserName().equalsIgnoreCase("anonymous")) {
			response.sendRedirect(servletContext.getContextPath() + "/cf/home");
			return null;
		} 
		
		Map<String, Object>	mapDetails	= new HashMap<>();
		String				error		= getCookieValue(request, "error");
		if ("true".equalsIgnoreCase(error)) {
			mapDetails.put("queryString", "error");
			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					switch (cookie.getName()) {
						case "loginErrorMessage":
							mapDetails.put("exceptionMessage", getCookieValue(request, "loginErrorMessage"));
							break;
						case "previousMail":
							mapDetails.put("previousMail", getCookieValue(request, "previousMail"));
							break;
						case "prevAuthType":
							mapDetails.put("prevAuthType", getCookieValue(request, "prevAuthType"));
							break;
					}
				}
			}
		}
		userConfigService.getConfigurableDetails(mapDetails);

		clearCookie(response, "loginErrorMessage");
		clearCookie(response, "previousMail");
		clearCookie(response, "prevAuthType");
		clearCookie(response, "error");

		TemplateVO templateVO = templatingService.getTemplateByName("jws-login");
		return templatingUtils.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(),
				mapDetails);

	}
	
	private void clearCookie(HttpServletResponse response, String name) {
		Cookie cookie = new Cookie(name, null);
		cookie.setPath("/"); // Set path to your app root
		cookie.setMaxAge(0); // Invalidate cookie
		response.addCookie(cookie);
	}

	private String getCookieValue(HttpServletRequest request, String name) throws UnsupportedEncodingException {
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if (cookie.getName().equals(name)) {
					return URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8.name());
				}
			}
		}
		return null;
	}

	@GetMapping("/register")
	@ResponseBody
	public String userRegistrationPage(HttpServletResponse response) throws Exception, CustomStopException {
		Map<String, Object> mapDetails = new HashMap<>();
		String enableRegistrationPropertyName = "enableRegistration";
		try {
			if (applicationSecurityDetails.getIsAuthenticationEnabled()) {
				userConfigService.getConfigurableDetails(mapDetails);
				if (mapDetails.get(enableRegistrationPropertyName) != null
						&& mapDetails.get(enableRegistrationPropertyName).toString().equalsIgnoreCase("false")) {
					fileUtilities.customSendError(response,HttpStatus.FORBIDDEN.value(), "You dont have rights to access these module");
				}
			} else {
				fileUtilities.customSendError(response,HttpStatus.FORBIDDEN.value(), "You dont have rights to access these module");
				return null;
			}
			TemplateVO templateVO = templatingService.getTemplateByName("jws-register");
			return templatingUtils.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(),
					mapDetails);
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in userRegistrationPage.", custStopException);
			throw custStopException;
		}
	}

	@PostMapping(value = "/register")
	@ResponseBody
	public String registerUser(HttpServletRequest request, JwsUserVO user, HttpServletResponse response)
			throws Exception, CustomStopException {

		Map<String, Object> mapDetails = new HashMap<>();
		String viewName = "jws-successfulRegisteration";
		try {
			if (applicationSecurityDetails.getIsAuthenticationEnabled()) {
				userConfigService.getConfigurableDetails(mapDetails);
			} else {
				fileUtilities.customSendError(response,HttpStatus.FORBIDDEN.value(), "You dont have rights to access these module");
				return null;
			}
			if(user.getPassword() != null) {
				String password = user.getPassword();
				String requestId = null;
				if (request.getParameter("requestId") != null) {
					requestId = request.getParameter("requestId");
				}
		        String decryptedPassword = userConfigService.decryptPassword(password,requestId);
				user.setPassword(decryptedPassword); 
			}
			boolean isInValid = userManagementService.validateUserRegistration(request, user, mapDetails);

			if (isInValid) {
				viewName = "jws-register";
				TemplateVO templateVO = templatingService.getTemplateByName(viewName);
				return templatingUtils.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(),
						mapDetails);
			}
			String verificationType = mapDetails.get("verificationType").toString();
			switch (verificationType) {
			case Constants.OTP_VERFICATION:
				userManagementService.createUserForOtpAuth(user);
				break;
			case Constants.PASSWORD_VERFICATION:
				userManagementService.createUserForPasswordAuth(user);
				break;
			case Constants.TOTP_VERFICATION:
				userManagementService.createUserForTotpAuth(user);
				break;
			}
			if (mapDetails.get("enableCaptcha").toString().equalsIgnoreCase("true")) {
				//HttpSession session = request.getSession();
				//session.removeAttribute("registerCaptcha");
				String captchaRequest_Id=request.getHeader("r");
				if (StringUtils.isBlank(captchaRequest_Id) == false) {
					   captchaService.deleteCaptcha(captchaRequest_Id);
					}
			}
			mapDetails.put("emailId", user.getEmail());
			TemplateVO templateVO = templatingService.getTemplateByName(viewName);
			return templatingUtils.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(),
					mapDetails);
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in registerUser.", custStopException);
			throw custStopException;
		}
	}

	@GetMapping(value = "/confirm-account")
	@ResponseBody
	public String confirmUserAccount(ModelAndView modelAndView, @RequestParam("token") String confirmationToken,
			HttpServletResponse response) throws Exception, CustomStopException {

		Map<String, Object> mapDetails = new HashMap<>();
		String viewName = null;
		try {
			if (applicationSecurityDetails.getIsAuthenticationEnabled()) {
				JwsConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

				if (token != null) {
					JwsUser user = userRepository.findByEmailIgnoreCase(token.getUserRegistration().getEmail());
					user.setIsActive(Constants.ISACTIVE);
					userRepository.save(user);

					JwsUserRoleAssociation adminRoleAssociation = new JwsUserRoleAssociation();
					adminRoleAssociation.setUserId(user.getUserId());
					adminRoleAssociation.setRoleId(Constants.AUTHENTICATED_ROLE_ID);
					userRoleAssociationRepository.save(adminRoleAssociation);

					StringBuffer sb = new StringBuffer("First Name :" + user.getFirstName().trim());
					sb.append("Last Name :" + user.getLastName().trim());
					sb.append("Email :" + user.getEmail().trim());
					viewName = "jws-accountVerified";
				} else {
					fileUtilities.customSendError(response,HttpStatus.BAD_REQUEST.value(), "The link is invalid or broken!");
					return null;
				}

				TemplateVO templateVO = templatingService.getTemplateByName(viewName);
				return templatingUtils.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(),
						mapDetails);
			} else {
				fileUtilities.customSendError(response,HttpStatus.FORBIDDEN.value(), "You dont have rights to access these module");
				return null;
			}
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in confirmUserAccount.", custStopException);
			throw custStopException;
		}
	}

	@GetMapping(value = "/captcha/{flagCaptcha}")
	public String loadCaptcha(@PathVariable String flagCaptcha, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		try {
			String captchaStr = "";
			String captchaVal = "";
			int captchaType = 0;

			// String formId = request.getParameter("request_Id").toString();
			if (CaptchaUtil.getRandomNumber(10, 100) % 2 == 0) {
				captchaStr = CaptchaUtil.getCaptchaString();
				captchaVal = captchaStr;
				captchaType = 0;
			} else {
				Map<String, String> captchaMap = CaptchaUtil.getMathCaptcha();
				captchaStr = captchaMap.get("cs");
				captchaVal = captchaMap.get("cv");
				captchaType = 1;
			}

			int width = 130;
			int height = 59;

			// currently storing Captcha in session now store in DB -- remove this
//		HttpSession	session	= request.getSession();
//		session.setAttribute(flagCaptcha, captchaVal);

			// Storing Captch in DB

			CaptchaDetails captchaDetails = new CaptchaDetails();
			captchaService.deleteExpiredCaptcha();
			if (null != captchaVal) {
				captchaDetails.setCaptcha(captchaVal);
				String request_id = request.getHeader("r");
				if (null == request_id || "null".equalsIgnoreCase(request_id) || request_id.isBlank()
						|| request_id.isEmpty()) {
					captchaService.saveCaptchDetails(captchaDetails);
				} else {

					CaptchaDetails captcha = captchaService.fetchCaptchDetailsById(request_id);
					if (null != captcha) {
						captchaDetails.setRequestId(captcha.getRequestId());
						captchaDetails.setRequestTime(new Date());
						captchaService.updateCaptchDetails(captchaDetails);
					} else {
						captchaDetails.setCaptcha(captchaVal);
						captchaDetails.setRequestTime(new Date());
						captchaService.saveCaptchDetails(captchaDetails);
					}

				}
				if (null != captchaDetails && null != captchaDetails.getRequestId()) {
					response.addHeader("r", captchaDetails.getRequestId());
				}
			}

			// end of Storing Captcha in DB

			OutputStream outputStream = response.getOutputStream();
			CaptchaUtil.generateCaptcha(new Dimension(width, height), captchaStr, outputStream, captchaType);
			outputStream.close();
			return captchaStr;
		} catch (Exception exception) {
			logger.error("Captcha Generation Failed.", exception);
			// throw exception;
			fileUtilities.customSendError(response, HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Captcha Generation Failed.");
			return null;
		}
	}

	@GetMapping(value = "/profile")
	public String profilePage(HttpServletResponse httpServletResponse) throws Exception {
		try {
			return userManagementService.getProfilePage();
		} catch (Exception a_exception) {
			logger.error("Error occured while loading profile Page.", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			fileUtilities.customSendError(httpServletResponse,HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}
	}
	
	@GetMapping(value = "/saveOtpAndSendMail")
	@ResponseBody
	public String saveOtpAndSendMail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String userEmailId = request.getParameter("email");
		if (applicationSecurityDetails.getIsAuthenticationEnabled()) {
			Map<String, Object> mapDetails = applicationSecurityDetails.getAuthenticationDetails();
			if (userEmailId == null || userEmailId.isEmpty()) {
				fileUtilities.customSendError(response,HttpStatus.BAD_REQUEST.value(), "Email is required.");
				return null;
			}
			JwsUser existingUser = userManagementService.findByEmailIgnoreCase(userEmailId);
			if (existingUser == null) {
				fileUtilities.customSendError(response,HttpStatus.NOT_FOUND.value(), "Invalid user");
				return null;
			}
		
			String isActiveUser = userManagementService.findbyEmailAndIsActive(userEmailId);

			if (isActiveUser == null) {
			    fileUtilities.customSendError(response, HttpStatus.UNAUTHORIZED.value(), "Inactive user");
			    return null;
			}
			
			String mailConfiguration = propertyMasterService.findPropertyMasterValue("mail-configuration");
			if(mailConfiguration == null || mailConfiguration.isEmpty()) {
				fileUtilities.customSendError(response,HttpStatus.SERVICE_UNAVAILABLE.value(), "SMTP configuration not available");
				return null;
			}
			List<JwsUserLoginVO> multiAuthLoginVOs = (List<JwsUserLoginVO>) mapDetails.get("activeAutenticationDetails");
			if (multiAuthLoginVOs != null && multiAuthLoginVOs.isEmpty() == false) {
				for (JwsUserLoginVO multiAuthLogin : multiAuthLoginVOs) {
					if(multiAuthLogin !=null) {
						if (Constants.AuthType.DAO.getAuthType() == multiAuthLogin.getAuthenticationType()) {
							Map<String, Object> loginAttributes = multiAuthLogin.getLoginAttributes();
							if (loginAttributes != null && loginAttributes.isEmpty() == false) {
								if (loginAttributes.containsKey("verificationType")) {
									String verificationTypeValue = (String) loginAttributes.get("verificationType");
									if (verificationTypeValue != null && verificationTypeValue.equals(VerificationType.OTP.getVerificationType())== false) {
										fileUtilities.customSendError(response,HttpStatus.NOT_IMPLEMENTED.value(), "OTP auhentcation not supported !");
										return null;
									}
								}
							}
						}
					}
				}
			}
			
			Integer	generatedOtp	= otpService.generateOTP(userEmailId);
			boolean	isOtpValid		= otpService.validateOTP(userEmailId, generatedOtp);
			if (isOtpValid == false) {
				String	propertyAdminEmailId	= propertyMasterService.findPropertyMasterValue("system", "system",
						"adminEmailId");
				String	adminEmail				= propertyAdminEmailId == null ? "admin@jquiver.io"
						: propertyAdminEmailId.equals("") ? "admin@jquiver.io" : propertyAdminEmailId;
				
				userConfigService.getConfigurableDetails(mapDetails);
				mapDetails.put("email", userEmailId);
				mapDetails.put("oneTimePassword", generatedOtp);
				mapDetails.put("adminEmailAddress", adminEmail);
				mapDetails.put("firstName", existingUser.getFirstName()+ " "+ existingUser.getLastName());
				String baseURL = otpService.getBaseURL(propertyMasterService, servletContext);
				mapDetails.put("baseURL", baseURL);
				JwsUser userOtpUpdateInfo = otpService.saveOtp(mapDetails);
				if (userOtpUpdateInfo != null) {
					otpService.sendMailForOtp(mapDetails);
					mapDetails.put("successOtpPasswordMsg",
							"Check your email for a instructions to login through OTP. If it doesn’t appear within a few minutes, check your spam folder.");
				}
				return "OTP sent to "+userEmailId;
			}
			generatedOtp = otpService.getOPTByKey(userEmailId);
			mapDetails.put("email", userEmailId);
			mapDetails.put("oneTimePassword", generatedOtp);
			otpService.sendMailForOtp(mapDetails);
			return "OTP sent to "+userEmailId;
		} else {
			fileUtilities.customSendError(response,HttpStatus.FORBIDDEN.value(), "You dont have rights to access these module");
			return null;
		}
	}

	@GetMapping(value = "/fetchSalt")
	public Map<String, String> fetchSalt(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return userConfigService.fetchSalt();
	}

	@PostMapping(value = "/supd")
	public Boolean saveUserProdifle(@RequestBody JwsUserVO userData) throws Exception {

		userManagementService.saveUserProdifleData(userData);

		if (userData.getFormData() != null) {
			List<Map<String, String>> formData = new Gson().fromJson(userData.getFormData(), List.class);
			for (Map<String, String> formEntry : formData) {
				if (formEntry.containsKey("valueType") && formEntry.get("valueType").equalsIgnoreCase("fileBin")) {
					filesStorageService.commitChanges(formEntry.get("FileBinID"), formEntry.get("fileAssociationID"));
				}
			}
		}
		return true;
	}

}
