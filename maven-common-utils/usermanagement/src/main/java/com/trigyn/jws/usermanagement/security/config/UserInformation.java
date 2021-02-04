package com.trigyn.jws.usermanagement.security.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.trigyn.jws.usermanagement.entities.JwsUser;
import com.trigyn.jws.usermanagement.vo.JwsRoleVO;

public class UserInformation implements UserDetails, OAuth2User, OidcUser {

	private static final long		serialVersionUID	= -1617042842214166605L;

	private String					userName			= null;

	private String					password			= null;

	private boolean					active				= Boolean.FALSE;

	private String					fullName			= null;

	private String					userId				= null;

	private boolean					isDefaultPassword	= Boolean.FALSE;

	private List<GrantedAuthority>	authorities			= new ArrayList<>();

	private List<String>			roles				= new ArrayList<>();

	private Map<String, Object>		oAuthAttributes		= new HashMap<String, Object>();

	public UserInformation() {

	}

	public UserInformation(JwsUser user, List<JwsRoleVO> roleVOs) {

		this.userId				= user.getUserId();
		this.userName			= user.getEmail();
		this.password			= user.getPassword();
		this.fullName			= user.getFirstName() + " " + user.getLastName();
		this.active				= (user.getIsActive() != null) ? (user.getIsActive() == 1 ? true : false) : false;
		this.isDefaultPassword	= (user.getForcePasswordChange() != null)
				? (user.getForcePasswordChange() == 1 ? true : false)
				: false;

		for (JwsRoleVO jwsRoleVO : roleVOs) {
			SimpleGrantedAuthority authority = new SimpleGrantedAuthority(jwsRoleVO.getRoleName());
			roles.add(jwsRoleVO.getRoleName());
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

	public List<String> getRoles() {
		return roles;
	}

	public boolean isDefaultPassword() {
		return isDefaultPassword;
	}

	public void setDefaultPassword(boolean isDefaultPassword) {
		this.isDefaultPassword = isDefaultPassword;
	}

	/// OAuth2User overriden methods
	@Override
	public Map<String, Object> getAttributes() {
		return oAuthAttributes;
	}

	@Override
	public String getName() {
		return fullName;
	}

	public UserInformation create(JwsUser user, Map<String, Object> attributes, List<JwsRoleVO> roleVOs) {

		// oauth user with default credentials
		for (JwsRoleVO jwsRoleVO : roleVOs) {
			SimpleGrantedAuthority authority = new SimpleGrantedAuthority(jwsRoleVO.getRoleName());
			roles.add(jwsRoleVO.getRoleName());
			authorities.add(authority);
		}
		return new UserInformation(user, roleVOs);
	}

	// OidcUser overriden methods

	@Override
	public Map<String, Object> getClaims() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OidcUserInfo getUserInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OidcIdToken getIdToken() {
		// TODO Auto-generated method stub
		return null;
	}

}
