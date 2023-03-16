package com.trigyn.jws.webstarter.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.google.gson.Gson;
import com.trigyn.jws.dashboard.service.DashletService;
import com.trigyn.jws.dbutils.service.ModuleService;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.dynamicform.service.DynamicFormService;
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.usermanagement.entities.JwsMasterModules;
import com.trigyn.jws.usermanagement.repository.JwsMasterModulesRepository;
import com.trigyn.jws.webstarter.utils.Constant;

@Service
@Transactional
public class MasterModuleService {

	private final static Logger			logger						= LogManager.getLogger(MasterModuleService.class);

	@Autowired
	private ModuleService				moduleService				= null;

	@Autowired
	private MenuService					menuService					= null;

	@Autowired
	private DashletService				dashletService				= null;

	@Autowired
	private IUserDetailsService			userDetails					= null;

	@Autowired
	private DynamicFormService			dynamicFormService			= null;

	@Autowired
	private JwsMasterModulesRepository	jwsMasterModulesRepository	= null;

	@Autowired
	private SessionLocaleResolver		sessionLocaleResolver		= null;

	public List<JwsMasterModules> getModules() {

		List<JwsMasterModules> masterModules = new ArrayList<>();
		masterModules = jwsMasterModulesRepository.findAllModulesForImportExport(1);

		return masterModules;
	}

	public String loadTemplate(HttpServletRequest httpServletRequest, String moduleUrl,
			HttpServletResponse httpServletResponse) {
		try {
			StringBuilder queryString = new StringBuilder();
			if (StringUtils.isNotBlank(httpServletRequest.getQueryString())) {
				queryString.append("?").append(httpServletRequest.getQueryString());
			}
			if ("".equals(moduleUrl)) {
				return menuService.getTemplateWithSiteLayout("control-panel", new HashMap<String, Object>());
			}
			Map<String, Object> moduleDetailsMap = getModuleDetails(moduleUrl, httpServletRequest);
			if (CollectionUtils.isEmpty(moduleDetailsMap) == true) {
				StringBuilder moduleUrlWithParam = new StringBuilder(moduleUrl).append(queryString);
				moduleDetailsMap = moduleService.getModuleTargetByURL(moduleUrlWithParam.toString());
			}

			Map<String, String> requestMap = new HashMap<String, String>();
			if (moduleDetailsMap != null && moduleDetailsMap.containsKey("requestParamJson")
					&& moduleDetailsMap.get("requestParamJson") != null) {
				String	requestParam	= String.valueOf(moduleDetailsMap.get("requestParamJson"));

				Gson	g				= new Gson();
				if (requestParam != null && requestParam.isEmpty() == false) {
					Map<String, String> requestBodyMap = g.fromJson(requestParam.toString(), Map.class);
					if (requestBodyMap != null && requestBodyMap.isEmpty() == false) {
						for (Entry<String, String> entry : requestBodyMap.entrySet()) {
							requestMap.put(entry.getKey(), entry.getValue());
						}
					}
				}
			}
			String template = renderTemplate(httpServletRequest, moduleDetailsMap, requestMap, httpServletResponse);
			
			if (template != null && CollectionUtils.isEmpty(moduleDetailsMap) == false
					&& StringUtils.isBlank((String) moduleDetailsMap.get("headerJson")) == false) {
				Gson				gson			= new Gson();
				Map<String, String>	headerConfig	= gson.fromJson(moduleDetailsMap.get("headerJson").toString(),
						Map.class);
				headerConfig.forEach((key, value) -> {
					if ("Content-Language".equals(key) == false) {
						httpServletResponse.setHeader(key, value);
					}
				});
				String localeId = sessionLocaleResolver.resolveLocale(httpServletRequest).toString();
				httpServletResponse.setHeader("Content-Language", localeId);

				if (StringUtils.isBlank(httpServletResponse.getContentType()) == false) {
					httpServletResponse.flushBuffer();
				}

			}
			return template;
		} catch (Exception a_exc) {
			logger.error("Error while loading master module ", a_exc);
			throw new RuntimeException(a_exc.getMessage());
		}
	}

