package com.trigyn.jws.webstarter.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.trigyn.jws.dbutils.repository.PropertyMasterRepository;
import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.spi.PropertyMasterDetails;
import com.trigyn.jws.dbutils.utils.ActivityLog;
import com.trigyn.jws.dbutils.utils.Constant;
import com.trigyn.jws.dbutils.utils.CustomStopException;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.dynamicform.service.DynamicFormService;
import com.trigyn.jws.dynarest.service.SendMailService;
import com.trigyn.jws.dynarest.vo.Email;
import com.trigyn.jws.dynarest.vo.EmailAttachedFile;
import com.trigyn.jws.templating.service.DBTemplatingService;
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.templating.utils.TemplatingUtils;
import com.trigyn.jws.templating.vo.TemplateVO;
import com.trigyn.jws.usermanagement.entities.JwsAuthenticationType;
import com.trigyn.jws.usermanagement.entities.JwsConfirmationToken;
import com.trigyn.jws.usermanagement.entities.JwsEntityRoleAssociation;
import com.trigyn.jws.usermanagement.entities.JwsMasterModules;
import com.trigyn.jws.usermanagement.entities.JwsRole;
import com.trigyn.jws.usermanagement.entities.JwsRoleMasterModulesAssociation;
import com.trigyn.jws.usermanagement.entities.JwsUser;
import com.trigyn.jws.usermanagement.entities.JwsUserRoleAssociation;
import com.trigyn.jws.usermanagement.repository.JwsAuthenticationTypeRepository;
import com.trigyn.jws.usermanagement.repository.JwsConfirmationTokenRepository;
import com.trigyn.jws.usermanagement.repository.JwsEntityRoleAssociationDAO;
import com.trigyn.jws.usermanagement.repository.JwsEntityRoleAssociationRepository;
import com.trigyn.jws.usermanagement.repository.JwsMasterModulesRepository;
import com.trigyn.jws.usermanagement.repository.JwsRoleMasterModulesAssociationRepository;
import com.trigyn.jws.usermanagement.repository.JwsRoleRepository;
import com.trigyn.jws.usermanagement.repository.JwsUserRepository;
import com.trigyn.jws.usermanagement.repository.JwsUserRoleAssociationRepository;
import com.trigyn.jws.usermanagement.repository.UserManagementDAO;
import com.trigyn.jws.usermanagement.security.config.ApplicationSecurityDetails;
import com.trigyn.jws.usermanagement.security.config.TwoFactorGoogleUtil;
import com.trigyn.jws.usermanagement.security.config.UserInformation;
import com.trigyn.jws.usermanagement.service.UserConfigService;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.usermanagement.utils.Constants.AuthTypeByName;
import com.trigyn.jws.usermanagement.vo.AuthProperty;
import com.trigyn.jws.usermanagement.vo.AuthenticationDetails;
import com.trigyn.jws.usermanagement.vo.ConnectionDetailsJSONSpecification;
import com.trigyn.jws.usermanagement.vo.JwsAuthAdditionalProperty;
import com.trigyn.jws.usermanagement.vo.JwsAuthConfiguration;
import com.trigyn.jws.usermanagement.vo.JwsAuthenticationTypeVO;
import com.trigyn.jws.usermanagement.vo.JwsEntityRoleAssociationVO;
import com.trigyn.jws.usermanagement.vo.JwsEntityRoleVO;
import com.trigyn.jws.usermanagement.vo.JwsMasterModulesVO;
import com.trigyn.jws.usermanagement.vo.JwsRoleMasterModulesAssociationVO;
import com.trigyn.jws.usermanagement.vo.JwsRoleVO;
import com.trigyn.jws.usermanagement.vo.JwsUserLoginVO;
import com.trigyn.jws.usermanagement.vo.JwsUserVO;
import com.trigyn.jws.usermanagement.vo.MultiAuthSecurityDetailsVO;
import com.trigyn.jws.usermanagement.vo.UserManagementVo;

@Service
@Transactional
public class UserManagementService {

	private static final Logger							logger							= LogManager
			.getLogger(UserManagementService.class);

	@Autowired
	private JwsRoleRepository							jwsRoleRepository				= null;

	@Autowired
	private JwsMasterModulesRepository					jwsmasterModuleRepository		= null;

	@Autowired
	private JwsRoleMasterModulesAssociationRepository	roleModuleRepository			= null;

	@Autowired
	private MenuService									menuService						= null;

	@Autowired
	private JwsUserRepository							jwsUserRepository				= null;

	@Autowired
	private UserManagementDAO							userManagementDAO				= null;

	@Autowired
	private JwsEntityRoleAssociationDAO					jwsEntityRoleAssociationDAO		= null;

	@Autowired
	private JwsUserRoleAssociationRepository			userRoleRepository				= null;

	@Autowired
	private PropertyMasterRepository					propertyMasterRepository		= null;

	@Autowired
	private JwsAuthenticationTypeRepository				authenticationTypeRepository	= null;

	@Autowired
	private JwsEntityRoleAssociationRepository			entityRoleAssociationRepository	= null;

	@Autowired
	private DBTemplatingService							templatingService				= null;

	@Autowired
	private TemplatingUtils								templatingUtils					= null;

	@Autowired
	private IUserDetailsService							userDetailsService				= null;

	@Autowired
	private PasswordEncoder								passwordEncoder					= null;

	@Autowired
	private ApplicationSecurityDetails					applicationSecurityDetails		= null;

	@Autowired
	private PropertyMasterService						propertyMasterService			= null;

	@Autowired
	private SendMailService								sendMailService					= null;

	@Autowired
	private UserConfigService							userConfigService				= null;

	@Autowired
	private DynamicFormService							dynamicFormService				= null;

	@Autowired
	private PropertyMasterDetails						propertyMasterDetails			= null;

	@Autowired
	private ServletContext								servletContext					= null;

	@Autowired
	private ActivityLog									activitylog						= null;

	@Autowired
	private JwsConfirmationTokenRepository				confirmationTokenRepository		= null;

	@Autowired
	protected SessionFactory							sessionFactory					= null;

	private ObjectMapper								mapper							= new ObjectMapper()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	public String addEditRole(String roleId) throws Exception, CustomStopException {

		Map<String, Object>	templateMap	= new HashMap<>();
		JwsRole				jwsRole		= new JwsRole();
		try {
			if (StringUtils.isNotEmpty(roleId)) {

				jwsRole = jwsRoleRepository.findById(roleId).get();
				/* Method called for implementing Activity Log */
				logActivity(jwsRole.getRoleName(), Constants.OPEN, Constants.USERMANAGEMENT);
			}
			templateMap.put("jwsRole", jwsRole);
			return menuService.getTemplateWithSiteLayout("addEditRole", templateMap);
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in addEditRole.", custStopException);
			throw custStopException;
		}
	}

	public JwsRole saveRoleData(JwsRoleVO roleData) {
		JwsRole	jwsRole	= roleData.convertVOToEntity(roleData);
		JwsRole	newRole	= jwsRoleRepository.save(jwsRole);
		return newRole;
	}

	public void updateEntityRole(JwsRoleVO roleData, JwsRole newRole) {
		// add role to entity table

		if (Boolean.TRUE.equals(StringUtils.isEmpty(roleData.getRoleId()))) {
			// new role created
			jwsEntityRoleAssociationDAO.createPermission(newRole.getRoleId(), roleData.getIsActive(),
					userDetailsService.getUserDetails().getUserId());
		} else {
			// update entry
			List<JwsEntityRoleAssociation> entityRolesAssociations = entityRoleAssociationRepository
					.getEntityRolesByRoleId(roleData.getRoleId());
			for (JwsEntityRoleAssociation currentJwsEntityRole : entityRolesAssociations) {
				JwsEntityRoleAssociation jwsEntityRoleAssociation = new JwsEntityRoleAssociation();
				jwsEntityRoleAssociation.setEntityRoleId(currentJwsEntityRole.getEntityRoleId());
				jwsEntityRoleAssociation.setEntityName(currentJwsEntityRole.getEntityName());
				jwsEntityRoleAssociation.setEntityId(currentJwsEntityRole.getEntityId());
				jwsEntityRoleAssociation.setIsActive(roleData.getIsActive());
				jwsEntityRoleAssociation.setModuleId(currentJwsEntityRole.getModuleId());
				jwsEntityRoleAssociation.setLastUpdatedBy(userDetailsService.getUserDetails().getUserId());
				jwsEntityRoleAssociation.setLastUpdatedDate(new Date());
				jwsEntityRoleAssociation.setModuleTypeId(currentJwsEntityRole.getModuleTypeId());
				jwsEntityRoleAssociation.setRoleId(roleData.getRoleId());
				jwsEntityRoleAssociation.setIsCustomUpdated(1);
				jwsEntityRoleAssociation.setRoleTypeId(currentJwsEntityRole.getRoleTypeId());
				entityRoleAssociationRepository.save(jwsEntityRoleAssociation);
			}
		}
	}

