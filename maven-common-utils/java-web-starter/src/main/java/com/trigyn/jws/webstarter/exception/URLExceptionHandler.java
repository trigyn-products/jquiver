package com.trigyn.jws.webstarter.exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.dbutils.repository.IModuleListingRepository;
import com.trigyn.jws.dbutils.repository.ModuleDAO;
import com.trigyn.jws.dbutils.vo.ModuleDetailsVO;
import com.trigyn.jws.templating.service.DBTemplatingService;
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.templating.vo.TemplateVO;
import com.trigyn.jws.webstarter.controller.MasterModuleController;

@RestController
public class URLExceptionHandler implements ErrorController {

    @Autowired
    private DBTemplatingService templateService 				= null;

    @Autowired
    private IModuleListingRepository iModuleListingRepository	= null; 
    
	@Autowired
	private ModuleDAO moduleDAO									= null;
	
	@Autowired
	private MenuService		menuService							= null;
	
	@Autowired 
	private MasterModuleController masterModuleController 		= null;
	
	private final static String HOME_PAGE_MODULE = "home-module";
    
    @RequestMapping("/error")
	public String errorHandler(HttpServletRequest httpServletRequest) throws Exception {
    	Object status = httpServletRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    	Exception exception = (Exception) httpServletRequest.getAttribute("javax.servlet.error.exception");
    	TemplateVO templateVO = templateService.getTemplateByName("error-page");
    	Map<String, Object> parameterMap = new HashMap<String, Object>();
    	if(status != null) {
    		Integer statusCode = Integer.parseInt(status.toString());
    		parameterMap.put("statusCode", statusCode);
    		String url = httpServletRequest.getAttribute(RequestDispatcher.ERROR_REQUEST_URI).toString().replace(httpServletRequest.getContextPath(), "");
    		if(statusCode == HttpStatus.NOT_FOUND.value()) {
    			if( "/".equals(url) ) {
    				url = HOME_PAGE_MODULE;
        		}
    			String fallbackTemplate = masterModuleController.loadTemplate(httpServletRequest, url);
    			if(fallbackTemplate == null) {
    				return menuService.getTemplateWithSiteLayout(templateVO.getTemplateName(), parameterMap);
    			} else {
    				return fallbackTemplate;
    			}
  			 }
        	if(exception != null) {
        		parameterMap.put("errorMessage", "<#noparse>" + exception.getCause() + "</#noparse>");
        	} else {
        		parameterMap.put("errorMessage", "<#noparse>" + httpServletRequest.getAttribute(RequestDispatcher.ERROR_MESSAGE) + "</#noparse>");
        	}
        		
    	}
    	return menuService.getTemplateWithSiteLayout(templateVO.getTemplateName(), parameterMap);
	}

	private TemplateVO getTemplateIfAssociatedWithURL(String requestedURL) throws Exception {
		TemplateVO templateVO 					= null;
    	ModuleDetailsVO moduleDetailsVO 		= iModuleListingRepository.getTargetTypeDetails(requestedURL);
    	
		if(moduleDetailsVO != null) {
			List<Map<String, Object>> targetTypeMapList = new ArrayList<>();
			targetTypeMapList = moduleDAO.findTargetTypeDetails(moduleDetailsVO.getTargetLookupId(), moduleDetailsVO.getTargetTypeId());
			if(!CollectionUtils.isEmpty(targetTypeMapList)) {
	        	String templateName = targetTypeMapList.get(0).get("targetTypeName").toString();
	        	templateVO = templateService.getTemplateByName(templateName);
			}
		}
		return templateVO;
	}

	@Override
	public String getErrorPath() {
		return null;
	}

}
