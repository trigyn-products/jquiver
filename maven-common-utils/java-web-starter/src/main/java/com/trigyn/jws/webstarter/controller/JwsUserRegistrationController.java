package com.trigyn.jws.webstarter.controller;

import java.awt.Dimension;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import javax.mail.internet.InternetAddress;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.trigyn.jws.usermanagement.entities.JwsAuthenticationType;
import com.trigyn.jws.usermanagement.entities.JwsConfirmationToken;
import com.trigyn.jws.usermanagement.entities.JwsUser;
import com.trigyn.jws.usermanagement.entities.JwsUserRoleAssociation;
import com.trigyn.jws.usermanagement.exception.InvalidLoginException;
import com.trigyn.jws.usermanagement.repository.JwsAuthenticationTypeRepository;
import com.trigyn.jws.usermanagement.repository.JwsConfirmationTokenRepository;
import com.trigyn.jws.usermanagement.repository.JwsUserRepository;
import com.trigyn.jws.usermanagement.repository.JwsUserRoleAssociationRepository;
import com.trigyn.jws.usermanagement.security.config.ApplicationSecurityDetails;
import com.trigyn.jws.usermanagement.security.config.CaptchaUtil;
import com.trigyn.jws.usermanagement.security.config.TwoFactorGoogleUtil;
import com.trigyn.jws.usermanagement.security.config.oauth.OAuthDetails;
import com.trigyn.jws.usermanagement.service.UserConfigService;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.usermanagement.vo.JwsUserVO;
import com.trigyn.jws.webstarter.service.SendMailService;
import com.trigyn.jws.webstarter.service.UserManagementService;
import com.trigyn.jws.webstarter.utils.Email;

@RestController
@RequestMapping("/cf")
public class JwsUserRegistrationController {

	@Autowired
	private JwsUserRepository					userRepository					= null;

	@Autowired
	private JwsConfirmationTokenRepository		confirmationTokenRepository		= null;

	@Autowired
	private PasswordEncoder						passwordEncoder					= null;

	@Autowired
	private JwsUserRoleAssociationRepository	userRoleAssociationRepository	= null;

	@Autowired
	private ApplicationSecurityDetails			applicationSecurityDetails		= null;

	@Autowired
	private JwsAuthenticationTypeRepository		authenticationTypeRepository	= null;

	@Autowired
	private UserManagementService				userManagementService			= null;

	@Autowired
	private DBTemplatingService					templatingService				= null;

	@Autowired
	private TemplatingUtils						templatingUtils					= null;

	@Autowired
	private SendMailService						sendMailService					= null;

	@Autowired
	private UserConfigService					userConfigService				= null;

	@Autowired
	private JwsUserRoleAssociationRepository	userRoleRepository				= null;

	@Autowired
	private OAuthDetails						oAuthDetails					= null;

	@Autowired
	private PropertyMasterService				propertyMasterService			= null;

	@Autowired
	private ServletContext						servletContext					= null;

