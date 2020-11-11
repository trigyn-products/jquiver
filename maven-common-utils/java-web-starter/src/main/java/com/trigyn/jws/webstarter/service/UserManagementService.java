package com.trigyn.jws.webstarter.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.mail.internet.InternetAddress;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.dbutils.entities.PropertyMaster;
import com.trigyn.jws.dbutils.repository.PropertyMasterRepository;
import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.templating.service.DBTemplatingService;
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.templating.utils.TemplatingUtils;
import com.trigyn.jws.templating.vo.TemplateVO;
import com.trigyn.jws.usermanagement.entities.JwsAuthenticationType;
import com.trigyn.jws.usermanagement.entities.JwsEntityRoleAssociation;
import com.trigyn.jws.usermanagement.entities.JwsMasterModules;
import com.trigyn.jws.usermanagement.entities.JwsRole;
import com.trigyn.jws.usermanagement.entities.JwsRoleMasterModulesAssociation;
import com.trigyn.jws.usermanagement.entities.JwsUser;
import com.trigyn.jws.usermanagement.entities.JwsUserRoleAssociation;
import com.trigyn.jws.usermanagement.repository.JwsAuthenticationTypeRepository;
import com.trigyn.jws.usermanagement.repository.JwsEntityRoleAssociationRepository;
import com.trigyn.jws.usermanagement.repository.JwsMasterModulesRepository;
import com.trigyn.jws.usermanagement.repository.JwsRoleMasterModulesAssociationRepository;
import com.trigyn.jws.usermanagement.repository.JwsRoleRepository;
import com.trigyn.jws.usermanagement.repository.JwsUserRepository;
import com.trigyn.jws.usermanagement.repository.JwsUserRoleAssociationRepository;
import com.trigyn.jws.usermanagement.repository.UserManagementDAO;
import com.trigyn.jws.usermanagement.security.config.ApplicationSecurityDetails;
import com.trigyn.jws.usermanagement.security.config.UserInformation;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.usermanagement.vo.JwsAuthenticationTypeVO;
import com.trigyn.jws.usermanagement.vo.JwsEntityRoleAssociationVO;
import com.trigyn.jws.usermanagement.vo.JwsEntityRoleVO;
import com.trigyn.jws.usermanagement.vo.JwsMasterModulesVO;
import com.trigyn.jws.usermanagement.vo.JwsRoleMasterModulesAssociationVO;
import com.trigyn.jws.usermanagement.vo.JwsRoleVO;
import com.trigyn.jws.usermanagement.vo.JwsUserVO;
import com.trigyn.jws.webstarter.utils.Email;

@Service
@Transactional
public class UserManagementService {

	@Autowired
	private JwsRoleRepository jwsRoleRepository = null;

	@Autowired
	private JwsMasterModulesRepository jwsmasterModuleRepository = null;

	@Autowired
	private JwsRoleMasterModulesAssociationRepository roleModuleRepository = null;

	@Autowired
	private MenuService menuService = null;

	@Autowired
	private JwsUserRepository jwsUserRepository = null;

	@Autowired
	private UserManagementDAO userManagementDAO = null;

	@Autowired
	private JwsUserRoleAssociationRepository userRoleRepository = null;
	
	@Autowired
	private PropertyMasterRepository propertyMasterRepository = null;
	
	@Autowired
	private JwsAuthenticationTypeRepository authenticationTypeRepository = null;
	
	@Autowired
	private JwsEntityRoleAssociationRepository entityRoleAssociationRepository = null;
	
    @Autowired
    private DBTemplatingService templatingService     = null;

	@Autowired
	private TemplatingUtils templatingUtils = null; 
	
	@Autowired
	private IUserDetailsService userDetailsService = null;
	
	@Autowired
	private PasswordEncoder passwordEncoder = null;
	
	@Autowired
	private ApplicationSecurityDetails applicationSecurityDetails = null;
	
	@Autowired
	private PropertyMasterService propertyMasterService	= null;
	
	@Autowired
	private SendMailService sendMailService = null;
	

	public String addEditRole(String roleId) throws Exception {

		Map<String, Object> templateMap = new HashMap<>();
		JwsRole jwsRole = new JwsRole();
		if (StringUtils.isNotEmpty(roleId)) {

			jwsRole = jwsRoleRepository.findById(roleId).get();
		}
		templateMap.put("jwsRole", jwsRole);
		return menuService.getTemplateWithSiteLayout("addEditRole", templateMap);
	}

