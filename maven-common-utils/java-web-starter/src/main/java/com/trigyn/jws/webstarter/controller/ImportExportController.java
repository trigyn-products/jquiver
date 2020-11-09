package com.trigyn.jws.webstarter.controller;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.webstarter.entities.MasterModule;
import com.trigyn.jws.webstarter.service.ExportService;
import com.trigyn.jws.webstarter.service.MasterModuleService;

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
	
	@GetMapping(value = "/vexp", produces = MediaType.TEXT_HTML_VALUE)
	public String viewExport(HttpServletRequest request, HttpServletResponse httpServletResponse) throws IOException  {
		try {
			Map<String, Object> vmTemplateData 	= new HashMap<>();
			List<MasterModule> moduleVOList 	= masterModuleService.getModules();
			vmTemplateData.put("moduleVOList", moduleVOList);
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

}
