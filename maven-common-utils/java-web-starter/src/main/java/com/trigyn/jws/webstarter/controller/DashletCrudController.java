package com.trigyn.jws.webstarter.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.scripting.bsh.BshScriptUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trigyn.jws.dashboard.service.DashletService;
import com.trigyn.jws.dashboard.utility.Constants;
import com.trigyn.jws.dashboard.vo.DashletVO;
import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.utils.ActivityLog;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.webstarter.service.DashboardCrudService;
import com.trigyn.jws.webstarter.utils.Constant;

@RestController
@RequestMapping("/cf")
@PreAuthorize("hasPermission('module','Dashlet')" + "&& hasPermission('module','Dashboard')")
public class DashletCrudController {

	private final static Logger		logger					= LogManager.getLogger(DashletCrudController.class);

	@Autowired
	private DashboardCrudService	dashboardCrudService	= null;

	@Autowired
	private DashletService			dashletServive			= null;

	@Autowired
	private IUserDetailsService		userDetails				= null;

	@Autowired
	private PropertyMasterDAO		propertyMasterDAO		= null;

	@Autowired
	private MenuService				menuService				= null;

	@Autowired
	private ActivityLog				activitylog				= null;

	@GetMapping(value = "/dlm", produces = MediaType.TEXT_HTML_VALUE)
	public String dashletMasterListing(HttpServletResponse httpServletResponse) throws IOException {
		try {
			Map<String, Object>	modelMap	= new HashMap<>();
			String				environment	= propertyMasterDAO.findPropertyMasterValue("system", "system", "profile");
			modelMap.put("environment", environment);
			return menuService.getTemplateWithSiteLayout("dashlet-listing", modelMap);
		} catch (Exception a_exception) {
			logger.error("Error ", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}
	}

	@PostMapping(value = "/aedl", produces = { MediaType.TEXT_HTML_VALUE })
	public String createEditDashlet(@RequestParam("dashlet-id") String dashletId,
			HttpServletResponse httpServletResponse) throws IOException {
		try {
			Map<String, Object>	templateMap			= new HashMap<>();
			DashletVO			dashletVO			= dashletServive.getDashletDetailsById(dashletId);
			Map<String, String>	componentsMap		= dashletServive
					.findComponentTypes(Constants.COMPONENT_TYPE_CATEGORY);
			Map<String, String>	contextDetailsMap	= dashboardCrudService.findContextDetails();
			templateMap.put("dashletVO", dashletVO);
			templateMap.put("componentMap", componentsMap);
			templateMap.put("contextDetailsMap", contextDetailsMap);

			/* Method called for implementing Activity Log */
			if (!StringUtils.isBlank(dashletId)) {
				logActivity(1, dashletVO.getDashletName());
			}
			return menuService.getTemplateWithSiteLayout("dashlet-manage-details", templateMap);
		} catch (Exception a_exception) {
			logger.error("Error ", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}
	}

	/**
	 * Purpose of this method is to log activities</br>
	 * in DashLets Module.
	 * 
	 * @author            Bibhusrita.Nayak
	 * @param  entityName
	 * @param  typeSelect
	 * @throws Exception
	 */
	private void logActivity(Integer typeSelect, String entityName) throws Exception {
		Date				activityTimestamp	= new Date();
		Map<String, String>	requestParams		= new HashMap<>();
		UserDetailsVO		detailsVO			= userDetails.getUserDetails();
		requestParams.put("entityName", entityName);
		requestParams.put("masterModuleType", Constants.Modules.DASHLETS.getModuleName());
		requestParams.put("userName", detailsVO.getUserName());
		requestParams.put("message", "");
		requestParams.put("date", activityTimestamp.toString());
		requestParams.put("action", Constants.Action.OPEN.getAction());
		if (typeSelect == Constants.Changetype.CUSTOM.getChangeTypeInt()) {
			requestParams.put("typeSelect", Constants.Changetype.CUSTOM.getChangetype());
		} else {
			requestParams.put("typeSelect", Constants.Changetype.SYSTEM.getChangetype());
		}
		activitylog.activitylog(requestParams);
	}

	@PostMapping(value = "/sdl")
	@ResponseBody
	public String saveDashlet(@RequestBody DashletVO dashletVO) throws Exception {
		return dashboardCrudService.saveDashlet(dashletVO, Constant.MASTER_SOURCE_VERSION_TYPE);
	}

	@PostMapping(value = "/sdlv")
	public void saveDashletByVersion(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse)
			throws Exception {
		String			modifiedContent	= a_httpServletRequest.getParameter("modifiedContent");
		ObjectMapper	objectMapper	= new ObjectMapper();
		DashletVO		dashletVO		= objectMapper.readValue(modifiedContent, DashletVO.class);
		dashboardCrudService.saveDashlet(dashletVO, Constant.REVISION_SOURCE_VERSION_TYPE);
	}

	@PostMapping(value = "/ddl")
	public void downloadAllDashletsToLocalDirectory(HttpSession session, HttpServletRequest request) throws Exception {
		dashboardCrudService.downloadDashlets(null);
	}

	@PostMapping(value = "/udl")
	public void uploadAllDashletsToDB(HttpSession session, HttpServletRequest request) throws Exception {
		dashboardCrudService.uploadDashlets(null);
	}

	@PostMapping(value = "/ddlbi")
	public void downloadDashletByIdToLocalDirectory(HttpSession session, HttpServletRequest request) throws Exception {
		String dashletId = request.getParameter("dashletId");
		dashboardCrudService.downloadDashlets(dashletId);
	}

	@PostMapping(value = "/udlbn")
	public void uploadDashletsByNameToDB(HttpSession session, HttpServletRequest request) throws Exception {
		String dashletName = request.getParameter("dashletName");
		dashboardCrudService.uploadDashlets(dashletName);
	}
}
