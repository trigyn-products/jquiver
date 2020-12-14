package com.trigyn.jws.webstarter.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.devtools.restart.Restarter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.entities.ModuleListing;
import com.trigyn.jws.dbutils.spi.PropertyMasterDetails;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.usermanagement.entities.JwsEntityRoleAssociation;
import com.trigyn.jws.usermanagement.entities.JwsRole;
import com.trigyn.jws.usermanagement.entities.JwsUser;
import com.trigyn.jws.usermanagement.security.config.ApplicationSecurityDetails;
import com.trigyn.jws.usermanagement.service.JwsUserService;
import com.trigyn.jws.usermanagement.vo.JwsEntityRoleAssociationVO;
import com.trigyn.jws.usermanagement.vo.JwsEntityRoleVO;
import com.trigyn.jws.usermanagement.vo.JwsMasterModulesVO;
import com.trigyn.jws.usermanagement.vo.JwsRoleMasterModulesAssociationVO;
import com.trigyn.jws.usermanagement.vo.JwsRoleVO;
import com.trigyn.jws.usermanagement.vo.JwsUserVO;
import com.trigyn.jws.webstarter.service.UserManagementService;

@RestController
@RequestMapping(value = "/cf")
public class JwsUserManagementController {

	@Autowired 
	private MenuService  menuService 						= 	null;
	
	@Autowired
	private PropertyMasterDetails propertyMasterDetails 	=	null;
	
	@Autowired 
	private UserManagementService  userManagementService 	= 	null;
	
	@Autowired
    private ApplicationSecurityDetails applicationSecurityDetails = null;
	
	@Autowired
	private JwsUserService jwsUserService = null;
	
	@Autowired
	private IUserDetailsService userDetailsService = null;
	
	
	@GetMapping(value="/um")
	public String userManagement() throws Exception {
		
		return userManagementService.loadUserManagement();
	}
	
	
	@GetMapping(value="/rl")
	public String roleListing (HttpServletResponse response) throws Exception {
		if(userManagementService.checkAuthenticationEnabled()) {	
			Map<String, Object> mapDetails = new HashMap<>();
			return menuService.getTemplateWithSiteLayout("role-listing", mapDetails);
	 	} else {
			response.sendError(HttpStatus.FORBIDDEN.value(), "You dont have rights to access these module");
			return null;
		}
		
	}
	
	@PostMapping(value="/srd")
	public Boolean saveRole(
			@RequestBody JwsRoleVO roleData) throws Exception {
		
		userManagementService.saveRoleData(roleData);
		return true;
	}
	
	@PostMapping(value = "/aedr")
	public String addEditRole(@RequestParam("roleId") String roleId, HttpServletResponse httpServletResponse) throws IOException {
		try{
			return userManagementService.addEditRole(roleId);
		} catch (Exception exception) {
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
			return null;
		}
	}
	
	@GetMapping(value="/mrm")
	public String manageRoleModules() throws Exception {
		
		return userManagementService.manageRoleModules();
	}
	
	@PostMapping(value = "/srm")
	public Boolean saveRoleModules(@RequestBody JwsRoleMasterModulesAssociationVO roleModule
			, HttpServletResponse httpServletResponse) throws IOException {
		try{
			return userManagementService.saveRoleModules(roleModule);
		} catch (Exception exception) {
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
			return null;
		}
	}
	
	
	@GetMapping(value="/ul")
	public String userListing (HttpServletResponse response) throws Exception {
		if(userManagementService.checkAuthenticationEnabled()) {	
			Map<String, Object> mapDetails = new HashMap<>();
			String templateName = userManagementService.getPropertyValueByAuthName("user-profile-template-details", "templateName");
			UserDetailsVO detailsVO = userDetailsService.getUserDetails();
			mapDetails.put("userDetails", detailsVO);
			if(StringUtils.isNotBlank(templateName)) {
				mapDetails.put("isProfilePage", false);
				return menuService.getTemplateWithSiteLayout(templateName, mapDetails);
			}
			
			return menuService.getTemplateWithSiteLayout("jws-user-listing", mapDetails);
	 	} else {
			response.sendError(HttpStatus.FORBIDDEN.value(), "You dont have rights to access these module");
			return null;
		}
	}
	