	public String renderTemplate(HttpServletRequest httpServletRequest, Map<String, Object> moduleDetailsMap,
			Map<String, String> requestMap, HttpServletResponse httpServletResponse) throws Exception {
		if (CollectionUtils.isEmpty(moduleDetailsMap) == false) {
			Map<String, Object> parameterMap = validateAndProcessRequestParams(httpServletRequest);
			parameterMap.putAll(requestMap);
			List<String> pathVariableList = getPathVariables(httpServletRequest);
			if (CollectionUtils.isEmpty(pathVariableList) == false) {
				pathVariableList.remove(0);
			}
			parameterMap.put("pathVariableList", pathVariableList);
			Integer	targetLookupId	= Integer.parseInt(moduleDetailsMap.get("targetLookupId").toString());
			String	templateName	= moduleDetailsMap.get("targetTypeName").toString();
			String	targetTypeId	= moduleDetailsMap.get("targetTypeId").toString();
			Integer	includeLayout	= Integer.parseInt(moduleDetailsMap.get("includeLayout").toString());

			if (targetLookupId.equals(Constant.TargetLookupId.TEMPLATE.getTargetLookupId())) {
				if (includeLayout.equals(Constant.INCLUDE_LAYOUT)) {
					return menuService.getTemplateWithSiteLayout(templateName, parameterMap);
				}
				return menuService.getTemplateWithoutLayout(templateName, parameterMap);
			} else if (targetLookupId.equals(Constant.TargetLookupId.DASHBOARD.getTargetLookupId())) {
				Map<String, Object>	templateMap	= new HashMap<>();
				UserDetailsVO		detailsVO	= userDetails.getUserDetails();
				String				userId		= detailsVO.getUserId();
				List<String>		roleIdList	= detailsVO.getRoleIdList();
				templateMap.put("templateName", templateName);
				String template = dashletService.getDashletUI(userId, false, targetTypeId, roleIdList, false, httpServletRequest, httpServletResponse);
				if (includeLayout.equals(Constant.INCLUDE_LAYOUT)) {
					return menuService.getDashletWithLayout(template, templateMap);
				}
				return menuService.getDashletWithoutLayout(templateName, template, templateMap);
			} else if (targetLookupId.equals(Constant.TargetLookupId.DYANMICFORM.getTargetLookupId())) {
				parameterMap.put("includeLayout", includeLayout == 1 ? "true" : "false");
				String				template	= dynamicFormService.loadDynamicForm(targetTypeId, parameterMap, null);
				Map<String, Object>	templateMap	= new HashMap<>();
				templateMap.put("formId", targetTypeId);
				return template;
			}
		}

		httpServletResponse.sendError(HttpStatus.NOT_FOUND.value(), "Not found");
		return null;
	}

	public Map<String, Object> validateAndProcessRequestParams(HttpServletRequest httpServletRequest) {
		Map<String, Object> requestParams = new HashMap<>();
		for (String requestParamKey : httpServletRequest.getParameterMap().keySet()) {
			requestParams.put(requestParamKey, httpServletRequest.getParameter(requestParamKey));
		}
		return requestParams;
	}

	public Map<String, Object> getModuleDetails(String requestUrl, HttpServletRequest httpServletRequest)
			throws Exception {
		Map<String, Object>			moduleDetailsMap	= new HashMap<>();

		StringBuilder				moduleUrl			= new StringBuilder();
		StringBuilder				fixedModuleUrl		= new StringBuilder();
		List<String>				pathVariableList	= getPathVariables(httpServletRequest);

		List<Map<String, Object>>	moduleDetailsList	= moduleService.getModuleTargetTypeURL(requestUrl);
		for (String pathVariable : pathVariableList) {

			if (CollectionUtils.isEmpty(moduleDetailsList) == false) {
				if (StringUtils.isBlank(moduleUrl) == false) {
					moduleUrl.append("/");
					fixedModuleUrl.append("/");
				}
				moduleUrl.append(pathVariable);
				fixedModuleUrl.append(pathVariable);
				moduleUrl.append("/**");

				for (Map<String, Object> moduleDetailsMapDB : moduleDetailsList) {
					String moduleUrlDB = (String) moduleDetailsMapDB.get("moduleUrl");
					if (StringUtils.isBlank(moduleUrlDB) == false && (moduleUrlDB.equals(moduleUrl.toString())
							|| moduleUrlDB.equals(fixedModuleUrl.toString()))) {
						moduleDetailsMap.putAll(moduleDetailsMapDB);
						break;
					}
				}
				moduleUrl.delete(moduleUrl.indexOf("/**"), moduleUrl.length());
			}

		}
		if (CollectionUtils.isEmpty(pathVariableList) == true) {
			Map<String, Object> moduleDetails = moduleService.getModuleTargetByURL(requestUrl);
			if (CollectionUtils.isEmpty(moduleDetails) == true) {
				moduleUrl.append(requestUrl).append("/**");
				moduleDetails = moduleService.getModuleTargetByURL(moduleUrl.toString());
			}
			moduleDetailsMap.putAll(moduleDetails);
		}
		return moduleDetailsMap;
	}

	public List<String> getPathVariables(HttpServletRequest httpServletRequest) {
		List<String>	pathVariableList	= new ArrayList<>();
		String			moduleUrl			= httpServletRequest.getRequestURI()
				.substring(httpServletRequest.getContextPath().length());
		moduleUrl	= moduleUrl.replaceFirst("/view/", "");
		moduleUrl	= moduleUrl.replaceFirst("/cf/", "");

		if (moduleUrl.indexOf("/") != -1) {
			pathVariableList = Stream.of(moduleUrl.split("/")).map(urlElement -> new String(urlElement))
					.collect(Collectors.toList());
		}
		return pathVariableList;
	}

}
