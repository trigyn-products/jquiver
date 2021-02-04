package com.trigyn.jws.typeahead.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class AutocompleteParams {

	private String				autocompleteId	= null;
	private String				searchText		= null;
	private Integer				startIndex		= Integer.MIN_VALUE;
	private Integer				pageSize		= Integer.MIN_VALUE;
	private Map<String, Object>	criteriaParams	= null;

	public AutocompleteParams(HttpServletRequest request) {
		this.searchText		= request.getParameter("searchText");
		this.autocompleteId	= request.getParameter("autocompleteId");
		this.startIndex		= request.getParameter("startIndex") == null ? -1
				: Integer.parseInt(request.getParameter("startIndex"));
		this.pageSize		= request.getParameter("pageSize") == null ? -1
				: Integer.parseInt(request.getParameter("pageSize"));
		this.criteriaParams	= request.getParameter("additionalParamaters") == null ? new HashMap<String, Object>()
				: new Gson().fromJson(request.getParameter("additionalParamaters"), Map.class);
	}

	public AutocompleteParams proccessAutocompleteReqeustObject() {
		this.criteriaParams = processCriteriaParamsForComponent(this.criteriaParams);
		return this;
	}

	private Map<String, Object> processCriteriaParamsForComponent(Map<String, Object> criteriaParams) {
		Map<String, Object> parameterMap = new HashMap<>();
		if (criteriaParams == null) {
			return new HashMap<>();
		} else {
			for (String reqParamKey : criteriaParams.keySet()) {
				Object		reqParamValue	= this.criteriaParams.get(reqParamKey) == null ? null
						: this.criteriaParams.get(reqParamKey);
				String[]	keySets			= reqParamKey.split("_");
				if (reqParamValue != null && keySets.length <= 1) {
					createParameterDetails(parameterMap, reqParamKey, reqParamValue);
				} else if (reqParamValue != null && keySets.length >= 1) {
					String listReqParamValue = this.criteriaParams.get(reqParamKey).toString();
					createInParameterDetails(parameterMap, reqParamKey, listReqParamValue);
				}
			}
		}
		return parameterMap;
	}

	private void createParameterDetails(Map<String, Object> parameterMap, String reqParamKey, Object reqParamValue) {
		Object obj = null;
		if (reqParamValue instanceof Integer) {
			obj = Long.parseLong(reqParamValue.toString());
		} else if (reqParamValue instanceof Float) {
			obj = Double.parseDouble(reqParamValue.toString());
		} else if (reqParamValue instanceof String) {
			obj = reqParamValue.toString();
		}
		parameterMap.put(reqParamKey, obj);
	}

	private void createInParameterDetails(Map<String, Object> parameterMap, String reqParamKey, String reqParamValue) {
		Object obj = reqParamValue;
		if (reqParamKey.contains("li_")) {
			List<Integer> intListParameter = new Gson().fromJson(obj.toString(), new TypeToken<List<Integer>>() {
			}.getType());
			obj = intListParameter;
		}
		if (reqParamKey.contains("ls_")) {
			List<String> strListParameter = new Gson().fromJson(obj.toString(), new TypeToken<List<String>>() {
			}.getType());
			obj = strListParameter;
		}
		parameterMap.put(reqParamKey.split("_")[1], obj);
	}

	public String getAutocompleteId() {
		return autocompleteId;
	}

	public void setAutocompleteId(String autocompleteId) {
		this.autocompleteId = autocompleteId;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public Integer getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Map<String, Object> getCriteriaParams() {
		return criteriaParams;
	}

	public void setCriteriaParams(Map<String, Object> criteriaParams) {
		this.criteriaParams = criteriaParams;
	}

}