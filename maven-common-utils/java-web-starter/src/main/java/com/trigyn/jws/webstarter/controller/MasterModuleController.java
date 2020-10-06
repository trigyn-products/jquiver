package com.trigyn.jws.webstarter.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.dashboard.service.DashletService;
import com.trigyn.jws.dbutils.service.ModuleService;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.dynamicform.service.DynamicFormService;
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.webstarter.utils.Constant;

@RestController
@RequestMapping(value = "/view/**", produces = MediaType.TEXT_HTML_VALUE)
public class MasterModuleController {
	
	private final static Logger logger = LogManager.getLogger(MasterCreatorController.class);

	@Autowired
	private ModuleService moduleService 					= null;

	@Autowired
	private MenuService menuService 						= null;
	
	@Autowired
	private DashletService dashletService 					= null;
	
	@Autowired
	private IUserDetailsService userDetails 				= null;
	
	@Autowired
	private DynamicFormService dynamicFormService 			= null;

	@RequestMapping()
	public String loadModuleContent(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException
			 {
		try {	
			String moduleUrl = httpServletRequest.getRequestURI();
			moduleUrl = moduleUrl.replaceFirst("/view/", "");
			if("".equals(moduleUrl)) {
				return menuService.getTemplateWithSiteLayout("home", new HashMap<String, Object>());
			}
			Map<String, Object> moduleDetailsMap = moduleService.getModuleTargetTypeName(moduleUrl);
			Map<String, Object> parameterMap = validateAndProcessRequestParams(httpServletRequest);
			Integer targetLookupId = Integer.parseInt(moduleDetailsMap.get("targetLookupId").toString());
			String templateName = moduleDetailsMap.get("targetTypeName").toString();
			String targetTypeId = moduleDetailsMap.get("targetTypeId").toString();
			if(targetLookupId.equals(Constant.TargetLookupId.TEMPLATE.getTargetLookupId())) {
				return menuService.getTemplateWithSiteLayout(templateName, parameterMap);
			}else if(targetLookupId.equals(Constant.TargetLookupId.DASHBOARD.getTargetLookupId())){
				UserDetailsVO detailsVO = userDetails.getUserDetails();
				String userId = detailsVO.getUserId();
				String template = dashletService.getDashletUI(userId, false, targetTypeId);
				return menuService.getDashletTemplateWithLayout(template, null);
			}else if(targetLookupId.equals(Constant.TargetLookupId.DYANMICFORM.getTargetLookupId())){
				String template = dynamicFormService.loadDynamicForm(targetTypeId,parameterMap,null);
				Map<String, Object> templateMap = new HashMap<>();
				templateMap.put("formId", targetTypeId);
				return menuService.getDashletTemplateWithLayout(template, templateMap);
			}
		}catch (NullPointerException exception) {
			logger.error("Error ", exception);
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
		}catch (Exception exception) {
			logger.error("Error ", exception);
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
		}
		return null;
	}
	
	 private Map<String, Object> validateAndProcessRequestParams(HttpServletRequest httpServletRequest) {
	        Map<String, Object> requestParams = new HashMap<>();
	        for (String requestParamKey : httpServletRequest.getParameterMap().keySet()) {
	            requestParams.put(requestParamKey, httpServletRequest.getParameter(requestParamKey));
	        }
	        return requestParams;
	    }

}
