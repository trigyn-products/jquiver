package com.trigyn.jws.usermanagement.security.config;

import java.io.Serializable;
import java.util.Set;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;

public class CustomPermissionEvaluator implements PermissionEvaluator {
	
	private ApplicationSecurityDetails applicationSecurityDetails = null;
	
	public CustomPermissionEvaluator(ApplicationSecurityDetails applicationSecurityDetails) {
		this.applicationSecurityDetails = applicationSecurityDetails;
	}

	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
		if(applicationSecurityDetails.getIsAuthenticationEnabled()) {
			UserInformation userDetails = (UserInformation) authentication.getPrincipal();
			Set<String> authorityString = AuthorityUtils.authorityListToSet(userDetails.getAuthorities());
			for (String currentAuthority : authorityString) {
				if (userDetails.getRoleFunctionMap().get(currentAuthority).contains(permission)) {
					return true;
				}
			}
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
			Object permission) {
		// TODO Auto-generated method stub
		return false;
	}

}