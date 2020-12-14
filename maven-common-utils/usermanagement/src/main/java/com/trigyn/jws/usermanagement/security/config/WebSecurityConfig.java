package com.trigyn.jws.usermanagement.security.config;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.trigyn.jws.usermanagement.repository.JwsUserRepository;
import com.trigyn.jws.usermanagement.repository.JwsUserRoleAssociationRepository;
import com.trigyn.jws.usermanagement.security.config.oauth.OAuthDetails;
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
	private OAuthDetails oAuthDetails = null;
	
	private  static List<String> clients = new ArrayList<>(); //Arrays.asList("google", "facebook");
	
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
			String authenticationType = applicationSecurityDetails.getAuthenticationType();
			Integer authType = Integer.parseInt(authenticationType);
			if(Constants.AuthType.INMEMORY.getAuthType() == authType) {
				// TODO : 
			} 
			else if (Constants.AuthType.DAO.getAuthType() == authType) {
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
			} else if (Constants.AuthType.LDAP.getAuthType() == authType) {
				// TODO : 
			} else if (Constants.AuthType.OAUTH.getAuthType() == authType) {
				
				http.authorizeRequests()
					.antMatchers("/cf/createPassword","/cf/sendResetPasswordMail","/cf/resetPasswordPage","/cf/sendResetPasswordMail","/cf/resetPassword").denyAll()
					.antMatchers("/cf/register","/cf/confirm-account","/cf/captcha/**","/cf/changePassword","/cf/updatePassword","/cf/configureTOTP","/cf/sendConfigureTOTPMail").denyAll()
					.antMatchers("/webjars/**").permitAll()
					.antMatchers("/login/**","/logout/**").permitAll()
					.antMatchers("/**").authenticated()
					.and().oauth2Login()
					.loginPage("/cf/login").permitAll()
					.successHandler(customAuthSuccessHandler)
					.and().csrf().disable();
			}
		}else {
			http.authorizeRequests()
				.antMatchers("/cf/createPassword","/cf/sendResetPasswordMail","/cf/resetPasswordPage","/cf/sendResetPasswordMail","/cf/resetPassword","/cf/login").denyAll()
				.antMatchers("/cf/register","/cf/confirm-account","/cf/captcha/**","/cf/changePassword","/cf/updatePassword","/cf/configureTOTP","/cf/sendConfigureTOTPMail").denyAll()
				.antMatchers("/**").permitAll()
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
	
	@Bean
	public ClientRegistrationRepository clientRegistrationRepository() {
		clients.add(oAuthDetails.getOAuthClient());
		List<ClientRegistration> registrations = clients.stream().map(this::getRegistration)
				.filter(registration -> registration != null).collect(Collectors.toList());
		
		return new InMemoryClientRegistrationRepository(registrations);
	}
	
	private ClientRegistration getRegistration(String client) {
		String clientId = oAuthDetails.getOAuthClientId();
		String clientSecret = oAuthDetails.getOAuthClientSecret();

		if (client.equals("google")) {
			return CommonOAuth2Provider.GOOGLE.getBuilder(client).clientId(clientId).clientSecret(clientSecret).build();
		}else if (client.equals("facebook")) {
			return CommonOAuth2Provider.FACEBOOK.getBuilder(client).clientId(clientId).clientSecret(clientSecret)
					.build();
		}else if (client.equals("github")) {
			return CommonOAuth2Provider.GITHUB.getBuilder(client).clientId(clientId).clientSecret(clientSecret)
					.build();
		}else if(client.equals("office365")){
			
			
			String redirectUri = String.format("%s/login/oauth2/code/office365",applicationSecurityDetails.getBaseUrl());  
			return ClientRegistration.withRegistrationId("office365")
					.clientId(clientId)
					.clientSecret(clientSecret)
					.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
					.scope("openid")
					.redirectUriTemplate(redirectUri)
					
					.authorizationUri("https://login.microsoftonline.com/common/oauth2/authorize")
					.tokenUri("https://login.microsoftonline.com/common/oauth2/token")
					.jwkSetUri("https://login.microsoftonline.com/common/discovery/keys")
					.build();
		}
		return null;
		
	}
	 
}
