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

	@Value("${jquiver.template-storage-path}")
    private String templateStoragePath;

	@Value("${jquiver.file-upload-location}")
    private String fileUploadLocation;

	@Value("${jquiver.emlFileStoragePath}")
    private String emlFileStoragePath;

	@Value("${jquiver.file-copy-path}")
    private String fileCopyPath;

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

	public String getTemplateStoragePath() {
		return templateStoragePath;
	}

	public void setTemplateStoragePath(String templateStoragePath) {
		this.templateStoragePath = templateStoragePath;
	}

	public String getFileUploadLocation() {
		return fileUploadLocation;
	}

	public void setFileUploadLocation(String fileUploadLocation) {
		this.fileUploadLocation = fileUploadLocation;
	}

	public String getEmlFileStoragePath() {
		return emlFileStoragePath;
	}

	public void setEmlFileStoragePath(String emlFileStoragePath) {
		this.emlFileStoragePath = emlFileStoragePath;
	}

	public String getFileCopyPath() {
		return fileCopyPath;
	}

	public void setFileCopyPath(String fileCopyPath) {
		this.fileCopyPath = fileCopyPath;
	}
	
}
