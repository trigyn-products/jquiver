package com.trigyn.jws.webstarter.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.trigyn.jws.dbutils.service.ModuleService;
import com.trigyn.jws.dbutils.vo.ModuleDetailsVO;
import com.trigyn.jws.dbutils.vo.ModuleTargetLookupVO;
import com.trigyn.jws.dbutils.vo.UserRoleVO;
import com.trigyn.jws.templating.service.MenuService;

@RestController
@RequestMapping("/cf")
@PreAuthorize("hasPermission('module','Site Layout')")
public class MenuCrudController {

	private final static Logger				logger			= LogManager.getLogger(MenuCrudController.class);

	@Autowired
	private ModuleService					moduleService	= null;

	@Autowired
	private MenuService						menuService		= null;

	@Autowired
	private RequestMappingHandlerMapping	handlerMapping	= null;

	@GetMapping(value = "/mul", produces = MediaType.TEXT_HTML_VALUE)
	public String moduleListingPage(HttpServletResponse httpServletResponse) throws Exception {
		try {
			return menuService.getTemplateWithSiteLayout("menu-module-listing", new HashMap<>());
		} catch (Exception a_exception) {
			logger.error("Error ", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}

	}

	@PostMapping(value = "/aem", produces = { MediaType.TEXT_HTML_VALUE })
	public String addEditModule(@RequestParam(value = "module-id") String moduleId, HttpServletRequest a_httHttpServletRequest,
		HttpServletResponse httpServletResponse) throws IOException {
		try {
			Map<String, Object>			templateMap					= new HashMap<>();
			ModuleDetailsVO				moduleDetailsVO				= moduleService.getModuleDetails(moduleId);
			List<ModuleDetailsVO>		moduleListingVOList			= moduleService.getAllParentModules(moduleId);
			List<ModuleTargetLookupVO>	moduleTargetLookupVOList	= moduleService.getAllModuleLookUp();
			List<UserRoleVO>			userRoleVOs					= moduleService.getAllUserRoles();
			Integer						defaultSequence				= moduleService.getModuleMaxSequence();
			String						uri							= a_httHttpServletRequest.getRequestURI()
					.substring(a_httHttpServletRequest.getContextPath().length());
			String						url							= a_httHttpServletRequest.getRequestURL().toString();
			StringBuilder				urlPrefix					= new StringBuilder();
			url = url.replace(uri, "");
			urlPrefix.append(url).append("/view/");

			templateMap.put("urlPrefix", urlPrefix);
			templateMap.put("userRoleVOs", userRoleVOs);
			templateMap.put("defaultSequence", defaultSequence);
			templateMap.put("moduleDetailsVO", moduleDetailsVO);
			templateMap.put("moduleListingVOList", moduleListingVOList);
			templateMap.put("moduleTargetLookupVOList", moduleTargetLookupVOList);
			return menuService.getTemplateWithSiteLayout("module-manage-details", templateMap);
		} catch (Exception a_exception) {
			logger.error("Error ", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}

	}

	@GetMapping(value = "/ltlm", produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<Map<String, Object>> getTargetTypes(@RequestHeader(value = "target-lookup-id", required = true) Integer targetLookupId)
			throws Exception {
		return moduleService.getTargetTypes(targetLookupId, null);
	}

	@GetMapping(value = "/cms")
	@ResponseBody
	public String checkSequenceNumber(@RequestHeader(name = "parent-module-id", required = false) String parentModuleId,
		@RequestHeader(name = "sequence", required = true) Integer sequence) throws Exception {
		try {
			return moduleService.getModuleIdBySequence(parentModuleId, sequence);
		} catch (Exception a_exception) {
			logger.error("Error ", a_exception);
			return "Error occurred";
		}
	}

	@GetMapping(value = "/ced", produces = { MediaType.APPLICATION_JSON_VALUE })
	public Map<String, Object> getExistingData(@RequestHeader(name = "module-name", required = false) String moduleName,
		@RequestHeader(name = "parent-module-id", required = false) String parentModuleId,
		@RequestHeader(name = "sequence", required = false) Integer sequence,
		@RequestHeader(name = "module-url", required = false) String moduleURL,
		@RequestHeader(name = "module-id", required = false) String moduleId) throws Exception {
		return moduleService.getExistingModuleData(moduleId, moduleName, parentModuleId, sequence, moduleURL);
	}

	@PostMapping(value = "/sm")
	@ResponseBody
	public String saveModule(@RequestBody ModuleDetailsVO moduleDetailsVO) throws Exception {
		// List<?> systemUrls = handlerMapping.getHandlerMethods().keySet()
		// .stream()
		// .map(requestMappingInfo -> requestMappingInfo.getPatternsCondition())
		// .collect(Collectors.toList());
		return moduleService.saveModuleDetails(moduleDetailsVO);
	}

	@GetMapping(value = "/dsp")
	@ResponseBody
	public Integer getMaxSequenceByParent(@RequestHeader(name = "parent-module-id", required = false) String parentModuleId)
			throws Exception {
		return moduleService.getMaxSequenceByParent(parentModuleId);
	}

	@GetMapping(value = "/dsg")
	@ResponseBody
	public Integer getMaxSequenceByGroup() throws Exception {
		return moduleService.getModuleMaxSequence();
	}

	@GetMapping(value = "/chpl", produces = MediaType.TEXT_HTML_VALUE)
	public String homePageListing(HttpServletResponse httpServletResponse) throws IOException {

		try {
			Map<String, Object> templateMap = new HashMap<>();
			return menuService.getTemplateWithSiteLayout("config-home-page-listing", templateMap);
		} catch (Exception a_exception) {
			logger.error("Error ", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}

	}

	@PostMapping(value = "/schm")
	@ResponseBody
	public String saveConfigHomeModule(HttpServletRequest a_httHttpServletRequest, HttpServletResponse httpServletResponse)
			throws Exception {
		logger.debug("Inside MenuCrudController.saveConfigHomeModule()");
		moduleService.saveModuleRoleAssociation(a_httHttpServletRequest);
		return moduleService.saveConfigHomePage(a_httHttpServletRequest);
	}

	@PostMapping(value = "/vchm")
	@ResponseBody
	public Boolean validateConfigHomeModule(HttpServletRequest a_httHttpServletRequest, HttpServletResponse httpServletResponse)
			throws Exception {
		String	moduleId	= a_httHttpServletRequest.getParameter("moduleId");
		String	roleId		= a_httHttpServletRequest.getParameter("roleId");
		String	moduleIdDB	= moduleService.findModuleIdByRoleId(roleId, moduleId);
		if (StringUtils.isBlank(moduleIdDB) == false && moduleIdDB.equals(moduleId) == false) {
			return true;
		}
		return false;
	}

	@GetMapping(value = "/gprm")
	public List<?> getMappings() {
		return handlerMapping.getHandlerMethods().keySet().stream().collect(Collectors.toList());
	}
}
