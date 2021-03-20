package com.trigyn.jws.usermanagement.security.config;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.trigyn.jws.usermanagement.repository.JwsUserRepository;
import com.trigyn.jws.usermanagement.repository.JwsUserRoleAssociationRepository;
import com.trigyn.jws.usermanagement.security.config.oauth.OAuthDetails;
import com.trigyn.jws.usermanagement.service.UserConfigService;
import com.trigyn.jws.usermanagement.utils.Constants;

@EnableWebSecurity
public class MultiHttpSecurityConfig {

	@Autowired
	private UserDetailsService				userDetailsService			= null;

	@Autowired
	private AuthenticationSuccessHandler	customAuthSuccessHandler	= null;

	@Autowired
	private LogoutHandler					customLogoutSuccessHandler	= null;

	@Autowired
	private ApplicationSecurityDetails		applicationSecurityDetails	= null;

	@Autowired
	private PasswordEncoder					passwordEncoder				= null;

	@Autowired
	private DataSource						dataSource					= null;

	@Autowired
	private OAuthDetails					oAuthDetails				= null;

	private static List<String>				clients						= new ArrayList<>();	// Arrays.asList("google",
																								// "facebook");
	@Autowired
	private JwtRequestFilter				jwtRequestFilter			= null;

	@Autowired
	private ServletContext					servletContext				= null;

	// @Bean
	// public AuthenticationManager authenticationManagerBean() throws Exception {
	// return super.authenticationManagerBean();
	// }

	@Bean
	@ConditionalOnMissingBean
	public UserDetailsService userDetailsService(JwsUserRepository userRepository,
			JwsUserRoleAssociationRepository userRoleAssociationRepository, UserConfigService userConfigService) {
		return new DefaultUserDetailsServiceImpl(userRepository, userRoleAssociationRepository, userConfigService);
	}

	@Bean
	@ConditionalOnMissingBean
	public AuthenticationSuccessHandler authenticationSuccessHandler() {
		return new CustomAuthSuccessHandler();
	}

	@Bean
	public CustomLoginFailureHandler loginFailureHandler() {
		return new CustomLoginFailureHandler();
	}

	@Bean
	@ConditionalOnMissingBean
	public LogoutHandler logoutHandler() {
		return new CustomLogoutSuccessHandler();
	}

	@Configuration
	@Order(1)
	public class ApiSecurityAdapter extends WebSecurityConfigurerAdapter {

		@Autowired
		private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint = null;

