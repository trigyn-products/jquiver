package com.trigyn.jws.webstarter.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dbutils.utils.Constant;
import com.trigyn.jws.dynarest.entities.FileUploadConfig;
import com.trigyn.jws.dynarest.service.FileUploadConfigService;
import com.trigyn.jws.dynarest.vo.FileUploadConfigVO;
import com.trigyn.jws.templating.service.MenuService;

@RestController
@RequestMapping("/cf")
@PreAuthorize("hasPermission('module','File Bin')")
public class FileController {

	private final static Logger		logger					= LogManager.getLogger(FileController.class);

	@Autowired
	private PropertyMasterService	propertyMasterService	= null;

	@Autowired
	private MenuService				menuService				= null;

	@Autowired
	private FileUploadConfigService	fileUploadConfigService	= null;

	@GetMapping(value = "/fucl", produces = MediaType.TEXT_HTML_VALUE)
	public String fileUploadConfigListing(HttpServletResponse httpServletResponse) throws IOException {
		try {
			Map<String, Object>	modelMap	= new HashMap<>();
			String				environment	= propertyMasterService.findPropertyMasterValue("system", "system", "profile");
			modelMap.put("environment", environment);
			return menuService.getTemplateWithSiteLayout("file-upload-config-listing", modelMap);
		} catch (Exception a_exception) {
			logger.error("Error occured while loading File Upload Config Listing page.", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}
	}

	@PostMapping(value = "/fuj")
	public String getLastUpdatedFileJsonData(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse)
			throws Exception {
		String entityId = a_httpServletRequest.getParameter("entityId");
		return fileUploadConfigService.getFileUploadJson(entityId);
	}

	@PostMapping(value = "/sfuc")
	public void saveFileUploadConfig(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse) throws Exception {
		ObjectMapper		objectMapper		= new ObjectMapper();
		/** Added for parsing the date */
		String dbDateFormat = propertyMasterService.getDateFormatByName(Constant.PROPERTY_MASTER_OWNER_TYPE,
				Constant.PROPERTY_MASTER_OWNER_ID, Constant.JWS_DATE_FORMAT_PROPERTY_NAME,
				Constant.JWS_JAVA_DATE_FORMAT_PROPERTY_NAME);
		DateFormat dateFormat = new SimpleDateFormat(dbDateFormat);
		objectMapper.setDateFormat(dateFormat);
		/** Ends Here */
		String				modifiedContent		= a_httpServletRequest.getParameter("modifiedContent");
		FileUploadConfigVO	fileUploadConfigVO	= objectMapper.readValue(modifiedContent, FileUploadConfigVO.class);
		FileUploadConfig	fileUploadConfig	= fileUploadConfigService.convertFileUploadVOToEntity(fileUploadConfigVO);
		fileUploadConfigService.saveFileUploadConfig(fileUploadConfig);
	}

	@PostMapping(value = "/gqc")
	public String getQueryContent(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse) throws Exception {
		String	fileBinId	= a_httpServletRequest.getParameter("fileBinId");
		Gson	gson		= new Gson();
		return gson.toJson(fileUploadConfigService.getFileUploadConfigByBinId(fileBinId));
	}

}
