package com.trigyn.jws.usermanagement.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.usermanagement.security.config.UserInformation;
import com.trigyn.jws.usermanagement.service.UserManagementService;
import com.trigyn.jws.usermanagement.vo.JwsEntityRoleAssociationVO;
import com.trigyn.jws.usermanagement.vo.JwsMasterModulesVO;
import com.trigyn.jws.usermanagement.vo.JwsRoleMasterModulesAssociationVO;
import com.trigyn.jws.usermanagement.vo.JwsRoleVO;
import com.trigyn.jws.usermanagement.vo.JwsUserVO;

@RestController
@RequestMapping(value = "/cf")
public class JwsUserManagementController {

	@Autowired 
	private MenuService  menuService = null;
	
	
	@Autowired 
	private UserManagementService  userManagementService = null;
	
	
	@GetMapping(value="/um")
	public String userManagement() throws Exception {
		
		return userManagementService.loadUserManagement();
	}
	
	
	@GetMapping(value="/role")
	public String roleListing () throws Exception {
		Map<String, Object> mapDetails = new HashMap<>();
		return menuService.getTemplateWithSiteLayout("role-listing", mapDetails);
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
	public String saveRoleModules(@RequestBody List<JwsRoleMasterModulesAssociationVO> roleModulesList
			, HttpServletResponse httpServletResponse) throws IOException {
		try{
			return userManagementService.saveRoleModules(roleModulesList);
		} catch (Exception exception) {
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
			return null;
		}
	}
	
	
	@GetMapping(value="/user")
	public String userListing () throws Exception {
		
		Map<String, Object> mapDetails = new HashMap<>();
		return menuService.getTemplateWithSiteLayout("jws-user-listing", mapDetails);
	}
	
	@PostMapping(value="/sud")
	public Boolean saveUser(
			@RequestBody JwsUserVO userData) throws Exception {
		
		userManagementService.saveUserData(userData);
		return true;
	}
	
	@PostMapping(value = "/aedu")
	public String addEditUser(@RequestParam("userId") String userId, HttpServletResponse httpServletResponse) throws IOException {
		try{
			return userManagementService.addEditUser(userId);
		} catch (Exception exception) {
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
			return null;
		}
	}
	
	@PostMapping(value="/sat")
	public Boolean saveAuthenticationType(
			@RequestParam("authenticationEnabled") String authenticationEnabled,
			@RequestParam("authenticationTypeId") String authenticationTypeId,
			@RequestParam("propertyJson") String propertyJson) throws Exception {
		
		userManagementService.updatePropertyMasterValuesAndAuthProperties(authenticationEnabled,authenticationTypeId,propertyJson);
		return true;
	}
	
	@GetMapping(value="/profile")
	public String profilePage() throws Exception {
		Map<String, Object> mapDetails = new HashMap<>();
		String  name = SecurityContextHolder.getContext().getAuthentication().getName();
		if(name!=null && !name.equalsIgnoreCase("anonymousUser")) {
			UserInformation userDetails = (UserInformation) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			mapDetails.put("loggedInUser", Boolean.TRUE);
			mapDetails.put("userName", userDetails.getFullName());
			
		}else {
			mapDetails.put("loggedInUser", Boolean.FALSE);
		}
		return menuService.getTemplateWithSiteLayout("my-profile", mapDetails);
		
	}
	
	@GetMapping(value="/mer")
	public String manageEntityRoles() throws Exception {
		
		return userManagementService.manageEntityRoles();
	}
	
	@PostMapping(value="/suer")
	public Boolean saveUpdateEntityRole(
			@RequestBody JwsEntityRoleAssociationVO entityData) throws Exception {
		
		userManagementService.saveUpdateEntityRole(entityData);
		return true;
	}
	
	@GetMapping(value="/modules")
	public List<JwsMasterModulesVO> getModules() throws Exception {
		
		return userManagementService.getModules();
	}
	
}
