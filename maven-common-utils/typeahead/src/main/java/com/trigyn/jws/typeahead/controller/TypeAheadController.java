package com.trigyn.jws.typeahead.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.trigyn.jws.typeahead.model.AutocompleteParams;
import com.trigyn.jws.typeahead.service.TypeAheadService;
import com.trigyn.jws.usermanagement.security.config.Authorized;
import com.trigyn.jws.usermanagement.utils.Constants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "Perform all autocomplete related operations.")
@RequestMapping(value = "/cf")
public class TypeAheadController {

	@Autowired
	private TypeAheadService typeAheadService = null;

	@ApiOperation(value = "Get count of autocomplete data by autocomplete id.")
	@PostMapping(value = "/autocomplete-data", produces = { MediaType.APPLICATION_JSON_VALUE })
	@Authorized(moduleName = Constants.AUTOCOMPLETE)
	public List<Map<String, Object>> getAutocompleteData(HttpServletRequest request) throws Exception {
		AutocompleteParams	autocompleteParams	= new AutocompleteParams(request);
		Map<String, Object>	requestParamMap		= request.getParameter("requestParameters") == null ? new HashMap<String, Object>()
				: new Gson().fromJson(request.getParameter("requestParameters"), Map.class);
		return typeAheadService.getAutocompleteData(autocompleteParams, requestParamMap);
	}

	@ApiOperation(value = "Get count of autocomplete data by autocomplete id")
	@GetMapping(value = "/count-data", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@Authorized(moduleName = Constants.AUTOCOMPLETE)
	public Integer getCountOfData(@RequestBody AutocompleteParams requestParams) {
		AutocompleteParams autocompleteParams = requestParams.proccessAutocompleteReqeustObject();
		return typeAheadService.getCountOfData(autocompleteParams);
	}

}
