package com.trigyn.jws.webstarter.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.devtools.restart.FailureHandler;
import org.springframework.boot.devtools.restart.Restarter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.utils.ActivityLog;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.dynarest.service.FilesStorageService;
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.usermanagement.entities.JwsEntityRoleAssociation;
import com.trigyn.jws.usermanagement.entities.JwsRole;
import com.trigyn.jws.usermanagement.entities.JwsUser;
import com.trigyn.jws.usermanagement.repository.JwsRoleRepository;
import com.trigyn.jws.usermanagement.security.config.ApplicationSecurityDetails;
import com.trigyn.jws.usermanagement.security.config.LdapConfigService;
import com.trigyn.jws.usermanagement.service.UserConfigService;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.usermanagement.vo.JwsEntityRoleAssociationVO;
import com.trigyn.jws.usermanagement.vo.JwsEntityRoleVO;
import com.trigyn.jws.usermanagement.vo.JwsMasterModulesVO;
import com.trigyn.jws.usermanagement.vo.JwsRoleMasterModulesAssociationVO;
import com.trigyn.jws.usermanagement.vo.JwsRoleVO;
import com.trigyn.jws.usermanagement.vo.JwsUserVO;
import com.trigyn.jws.usermanagement.vo.UserManagementVo;
import com.trigyn.jws.webstarter.service.OtpService;
import com.trigyn.jws.webstarter.service.UserManagementService;

@RestController
@RequestMapping(value = "/cf")
@PreAuthorize("hasPermission('module','User Management')")
public class JwsUserManagementController {

	private final static Logger			logger						= LogManager
			.getLogger(JwsUserManagementController.class);

	@Autowired
	private MenuService					menuService					= null;

	@Autowired
	private UserManagementService		userManagementService		= null;

	@Autowired
	private ApplicationSecurityDetails	applicationSecurityDetails	= null;

	@Autowired
	private IUserDetailsService			userDetailsService			= null;

	@Autowired
	private FilesStorageService			filesStorageService			= null;

	@Autowired
	private OtpService					otpService					= null;

	@Autowired
	private UserConfigService			userConfigService			= null;

	@Autowired
	private JwsRoleRepository			jwsRoleRepository			= null;

	@Autowired
	private ActivityLog					activitylog					= null;
	
	@Autowired
	private LdapConfigService			ldapService					= null;