	public String manageRoleModules() throws Exception, CustomStopException {
		try {
			Map<String, Object>	mapDetails	= new HashMap<>();

			List<JwsRole>		roles		= new ArrayList<>();
			roles = jwsRoleRepository.findAllRoles();

			List<JwsMasterModulesVO>	masterModulesVO	= new ArrayList<>();
			List<JwsMasterModules>		masterModules	= new ArrayList<>();
			masterModules = jwsmasterModuleRepository.findAllModulesForPermission(1);

			for (JwsMasterModules jwsMasterModule : masterModules) {
				masterModulesVO.add(new JwsMasterModulesVO().convertEntityToVO(jwsMasterModule));
			}

			List<JwsRoleMasterModulesAssociation> roleModulesAssociations = new ArrayList<>();
			roleModulesAssociations = roleModuleRepository.findAll();

			mapDetails.put("roles", roles);
			mapDetails.put("masterModules", masterModulesVO);
			mapDetails.put("roleModulesAssociations", roleModulesAssociations);

			TemplateVO templateVO = templatingService.getTemplateByName("manageRoleModule");
			return templatingUtils.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(),
					mapDetails);
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in manageRoleModules.", custStopException);
			throw custStopException;
		}
	}

	/**
	 * Purpose of this method is to log activities</br>
	 * in User Management Module.
	 * 
	 * @author            Bibhusrita.Nayak
	 * @param  entityName
	 * @param  isActive
	 * @param  moduleName
	 * @throws Exception
	 */

	private void logActivity(String entityName, Integer isActive, String moduleName) throws Exception {
		Map<String, String>	requestParams		= new HashMap<>();
		UserDetailsVO		detailsVO			= userDetailsService.getUserDetails();
		Date				activityTimestamp	= new Date();
		String				action				= "";
		if (Constants.ISACTIVE == isActive) {
			action = Constants.Action.PERMISSIONACTIVATED.getAction();
		} else if (Constants.INACTIVE == isActive) {
			action = Constants.Action.PERMISSIONINACTIVATED.getAction();
		} else if (Constants.OPEN == isActive) {
			action = Constants.Action.OPEN.getAction();
		}

		requestParams.put("typeSelect", Constants.Changetype.CUSTOM.getChangetype());
		requestParams.put("entityName", entityName);
		requestParams.put("masterModuleType", moduleName);
		requestParams.put("userName", detailsVO.getUserName());
		requestParams.put("message", "");
		requestParams.put("date", activityTimestamp.toString());
		requestParams.put("action", action);
		activitylog.activitylog(requestParams);
	}

	public Boolean saveRoleModules(JwsRoleMasterModulesAssociationVO roleModule) throws Exception {

		JwsRoleMasterModulesAssociation	masterModuleAssociation	= roleModule.convertVOToEntity(roleModule);

		// roleModuleRepository.save(masterModuleAssociation);

		JwsMasterModules				jwsMasterModule			= jwsmasterModuleRepository
				.findById(masterModuleAssociation.getModuleId()).get();
		masterModuleAssociation.setRoleTypeId(2);
		roleModuleRepository.updateJwsRoleMasterModulesAssociation(jwsMasterModule.getModuleId(),
				masterModuleAssociation.getRoleId(), masterModuleAssociation.getIsActive());
		JwsRole jwsRole = new JwsRole();
		/* Method called for implementing Activity Log */
		logActivity(jwsRole.getRoleName() + "-" + (jwsMasterModule.getModuleName()), roleModule.getIsActive(),
				Constants.MANAGEROLEMODULES);

		// set isactive by moduletype id
		entityRoleAssociationRepository.updateEntityRelatedToModule(jwsMasterModule.getModuleTypeId(),
				masterModuleAssociation.getRoleId(), masterModuleAssociation.getIsActive());

		return true;
	}

	public void saveUserData(JwsUserVO userData) throws Exception {
		String				password	= null;
		JwsUser				jwsUser		= new JwsUser();
		Map<String, Object>	mapDetails	= new HashMap<>();

		userConfigService.getConfigurableDetails(mapDetails);

		if (userData.getIsProfilePage()) {
			jwsUser = jwsUserRepository.findByUserId(userData.getUserId());
			jwsUser.setFirstName(userData.getFirstName());
			jwsUser.setLastName(userData.getLastName());
			if (jwsUser.getIsActive() == Constants.ISACTIVE) {
				jwsUser.setFailedAttempt(0);
			}
			jwsUserRepository.save(jwsUser);
		} else {
			if (StringUtils.isNotEmpty(userData.getUserId())) {
				jwsUser = jwsUserRepository.findByUserId(userData.getUserId());
				jwsUser.setFirstName(userData.getFirstName());
				jwsUser.setLastName(userData.getLastName());
				userManagementDAO.deleteUserRoleAssociation(jwsUser.getUserId());
				jwsUser.setIsActive(userData.getIsActive());
				jwsUser.setForcePasswordChange(userData.getForcePasswordChange());// force password change
				if (jwsUser.getIsActive() == Constants.ISACTIVE) {
					jwsUser.setFailedAttempt(0);
				}
				jwsUserRepository.save(jwsUser);
				if (userData.getForcePasswordChange() == 1 && userData.getIsSendMail() == true) {
					Email email = new Email();
					forcePasswordAndMail(userData, jwsUser, email);
				} else if (userData.getIsSendMail() == true) {
					Email email = new Email();
					sendMailForUserUpdate(jwsUser.getUserId(), userData.getEmail(), email);
				}
			} else {
				jwsUser.setFirstName(userData.getFirstName());
				jwsUser.setLastName(userData.getLastName());
				jwsUser.setEmail(userData.getEmail());
				jwsUser.setRegisteredBy(Constants.AuthType.DAO.getAuthType());
				// jwsUser.setUserId(UUID.randomUUID().toString()); // new user uuid
				jwsUser.setSecretKey(new TwoFactorGoogleUtil().generateSecretKey()); // generating secret key
				Email email = new Email();

				if (mapDetails.get("enableGoogleAuthenticator").toString().equalsIgnoreCase("false")) {
					forcePasswordAndMail(userData, jwsUser, email);
				} else {
					jwsUser.setPassword(null);
					jwsUser.setIsActive(userData.getIsActive());
					jwsUser.setForcePasswordChange(userData.getForcePasswordChange());// force password change
					if (jwsUser.getIsActive() == Constants.ISACTIVE) {
						jwsUser.setFailedAttempt(0);
					}
					jwsUserRepository.save(jwsUser);

					if (userData.getIsSendMail() == true) {
						if (mapDetails.get("authType").toString().equalsIgnoreCase("4")) {
							sendMailForO365Authentication(jwsUser.getUserId(), userData.getForcePasswordChange(),
									userData.getEmail(), email, password);
						} else {
							sendMailForTotpAuthentication(jwsUser, email);
						}
					}
				}
			}

			if (userData.getRoleIds().contains("2ace542e-0c63-11eb-9cf5-f48e38ab9348") == false) {
				userData.getRoleIds().add("2ace542e-0c63-11eb-9cf5-f48e38ab9348");
			}
			for (String roleId : userData.getRoleIds()) {
				Date					currentDate			= new Date();
				JwsUserRoleAssociation	userRoleAssociation	= new JwsUserRoleAssociation();
				userRoleAssociation.setRoleId(roleId);
				userRoleAssociation.setUserId(jwsUser.getUserId());
				userRoleAssociation.setUpdatedDate(currentDate);
				userRoleRepository.save(userRoleAssociation);
			}
		}
	}

	public void saveUserProdifleData(JwsUserVO userData) throws Exception {
		JwsUser jwsUser = new JwsUser();
		if (userData.getIsProfilePage()) {
			jwsUser = jwsUserRepository.findByUserId(userData.getUserId());
			jwsUser.setFirstName(userData.getFirstName());
			jwsUser.setLastName(userData.getLastName());
			if (jwsUser.getIsActive() == Constants.ISACTIVE) {
				jwsUser.setFailedAttempt(0);
			}
			jwsUserRepository.save(jwsUser);
		}
	}

	public void saveUserRolesAndPolicies(Integer isEdit, JwsUserVO userData) throws Exception {
		String	password	= null;
		JwsUser	jwsUser		= new JwsUser();
		if (isEdit != null && isEdit.equals(Constants.IS_NEW_USER)) {
			jwsUser.setUserId(userData.getUserId());
			jwsUser.setFirstName(userData.getFirstName());
			jwsUser.setLastName(userData.getLastName());
			jwsUser.setRegisteredBy(Constants.AuthType.DAO.getAuthType());
			if (jwsUser.getIsActive() == Constants.ISACTIVE) {
				jwsUser.setFailedAttempt(0);
			}
			jwsUserRepository.save(jwsUser);
		}
		jwsUser = jwsUserRepository.findByUserId(userData.getUserId());
		Integer	forcePasswordChange	= userData.getForcePasswordChange();
		Integer	isActive			= userData.getIsActive();
		if (forcePasswordChange == 1) {
			isActive = Constants.INACTIVE;
		}
		jwsUser.setForcePasswordChange(forcePasswordChange);
		jwsUser.setIsActive(isActive);
		if (jwsUser.getIsActive() == Constants.ISACTIVE) {
			jwsUser.setFailedAttempt(0);
		}
		jwsUserRepository.save(jwsUser);

		if (userData.getForcePasswordChange() == 1) {
			Email email = new Email();
			userData.setEmail(jwsUser.getEmail());
			forcePasswordAndMail(userData, jwsUser, email);
		} else if (userData.getIsSendMail() == true) {
			Email email = new Email();
			sendMailForUserUpdate(jwsUser.getUserId(), userData.getEmail(), email);
		}

		userManagementDAO.deleteUserRoleAssociation(jwsUser.getUserId());
		for (String roleId : userData.getRoleIds()) {
			Date					currentDate			= new Date();
			JwsUserRoleAssociation	userRoleAssociation	= new JwsUserRoleAssociation();
			userRoleAssociation.setRoleId(roleId);
			userRoleAssociation.setUserId(jwsUser.getUserId());
			userRoleAssociation.setUpdatedDate(currentDate);
			userRoleRepository.save(userRoleAssociation);
		}
	}

	private void forcePasswordAndMail(JwsUserVO userData, JwsUser jwsUser, Email email) throws Exception {
		String password;
		password = UUID.randomUUID().toString();
		jwsUser.setPassword(passwordEncoder.encode(password));
		jwsUser.setIsActive(userData.getIsActive());
		jwsUser.setForcePasswordChange(userData.getForcePasswordChange());// force password change
		if (jwsUser.getIsActive() == Constants.ISACTIVE) {
			jwsUser.setFailedAttempt(0);
		}
		jwsUserRepository.save(jwsUser);

		if (userData.getIsSendMail() == true) {
			sendMailForForcePassword(jwsUser.getUserId(), userData.getForcePasswordChange(), userData.getEmail(), email,
					password);
		}
	}

	public static String getBaseURL(PropertyMasterService propertyMasterService, ServletContext servletContext)
			throws Exception {
		String baseURL = propertyMasterService.findPropertyMasterValue("base-url");
		if (servletContext.getContextPath().isBlank() == false) {
			baseURL = baseURL + servletContext.getContextPath();
		}
		return baseURL;
	}

	public String addEditUser(String userId, boolean isProfilePage) throws Exception {
		Map<String, Object>	templateMap	= new HashMap<>();
		String				formId		= getDaoPropertyValueByName("enableDynamicForm", "formName");
		userConfigService.getConfigurableDetails(templateMap);
		JwsUser			jwsUser		= new JwsUser();
		List<String>	userRoleIds	= new ArrayList<>();
		templateMap.put("userId", userId);
		templateMap.put("formId", formId);
		templateMap.put("verificationType", templateMap.get("verificationType"));
		templateMap.put("authenticationType", templateMap.get("authenticationType"));

		if (StringUtils.isNotEmpty(userId) && StringUtils.isBlank(formId)) {

			jwsUser = jwsUserRepository.findById(userId).get();
			/* Method called for implementing Activity Log */
			logActivity(jwsUser.getEmail(), Constants.OPEN, Constants.USERMANAGEMENT);
		}
		if (StringUtils.isNotEmpty(userId)) {
			userRoleIds = userManagementDAO.getRoleIdsByUserId(userId);
		}
		JwsRole authenticatedRole = jwsRoleRepository.findByRoleName(Constants.ANONYMOUS_ROLE_NAME);
		userRoleIds.add(authenticatedRole.getRoleId());
		templateMap.put("userRoleIds", userRoleIds);

		List<JwsRole> roles = new ArrayList<>();
		roles = jwsRoleRepository.findAllRoles().stream()
				.filter(role -> !(role.getRoleName().equalsIgnoreCase(Constants.ANONYMOUS_ROLE_NAME)))
				.collect(Collectors.toList());
		templateMap.put("roles", roles);
		templateMap.put("jwsUser", jwsUser);
		templateMap.put("isProfilePage", isProfilePage);
		templateMap.put("verificationType", templateMap.get("verificationType"));
		if (StringUtils.isNotEmpty(formId)) {
			return menuService.getTemplateWithSiteLayout("jws-user-manage-details", templateMap);
		}
		return menuService.getTemplateWithSiteLayout("addEditJwsUser", templateMap);
	}

	public String manageUserRoleAndPermission(String userId) throws Exception {
		Map<String, Object> templateMap = new HashMap<>();
		userConfigService.getConfigurableDetails(templateMap);
		List<String>	userRoleIds	= new ArrayList<>();
		JwsUser			jwsUser		= new JwsUser();
		if (StringUtils.isNotEmpty(userId)) {

			jwsUser		= jwsUserRepository.findById(userId).get();
			userRoleIds	= userManagementDAO.getRoleIdsByUserId(userId);
		}
		JwsRole authenticatedRole = jwsRoleRepository.findByRoleName(Constants.ANONYMOUS_ROLE_NAME);
		userRoleIds.add(authenticatedRole.getRoleId());
		templateMap.put("userRoleIds", userRoleIds);

		List<JwsRole> roles = new ArrayList<>();
		roles = jwsRoleRepository.findAllRoles().stream()
				.filter(role -> !(role.getRoleName().equalsIgnoreCase(Constants.ANONYMOUS_ROLE_NAME)))
				.collect(Collectors.toList());
		templateMap.put("roles", roles);
		templateMap.put("jwsUser", jwsUser);
		return menuService.getTemplateWithoutLayout("manage-user-roles-policy", templateMap);
	}

	public String loadUserManagement() throws Exception, CustomStopException {
		try {
			Map<String, Object>				authenticationDetails	= applicationSecurityDetails
					.getAuthenticationDetails();
			List<JwsAuthenticationTypeVO>	authenticationTypes		= new ArrayList<JwsAuthenticationTypeVO>();
			List<JwsAuthenticationTypeVO>	activAuthDetails		= new ArrayList<JwsAuthenticationTypeVO>();
			for (JwsAuthenticationType authenticationType : authenticationTypeRepository.getAuthenticationTypes()) {
				JwsAuthenticationTypeVO authenticationTypeVO = new JwsAuthenticationTypeVO()
						.convertEntityToVO(authenticationType);
				authenticationTypes.add(authenticationTypeVO);
			}

			List<MultiAuthSecurityDetailsVO> multiAuthSecurityDetails = (List<MultiAuthSecurityDetailsVO>) authenticationDetails
					.get("authenticationDetails");
			for (MultiAuthSecurityDetailsVO sauthScurityDetails : multiAuthSecurityDetails) {
				ConnectionDetailsJSONSpecification connectionDetails = sauthScurityDetails.getConnectionDetailsVO();
				if (connectionDetails != null && connectionDetails.getAuthenticationType() != null
						&& connectionDetails.getAuthenticationType().getValue().equalsIgnoreCase("true")) {
					JwsAuthenticationTypeVO authenticationTypeVO = sauthScurityDetails.getAuthenticationTypeVO();
					activAuthDetails.add(authenticationTypeVO);
				}
			}
			authenticationDetails.put("activAuthDetails", activAuthDetails);
			authenticationDetails.put("authenticationDetails", multiAuthSecurityDetails);
			authenticationDetails.put("authenticationTypes", authenticationTypes);

			return menuService.getTemplateWithSiteLayout("user-management", authenticationDetails);
		} catch (CustomStopException custStopException) {
			logger.error("Error occured while loading User Management page.", custStopException);
			throw custStopException;
		}
	}

	private void sendMailForForcePassword(String userId, Integer forcePasswordChange, String emailId, Email email,
			String password) throws Exception, CustomStopException {
		try {
			Map<String, Object> mailDetails = new HashMap<>();
			mailDetails.put("password", password);
			mailDetails.put("forcePasswordChange", forcePasswordChange);
			mailDetails.put("userId", userId);

			if (userId != null && userId.isEmpty() == false) {
				JwsUser jwsUser = jwsUserRepository.findByUserId(userId);
				mailDetails.put("firstName", jwsUser.getFirstName() + " " + jwsUser.getLastName());
			}
			String baseURL = getBaseURL(propertyMasterService, servletContext);
			mailDetails.put("baseURL", baseURL);
			email.setInternetAddressToArray(InternetAddress.parse(emailId));
			/* For inserting notification in case of mail failure only on access of Admin */
			email.setIsAuthenticationEnabled(applicationSecurityDetails.getIsAuthenticationEnabled());
			email.setLoggedInUserRole(userDetailsService.getUserDetails().getRoleIdList());
			TemplateVO	subjectTemplateVO	= templatingService.getTemplateByName("force-password-mail-subject");
			String		subject				= templatingUtils.processTemplateContents(subjectTemplateVO.getTemplate(),
					subjectTemplateVO.getTemplateName(), mailDetails);
			email.setSubject(subject);

			TemplateVO	templateVO	= templatingService.getTemplateByName("force-password-mail");
			String		mailBody	= templatingUtils.processTemplateContents(templateVO.getTemplate(),
					templateVO.getTemplateName(), mailDetails);
			email.setBody(mailBody);
			// System.out.println(mailBody);
			sendMailService.sendTestMail(email);
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in sendMailForForcePassword.", custStopException);
			throw custStopException;
		}
	}

	private void sendMailForUserUpdate(String userId, String emailId, Email email)
			throws Exception, CustomStopException {
		try {
			Map<String, Object> mailDetails = new HashMap<>();
			mailDetails.put("forcePasswordChange", 0);
			mailDetails.put("userId", userId);
			String baseURL = getBaseURL(propertyMasterService, servletContext);

			mailDetails.put("baseURL", baseURL);
			/* For inserting notification in case of mail failure only on access of Admin */
			email.setIsAuthenticationEnabled(applicationSecurityDetails.getIsAuthenticationEnabled());
			email.setLoggedInUserRole(userDetailsService.getUserDetails().getRoleIdList());
			email.setInternetAddressToArray(InternetAddress.parse(emailId));
			TemplateVO	subjectTemplateVO	= templatingService.getTemplateByName("user-updated-mail-subject");
			String		subject				= templatingUtils.processTemplateContents(subjectTemplateVO.getTemplate(),
					subjectTemplateVO.getTemplateName(), mailDetails);
			email.setSubject(subject);

			TemplateVO	templateVO	= templatingService.getTemplateByName("user-updated-mail");
			String		mailBody	= templatingUtils.processTemplateContents(templateVO.getTemplate(),
					templateVO.getTemplateName(), mailDetails);
			email.setBody(mailBody);
			// System.out.println(mailBody);
			sendMailService.sendTestMail(email);
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in sendMailForUserUpdate.", custStopException);
			throw custStopException;
		}
	}

	public void sendMailForTotpAuthentication(JwsUser jwsUser, Email email)
			throws FileNotFoundException, AddressException, Exception, CustomStopException {
		try {
			TwoFactorGoogleUtil	twoFactorGoogleUtil	= new TwoFactorGoogleUtil();
			int					width				= 300;
			int					height				= 300;
			String				filePath			= System.getProperty("java.io.tmpdir") + File.separator
					+ jwsUser.getUserId() + ".png";
			File				file				= new File(filePath);
			FileOutputStream	fileOutputStream	= new FileOutputStream(filePath);
			String				barcodeData			= twoFactorGoogleUtil
					.getGoogleAuthenticatorBarCode(jwsUser.getEmail(), "Jquiver", jwsUser.getSecretKey());
			twoFactorGoogleUtil.createQRCode(barcodeData, fileOutputStream, height, width);

			email.setInternetAddressToArray(InternetAddress.parse(jwsUser.getEmail()));
			/* For inserting notification in case of mail failure only on access of Admin */
			email.setIsAuthenticationEnabled(applicationSecurityDetails.getIsAuthenticationEnabled());
			email.setLoggedInUserRole(userDetailsService.getUserDetails().getRoleIdList());
			Map<String, Object>	mailDetails				= new HashMap<>();
			String				propertyAdminEmailId	= propertyMasterService.findPropertyMasterValue("system",
					"system", "adminEmailId");
			String				adminEmail				= propertyAdminEmailId == null ? "admin@jquiver.io"
					: propertyAdminEmailId.equals("") ? "admin@jquiver.io" : propertyAdminEmailId;

			TemplateVO			subjectTemplateVO		= templatingService.getTemplateByName("totp-subject");
			String				subject					= templatingUtils.processTemplateContents(
					subjectTemplateVO.getTemplate(), subjectTemplateVO.getTemplateName(), mailDetails);
			email.setSubject(subject);
			// email.setMailFrom("admin@jquiver.com");
			if (jwsUser != null) {
				mailDetails.put("firstName", jwsUser.getFirstName() + " " + jwsUser.getLastName());
			}
			mailDetails.put("adminEmailAddress", adminEmail);
			String baseURL = getBaseURL(propertyMasterService, servletContext);
			mailDetails.put("baseURL", baseURL);
			TemplateVO	templateVO	= templatingService.getTemplateByName("totp-qr-mail");
			String		mailBody	= templatingUtils.processTemplateContents(templateVO.getTemplate(),
					templateVO.getTemplateName(), mailDetails);
			email.setBody(mailBody);
			// System.out.println(mailBody);
			List<EmailAttachedFile>	attachedFiles		= new ArrayList<>();
			EmailAttachedFile		emailAttachedFile	= new EmailAttachedFile();
			emailAttachedFile.setFile(file);
			attachedFiles.add(emailAttachedFile);
			EmailAttachedFile qrCodeAttachment = new EmailAttachedFile();
			qrCodeAttachment.setFile(file);
			qrCodeAttachment.setIsEmbeddedImage(true);
			qrCodeAttachment.setEmbeddedImageValue("qrcode");
			attachedFiles.add(qrCodeAttachment);
			email.setAttachementsArray(attachedFiles);
			CompletableFuture<Boolean> mailSuccess = sendMailService.sendTestMail(email);
			if (mailSuccess.isDone()) {
				email.getAttachementsArray().stream().forEach(f -> f.getFile().delete());
			}
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in sendMailForTotpAuthentication.", custStopException);
			throw custStopException;
		}
	}

	private void sendMailForO365Authentication(String userId, Integer o365Type, String emailId, Email email,
			String password) throws Exception, CustomStopException {
		try {
			Map<String, Object> mailDetails = new HashMap<>();
			mailDetails.put("userId", userId);
			mailDetails.put("o365-type", o365Type);
			String baseURL = getBaseURL(propertyMasterService, servletContext);
			mailDetails.put("baseURL", baseURL);

			email.setInternetAddressToArray(InternetAddress.parse(emailId));
			/* For inserting notification in case of mail failure only on access of Admin */
			email.setIsAuthenticationEnabled(applicationSecurityDetails.getIsAuthenticationEnabled());
			email.setLoggedInUserRole(userDetailsService.getUserDetails().getRoleIdList());
			TemplateVO	subjectTemplateVO	= templatingService.getTemplateByName("o365-mail-subject");
			String		subject				= templatingUtils.processTemplateContents(subjectTemplateVO.getTemplate(),
					subjectTemplateVO.getTemplateName(), mailDetails);
			email.setSubject(subject);

			TemplateVO	templateVO	= templatingService.getTemplateByName("o365-mail");
			String		mailBody	= templatingUtils.processTemplateContents(templateVO.getTemplate(),
					templateVO.getTemplateName(), mailDetails);
			email.setBody(mailBody);
			// System.out.println(mailBody);
			sendMailService.sendTestMail(email);
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in sendMailForO365Authentication.", custStopException);
			throw custStopException;
		}
	}

	public String manageEntityRoles() throws Exception, CustomStopException {
		try {
			Map<String, Object>	mapDetails	= new HashMap<>();
			List<JwsRole>		roles		= new ArrayList<>();
			roles = jwsRoleRepository.findAllRoles();
			mapDetails.put("roles", roles);

			TemplateVO templateVO = templatingService.getTemplateByName("manageEntityRoles");
			return templatingUtils.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(),
					mapDetails);
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in manageEntityRoles.", custStopException);
			throw custStopException;
		}
	}

	public void saveUpdateEntityRole(List<JwsEntityRoleAssociationVO> entityRoleAssociations) {

		if (userDetailsService == null || userDetailsService.getUserDetails() == null) {
			logger.error("Updation of permission is not allowed while authentication is disabled.", "error");
			return;
		}

		for (JwsEntityRoleAssociationVO jwsEntityRoleAssociationVO : entityRoleAssociations) {
			JwsEntityRoleAssociation	jwsEntityRoleAssociationData	= entityRoleAssociationRepository
					.getJwsEntityRoleAssociation(jwsEntityRoleAssociationVO.getEntityRoleId());
			JwsEntityRoleAssociation	jwsEntityRoleAssociation		= jwsEntityRoleAssociationVO
					.convertVOtoEntity(jwsEntityRoleAssociationData, jwsEntityRoleAssociationVO);
			// logActivity(jwsEntityRoleAssociation.getJwsRole().getRoleName()+'-'+jwsEntityRoleAssociation.getEntityName(),jwsEntityRoleAssociation.getIsActive(),Constants.MANAGEENTITYROLES);
			String						updatedBy						= null;
			if (userDetailsService != null && userDetailsService.getUserDetails() != null
					&& userDetailsService.getUserDetails().getUserId() != null) {
				updatedBy = userDetailsService.getUserDetails().getUserId();
			}

			if (updatedBy == null) {
				logger.error("Updation of permission is not allowed for invalid user.", "error");
				return;
			}

			jwsEntityRoleAssociation.setLastUpdatedBy(updatedBy);
			jwsEntityRoleAssociation.setEntityRoleId(jwsEntityRoleAssociationVO.getEntityRoleId());
			jwsEntityRoleAssociation.setRoleTypeId(jwsEntityRoleAssociationVO.getRoleTypeId());
			entityRoleAssociationRepository.save(jwsEntityRoleAssociation);
		}
	}

	public List<JwsMasterModulesVO> getModules() {

		List<JwsMasterModulesVO>	masterModulesVO	= new ArrayList<>();
		List<JwsMasterModules>		masterModules	= new ArrayList<>();
		masterModules = jwsmasterModuleRepository.findAllModulesForEntityLevelPermission(1);

		for (JwsMasterModules jwsMasterModule : masterModules) {
			masterModulesVO.add(new JwsMasterModulesVO().convertEntityToVO(jwsMasterModule));
		}

		return masterModulesVO;
	}

	/**
	 * Added for getting the TypeSelect whether Custom or System of all Modules.
	 * 
	 * @author             Bibhusrita.Nayak
	 * 
	 * @param  entityRoles is the list of entity role object.
	 * @return             integer value to get roleTypeId
	 * 
	 */
	public Integer getRoleTypeID(JwsEntityRoleVO entityRoles) {

		String query = "";
		if (entityRoles.getModuleId().equals("1b0a2e40-098d-11eb-9a16-f48e38ab9348")) {

			query = "SELECT templateTypeId FROM TemplateMaster WHERE templateId = '" + entityRoles.getEntityId() + "'";

		} else if (entityRoles.getModuleId().equals("30a0ff61-0ecf-11eb-94b2-f48e38ab9348")) {

			query = "SELECT formTypeId FROM DynamicForm WHERE formId = '" + entityRoles.getEntityId() + "'";

		} else if (entityRoles.getModuleId().equals("07067149-098d-11eb-9a16-f48e38ab9348")) {

			query = "SELECT gridTypeId FROM GridDetails WHERE gridId = '" + entityRoles.getEntityId() + "'";

		} else if (entityRoles.getModuleId().equals("248ffd91-7760-11eb-94ed-f48e38ab8cd7")) {

			query = "SELECT 1 from FileUploadConfig where fileBinId = '" + entityRoles.getEntityId() + "'";

		} else if (entityRoles.getModuleId().equals("6ac6a54c-8d3f-11eb-8dcd-0242ac130003")) {

			query = "SELECT 1";

		} else if (entityRoles.getModuleId().equals("76270518-8c8f-11eb-8dcd-0242ac130003")) {

			query = "SELECT 1";

		} else if (entityRoles.getModuleId().equals("5f6dd374-8c8f-11eb-8dcd-0242ac130003")) {

			query = "SELECT 2 ";

		} else if (entityRoles.getModuleId().equals("47030ee1-0ecf-11eb-94b2-f48e38ab9348")) {

			query = "SELECT jwsDynamicRestTypeId FROM JwsDynamicRestDetail WHERE jwsDynamicRestId = '"
					+ entityRoles.getEntityId() + "'";

		} else if (entityRoles.getModuleId().equals("5559212c-8c8f-11eb-8dcd-0242ac130003")) {

			query = "SELECT 2";

		} else if (entityRoles.getModuleId().equals("91a81b68-0ece-11eb-94b2-f48e38ab9348")) {

			query = "SELECT acTypeId FROM Autocomplete WHERE autocompleteId = '" + entityRoles.getEntityId() + "'";

		} else if (entityRoles.getModuleId().equals("b0f8646c-0ecf-11eb-94b2-f48e38ab9348")) {

			query = "SELECT dashboardType FROM Dashboard WHERE dashboardId = '" + entityRoles.getEntityId() + "'";

		} else if (entityRoles.getModuleId().equals("c6cc466a-0ed3-11eb-94b2-f48e38ab9348")) {

			query = "SELECT moduleTypeId FROM ModuleListing WHERE moduleId = '" + entityRoles.getEntityId() + "'";

		} else if (entityRoles.getModuleId().equals("7982cc6a-6bd3-11ed-997d-7c8ae1bb24d8")) {

			query = "SELECT 1";

		} else if (entityRoles.getModuleId().equals("fcd0df1f-783f-11eb-94ed-f48e38ab8cd7")) {

			query = "SELECT isSystemManual FROM ManualType WHERE manualId = '" + entityRoles.getEntityId() + "'";

		} else if (entityRoles.getModuleId().equals("19aa8996-80a2-11eb-971b-f48e38ab8cd7")) {

			query = "SELECT dashletTypeId FROM Dashlet WHERE dashletId = '" + entityRoles.getEntityId() + "'";
		}

		return userManagementDAO.getEntityRoleTypeID(query);
	}

	public void deleteAndSaveEntityRole(JwsEntityRoleVO entityRoles) {

		if (!entityRoles.getRoleIds().contains(Constants.ADMIN_ROLE_ID)) {
			entityRoles.getRoleIds().add(Constants.ADMIN_ROLE_ID);
			entityRoles.setRoleIds(entityRoles.getRoleIds());
		}
		Integer							roleTypeId				= getRoleTypeID(entityRoles);

		List<String>					newRoleIds				= new ArrayList<>(entityRoles.getRoleIds());
		List<JwsEntityRoleAssociation>	entityRoleAssociations	= entityRoleAssociationRepository
				.getEntityRoles(entityRoles.getEntityId(), entityRoles.getModuleId());
		for (JwsEntityRoleAssociation jwsEntityRoleAssociation : entityRoleAssociations) {
			// inactive
			if (!entityRoles.getRoleIds().contains(jwsEntityRoleAssociation.getRoleId())) {
				newRoleIds.remove(jwsEntityRoleAssociation.getRoleId());
				jwsEntityRoleAssociation.setEntityName(entityRoles.getEntityName());
				jwsEntityRoleAssociation.setLastUpdatedDate(new Date());
				jwsEntityRoleAssociation.setLastUpdatedBy(userDetailsService.getUserDetails().getUserId());
				jwsEntityRoleAssociation.setIsActive(Constants.INACTIVE);
				jwsEntityRoleAssociation.setRoleTypeId(roleTypeId);
				entityRoleAssociationRepository.save(jwsEntityRoleAssociation);
			} else {
				newRoleIds.remove(jwsEntityRoleAssociation.getRoleId());
				jwsEntityRoleAssociation.setEntityName(entityRoles.getEntityName());
				jwsEntityRoleAssociation.setIsActive(Constants.ISACTIVE);
				jwsEntityRoleAssociation.setLastUpdatedDate(new Date());
				jwsEntityRoleAssociation.setLastUpdatedBy(userDetailsService.getUserDetails().getUserId());
				jwsEntityRoleAssociation.setRoleTypeId(roleTypeId);
				entityRoleAssociationRepository.save(jwsEntityRoleAssociation);
			}
		}

		for (String roleId : newRoleIds) {
			JwsEntityRoleAssociation entityRoleAssociation = null;
			 entityRoleAssociation = entityRoleAssociationRepository.findByCompositeKeys(entityRoles.getEntityId(), roleId , entityRoles.getModuleId()).orElse(null);
			if(entityRoleAssociation != null) {
				 entityRoleAssociation.setIsActive(Constants.ISACTIVE);
			}else {
				entityRoleAssociation = new JwsEntityRoleAssociation();			
				entityRoleAssociation.setRoleId(roleId);
				entityRoleAssociation.setEntityId(entityRoles.getEntityId());
				entityRoleAssociation.setEntityName(entityRoles.getEntityName());
				entityRoleAssociation.setModuleId(entityRoles.getModuleId());
				entityRoleAssociation.setIsActive(Constants.ISACTIVE);
				entityRoleAssociation.setModuleTypeId(Constants.DEFAULT_MODULE_TYPE_ID);
				entityRoleAssociation.setLastUpdatedDate(new Date());
				entityRoleAssociation.setLastUpdatedBy(userDetailsService.getUserDetails().getUserId());
				entityRoleAssociation.setRoleTypeId(roleTypeId);
			}
				entityRoleAssociationRepository.save(entityRoleAssociation);
		}

	}

	public List<JwsRoleVO> getEntityRoles(String entityId, String moduleId) {
		List<JwsRoleVO> roles = entityRoleAssociationRepository.getRoles(entityId, moduleId, Constants.ISACTIVE);
		return roles;
	}

	public Boolean validatePassword(String password) throws Exception {
		Boolean isValid = Boolean.FALSE;
		if (password == null || (password != null && password.trim().isEmpty())) {
			return isValid;
		} else {
			Map<String, Object> authenticationDetails = new HashMap<>();
			userConfigService.getConfigurableDetails(authenticationDetails);
			if (authenticationDetails != null) {
				@SuppressWarnings("unchecked")
				List<JwsUserLoginVO>	multiAuthLoginVOs	= (List<JwsUserLoginVO>) authenticationDetails
						.get("activeAutenticationDetails");
				JwsUserLoginVO			multiAuthLoginVO	= multiAuthLoginVOs.stream()
						.filter(loginVO -> loginVO.getAuthenticationType().equals(Constants.AuthType.DAO.getAuthType()))
						.findAny().orElse(null);
				if (multiAuthLoginVO != null) {
					Map<String, Object> daoAuthAttributes = multiAuthLoginVO.getLoginAttributes();
					if (daoAuthAttributes != null && daoAuthAttributes.containsKey("enableVerificationStep")) {
						String enableVerificationStepValue = (String) daoAuthAttributes.get("enableVerificationStep");
						if (enableVerificationStepValue.equalsIgnoreCase("true")) {
							if (daoAuthAttributes != null && daoAuthAttributes.containsKey("enableRegex")) {
								String enableRegexValue = (String) daoAuthAttributes.get("enableRegex");
								if (enableRegexValue != null && enableRegexValue.equalsIgnoreCase("true")) {
									if (daoAuthAttributes != null && daoAuthAttributes.containsKey("regexPattern")) {
										String regexPattern = (String) daoAuthAttributes.get("regexPattern");
										if (regexPattern != null && regexPattern.isEmpty() == false) {
											//String	unescapePattern	= StringEscapeUtils.escapeJava(regexPattern.replaceAll("/",""));
											String	escapePattern	= StringEscapeUtils.unescapeJava(regexPattern.toString());
											Pattern	pattern			= Pattern.compile(escapePattern);
											Matcher	isMatches		= pattern.matcher(password);
											isValid = Boolean.TRUE;
											return isMatches.matches();
										}
									}
								} else {
									isValid = Boolean.TRUE;
								}
							}
						}
					}
				}
			}
		}
		return isValid;
	}

	public JwsUser findByEmailIgnoreCase(String email) {
		return jwsUserRepository.findByEmailIgnoreCase(email);
	}

	public String getProfilePage() throws Exception {

		boolean			isProfilePage	= true;
		UserInformation	userDetails		= (UserInformation) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		String			userId			= userDetails.getUserId();
		String			userName		= userDetails.getUsername();
		String			formId			= getDaoPropertyValueByName("enableDynamicForm", "formName");
		if (!StringUtils.isBlank(formId)) {
			Map<String, Object> requestParam = new HashMap<>();
			requestParam.put("userId", userId);
			requestParam.put("userName", userName);
			return dynamicFormService.loadDynamicForm(formId, requestParam, null);
		}
		return addEditUser(userId, isProfilePage);

	}

	public String getDaoPropertyValueByName(String propertyName, String propertyKey) throws Exception {
		Map<String, Object> authenticationDetails = new HashMap<>();
		userConfigService.getConfigurableDetails(authenticationDetails);
		@SuppressWarnings("unchecked")
		List<JwsUserLoginVO> multiAuthLoginVOs = (List<JwsUserLoginVO>) authenticationDetails
				.get("activeAutenticationDetails");
		if (checkVerificationStep(multiAuthLoginVOs)) {
			if (multiAuthLoginVOs != null) {
				JwsUserLoginVO		multiAuthLoginVO	= multiAuthLoginVOs.stream()
						.filter(loginVO -> loginVO.getAuthenticationType().equals(Constants.AuthType.DAO.getAuthType()))
						.findAny().orElse(null);
				Map<String, Object>	daoAuthAttributes	= multiAuthLoginVO.getLoginAttributes();
				if (daoAuthAttributes != null && daoAuthAttributes.containsKey(propertyName)) {
					if (daoAuthAttributes.containsKey(propertyKey)) {
						return (String) daoAuthAttributes.get(propertyKey);
					}
				}
			}
		}
		return null;
	}

	public String getInputFieldsByProperty(String propertyName) throws Exception {
		String propertyMasterRegex = propertyMasterService.findPropertyMasterValue("system", "system", propertyName);
		return propertyMasterRegex;
	}

	public JwsEntityRoleAssociation findByEntityRoleID(String entityRoleId) throws Exception {
		return entityRoleAssociationRepository.getJwsEntityRoleAssociation(entityRoleId);
	}

	public void saveJwsEntityRoleAssociation(JwsEntityRoleAssociation jwsRoleAssoc) throws Exception {
		entityRoleAssociationRepository.save(jwsRoleAssoc);
	}

	public String getJwsEntityRoleAssociationJson(String entityId) throws Exception {

		JwsEntityRoleAssociation	role		= findByEntityRoleID(entityId);
		String						jsonString	= "";
		if (role != null) {
			role = role.getObject();
			JwsEntityRoleAssociationVO vo = new JwsEntityRoleAssociationVO();
			vo = vo.convertEntityToVO(role);
			Gson			gson			= new Gson();

			ObjectMapper	objectMapper	= new ObjectMapper();
			String			dbDateFormat	= propertyMasterService.getDateFormatByName(
					Constant.PROPERTY_MASTER_OWNER_TYPE, Constant.PROPERTY_MASTER_OWNER_ID,
					Constant.JWS_DATE_FORMAT_PROPERTY_NAME,
					com.trigyn.jws.dbutils.utils.Constant.JWS_JAVA_DATE_FORMAT_PROPERTY_NAME);
			DateFormat		dateFormat		= new SimpleDateFormat(dbDateFormat);
			objectMapper.setDateFormat(dateFormat);
			Map<String, Object> objectMap = objectMapper.convertValue(vo, TreeMap.class);
			jsonString = gson.toJson(objectMap);
		}

		return jsonString;
	}

	public boolean checkAuthenticationEnabled() throws Exception {
		String propertyName = "enable-user-management";
		propertyMasterDetails.resetPropertyMasterDetails();
		String propertyValue = propertyMasterService.findPropertyMasterValue("system", "system", propertyName);
		return Boolean.valueOf(propertyValue);
	}

	public String getJwsUserJson(String entityId) throws Exception {
		JwsUser	user		= getJwsUser(entityId);
		String	jsonString	= "";
		if (user != null) {
			user = user.getObject();

			JwsUserVO		vo				= user.convertEntityToVO(user);
			Gson			gson			= new Gson();
			ObjectMapper	objectMapper	= new ObjectMapper();
			String			dbDateFormat	= propertyMasterService.getDateFormatByName(
					Constant.PROPERTY_MASTER_OWNER_TYPE, Constant.PROPERTY_MASTER_OWNER_ID,
					Constant.JWS_DATE_FORMAT_PROPERTY_NAME,
					com.trigyn.jws.dbutils.utils.Constant.JWS_JAVA_DATE_FORMAT_PROPERTY_NAME);
			DateFormat		dateFormat		= new SimpleDateFormat(dbDateFormat);
			objectMapper.setDateFormat(dateFormat);
			Map<String, Object> objectMap = objectMapper.convertValue(vo, TreeMap.class);
			jsonString = gson.toJson(objectMap);

		}
		return jsonString;
	}

	public JwsUser getJwsUser(String userId) throws Exception {
		return jwsUserRepository.findByUserId(userId);
	}

	public String getJwsRoleJson(String entityId) throws Exception {
		JwsRole	role		= getJwsRole(entityId);
		String	jsonString	= "";
		if (role != null) {
			role = role.getObject();

			JwsRoleVO		vo				= role.convertEntityToVO(role);
			Gson			gson			= new Gson();
			ObjectMapper	objectMapper	= new ObjectMapper();
			String			dbDateFormat	= propertyMasterService.getDateFormatByName(
					Constant.PROPERTY_MASTER_OWNER_TYPE, Constant.PROPERTY_MASTER_OWNER_ID,
					Constant.JWS_DATE_FORMAT_PROPERTY_NAME,
					com.trigyn.jws.dbutils.utils.Constant.JWS_JAVA_DATE_FORMAT_PROPERTY_NAME);
			DateFormat		dateFormat		= new SimpleDateFormat(dbDateFormat);
			objectMapper.setDateFormat(dateFormat);
			Map<String, Object> objectMap = objectMapper.convertValue(vo, TreeMap.class);
			jsonString = gson.toJson(objectMap);

		}
		return jsonString;
	}

	public JwsRole getJwsRole(String role) throws Exception {
		return jwsRoleRepository.findByRoleName(role);
	}

	public JwsUser saveJwsUser(JwsUser user) throws Exception {
		return jwsUserRepository.save(user);
	}

	public void saveJwsRole(JwsRole role) throws Exception {
		jwsRoleRepository.save(role);
	}

	public void forceChangePassword() throws Exception {
		List<JwsUser>	jwsUsers				= jwsUserRepository.findAll();
		String			propertyAdminEmailId	= propertyMasterService.findPropertyMasterValue("system", "system",
				"adminEmailId");
		String			adminEmail				= propertyAdminEmailId == null ? "admin@jquiver.io"
				: propertyAdminEmailId.equals("") ? "admin@jquiver.io" : propertyAdminEmailId;
		for (JwsUser jwsUser : jwsUsers) {
			if (jwsUser.getEmail().equalsIgnoreCase(adminEmail) == false
					&& jwsUser.getEmail().equalsIgnoreCase(adminEmail) == false) {
				String password = UUID.randomUUID().toString();
				jwsUser.setPassword(passwordEncoder.encode(password));
				jwsUser.setForcePasswordChange(1);// force password change
				jwsUser.setFailedAttempt(0);
				jwsUserRepository.save(jwsUser);
				Email email = new Email();
				sendMailForForcePassword(jwsUser.getUserId(), 1, jwsUser.getEmail(), email, password);
			}
		}
	}

	private Map<Integer, String> getDefaultProperties() throws JsonMappingException, JsonProcessingException {
		Map<Integer, String>		resetMap			= new HashMap<>();
		List<JwsAuthenticationType>	currentAuthTypes	= authenticationTypeRepository.getAuthenticationTypes();
		for (JwsAuthenticationType jwsAuthenticationType : currentAuthTypes) {

			mapper = new ObjectMapper().setSerializationInclusion(Include.NON_NULL);
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			JwsAuthenticationTypeVO				currentAuthTypeVO		= new JwsAuthenticationTypeVO()
					.convertEntityToVO(jwsAuthenticationType);
			String								currentAuthProperties	= currentAuthTypeVO
					.getDefaultAuthProperties();
			ConnectionDetailsJSONSpecification	defaultAuthType			= mapper.readValue(currentAuthProperties,
					new TypeReference<ConnectionDetailsJSONSpecification>() {
																				});
			
			mapper = new ObjectMapper().setSerializationInclusion(Include.NON_NULL);
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			String propertyJson = mapper.writeValueAsString(defaultAuthType);
			resetMap.put(currentAuthTypeVO.getId(), propertyJson);
		}
		return resetMap;

	}

	public boolean updateAuthProperties(UserManagementVo userManagementData) {

		boolean isAuthenticated = userManagementData.isAuthenticationEnabled();
		try {
			Map<Integer, String>	defaultAuthTypes		= getDefaultProperties();
			if(isAuthenticated) {
				boolean					triggerPasswordReset	= false;
				Map<Integer, String>	modifiedAuthTypes		= getModifiedProperties(userManagementData);
				for (Map.Entry<Integer, String> defaultAuthType : defaultAuthTypes.entrySet()) {
					Integer defAuthId = defaultAuthType.getKey();
					if (modifiedAuthTypes.containsKey(defAuthId) == false) {
						String propertyJson = defaultAuthType.getValue();
						//System.out.println("JSON : " + propertyJson);
						authenticationTypeRepository.updatePropertyById(defAuthId, propertyJson);
					} else if (modifiedAuthTypes.containsKey(defAuthId)) {
						mapper = new ObjectMapper().setSerializationInclusion(Include.NON_NULL);
						mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
						ConnectionDetailsJSONSpecification	modConnSpec		= mapper.readValue(
								modifiedAuthTypes.get(defAuthId), new TypeReference<ConnectionDetailsJSONSpecification>() {
																					});
						
						//System.out.println("M-JSON : " + propertyJson);
						if (defAuthId.equals(Constants.AuthType.DAO.getAuthType())) {
							Map<String, Object> authenticationDetails = new HashMap<>();
							userConfigService.getConfigurableDetails(authenticationDetails);
							triggerPasswordReset = checkTotpPwdChange(modConnSpec, authenticationDetails);
							modConnSpec = escapeRegexProperties(modConnSpec);
						}
						String								propertyJson	= mapper.writeValueAsString(modConnSpec);
						//System.out.println("JSON : " + propertyJson);
						authenticationTypeRepository.updatePropertyById(defAuthId, propertyJson);
					}
				}

				if (triggerPasswordReset) {
					resetPwdSendMailForVerificationChange();
				}
				
			} else {
				for (Map.Entry<Integer, String> defaultAuthType : defaultAuthTypes.entrySet()) {
					Integer defAuthId = defaultAuthType.getKey();
					String propertyJson = defaultAuthType.getValue();
					//System.out.println("JSON : " + propertyJson);
					authenticationTypeRepository.updatePropertyById(defAuthId, propertyJson);
				}
				
			}
			List<String> secureEntityIDs = xmlExtractor();

			entityRoleAssociationRepository.toggleAnonymousUserAccess(isAuthenticated ? 0 : 1, secureEntityIDs);

			List<String> secureMasterModuleIDs = xmlExtractorForMasterModule();

			roleModuleRepository.toggleAnonymousUserAccessInMasterModule(isAuthenticated ? 0 : 1,
					secureMasterModuleIDs);
			updateDatabaseAuthPropertyMasterValues(userManagementData);
		} catch (Exception a_exception) {
			logger.error("Error occured in updateAuthProperties.", a_exception);
			return false;
		}
		return true;
	}


	private Map<Integer, String> getModifiedProperties(UserManagementVo userManagementData)
			throws JsonProcessingException {
		List<AuthProperty>		authProperties	= userManagementData.getAuthProperties();
		Map<Integer, String>	modAuthMap		= new HashMap<>();
		for (AuthProperty modAuthProperty : authProperties) {
			List<ConnectionDetailsJSONSpecification> modAuthTypes = modAuthProperty.getAuthTypes();
			for (ConnectionDetailsJSONSpecification jsonSpec : modAuthTypes) {
				mapper = new ObjectMapper().setSerializationInclusion(Include.NON_NULL);
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				AuthTypeByName	authTypeByName	= AuthTypeByName
						.valueOfAt((jsonSpec.getAuthenticationType().getName()));
				Integer			modAuthId		= authTypeByName.getAuthTypeById();
				String			propertyJson	= mapper.writeValueAsString(jsonSpec);
				modAuthMap.put(modAuthId, propertyJson);
			}

		}
		return modAuthMap;
	}

	private void updateJsonSpec(ConnectionDetailsJSONSpecification modConnSpec) {
		modConnSpec.getAuthenticationType().setValue(Constants.TRUE);
	}

	/**
	 * Extracts all the IDs for secure entities taken from XML file into a List of
	 * String
	 * 
	 * @author Anomitro.Mukherjee
	 * 
	 * @return
	 */
	private List<String> xmlExtractor() {
		List<String> l_secureEntityIDs = new ArrayList<String>();
		try {
			/*
			 * SAXParserFactory is a factory API that enables applications to configure and
			 * obtain a SAX based parser to parse XML documents.
			 */
			SAXParserFactory	factory					= SAXParserFactory.newInstance();

			// Creating a new instance of a SAXParser using
			// the currently configured factory parameters.
			SAXParser			saxParser				= factory.newSAXParser();

			// DefaultHandler is Default base class for SAX2
			// event handlers.
			DefaultHandler		l_handler				= new DefaultHandler() {
															boolean l_entityID = false;

															// Receive notification of the start of an
															// element. parser starts parsing a element
															// inside the document
															public void startElement(String uri, String localName,
																	String qName, Attributes attributes)
																	throws SAXException {

																if (qName.equalsIgnoreCase("entityID")) {
																	l_entityID = true;
																}

															}

															// Receive notification of character data
															// inside an element, reads the text value of
															// the currently parsed element
															public void characters(char a_ch[], int a_start,
																	int a_length) throws SAXException {

																if (l_entityID) {
																	l_secureEntityIDs
																			.add(new String(a_ch, a_start, a_length));
																	l_entityID = false;
																}

															}
														};

			/*
			 * Parse the content described by the giving Uniform Resource Identifier (URI)
			 * as XML using the specified DefaultHandler.
			 */
			InputStream			l_secureEntitiesStream	= new ClassPathResource("secureEntitiesXML.xml")
					.getInputStream();
			saxParser.parse(l_secureEntitiesStream, l_handler);

		} catch (Exception exception) {
			logger.error("Error in XML Extractor ", exception);
		}

		return l_secureEntityIDs;
	}

	/**
	 * Extracts all the IDs for secure entities taken from XML file into a List of
	 * String for Master Module as role association with master module exists in a
	 * separate table and we need to secure anonymous access for them after
	 * extracting the module ids
	 * 
	 * @author Anomitro.Mukherjee
	 * 
	 * @return
	 */
	private List<String> xmlExtractorForMasterModule() {
		List<String> l_secureEntityIDs = new ArrayList<String>();
		try {
			/*
			 * SAXParserFactory is a factory API that enables applications to configure and
			 * obtain a SAX based parser to parse XML documents.
			 */
			SAXParserFactory	factory					= SAXParserFactory.newInstance();

			// Creating a new instance of a SAXParser using
			// the currently configured factory parameters.
			SAXParser			saxParser				= factory.newSAXParser();

			// DefaultHandler is Default base class for SAX2
			// event handlers.
			DefaultHandler		l_handler				= new DefaultHandler() {
															boolean l_entityID = false;

															// Receive notification of the start of an
															// element. parser starts parsing a element
															// inside the document
															public void startElement(String uri, String localName,
																	String qName, Attributes attributes)
																	throws SAXException {

																if (qName.equalsIgnoreCase("entityID")) {
																	l_entityID = true;
																}

															}

															// Receive notification of character data
															// inside an element, reads the text value of
															// the currently parsed element
															public void characters(char a_ch[], int a_start,
																	int a_length) throws SAXException {

																if (l_entityID) {
																	l_secureEntityIDs
																			.add(new String(a_ch, a_start, a_length));
																	l_entityID = false;
																}

															}
														};

			/*
			 * Parse the content described by the giving Uniform Resource Identifier (URI)
			 * as XML using the specified DefaultHandler.
			 */
			InputStream			l_secureEntitiesStream	= new ClassPathResource("secureMasterModulesXML.xml")
					.getInputStream();
			saxParser.parse(l_secureEntitiesStream, l_handler);

		} catch (Exception exception) {
			logger.error("Error in Master Module XML Extractor ", exception);
		}

		return l_secureEntityIDs;
	}

	private boolean checkVerificationStep(List<JwsUserLoginVO> multiAuthLoginVOs) {
		try {
			if (multiAuthLoginVOs != null && multiAuthLoginVOs.isEmpty() == false) {
				JwsUserLoginVO multiAuthLoginVO = multiAuthLoginVOs.stream()
						.filter(loginVO -> loginVO.getAuthenticationType().equals(Constants.AuthType.DAO.getAuthType()))
						.findAny().orElse(null);
				if (multiAuthLoginVO != null) {
					Map<String, Object> daoAuthAttributes = multiAuthLoginVO.getLoginAttributes();
					if (daoAuthAttributes != null && daoAuthAttributes.containsKey("enableVerificationStep")) {
						String enableVerificationStepValue = (String) daoAuthAttributes.get("enableVerificationStep");
						if (enableVerificationStepValue.equalsIgnoreCase(Constants.TRUE))
							return true;
						else
							return false;
					}
				}
			}
		} catch (Exception exception) {
			logger.error("Error ", exception);
			return false;
		}
		return false;
	}

	private void resetPwdSendMailForVerificationChange() {
		// when authentication before and after is database and verification type is
		// reset from TOTP to password or password/captcha
		List<JwsUser> jwsUsers = jwsUserRepository.findAll();
		for (JwsUser jwsUser : jwsUsers) {
			if (jwsUser.getIsActive() == Constants.ISACTIVE
					&& jwsUser.getRegisteredBy() == Constants.AuthType.DAO.getAuthType()
					&& !(jwsUser.getUserId().equals(Constants.ADMIN_USER_ID))) {
				try {
					String password = UUID.randomUUID().toString();
					jwsUser.setPassword(passwordEncoder.encode(password));
					jwsUser.setForcePasswordChange(Constants.ISACTIVE);
					jwsUser.setIsActive(Constants.INACTIVE);
					jwsUserRepository.save(jwsUser);
					Email email = new Email();
					sendMailForForcePassword(jwsUser.getUserId(),
					jwsUser.getForcePasswordChange(), jwsUser.getEmail(),
					email, password);
				} catch (Exception exception) {
					logger.error("Error occured in resetPwdSendMailForVerificationChange().", exception);
				}
			}
		}

	}

	private void updateDatabaseAuthPropertyMasterValues(UserManagementVo userManagementData) throws Exception {
		propertyMasterRepository.updatePropertyValueByName(String.valueOf(userManagementData.isAuthenticationEnabled()),
				"enable-user-management");
	}

	private ConnectionDetailsJSONSpecification escapeRegexProperties(
			ConnectionDetailsJSONSpecification connectionSpec) {
		List<List<JwsAuthConfiguration>> jwsAuthConfigurations = connectionSpec.getAuthenticationDetails()
				.getConfigurations();
		for (List<JwsAuthConfiguration> configurations : jwsAuthConfigurations) {
			if (configurations != null) {
				for (JwsAuthConfiguration configuration : configurations) {
					if (configuration != null && configuration.getAdditionalDetails() != null
							&& configuration.getAdditionalDetails().getAdditionalProperties() != null) {
						List<JwsAuthAdditionalProperty> authAdditionalProperties = configuration
								.getAdditionalDetails().getAdditionalProperties();
						if (authAdditionalProperties != null) {
							for (JwsAuthAdditionalProperty additionalProperty : authAdditionalProperties) {
								if (additionalProperty.getName().equalsIgnoreCase("regexPattern")) {
									String regexPattern = StringEscapeUtils.escapeJava(additionalProperty.getValue());
									additionalProperty
									.setValue(regexPattern);
								}
									
							}
						}
					}
				}
			}
		}
		return connectionSpec;
	}

	private boolean checkTotpPwdChange(ConnectionDetailsJSONSpecification connectionSpec,
			Map<String, Object> authenticationDetails) {
		boolean	triggerPasswordReset	= false;
		String	existVerificationType	= (String) authenticationDetails.get("verificationType");
		if (Constants.VerificationType.TOTP.getVerificationType().equals(existVerificationType)) {
			AuthenticationDetails				authentications			= connectionSpec.getAuthenticationDetails();
			List<List<JwsAuthConfiguration>>	configurationDetails	= authentications.getConfigurations();
			for (List<JwsAuthConfiguration> jwsAuthConfigurations : configurationDetails) {
				JwsAuthConfiguration configuration = jwsAuthConfigurations.stream()
						.filter(config -> config.getName().equals("enableVerificationStep")).findAny().orElse(null);
				if (configuration != null && configuration.getValue().equalsIgnoreCase("true")) {
					List<JwsAuthAdditionalProperty> authAdditionalProperties = configuration
							.getAdditionalDetails().getAdditionalProperties();
					if (authAdditionalProperties != null) {
						JwsAuthAdditionalProperty	additionalProperty	= authAdditionalProperties.stream()
								.filter(property -> property.getName().equals("verificationType")).findAny()
								.orElse(null);
						String						newVerificationType	= additionalProperty.getValue();
						if (Constants.VerificationType.PASSWORD.getVerificationType().equals(newVerificationType))
							triggerPasswordReset = true;
					}

				}
			}

		}
		return triggerPasswordReset;
	}

	public void createUserForOtpAuth(JwsUserVO user) throws AddressException, Exception {
		user.setPassword(null);
		user.setIsActive(Constants.ISACTIVE);
		user.setForcePasswordChange(Constants.INACTIVE);
		JwsUser userEntityFromVo = user.convertVOToEntity(user);
		userEntityFromVo.setForcePasswordChange(Constants.INACTIVE);
		userEntityFromVo.setSecretKey(null);
		jwsUserRepository.save(userEntityFromVo);

		// adding role to the user
		JwsUserRoleAssociation userRoleAssociation = new JwsUserRoleAssociation();
		userRoleAssociation.setRoleId(Constants.AUTHENTICATED_ROLE_ID);
		userRoleAssociation.setUserId(userEntityFromVo.getUserId());
		userRoleAssociation.setUpdatedDate(new Date());
		userRoleRepository.save(userRoleAssociation);

		Email email = new Email();
		email.setInternetAddressToArray(InternetAddress.parse(user.getEmail()));

		Map<String, Object> mailDetails = new HashMap<>();
		mailDetails.put("emailId", user.getEmail());
		TemplateVO	subjectTemplateVO	= templatingService.getTemplateByName("otp-user-register-mail-subject");
		String		subject				= templatingUtils.processTemplateContents(subjectTemplateVO.getTemplate(),
				subjectTemplateVO.getTemplateName(), mailDetails);
		email.setSubject(subject);
		TemplateVO	templateVO	= templatingService.getTemplateByName("otp-user-register-confirmation-mail");
		String		mailBody	= templatingUtils.processTemplateContents(templateVO.getTemplate(),
				templateVO.getTemplateName(), mailDetails);
		email.setBody(mailBody);
		//System.out.println(mailBody);
		sendMailService.sendTestMail(email);

	}

	public void createUserForTotpAuth(JwsUserVO user) throws FileNotFoundException, AddressException, Exception {
		user.setPassword(null);
		user.setIsActive(Constants.ISACTIVE);
		JwsUser userEntityFromVo = user.convertVOToEntity(user);
		userEntityFromVo.setForcePasswordChange(Constants.INACTIVE);
		userEntityFromVo.setSecretKey(new TwoFactorGoogleUtil().generateSecretKey());
		jwsUserRepository.save(userEntityFromVo);

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
		String				barcodeData			= twoFactorGoogleUtil
				.getGoogleAuthenticatorBarCode(userEntityFromVo.getEmail(), "Jquiver", userEntityFromVo.getSecretKey());
		twoFactorGoogleUtil.createQRCode(barcodeData, fileOutputStream, height, width);

		Email email = new Email();
		email.setInternetAddressToArray(InternetAddress.parse(user.getEmail()));
		email.setSubject("TOTP Login");
		String	propertyAdminEmailId	= propertyMasterService.findPropertyMasterValue("system", "system",
				"adminEmailId");
		String	adminEmail				= propertyAdminEmailId == null ? "admin@jquiver.io"
				: propertyAdminEmailId.equals("") ? "admin@jquiver.io" : propertyAdminEmailId;
		email.setMailFrom(InternetAddress.parse(adminEmail));
		String fullUrl = getBaseURL(propertyMasterService, servletContext);
		Map<String, Object>	mailDetails	= new HashMap<>();
		mailDetails.put("firstName", user.getFirstName());
		mailDetails.put("baseURL", fullUrl.toString());
		TemplateVO			templateVO	= templatingService.getTemplateByName("totp-qr-mail");
		String				mailBody	= templatingUtils.processTemplateContents(templateVO.getTemplate(),
				templateVO.getTemplateName(), mailDetails);
		email.setBody(mailBody);
		// System.out.println(mailBody);

		List<EmailAttachedFile>	attachedFiles		= new ArrayList<>();
		EmailAttachedFile		emailAttachedFile	= new EmailAttachedFile();
		emailAttachedFile.setFile(file);
		attachedFiles.add(emailAttachedFile);

		CompletableFuture<Boolean> mailSuccess = sendMailService.sendTestMail(email);
		if (mailSuccess.isDone()) {
			email.getAttachementsArray().stream().forEach(f -> f.getFile().delete());
		}
	}

	public void createUserForPasswordAuth(JwsUserVO user) throws AddressException, Exception, CustomStopException {
		try {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setIsActive(Constants.INACTIVE);
			user.setForcePasswordChange(Constants.INACTIVE);
			JwsUser userEntityFromVo = user.convertVOToEntity(user);
			userEntityFromVo.setForcePasswordChange(Constants.INACTIVE);
			userEntityFromVo.setSecretKey(new TwoFactorGoogleUtil().generateSecretKey());
			jwsUserRepository.save(userEntityFromVo);
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
			/* For inserting notification in case of mail failure only on access of Admin */
			email.setIsAuthenticationEnabled(applicationSecurityDetails.getIsAuthenticationEnabled());
			email.setLoggedInUserRole(userDetailsService.getUserDetails().getRoleIdList());
			String baseURL = UserManagementService.getBaseURL(propertyMasterService, servletContext);
			mailDetails.put("baseURL", baseURL);
			mailDetails.put("firstName", userEntityFromVo.getFirstName() + " " + userEntityFromVo.getLastName());
			mailDetails.put("tokenId", confirmationToken.getConfirmationToken());
			TemplateVO	templateVO	= templatingService.getTemplateByName("confirm-account-mail");
			String		mailBody	= templatingUtils.processTemplateContents(templateVO.getTemplate(),
					templateVO.getTemplateName(), mailDetails);
			email.setBody(mailBody);
			// System.out.println(mailBody);
			sendMailService.sendTestMail(email);
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in createUserForPasswordAuth.", custStopException);
			throw custStopException;
		}
	}

	public boolean validateUserRegistration(HttpServletRequest request, JwsUserVO user, Map<String, Object> mapDetails)
			throws JSONException, Exception {
		Map<String, Object> authDetails = new HashMap<>();
		userConfigService.getConfigurableDetails(authDetails);
		List<JwsUserLoginVO>	multiAuthLoginVOs	= (List<JwsUserLoginVO>) authDetails
				.get("activeAutenticationDetails");
		JwsUserLoginVO			daoAuthDetails		= multiAuthLoginVOs.stream()
				.filter(loginVO -> loginVO.getAuthenticationType().equals(Constants.AuthType.DAO.getAuthType()))
				.findAny().orElse(null);

		if (daoAuthDetails == null) {
			mapDetails.put("error", "Authentication not supported.");
			mapDetails.put("errorCode", HttpStatus.NOT_IMPLEMENTED);
			return true;
		}

		if (user != null && (user.getEmail() == null || user.getEmail().isEmpty())) {
			mapDetails.put("error", "Email is requred ");
			mapDetails.put("errorCode", HttpStatus.BAD_REQUEST);
			return true;
		}
		if (mapDetails != null && mapDetails.containsKey("error") == false && user != null
				&& (user.getFirstName() == null || user.getFirstName().isEmpty())) {
			mapDetails.put("error", "First name is required ");
			mapDetails.put("errorCode", HttpStatus.BAD_REQUEST);
			return true;
		}
		if (mapDetails != null && mapDetails.containsKey("error") == false && user != null
				&& (user.getLastName() == null || user.getLastName().isEmpty())) {
			mapDetails.put("error", "Last name is required ");
			mapDetails.put("errorCode", HttpStatus.BAD_REQUEST);
			return true;
		}
		JwsUser existingUser = jwsUserRepository.findByEmailIgnoreCase(user.getEmail());
		if (existingUser != null) {
			mapDetails.put("error", "This email already exists!");
			mapDetails.put("errorCode", HttpStatus.CONFLICT);
			mapDetails.put("firstName", user.getFirstName().trim());
			mapDetails.put("lastName", user.getLastName().trim());
			return true;
		}

		HttpSession session = request.getSession();
		if (mapDetails != null && mapDetails.get("enableCaptcha") != null
				&& mapDetails.get("enableCaptcha").toString().equalsIgnoreCase("true")) {
			if (user.getCaptcha() == null) {
				mapDetails.put("error", "Invalid Captcha!");
				mapDetails.put("errorCode", HttpStatus.BAD_REQUEST);
				mapDetails.put("firstName", user.getFirstName().trim());
				mapDetails.put("lastName", user.getLastName().trim());
				return true;
			}
			if (session.getAttribute("registerCaptcha") == null) {
				mapDetails.put("error", "Captcha is required!");
				mapDetails.put("errorCode", HttpStatus.BAD_REQUEST);
				mapDetails.put("firstName", user.getFirstName().trim());
				mapDetails.put("lastName", user.getLastName().trim());
				return true;
			}

			if (!(user.getCaptcha().equals(session.getAttribute("registerCaptcha").toString()))) {
				mapDetails.put("error", "Please verify captcha!");
				mapDetails.put("firstName", user.getFirstName().trim());
				mapDetails.put("lastName", user.getLastName().trim());
				return true;
			}

		}
		if (mapDetails != null && mapDetails.containsKey("error")) {
			return true;
		}
		return false;

	}

}