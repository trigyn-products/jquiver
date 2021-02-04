package com.trigyn.jws.usermanagement.security.config;

public interface LogoutSuccessEventListener {

	void onLogout(UserInformation userInformation);
}
