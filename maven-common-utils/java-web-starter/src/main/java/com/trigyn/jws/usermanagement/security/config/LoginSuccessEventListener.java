package com.trigyn.jws.usermanagement.security.config;

public interface LoginSuccessEventListener {

	void onLogin(UserInformation userInformation);
}
