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
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.webstarter.entities.MasterModule;
import com.trigyn.jws.webstarter.service.ExportService;
import com.trigyn.jws.webstarter.service.ImportService;
import com.trigyn.jws.webstarter.service.MasterModuleService;
import com.trigyn.jws.webstarter.xml.MetadataXMLVO;

@RestController
@RequestMapping("/cf")
public class ImportExportController {

	private final static Logger logger 						= LogManager.getLogger(ImportExportController.class);

	@Autowired
	private 	MasterModuleService 		masterModuleService = null;
	
	@Autowired
	private 	MenuService 				menuService			= null;
	
	@Autowired
	private 	ExportService				exportService	= null;

	@Autowired
	private 	ImportService				importService	= null;
	
	@GetMapping(value = "/vexp", produces = MediaType.TEXT_HTML_VALUE)
	public String viewExport(HttpServletRequest request, HttpServletResponse httpServletResponse) throws IOException  {
		try {
			Map<String, Object> vmTemplateData 	= new HashMap<>();
			List<MasterModule> moduleVOList 	= masterModuleService.getModules();
			List<Map<String, Object>> customEntities = exportService.getAllCustomEntity();
			vmTemplateData.put("moduleVOList", moduleVOList);
			vmTemplateData.put("customEntities", customEntities);
			return menuService.getTemplateWithSiteLayout("export-config", vmTemplateData);
		} catch (Exception exception) {
			logger.error("Error ", exception);
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
			return null;
		}
	}

	@RequestMapping(value ="/ecd")
	@ResponseBody
	public String exportConfigData(@RequestBody Map<String, String> map, HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
		return exportService.exportConfigData(request, httpServletResponse, map);
	}

	@RequestMapping(value ="/downloadExport",method = RequestMethod.POST)
	@ResponseBody
	public void downloadExport(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String filePath	= request.getParameter("filePath");
		exportService.downloadExport(request, response, filePath);
	}

	@GetMapping(value = "/vimp", produces = MediaType.TEXT_HTML_VALUE)
	public String viewImport(HttpServletRequest request, HttpServletResponse httpServletResponse) throws IOException  {
		try {
			Map<String, Object> vmTemplateData 	= new HashMap<>();
			return menuService.getTemplateWithSiteLayout("import-config", vmTemplateData);
		} catch (Exception exception) {
			logger.error("Error ", exception);
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
			return null;
		}
	}

	@PostMapping(value = "/impF")
	@ResponseBody
	public String importFile(HttpServletRequest request, HttpServletResponse httpServletResponse) throws IOException  {
		try {
			Part file = request.getPart("inputFile");
			Map<String, Object> map = importService.importConfig(file);
			
			MetadataXMLVO metadataXmlvo = (MetadataXMLVO) map.get("metadataVO");
			String unZipFilePath = (String) map.get("unZipFilePath");

			String jsonArray = importService.getJsonArrayFromMetadataXMLVO(metadataXmlvo);
			Map<String, Object> zipFileDataMap = importService.getXMLJsonDataMap(metadataXmlvo, unZipFilePath);
			
			zipFileDataMap.put("completeZipJsonData", jsonArray);
			
			Gson gson = new GsonBuilder().create();
		    String jsonString = gson.toJson(zipFileDataMap);
		    
			return jsonString;
		} catch (Exception exception) {
			logger.error("Error ", exception);
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
			return null;
		}
	}

	@PostMapping(value = "/glv")
	@ResponseBody
	public String getLatestVersion(HttpServletRequest request, HttpServletResponse httpServletResponse) throws IOException  {
		try {
			Gson gson = new Gson();
			String inputData = request.getParameter("imporatableData");
			JSONObject imporatableDataJson = new JSONObject(inputData);
			Map<String, Object> zipFileDataMap = new ObjectMapper().readValue(inputData, Map.class);;
			Map<String,String> versionMap = importService.getLatestVersion(zipFileDataMap);
			return gson.toJson(versionMap);
		} catch (Exception exception) {
			logger.error("Error ", exception);
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
			return null;
		}
	}

	@PostMapping(value = "/glcrc")
	@ResponseBody
	public String getLatestCRC(HttpServletRequest request, HttpServletResponse httpServletResponse) throws IOException  {
		try {
			String inputData = request.getParameter("imporatableData");
			JSONObject imporatableDataJson = new JSONObject(inputData);
			Gson gson = new Gson();
			Map<String, Object> zipFileDataMap = new ObjectMapper().readValue(inputData, Map.class);;
			Map<String, Boolean> crcMap = importService.getLatestCRC(zipFileDataMap);
			return gson.toJson(crcMap);
		} catch (Exception exception) {
			logger.error("Error ", exception);
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
			return null;
		}
	}

	@PostMapping(value = "/importConfig")
	@ResponseBody
	public String importConfig(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception  {
		String imporatableData = request.getParameter("imporatableData");
		String importId		   = request.getParameter("importId");
		String moduleType		= request.getParameter("moduleType");
		
		return importService.importConfig(imporatableData, importId, moduleType);
	}

	@PostMapping(value = "/importAll")
	@ResponseBody
	public void importAll(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception  {
		String imporatableData = request.getParameter("imporatableData");
		String importedIdList  = request.getParameter("importedIdList");

		importService.importAll(imporatableData, importedIdList);
	}

}
