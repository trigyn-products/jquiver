package com.trigyn.jws.usermanagement.security.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.trigyn.jws.usermanagement.repository.JwsUserRepository;
import com.trigyn.jws.usermanagement.repository.JwsUserRoleAssociationRepository;
import com.trigyn.jws.usermanagement.service.UserConfigService;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.usermanagement.vo.AuthenticationDetails;
import com.trigyn.jws.usermanagement.vo.ConnectionDetailsJSONSpecification;
import com.trigyn.jws.usermanagement.vo.DropDownData;
import com.trigyn.jws.usermanagement.vo.JwsAuthAdditionalProperty;
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

	private static List<String>				clients							= new ArrayList<>();
	// Arrays.asList("google", "facebook");

	@Autowired
	private ServletContext					servletContext					= null;

	@Autowired
	private CustomAuthenticationProvider	customAuthenticationProvider	= null;

	@Bean
	public UserDetailsService userDetailsService(JwsUserRepository userRepository,
			JwsUserRoleAssociationRepository userRoleAssociationRepository, UserConfigService userConfigService) {
		return new DefaultUserDetailsServiceImpl(userRepository, userRoleAssociationRepository, userConfigService);
	}
	
	@Bean
	public AuthenticationSuccessHandler customAuthSuccessHandler() {
		return new CustomAuthSuccessHandler();
	}


	@Bean
	public CustomLoginFailureHandler loginFailureHandler() {
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
							} else if (Constants.AuthType.DAO.getAuthType() == authType
									|| Constants.AuthType.LDAP.getAuthType() == authType) {
								auth.userDetailsService(userDetailsService);
								auth.authenticationProvider(customAuthenticationProvider);
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
										http.authorizeRequests().antMatchers("/webjars/**").permitAll().antMatchers("/")
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
										http.authorizeRequests().antMatchers("/webjars/**").permitAll().antMatchers("/")
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
										List<DropDownData>					selectedDropDownData	= null;
										for (List<JwsAuthConfiguration> multiAuthSecConfigurations : configurations) {
											selectedDropDownData = multiAuthSecConfigurations.stream()
													.filter(configuration -> configuration.getDropDownData() != null)
													.flatMap(dropDownDatas -> dropDownDatas.getDropDownData().stream())
													.filter(dropDownData -> dropDownData.getSelected() != null
															&& dropDownData.getSelected())
													.collect(Collectors.toList());
										}

										if (selectedDropDownData != null && selectedDropDownData.size() > 0) {
											http.authorizeRequests().antMatchers("/cf/confirm-account").denyAll()
													.antMatchers("/webjars/**").permitAll()
													.antMatchers("/login/**", "/logout/**").permitAll()
													.antMatchers("/cf/files/**", "/view/**", "/cf/gl", "/cf/psdf","/cf/getResourceBundleData")
													.permitAll()
													// below line will enable redirection to authentication page, if
													// user
													// has not logged in. Example, clicking directly to survey link.
													.antMatchers("/cf/**").authenticated().and().oauth2Login()
													.clientRegistrationRepository(clientRegistrationRepository())
													.loginPage("/cf/login").permitAll()
													.failureHandler(loginFailureHandler())
													.successHandler(customAuthSuccessHandler())
													.and().logout()
													.addLogoutHandler(customLogoutSuccessHandler).and().csrf()
													.disable();
										}
									}
								}
							}

						} else {
							http.authorizeRequests()
									.antMatchers("/cf/createPassword", "/cf/sendResetPasswordMail",
											"/cf/resetPasswordPage", "/cf/sendResetPasswordMail", "/cf/resetPassword",
											"/cf/login")
									.denyAll()
									.antMatchers("/cf/register", "/cf/confirm-account", "/cf/captcha/**",
											"/cf/changePassword", "/cf/updatePassword", "/cf/configureTOTP",
											"/cf/sendConfigureTOTPMail")
									.denyAll().antMatchers("/cf/**", "/view/**", "/").permitAll().and().csrf()
									.disable();
						}
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

	private ClientRegistrationRepository clientRegistrationRepository() {
		List<ClientRegistration>	registrations			= new ArrayList<ClientRegistration>();
		Map<String, Object>			authenticationDetails	= applicationSecurityDetails.getAuthenticationDetails();
		if (authenticationDetails != null) {
			@SuppressWarnings("unchecked")
			List<MultiAuthSecurityDetailsVO> multiAuthSecurityDetails = (List<MultiAuthSecurityDetailsVO>) authenticationDetails
					.get("authenticationDetails");
			if (multiAuthSecurityDetails != null) {
				for (MultiAuthSecurityDetailsVO authSecurityDetail : multiAuthSecurityDetails) {
					Integer authType = authSecurityDetail.getAuthenticationTypeVO().getId();
					if (authType != null && Constants.AuthType.OAUTH.getAuthType() == authType) {
						ConnectionDetailsJSONSpecification oAuthType = authSecurityDetail.getConnectionDetailsVO();
						if (oAuthType.getAuthenticationType() != null && oAuthType.getAuthenticationDetails() != null) {
							AuthenticationDetails authenticationDetail = oAuthType.getAuthenticationDetails();
							for (List<JwsAuthConfiguration> oAuthConfigurationDetails : authenticationDetail
									.getConfigurations()) {
								for (JwsAuthConfiguration authConfiguration : oAuthConfigurationDetails) {
									List<DropDownData> dropDownDatas = authConfiguration.getDropDownData();
									for (DropDownData dropDownData : dropDownDatas) {
										if (!clients.contains(dropDownData.getName())) {
											clients.add(dropDownData.getName());
											ClientRegistration client = getRegistration(dropDownData);
											if (client != null)
												registrations.add(client);
										}

									}
								}

							}
						}
					}
				}
			}
		}
		return new InMemoryClientRegistrationRepository(registrations);
	}

	private ClientRegistration getRegistration(DropDownData dropDownData) {
		if (dropDownData == null) {
			return null;
		}
		String									clientName				= dropDownData.getName();
		String									clientId				= null;
		String									clientSecret			= null;
		String									clientType				= dropDownData.getName();

		List<List<JwsAuthAdditionalProperty>>	additionalProperties	= dropDownData.getAdditionalDetails()
				.getAdditionalProperties();
		if (additionalProperties == null) {
			return null;
		}
		for (List<JwsAuthAdditionalProperty> additionalProps : additionalProperties) {

			JwsAuthAdditionalProperty	oAuthClientId		= additionalProps.stream()
					.filter(additionalProperty -> additionalProperty != null
							&& additionalProperty.getName().equalsIgnoreCase("client-id"))
					.findAny().orElse(null);
			JwsAuthAdditionalProperty	oAuthClientSecret	= additionalProps.stream()
					.filter(additionalProperty -> additionalProperty != null
							&& additionalProperty.getName().equalsIgnoreCase("client-secret"))
					.findAny().orElse(null);

			if (oAuthClientId != null)
				clientId = oAuthClientId.getValue();

			if (oAuthClientSecret != null)
				clientSecret = oAuthClientSecret.getValue();

			if (clientId == null || clientSecret == null) {
				return null;
			}

			if (clientType.equals("google")) {
				return CommonOAuth2Provider.GOOGLE.getBuilder(clientName).clientId(clientId).clientSecret(clientSecret)
						.build();
			}
			if (clientType.equals("facebook")) {
				return CommonOAuth2Provider.FACEBOOK.getBuilder(clientName).clientId(clientId)
						.clientSecret(clientSecret).build();
			}
			if (clientType.equals("github")) {
				return CommonOAuth2Provider.GITHUB.getBuilder(clientName).clientId(clientId).clientSecret(clientSecret)
						.build();
			}
			if (clientType.equals("office-365")) {
				JwsAuthAdditionalProperty registrationId = additionalProps.stream()
						.filter(additionalProperty -> additionalProperty != null
								&& additionalProperty.getName().equalsIgnoreCase("registration-id"))
						.findAny().orElse(null);
				if (registrationId != null) {
					String redirectUri = String.format("%s%s/login/oauth2/code/" + registrationId.getValue(),
							applicationSecurityDetails.getBaseUrl(), servletContext.getContextPath());
					return ClientRegistration.withRegistrationId(registrationId.getValue()).clientId(clientId)
							.clientSecret(clientSecret)
							.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE).scope("openid")
							.redirectUriTemplate(redirectUri)

							.authorizationUri("https://login.microsoftonline.com/common/oauth2/authorize")
							.tokenUri("https://login.microsoftonline.com/common/oauth2/token")
							.jwkSetUri("https://login.microsoftonline.com/common/discovery/keys").build();
				}

			}
		}
		return null;
	}

}