	@PostMapping(value="/sud")
	public Boolean saveUser(
			@RequestBody JwsUserVO userData) throws Exception {
		
		userManagementService.saveUserData(userData);
		return true;
	}
	
	@PostMapping(value="/surap")
	public Boolean saveUserRolesAndPolicies(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
		String userDataJson = httpServletRequest.getParameter("userData");
		Gson gson = new Gson();
		JwsUserVO userData = gson.fromJson(userDataJson, JwsUserVO.class);
		Integer isEdit = httpServletRequest.getParameter("isEdit") == null? 0 
				: Integer.parseInt(httpServletRequest.getParameter("isEdit")); 
		userManagementService.saveUserRolesAndPolicies(isEdit, userData);
		return true;
	}
	
	
	@PostMapping(value = "/aedu")
	public String addEditUser(@RequestParam("userId") String userId, HttpServletResponse httpServletResponse) throws IOException {
		try{
			boolean isProfilePage = false;
			return userManagementService.addEditUser(userId,isProfilePage);
		} catch (Exception exception) {
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
			return null;
		}
	}
	
	private Map<String, Object> processRequestParams(HttpServletRequest httpServletRequest) {
        Map<String, Object> requestParams = new HashMap<>();
        for (String requestParamKey : httpServletRequest.getParameterMap().keySet()) {
        	if(Boolean.FALSE.equals("formId".equalsIgnoreCase(requestParamKey))) {
        		requestParams.put(requestParamKey, httpServletRequest.getParameter(requestParamKey));
        	}
        }
        return requestParams;
    }
	
	@PostMapping(value = "/mpar")
	public String manageUserRoleAndPermission(@RequestParam("userId") String userId
			, HttpServletResponse httpServletResponse) throws IOException {
		try{
			return userManagementService.manageUserRoleAndPermission(userId);
		} catch (Exception exception) {
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
			return null;
		}
	}
	
	@PostMapping(value="/sat")
	public Boolean saveAuthenticationType(
			@RequestParam("authenticationEnabled") String authenticationEnabled,
			@RequestParam("authenticationTypeId") String authenticationTypeId,
			@RequestParam("propertyJson") String propertyJson,
			@RequestParam("regexObj") String regexObj,
			@RequestParam("userProfileForm") String userProfileForm,
			@RequestParam("userProfileTemplate") String userProfileTemplate) throws Exception {
		
		userManagementService.updatePropertyMasterValuesAndAuthProperties(authenticationEnabled,authenticationTypeId
				,propertyJson,regexObj, userProfileForm, userProfileTemplate);
		propertyMasterDetails.resetPropertyMasterDetails();
		applicationSecurityDetails.resetApplicationSecurityDetails();
		return true;
	}
	
	@GetMapping(value="/profile")
	public String profilePage() throws Exception {
		
		return	userManagementService.getProfilePage();
	}
	
	@GetMapping(value="/mer")
	public String manageEntityRoles() throws Exception {
		
		return userManagementService.manageEntityRoles();
	}
	
	@PostMapping(value="/suer")
	public Boolean saveUpdateEntityRole(
			@RequestBody List<JwsEntityRoleAssociationVO> entityDataList) throws Exception {
		
		userManagementService.saveUpdateEntityRole(entityDataList);
		return true;
	}
	
	@GetMapping(value="/modules")
	public List<JwsMasterModulesVO> getModules() throws Exception {
		
		return userManagementService.getModules();
	}
	
	 	@GetMapping(value = "/restart")
	    public String restart()  {
	 		
	 		UserDetailsVO  userVO =  userDetailsService.getUserDetails();
	 		new Thread(() ->    {
	 			try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	 			 Restarter.getInstance().restart();
	 		}) .start();
	 		
	 		if(userVO.getUserId().equalsIgnoreCase("anonymous-user")) {
	 			return "false";
	 		}
	 		
	 		
	 		return "true";

	}
	 	