	@GetMapping(value = "/um")
	public String userManagement(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws Exception {

		try {
			return userManagementService.loadUserManagement();
		} catch (Exception exception) {
			logger.error("Error occured while loading User Management page.", exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
		}
		return null;
	}

	@GetMapping(value = "/rl")
	public String roleListing(HttpServletResponse httpServletResponse) throws Exception {
		try {
			if (userManagementService.checkAuthenticationEnabled()) {
				Map<String, Object> mapDetails = new HashMap<>();
				return menuService.getTemplateWithSiteLayout("role-listing", mapDetails);
			} else {
				httpServletResponse.sendError(HttpStatus.FORBIDDEN.value(),
						"You dont have rights to access these module");
				return null;
			}
		} catch (Exception a_exception) {
			logger.error("Error occured while loading Role Listing page.", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}

	}

	@PostMapping(value = "/srd")
	public Boolean saveRole(@RequestBody JwsRoleVO roleData) throws Exception {
		/* Method called for implementing Activity Log */
		logActivity(roleData.getRoleName(), roleData.getRoleId());
		userManagementService.saveRoleData(roleData);
		return true;
	}

	@PostMapping(value = "/aedr")
	public String addEditRole(@RequestParam("roleId") String roleId, HttpServletResponse httpServletResponse)
			throws IOException {
		try {
			return userManagementService.addEditRole(roleId);
		} catch (Exception a_exception) {
			logger.error("Error occured while loading role : RoleId : "+ roleId, a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}
	}

	@GetMapping(value = "/mrm")
	public String manageRoleModules() throws Exception {

		return userManagementService.manageRoleModules();
	}

	@PostMapping(value = "/srm")
	public Boolean saveRoleModules(@RequestBody JwsRoleMasterModulesAssociationVO roleModule,
			HttpServletResponse httpServletResponse) throws IOException {
		try {
			return userManagementService.saveRoleModules(roleModule);
		} catch (Exception a_exception) {
			logger.error("Error occured while saving Role Module.", a_exception);
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}
	}

	@GetMapping(value = "/ul")
	public String userListing(HttpServletResponse httpServletResponse) throws Exception {
		try {
			if (userManagementService.checkAuthenticationEnabled()) {
				Map<String, Object>	mapDetails		= new HashMap<>();
				String				templateName	= userManagementService.getDaoPropertyValueByName("user-profile-template-details", "templateName");
				UserDetailsVO		detailsVO		= userDetailsService.getUserDetails();
				mapDetails.put("userDetails", detailsVO);
				if (StringUtils.isNotBlank(templateName)) {
					mapDetails.put("isProfilePage", false);
					return menuService.getTemplateWithSiteLayout(templateName, mapDetails);
				}

				return menuService.getTemplateWithSiteLayout("jws-user-listing", mapDetails);
			} else {
				httpServletResponse.sendError(HttpStatus.FORBIDDEN.value(),
						"You dont have rights to access these module");
				return null;
			}
		} catch (Exception a_exception) {
			logger.error("Error occured while loading User Listing page.", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}
	}

	@PostMapping(value = "/sud")
	public Boolean saveUser(@RequestBody JwsUserVO userData) throws Exception {

		userManagementService.saveUserData(userData);
		/* Method called for implementing Activity Log */
		logActivity(userData.getEmail(), userData.getUserId());
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

	@PostMapping(value = "/surap")
	public Boolean saveUserRolesAndPolicies(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws Exception {
		String		userDataJson	= httpServletRequest.getParameter("userData");
		Gson		gson			= new Gson();
		JwsUserVO	userData		= gson.fromJson(userDataJson, JwsUserVO.class);
		Integer		isEdit			= httpServletRequest.getParameter("isEdit") == null ? 0
				: Integer.parseInt(httpServletRequest.getParameter("isEdit"));
		userManagementService.saveUserRolesAndPolicies(isEdit, userData);
		return true;
	}

	@PostMapping(value = "/aedu")
	public String addEditUser(@RequestParam("userId") String userId, HttpServletResponse httpServletResponse)
			throws IOException {
		try {
			boolean isProfilePage = false;
			return userManagementService.addEditUser(userId, isProfilePage);
		} catch (Exception a_exception) {
			logger.error("Error occured while loading user with userId::"+userId, a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}
	}

	private Map<String, Object> processRequestParams(HttpServletRequest httpServletRequest) {
		Map<String, Object> requestParams = new HashMap<>();
		for (String requestParamKey : httpServletRequest.getParameterMap().keySet()) {
			if (Boolean.FALSE.equals("formId".equalsIgnoreCase(requestParamKey))) {
				requestParams.put(requestParamKey, httpServletRequest.getParameter(requestParamKey));
			}
		}
		return requestParams;
	}

	@PostMapping(value = "/mpar")
	public String manageUserRoleAndPermission(@RequestParam("userId") String userId,
			HttpServletResponse httpServletResponse) throws IOException {
		try {
			if (userManagementService.checkAuthenticationEnabled()) {
				Map<String, Object>	mapDetails	= new HashMap<>();
				List<JwsRole>		roles		= new ArrayList<>();
				roles = jwsRoleRepository.findAllRoles();
				mapDetails.put("roles", roles);

				return menuService.getTemplateWithSiteLayout("manage-permission", mapDetails);
			} else {
				httpServletResponse.sendError(HttpStatus.FORBIDDEN.value(),
						"You dont have rights to access these module");
				return null;
			}
		} catch (Exception a_exception) {
			logger.error("Error occured while loading manage permission page with userId::"+userId, a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}
	}

	/**
	 * Purpose of this method is to log activities</br>
	 * in User Management Module.
	 * 
	 * @author            Bibhusrita.Nayak
	 * @param  entityName
	 * @param  action
	 * @throws Exception
	 */
	private void logActivity(String entityName, String action) throws Exception {
		Map<String, String>	requestParams		= new HashMap<>();
		UserDetailsVO		detailsVO			= userDetailsService.getUserDetails();
		Date				activityTimestamp	= new Date();
		if (action.isEmpty() == true) {
			action = Constants.Action.ADD.getAction();
		} else {
			if (Constants.ACTION == action) {
				action = Constants.Action.OPEN.getAction();
			} else {
				action = Constants.Action.EDIT.getAction();
			}
		}
		requestParams.put("typeSelect", Constants.Changetype.CUSTOM.getChangetype());
		requestParams.put("entityName", entityName);
		requestParams.put("masterModuleType", Constants.Modules.USERMANAGEMENT.getModuleName());
		requestParams.put("userName", detailsVO.getUserName());
		requestParams.put("message", "");
		requestParams.put("date", activityTimestamp.toString());
		requestParams.put("action", action);
		activitylog.activitylog(requestParams);
	}

	@RequestMapping(value = "/sat", consumes = MediaType.APPLICATION_JSON_VALUE)
	public Boolean saveAuthenticationType(@RequestBody UserManagementVo userManagementData) throws Exception {
		userManagementService.updateAuthProperties(userManagementData);
		applicationSecurityDetails.resetApplicationSecurityDetails();
		return true;
	}

	@GetMapping(value = "/mer")
	public String manageEntityRoles(HttpServletResponse httpServletResponse) throws Exception {
		try {
			return userManagementService.manageEntityRoles();
		} catch (Exception a_exception) {
			logger.error("Error occured while loading manageEntityRoles page.", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}
	}

	@PostMapping(value = "/suer")
	public Boolean saveUpdateEntityRole(@RequestBody List<JwsEntityRoleAssociationVO> entityDataList) throws Exception {

		userManagementService.saveUpdateEntityRole(entityDataList);
		return true;
	}

	@GetMapping(value = "/modules")
	public List<JwsMasterModulesVO> getModules() throws Exception {

		return userManagementService.getModules();
	}

	@GetMapping(value = "/restart")
	public void restart() {
		new Thread(() -> {
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException a_exception) {
				logger.error("Error occure while restarting server.", a_exception);
			}
			Restarter.getInstance().restart(FailureHandler.NONE);
		}).start();

	}

	@GetMapping(value = "/mp")
	public String managePermissions(HttpServletResponse httpServletResponse) throws Exception {
		try {
			if (userManagementService.checkAuthenticationEnabled()) {
				Map<String, Object>	mapDetails	= new HashMap<>();
				List<JwsRole>		roles		= new ArrayList<>();
				roles = jwsRoleRepository.findAllRoles();
				mapDetails.put("roles", roles);
				return menuService.getTemplateWithSiteLayout("manage-permission", mapDetails);
			} else {
				httpServletResponse.sendError(HttpStatus.FORBIDDEN.value(),
						"You dont have rights to access these module");
				return null;
			}
		} catch (Exception a_exception) {
			logger.error("Error occured while loading manage-permission page.", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}
	}

	@PostMapping(value = "/ser")
	public Boolean saveEntityRole(@RequestBody JwsEntityRoleVO entityRoles) throws Exception {

		userManagementService.deleteAndSaveEntityRole(entityRoles);
		return true;
	}

	@GetMapping(value = "/ler", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<JwsRoleVO> getEntityRole(@RequestParam("entityId") String entityId,
			@RequestParam("moduleId") String moduleId) throws Exception {

		return userManagementService.getEntityRoles(entityId, moduleId);
	}

	@GetMapping(value = "/cee")
	public boolean checkEmailExist(HttpServletRequest request, HttpServletResponse response) throws Throwable {

		boolean	emailExist		= true;

		String	email			= request.getParameter("email");
		JwsUser	existingUser	= userManagementService.findByEmailIgnoreCase(email);
		if (existingUser == null) {
			emailExist = false;
		}
		return emailExist;
	}

	@GetMapping(value = "/gif", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getInputFieldsByProperty(HttpServletRequest request) throws Exception {

		String	propertyName	= request.getParameter("inputId");
		String	propertyValue	= userManagementService.getInputFieldsByProperty(propertyName);
		if (StringUtils.isBlank(propertyValue)) {
			propertyValue = "{}";
		}
		return propertyValue;
	}

	@PostMapping(value = "/puj")
	public String getLastUpdatedPermissionJsonData(HttpServletRequest a_httpServletRequest,
			HttpServletResponse a_httpServletResponse) throws Exception {
		String entityId = a_httpServletRequest.getParameter("entityId");
		return userManagementService.getJwsEntityRoleAssociationJson(entityId);
	}

	@PostMapping(value = "/sjra")
	public void savePermission(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse)
			throws Exception {
		String						modifiedContent	= a_httpServletRequest.getParameter("modifiedContent");
		ObjectMapper				objectMapper	= new ObjectMapper();
		JwsEntityRoleAssociation	jwsRoleAssoc	= objectMapper.readValue(modifiedContent,
				JwsEntityRoleAssociation.class);
		userManagementService.saveJwsEntityRoleAssociation(jwsRoleAssoc);
	}

	@GetMapping(value = "/cae")
	public boolean checkAuthenticationEnabled(HttpServletRequest a_httpServletRequest,
			HttpServletResponse a_httpServletResponse) throws Exception {
		return userManagementService.checkAuthenticationEnabled();
	}

	@PostMapping(value = "/mjwsu")
	public String getLastUpdatedJwsUser(HttpServletRequest a_httpServletRequest,
			HttpServletResponse a_httpServletResponse) throws Exception {
		String entityId = a_httpServletRequest.getParameter("entityId");
		return userManagementService.getJwsUserJson(entityId);
	}

	@PostMapping(value = "/sjwsu")
	public void saveJwsUser(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse)
			throws Exception {
		String			modifiedContent	= a_httpServletRequest.getParameter("modifiedContent");
		ObjectMapper	objectMapper	= new ObjectMapper();
		JwsUser			user			= objectMapper.readValue(modifiedContent, JwsUser.class);
		userManagementService.saveJwsUser(user);
	}

	@PostMapping(value = "/mjwsr")
	public String getLastUpdatedJwsRole(HttpServletRequest a_httpServletRequest,
			HttpServletResponse a_httpServletResponse) throws Exception {
		String entityId = a_httpServletRequest.getParameter("entityId");
		return userManagementService.getJwsRoleJson(entityId);
	}

	@PostMapping(value = "/sjwsr")
	public void saveJwsRole(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse)
			throws Exception {
		String			modifiedContent	= a_httpServletRequest.getParameter("modifiedContent");
		ObjectMapper	objectMapper	= new ObjectMapper();
		JwsRole			role			= objectMapper.readValue(modifiedContent, JwsRole.class);
		userManagementService.saveJwsRole(role);
	}

	@PostMapping(value = "/fcp")
	public void forceChangePassword(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse)
			throws Exception {
		userManagementService.forceChangePassword();
	}

	@GetMapping(value = "/saveOtpAndSendMail")
	@ResponseBody
	public String saveOtpAndSendMail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String	userEmailId		= request.getParameter("email");
		String	generatedOtp	= request.getParameter("generatedOtp");
		if (applicationSecurityDetails.getIsAuthenticationEnabled()) {
			Map<String, Object>	mapDetails	= new HashMap<>();
			JwsUser	existingUser	= userManagementService.findByEmailIgnoreCase(userEmailId);
	        if(existingUser == null) {
	        	response.sendError(HttpStatus.NOT_FOUND.value(), "User not found");
				return null;
	        }
			boolean				isOtpValid	= otpService.validateOTP(userEmailId, Integer.valueOf(generatedOtp));
			if (!isOtpValid) {
				userConfigService.getConfigurableDetails(mapDetails);
				mapDetails.put("email", userEmailId);
				mapDetails.put("oneTimePassword", generatedOtp);
				JwsUser userOtpUpdateInfo = otpService.createOtp(mapDetails);
				if (userOtpUpdateInfo != null) {
					otpService.sendMailForOtp(mapDetails);
					mapDetails.put("successOtpPasswordMsg",
							"Check your email for a instructions to login through OTP. If it doesn’t appear within a few minutes, check your spam folder.");
				}
				return generatedOtp;
			}
			generatedOtp = String.valueOf(otpService.getOPTByKey(userEmailId));
			mapDetails.put("email", userEmailId);
			mapDetails.put("oneTimePassword", generatedOtp);
			otpService.sendMailForOtp(mapDetails);
			return generatedOtp;
		} else {
			response.sendError(HttpStatus.FORBIDDEN.value(), "You dont have rights to access these module");
			return null;
		}
	}
	
	@PostMapping(value = "/checkLdapConnection")
	public Boolean checkLdapConnection(@RequestBody MultiValueMap<String, Object> formLdapData,
			HttpServletRequest a_httpServletRequest) throws Exception {
		return ldapService.checkLdapConnection(formLdapData);
	}
}