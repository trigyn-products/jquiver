package com.trigyn.jws.menu.exception;

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

import freemarker.core.InvalidReferenceException;

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
    
    @RequestMapping("/error")
	public String errorHandler(HttpServletRequest httpServletRequest) throws Exception {
    	Object status = httpServletRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    	Exception exception = (Exception) httpServletRequest.getAttribute("javax.servlet.error.exception");
    	TemplateVO templateVO = templateService.getTemplateByName("error-page");
    	Map<String, Object> parameterMap = new HashMap<String, Object>();
    	if(status != null) {
    		Integer statusCode = Integer.parseInt(status.toString());
    		if(statusCode == HttpStatus.NOT_FOUND.value()) {
    			TemplateVO fallbackTemplate = getTemplateIfAssociatedWithURL(httpServletRequest);
    			templateVO =  fallbackTemplate == null ? templateVO : fallbackTemplate;
    			if(templateVO != null) {
    				return menuService.getTemplateWithSiteLayout(templateVO.getTemplateName(), new HashMap<>());
    			}
  			 }
        	parameterMap.put("statusCode", statusCode);
        	if(exception != null) {
        		parameterMap.put("errorMessage", "<#noparse>" + exception.getCause() + "</#noparse>");
        	} else {
        		parameterMap.put("errorMessage", "<#noparse>" + httpServletRequest.getAttribute(RequestDispatcher.ERROR_MESSAGE) + "</#noparse>");
        	}
        		
    	}
    	return menuService.getTemplateWithSiteLayout(templateVO.getTemplateName(), parameterMap);
	}

	private TemplateVO getTemplateIfAssociatedWithURL(HttpServletRequest httpServletRequest) throws Exception {
		TemplateVO templateVO 					= null;
    	String requestedURL 					= httpServletRequest.getRequestURI();
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
