package com.trigyn.jws.usermanagement.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.trigyn.jws.usermanagement.repository.JwsUserRepository;
import com.trigyn.jws.usermanagement.repository.JwsUserRoleAssociationRepository;
import com.trigyn.jws.usermanagement.utils.Constants;

@Configuration
@Order(1)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService = null;

	@Autowired
	private AuthenticationSuccessHandler customAuthSuccessHandler = null;
	
	@Autowired
	private ApplicationSecurityDetails applicationSecurityDetails = null;
	
	@Autowired
	private PasswordEncoder passwordEncoder = null;
	
	@Bean
	@ConditionalOnMissingBean
	public UserDetailsService userDetailsService(JwsUserRepository userRepository, JwsUserRoleAssociationRepository userRoleAssociationRepository) {
		return new DefaultUserDetailsServiceImpl(userRepository, userRoleAssociationRepository);
	}
	
	@Bean
	@ConditionalOnMissingBean
	public AuthenticationSuccessHandler authenticationSuccessHandler() {
		return new CustomAuthSuccessHandler();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		String authenticationType = applicationSecurityDetails.getAuthenticationType();
		if(authenticationType == null) {
			auth.inMemoryAuthentication().withUser("root@trigyn.com").password(passwordEncoder.encode("root")).roles("ADMIN");
		} else {
			Integer authType = Integer.parseInt(authenticationType);
			if(Constants.AuthType.INMEMORY.getAuthType() == authType) {
				auth.inMemoryAuthentication().withUser("root@trigyn.com").password(passwordEncoder.encode("root")).roles("ADMIN");
			} else if (Constants.AuthType.DAO.getAuthType() == authType) {
				auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
			} else if (Constants.AuthType.LDAP.getAuthType() == authType) {
				// TODO : 
			} else if (Constants.AuthType.OAUTH.getAuthType() == authType) {
				// TODO : 
			}
		}
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		if (applicationSecurityDetails.getIsAuthenticationEnabled()) {
			http.authorizeRequests().antMatchers("/webjars/**").permitAll()
					.antMatchers("/").permitAll()
					.antMatchers("/cf/createPassword","/cf/sendResetPasswordMail","/cf/resetPasswordPage","/cf/sendResetPasswordMail","/cf/resetPassword").permitAll()
					.antMatchers("/cf/register","/cf/confirm-account").permitAll()
					.antMatchers("/**").authenticated()
					.and()
					.csrf().disable()
					.formLogin().loginPage("/cf/login").usernameParameter("email").permitAll().successHandler(customAuthSuccessHandler)
					.and()
					.logout().deleteCookies("JSESSIONID").invalidateHttpSession(true).permitAll();
		} else {
			http.authorizeRequests().antMatchers("/**").permitAll().and().csrf().disable();
		}

	}

	public UserDetailsService getUserDetailsService() {
		return userDetailsService;
	}

	public AuthenticationSuccessHandler getCustomAuthSuccessHandler() {
		return customAuthSuccessHandler;
	}

	public ApplicationSecurityDetails getApplicationSecurityDetails() {
		return applicationSecurityDetails;
	}
	
}
