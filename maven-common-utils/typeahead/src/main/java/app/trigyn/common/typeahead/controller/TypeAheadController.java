package app.trigyn.common.typeahead.controller;

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

import app.trigyn.common.typeahead.model.AutocompleteParams;
import app.trigyn.common.typeahead.service.TypeAheadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "Perform all autocomplete related operations.")
@RequestMapping(value = "/cf")
public class TypeAheadController {
    
    @Autowired
	private TypeAheadService typeAheadService = null;

    /**
	 * @param a_httpServletRequest
	 * @param a_httpServletRequestResponse
	 * @return
	 */
	@ApiOperation(value = "Get count of autocomplete data by autocomplete id.")
	@PostMapping(value = "/autocomplete-data", produces = {MediaType.APPLICATION_JSON_VALUE})
	public List<Map<String, Object>> getAutocompleteData(HttpServletRequest request) {
		AutocompleteParams autocompleteParams = new AutocompleteParams(request);
		List<Map<String, Object>> data = typeAheadService.getAutocompleteData(autocompleteParams);
		return data;
	}

	/**
	 * @param request
	 * @param response
	 * @return
	 */
	@ApiOperation(value = "Get count of autocomplete data by autocomplete id")
	@GetMapping(value = "/count-data", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
	public Integer getCountOfData(@RequestBody AutocompleteParams requestParams) {
		AutocompleteParams autocompleteParams = requestParams.proccessAutocompleteReqeustObject();
		Integer count = typeAheadService.getCountOfData(autocompleteParams);
		return count;
	}
}