 	@GetMapping(value="/mp")
	public String managePermissions(HttpServletResponse response) throws Exception {
		if(userManagementService.checkAuthenticationEnabled()) {	
	 		Map<String, Object> mapDetails = new HashMap<>();
			return menuService.getTemplateWithSiteLayout("manage-permission", mapDetails);
	 	} else {
			response.sendError(HttpStatus.FORBIDDEN.value(), "You dont have rights to access these module");
			return null;
		}
	} 	
 	
 	@PostMapping(value="/ser")
	public Boolean saveEntityRole(
			@RequestBody JwsEntityRoleVO entityRoles) throws Exception {
		
		userManagementService.deleteAndSaveEntityRole(entityRoles);
		return true;
	}
	
 	@GetMapping(value="/ler", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<JwsRoleVO> getEntityRole(
			@RequestParam("entityId") String entityId,
			@RequestParam("moduleId") String moduleId) throws Exception {
		
		return userManagementService.getEntityRoles(entityId,moduleId);
	}
 	
 	@GetMapping(value="/cee")
	public boolean checkEmailExist(HttpServletRequest request,HttpServletResponse response) throws Throwable {
 		
 		boolean emailExist = true; 
 		
		String email = request.getParameter("email");
		JwsUser existingUser = userManagementService.findByEmailIgnoreCase(email);
		if(existingUser == null) {
			emailExist = false;
		}	
		return emailExist;
	}
 	
 	
	@GetMapping(value="/gif", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getInputFieldsByProperty(HttpServletRequest request) throws Exception {
		
		String propertyName = request.getParameter("inputId");
		String propertyValue =  userManagementService.getInputFieldsByProperty(propertyName);
		if(StringUtils.isBlank(propertyValue)) {
			propertyValue = "{}";
		}
		return propertyValue;
	}

	@PostMapping(value = "/puj")
	public String getLastUpdatedPermissionJsonData(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse) throws Exception{
		String entityId = a_httpServletRequest.getParameter("entityId");
		return userManagementService.getJwsEntityRoleAssociationJson(entityId);
	}

	@PostMapping(value = "/sjra")
	public void savePermission(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse) throws Exception{
		String modifiedContent 				= a_httpServletRequest.getParameter("modifiedContent");
		ObjectMapper objectMapper			= new ObjectMapper();
		JwsEntityRoleAssociation jwsRoleAssoc		= objectMapper.readValue(modifiedContent, JwsEntityRoleAssociation.class);
		userManagementService.saveJwsEntityRoleAssociation(jwsRoleAssoc);
	}
	
	@GetMapping(value = "/cae")
	public boolean checkAuthenticationEnabled(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse) throws Exception{
		return userManagementService.checkAuthenticationEnabled();
	}

	@PostMapping(value = "/mjwsu")
	public String getLastUpdatedJwsUser(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse) throws Exception{
		String entityId = a_httpServletRequest.getParameter("entityId");
		return userManagementService.getJwsUserJson(entityId);
	}

	@PostMapping(value = "/sjwsu")
	public void saveJwsUser(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse) throws Exception{
		String modifiedContent 				= a_httpServletRequest.getParameter("modifiedContent");
		ObjectMapper objectMapper			= new ObjectMapper();
		JwsUser user		= objectMapper.readValue(modifiedContent, JwsUser.class);
		userManagementService.saveJwsUser(user);
	}

	@PostMapping(value = "/mjwsr")
	public String getLastUpdatedJwsRole(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse) throws Exception{
		String entityId = a_httpServletRequest.getParameter("entityId");
		return userManagementService.getJwsRoleJson(entityId);
	}

	@PostMapping(value = "/sjwsr")
	public void saveJwsRole(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse) throws Exception{
		String modifiedContent 				= a_httpServletRequest.getParameter("modifiedContent");
		ObjectMapper objectMapper			= new ObjectMapper();
		JwsRole role		= objectMapper.readValue(modifiedContent, JwsRole.class);
		userManagementService.saveJwsRole(role);
	}
}
