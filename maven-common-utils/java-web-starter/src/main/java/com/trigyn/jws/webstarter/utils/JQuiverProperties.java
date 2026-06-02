package com.trigyn.jws.webstarter.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JQuiverProperties {

	@Value("${jquiver.baseurl:http://localhost:8080}")
    private String baseUrl;
	
	@Value("${jquiver.view.path:/view}")
    private String viewPath;
	
	@Value("${jquiver.api.path:/api}")
    private String apiPath;

	@Value("${javamelody.init-parameters.monitoring-path:/monitoring}")
    private String monitoringPath;
	
	@Value("${jquiver.enableSecuredAuthentication:false}")
    private boolean enableSecuredAuthentication;
	
	@Value("${jquiver.authTokenExpiryTime:90}")
    private int authTokenExpiryTime;
	
	@Value("${server.port:8080}")
    private int serverPort;

	public int getServerPort() {
		return serverPort;
	}

	public int getAuthTokenExpiryTime() {
		return authTokenExpiryTime;
	}

	public void setAuthTokenExpiryTime(int authTokenExpiryTime) {
		this.authTokenExpiryTime = authTokenExpiryTime;
	}

	public boolean isEnableSecuredAuthentication() {
		return enableSecuredAuthentication;
	}

	public void setEnableSecuredAuthentication(boolean enableSecuredAuthentication) {
		this.enableSecuredAuthentication = enableSecuredAuthentication;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getViewPath() {
		return viewPath;
	}

	public String getApiPath() {
		return apiPath;
	}

	public String getMonitoringPath() {
		return monitoringPath;
	}
	
}
