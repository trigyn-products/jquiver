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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.trigyn.jws.dynarest.vo.RestApiDetails;
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.webstarter.service.HelpManualService;

@RestController
@RequestMapping("/cf")
public class HelpManualController {

	private final static Logger logger 						= LogManager.getLogger(HelpManualController.class);

	@Autowired
	private MenuService 	menuService						= null;
	
	@Autowired
	private HelpManualService helpManualService				= null;
    
    @GetMapping(value = "/help", produces = MediaType.TEXT_HTML_VALUE)
    public String manualListingPage(HttpServletResponse httpServletResponse) throws IOException  {
    	try {
    		return menuService.getTemplateWithSiteLayout("manual-type-template", new HashMap<>());
    	}catch (Exception exception) {
    		logger.error("Error while loding manual page ", exception);
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
			return null;
		}
    }
    
    @GetMapping(value = "/ehme", produces = MediaType.TEXT_HTML_VALUE)
    public String manualEntityListingPage(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException  {
    	try {
    		String manualType = httpServletRequest.getParameter("mt");
    		Map<String, Object> parameterMap = new HashMap<>();
    		parameterMap.put("mt", manualType);
    		return menuService.getTemplateWithSiteLayout("manual-entry-template", parameterMap);
    	}catch (Exception exception) {
    		logger.error("Error while loding manual page ", exception);
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
			return null;
		}
    }
    
    @PostMapping(value = "/shmt", produces = MediaType.TEXT_HTML_VALUE)
    public void saveManualType(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException  {
    	try {
    		String manualId = httpServletRequest.getParameter("manualId") == "" ? null : httpServletRequest.getParameter("manualId");
    		String name = httpServletRequest.getParameter("name");
    		helpManualService.saveManualType(manualId, name);
    	}catch (Exception exception) {
    		logger.error("Error while loding manual page ", exception);
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
		}
    }
    
    @PostMapping(value = "/smfd", produces = MediaType.APPLICATION_JSON_VALUE)
    public void saveFileManualDetails(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException  {
    	try {
    		String manualEntryId = httpServletRequest.getParameter("manualEntryId") == "" ? null : httpServletRequest.getParameter("manualEntryId");
    		String entryName = httpServletRequest.getParameter("entryName");
    		String manualId = httpServletRequest.getParameter("manualId");
    		List<String> fileIds = new Gson().fromJson(httpServletRequest.getParameter("fileIds"), List.class);
    		helpManualService.saveFileForManualEntry(manualEntryId, manualId, entryName, fileIds);
    	}catch (Exception exception) {
    		logger.error("Error while loding manual page ", exception);
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
		}
    }
    
    @PostMapping(value = "/shmd", produces = MediaType.APPLICATION_JSON_VALUE)
    public void saveManualEntryDetails(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException  {
    	try {
    		Map<String, Object> parameters = validateAndProcessRequestParams(httpServletRequest);
    		helpManualService.saveManualEntryDetails(parameters);
    	}catch (Exception exception) {
    		logger.error("Error while loding manual page ", exception);
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
		}
    }
    
    @GetMapping(value = "manual", produces = MediaType.TEXT_HTML_VALUE)
    public String showManual(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException  {
    	try {
    		String manualType = httpServletRequest.getParameter("mt");
    		Map<String, Object> parameterMap = new HashMap<>();
    		parameterMap.put("mt", manualType);
    		return menuService.getTemplateWithSiteLayout("manual-display", parameterMap);
    	}catch (Exception exception) {
    		logger.error("Error while loding manual page ", exception);
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
			return null;
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
