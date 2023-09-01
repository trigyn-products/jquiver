package com.trigyn.jws.usermanagement.security.config;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.stereotype.Component;

@Component
public class LdapUserAuthoritiesPopulator implements LdapAuthoritiesPopulator {

	private final Log			logger				= LogFactory.getLog(getClass());

	@Lazy
	@Autowired
	private UserDetailsService	userDetailsService	= null;

	@Override
	public Collection<? extends GrantedAuthority> getGrantedAuthorities(DirContextOperations userData,
			String username) {
		Collection<? extends GrantedAuthority> authorities = new ArrayList<>();
		try {
			authorities = userDetailsService.loadUserByUsername(username).getAuthorities();
			logger.info("LdapUserAuthoritiesPopulator --> " + authorities);
		} catch (Exception exec) {
			logger.error(
					"Exception occurred while trying to fetch the user authorities from the database - User might not belong to local users");
		}
		return authorities;

	}

}
