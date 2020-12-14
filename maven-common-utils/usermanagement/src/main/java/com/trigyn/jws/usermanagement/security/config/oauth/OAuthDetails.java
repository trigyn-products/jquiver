package com.trigyn.jws.usermanagement.security.config.oauth;


import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trigyn.jws.usermanagement.entities.JwsAuthenticationType;
import com.trigyn.jws.usermanagement.repository.JwsAuthenticationTypeRepository;
import com.trigyn.jws.usermanagement.utils.Constants;

@Component
public class OAuthDetails {

	
	private String oAuthClient = null;
	
	private String oAuthClientId = null;
	
	private String oAuthClientSecret = null;
	
	
	@Autowired
	private JwsAuthenticationTypeRepository authenticationTypeRepository = null;
	
	public OAuthDetails(JwsAuthenticationTypeRepository authenticationTypeRepository) throws JSONException {
		JwsAuthenticationType oAuthType = authenticationTypeRepository.findById(Constants.AuthType.OAUTH.getAuthType()).get();
		JSONObject jsonObject = null ;
		if(oAuthType.getAuthenticationProperties()!=null) {
			JSONArray jsonArray = new JSONArray(oAuthType.getAuthenticationProperties());
			jsonObject = getActiveOAuthJsonObjectFromPropertyValue(jsonObject,jsonArray);
			oAuthClient = jsonObject.getString("name");
			oAuthClientId = jsonObject.getString("client-id");
			oAuthClientSecret = jsonObject.getString("client-secret");
		}	
		
	}
	
	 public JSONObject getActiveOAuthJsonObjectFromPropertyValue(JSONObject jsonObject, JSONArray jsonArray)
				throws JSONException {
		 String propertyName= "selected";
			for (int i = 0; i < jsonArray.length(); i++) {
				 jsonObject = jsonArray.getJSONObject(i);
				if(jsonObject.get(propertyName).toString().equalsIgnoreCase("true") ) {
					break;
				}else {
					jsonObject = null ;
				}
			}
			return jsonObject;
		}

	public String getOAuthClient() {
		return oAuthClient;
	}

	public String getOAuthClientId() {
		return oAuthClientId;
	}

	public String getOAuthClientSecret() {
		return oAuthClientSecret;
	}
	 
	 
}
