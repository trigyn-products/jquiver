package com.trigyn.jws.usermanagement.service;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.usermanagement.entities.JwsAuthenticationType;
import com.trigyn.jws.usermanagement.repository.JwsAuthenticationTypeRepository;
import com.trigyn.jws.usermanagement.security.config.ApplicationSecurityDetails;

@Service
public class UserConfigService {

	@Autowired
	private ApplicationSecurityDetails		applicationSecurityDetails		= null;

	@Autowired
	private JwsAuthenticationTypeRepository	authenticationTypeRepository	= null;

	@Autowired
	private PropertyMasterService			propertyMasterService			= null;

	public void getConfigurableDetails(Map<String, Object> mapDetails) throws Exception, JSONException {
		JSONObject	jsonObjectCaptcha				= null;
		JSONObject	jsonObjectRegex					= null;
		String		captchaPropertyName				= "enableCaptcha";
		String		authenticatorPropertyName		= "enableGoogleAuthenticator";
		String		regexPropertyName				= "enableRegex";
		String		enableRegistrationPropertyName	= "enableRegistration";
		if (applicationSecurityDetails.getAuthenticationType() != null) {
			Integer authType = Integer.parseInt(applicationSecurityDetails.getAuthenticationType());
			mapDetails.put("authType", String.valueOf(authType));
			JwsAuthenticationType authenticationType = authenticationTypeRepository.findById(authType)
					.orElseThrow(() -> new Exception("No auth type found with id : " + authType));
			if (StringUtils.isNotBlank(authenticationType.getAuthenticationProperties())) {
				JSONArray	jsonArray						= new JSONArray(authenticationType.getAuthenticationProperties());
				String		verificationStepPropertyName	= "enableVerificationStep";
				captchaPropertyName			= "enableCaptcha";
				authenticatorPropertyName	= "enableGoogleAuthenticator";

				jsonObjectCaptcha			= getJsonObjectFromPropertyValue(jsonObjectCaptcha, jsonArray, verificationStepPropertyName);
				if (jsonObjectCaptcha != null && jsonObjectCaptcha.getString("value").equalsIgnoreCase("true")) {
					if (jsonObjectCaptcha.get("selectedValue") != null
							&& Integer.parseInt(jsonObjectCaptcha.get("selectedValue").toString()) == 2) {
						mapDetails.put(captchaPropertyName, Boolean.FALSE);
						mapDetails.put(authenticatorPropertyName, Boolean.TRUE);
					} else if (jsonObjectCaptcha.get("selectedValue") != null
							&& Integer.parseInt(jsonObjectCaptcha.get("selectedValue").toString()) == 1) {
						mapDetails.put(captchaPropertyName, Boolean.TRUE);
						mapDetails.put(authenticatorPropertyName, Boolean.FALSE);
					} else {
						mapDetails.put(captchaPropertyName, Boolean.FALSE);
						mapDetails.put(authenticatorPropertyName, Boolean.FALSE);
					}
				} else if (authType != null && authType == 4) {
					mapDetails.put(captchaPropertyName, Boolean.FALSE);
					mapDetails.put(authenticatorPropertyName, Boolean.FALSE);
				} else {
					mapDetails.put(captchaPropertyName, Boolean.FALSE);
					mapDetails.put(authenticatorPropertyName, Boolean.FALSE);
				}
				jsonObjectRegex = getJsonObjectFromPropertyValue(jsonObjectCaptcha, jsonArray, regexPropertyName);
				if (jsonObjectRegex != null && jsonObjectRegex.getString("value").equalsIgnoreCase("true")) {
					mapDetails.put(regexPropertyName, Boolean.TRUE);
					String		propertyMasterRegex		= propertyMasterService.findPropertyMasterValue("system", "system", "regexPattern");
					JSONObject	jsonPropertyMasterRegex	= new JSONObject(propertyMasterRegex);
					mapDetails.put("expression", jsonPropertyMasterRegex.getString("expression"));
				} else {
					mapDetails.put(regexPropertyName, Boolean.FALSE);
				}

				if (applicationSecurityDetails.getAuthenticationType() != null) {

					JSONObject jsonObject = null;
					jsonObject = getJsonObjectFromPropertyValue(jsonObject, jsonArray, enableRegistrationPropertyName);
					if (jsonObject != null && jsonObject.getString("value").equalsIgnoreCase("true")) {
						mapDetails.put(enableRegistrationPropertyName, Boolean.TRUE);
					} else {
						mapDetails.put(enableRegistrationPropertyName, Boolean.FALSE);
					}

				}
			} else {
				mapDetails.put(captchaPropertyName, Boolean.FALSE);
				mapDetails.put(authenticatorPropertyName, Boolean.FALSE);
				mapDetails.put(regexPropertyName, Boolean.FALSE);
				mapDetails.put(enableRegistrationPropertyName, Boolean.FALSE);
			}
		} else {
			mapDetails.put(captchaPropertyName, Boolean.FALSE);
			mapDetails.put(authenticatorPropertyName, Boolean.FALSE);
			mapDetails.put(regexPropertyName, Boolean.FALSE);
			mapDetails.put(enableRegistrationPropertyName, Boolean.FALSE);
		}
	}

	public JSONObject getJsonObjectFromPropertyValue(JSONObject jsonObject, JSONArray jsonArray, String propertyName) throws JSONException {
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
