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
import com.trigyn.jws.usermanagement.vo.DropDownData;
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
						JwsAuthenticationType authenticationType = securityAuthDetail.getConnectionDetailsVO().getAuthenticationType();
						if (authenticationType!=null && authenticationType.getValue().equalsIgnoreCase("true")) {
							Map<String, Object>	authDetail		= new HashMap<>();
							JwsUserLoginVO		jwsUserLoginVO	= new JwsUserLoginVO();
							List<String> ldapDisplayDetails = new ArrayList<>();
							boolean isEldapEnabled = false;
							jwsUserLoginVO.setAuthenticationType(securityAuthDetail.getAuthenticationTypeVO().getId());
							jwsUserLoginVO.setVerificationType(0);
							authDetail.put(authenticationType.getName(), authenticationType.getValue());
							mapDetails.put(authenticationType.getName(), authenticationType.getValue());
							AuthenticationDetails authenticationDetail = securityAuthDetail.getConnectionDetailsVO().getAuthenticationDetails();
							if (authenticationDetail != null && authenticationDetail.getConfigurations() != null) {
								if (authenticationType.getConfigurationType().equalsIgnoreCase(Constants.SINGLE_AUTH_TYPE)) {
									for (List<JwsAuthConfiguration> configurationDetail : authenticationDetail
											.getConfigurations()) {
										if (configurationDetail != null) {
											for (JwsAuthConfiguration authConfiguration : configurationDetail) {
												if (authConfiguration!=null && authenticationType.getConfigurationType() !=null && authConfiguration.getName()!=null && authConfiguration.getValue()!=null) {
													authDetail.put(authConfiguration.getName(), authConfiguration.getValue());
													mapDetails.put(authConfiguration.getName(),	authConfiguration.getValue());
													AdditionalDetails  additionalDetails = authConfiguration.getAdditionalDetails();
													if(securityAuthDetail.getAuthenticationTypeVO().getId().equals(Constants.AuthType.LDAP.getAuthType())) {
														if(StringUtils.isNotEmpty(authConfiguration.getName()) && StringUtils.isNotEmpty(authConfiguration.getValue()) && authConfiguration.getName().equalsIgnoreCase("displayName")) {
															ldapDisplayDetails.add(authConfiguration.getValue());
															isEldapEnabled = true;
														}
													}
													if (additionalDetails != null) {
														
															List<List<JwsAuthAdditionalProperty>> authAdditionalProperties = additionalDetails.getAdditionalProperties();
															if (authAdditionalProperties != null) {
																
																for (List<JwsAuthAdditionalProperty> additionalProperties : authAdditionalProperties) {
																	if (additionalProperties != null) {
																		JwsAuthAdditionalProperty verficationType = additionalProperties.stream()
																				.filter(config -> config!=null &&  config.getName() !=null && config.getName()
																						.equals("verificationType")).findFirst().orElse(null);
																		if (verficationType != null) {
																			jwsUserLoginVO.setVerificationType(Integer.valueOf(verficationType.getValue()));
																			mapDetails.put("verificationType", verficationType.getValue());
																			if (VerificationType.TOTP.getVerificationType().equals(verficationType.getValue())) {
																				mapDetails.put("enableGoogleAuthenticator", true);
																			}
																		}
																		for (JwsAuthAdditionalProperty additionalProperty : additionalProperties) {
																			if (additionalProperty != null && additionalProperty.getName()!=null && additionalProperty.getValue()!=null) {
																				authDetail.put(additionalProperty.getName(), additionalProperty.getValue());
																				mapDetails.put(additionalProperty.getName(), additionalProperty.getValue());
																			}
																		}
																	}
																}
															}
													}
												}
											}
										}
									}
								}else if (authenticationType.getConfigurationType().equalsIgnoreCase(Constants.MULTI_AUTH_TYPE)) {
									List<String> officeRegisrationIds = new ArrayList<String>();
									if (authenticationDetail != null && authenticationDetail.getConfigurations() != null) {
										for (List<JwsAuthConfiguration> configurationDetail : authenticationDetail.getConfigurations()) {
											if (configurationDetail != null) {
												for (JwsAuthConfiguration authConfiguration : configurationDetail) {
													if(authConfiguration!=null) {
														if (authenticationType.getConfigurationType() !=null && authConfiguration.getName()!=null) {
															authDetail.put(authConfiguration.getName(), authConfiguration.getType());
															mapDetails.put(authConfiguration.getName(),	authConfiguration.getType());
														}
														List<DropDownData> dropDownDatas = authConfiguration.getDropDownData();
														if (dropDownDatas != null) {
															for (DropDownData dropDownData : dropDownDatas) {
																if (dropDownData != null && dropDownData.getSelected()!=null && dropDownData.getName()!=null) {
																	authDetail.put(dropDownData.getName(), dropDownData.getSelected());
																	mapDetails.put(dropDownData.getName(), dropDownData.getSelected());
																	List<List<JwsAuthAdditionalProperty>> dropDownAddProps = dropDownData.getAdditionalDetails().getAdditionalProperties();
																	if (dropDownAddProps != null) {
																		for (List<JwsAuthAdditionalProperty> dropAddProperties : dropDownAddProps) {
																			if (dropAddProperties != null) {
																				
																				for (JwsAuthAdditionalProperty additionalProperty : dropAddProperties) {
																					if (additionalProperty != null && additionalProperty.getName() != null && additionalProperty.getValue() != null) {
																						authDetail.put(additionalProperty.getName(), additionalProperty.getValue());
																						mapDetails.put(additionalProperty.getName(), additionalProperty.getValue());
																						if (dropDownData.getType() != null	&& dropDownData.getType().equalsIgnoreCase("office365")) {
																							if (additionalProperty.getName().equalsIgnoreCase("registration-id")) {
																								officeRegisrationIds.add(additionalProperty.getValue());
																							}
																						}
																					}
																				}
																				
																			}
																		}
																	}
																}
															}
														}
														if (authConfiguration.getAdditionalDetails() !=null) {
															AdditionalDetails  additionalDetails = authConfiguration.getAdditionalDetails();
															if (additionalDetails != null) {
																List<List<JwsAuthAdditionalProperty>> authAdditionalProperties = additionalDetails.getAdditionalProperties();
																if (authAdditionalProperties != null) {
																	for (List<JwsAuthAdditionalProperty> additionalProperties : authAdditionalProperties) {
																		if (additionalProperties != null) {
																			for (JwsAuthAdditionalProperty additionalProperty : additionalProperties) {
																				if (additionalProperty != null && additionalProperty.getName()!=null && additionalProperty.getValue()!=null) {
																					authDetail.put(additionalProperty.getName(), additionalProperty.getValue());
																					mapDetails.put(additionalProperty.getName(), additionalProperty.getValue());
																				}
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
											if (officeRegisrationIds.isEmpty() == false) {
												authDetail.put("regisrationIds", officeRegisrationIds);
												mapDetails.put("regisrationIds", officeRegisrationIds);
											}
										}
									}
								}
							}
							if(isEldapEnabled) {
								Collections.sort(ldapDisplayDetails);
								authDetail.put("ldapDisplayDetails", ldapDisplayDetails);
								mapDetails.put("ldapDisplayDetails", ldapDisplayDetails);
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
