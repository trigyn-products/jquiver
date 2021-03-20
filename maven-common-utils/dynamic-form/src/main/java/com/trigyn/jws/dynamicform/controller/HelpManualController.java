package com.trigyn.jws.dynamicform.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dbutils.utils.Constant;
import com.trigyn.jws.dynamicform.service.HelpManualService;
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.usermanagement.security.config.Authorized;

@RestController
@RequestMapping("/cf")
public class HelpManualController {

	private final static Logger		logger					= LogManager.getLogger(HelpManualController.class);

	@Autowired
	private MenuService				menuService				= null;

	@Autowired
	private HelpManualService		helpManualService		= null;

	@Autowired
	private PropertyMasterService	propertyMasterService	= null;

	@PostMapping(value = "/ehme", produces = MediaType.TEXT_HTML_VALUE)
	@Authorized(moduleName = com.trigyn.jws.usermanagement.utils.Constants.HELPMANUAL)
	public String manualEntityListingPage(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws IOException {
		try {
			String				manualType		= httpServletRequest.getParameter("mt");
			String				manualName		= httpServletRequest.getParameter("mn");
			Map<String, Object>	parameterMap	= new HashMap<>();
			String				dbDateFormat	= propertyMasterService.getDateFormatByName(
					Constant.PROPERTY_MASTER_OWNER_TYPE, Constant.PROPERTY_MASTER_OWNER_ID,
					Constant.JWS_DATE_FORMAT_PROPERTY_NAME, Constant.JWS_JAVA_DATE_FORMAT_PROPERTY_NAME);

			parameterMap.put("mt", manualType);
			parameterMap.put("mn", manualName);
			parameterMap.put("dbDateFormat", dbDateFormat);
			return menuService.getTemplateWithSiteLayout("manual-entry-template", parameterMap);
		} catch (Exception a_exception) {
			logger.error("Error while loding manual page ", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}
	}

	@PostMapping(value = "/shmt", produces = MediaType.TEXT_HTML_VALUE)
	@Authorized(moduleName = com.trigyn.jws.usermanagement.utils.Constants.HELPMANUAL)
	public String saveManualType(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws IOException {
		try {
			String	manualId	= StringUtils.isBlank(httpServletRequest.getParameter("mt")) == true ? null
					: httpServletRequest.getParameter("mt");
			String	isEdit		= httpServletRequest.getParameter("ie");
			String	name		= httpServletRequest.getParameter("name");
			return helpManualService.saveManualType(manualId, name, isEdit);
		} catch (Exception a_exception) {
			logger.error("Error while loding manual page ", a_exception);
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
		}
		return null;
	}

	@PostMapping(value = "/smfd", produces = MediaType.APPLICATION_JSON_VALUE)
	@Authorized(moduleName = com.trigyn.jws.usermanagement.utils.Constants.HELPMANUAL)
	public void saveFileManualDetails(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws IOException {
		try {
			String			manualEntryId	= httpServletRequest.getParameter("manualEntryId") == "" ? null
					: httpServletRequest.getParameter("manualEntryId");
			String			entryName		= httpServletRequest.getParameter("entryName");
			String			manualId		= httpServletRequest.getParameter("manualId");
			List<String>	fileIds			= new Gson().fromJson(httpServletRequest.getParameter("fileIds"),
					List.class);
			helpManualService.saveFileForManualEntry(manualEntryId, manualId, entryName, fileIds);
		} catch (Exception a_exception) {
			logger.error("Error while loding manual page ", a_exception);
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
		}
	}

	@PostMapping(value = "/shmd")
	@Authorized(moduleName = com.trigyn.jws.usermanagement.utils.Constants.HELPMANUAL)
	public String saveManualEntryDetails(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws IOException {
		try {
			Map<String, Object> parameters = validateAndProcessRequestParams(httpServletRequest);
			return helpManualService.saveManualEntryDetails(parameters);
		} catch (Exception a_exception) {
			logger.error("Error while loding manual page ", a_exception);
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
		}
		return null;
	}

	@DeleteMapping(value = "/dme", produces = MediaType.APPLICATION_JSON_VALUE)
	@Authorized(moduleName = com.trigyn.jws.usermanagement.utils.Constants.HELPMANUAL)
	public void deleteManualEntryDetails(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws IOException {
		try {
			String	manualType		= httpServletRequest.getParameter("mt");
			String	manualEntryId	= httpServletRequest.getParameter("mei");
			Integer	sortIndex		= httpServletRequest.getParameter("si") == null ? 0
					: Integer.parseInt(httpServletRequest.getParameter("si"));
			helpManualService.deleteManualEntryId(manualType, manualEntryId, sortIndex);
		} catch (Exception a_exception) {
			logger.error("Error while loding manual page ", a_exception);
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
		}
	}

	private Map<String, Object> validateAndProcessRequestParams(HttpServletRequest httpServletRequest) {
		Map<String, Object> requestParams = new HashMap<>();
		for (String requestParamKey : httpServletRequest.getParameterMap().keySet()) {
			requestParams.put(requestParamKey, httpServletRequest.getParameter(requestParamKey));
		}
		return requestParams;
	}
}
