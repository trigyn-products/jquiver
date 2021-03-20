package com.trigyn.jws.webstarter.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.trigyn.jws.dashboard.service.DashletService;
import com.trigyn.jws.dbutils.service.ModuleService;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.dynamicform.service.DynamicFormService;
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.usermanagement.security.config.Authorized;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.webstarter.utils.Constant;

@RestController
@RequestMapping(value = "/view/**", produces = MediaType.TEXT_HTML_VALUE)
public class MasterModuleController {

	private final static Logger				logger				= LogManager.getLogger(MasterCreatorController.class);

	@Autowired
	private ModuleService					moduleService		= null;

	@Autowired
	private MenuService						menuService			= null;

	@Autowired
	private DashletService					dashletService		= null;

	@Autowired
	private IUserDetailsService				userDetails			= null;

	@Autowired
	private DynamicFormService				dynamicFormService	= null;

	@Autowired
	private RequestMappingHandlerMapping	handlerMapping		= null;

	@Autowired
	private ApplicationContext				applicationContext	= null;

	@RequestMapping()
	@Authorized(moduleName = Constants.SITELAYOUT)
	public String loadModuleContent(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws IOException {
		try {
			String moduleUrl = httpServletRequest.getRequestURI()
					.substring(httpServletRequest.getContextPath().length());
			moduleUrl = moduleUrl.replaceFirst("/view/", "");
			if (moduleUrl.indexOf("/") != -1) {
				moduleUrl = moduleUrl.substring(0, moduleUrl.indexOf("/"));
			}
			return loadTemplate(httpServletRequest, moduleUrl);
		} catch (Exception a_exception) {
			logger.error("Error ", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
		}
		return null;
	}

	public String loadTemplate(HttpServletRequest httpServletRequest, String moduleUrl) throws Exception {
		StringBuilder queryString = new StringBuilder();
		if (StringUtils.isNotBlank(httpServletRequest.getQueryString())) {
			queryString.append("?").append(httpServletRequest.getQueryString());
		}
		if ("".equals(moduleUrl)) {
			return menuService.getTemplateWithSiteLayout("home", new HashMap<String, Object>());
		}
		Map<String, Object> moduleDetailsMap = getModuleDetails(moduleUrl, httpServletRequest);
		if (CollectionUtils.isEmpty(moduleDetailsMap) == true) {
			StringBuilder moduleUrlWithParam = new StringBuilder(moduleUrl).append(queryString);
			moduleDetailsMap = moduleService.getModuleTargetByURL(moduleUrlWithParam.toString());
		}
		if (CollectionUtils.isEmpty(moduleDetailsMap) == false) {
			Map<String, Object>	parameterMap		= validateAndProcessRequestParams(httpServletRequest);
			List<String>		pathVariableList	= getPathVariables(httpServletRequest);
			if (CollectionUtils.isEmpty(pathVariableList) == false) {
				pathVariableList.remove(0);
			}
			parameterMap.put("pathVariableList", pathVariableList);
			Integer	targetLookupId	= Integer.parseInt(moduleDetailsMap.get("targetLookupId").toString());
			String	templateName	= moduleDetailsMap.get("targetTypeName").toString();
			String	targetTypeId	= moduleDetailsMap.get("targetTypeId").toString();
			if (targetLookupId.equals(Constant.TargetLookupId.TEMPLATE.getTargetLookupId())) {
				return menuService.getTemplateWithSiteLayout(templateName, parameterMap);
			} else if (targetLookupId.equals(Constant.TargetLookupId.DASHBOARD.getTargetLookupId())) {
				Map<String, Object>	templateMap	= new HashMap<>();
				UserDetailsVO		detailsVO	= userDetails.getUserDetails();
				String				userId		= detailsVO.getUserId();
				List<String>		roleIdList	= detailsVO.getRoleIdList();
				templateMap.put("templateName", templateName);
				String template = dashletService.getDashletUI(userId, false, targetTypeId, roleIdList, false);
				return menuService.getDashletTemplateWithLayout(template, templateMap);
			} else if (targetLookupId.equals(Constant.TargetLookupId.DYANMICFORM.getTargetLookupId())) {
				String				template	= dynamicFormService.loadDynamicForm(targetTypeId, parameterMap, null);
				Map<String, Object>	templateMap	= new HashMap<>();
				templateMap.put("formId", targetTypeId);
				return template;
			} else if (targetLookupId.equals(Constant.TargetLookupId.MODELANDVIEW.getTargetLookupId())) {
				List<?> systemUrls = handlerMapping.getHandlerMethods().keySet().stream()
						.map(requestMappingInfo -> requestMappingInfo.getPatternsCondition())
						.collect(Collectors.toList());
				return null;
			}
		} else {
			return null;
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

	private Map<String, Object> getModuleDetails(String requestUrl, HttpServletRequest httpServletRequest)
			throws Exception {
		Map<String, Object>			moduleDetailsMap	= new HashMap<>();

		StringBuilder				moduleUrl			= new StringBuilder();
		List<String>				pathVariableList	= getPathVariables(httpServletRequest);

		List<Map<String, Object>>	moduleDetailsList	= moduleService.getModuleTargetTypeURL(requestUrl);
		if (CollectionUtils.isEmpty(moduleDetailsList) == false) {

			for (String pathVariable : pathVariableList) {
				if (StringUtils.isBlank(moduleUrl) == false) {
					moduleUrl.append("/");
				}
				moduleUrl.append(pathVariable);
				moduleUrl.append("/**");
				for (Map<String, Object> moduleDetailsMapDB : moduleDetailsList) {
					String moduleUrlDB = (String) moduleDetailsMapDB.get("moduleUrl");
					if (StringUtils.isBlank(moduleUrlDB) == false && moduleUrlDB.equals(moduleUrl.toString())) {
						moduleDetailsMap.putAll(moduleDetailsMapDB);
						break;
					}
				}
				moduleUrl.delete(moduleUrl.indexOf("/**"), moduleUrl.length());
			}

			if (CollectionUtils.isEmpty(pathVariableList) == true) {
				moduleDetailsMap.putAll(moduleDetailsList.get(0));
			}

		}
		return moduleDetailsMap;
	}

	private List<String> getPathVariables(HttpServletRequest httpServletRequest) {
		List<String>	pathVariableList	= new ArrayList<>();
		String			moduleUrl			= httpServletRequest.getRequestURI()
				.substring(httpServletRequest.getContextPath().length());
		moduleUrl = moduleUrl.replaceFirst("/view/", "");

		if (moduleUrl.indexOf("/") != -1) {
			pathVariableList = Stream.of(moduleUrl.split("/")).map(urlElement -> new String(urlElement))
					.collect(Collectors.toList());
		}
		return pathVariableList;
	}

}