	@GetMapping("/login")
	@ResponseBody
	public String userLoginPage(HttpServletRequest request, HttpSession session, HttpServletResponse response)
			throws Exception {

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
		if (applicationSecurityDetails.getIsAuthenticationEnabled()) {
			mapDetails.put("authenticationType", applicationSecurityDetails.getAuthenticationType());
			if (Integer.parseInt(applicationSecurityDetails.getAuthenticationType()) == Constants.AuthType.OAUTH
					.getAuthType() && oAuthDetails != null) {
				mapDetails.put("client", oAuthDetails.getOAuthClient());
			}
			userConfigService.getConfigurableDetails(mapDetails);
		} else {
			response.sendError(HttpStatus.FORBIDDEN.value(), "You dont have rights to access these module");
			return null;
		}
		TemplateVO templateVO = templatingService.getTemplateByName("jws-login");
		return templatingUtils.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(),
				mapDetails);

	}

	@GetMapping("/register")
	@ResponseBody
	public String userRegistrationPage(HttpServletResponse response) throws Exception {
		Map<String, Object>	mapDetails						= new HashMap<>();
		String				enableRegistrationPropertyName	= "enableRegistration";
		if (applicationSecurityDetails.getIsAuthenticationEnabled()) {
			Integer					authType			= Integer
					.parseInt(applicationSecurityDetails.getAuthenticationType());
			JwsAuthenticationType	authenticationType	= authenticationTypeRepository.findById(authType)
					.orElseThrow(() -> new Exception("No auth type found with id : " + authType));
			JSONArray				jsonArray			= new JSONArray(
					authenticationType.getAuthenticationProperties());

			JSONObject				jsonObject			= null;
			jsonObject = getJsonObjectFromPropertyValue(jsonObject, jsonArray, enableRegistrationPropertyName);
			if (jsonObject != null && jsonObject.getString("value").equalsIgnoreCase("false")) {
				response.sendError(HttpStatus.FORBIDDEN.value(), "You dont have rights to access these module");
			}
			userConfigService.getConfigurableDetails(mapDetails);
		} else {
			response.sendError(HttpStatus.FORBIDDEN.value(), "You dont have rights to access these module");
			return null;
		}
		TemplateVO templateVO = templatingService.getTemplateByName("jws-register");
		return templatingUtils.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(),
				mapDetails);

	}

	@PostMapping(value = "/register")
	@ResponseBody
	public String registerUser(HttpServletRequest request, JwsUserVO user, HttpServletResponse response)
			throws Exception {

		Map<String, Object>	mapDetails		= new HashMap<>();
		String				viewName		= null;
		JwsUser				existingUser	= userRepository.findByEmailIgnoreCase(user.getEmail());
		if (applicationSecurityDetails.getIsAuthenticationEnabled()) {
			userConfigService.getConfigurableDetails(mapDetails);
		} else {
			response.sendError(HttpStatus.FORBIDDEN.value(), "You dont have rights to access these module");
			return null;
		}

		if (existingUser != null) {
			mapDetails.put("error", "This email already exists!");
			mapDetails.put("firstName", user.getFirstName().trim());
			mapDetails.put("lastName", user.getLastName().trim());
			viewName = "jws-register";
		} else {
			HttpSession session = request.getSession();
			if (mapDetails.get("enableCaptcha").toString().equalsIgnoreCase("true")
					&& session.getAttribute("registerCaptcha") != null
					&& !(user.getCaptcha().equals(session.getAttribute("registerCaptcha").toString()))) {
				mapDetails.put("error", "Please verify captcha!");
				mapDetails.put("firstName", user.getFirstName().trim());
				mapDetails.put("lastName", user.getLastName().trim());
				viewName = "jws-register";
				TemplateVO templateVO = templatingService.getTemplateByName(viewName);
				return templatingUtils.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(),
						mapDetails);
			}

			if (mapDetails.get("enableGoogleAuthenticator").toString().equalsIgnoreCase("false")) {
				if (userManagementService.validatePassword(user.getPassword())) {

					user.setPassword(passwordEncoder.encode(user.getPassword()));
					user.setIsActive(Constants.INACTIVE);
					user.setForcePasswordChange(Constants.INACTIVE);
					JwsUser userEntityFromVo = user.convertVOToEntity(user);
					userEntityFromVo.setForcePasswordChange(Constants.INACTIVE);
					userEntityFromVo.setSecretKey(new TwoFactorGoogleUtil().generateSecretKey());
					userRepository.save(userEntityFromVo);

					JwsConfirmationToken confirmationToken = new JwsConfirmationToken(userEntityFromVo);
					confirmationTokenRepository.save(confirmationToken);

					Email email = new Email();
					email.setInternetAddressToArray(InternetAddress.parse(user.getEmail()));
					// email.setMailFrom("admin@jquiver.com");

					Map<String, Object>	mailDetails			= new HashMap<>();
					TemplateVO			subjectTemplateVO	= templatingService
							.getTemplateByName("confirm-account-mail-subject");
					String				subject				= templatingUtils.processTemplateContents(
							subjectTemplateVO.getTemplate(), subjectTemplateVO.getTemplateName(), mailDetails);
					email.setSubject(subject);

					String baseURL = UserManagementService.getBaseURL(propertyMasterService, servletContext);
					mailDetails.put("baseURL", baseURL);
					
					mailDetails.put("tokenId", confirmationToken.getConfirmationToken());
					TemplateVO	templateVO	= templatingService.getTemplateByName("confirm-account-mail");
					String		mailBody	= templatingUtils.processTemplateContents(templateVO.getTemplate(),
							templateVO.getTemplateName(), mailDetails);
					email.setBody(mailBody);
					System.out.println(mailBody);
					sendMailService.sendTestMail(email);

					viewName = "jws-successfulRegisteration";
				} else {
					viewName = "jws-register";
					mapDetails.put("firstName", user.getFirstName().trim());
					mapDetails.put("lastName", user.getLastName().trim());
					mapDetails.put("errorPassword",
							"Password must contain atleast 6 characters including UPPER/lowercase/Special charcters and numbers!");
				}
			} else {

				user.setPassword(null);
				user.setIsActive(Constants.ISACTIVE);
				JwsUser userEntityFromVo = user.convertVOToEntity(user);
				userEntityFromVo.setForcePasswordChange(Constants.INACTIVE);
				userEntityFromVo.setSecretKey(new TwoFactorGoogleUtil().generateSecretKey());
				userRepository.save(userEntityFromVo);

				// adding role to the user
				JwsUserRoleAssociation userRoleAssociation = new JwsUserRoleAssociation();
				userRoleAssociation.setRoleId(Constants.AUTHENTICATED_ROLE_ID);
				userRoleAssociation.setUserId(userEntityFromVo.getUserId());
				userRoleAssociation.setUpdatedDate(new Date());
				userRoleRepository.save(userRoleAssociation);

				TwoFactorGoogleUtil	twoFactorGoogleUtil	= new TwoFactorGoogleUtil();
				int					width				= 300;
				int					height				= 300;
				String				filePath			= System.getProperty("java.io.tmpdir") + File.separator
						+ userEntityFromVo.getUserId() + ".png";
				File				file				= new File(filePath);
				FileOutputStream	fileOutputStream	= new FileOutputStream(filePath);
				String				barcodeData			= twoFactorGoogleUtil.getGoogleAuthenticatorBarCode(
						userEntityFromVo.getEmail(), "Jquiver", userEntityFromVo.getSecretKey());
				twoFactorGoogleUtil.createQRCode(barcodeData, fileOutputStream, height, width);

				Email email = new Email();
				email.setInternetAddressToArray(InternetAddress.parse(user.getEmail()));
				email.setSubject("TOTP Login");
				email.setMailFrom("admin@jquiver.com");
				Map<String, Object>	mailDetails	= new HashMap<>();
				TemplateVO			templateVO	= templatingService.getTemplateByName("totp-qr-mail");
				String				mailBody	= templatingUtils.processTemplateContents(templateVO.getTemplate(),
						templateVO.getTemplateName(), mailDetails);
				email.setBody(mailBody);
				System.out.println(mailBody);

				List<File> attachedFiles = new ArrayList<>();
				attachedFiles.add(file);
				email.setAttachementsArray(attachedFiles);
				viewName = "jws-successfulRegisteration";

				CompletableFuture<Boolean> mailSuccess = sendMailService.sendTestMail(email);
				if (mailSuccess.isDone()) {
					email.getAttachementsArray().stream().forEach(f -> f.delete());
				}

			}

			mapDetails.put("emailId", user.getEmail());
		}

		TemplateVO templateVO = templatingService.getTemplateByName(viewName);
		return templatingUtils.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(),
				mapDetails);

	}

	private JSONObject getJsonObjectFromPropertyValue(JSONObject jsonObject, JSONArray jsonArray, String propertyName)
			throws JSONException {
		for (int i = 0; i < jsonArray.length(); i++) {
			jsonObject = jsonArray.getJSONObject(i);
			if (jsonObject.get("name").toString().equalsIgnoreCase(propertyName)) {
				break;
			} else {
				jsonObject = null;
			}
		}
		return jsonObject;
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
			return templatingUtils.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(),
					mapDetails);

		} else {

			response.sendError(HttpStatus.FORBIDDEN.value(), "You dont have rights to access these module");
			return null;
		}

	}

	@GetMapping(value = "/captcha/{flagCaptcha}")
	public void loadCaptcha(@PathVariable String flagCaptcha, HttpServletRequest request, HttpServletResponse response)
			throws Throwable {

		String captchaStr = CaptchaUtil.getCaptchaString();
		System.out.println(captchaStr);
		int			width	= 130;
		int			height	= 59;
		HttpSession	session	= request.getSession();
		session.setAttribute(flagCaptcha, captchaStr);

		OutputStream outputStream = response.getOutputStream();
		CaptchaUtil.generateCaptcha(new Dimension(width, height), captchaStr, outputStream);
		outputStream.close();
	}

}
