package com.trigyn.jws.webstarter.controller;

import java.awt.Dimension;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.devtools.restart.Restarter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.usermanagement.entities.JwsUser;
import com.trigyn.jws.usermanagement.security.config.CaptchaUtil;
import com.trigyn.jws.usermanagement.security.config.UserInformation;
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
	private MenuService  menuService = null;
	
	
	@Autowired 
	private UserManagementService  userManagementService = null;
	
	
	@GetMapping(value="/um")
	public String userManagement() throws Exception {
		
		return userManagementService.loadUserManagement();
	}
	
	
	@GetMapping(value="/rl")
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
			boolean isProfilePage = false;
			return userManagementService.addEditUser(userId,isProfilePage);
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
	    public void restart() {    
	        Restarter.getInstance().restart();

	}
	 	
 	@PostMapping(value="/mp")
	public String managePermissions() throws Exception {
		
 		Map<String, Object> mapDetails = new HashMap<>();
		return menuService.getTemplateWithSiteLayout("manage-permission", mapDetails);
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
}
