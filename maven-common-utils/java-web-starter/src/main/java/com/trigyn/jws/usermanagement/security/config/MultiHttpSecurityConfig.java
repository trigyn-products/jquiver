package com.trigyn.jws.usermanagement.security.config;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.trigyn.jws.usermanagement.repository.JwsUserRepository;
import com.trigyn.jws.usermanagement.repository.JwsUserRoleAssociationRepository;
import com.trigyn.jws.usermanagement.security.config.oauth.CustomOAuth2UserService;
import com.trigyn.jws.usermanagement.security.config.oauth.CustomOidcUserService;
import com.trigyn.jws.usermanagement.security.config.oauth.OAuth2HelperService;
import com.trigyn.jws.usermanagement.service.UserConfigService;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.usermanagement.vo.JwsAuthConfiguration;
import com.trigyn.jws.usermanagement.vo.JwsAuthenticationType;
import com.trigyn.jws.usermanagement.vo.MultiAuthSecurityDetailsVO;

@EnableWebSecurity
public class MultiHttpSecurityConfig {
	
	@Autowired
	@Lazy
	private UserDetailsService				userDetailsService			= null;
	
	@Autowired
	private LogoutHandler					customLogoutSuccessHandler		= null;

	@Autowired
	private ApplicationSecurityDetails		applicationSecurityDetails		= null;

	@Autowired
	private PasswordEncoder					passwordEncoder					= null;

	@Autowired
	private DataSource						dataSource						= null;
	
	@Autowired
	private OAuth2HelperService				oAuth2HelperService				= null;
	
	@Autowired
	private CustomOidcUserService			customOidcUserService			= null;
	
