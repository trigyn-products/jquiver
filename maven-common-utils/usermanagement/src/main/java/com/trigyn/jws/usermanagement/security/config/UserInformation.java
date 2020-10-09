package com.trigyn.jws.usermanagement.security.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.trigyn.jws.usermanagement.entities.JwsRoleMasterModulesAssociation;
import com.trigyn.jws.usermanagement.entities.JwsUser;
import com.trigyn.jws.usermanagement.entities.JwsUserRoleAssociation;

public class UserInformation implements UserDetails {

	private static final long serialVersionUID = -1617042842214166605L;

	private String userName = null;

	private String password = null;

	private boolean active = Boolean.FALSE;

	private String fullName = null;

	private String userId = null;

	private List<GrantedAuthority> authorities = new ArrayList<>();

	private Map<String, List<String>> roleFunctionMap = new HashMap<>();

	public UserInformation(JwsUser user, List<JwsUserRoleAssociation> roleAssociation) {
		
		this.userId =  user.getUserId();
		this.userName = user.getEmail();
		this.password = user.getPassword();
		this.fullName = user.getFirstName() +" " +user.getLastName(); 
		this.active = user.getIsActive() == 1? true:false;
		
		for (JwsUserRoleAssociation  currentRole : roleAssociation) {
			String roleName = currentRole.getJwsRole().getRoleName();
			if(roleFunctionMap.containsKey(roleName)) {
				continue;
			}
			SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleName);
			List<String> functionNames = new ArrayList<>();	
			for (JwsRoleMasterModulesAssociation currentModule : currentRole.getJwsRole().getJwsRoleMasterModulesAssociation()) {
				functionNames.add(currentModule.getModule().getModuleName());
			}
			roleFunctionMap.put(roleName,functionNames);
			authorities.add(authority);
		}

	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return active;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Map<String, List<String>> getRoleFunctionMap() {
		return roleFunctionMap;
	}

}
