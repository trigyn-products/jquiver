package com.trigyn.jws.usermanagement.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.trigyn.jws.usermanagement.repository.JwsUserRepository;
import com.trigyn.jws.usermanagement.repository.JwsUserRoleAssociationRepository;
import com.trigyn.jws.usermanagement.utils.Constants;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService = null;

	@Autowired
	private AuthenticationSuccessHandler customAuthSuccessHandler = null;
	
	@Autowired
	private ApplicationSecurityDetails applicationSecurityDetails = null;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(10);
	}
	
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
			auth.inMemoryAuthentication().withUser("root@trigyn.com").password(passwordEncoder().encode("root")).roles("ADMIN");
		} else {
			if(Constants.AuthType.INMEMORY.getAuthType().equals(authenticationType)) {
				auth.inMemoryAuthentication().withUser("root@trigyn.com").password(passwordEncoder().encode("root")).roles("ADMIN");
			} else if (Constants.AuthType.DAO.getAuthType().equals(authenticationType)) {
				auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
			} else if (Constants.AuthType.LDAP.getAuthType().equals(authenticationType)) {
				// TODO : 
			} else if (Constants.AuthType.OAUTH.getAuthType().equals(authenticationType)) {
				// TODO : 
			}
		}
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		if (applicationSecurityDetails.getIsAuthenticationEnabled()) {
			http.authorizeRequests().antMatchers("/webjars/**").permitAll().antMatchers("/**")
					.authenticated().antMatchers("/").permitAll().antMatchers("/cf/register").permitAll()
					.antMatchers("/cf/confirm-account").permitAll().and().csrf().disable().formLogin().loginPage("/cf/login")
					.usernameParameter("email").permitAll().successHandler(customAuthSuccessHandler).and().logout()
					.deleteCookies("JSESSIONID").invalidateHttpSession(true).permitAll();
		} else {
			http.authorizeRequests().antMatchers("/**").permitAll().and().csrf().disable();
		}

	}

}