	public void saveRoleData(JwsRoleVO roleData) {
		JwsRole jwsRole = roleData.convertVOToEntity(roleData);
		jwsRoleRepository.save(jwsRole);
		
		// add role to entity table
		
		if(Boolean.FALSE.equals(StringUtils.isNotEmpty(roleData.getRoleId())) && jwsRole.getIsActive() == Constants.ISACTIVE) {
			List<JwsEntityRoleAssociation> entityRolesAssociations = entityRoleAssociationRepository.findEntityByModuleTypeId(Constants.COMMON_MODULE_TYPE_ID);
			for (JwsEntityRoleAssociation currentJwsEntityRole : entityRolesAssociations) {
				JwsEntityRoleAssociation jwsEntityRoleAssociation = new JwsEntityRoleAssociation();
				jwsEntityRoleAssociation.setEntityName(currentJwsEntityRole.getEntityName());
				jwsEntityRoleAssociation.setEntityId(currentJwsEntityRole.getEntityId());
				jwsEntityRoleAssociation.setIsActive(Constants.ISACTIVE);
				jwsEntityRoleAssociation.setModuleId(currentJwsEntityRole.getModuleId());
				jwsEntityRoleAssociation.setLastUpdatedBy(userDetailsService.getUserDetails().getUserId());
				jwsEntityRoleAssociation.setLastUpdatedDate(new Date());
				jwsEntityRoleAssociation.setModuleTypeId(Constants.COMMON_MODULE_TYPE_ID);
				jwsEntityRoleAssociation.setRoleId(jwsRole.getRoleId());
				entityRoleAssociationRepository.save(jwsEntityRoleAssociation);
			}
		}

	}

