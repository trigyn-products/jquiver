package com.trigyn.jws.webstarter.controller;

import java.awt.Dimension;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.templating.service.DBTemplatingService;
import com.trigyn.jws.templating.utils.TemplatingUtils;
import com.trigyn.jws.templating.vo.TemplateVO;
import com.trigyn.jws.usermanagement.entities.JwsConfirmationToken;
import com.trigyn.jws.usermanagement.entities.JwsUser;
import com.trigyn.jws.usermanagement.entities.JwsUserRoleAssociation;
import com.trigyn.jws.usermanagement.exception.InvalidLoginException;
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
import com.trigyn.jws.webstarter.service.OtpService;
import com.trigyn.jws.webstarter.service.UserManagementService;

@RestController
@RequestMapping("/cf")
public class JwsUserRegistrationController {

	private final static Logger			logger						= LogManager.getLogger(JwsUserRegistrationController.class);

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
	
	@GetMapping("/login")
	@ResponseBody
	public String userLoginPage(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws Exception {

		UserDetailsVO userDetailsVO = userDetails.getUserDetails();
		if (userDetailsVO != null && !userDetailsVO.getUserName().equalsIgnoreCase("anonymous")) {
			response.sendRedirect(servletContext.getContextPath() + "/cf/home");
			return null;
		} else {
			Map<String, Object>	mapDetails	= new HashMap<>();

			String				queryString	= request.getQueryString();
			if (StringUtils.isNotEmpty(request.getQueryString())) {
				mapDetails.put("queryString", queryString);
				if (queryString.equalsIgnoreCase("error")) {
					Exception excep = (Exception) session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
					if (excep.getCause() instanceof InvalidLoginException) {
						mapDetails.put("exceptionMessage", excep.getMessage());
					}
				}
			} else if (session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION") != null) {
				Exception exc = (Exception) session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
				if (exc != null && !exc.getMessage().isBlank()) {
					mapDetails.put("queryString", "error");
					mapDetails.put("exceptionMessage", exc.getMessage());
				}
				session.setAttribute("SPRING_SECURITY_LAST_EXCEPTION", null);
			}
			userConfigService.getConfigurableDetails(mapDetails);
			TemplateVO templateVO = templatingService.getTemplateByName("jws-login");
			return templatingUtils.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(), mapDetails);
		}
	}

	@GetMapping("/register")
	@ResponseBody
	public String userRegistrationPage(HttpServletResponse response) throws Exception {
		Map<String, Object>	mapDetails						= new HashMap<>();
		String				enableRegistrationPropertyName	= "enableRegistration";
		if (applicationSecurityDetails.getIsAuthenticationEnabled()) {
			userConfigService.getConfigurableDetails(mapDetails);
			if (mapDetails.get(enableRegistrationPropertyName)!=null && mapDetails.get(enableRegistrationPropertyName).toString().equalsIgnoreCase("false")) {
				response.sendError(HttpStatus.FORBIDDEN.value(), "You dont have rights to access these module");
			}
		} else {
			response.sendError(HttpStatus.FORBIDDEN.value(), "You dont have rights to access these module");
			return null;
		}
		TemplateVO templateVO = templatingService.getTemplateByName("jws-register");
		return templatingUtils.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(), mapDetails);

	}

	@PostMapping(value = "/register")
	@ResponseBody
	public String registerUser(HttpServletRequest request, JwsUserVO user, HttpServletResponse response) throws Exception {

		Map<String, Object>	mapDetails		= new HashMap<>();
		String				viewName		= "jws-successfulRegisteration";
		if (applicationSecurityDetails.getIsAuthenticationEnabled()) {
			userConfigService.getConfigurableDetails(mapDetails);
		} else {
			response.sendError(HttpStatus.FORBIDDEN.value(), "You dont have rights to access these module");
			return null;
		}
		boolean isInValid = userManagementService.validateUserRegistration(request, user, mapDetails);
		if(isInValid) {
			viewName = "jws-register";
			TemplateVO templateVO = templatingService.getTemplateByName(viewName);
			return templatingUtils.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(), mapDetails);
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
			HttpSession session = request.getSession();
			session.removeAttribute("registerCaptcha");
		}
		mapDetails.put("emailId", user.getEmail());
		TemplateVO templateVO = templatingService.getTemplateByName(viewName);
		return templatingUtils.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(), mapDetails);
	}

	@GetMapping(value = "/confirm-account")
	@ResponseBody
	public String confirmUserAccount(ModelAndView modelAndView, @RequestParam("token") String confirmationToken,
		HttpServletResponse response) throws Exception {

		Map<String, Object>	mapDetails	= new HashMap<>();
		String				viewName	= null;

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
				response.sendError(HttpStatus.BAD_REQUEST.value(), "The link is invalid or broken!");
				return null;
			}

			TemplateVO templateVO = templatingService.getTemplateByName(viewName);
			return templatingUtils.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(), mapDetails);

		} else {

			response.sendError(HttpStatus.FORBIDDEN.value(), "You dont have rights to access these module");
			return null;
		}

	}

	@GetMapping(value = "/captcha/{flagCaptcha}")
	public String loadCaptcha(@PathVariable String flagCaptcha, HttpServletRequest request, HttpServletResponse response) throws Throwable {

		String captchaStr = CaptchaUtil.getCaptchaString();
		int			width	= 130;
		int			height	= 59;
		HttpSession	session	= request.getSession();
		session.setAttribute(flagCaptcha, captchaStr);

		OutputStream outputStream = response.getOutputStream();
		CaptchaUtil.generateCaptcha(new Dimension(width, height), captchaStr, outputStream);
		outputStream.close();
		return captchaStr;
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
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
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
				response.sendError(HttpStatus.BAD_REQUEST.value(), "Email is required.");
				return null;
			}
			JwsUser existingUser = userManagementService.findByEmailIgnoreCase(userEmailId);
			if (existingUser == null) {
				response.sendError(HttpStatus.NOT_FOUND.value(), "Invalid user");
				return null;
			}
			String mailConfiguration = propertyMasterService.findPropertyMasterValue("mail-configuration");
			if(mailConfiguration == null || mailConfiguration.isEmpty()) {
				response.sendError(HttpStatus.SERVICE_UNAVAILABLE.value(), "SMTP configuration not available");
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
										response.sendError(HttpStatus.NOT_IMPLEMENTED.value(), "OTP auhentcation not supported !");
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
			response.sendError(HttpStatus.FORBIDDEN.value(), "You dont have rights to access these module");
			return null;
		}
	}

}
