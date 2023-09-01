package com.trigyn.jws.usermanagement.security.config.oauth;

import java.util.Map;

import com.trigyn.jws.usermanagement.exception.OAuth2AuthenticationProcessingException;

public class OAuth2UserInfoFactory {

	public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
		if (registrationId.equalsIgnoreCase("google")) {
			return new GoogleOAuth2UserInfo(attributes);
		} else if (registrationId.equalsIgnoreCase("facebook")) {
			return new FacebookOAuth2UserInfo(attributes);
		} else if (registrationId.equalsIgnoreCase("github")) {
			return new GithubOAuth2UserInfo(attributes);
		} else if (registrationId.equalsIgnoreCase("office365")) {
			return new Office365OAuth2UserInfo(attributes);
		} else {
			throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
		}
	}
}
