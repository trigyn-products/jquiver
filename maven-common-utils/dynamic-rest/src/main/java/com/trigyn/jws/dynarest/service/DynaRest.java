package com.trigyn.jws.dynarest.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.trigyn.jws.dbutils.vo.UserDetailsVO;

public class DynaRest {

	/**
	 * 
	 * Method to get dynamic rest details
	 *
	 */
	public Map<String, Object> getDynamicRestDetails(HttpServletRequest a_httpServletRequest,
			Map<String, Object> dAOparameters, UserDetailsVO userDetails) {
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
	public Map<String, Object> defaultTemplates(HttpServletRequest a_httpServletRequest,
			Map<String, Object> dAOparameters, UserDetailsVO userDetails) {
		Map<String, Object> response = new HashMap<>();
		response.put("defaultTemplates", dAOparameters.get("defaultTemplates"));
		return response;
	}

	public Map<String, Object> insertData(HttpServletRequest a_httpServletRequest,
			Map<String, Object> requestParameters, Map<String, Object> daoResultSets, UserDetailsVO userDetails) {
		return daoResultSets;
	}

	public Map<String, Object> asdsa(HttpServletRequest a_httpServletRequest, Map requestParameters, Map daoResultSets,
			UserDetailsVO userDetails) {
		return null;
	}
}
