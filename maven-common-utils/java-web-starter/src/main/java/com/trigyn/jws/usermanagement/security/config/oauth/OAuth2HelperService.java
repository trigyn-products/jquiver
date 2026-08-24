package com.trigyn.jws.usermanagement.security.config.oauth;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.trigyn.jws.usermanagement.security.config.ApplicationSecurityDetails;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.usermanagement.vo.AuthenticationDetails;
import com.trigyn.jws.usermanagement.vo.ConnectionDetailsJSONSpecification;
import com.trigyn.jws.usermanagement.vo.JwsAuthConfiguration;
import com.trigyn.jws.usermanagement.vo.MultiAuthSecurityDetailsVO;

import jakarta.servlet.ServletContext;

@Service
public class OAuth2HelperService {
	
	private final Log							logger							= LogFactory.getLog(getClass());

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
				.redirectUri(redirectUri)
//				.redirectUriTemplate(redirectUriTemplate.getValue())
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)// ClientAuthenticationMethod(String value)
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
		tokenResponseHttpMessageConverter.setAccessTokenResponseConverter(new CustomAccessTokenResponseConverter()); // https://github.com/jzheaux/messaging-app/blob/392a1eb724b7447928c750fb2e47c22ed26d144e/client-app/src/main/java/sample/web/CustomAccessTokenResponseConverter.java#L35

		RestTemplate restTemplate = new RestTemplate(
				Arrays.asList(new FormHttpMessageConverter(), tokenResponseHttpMessageConverter));
		restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());

		DefaultAuthorizationCodeTokenResponseClient tokenResponseClient = new DefaultAuthorizationCodeTokenResponseClient();
		tokenResponseClient.setRestOperations(restTemplate);

		return tokenResponseClient;
	}
	
	public Boolean checkOauthConnection(MultiValueMap<String, Object> formOauthData) {

		try {
			String registrationId = (String) formOauthData.getFirst("registrationId");
			String clientId = (String) formOauthData.getFirst("clientId");
			String clientSecret = (String) formOauthData.getFirst("clientSecret");
			String authorizationUri = (String) formOauthData.getFirst("authorizationUri");
			String tokenUri = (String) formOauthData.getFirst("tokenUri");
			String scope = (String) formOauthData.getFirst("scope");
			String redirectUri = (String) formOauthData.getFirst("redirectUri");
			String jwkUri = (String) formOauthData.getFirst("jwkUri");
			String usernameAttr = (String) formOauthData.getFirst("usernameAttr");
			/*
			 * ========================================================= 1. Mandatory field
			 * validation =========================================================
			 */

			if (isBlank(registrationId)) {
				logger.error("OAuth Registration ID is empty.");
				return false;
			}
			if (isBlank(clientId)) {
				logger.error("OAuth Client ID is empty.");
				return false;
			}
			if (isBlank(clientSecret)) {
				logger.error("OAuth Client Secret is empty.");
				return false;
			}
			if (isBlank(authorizationUri)) {
				logger.error("OAuth Authorization URI is empty.");
				return false;
			}
			if (isBlank(tokenUri)) {
				logger.error("OAuth Token URI is empty.");
				return false;
			}
			if (isBlank(redirectUri)) {
				logger.error("OAuth Redirect URI is empty.");
				return false;
			}
			if (isBlank(jwkUri)) {
				logger.error("OAuth JWK URI is empty.");
				return false;
			}
			logger.info("Checking OAuth configuration for registrationId: " + registrationId);
			/*
			 * Client ID validation 
			 * Microsoft Client ID is normally a GUID.
			 */
			if (isValidClientId(clientId) == false) {
				logger.error("Invalid OAuth Client ID format.");
				return false;
			}
			/*
			 * Client Secret validation 
			 * Only local length validation is performed.
			 * No request is made to Microsoft using the secret.
			 */
			if (isValidClientSecret(clientSecret) == false) {
				logger.error("Invalid OAuth Client Secret length.");
				return false;
			}
			/*
			 * Authorization URI validation 
			 */
			if (isOAuthEndpointReachable(authorizationUri) == false) {
				logger.error("Authorization URI is not reachable: " + authorizationUri);
				return false;
			}
			/*
			 * Token URI validation
			 */
			if (isOAuthEndpointReachable(tokenUri) == false) {
				logger.error("Token URI is not reachable: " + tokenUri);
				return false;
			}
			/*
			 * JWK URI validation
			 */
			if (isOAuthEndpointReachable(jwkUri) == false) {
				logger.error("JWK URI is not reachable: " + jwkUri);
				return false;
			}
			/*
			 * OAuth configuration
			 * passed all local validations
			 */
			logger.info("OAuth configuration verified successfully for registrationId: " + registrationId);
			return true;
		} catch (Exception ex) {
			logger.error("Failed to verify OAuth configuration.", ex);
			return false;
		}
	}

	/**
	 * Client ID validation 
	 * Microsoft OAuth Client ID / Application ID is normally represented as a GUID.
	 */
	private boolean isValidClientId(String clientId) {
		if (isBlank(clientId)) {
			return false;
		}
		String clientIdPattern = "^[0-9a-fA-F]{8}-" + "[0-9a-fA-F]{4}-" + "[0-9a-fA-F]{4}-" + "[0-9a-fA-F]{4}-"
				+ "[0-9a-fA-F]{12}$";
		return clientId.matches(clientIdPattern);
	}

	/**
	 * Client Secret validation
	 * Only performs local validation.
	 * We are intentionally NOT sending the Client Secret to Microsoft's token
	 * endpoint.
	 */
	private boolean isValidClientSecret(String clientSecret) {
		if (isBlank(clientSecret)) {
			return false;
		}

		// Basic length validation.
		// This prevents obviously incomplete/short secrets.
		return clientSecret.length() >= 30;
	}

	/**
	 * Null / empty validation
	 */
	private boolean isBlank(String value) {
		return value == null || value.trim().isEmpty();
	}

	/**
	 * OAuth endpoint validation
	 * This only checks whether the configured URL is reachable.
	 * It does NOT validate Client ID or Client Secret.
	 */
	private boolean isOAuthEndpointReachable(String endpointUrl) {
		HttpURLConnection connection = null;
		try {
			URL url = new URL(endpointUrl);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(10000);
			int responseCode = connection.getResponseCode();

			/*
			 * Any response between 200 and 499 means that the server was reachable.
			 *
			 * 404 means the configured endpoint does not exist.
			 */

			if (responseCode >= 200 && responseCode < 500) {
				if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
					logger.error("OAuth endpoint returned 404: " + endpointUrl);
					return false;
				}
				logger.info("OAuth endpoint reachable: " + endpointUrl + " Response Code: " + responseCode);
				return true;
			}
			logger.error("OAuth endpoint unavailable: " + endpointUrl + " Response Code: " + responseCode);
			return false;
		} catch (Exception ex) {
			logger.error("Unable to reach OAuth endpoint: " + endpointUrl, ex);
			return false;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
}
