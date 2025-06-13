package com.trigyn.jws.usermanagement.security.config;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import com.trigyn.jws.usermanagement.repository.JwsRoleMasterModulesAssociationRepository;
import com.trigyn.jws.usermanagement.repository.JwsUserRepository;
import com.trigyn.jws.usermanagement.repository.JwsUserRoleAssociationRepository;
import com.trigyn.jws.usermanagement.security.config.oauth.CustomOAuth2UserService;
import com.trigyn.jws.usermanagement.security.config.oauth.CustomOidcUserService;
import com.trigyn.jws.usermanagement.security.config.oauth.CustomSAMLUserService;
import com.trigyn.jws.usermanagement.security.config.oauth.OAuth2HelperService;
import com.trigyn.jws.usermanagement.service.UserConfigService;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.usermanagement.vo.JwsAuthenticationType;
import com.trigyn.jws.usermanagement.vo.MultiAuthSecurityDetailsVO;
import com.trigyn.jws.webstarter.utils.JQuiverProperties;

@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class MultiHttpSecurityConfig {

	private final static Logger							logger									= LoggerFactory
			.getLogger(MultiHttpSecurityConfig.class);

	@Autowired
	@Lazy
	private UserDetailsService							userDetailsService						= null;

	// @Autowired
	private LogoutHandler								customLogoutSuccessHandler				= null;

	@Autowired
	private ApplicationSecurityDetails					applicationSecurityDetails				= null;

	@Autowired
	private DataSource									dataSource								= null;

	@Autowired
	private OAuth2HelperService							oAuth2HelperService						= null;

	@Autowired
	private CustomOidcUserService						customOidcUserService					= null;

	@Autowired
	private CustomOAuth2UserService						customOAuth2UserService					= null;

	@Autowired
	private CustomOpenSamlAuthenticationProvider		customOpenSamlAuthenticationProvider	= null;

	@Autowired
	private JwsRoleMasterModulesAssociationRepository	roleModuleRepository					= null;

	@Autowired
	private CustomSAMLUserService						customSAMLUserService					= null;

	@Autowired
	private JQuiverProperties							jQuiverPropeties						= null;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(customOpenSamlAuthenticationProvider);
	}

	@Bean
	public DefaultMethodSecurityExpressionHandler expressionHandler() {
		DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
		expressionHandler.setPermissionEvaluator(
				new CustomPermissionEvaluator(applicationSecurityDetails, roleModuleRepository));
		return expressionHandler;
	}

	@Bean
	@ConditionalOnMissingBean
	public PermissionEvaluator permissionEvaluator(ApplicationSecurityDetails applicationSecurityDetails) {
		return new CustomPermissionEvaluator(applicationSecurityDetails, roleModuleRepository);
	}

	@Bean
	@ConditionalOnMissingBean
	CustomAuthenticationProvider customAuthenticationProvider() {
		return new CustomAuthenticationProvider();
	}

	@Bean
	UserDetailsService userDetailsService(JwsUserRepository userRepository,
			JwsUserRoleAssociationRepository userRoleAssociationRepository, UserConfigService userConfigService) {
		return new DefaultUserDetailsServiceImpl(userRepository, userRoleAssociationRepository, userConfigService);
	}

	@Bean
	AuthenticationSuccessHandler customAuthSuccessHandler() {
		return new CustomAuthSuccessHandler();
	}

	@Bean
	CustomLoginFailureHandler loginFailureHandler() {
		return new CustomLoginFailureHandler();
	}

	@Bean
	public LogoutHandler logoutHandler() {
		customLogoutSuccessHandler = new CustomLogoutSuccessHandler();
		return customLogoutSuccessHandler;
	}

	@Configuration
	@Order(1)
	public class WebSecurityConfig {

		@Bean
		public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration)
				throws Exception {
			return authConfiguration.getAuthenticationManager();
		}

		@Bean
		public AuthenticationManager authManager(HttpSecurity http) throws Exception {
			AuthenticationManagerBuilder	authenticationManagerBuilder	= http
					.getSharedObject(AuthenticationManagerBuilder.class);

			Map<String, Object>				authenticationDetails			= applicationSecurityDetails
					.getAuthenticationDetails();
			if (authenticationDetails != null) {
				List<MultiAuthSecurityDetailsVO> multiAuthSecurityDetails = (List<MultiAuthSecurityDetailsVO>) authenticationDetails
						.get("authenticationDetails");
				if (multiAuthSecurityDetails != null && multiAuthSecurityDetails.isEmpty() == false) {
					for (MultiAuthSecurityDetailsVO multiAuthLogin : multiAuthSecurityDetails) {
						Integer authType = multiAuthLogin.getAuthenticationTypeVO().getId();
						if (authenticationDetails != null) {
							if ((Constants.AuthType.DAO.getAuthType() == authType
									|| Constants.AuthType.LDAP.getAuthType() == authType)) {
								authenticationManagerBuilder.authenticationProvider(customAuthenticationProvider());
							}
						}
					}
				}
			}

			return authenticationManagerBuilder.build();

		}

		@Bean
		public SecurityFilterChain filterChain(HttpSecurity http, SecurityContextRepository securityContextRepository)
				throws Exception, JSONException {

			CustomAuthSuccessHandler.addLoginListener(new LoginSuccessEventListenerRestImpl());
			Map<String, Object> authenticationDetails = applicationSecurityDetails.getAuthenticationDetails();
			if (authenticationDetails != null) {
				@SuppressWarnings("unchecked")
				List<MultiAuthSecurityDetailsVO> multiAuthLogiVos = (List<MultiAuthSecurityDetailsVO>) authenticationDetails
						.get("authenticationDetails");
				if (multiAuthLogiVos.isEmpty() == false && applicationSecurityDetails.getIsAuthenticationEnabled()) {
					for (MultiAuthSecurityDetailsVO multiAuthLogin : multiAuthLogiVos) {
						if (multiAuthLogin != null) {
							Integer					authType				= multiAuthLogin.getAuthenticationTypeVO()
									.getId();
							JwsAuthenticationType	authVerificationType	= multiAuthLogin.getConnectionDetailsVO()
									.getAuthenticationType();
							if (authVerificationType != null && authVerificationType.getValue() != null) {
								Boolean enableVerification = Boolean.parseBoolean(authVerificationType.getValue());
								if (enableVerification) {
									if (Constants.AuthType.INMEMORY.getAuthType() == authType) {
										// TODO :
									}
									if (Constants.AuthType.DAO.getAuthType() == authType) {
										http.cors(Customizer.withDefaults()).authorizeHttpRequests(auth -> auth
												.requestMatchers("/webjars/**").permitAll().requestMatchers("/")
												.permitAll()
												.requestMatchers("/cf/createPassword", "/cf/sendResetPasswordMail",
														"/cf/resetPasswordPage", "/cf/sendResetPasswordMail",
														"/cf/resetPassword", "/cf/authenticate",
														"/cf/saveOtpAndSendMail", "/cf/getResourceBundleData",
														"/cf/hmfn", "/cf/search", "/cf/grid-data", "/cf/pq-grid-data",
														"cf/hmfc", "/error")
												.permitAll()
												.requestMatchers("/cf/register", "/cf/confirm-account",
														"/cf/captcha/**", "/cf/changePassword", "/cf/updatePassword",
														"/cf/configureTOTP", "/cf/sendConfigureTOTPMail", "/japi/**")
												.permitAll().requestMatchers("/login/**", "/logout/**").permitAll()
												.requestMatchers("/sch-api/**", "/japi/**").permitAll()
												.requestMatchers(jQuiverPropeties.getApiPath() + "/**", "/cf/files/**",
														jQuiverPropeties.getViewPath() + "/**", "/cf/gl", "/cf/psdf",
														"/cf/getResourceBundleData", "/cf/hmfn", "/cf/search",
														"cf/hmfc", "/error")
												.permitAll().requestMatchers("/webjars/**").permitAll()
												.requestMatchers("/cf/**", jQuiverPropeties.getApiPath() + "/**",
														"/japi/**", "/**", jQuiverPropeties.getMonitoringPath())
												.authenticated()).csrf(csrf -> csrf.disable())
												.formLogin(formLogin -> formLogin.loginPage("/cf/login").permitAll()
														.loginProcessingUrl("/cf/login").permitAll()
														.usernameParameter("email").permitAll()
														.failureHandler(loginFailureHandler())
														.successHandler(customAuthSuccessHandler()))
												.rememberMe(rememberMe -> rememberMe.rememberMeParameter("remember-me")
														.tokenRepository(tokenRepository()))
												.authenticationProvider(customAuthenticationProvider())
												.securityContext(secCon -> secCon
														.securityContextRepository(securityContextRepository))
												.logout(logout -> logout.addLogoutHandler(logoutHandler())
														.deleteCookies("JSESSIONID"));
									}
									if (Constants.AuthType.SAML.getAuthType() == authType) {
										http.cors(Customizer.withDefaults()).authorizeHttpRequests(auth -> auth
												.requestMatchers("/webjars/**").permitAll().requestMatchers("/")
												.permitAll().requestMatchers("/cf/confirm-account").denyAll()
												.requestMatchers("/webjars/**").permitAll()
												.requestMatchers("/login/**", "/logout/**").permitAll()
												.requestMatchers("/sch-api/**", "/japi/**").permitAll()
												.requestMatchers("/cf/files/**", jQuiverPropeties.getViewPath() + "/**",
														"/cf/gl", "/cf/psdf", "/cf/getResourceBundleData", "/cf/hmfn",
														"/cf/search", "/cf/grid-data", "/cf/pq-grid-data", "cf/hmfc",
														"/error", jQuiverPropeties.getMonitoringPath())
												.permitAll().requestMatchers("/cf/**", "/japi/**", "/**")
												.authenticated()).saml2Login(saml2 -> {
													try {
														saml2.relyingPartyRegistrationRepository(customSAMLUserService
																.relyingPartyRegistrationRepository())
																.authenticationManager(new ProviderManager(
																		customOpenSamlAuthenticationProvider));
													} catch (Exception exc) {
														logger.error("Error occured in SAML Authentication.", exc);
													}
												})
												.securityContext(secCon -> secCon
														.securityContextRepository(securityContextRepository))
												.logout(logout -> logout.addLogoutHandler(logoutHandler())
														.deleteCookies("JSESSIONID"))
												.csrf(csrf -> csrf.disable());
									}
									if (Constants.AuthType.LDAP.getAuthType() == authType) {
										http.cors(Customizer.withDefaults()).authorizeHttpRequests(auth -> auth
												.requestMatchers("/webjars/**").permitAll().requestMatchers("/")
												.permitAll()
												.requestMatchers("/cf/createPassword", "/cf/sendResetPasswordMail",
														"/cf/resetPasswordPage", "/cf/sendResetPasswordMail",
														"/cf/resetPassword", "/cf/authenticate",
														"/cf/saveOtpAndSendMail", "/cf/getResourceBundleData",
														"/cf/hmfn", "/cf/search", "/cf/grid-data", "/cf/pq-grid-data",
														"cf/hmfc", "/error")
												.permitAll()
												.requestMatchers(jQuiverPropeties.getApiPath() + "/**", "/cf/register",
														"/cf/confirm-account", "/cf/captcha/**", "/cf/changePassword",
														"/cf/updatePassword", "/cf/configureTOTP",
														"/cf/sendConfigureTOTPMail", "/japi/**")
												.permitAll().requestMatchers("/login/**", "/logout/**").permitAll()
												.requestMatchers("/sch-api/**", "/japi/**").permitAll()
												.requestMatchers("/cf/files/**", jQuiverPropeties.getViewPath() + "/**",
														"/cf/gl", "/cf/psdf", "/cf/getResourceBundleData", "/cf/hmfn",
														"/cf/search", "cf/hmfc", "/error")
												.permitAll()
												.requestMatchers("/cf/**", jQuiverPropeties.getApiPath() + "/**",
														"/japi/**", "/**", jQuiverPropeties.getMonitoringPath())
												.authenticated()).csrf(csrf -> csrf.disable())
												.formLogin(formLogin -> formLogin.loginPage("/cf/login")
														.usernameParameter("email").permitAll()
														.failureHandler(loginFailureHandler())
														.successHandler(customAuthSuccessHandler()))
												.rememberMe(rememberMe -> rememberMe.rememberMeParameter("remember-me")
														.tokenRepository(tokenRepository()))
												.securityContext(secCon -> secCon
														.securityContextRepository(securityContextRepository))
												.logout(logout -> logout.addLogoutHandler(logoutHandler())
														.deleteCookies("JSESSIONID"));

									}

									if (Constants.AuthType.OAUTH.getAuthType() == authType) {
										http.cors(Customizer.withDefaults()).authorizeHttpRequests(auth -> auth
												.requestMatchers("/webjars/**").permitAll().requestMatchers("/")
												.permitAll().requestMatchers("/cf/confirm-account").denyAll()
												.requestMatchers("/login/**", "/logout/**").permitAll()
												.requestMatchers("/sch-api/**", "/japi/**").permitAll()
												.requestMatchers("/cf/files/**", jQuiverPropeties.getViewPath() + "/**",
														"/cf/gl", "/cf/psdf", "/cf/getResourceBundleData", "/cf/hmfn",
														"/cf/search", "/cf/grid-data", "/cf/pq-grid-data", "cf/hmfc",
														"/error")
												.permitAll()
												.requestMatchers("/cf/**", "/japi/**", "/**",
														jQuiverPropeties.getMonitoringPath())
												.authenticated())
												.oauth2Login(oauth2 -> oauth2
														.clientRegistrationRepository(
																oAuth2HelperService.clientRegistrationRepository())
														.loginPage("/cf/login").permitAll()
														.failureHandler(loginFailureHandler())
														.successHandler(customAuthSuccessHandler())
														// .authorizationEndpoint(authEndPoint ->
														// authEndPoint.authorizationEndpoint())
														// Below Changes of for Linkedin
														.tokenEndpoint(token -> token
																.accessTokenResponseClient(oAuth2HelperService
																		.authorizationCodeTokenResponseClient()))
														// Below Changes of for OIDC
														.userInfoEndpoint(userInfo -> userInfo
																// .userAuthoritiesMapper(this.userAuthoritiesMapper())
																.userService(customOAuth2UserService)
																.oidcUserService(customOidcUserService)))
												.securityContext(secCon -> secCon
														.securityContextRepository(securityContextRepository))
												.logout(logout -> logout.addLogoutHandler(logoutHandler())
														.deleteCookies("JSESSIONID"))
												.csrf(csrf -> csrf.disable());
									}
								}
							}

						} else {
							http.cors(Customizer.withDefaults()).authorizeHttpRequests(auth -> auth
									.requestMatchers("/cf/createPassword", "/cf/sendResetPasswordMail",
											"/cf/resetPasswordPage", "/cf/sendResetPasswordMail", "/cf/resetPassword",
											"/cf/login")
									.denyAll()
									.requestMatchers("/cf/register", "/cf/confirm-account", "/cf/changePassword",
											"/cf/updatePassword", "/cf/configureTOTP", "/cf/sendConfigureTOTPMail")
									.denyAll()
									.requestMatchers("/cf/**", jQuiverPropeties.getViewPath() + "/**",
											jQuiverPropeties.getApiPath() + "/**", "/sch-api/**", "/japi/**", "/",
											"/error", jQuiverPropeties.getMonitoringPath())
									.permitAll().requestMatchers("/webjars/**").permitAll())
									.csrf(csrf -> csrf.disable());
						}
					}
				} else {
					http.cors(Customizer.withDefaults()).authorizeHttpRequests(auth -> auth
							.requestMatchers("/cf/createPassword", "/cf/sendResetPasswordMail", "/cf/resetPasswordPage",
									"/cf/sendResetPasswordMail", "/cf/resetPassword", "/cf/login")
							.denyAll()
							.requestMatchers("/cf/register", "/cf/confirm-account", "/cf/changePassword",
									"/cf/updatePassword", "/cf/configureTOTP", "/cf/sendConfigureTOTPMail")
							.denyAll()
							.requestMatchers("/cf/**", jQuiverPropeties.getViewPath() + "/**",
									jQuiverPropeties.getApiPath() + "/**", "/sch-api/**", "/japi/**", "/", "/error",
									jQuiverPropeties.getMonitoringPath())
							.permitAll().requestMatchers("/webjars/**").permitAll()).csrf(csrf -> csrf.disable());
				}
			} else {
				http.cors(Customizer.withDefaults()).authorizeHttpRequests(auth -> auth
						.requestMatchers("/cf/createPassword", "/cf/sendResetPasswordMail", "/cf/resetPasswordPage",
								"/cf/sendResetPasswordMail", "/cf/resetPassword", "/cf/login")
						.denyAll()
						.requestMatchers("/cf/register", "/cf/confirm-account", "/cf/changePassword",
								"/cf/updatePassword", "/cf/configureTOTP", "/cf/sendConfigureTOTPMail")
						.denyAll()
						.requestMatchers("/cf/**", jQuiverPropeties.getViewPath() + "/**",
								jQuiverPropeties.getApiPath() + "/**", "/sch-api/**", "/japi/**", "/", "/error",
								jQuiverPropeties.getMonitoringPath())
						.permitAll().requestMatchers("/webjars/**").permitAll()).csrf(csrf -> csrf.disable());
			}

			http.sessionManagement(
					sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
			http.headers(headers -> headers.xssProtection(Customizer.withDefaults()).contentSecurityPolicy(
					contentSecurityPolicy -> contentSecurityPolicy.policyDirectives(" ").reportOnly()));
			return http.build();
		}

	}

	@Bean
	public PersistentTokenRepository tokenRepository() {
		JdbcTokenRepositoryImpl jdbcTokenRepositoryImpl = new JdbcTokenRepositoryImpl();
		jdbcTokenRepositoryImpl.setDataSource(dataSource);
		return jdbcTokenRepositoryImpl;
	}

	@Bean
	public SecurityContextRepository securityContextRepository() {
		return new DelegatingSecurityContextRepository(new RequestAttributeSecurityContextRepository(),
				new HttpSessionSecurityContextRepository());
	}
}