		@Bean
		public AuthenticationManager authenticationManagerBean() throws Exception {
			return super.authenticationManagerBean();
		}

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.antMatcher("/japi/**") // <= Security only available for /japi/**
					.authorizeRequests().antMatchers("/japi/register", "/japi/login", "/japi/error").permitAll()
					.anyRequest().authenticated().and().csrf().disable().exceptionHandling()
					.authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
					.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class).sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		}
	}

	@Configuration
	public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			String authenticationType = applicationSecurityDetails.getAuthenticationType();
			// auth.parentAuthenticationManager(null);
			if (authenticationType == null) {
				auth.inMemoryAuthentication().withUser("root@trigyn.com").password(passwordEncoder.encode("root"))
						.roles("ADMIN");
			} else {
				Integer authType = Integer.parseInt(authenticationType);
				if (Constants.AuthType.INMEMORY.getAuthType() == authType) {
					auth.inMemoryAuthentication().withUser("root@trigyn.com").password(passwordEncoder.encode("root"))
							.roles("ADMIN");
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
				String	authenticationType	= applicationSecurityDetails.getAuthenticationType();
				Integer	authType			= Integer.parseInt(authenticationType);
				if (Constants.AuthType.INMEMORY.getAuthType() == authType) {
					// TODO :
				} else if (Constants.AuthType.DAO.getAuthType() == authType) {
					http.authorizeRequests().antMatchers("/webjars/**").permitAll().antMatchers("/").permitAll()
							.antMatchers("/cf/createPassword", "/cf/sendResetPasswordMail", "/cf/resetPasswordPage",
									"/cf/sendResetPasswordMail", "/cf/resetPassword", "/cf/authenticate")
							.permitAll()
							.antMatchers("/cf/register", "/cf/confirm-account", "/cf/captcha/**", "/cf/changePassword",
									"/cf/updatePassword", "/cf/configureTOTP", "/cf/sendConfigureTOTPMail", "/cf/**",
									"/", "/view/**")
							.permitAll().and().csrf().disable().formLogin().loginPage("/cf/login")
							.usernameParameter("email").permitAll().failureHandler(loginFailureHandler())
							.successHandler(customAuthSuccessHandler).and().rememberMe()
							.rememberMeParameter("remember-me").tokenRepository(tokenRepository()).and().logout()
							.addLogoutHandler(customLogoutSuccessHandler).deleteCookies("JSESSIONID")
							.invalidateHttpSession(true);
					// .and().sessionManagement().maximumSessions(1).sessionRegistry(sessionRegistry());

				} else if (Constants.AuthType.LDAP.getAuthType() == authType) {
					// TODO :
				} else if (Constants.AuthType.OAUTH.getAuthType() == authType) {

					http.authorizeRequests()
							.antMatchers("/cf/createPassword", "/cf/sendResetPasswordMail", "/cf/resetPasswordPage",
									"/cf/sendResetPasswordMail", "/cf/resetPassword")
							.denyAll()
							.antMatchers("/cf/register", "/cf/confirm-account", "/cf/captcha/**", "/cf/changePassword",
									"/cf/updatePassword", "/cf/configureTOTP", "/cf/sendConfigureTOTPMail")
							.denyAll().antMatchers("/webjars/**").permitAll().antMatchers("/login/**", "/logout/**")
							.permitAll().and().oauth2Login().loginPage("/cf/login").permitAll()
							.failureHandler(loginFailureHandler()).successHandler(customAuthSuccessHandler).and()
							.logout().addLogoutHandler(customLogoutSuccessHandler).and().csrf().disable();
				}
			} else {
				http.authorizeRequests()
						.antMatchers("/cf/createPassword", "/cf/sendResetPasswordMail", "/cf/resetPasswordPage",
								"/cf/sendResetPasswordMail", "/cf/resetPassword", "/cf/login")
						.denyAll()
						.antMatchers("/cf/register", "/cf/confirm-account", "/cf/captcha/**", "/cf/changePassword",
								"/cf/updatePassword", "/cf/configureTOTP", "/cf/sendConfigureTOTPMail")
						.denyAll().antMatchers("/cf/**", "/view/**", "/").permitAll().and().csrf().disable();
			}

		}
	}

	@Bean
	public PersistentTokenRepository tokenRepository() {
		JdbcTokenRepositoryImpl jdbcTokenRepositoryImpl = new JdbcTokenRepositoryImpl();
		jdbcTokenRepositoryImpl.setDataSource(dataSource);
		return jdbcTokenRepositoryImpl;
	}

	@Bean
	public ClientRegistrationRepository clientRegistrationRepository() {
		List<ClientRegistration> registrations = new ArrayList<ClientRegistration>();
		if (oAuthDetails != null) {
			clients.add(oAuthDetails.getOAuthClient());
			registrations = clients.stream().map(this::getRegistration).filter(registration -> registration != null)
					.collect(Collectors.toList());
		}

		return new InMemoryClientRegistrationRepository(registrations);
	}

	private ClientRegistration getRegistration(String client) {
		String	clientId		= oAuthDetails.getOAuthClientId();
		String	clientSecret	= oAuthDetails.getOAuthClientSecret();

		if (client.equals("google")) {
			return CommonOAuth2Provider.GOOGLE.getBuilder(client).clientId(clientId).clientSecret(clientSecret).build();
		} else if (client.equals("facebook")) {
			return CommonOAuth2Provider.FACEBOOK.getBuilder(client).clientId(clientId).clientSecret(clientSecret)
					.build();
		} else if (client.equals("github")) {
			return CommonOAuth2Provider.GITHUB.getBuilder(client).clientId(clientId).clientSecret(clientSecret).build();
		} else if (client.equals("office365")) {

			String redirectUri = String.format("%s%s/login/oauth2/code/office365",
					applicationSecurityDetails.getBaseUrl(), servletContext.getContextPath());
			return ClientRegistration.withRegistrationId("office365").clientId(clientId).clientSecret(clientSecret)
					.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE).scope("openid")
					.redirectUriTemplate(redirectUri)

					.authorizationUri("https://login.microsoftonline.com/common/oauth2/authorize")
					.tokenUri("https://login.microsoftonline.com/common/oauth2/token")
					.jwkSetUri("https://login.microsoftonline.com/common/discovery/keys").build();
		}
		return null;

	}

}
