package com.trigyn.jws.usermanagement.security.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trigyn.jws.dbutils.entities.PropertyMaster;
import com.trigyn.jws.dbutils.repository.PropertyMasterRepository;
import com.trigyn.jws.usermanagement.entities.JwsAuthenticationType;
import com.trigyn.jws.usermanagement.repository.JwsAuthenticationTypeRepository;
import com.trigyn.jws.usermanagement.vo.ConnectionDetailsJSONSpecification;
import com.trigyn.jws.usermanagement.vo.JwsAuthenticationTypeVO;
import com.trigyn.jws.usermanagement.vo.MultiAuthSecurityDetailsVO;

@Component
public class ApplicationSecurityDetails {

	private static final Logger				logger							= LogManager
			.getLogger(ApplicationSecurityDetails.class);

	private Boolean							isAuthenticationEnabled			= null;

	private Map<String, Object>				authenticationDetails			= new HashMap<>();

	private String							baseUrl							= null;

	@Autowired
	private PropertyMasterRepository		propertyMasterRepository		= null;

	@Autowired
	private JwsAuthenticationTypeRepository	authenticationTypeRepository	= null;

	private Map<String, Object>				activeAuthDetails				= new HashMap<>();

	final ObjectMapper						mapper							= new ObjectMapper()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);;

	public ApplicationSecurityDetails(PropertyMasterRepository propertyMasterRepository) throws Exception {
		super();
		PropertyMaster propertyMaster = propertyMasterRepository.findByOwnerTypeAndOwnerIdAndPropertyName("system",
				"system", "enable-user-management");
		this.isAuthenticationEnabled = Boolean.parseBoolean(propertyMaster.getPropertyValue());
		PropertyMaster propertyMasterBaseUrl = propertyMasterRepository
				.findByOwnerTypeAndOwnerIdAndPropertyName("system", "system", "base-url");
		this.baseUrl = propertyMasterBaseUrl.getPropertyValue();

	}

	public Boolean getIsAuthenticationEnabled() {
		return isAuthenticationEnabled;
	}

	public Map<String, Object> getAuthenticationDetails() {
		if (isAuthenticationEnabled != null) {
			List<JwsAuthenticationType>			authenticationTypes	= authenticationTypeRepository
					.getAuthenticationTypes();
			List<MultiAuthSecurityDetailsVO>	multiAuthDetails	= new ArrayList<MultiAuthSecurityDetailsVO>();
			if (authenticationTypes != null && authenticationTypes.isEmpty() == false) {
				for (JwsAuthenticationType authenticationType : authenticationTypes) {
					JwsAuthenticationTypeVO	authenticationTypeVO		= new JwsAuthenticationTypeVO()
							.convertEntityToVO(authenticationType);
					String					authenticationProperties	= authenticationTypeVO
							.getAuthenticationProperties();
					if (authenticationProperties != null) {
						try {
							ConnectionDetailsJSONSpecification connectionSpecification = mapper.readValue(
									authenticationProperties, new TypeReference<ConnectionDetailsJSONSpecification>() {
									});
							if (connectionSpecification != null) {
								MultiAuthSecurityDetailsVO userLoginVO = new MultiAuthSecurityDetailsVO();
								userLoginVO.setAuthenticationTypeVO(authenticationTypeVO);
								userLoginVO.setConnectionDetailsVO(connectionSpecification);
								if (isAuthenticationEnabled && connectionSpecification.getAuthenticationType().getValue()
										.equalsIgnoreCase("true")) {
									activeAuthDetails.put(authenticationTypeVO.getId().toString(),
											connectionSpecification);
								}
								multiAuthDetails.add(userLoginVO);
							}

						} catch (JsonProcessingException error) {
							logger.error("ERROR : " + error);
						}
					}
				}
			}

			authenticationDetails.put("isAuthenticationEnabled", isAuthenticationEnabled);
			authenticationDetails.put("authenticationDetails", multiAuthDetails);
			authenticationDetails.put("activeAuthDetails", activeAuthDetails);
			return authenticationDetails;
		}
		return null;

	}

	public void resetApplicationSecurityDetails() {
		PropertyMaster propertyMaster = propertyMasterRepository.findByOwnerTypeAndOwnerIdAndPropertyName("system",
				"system", "enable-user-management");
		this.isAuthenticationEnabled	= Boolean.parseBoolean(propertyMaster.getPropertyValue());
		this.authenticationDetails		= getAuthenticationDetails();
	}

	public String getBaseUrl() {
		return baseUrl;
	}

}
