package com.trigyn.jws.usermanagement.security.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.trigyn.jws.dbutils.entities.PropertyMaster;
import com.trigyn.jws.dbutils.entities.PropertyMasterPK;
import com.trigyn.jws.dbutils.repository.PropertyMasterRepository;

@Component
public class ApplicationSecurityDetails {

	private Boolean isAuthenticationEnabled = null;
	
	private String authenticationType		= null;
	
	private Map<String, Object> authenticationDetails = new HashMap<>();

	public ApplicationSecurityDetails(PropertyMasterRepository propertyMasterRepository) throws Exception {
		super();
		PropertyMasterPK id = new PropertyMasterPK("system", "system", "enable-user-management");
		PropertyMaster propertyMaster = propertyMasterRepository.findById(id).orElse(new PropertyMaster());
		this.isAuthenticationEnabled = Boolean.parseBoolean(propertyMaster.getPropertyValue());
		PropertyMasterPK idAuthType = new PropertyMasterPK("system", "system", "authentication-type");
		PropertyMaster propertyMasterAuthType = propertyMasterRepository.findById(idAuthType).orElse(new PropertyMaster());
		this.authenticationType = propertyMasterAuthType.getPropertyValue();
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
	
}
