package com.trigyn.jws.webstarter.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.utils.ActivityLog;
import com.trigyn.jws.dbutils.utils.IMonacoSuggestion;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.dynamicform.entities.DynamicForm;
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.typeahead.dao.TypeAheadRepository;
import com.trigyn.jws.typeahead.entities.Autocomplete;
import com.trigyn.jws.typeahead.model.AutocompleteVO;
import com.trigyn.jws.typeahead.service.TypeAheadService;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.webstarter.utils.Constant;

@RestController
@RequestMapping("/cf")
@PreAuthorize("hasPermission('module','TypeAhead Autocomplete')")
public class TypeAheadCrudController {

	private final static Logger logger = LogManager.getLogger(TypeAheadCrudController.class);

	@Autowired
	private TypeAheadService typeAheadService = null;

	@Autowired
	private MenuService menuService = null;

	@Autowired
	private ActivityLog activitylog = null;

	@Autowired
	private IUserDetailsService userDetailsService = null;

	@Autowired
	private TypeAheadRepository typeAheadRepository = null;

	@GetMapping(value = "/adl", produces = MediaType.TEXT_HTML_VALUE)
	public String autocompleteListingsPage(HttpServletResponse httpServletResponse) throws IOException {
		try {
			return menuService.getTemplateWithSiteLayout("autocomplete-listing", new HashMap<>());
		} catch (Exception a_exception) {
			logger.error("Error occured while loading Autocomplete Listing Page", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}
	}

	@GetMapping(value = "/da", produces = MediaType.TEXT_HTML_VALUE)
	public String demoAutocomplete(HttpServletResponse httpServletResponse) throws Exception {
		try {
			return menuService.getTemplateWithSiteLayout("autocomplete-demo", new HashMap<>());
		} catch (Exception a_exception) {
			logger.error("Error occured while loading Autocomplete Demo.", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}
	}

	@GetMapping(value = "/aea", produces = MediaType.TEXT_HTML_VALUE)
	public String addEditAutocompleteDetails(HttpServletRequest request, HttpServletResponse httpServletResponse)
			throws IOException {

		try {
			String autocompleteId = request.getParameter("acId");
			Map<String, Object> templateData = new HashMap<>();
			Autocomplete autocomplete = new Autocomplete();
			if (StringUtils.isBlank(autocompleteId) == false) {

				AutocompleteVO autocompleteVO = typeAheadService.getAutocompleteDetailsId(autocompleteId);
				templateData.put("autocompleteVO", autocompleteVO);
				Optional<Autocomplete> autocompleteOptional = typeAheadRepository.findById(autocompleteId);
				autocomplete = autocompleteOptional.get();
				Integer typeSelect = autocomplete.getAcTypeId();
				/* Method called for implementing Activity Log */
				logActivity(autocompleteId, typeSelect, autocompleteVO.getAutocompleteId());

			}
			/* ContextPath Suggestion in Monaco Editor */
			String contextSuggestions = IMonacoSuggestion.getTemplateSuggestion();
			templateData.put("suggestions", contextSuggestions);
			// else {
			// List<String> tableNameList = typeAheadService.getAllTablesListInSchema();
			// templateData.put("tableNameList", tableNameList);
			// }
			return menuService.getTemplateWithSiteLayout("autocomplete-manage-details", templateData);
		} catch (Exception a_exception) {
			logger.error("Error occured in TypeAhead/Autocomplete :  AutocompleteId : "+request.getParameter("acId"), a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}
	}

	/**
	 * Purpose of this method is to log activities</br>
	 * in TypeAhead/Autocomplete Module.
	 * 
	 * @author Bibhusrita.Nayak
	 * @param entityName
	 * @param typeSelect
	 * @param autocompleteId
	 * @throws Exception
	 */
	private void logActivity(String autocompleteId, Integer typeSelect, String entityName) throws Exception {
		if (StringUtils.isBlank(autocompleteId) == false) {
			Date activityTimestamp = new Date();
			Map<String, String> requestParams = new HashMap<>();
			UserDetailsVO detailsVO = userDetailsService.getUserDetails();
			String masterModuleType = Constants.Modules.AUTOCOMPLETE.getModuleName();
			requestParams.put("entityName", entityName);
			requestParams.put("masterModuleType", masterModuleType);
			requestParams.put("userName", detailsVO.getUserName());
			requestParams.put("message", "");
			requestParams.put("date", activityTimestamp.toString());
			requestParams.put("action", Constants.Action.OPEN.getAction());
			if (typeSelect == Constants.Changetype.CUSTOM.getChangeTypeInt()) {
				requestParams.put("typeSelect", Constants.Changetype.CUSTOM.getChangetype());
			} else {
				requestParams.put("typeSelect", Constants.Changetype.SYSTEM.getChangetype());
			}
			activitylog.activitylog(requestParams);
		}
	}

	@PostMapping(value = "/sacd", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ResponseBody
	public String saveAutocompleteDetails(@RequestBody MultiValueMap<String, String> formDataMap) throws Exception {
		return typeAheadService.saveAutocompleteDetails(formDataMap, Constant.MASTER_SOURCE_VERSION_TYPE);
	}

	@PostMapping(value = "/uad")
	public void updateAutocompleteDataSource(HttpServletRequest httpServletRequest) throws Exception {
		String autocompleteId = httpServletRequest.getParameter("autocompleteId");
		String additionalDataSourceId = httpServletRequest.getParameter("additionalDataSourceId");
		typeAheadService.updateAutocompleteDataSource(autocompleteId, additionalDataSourceId);
	}

	@PostMapping(value = "/cnbtn", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Map<String, Object>> getColumnNamesByTableName(HttpServletRequest httpServletRequest) throws Exception {
		String tableName = httpServletRequest.getParameter("tableName");
		String additionalDataSourceId = httpServletRequest.getParameter("additionalDataSourceId");
		return typeAheadService.getColumnNamesByTableName(additionalDataSourceId, tableName);
	}

}