	public String manageRoleModules() throws Exception {

		Map<String, Object> mapDetails = new HashMap<>();

		List<JwsRole> roles = new ArrayList<>();
		roles = jwsRoleRepository.findAllRoles();

		List<JwsMasterModulesVO> masterModulesVO = new ArrayList<>();
		List<JwsMasterModules> masterModules = new ArrayList<>();
		masterModules = jwsmasterModuleRepository.findAll();
		
		for (JwsMasterModules jwsMasterModule : masterModules) {
			masterModulesVO.add(new JwsMasterModulesVO().convertEntityToVO(jwsMasterModule));
		}

		List<JwsRoleMasterModulesAssociation> roleModulesAssociations = new ArrayList<>();
		roleModulesAssociations = roleModuleRepository.findAll();

		mapDetails.put("roles", roles);
		mapDetails.put("masterModules", masterModulesVO);
		mapDetails.put("roleModulesAssociations", roleModulesAssociations);
		
		TemplateVO templateVO =  templatingService.getTemplateByName("manageRoleModule");
		return	templatingUtils.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(), mapDetails);
	}

	public Boolean saveRoleModules(JwsRoleMasterModulesAssociationVO roleModule) {

			JwsRoleMasterModulesAssociation masterModuleAssociation = roleModule
					.convertVOToEntity(roleModule);

			roleModuleRepository.save(masterModuleAssociation);
			
			JwsMasterModules  jwsMasterModule = jwsmasterModuleRepository.findById(masterModuleAssociation.getModuleId()).get();
			
			// set isactive  by moduletype id
			entityRoleAssociationRepository.updateEntityRelatedToModule(jwsMasterModule.getModuleTypeId(),
					masterModuleAssociation.getRoleId(),masterModuleAssociation.getIsActive());

		return true;
	}

	public void saveUserData(JwsUserVO userData) throws Exception {
		String password = null;
		JwsUser jwsUser = new JwsUser();
		
		if(userData.getIsProfilePage()) {
			jwsUser = jwsUserRepository.findByUserId(userData.getUserId());
			jwsUser.setFirstName(userData.getFirstName());
			jwsUser.setLastName(userData.getLastName());
			jwsUserRepository.save(jwsUser);
		}
		else {
			if(StringUtils.isNotEmpty(userData.getUserId())){
				jwsUser = jwsUserRepository.findByUserId(userData.getUserId());
				jwsUser.setFirstName(userData.getFirstName());
				jwsUser.setLastName(userData.getLastName());
				userManagementDAO.deleteUserRoleAssociation(jwsUser.getUserId());
			}else {
				jwsUser.setFirstName(userData.getFirstName());
				jwsUser.setLastName(userData.getLastName());
				jwsUser.setEmail(userData.getEmail());
				password = UUID.randomUUID().toString();
				jwsUser.setPassword(passwordEncoder.encode(password));
				System.out.println("Your account password is  "+ password) ;
				Email email = new Email();
				email.setInternetAddressToArray(InternetAddress.parse(userData.getEmail()));  
				email.setSubject("Account Password");  
				email.setMailFrom("admin@jquiver.com");
				email.setBody("Your account password is  "+ password+" You can login through these url : http://localhost:8080/cf/login");
		  		
				sendMailService.sendTestMail(email);
			}
			jwsUser.setIsActive(userData.getForcePasswordChange()==1?Constants.INACTIVE:Constants.ISACTIVE);
			jwsUser.setForcePasswordChange(userData.getForcePasswordChange());// force password change
			jwsUserRepository.save(jwsUser);
			if(userData.getForcePasswordChange()==1) {
				password = UUID.randomUUID().toString();
				System.out.println("Password is :" +  password);
				jwsUser.setPassword(passwordEncoder.encode(password));
		  		StringBuilder messageText = new StringBuilder("Your account password is  "+ password) ; 
		  		if(userData.getForcePasswordChange()==1) {
		  			messageText.append(" Please change your password through these url : http://localhost:8080/cf/changePassword?token=" +jwsUser.getUserId());
		  		}else {
		  			messageText.append(" You can login through these url : http://localhost:8080/cf/login");
		  		}
		  		System.out.println(messageText.toString());
				Email email = new Email();
				email.setInternetAddressToArray(InternetAddress.parse(userData.getEmail()));  
				email.setSubject("Account Password");  
				email.setMailFrom("admin@jquiver.com");
				email.setBody(messageText.toString());
		  		
				sendMailService.sendTestMail(email);
			}
			
			for (String roleId : userData.getRoleIds()) {
				Date currentDate = new Date();
				JwsUserRoleAssociation userRoleAssociation = new JwsUserRoleAssociation();
				userRoleAssociation.setRoleId(roleId);
				userRoleAssociation.setUserId(jwsUser.getUserId());
				userRoleAssociation.setUpdatedDate(currentDate);
				userRoleRepository.save(userRoleAssociation);
			}
		}	
	}

	public String addEditUser(String userId, boolean isProfilePage) throws Exception {

		Map<String, Object> templateMap = new HashMap<>();
		JwsUser jwsUser = new JwsUser();
		List<String> userRoleIds = new ArrayList<>();
		if (StringUtils.isNotEmpty(userId)) {

			jwsUser = jwsUserRepository.findById(userId).get();
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
		return menuService.getTemplateWithSiteLayout("addEditJwsUser", templateMap);
	}


	public String loadUserManagement() throws Exception {
		Map<String, Object> mapDetails = new HashMap<>();
		PropertyMaster propertyMaster = propertyMasterRepository.findByOwnerTypeAndOwnerIdAndPropertyName("system", "system", "enable-user-management");
		mapDetails.put("authEnabled", Boolean.parseBoolean(propertyMaster.getPropertyValue()));
		PropertyMaster propertyMasterAuthType = propertyMasterRepository.findByOwnerTypeAndOwnerIdAndPropertyName("system", "system", "authentication-type");
		mapDetails.put("authTypeId", propertyMasterAuthType.getPropertyValue());
		
		List<JwsAuthenticationType> authenticationTypes = authenticationTypeRepository.findAll();
		List<JwsAuthenticationTypeVO> authenticationTypesVO = new ArrayList<>();
		
		for (JwsAuthenticationType authenticationType : authenticationTypes) {
			authenticationTypesVO.add(new JwsAuthenticationTypeVO().convertEntityToVO(authenticationType));
		}
		mapDetails.put("authenticationTypesVO", authenticationTypesVO);
		
		return menuService.getTemplateWithSiteLayout("user-management", mapDetails);
		
	}

	public void updatePropertyMasterValuesAndAuthProperties(String authenticationEnabled, String authenticationTypeId,
			String propertyJson,String regexObj) {
		
		propertyMasterRepository.updatePropertyValueByName(authenticationEnabled, "enable-user-management");
		propertyMasterRepository.updatePropertyValueByName(authenticationTypeId, "authentication-type");
		
		authenticationTypeRepository.updatePropertyById(Integer.parseInt(authenticationTypeId),propertyJson);
		if(StringUtils.isNotBlank(regexObj)) {
			propertyMasterRepository.updatePropertyValueByName(regexObj, "regexPattern");
		}
	}

	public String manageEntityRoles() throws Exception {
		Map<String, Object> mapDetails = new HashMap<>();
		List<JwsRole> roles = new ArrayList<>();
		roles = jwsRoleRepository.findAllRoles();
		mapDetails.put("roles", roles);
		
		TemplateVO templateVO =  templatingService.getTemplateByName("manageEntityRoles");
		return	templatingUtils.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(), mapDetails);
	}

	public void saveUpdateEntityRole(List<JwsEntityRoleAssociationVO> entityRoleAssociations) {
		
		for (JwsEntityRoleAssociationVO jwsEntityRoleAssociationVO : entityRoleAssociations) {
			
			JwsEntityRoleAssociation jwsEntityRoleAssociation = jwsEntityRoleAssociationVO.convertVOtoEntity(jwsEntityRoleAssociationVO);
			jwsEntityRoleAssociation.setLastUpdatedBy("admin");
			String entityRoleId = entityRoleAssociationRepository.getEntityRoleIdByEntityAndRoleId(jwsEntityRoleAssociation.getEntityId(),jwsEntityRoleAssociation.getRoleId());
			jwsEntityRoleAssociation.setEntityRoleId(entityRoleId);
			entityRoleAssociationRepository.save(jwsEntityRoleAssociation);
		}
	}

	public List<JwsMasterModulesVO> getModules() {
		
		List<JwsMasterModulesVO> masterModulesVO = new ArrayList<>();
		List<JwsMasterModules> masterModules = new ArrayList<>();
		masterModules = jwsmasterModuleRepository.findAll();
		
		for (JwsMasterModules jwsMasterModule : masterModules) {
			masterModulesVO.add(new JwsMasterModulesVO().convertEntityToVO(jwsMasterModule));
		}
		
		return masterModulesVO;
	}

	public void deleteAndSaveEntityRole(JwsEntityRoleVO entityRoles) {
		
		
		if(!entityRoles.getRoleIds().contains(Constants.ADMIN_ROLE_ID)) {
			entityRoles.getRoleIds().add(Constants.ADMIN_ROLE_ID);
			entityRoles.setRoleIds(entityRoles.getRoleIds());
		}
		
		List<String> newRoleIds = new ArrayList<>(entityRoles.getRoleIds());
		
		List<JwsEntityRoleAssociation> entityRoleAssociations =  entityRoleAssociationRepository.getEntityRoles(entityRoles.getEntityId(),entityRoles.getModuleId());
		for (JwsEntityRoleAssociation jwsEntityRoleAssociation : entityRoleAssociations) {
			//inactive
			if(!entityRoles.getRoleIds().contains(jwsEntityRoleAssociation.getRoleId()))
			{
				newRoleIds.remove(jwsEntityRoleAssociation.getRoleId());
				jwsEntityRoleAssociation.setIsActive(Constants.INACTIVE);
				jwsEntityRoleAssociation.setLastUpdatedDate(new Date());
				jwsEntityRoleAssociation.setLastUpdatedBy(userDetailsService.getUserDetails().getUserId());
				entityRoleAssociationRepository.save(jwsEntityRoleAssociation);
			}else {
				newRoleIds.remove(jwsEntityRoleAssociation.getRoleId());
				jwsEntityRoleAssociation.setIsActive(Constants.ISACTIVE);
				jwsEntityRoleAssociation.setLastUpdatedDate(new Date());
				jwsEntityRoleAssociation.setLastUpdatedBy(userDetailsService.getUserDetails().getUserId());
				entityRoleAssociationRepository.save(jwsEntityRoleAssociation);
			}
		}
		
		for (String roleId : newRoleIds) {
			JwsEntityRoleAssociation entityRoleAssociation = new JwsEntityRoleAssociation();
			entityRoleAssociation.setRoleId(roleId);
			entityRoleAssociation.setEntityId(entityRoles.getEntityId());
			entityRoleAssociation.setEntityName(entityRoles.getEntityName());
			entityRoleAssociation.setModuleId(entityRoles.getModuleId());
			entityRoleAssociation.setIsActive(Constants.ISACTIVE);
			entityRoleAssociation.setModuleTypeId(Constants.DEFAULT_MODULE_TYPE_ID);
			entityRoleAssociation.setLastUpdatedDate(new Date());
			entityRoleAssociation.setLastUpdatedBy(userDetailsService.getUserDetails().getUserId());
			entityRoleAssociationRepository.save(entityRoleAssociation);
			
		}
		
	}

	public List<JwsRoleVO> getEntityRoles(String entityId, String moduleId) {
		List<JwsRoleVO> roles =  entityRoleAssociationRepository.getRoles(entityId,moduleId,Constants.ISACTIVE);
		return roles;
	}

	
	 public Boolean validatePassword(String password) throws Exception {
		 Boolean isValid = Boolean.FALSE;
		 	if(password == null || (password !=null && password.trim().isEmpty())) {
		 		return isValid;
		 	}else {
		 		Integer authType = Integer.parseInt(applicationSecurityDetails.getAuthenticationType());
		 		if(Constants.AuthType.DAO.getAuthType() == authType) {
				 		
		 				JwsAuthenticationType authenticationType =authenticationTypeRepository.findById(authType)
		 						.orElseThrow(() -> new Exception("No auth type found with id : " + authType));
		 				
		 				
		 				JSONObject jsonObject = null ;
		 				JSONArray jsonArray = new JSONArray(authenticationType.getAuthenticationProperties());
		 				String propertyName = "enableRegex";
		 				jsonObject = getJsonObjectFromPropertyValue(jsonObject, jsonArray, propertyName);
		 				
		 				if(jsonObject!= null && jsonObject.getString("value").equalsIgnoreCase("true")){
		 					
		 					String regex =  propertyMasterService.findPropertyMasterValue("system", "system", "regexPattern"); 
		 					JSONObject jsonObjectRegex = new JSONObject(regex);
		 					Pattern pattern = Pattern.compile(jsonObjectRegex.getString("regexValue")); 
		 					Matcher isMatches = pattern.matcher(password); 
		 					return isMatches.matches();
		 				}
		 				isValid = Boolean.TRUE;
		 		}
		 	}
		 	return isValid;
	}
	
	 private JSONObject getJsonObjectFromPropertyValue(JSONObject jsonObject, JSONArray jsonArray, String propertyName)
				throws JSONException {
			for (int i = 0; i < jsonArray.length(); i++) {
				 jsonObject = jsonArray.getJSONObject(i);
				if(jsonObject.get("name").toString().equalsIgnoreCase(propertyName)) {
					break;
				}else {
					jsonObject = null ;
				}
			}
			return jsonObject;
		}
	 

		public void getConfigurableDetails(Map<String, Object> mapDetails) throws Exception, JSONException {
			JSONObject jsonObjectCaptcha = null ;
			JSONObject jsonObjectRegex = null ;
			Integer authType = Integer.parseInt(applicationSecurityDetails.getAuthenticationType());
			JwsAuthenticationType authenticationType = authenticationTypeRepository.findById(authType)
					.orElseThrow(() -> new Exception("No auth type found with id : " + authType));
			JSONArray jsonArray = new JSONArray(authenticationType.getAuthenticationProperties());
			String captchaPropertyName = "enableCaptcha";
			jsonObjectCaptcha = getJsonObjectFromPropertyValue(jsonObjectCaptcha, jsonArray, captchaPropertyName);
			if (jsonObjectCaptcha != null && jsonObjectCaptcha.getString("value").equalsIgnoreCase("true")) {
				mapDetails.put(captchaPropertyName,Boolean.TRUE);
			}else {
				mapDetails.put(captchaPropertyName,Boolean.FALSE);
			}
			String regexPropertyName = "enableRegex";
			jsonObjectRegex = getJsonObjectFromPropertyValue(jsonObjectCaptcha, jsonArray, regexPropertyName);
			if(jsonObjectRegex!= null && jsonObjectRegex.getString("value").equalsIgnoreCase("true")){
				mapDetails.put(regexPropertyName,Boolean.TRUE);
				String propertyMasterRegex =  propertyMasterService.findPropertyMasterValue("system", "system", "regexPattern"); 
				JSONObject jsonPropertyMasterRegex = new JSONObject(propertyMasterRegex);
				mapDetails.put("regexExample",jsonPropertyMasterRegex.getString("regexExample"));
			}else {
				mapDetails.put(regexPropertyName,Boolean.FALSE);
			}
			
			String enableRegistrationPropertyName = "enableRegistration";
			
			if(applicationSecurityDetails.getAuthenticationType()!=null){
			
			JSONObject jsonObject = null ;
			jsonObject = getJsonObjectFromPropertyValue(jsonObject, jsonArray, enableRegistrationPropertyName);
			if(jsonObject!= null && jsonObject.getString("value").equalsIgnoreCase("true")){
				mapDetails.put(enableRegistrationPropertyName,Boolean.TRUE);
			}else {
				mapDetails.put(enableRegistrationPropertyName,Boolean.FALSE);
			}
			
			}
		}

		public JwsUser findByEmailIgnoreCase(String email) {
			return jwsUserRepository.findByEmailIgnoreCase(email);
		}

		public String getProfilePage() throws Exception {
			
			boolean isProfilePage = true;
			UserInformation userDetails = (UserInformation) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String userId = userDetails.getUserId();
			return addEditUser(userId,isProfilePage);
			
		}

		public String getInputFieldsByProperty(String propertyName) throws Exception {
			String propertyMasterRegex =  propertyMasterService.findPropertyMasterValue("system", "system", propertyName);
			return propertyMasterRegex;
		}
	 
}
