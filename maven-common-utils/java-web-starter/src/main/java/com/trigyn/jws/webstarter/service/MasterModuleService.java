package com.trigyn.jws.webstarter.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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

	public List<JwsMasterModules> getModules() {

		List<JwsMasterModules> masterModules = new ArrayList<>();
		masterModules = jwsMasterModulesRepository.findAllModulesForImportExport(1);

		return masterModules;
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
		return renderTemplate(httpServletRequest, moduleDetailsMap);
	}

	public String renderTemplate(HttpServletRequest httpServletRequest, Map<String, Object> moduleDetailsMap)
			throws Exception {
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
				String template = dashletService.getDashletUI(userId, false, targetTypeId, roleIdList, false);
				if (includeLayout.equals(Constant.INCLUDE_LAYOUT)) {
					return menuService.getDashletTemplateWithLayout(template, templateMap);
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
		Map<String, Object>	moduleDetailsMap	= new HashMap<>();

		StringBuilder		moduleUrl			= new StringBuilder();
		List<String>		pathVariableList	= getPathVariables(httpServletRequest);

		for (String pathVariable : pathVariableList) {

			List<Map<String, Object>> moduleDetailsList = moduleService.getModuleTargetTypeURL(requestUrl);
			if (CollectionUtils.isEmpty(moduleDetailsList) == false) {
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

		}
		if (CollectionUtils.isEmpty(pathVariableList) == true) {
			Map<String, Object> moduleDetailsList = moduleService.getModuleTargetByURL(requestUrl);
			if (CollectionUtils.isEmpty(moduleDetailsList) == true) {
				moduleUrl.append(requestUrl).append("/**");
				moduleDetailsList = moduleService.getModuleTargetByURL(moduleUrl.toString());
			}
			moduleDetailsMap.putAll(moduleDetailsList);
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
