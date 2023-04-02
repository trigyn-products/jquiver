package com.trigyn.jws.webstarter.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dbutils.vo.xml.MetadataXMLVO;
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.usermanagement.entities.JwsMasterModules;
import com.trigyn.jws.webstarter.service.ExportService;
import com.trigyn.jws.webstarter.service.ImportService;
import com.trigyn.jws.webstarter.service.MasterModuleService;

@RestController
@RequestMapping("/cf")
//@PreAuthorize("hasPermission('module','Import/Export')")
public class ImportExportController {

	private final static Logger		logger					= LogManager.getLogger(ImportExportController.class);

	@Autowired
	private MasterModuleService		masterModuleService		= null;

	@Autowired
	private MenuService				menuService				= null;

	@Autowired
	private ExportService			exportService			= null;

	@Autowired
	private ImportService			importService			= null;

	@Autowired
	private PropertyMasterService	propertyMasterService	= null;

	@GetMapping(value = "/vexp", produces = MediaType.TEXT_HTML_VALUE)
	public String viewExport(HttpServletRequest request, HttpServletResponse httpServletResponse) throws IOException {
		try {
			Map<String, Object>			vmTemplateData		= new HashMap<>();
			List<JwsMasterModules>		moduleVOList		= masterModuleService.getModules();
			List<Map<String, Object>>	customEntities		= exportService.getAllCustomEntity();
			List<Map<String, Object>>	customEntityCount	= exportService.getCustomEntityCount();
			List<Map<String, Object>>	allEntityCount		= exportService.getAllEntityCount();
			vmTemplateData.put("moduleVOList", moduleVOList);
			vmTemplateData.put("customEntities", customEntities);
			vmTemplateData.put("customEntityCount", customEntityCount);
			vmTemplateData.put("allEntityCount", allEntityCount);
			vmTemplateData.put("serverProfile", propertyMasterService.findPropertyMasterValue("profile"));
			return menuService.getTemplateWithSiteLayout("export-config", vmTemplateData);
		} catch (Exception a_exception) {
			logger.error("Error occured while loading View Export page.", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}
	}

	@RequestMapping(value = "/ecd")
	@ResponseBody
	public String exportConfigData(@RequestBody Map<String, String> map, HttpServletRequest request,
		HttpServletResponse httpServletResponse) throws Exception {
		return exportService.exportConfigData(request, httpServletResponse, map, false);
	}

	@RequestMapping(value = "/downloadExport", method = RequestMethod.POST)
	@ResponseBody
	public void downloadExport(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String filePath = request.getParameter("filePath");
		exportService.downloadExport(request, response, filePath);
	}

	@GetMapping(value = "/vimp", produces = MediaType.TEXT_HTML_VALUE)
	public String viewImport(HttpServletRequest request, HttpServletResponse httpServletResponse) throws IOException {
		try {
			Map<String, Object> vmTemplateData = new HashMap<>();
			vmTemplateData.put("serverProfile", propertyMasterService.findPropertyMasterValue("profile"));
			return menuService.getTemplateWithSiteLayout("import-config", vmTemplateData);
		} catch (Exception a_exception) {
			logger.error("Error occured while loading View Import page.", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}
	}

	@PostMapping(value = "/impF")
	@ResponseBody
	public String importFile(HttpServletRequest request, HttpServletResponse httpServletResponse) throws IOException {
		try {
			Part				file			= request.getPart("inputFile");
			Map<String, Object>	map				= importService.importConfig(file, false);
	
			MetadataXMLVO		metadataXmlvo	= (MetadataXMLVO) map.get("metadataVO");
			String				unZipFilePath	= (String) map.get("unZipFilePath");
	
			String				jsonArray		= importService.getJsonArrayFromMetadataXMLVO(metadataXmlvo);
			Map<String, Object>	zipFileDataMap	= importService.getXMLJsonDataMap(metadataXmlvo, unZipFilePath);
	
			Gson	gson		= new GsonBuilder().create();
			
			zipFileDataMap.put("completeZipJsonData", jsonArray);
			Map<String, String> versionMap = importService.getLatestVersion(zipFileDataMap);
			zipFileDataMap.put("versionMap", gson.toJson(versionMap));
			
			Map<String, Boolean> crcMap = importService.getLatestCRC(zipFileDataMap);
			zipFileDataMap.put("crcMap", gson.toJson(crcMap));
			String	jsonString	= gson.toJson(zipFileDataMap);
	
			return jsonString;
		} catch (Exception a_exception) {
			logger.error("Error occured while importing file.", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			return "fail:" + a_exception.getMessage();
		}
	}

	@RequestMapping(value = "/glv")
	@ResponseBody
	public String getLatestVersion(HttpServletRequest request, HttpServletResponse httpServletResponse) throws IOException {
		try {
			Gson				gson				= new Gson();
			String				inputData			= request.getParameter("imporatableData");
			JSONObject			imporatableDataJson	= new JSONObject(inputData);
			Map<String, Object>	zipFileDataMap		= new ObjectMapper().readValue(inputData, Map.class);
			;
			Map<String, String> versionMap = importService.getLatestVersion(zipFileDataMap);
			return gson.toJson(versionMap);
		} catch (Exception a_exception) {
			logger.error("Error occured while loading latest version.", a_exception);
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}
	}

	@PostMapping(value = "/glcrc")
	@ResponseBody
	public String getLatestCRC(HttpServletRequest request, HttpServletResponse httpServletResponse) throws IOException {
		try {
			String				inputData			= request.getParameter("imporatableData");
			JSONObject			imporatableDataJson	= new JSONObject(inputData);
			Gson				gson				= new Gson();
			Map<String, Object>	zipFileDataMap		= new ObjectMapper().readValue(inputData, Map.class);
			;
			Map<String, Boolean> crcMap = importService.getLatestCRC(zipFileDataMap);
			return gson.toJson(crcMap);
		} catch (Exception exception) {
			logger.error("Error occured while loading latest CRC.", exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
			return null;
		}
	}

	@RequestMapping(value = "/importConfig", method = RequestMethod.POST)
	@ResponseBody
	public String importConfig(HttpServletRequest request, HttpServletResponse httpServletResponse) {
		try {
			String	exportedFormatObject	= request.getParameter("exportedFormatObject");
			String	importId		= request.getParameter("importId");
			String	moduleType		= request.getParameter("moduleType");

			return importService.importConfig(exportedFormatObject, importId, moduleType, false, null);
		} catch (Exception exception) {
			logger.error("Error occured while importing data : Module Type : "+ request.getParameter("moduleType")+" ImportId : " + request.getParameter("importId"), exception);
			return "fail:" + exception.getMessage();
		}
	}

	@RequestMapping(value = "/importAll", method = RequestMethod.POST)
	@ResponseBody
	public String importAll(HttpServletRequest request, HttpServletResponse httpServletResponse) {
		try {
			String	imporatableData	= request.getParameter("imporatableData");
			String	importedIdList	= request.getParameter("importedIdList");

			return importService.importAll(imporatableData, importedIdList, false);
		} catch (Exception exception) {
			logger.error("Error occured while importing all data.", exception);
			return "fail:" + exception.getMessage();
		}
	}

	@RequestMapping(value = "/etl")
	@ResponseBody
	public String exportToLocal(@RequestBody Map<String, String> map, HttpServletRequest request,
		HttpServletResponse httpServletResponse) throws Exception {
		return exportService.exportConfigData(request, httpServletResponse, map, true);
	}

	@RequestMapping(value = "/ifl")
	@ResponseBody
	public String importFromLocal(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
		try {
			Map<String, Object>	map				= importService.importConfig(null, true);
	
			MetadataXMLVO		metadataXmlvo	= (MetadataXMLVO) map.get("metadataVO");
			String				unZipFilePath	= (String) map.get("unZipFilePath");
	
			String				jsonArray		= importService.getJsonArrayFromMetadataXMLVO(metadataXmlvo);
			Map<String, Object>	zipFileDataMap	= importService.getXMLJsonDataMap(metadataXmlvo, unZipFilePath);
	
			Gson	gson		= new GsonBuilder().create();
			
			zipFileDataMap.put("completeZipJsonData", jsonArray);
			Map<String, String> versionMap = importService.getLatestVersion(zipFileDataMap);
			zipFileDataMap.put("versionMap", gson.toJson(versionMap));
			
			Map<String, Boolean> crcMap = importService.getLatestCRC(zipFileDataMap);
			zipFileDataMap.put("crcMap", gson.toJson(crcMap));
			String	jsonString	= gson.toJson(zipFileDataMap);
	
			return jsonString;
		} catch (Exception a_exception) {
			logger.error("Error occured while importing data from Local.", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			return "fail:" + a_exception.getMessage();
		}
	}

}
