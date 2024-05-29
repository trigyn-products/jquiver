package com.trigyn.jws.usermanagement.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trigyn.jws.usermanagement.security.config.ApplicationSecurityDetails;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.usermanagement.utils.Constants.VerificationType;
import com.trigyn.jws.usermanagement.vo.AdditionalDetails;
import com.trigyn.jws.usermanagement.vo.AuthenticationDetails;
import com.trigyn.jws.usermanagement.vo.JwsAuthAdditionalProperty;
import com.trigyn.jws.usermanagement.vo.JwsAuthConfiguration;
import com.trigyn.jws.usermanagement.vo.JwsAuthenticationType;
import com.trigyn.jws.usermanagement.vo.JwsUserLoginVO;
import com.trigyn.jws.usermanagement.vo.MultiAuthSecurityDetailsVO;

@Service
public class UserConfigService {

	@Autowired
	private ApplicationSecurityDetails applicationSecurityDetails = null;

	public void getConfigurableDetails(Map<String, Object> mapDetails) throws Exception, JSONException {
		if (applicationSecurityDetails.getAuthenticationDetails() != null) {
			Map<String, Object>	authenticationDetails	= applicationSecurityDetails.getAuthenticationDetails();
			Boolean				isAuthenticationEnabled	= applicationSecurityDetails.getIsAuthenticationEnabled();
			mapDetails.put("isAuthenticationEnabled", isAuthenticationEnabled);
			mapDetails.put("enableGoogleAuthenticator", false);
			List<JwsUserLoginVO> userLoginDetails = new ArrayList<JwsUserLoginVO>();
			if (authenticationDetails.isEmpty() == false && isAuthenticationEnabled) {
				@SuppressWarnings("unchecked")
				List<MultiAuthSecurityDetailsVO> multiAuthSecurityDetails = (List<MultiAuthSecurityDetailsVO>) authenticationDetails
						.get("authenticationDetails");
				if (multiAuthSecurityDetails != null) {
					for (MultiAuthSecurityDetailsVO securityAuthDetail : multiAuthSecurityDetails) {
						JwsAuthenticationType authenticationType = securityAuthDetail.getConnectionDetailsVO()
								.getAuthenticationType();
						if (authenticationType != null && authenticationType.getValue().equalsIgnoreCase("true")) {
							Map<String, Object>	authDetail			= new HashMap<>();
							JwsUserLoginVO		jwsUserLoginVO		= new JwsUserLoginVO();
							List<String>		ldapDisplayDetails	= new ArrayList<>();
							boolean				isEldapEnabled		= false;
							jwsUserLoginVO.setAuthenticationType(securityAuthDetail.getAuthenticationTypeVO().getId());
							jwsUserLoginVO.setVerificationType(0);
							authDetail.put(authenticationType.getName(), authenticationType.getValue());
							mapDetails.put(authenticationType.getName(), authenticationType.getValue());
							AuthenticationDetails	authenticationDetail	= securityAuthDetail
									.getConnectionDetailsVO().getAuthenticationDetails();
							List<String>			regisrationIds			= new ArrayList<String>();
							Map<String, String>		registIds				= new HashMap<>();
							if (authenticationDetail != null && authenticationDetail.getConfigurations() != null) {
								for (List<JwsAuthConfiguration> configurationDetail : authenticationDetail
										.getConfigurations()) {
									if (configurationDetail != null) {
										if (securityAuthDetail.getAuthenticationTypeVO().getId()
												.equals(Constants.AuthType.OAUTH.getAuthType())) {
											JwsAuthConfiguration	registrationId	= configurationDetail.stream()
													.filter(additionalProperty -> additionalProperty != null && additionalProperty.getName() !=null
															&& additionalProperty.getName().equalsIgnoreCase("registration-id"))
													.findAny().orElse(null);
											JwsAuthConfiguration	displayName		= configurationDetail.stream()
													.filter(configProperty -> configProperty != null && configProperty.getName() !=null 
															&& configProperty.getName().equalsIgnoreCase("displayName"))
													.findAny().orElse(null);
											if(registrationId != null && registrationId.getValue() !=null) {
												regisrationIds.add(registrationId.getValue());
												JwsAuthConfiguration	imgPath	= configurationDetail.stream()
													.filter(additionalProperty -> additionalProperty != null && additionalProperty.getName()!=null 
															&& additionalProperty.getName().equalsIgnoreCase("img-path"))
													.findAny().orElse(null);
												
												if(imgPath != null)
													registIds.put(registrationId.getValue(),
															imgPath.getValue());
											}

										}
										for (JwsAuthConfiguration authConfiguration : configurationDetail) {
											if (authConfiguration != null
													&& authenticationType.getConfigurationType() != null
													&& authConfiguration.getName() != null
													&& authConfiguration.getValue() != null) {
												authDetail.put(authConfiguration.getName(),
														authConfiguration.getValue());
												mapDetails.put(authConfiguration.getName(),
														authConfiguration.getValue());
												AdditionalDetails additionalDetails = authConfiguration
														.getAdditionalDetails();
												if (securityAuthDetail.getAuthenticationTypeVO().getId()
														.equals(Constants.AuthType.LDAP.getAuthType())) {
													if (StringUtils.isNotEmpty(authConfiguration.getName())
															&& StringUtils.isNotEmpty(authConfiguration.getValue())
															&& authConfiguration.getName()
																	.equalsIgnoreCase("displayName")) {
														ldapDisplayDetails.add(authConfiguration.getValue());
														isEldapEnabled = true;
													}
												}
												
												if (additionalDetails != null) {

													List<JwsAuthAdditionalProperty> authAdditionalProperties = additionalDetails
															.getAdditionalProperties();
													if (authAdditionalProperties != null) {

														JwsAuthAdditionalProperty verficationType = authAdditionalProperties
																.stream()
																.filter(config -> config != null
																		&& config.getName() != null
																		&& config.getName()
																				.equals("verificationType"))
																.findFirst().orElse(null);
														if (verficationType != null) {
															jwsUserLoginVO.setVerificationType(Integer
																	.valueOf(verficationType.getValue()));
															mapDetails.put("verificationType",
																	verficationType.getValue());
															mapDetails.put("authenticationType",
																	Constants.AuthType.DAO.getAuthType());
															if (VerificationType.TOTP.getVerificationType()
																	.equals(verficationType.getValue())) {
																mapDetails.put("enableGoogleAuthenticator",
																		true);
															}
														}
														for (JwsAuthAdditionalProperty additionalProperty : authAdditionalProperties) {
															if (additionalProperty != null
																	&& additionalProperty.getName() != null
																	&& additionalProperty.getValue() != null) {
																authDetail.put(additionalProperty.getName(),
																		additionalProperty.getValue());
																mapDetails.put(additionalProperty.getName(),
																		additionalProperty.getValue());
															}
														}
													
													}
												}
											}
										}
									}
								}
								if (regisrationIds.isEmpty() == false) {
									authDetail.put("regisrationIds", regisrationIds);
									mapDetails.put("regisrationIds", regisrationIds);
									mapDetails.put("registIds", registIds);
									authDetail.put("registIds", registIds);
								}
							}
							if (isEldapEnabled) {
								Collections.sort(ldapDisplayDetails);
								authDetail.put("ldapDisplayDetails", ldapDisplayDetails);
								mapDetails.put("ldapDisplayDetails", ldapDisplayDetails);
							}
							if(mapDetails!= null && mapDetails.get("previousMail") != null) {
								authDetail.put("previousMail", mapDetails.get("previousMail"));
							}
							jwsUserLoginVO.setLoginAttributes(authDetail);
							userLoginDetails.add(jwsUserLoginVO);
						}
					}
				}
			}
			mapDetails.put("activeAutenticationDetails", userLoginDetails);
			mapDetails.put("authDetails", mapDetails);
		}

	}

	public JSONObject getJsonObjectFromPropertyValue(JSONObject jsonObject, JSONArray jsonArray, String propertyName)
			throws JSONException {
		for (int i = 0; i < jsonArray.length(); i++) {
			jsonObject = jsonArray.getJSONObject(i);
			if (jsonObject.get("name").toString().equalsIgnoreCase(propertyName)) {
				break;
			} else {
				jsonObject = null;
			}
		}
		return jsonObject;
	}
}
