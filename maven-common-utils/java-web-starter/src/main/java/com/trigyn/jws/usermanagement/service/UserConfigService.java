package com.trigyn.jws.usermanagement.service;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.trigyn.jws.dbutils.cipher.utils.CipherUtilFactory;
import com.trigyn.jws.dynamicform.dao.DynamicFormCrudDAO;
import com.trigyn.jws.dynamicform.utils.Constant;
import com.trigyn.jws.usermanagement.repository.SaltDetailsRepository;
import com.trigyn.jws.usermanagement.security.config.ApplicationSecurityDetails;
import com.trigyn.jws.usermanagement.security.config.SaltDetails;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.usermanagement.utils.Constants.VerificationType;
import com.trigyn.jws.usermanagement.vo.AdditionalDetails;
import com.trigyn.jws.usermanagement.vo.AuthenticationDetails;
import com.trigyn.jws.usermanagement.vo.JwsAuthAdditionalProperty;
import com.trigyn.jws.usermanagement.vo.JwsAuthConfiguration;
import com.trigyn.jws.usermanagement.vo.JwsAuthenticationType;
import com.trigyn.jws.usermanagement.vo.JwsUserLoginVO;
import com.trigyn.jws.usermanagement.vo.MultiAuthSecurityDetailsVO;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserConfigService {

	private final static Logger			logger						= LoggerFactory.getLogger(UserConfigService.class);

	@Autowired
	private ApplicationSecurityDetails	applicationSecurityDetails	= null;

	@Autowired
	private SaltDetailsRepository		saltDetailsRepository		= null;

	@Autowired
	private DynamicFormCrudDAO			dynamicFormDAO				= null;

	@Autowired(required = false)
	private HttpServletRequest			request						= null;

	public void getConfigurableDetails(Map<String, Object> mapDetails) throws Exception, JSONException {
		Map<String, Object> authDetail = new HashMap<>();
		if (applicationSecurityDetails.getAuthenticationDetails() != null) {
			Map<String, Object>	authenticationDetails	= applicationSecurityDetails.getAuthenticationDetails();
			Boolean				isAuthenticationEnabled	= applicationSecurityDetails.getIsAuthenticationEnabled();
			String databaseDisplayName = "databaseDisplayName";
			String ldapDisplayName = null;
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
							JwsUserLoginVO	jwsUserLoginVO		= new JwsUserLoginVO();
							List<String>	ldapDisplayDetails	= new ArrayList<>();
							boolean			isEldapEnabled		= false;
							jwsUserLoginVO.setAuthenticationType(securityAuthDetail.getAuthenticationTypeVO().getId());
							jwsUserLoginVO.setVerificationType(0);
							authDetail.put(authenticationType.getName(), authenticationType.getValue());
							mapDetails.put(authenticationType.getName(), authenticationType.getValue());
							AuthenticationDetails	authenticationDetail	= securityAuthDetail
									.getConnectionDetailsVO().getAuthenticationDetails();
							List<String>			regisrationIds			= new ArrayList<String>();
							Map<String, String>		registIds				= new HashMap<>();
							List<String>			ssoUrls					= new ArrayList<String>();
							Map<String, String>		ssoUrlMap				= new HashMap<>();
							if (authenticationDetail != null && authenticationDetail.getConfigurations() != null) {
								for (List<JwsAuthConfiguration> configurationDetail : authenticationDetail
										.getConfigurations()) {
									if (configurationDetail != null) {
										JwsAuthConfiguration	displayName		= configurationDetail.stream()
												.filter(configProperty -> configProperty != null
														&& configProperty.getName() != null
														&& configProperty.getName().equalsIgnoreCase("displayName"))
												.findAny().orElse(null);
										if(displayName != null ) {
											if (securityAuthDetail.getAuthenticationTypeVO().getId()
													.equals(Constants.AuthType.DAO.getAuthType())) {
												mapDetails.put(databaseDisplayName, displayName.getValue());
												authDetail.put(databaseDisplayName, displayName.getValue());
												jwsUserLoginVO.setDatabaseDisplayName(displayName.getValue());
											}
											if (securityAuthDetail.getAuthenticationTypeVO().getId()
													.equals(Constants.AuthType.LDAP.getAuthType())) {
												mapDetails.put(ldapDisplayName, displayName.getValue());
												authDetail.put(ldapDisplayName, displayName.getValue());
												jwsUserLoginVO.setLdapDisplayName(displayName.getValue());
											}
										}
										
										if (securityAuthDetail.getAuthenticationTypeVO().getId()
												.equals(Constants.AuthType.OAUTH.getAuthType())) {
											JwsAuthConfiguration	registrationId	= configurationDetail.stream()
													.filter(additionalProperty -> additionalProperty != null
															&& additionalProperty.getName() != null
															&& additionalProperty.getName()
																	.equalsIgnoreCase("registration-id"))
													.findAny().orElse(null);
											
											if (registrationId != null && registrationId.getValue() != null) {
												regisrationIds.add(registrationId.getValue());
												JwsAuthConfiguration imgPath = configurationDetail.stream()
														.filter(additionalProperty -> additionalProperty != null
																&& additionalProperty.getName() != null
																&& additionalProperty.getName()
																		.equalsIgnoreCase("img-path"))
														.findAny().orElse(null);

												if (imgPath != null)
													registIds.put(registrationId.getValue(), imgPath.getValue());
											}

										} else if (securityAuthDetail.getAuthenticationTypeVO().getId()
												.equals(Constants.AuthType.SAML.getAuthType())) {
											JwsAuthConfiguration idpWebSsoUrl = configurationDetail.stream()
													.filter(additionalProperty -> additionalProperty != null
															&& additionalProperty.getName() != null
															&& additionalProperty.getName()
																	.equalsIgnoreCase("idpWebSsoUrl"))
													.findAny().orElse(null);
											if (idpWebSsoUrl != null && idpWebSsoUrl.getValue() != null) {
												ssoUrls.add(idpWebSsoUrl.getValue());
												ssoUrlMap.put(idpWebSsoUrl.getName(), idpWebSsoUrl.getValue());
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
																		&& config.getName().equals("verificationType"))
																.findFirst().orElse(null);
														if (verficationType != null) {
															jwsUserLoginVO.setVerificationType(
																	Integer.valueOf(verficationType.getValue()));
															mapDetails.put("verificationType",
																	verficationType.getValue());
															mapDetails.put("authenticationType",
																	Constants.AuthType.DAO.getAuthType());
															if (VerificationType.TOTP.getVerificationType()
																	.equals(verficationType.getValue())) {
																mapDetails.put("enableGoogleAuthenticator", true);
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
								if (ssoUrls.isEmpty() == false) {
									authDetail.put("ssoUrls", ssoUrls);
									mapDetails.put("ssoUrls", ssoUrls);
									mapDetails.put("ssoUrlMap", ssoUrlMap);
									authDetail.put("ssoUrlMap", ssoUrlMap);
								}
							}
							if (isEldapEnabled) {
								Collections.sort(ldapDisplayDetails);
								authDetail.put("ldapDisplayDetails", ldapDisplayDetails);
								mapDetails.put("ldapDisplayDetails", ldapDisplayDetails);
							}
							if (mapDetails != null && mapDetails.get("previousMail") != null) {
								authDetail.put("previousMail", mapDetails.get("previousMail"));
							}
							if (mapDetails != null && mapDetails.get("prevAuthType") != null) {
								authDetail.put("prevAuthType", mapDetails.get("prevAuthType"));
							}
							jwsUserLoginVO.setLoginAttributes(authDetail);
							userLoginDetails.add(jwsUserLoginVO);
						}
					}
					if (mapDetails != null) {
						if (authDetail.get("salt") == null && mapDetails.get("salt") == null) {
							Map<String, String> saltMap = fetchSalt();
							mapDetails.put("salt", saltMap.get("salt"));
							mapDetails.put("requestId", saltMap.get("requestId"));
							authDetail.put("salt", mapDetails.get("salt"));
							authDetail.put("requestId", mapDetails.get("requestId"));
						} else if (authDetail.get("salt") == null && mapDetails.get("salt") != null) {
							authDetail.put("salt", mapDetails.get("salt"));
							authDetail.put("requestId", mapDetails.get("requestId"));
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

	public String decryptPassword(String password, String requestId)
			throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException,
			NoSuchPaddingException, Exception {
		String salt = null;
		if (requestId != null) {
			SaltDetails saltDetails = saltDetailsRepository.findByRequestId(requestId);
			if (saltDetails != null) {
				salt = saltDetails.getSalt();
			}
		}
		if(salt == null) {
			throw new UsernameNotFoundException("Login page expired. Please relogin.");
		}
		
		String	paddedKey			= padOrTruncateKey(salt, 16);
		String	decryptedPassword	= CipherUtilFactory
				.getCipherUtil(Constant.AES, Constant.ECB, Constant.PKCS5PADDING, 128)
				.decrypt(password, paddedKey, Constant.AES);
		return decryptedPassword;

	}

	public static String padOrTruncateKey(String key, int length) {
		if (key.length() < length) {
			return String.format("%-" + length + "s", key).replace(' ', '0');
		} else if (key.length() > length) {
			return key.substring(0, length);
		}
		return key;
	}

	public Map<String, String> fetchSalt() {
		Map<String, String> responseMap = new HashMap<>();
		try {
			SaltDetails saltDetails = generateAndStoreSalt();

			responseMap.put("requestId", saltDetails.getRequestId());
			responseMap.put("salt", saltDetails.getSalt());
		} catch (Exception exc) {
			logger.error("Error Occured in fetchSalt().", exc);
		}
		return responseMap;
	}

	public String getAuthType() {
		String authType;
		authType = request.getParameter("enableAuthenticationType");

		if (authType == null || authType.isEmpty() || authType.isBlank()) {
			String authTypeHeader = request.getHeader("at");
			if (authTypeHeader.equals(Constants.AuthTypeHeaderKey.DAO.getAuthTypeHeaderKey())) {
				authType = Constants.DAO_ID;
			} else if (authTypeHeader.equals(Constants.AuthTypeHeaderKey.LDAP.getAuthTypeHeaderKey())) {
				authType = Constants.LDAP_ID;
			} else if (authTypeHeader.equals(Constants.AuthTypeHeaderKey.OAUTH.getAuthTypeHeaderKey())) {
				authType = Constants.OAUTH_ID;
			}
		}
		return authType;
	}
	
	public SaltDetails generateAndStoreSalt() throws NumberFormatException, Exception {
		dynamicFormDAO.deleteOldRecords();

		String requestId = UUID.randomUUID().toString();
		String salt = RandomStringUtils.randomAlphanumeric(16);
		SaltDetails saltEntity = new SaltDetails(requestId, salt, LocalDateTime.now());

		saltDetailsRepository.save(saltEntity);

		return saltEntity;
	}
}
