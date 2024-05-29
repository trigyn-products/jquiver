package com.trigyn.jws.usermanagement.security.config;

import java.util.Objects;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;

import com.trigyn.jws.dbutils.service.ApplicationContextProvider;
import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dbutils.utils.CustomRuntimeException;
import com.trigyn.jws.usermanagement.utils.Constants;

import reactor.core.publisher.Mono;

/**
 * @author Shrinath.Halki
 *
 */

public class LoginSuccessEventListenerRestImpl implements LoginSuccessEventListener {

	private final Log				logger					= LogFactory.getLog(getClass());

	private ServletContext			servletContext			= ApplicationContextProvider.getBean(ServletContext.class);

	private PropertyMasterService	propertyMasterService	= ApplicationContextProvider
			.getBean(PropertyMasterService.class);

	private JwsUserDetailsService	jwsUserDetailsService	= ApplicationContextProvider
			.getBean(JwsUserDetailsService.class);

	private JwtUtil					jwtUtil					= ApplicationContextProvider.getBean(JwtUtil.class);

	private UserDetailsService		userDetailsService		= ApplicationContextProvider
			.getBean(UserDetailsService.class);

	@Override
	public void onLogin(UserInformation userInformation) {
		String	contextPath	= servletContext.getContextPath();
		String	baseUrl;
		try {
			userInformation.getUsername();
			HttpServletRequest	request		= ((ServletRequestAttributes) Objects
					.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
			String				authType	= request.getParameter("enableAuthenticationType");
			if (authType == null || authType.isEmpty()|| authType.isBlank()) {
				authType = request.getHeader("at");
				if (Constants.AuthTypeHeaderKey.DAO.getAuthTypeHeaderKey().equals(authType)) {
					authType = Constants.DAO_ID;
				} else if (Constants.AuthTypeHeaderKey.LDAP.getAuthTypeHeaderKey().equals(authType)) {
					authType = Constants.LDAP_ID;
				} else if (Constants.AuthTypeHeaderKey.OAUTH.getAuthTypeHeaderKey().equals(authType)) {
					authType = Constants.OAUTH_ID;
				}
			}
			String	authHeaderType		= "";
			if(authType!=null) {
				if(Integer.valueOf(authType) == Constants.AuthType.LDAP.getAuthType()) {
					authHeaderType	=	Constants.AuthTypeHeaderKey.LDAP.getAuthTypeHeaderKey();
				}else if(Integer.valueOf(authType) == Constants.AuthType.DAO.getAuthType()) {
					authHeaderType	=	Constants.AuthTypeHeaderKey.DAO.getAuthTypeHeaderKey();
				}
			}else {
				authHeaderType	=	Constants.AuthTypeHeaderKey.OAUTH.getAuthTypeHeaderKey();
			}
			baseUrl = propertyMasterService.findPropertyMasterValue("base-url");
			StringBuilder fullRestApiUrl = new StringBuilder().append(baseUrl);
			if (contextPath != null && contextPath.isEmpty() == false) {
				fullRestApiUrl = fullRestApiUrl.append(contextPath);
			}
			fullRestApiUrl = fullRestApiUrl.append("/japi/authCallback");
			String							restApiUrl		= fullRestApiUrl.toString();
			Builder							builder			= WebClient.builder().baseUrl(restApiUrl)
					.defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
					.defaultHeader(HttpHeaders.USER_AGENT, "JQuiver")
					.defaultHeader(HttpHeaders.AUTHORIZATION,
							"Bearer " + jwtUtil.generateToken(userDetailsService
									.loadUserByUsername(userInformation.getUsername())))
					.defaultHeader("at", authHeaderType);

			MultiValueMap<String, String>	multipvalueMap	= new LinkedMultiValueMap<String, String>();
			WebClient						webClient		= builder.build();
			Mono<ResponseEntity<String>>	responseContent	= webClient.method(HttpMethod.resolve("POST"))
					.uri(restApiUrl, uri -> uri.queryParams(multipvalueMap).build())
					.retrieve().onStatus(HttpStatus::is4xxClientError, response -> {
						return response.bodyToMono(CustomRuntimeException.class).flatMap(error -> {
							return Mono.error(new CustomRuntimeException(error));
						});
					})
					.onStatus(HttpStatus::is5xxServerError, response -> {
						return response.bodyToMono(CustomRuntimeException.class).flatMap(error -> {
							return Mono.error(new CustomRuntimeException(error));
						});
					}).toEntity(String.class);
			//ResponseEntity<String>			responseString	= responseContent.block();
			
		} catch (Exception exec) {
			exec.printStackTrace();
			logger.error("Error : " + exec.getMessage());
		}

	}

}
