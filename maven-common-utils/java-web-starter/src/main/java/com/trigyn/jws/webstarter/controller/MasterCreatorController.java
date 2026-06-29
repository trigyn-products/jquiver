package com.trigyn.jws.webstarter.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trigyn.jws.dbutils.utils.CustomStopException;
import com.trigyn.jws.dbutils.utils.FileUtilities;
import com.trigyn.jws.templating.service.ModuleService;
import com.trigyn.jws.typeahead.service.TypeAheadService;
import com.trigyn.jws.webstarter.service.MasterCreatorService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/cf")
@PreAuthorize("hasPermission('module','Master Generator')")
public class MasterCreatorController {

	private final static Logger logger = LoggerFactory.getLogger(MasterCreatorController.class);

	@Autowired
	private MasterCreatorService masterCreatorService = null;

	@Autowired
	private ModuleService moduleService = null;

	@Autowired
	private FileUtilities fileUtilities = null;

	@Autowired
	protected JdbcTemplate jdbcTemplate = null;

	@Autowired
	TypeAheadService typeAheadService = null;

	@GetMapping(value = "/mg", produces = MediaType.TEXT_HTML_VALUE)
	public String masterGenertor(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws IOException, CustomStopException {
		try {
			return masterCreatorService.getModuleDetails(httpServletRequest);
		} catch (CustomStopException custStopException) {
			logger.error("Error occured while loading Master Genertor page.", custStopException);
			throw custStopException;
		} catch (Exception a_exception) {
			logger.error("Error occured while loading Master Genertor page.", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			fileUtilities.customSendError(httpServletResponse, HttpStatus.INTERNAL_SERVER_ERROR.value(),
					a_exception.getMessage());
			return null;
		}
	}

	@PostMapping(value = "/cm", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void createMasterModulePages(@RequestBody MultiValueMap<String, String> formData,
			HttpServletResponse httpServletResponse) throws IOException, CustomStopException {
		try {
			ObjectMapper		objectMapper	= new ObjectMapper();
			String				roleIdString	= formData.getFirst("roleIds");
			List<String>		roleIds			= objectMapper.readValue(roleIdString, List.class);
			Map<String, Object>	details			= masterCreatorService.initMasterCreationScript(formData,roleIds);
			masterCreatorService.saveEntityRolesForMasterGenerator(details, roleIds);
			String	businessModuleName	= null;
			String	busModIdString		= null;
			// Create method for saving Business Module
			if (formData.getFirst("businessModule") != null && !formData.getFirst("businessModule").isEmpty()) {
				businessModuleName = formData.getFirst("businessModule");
			}
			if(formData.getFirst("busModIds") != null && !formData.getFirst("busModIds").isEmpty()) {
				busModIdString = formData.getFirst("busModIds");
			}
			List<String>	busModIds						= objectMapper.readValue(busModIdString, List.class);
			String			isShowCreateLinkChkBoxChecked	= formData.getFirst("isShowCreateLinkChkBox");
			String foreignKeyDetails = formData.getFirst("foreignKeyDetails");
			if (businessModuleName != null || busModIds.isEmpty() != true) {
				masterCreatorService.saveBusinessModulesForMasterGenerator(details, businessModuleName, busModIds,
						isShowCreateLinkChkBoxChecked,foreignKeyDetails);
			}
		} catch (CustomStopException custStopException) {
			logger.error("Error occured while loading Master Genertor page.", custStopException);
			throw custStopException;
		} catch (Exception exception) {
			logger.error("Error occured while saving Master Module (formData: {})" + formData, exception);
			if (null != exception.getMessage()
					&& exception.getMessage().equalsIgnoreCase(HttpStatus.PRECONDITION_FAILED.toString())) {
				fileUtilities.customSendError(httpServletResponse, HttpStatus.PRECONDITION_FAILED.value(),
						"File Bin already exist");
			} else if (httpServletResponse.getStatus() != HttpStatus.FORBIDDEN.value()) {
				fileUtilities.customSendError(httpServletResponse, HttpStatus.INTERNAL_SERVER_ERROR.value(),
						exception.getMessage());
			}
		}
	}
	
	@PostMapping(value = "/gav")
	@ResponseBody
	public String getAutocompleteValue(@RequestParam(required = false, name="tableName") String tableName,
			@RequestParam(required = false, name="idColumn") String idColumn, @RequestParam(required = false, name="displayColumn") String displayColumn,
			@RequestParam("idValue") String idValue, @RequestParam("mode") String mode,
			@RequestParam(required = false, name="autocompleteId") String autocompleteId) throws Exception {

		return masterCreatorService.getAutocompleteValue(tableName, idColumn, displayColumn, idValue, mode,
				autocompleteId);
	}

@GetMapping("/getAutocompleteList")
public List<Map<String, Object>> getAutocompleteList() {
	return typeAheadService.getAutocompleteList();
}

	@GetMapping(value = "/mtd", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Map<String, Object>> getTableDetails(
			@RequestParam(required = true, name = "tableName") String tableName,
			@RequestParam(required = true, name = "dbProductID") String additionalDataSourceId,
			HttpServletResponse httpServletResponse) throws Exception {
		try {
			List<Map<String, Object>> masterList = masterCreatorService.getTableDetailsByTableName(tableName,
					additionalDataSourceId);
			return masterList;
		} catch (Exception a_exception) {
			logger.error("Error occured while loading table details : TableName : " + tableName, a_exception);
			fileUtilities.customSendError(httpServletResponse, HttpStatus.INTERNAL_SERVER_ERROR.value(),
					a_exception.getMessage());
			return null;
		}
	}

	@GetMapping(value = "/vmsd", produces = { MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> getExistingData(@RequestHeader(name = "module-name", required = false) String moduleName,
			@RequestHeader(name = "parent-module-id", required = false) String parentModuleId,
			@RequestHeader(name = "module-url", required = false) String moduleURL) throws Exception {
		return moduleService.getExistingModuleData(null, moduleName, parentModuleId, null, moduleURL);
	}
}
