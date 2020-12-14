package com.trigyn.jws.usermanagement.security.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trigyn.jws.dbutils.entities.PropertyMaster;
import com.trigyn.jws.dbutils.repository.PropertyMasterRepository;

@Component
public class ApplicationSecurityDetails {

	private Boolean isAuthenticationEnabled = null;
	
	private String authenticationType		= null;
	
	private Map<String, Object> authenticationDetails = new HashMap<>();
	
	private String baseUrl	= null;
	
	@Autowired
	private PropertyMasterRepository propertyMasterRepository = null;

	public ApplicationSecurityDetails(PropertyMasterRepository propertyMasterRepository) throws Exception {
		super();
		PropertyMaster propertyMaster = propertyMasterRepository.findByOwnerTypeAndOwnerIdAndPropertyName("system", "system", "enable-user-management");
		this.isAuthenticationEnabled = Boolean.parseBoolean(propertyMaster.getPropertyValue());
		PropertyMaster propertyMasterAuthType = propertyMasterRepository.findByOwnerTypeAndOwnerIdAndPropertyName("system", "system", "authentication-type");
		this.authenticationType = propertyMasterAuthType.getPropertyValue();
		PropertyMaster propertyMasterBaseUrl = propertyMasterRepository.findByOwnerTypeAndOwnerIdAndPropertyName("system", "system", "base-url");
		this.baseUrl = propertyMasterBaseUrl.getPropertyValue();
	}

	public Boolean getIsAuthenticationEnabled() {
		return isAuthenticationEnabled;
	}

	public String getAuthenticationType() {
		if(isAuthenticationEnabled) {
			return authenticationType;
		}
		return null;
	}

	public Map<String, Object> getAuthenticationDetails() {
		return authenticationDetails;
	}
	
	public void resetApplicationSecurityDetails() {
		PropertyMaster propertyMaster = propertyMasterRepository.findByOwnerTypeAndOwnerIdAndPropertyName("system", "system", "enable-user-management");
		this.isAuthenticationEnabled = Boolean.parseBoolean(propertyMaster.getPropertyValue());
		PropertyMaster propertyMasterAuthType = propertyMasterRepository.findByOwnerTypeAndOwnerIdAndPropertyName("system", "system", "authentication-type");
		this.authenticationType = propertyMasterAuthType.getPropertyValue();
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	
	
}
