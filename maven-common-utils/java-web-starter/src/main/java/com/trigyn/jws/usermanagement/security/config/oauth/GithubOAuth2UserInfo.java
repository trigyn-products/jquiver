package com.trigyn.jws.usermanagement.security.config.oauth;

import java.util.Map;

public class GithubOAuth2UserInfo extends OAuth2UserInfo {

	public GithubOAuth2UserInfo(Map<String, Object> attributes) {
		super(attributes);
	}

	@Override
	public String getId() {
		return ((Integer) attributes.get("id")).toString();
	}

	@Override
	public String getName() {
		return (String) attributes.get("name");
	}

	@Override
	public String getEmail() {
		if((String) attributes.get("upn") !=null)
			return (String) attributes.get("upn");
		else if((String) attributes.get("email") !=null)
			return (String) attributes.get("email");
		else
			return (String) attributes.get("login");
	}

	@Override
	public String getImageUrl() {
		return (String) attributes.get("avatar_url");
	}
}
