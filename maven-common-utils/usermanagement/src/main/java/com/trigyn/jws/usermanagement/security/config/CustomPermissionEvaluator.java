package com.trigyn.jws.usermanagement.security.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.trigyn.jws.usermanagement.repository.JwsRoleMasterModulesAssociationRepository;
import com.trigyn.jws.usermanagement.utils.Constants;

public class CustomPermissionEvaluator implements PermissionEvaluator {
	
	private ApplicationSecurityDetails applicationSecurityDetails = null;
	
	@Autowired
	private JwsRoleMasterModulesAssociationRepository roleModuleRepository = null;  
	
	public CustomPermissionEvaluator(ApplicationSecurityDetails applicationSecurityDetails) {
		this.applicationSecurityDetails = applicationSecurityDetails;
	}

	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
		List<String> roleNames = new ArrayList<>();
		
		if(applicationSecurityDetails.getIsAuthenticationEnabled() && !(authentication instanceof AnonymousAuthenticationToken)) {
			UserInformation userDetails = (UserInformation) authentication.getPrincipal();
			roleNames = userDetails.getRoles();
		
		} else {
			roleNames.add(Constants.ANONYMOUS_ROLE_NAME);
		}
		
		Long count = roleModuleRepository.checkModulePresentForCurrentRole(roleNames,permission.toString(),Constants.ISACTIVE);
		if(count > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
			Object permission) {
		// TODO Auto-generated method stub
		return false;
	}

}