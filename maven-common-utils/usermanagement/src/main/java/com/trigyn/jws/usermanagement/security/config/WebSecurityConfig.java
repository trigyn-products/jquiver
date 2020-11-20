package com.trigyn.jws.usermanagement.security.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.trigyn.jws.usermanagement.repository.JwsUserRepository;
import com.trigyn.jws.usermanagement.repository.JwsUserRoleAssociationRepository;
import com.trigyn.jws.usermanagement.service.UserConfigService;
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
	
	 @Autowired
	 private DataSource dataSource = null;
	
	@Autowired
	private UserConfigService userConfigService = null;
	
	@Bean
	@ConditionalOnMissingBean
	public UserDetailsService userDetailsService(JwsUserRepository userRepository, JwsUserRoleAssociationRepository userRoleAssociationRepository,UserConfigService userConfigService) {
		return new DefaultUserDetailsServiceImpl(userRepository, userRoleAssociationRepository,userConfigService);
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
					.antMatchers("/cf/register","/cf/confirm-account","/cf/captcha/**","/cf/changePassword","/cf/updatePassword","/cf/configureTOTP","/cf/sendConfigureTOTPMail").permitAll()
					.antMatchers("/**").authenticated()
					.and()
					.csrf().disable()
					.formLogin().loginPage("/cf/login").usernameParameter("email").permitAll().successHandler(customAuthSuccessHandler)
					.and()
					.rememberMe().rememberMeParameter("remember-me").tokenRepository(tokenRepository())
					.and()
					.logout().deleteCookies("JSESSIONID").invalidateHttpSession(true).permitAll();
		} else {
			http.authorizeRequests().antMatchers("/**").permitAll()
			.antMatchers("/cf/register","/cf/login").denyAll()
			.and().csrf().disable();
		}

	}

	@Bean
	@ConditionalOnMissingBean
	public PersistentTokenRepository tokenRepository() {
	    JdbcTokenRepositoryImpl jdbcTokenRepositoryImpl=new JdbcTokenRepositoryImpl();
	    jdbcTokenRepositoryImpl.setDataSource(dataSource);
	    return jdbcTokenRepositoryImpl;
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
