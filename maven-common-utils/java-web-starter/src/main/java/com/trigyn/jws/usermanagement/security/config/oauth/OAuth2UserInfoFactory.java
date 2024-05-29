package com.trigyn.jws.usermanagement.security.config.oauth;

import java.util.Map;

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
			return new CustomOAuth2UserInfo(attributes);
		}
	}
}