	@Autowired
	private CustomOAuth2UserService			customOAuth2UserService			= null;
	
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
		return new CustomLogoutSuccessHandler();
	}

	
	@Configuration
	@Order(1)
	public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			Map<String, Object> authenticationDetails = applicationSecurityDetails.getAuthenticationDetails();
			if (authenticationDetails != null) {
				@SuppressWarnings("unchecked")
				List<MultiAuthSecurityDetailsVO> multiAuthSecurityDetails = (List<MultiAuthSecurityDetailsVO>) authenticationDetails
						.get("authenticationDetails");
				if (multiAuthSecurityDetails != null && multiAuthSecurityDetails.isEmpty() == false) {
					for (MultiAuthSecurityDetailsVO multiAuthLogin : multiAuthSecurityDetails) {
						Integer authType = multiAuthLogin.getAuthenticationTypeVO().getId();
						if (authenticationDetails != null) {
							if (authType == null || Constants.AuthType.INMEMORY.getAuthType() == authType) {
								// TODO : Need to move INMEMORY Auth to customAuthenticationProvider with
								// another switch case
								auth.inMemoryAuthentication().withUser("root@trigyn.com")
										.password(passwordEncoder.encode("root")).roles("ADMIN");
							} else if ((Constants.AuthType.DAO.getAuthType() == authType
									|| Constants.AuthType.LDAP.getAuthType() == authType)) {
								auth.authenticationProvider(customAuthenticationProvider());
							}
						}
					}
				}
			}
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
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
										http.cors(Customizer.withDefaults()).authorizeRequests().antMatchers("/webjars/**").permitAll().antMatchers("/")
												.permitAll()
												.antMatchers("/cf/createPassword", "/cf/sendResetPasswordMail",
														"/cf/resetPasswordPage", "/cf/sendResetPasswordMail",
														"/cf/resetPassword", "/cf/authenticate",
														"/cf/saveOtpAndSendMail","/cf/getResourceBundleData")
												.permitAll()
												.antMatchers("/cf/register", "/cf/confirm-account", "/cf/captcha/**",
														"/cf/changePassword", "/cf/updatePassword", "/cf/configureTOTP",
														"/cf/sendConfigureTOTPMail","/japi/**")
												.permitAll().antMatchers("/login/**", "/logout/**").permitAll()
												.antMatchers("/cf/files/**", "/view/**", "/cf/gl", "/cf/psdf")
												.permitAll().and().csrf().disable().formLogin().loginPage("/cf/login")
												.usernameParameter("email").permitAll()
												.failureHandler(loginFailureHandler())
												.successHandler(customAuthSuccessHandler())
												.and().rememberMe()
												.rememberMeParameter("remember-me").tokenRepository(tokenRepository())
												.and().logout().addLogoutHandler(customLogoutSuccessHandler)
												.deleteCookies("JSESSIONID");
									}

									if (Constants.AuthType.LDAP.getAuthType() == authType) {
										http.cors(Customizer.withDefaults()).authorizeRequests().antMatchers("/webjars/**").permitAll().antMatchers("/")
												.permitAll()
												.antMatchers("/cf/createPassword", "/cf/sendResetPasswordMail",
														"/cf/resetPasswordPage", "/cf/sendResetPasswordMail",
														"/cf/resetPassword", "/cf/authenticate",
														"/cf/saveOtpAndSendMail","/cf/getResourceBundleData")
												.permitAll()
												.antMatchers("/cf/register", "/cf/confirm-account", "/cf/captcha/**",
														"/cf/changePassword", "/cf/updatePassword", "/cf/configureTOTP",
														"/cf/sendConfigureTOTPMail", "/japi/**")
												.permitAll().antMatchers("/login/**", "/logout/**").permitAll()
												.antMatchers("/cf/files/**", "/view/**", "/cf/gl", "/cf/psdf")
												.permitAll().and().csrf().disable().formLogin().loginPage("/cf/login")
												.usernameParameter("email").permitAll()
												.failureHandler(loginFailureHandler())
												.successHandler(customAuthSuccessHandler())
												.and().rememberMe()
												.rememberMeParameter("remember-me").tokenRepository(tokenRepository())
												.and().logout().addLogoutHandler(customLogoutSuccessHandler)
												.deleteCookies("JSESSIONID");

									}

									if (Constants.AuthType.OAUTH.getAuthType() == authType) {
										List<List<JwsAuthConfiguration>>	configurations			= multiAuthLogin
												.getConnectionDetailsVO().getAuthenticationDetails()
												.getConfigurations();
											
											
											http.authorizeRequests().antMatchers("/cf/confirm-account").denyAll()
													.antMatchers("/webjars/**").permitAll()
													.antMatchers("/login/**", "/logout/**").permitAll()
													.antMatchers("/cf/files/**", "/view/**", "/cf/gl", "/cf/psdf",
															"/cf/getResourceBundleData")
													.permitAll().antMatchers("/cf/**").authenticated().and()
													.oauth2Login(oauth2 -> oauth2
															.clientRegistrationRepository(
																	oAuth2HelperService.clientRegistrationRepository())
															.loginPage("/cf/login").permitAll()
															.failureHandler(loginFailureHandler())
															.successHandler(customAuthSuccessHandler())
															.authorizationEndpoint().and()
															// Below Changes of for Linkedin
															.tokenEndpoint(token -> token
																	.accessTokenResponseClient(oAuth2HelperService
																			.authorizationCodeTokenResponseClient()))
															// Below Changes of for OIDC
															.userInfoEndpoint(userInfo -> userInfo
																	// .userAuthoritiesMapper(this.userAuthoritiesMapper())
																	.userService(customOAuth2UserService)
																	.oidcUserService(customOidcUserService)))
													.logout().addLogoutHandler(customLogoutSuccessHandler).and().csrf()
													.disable();
									}
								}
							}

						} else {
							http.cors(Customizer.withDefaults()).authorizeRequests()
									.antMatchers("/cf/createPassword", "/cf/sendResetPasswordMail",
											"/cf/resetPasswordPage", "/cf/sendResetPasswordMail", "/cf/resetPassword",
											"/cf/login")
									.denyAll()
									.antMatchers("/cf/register", "/cf/confirm-account",
											"/cf/changePassword", "/cf/updatePassword", "/cf/configureTOTP",
											"/cf/sendConfigureTOTPMail")
									.denyAll().antMatchers("/cf/**", "/view/**", "/").permitAll().and().csrf()
									.disable();
						}
					}
				} else {
					http.cors(Customizer.withDefaults()).authorizeRequests()
							.antMatchers("/cf/createPassword", "/cf/sendResetPasswordMail", "/cf/resetPasswordPage",
									"/cf/sendResetPasswordMail", "/cf/resetPassword", "/cf/login")
							.denyAll()
							.antMatchers("/cf/register", "/cf/confirm-account",  "/cf/changePassword",
									"/cf/updatePassword", "/cf/configureTOTP", "/cf/sendConfigureTOTPMail")
							.denyAll().antMatchers("/cf/**", "/view/**", "/").permitAll().and().csrf().disable();
				}
			} else {
				http.cors(Customizer.withDefaults()).authorizeRequests()
						.antMatchers("/cf/createPassword", "/cf/sendResetPasswordMail", "/cf/resetPasswordPage",
								"/cf/sendResetPasswordMail", "/cf/resetPassword", "/cf/login")
						.denyAll()
						.antMatchers("/cf/register", "/cf/confirm-account", "/cf/changePassword",
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

	
}
