package com.trigyn.jws.dynarest.service;

import java.util.HashMap;
import java.util.Map;

import com.trigyn.jws.dbutils.vo.UserDetailsVO;

public class DynaRest {
	/**
	 * 
	 * Method to get employee details
	 *
	 */
	public Map<String, Object> getEmployeeDetails(Map<String, Object> parameters, Map<String, Object> dAOparameters,
			UserDetailsVO userDetails) {
		Map<String, Object> response = new HashMap<>();
		response.put("response", parameters.get("employees"));
		return response;
	}

	/**
	 * 
	 * Method to get dynamic rest details
	 *
	 */
	public Map<String, Object> getDynamicRestDetails(Map<String, Object> parameters, Map<String, Object> dAOparameters,
			UserDetailsVO userDetails) {
		Map<String, Object> response = new HashMap<>();
		response.put("methodTypes", dAOparameters.get("dynarestMethodType"));
		response.put("producerDetails", dAOparameters.get("dynarestProducerDetails"));
		response.put("dynarestDetails", dAOparameters.get("dynarestDetails"));
		return response;
	}

	/**
	 * 
	 * Method to get dynamic rest details
	 *
	 */
	public Map<String, Object> defaultTemplates(Map<String, Object> parameters, Map<String, Object> dAOparameters,
			UserDetailsVO userDetails) {
		Map<String, Object> response = new HashMap<>();
		response.put("defaultTemplates", dAOparameters.get("defaultTemplates"));
		return response;
	}
}
