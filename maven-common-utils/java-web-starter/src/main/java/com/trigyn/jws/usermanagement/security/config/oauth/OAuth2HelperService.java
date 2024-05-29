package com.trigyn.jws.usermanagement.security.config.oauth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.trigyn.jws.usermanagement.security.config.ApplicationSecurityDetails;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.usermanagement.vo.AuthenticationDetails;
import com.trigyn.jws.usermanagement.vo.ConnectionDetailsJSONSpecification;
import com.trigyn.jws.usermanagement.vo.DropDownData;
import com.trigyn.jws.usermanagement.vo.JwsAuthAdditionalProperty;
import com.trigyn.jws.usermanagement.vo.JwsAuthConfiguration;
import com.trigyn.jws.usermanagement.vo.MultiAuthSecurityDetailsVO;

@Service
public class OAuth2HelperService {

	@Autowired
	private ApplicationSecurityDetails	applicationSecurityDetails	= null;

	@Autowired
	private ServletContext				servletContext				= null;

	private static List<String>			clients						= new ArrayList<>();

	public ClientRegistrationRepository clientRegistrationRepository() {
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
								
								JwsAuthConfiguration registration = oAuthConfigurationDetails.stream()
										.filter(additionalProperty -> additionalProperty != null && additionalProperty.getName() !=null 
												&& additionalProperty.getName() 
														.equalsIgnoreCase("registration-id"))
										.findAny().orElse(null);
								JwsAuthConfiguration	displayName		= oAuthConfigurationDetails.stream()
										.filter(configProperty -> configProperty != null && configProperty.getName() !=null
												&& configProperty.getName().equalsIgnoreCase("displayName"))
										.findAny().orElse(null);
									clients.add(registration.getValue());
									ClientRegistration client = getRegistration(oAuthConfigurationDetails);
									if (client != null)
										registrations.add(client);

							}
						}
					}
				}
			}
		}
		return new InMemoryClientRegistrationRepository(registrations);
	}

	public ClientRegistration getRegistration(List<JwsAuthConfiguration> additionalProps) {

		String						clientId			= null;
		String						clientSecret		= null;
		Map<String, Object> configurationMetadata 		= new HashMap<>();

		JwsAuthConfiguration	oAuthClientId		= additionalProps.stream() 
				.filter(additionalProperty -> additionalProperty != null && additionalProperty.getName() !=null
						&& additionalProperty.getName().equalsIgnoreCase("client-id"))
				.findAny().orElse(null);
		JwsAuthConfiguration	oAuthClientSecret	= additionalProps.stream()
				.filter(additionalProperty -> additionalProperty != null && additionalProperty.getName() !=null
						&& additionalProperty.getName().equalsIgnoreCase("client-secret"))
				.findAny().orElse(null);

		JwsAuthConfiguration	clientNameObj		= additionalProps.stream()
				.filter(additionalProperty -> additionalProperty != null && additionalProperty.getName() !=null
						&& additionalProperty.getName().equalsIgnoreCase("displayName"))
				.findAny().orElse(null);

		if (oAuthClientId != null)
			clientId = oAuthClientId.getValue();

		if (oAuthClientSecret != null)
			clientSecret = oAuthClientSecret.getValue();

		if (clientId == null || clientSecret == null) {
			return null;
		}

		JwsAuthConfiguration	authorizationUri	= additionalProps.stream()
				.filter(additionalProperty -> additionalProperty != null && additionalProperty.getName() !=null
						&& additionalProperty.getName().equalsIgnoreCase("authorization-uri"))
				.findAny().orElse(null);

		JwsAuthConfiguration	tokenUri			= additionalProps.stream()
				.filter(additionalProperty -> additionalProperty != null && additionalProperty.getName() !=null
						&& additionalProperty.getName().equalsIgnoreCase("token-uri"))
				.findAny().orElse(null);

		JwsAuthConfiguration	jwkSetUri			= additionalProps.stream()
				.filter(additionalProperty -> additionalProperty != null && additionalProperty.getName() !=null
						&& additionalProperty.getName().equalsIgnoreCase("jwk-set-uri"))
				.findAny().orElse(null);

		JwsAuthConfiguration	registrationId		= additionalProps.stream()
				.filter(additionalProperty -> additionalProperty != null && additionalProperty.getName() !=null
						&& additionalProperty.getName().equalsIgnoreCase("registration-id"))
				.findAny().orElse(null);

		JwsAuthConfiguration	scopes				= additionalProps.stream()
				.filter(additionalProperty -> additionalProperty != null && additionalProperty.getName() !=null
						&& additionalProperty.getName().equalsIgnoreCase("scope"))
				.findAny().orElse(null);

		JwsAuthConfiguration	accessToken			= additionalProps.stream()
				.filter(additionalProperty -> additionalProperty != null && additionalProperty.getName() !=null
						&& additionalProperty.getName().equalsIgnoreCase("access_token"))
				.findAny().orElse(null);

		JwsAuthConfiguration	userInfoUri			= additionalProps.stream()
				.filter(additionalProperty -> additionalProperty != null && additionalProperty.getName() !=null
						&& additionalProperty.getName().equalsIgnoreCase("user-info-uri"))
				.findAny().orElse(null);

		JwsAuthConfiguration	redirectUriTemplate	= additionalProps.stream()
				.filter(additionalProperty -> additionalProperty != null && additionalProperty.getName() !=null
						&& additionalProperty.getName().equalsIgnoreCase("redirect-uri-template"))
				.findAny().orElse(null);

		String						redirectUri			= String.format(
				"%s%s/login/oauth2/code/" + registrationId.getValue(), applicationSecurityDetails.getBaseUrl(),
				servletContext.getContextPath());

		JwsAuthConfiguration	userNameAttribute	= additionalProps.stream()
				.filter(additionalProperty -> additionalProperty != null && additionalProperty.getName() !=null
						&& additionalProperty.getName().equalsIgnoreCase("user-name-attr"))
				.findAny().orElse(null);
		String						clientName			= registrationId.getValue();
		String userInfoUrl = (null == userInfoUri || userInfoUri.getValue().equalsIgnoreCase("null") || StringUtils.isEmpty(userInfoUri.getValue())) ? null
				: userInfoUri.getValue();
		if(null == userInfoUri || userInfoUri.getValue().trim().equalsIgnoreCase("NULL") || StringUtils.isEmpty(userInfoUri.getValue())) {
			userInfoUrl = null;
		}
		return ClientRegistration.withRegistrationId(registrationId.getValue())
				.redirectUriTemplate(redirectUriTemplate.getValue())
				.clientAuthenticationMethod(ClientAuthenticationMethod.POST)// ClientAuthenticationMethod(String value)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE) // AuthorizationGrantType(String
																					// value)
				.authorizationUri(authorizationUri.getValue())
				.scope((null == scopes || StringUtils.isEmpty(scopes.getValue())) ? null : scopes.getValue().split(",")) // str.split(",")
				.tokenUri(tokenUri.getValue())
				.jwkSetUri(
						(null == jwkSetUri || StringUtils.isEmpty(jwkSetUri.getValue())) ? null : jwkSetUri.getValue())
				.userInfoUri(userInfoUrl)
				.providerConfigurationMetadata(configurationMetadata)
				.userNameAttributeName(
						(null == userNameAttribute || StringUtils.isEmpty(userNameAttribute.getValue())) ? null
								: userNameAttribute.getValue().toLowerCase())
				.clientName(clientName).clientId(clientId).clientSecret(clientSecret).build();
	}

	public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> authorizationCodeTokenResponseClient() {
		OAuth2AccessTokenResponseHttpMessageConverter tokenResponseHttpMessageConverter = new OAuth2AccessTokenResponseHttpMessageConverter();
		tokenResponseHttpMessageConverter.setTokenResponseConverter(new CustomAccessTokenResponseConverter()); // https://github.com/jzheaux/messaging-app/blob/392a1eb724b7447928c750fb2e47c22ed26d144e/client-app/src/main/java/sample/web/CustomAccessTokenResponseConverter.java#L35

		RestTemplate restTemplate = new RestTemplate(
				Arrays.asList(new FormHttpMessageConverter(), tokenResponseHttpMessageConverter));
		restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());

		DefaultAuthorizationCodeTokenResponseClient tokenResponseClient = new DefaultAuthorizationCodeTokenResponseClient();
		tokenResponseClient.setRestOperations(restTemplate);

		return tokenResponseClient;
	}

}
