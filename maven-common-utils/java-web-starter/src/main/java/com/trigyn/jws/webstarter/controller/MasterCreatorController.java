package com.trigyn.jws.webstarter.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trigyn.jws.dbutils.service.ModuleService;
import com.trigyn.jws.webstarter.service.MasterCreatorService;

@RestController
@RequestMapping("/cf")
@PreAuthorize("hasPermission('module','Master Generator')")
public class MasterCreatorController {

	private final static Logger		logger					= LogManager.getLogger(MasterCreatorController.class);

	@Autowired
	private MasterCreatorService	masterCreatorService	= null;

	@Autowired
	private ModuleService			moduleService			= null;

	@GetMapping(value = "/mg", produces = MediaType.TEXT_HTML_VALUE)
	public String masterGenertor(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
		try {
			return masterCreatorService.getModuleDetails(httpServletRequest);
		} catch (Exception a_exception) {
			logger.error("Error ", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}
	}

	@PostMapping(value = "/cm", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void createMasterModulePages(@RequestBody MultiValueMap<String, String> formData, HttpServletResponse httpServletResponse)
			throws IOException {
		try {
			Map<String, Object>	details			= masterCreatorService.initMasterCreationScript(formData);
			ObjectMapper		objectMapper	= new ObjectMapper();
			String				roleIdString	= formData.getFirst("roleIds");
			List<String>		roleIds			= objectMapper.readValue(roleIdString, List.class);
			masterCreatorService.saveEntityRolesForMasterGenerator(details, roleIds);
		} catch (Exception a_exception) {
			logger.error("Error ", a_exception);
			if (httpServletResponse.getStatus() != HttpStatus.FORBIDDEN.value()) {
				httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			}
		}
	}

	@GetMapping(value = "/mtd", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Map<String, Object>> getTableDetails(
			@RequestParam(required = true, name = "tableName") String tableName,@RequestParam(required = true, name = "dbProductID") String additionalDataSourceId,
			HttpServletResponse httpServletResponse) throws IOException {
		try {;
			return masterCreatorService.getTableDetailsByTableName(tableName, additionalDataSourceId);
		} catch (Exception a_exception) {
			logger.error("Error ", a_exception);
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
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
