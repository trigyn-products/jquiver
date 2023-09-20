package com.trigyn.jws.webstarter.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trigyn.jws.dashboard.entities.Dashboard;
import com.trigyn.jws.dashboard.entities.DashboardRoleAssociation;
import com.trigyn.jws.dashboard.vo.DashboardVO;
import com.trigyn.jws.dashboard.vo.DashletVO;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.utils.ActivityLog;
import com.trigyn.jws.dbutils.utils.CustomStopException;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.usermanagement.entities.JwsRole;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.webstarter.service.DashboardCrudService;
import com.trigyn.jws.webstarter.utils.Constant;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/cf")
@PreAuthorize("hasPermission('module','Dashboard')")
@Api(tags = "Perform all dashboard related operation")
public class DashboardCrudController {

	private final static Logger		logger					= LogManager.getLogger(DashboardCrudController.class);

	@Autowired
	private DashboardCrudService	dashboardCrudService	= null;

	@Autowired
	private MenuService				menuService				= null;

	@Autowired
	private IUserDetailsService		userDetailsService		= null;

	@Autowired
	private ActivityLog				activitylog				= null;

	@GetMapping(value = "/dbm", produces = MediaType.TEXT_HTML_VALUE)
	@ApiOperation(value = "Dashboard Listing")
	public String dashboardMasterListing(HttpServletResponse httpServletResponse) throws IOException, CustomStopException {
		try {
			return menuService.getTemplateWithSiteLayout("dashboard-listing", new HashMap<>());
		} catch (CustomStopException custStopException) {
			logger.error("Error occured while loading Dashboard Listing page.", custStopException);
			throw custStopException;
		} catch (Exception a_exception) {
			logger.error("Error occured while loading Dashboard Listing page.", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}
	}

	@PostMapping(value = "/aedb", produces = { MediaType.TEXT_HTML_VALUE })
	@ApiOperation(value = "Add edit Dashboard")
	public String addEditDashboardDetails(@RequestParam(value = "dashboard-id") String dashboardId,
			HttpServletResponse httpServletResponse) throws IOException, CustomStopException {
		try {
			Map<String, Object>	templateMap	= new HashMap<>();
			Dashboard			dashboard	= new Dashboard();
			List<JwsRole>	userRoleVOs	= dashboardCrudService.getAllUserRoles();
			if (!StringUtils.isBlank(dashboardId)) {
				dashboard = dashboardCrudService.findDashboardByDashboardId(dashboardId);
				List<DashboardRoleAssociation> dashletRoleAssociation = dashboardCrudService
						.findDashboardRoleByDashboardId(dashboardId);
				if (!CollectionUtils.isEmpty(dashletRoleAssociation)) {
					dashboard.setDashboardRoles(dashletRoleAssociation);
				}
				/* Method called for implementing Activity Log */
				logActivity(dashboard.getDashboardType(), dashboard.getDashboardName());
			} else {
				dashboard.setDashboardRoles(new ArrayList<>());
			}
			templateMap.put("userRoleVOs", userRoleVOs);
			templateMap.put("dashboard", dashboard);
			return menuService.getTemplateWithSiteLayout("dashboard-manage-details", templateMap);
		} catch (CustomStopException custStopException) {
			logger.error("Error occured while loading Dashboard Listing page.", custStopException);
			throw custStopException;
		} catch (Exception a_exception) {
			logger.error("Error occured while loading Dashboard : DashboardId : "+ dashboardId, a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}
	}

	/**
	 * Purpose of this method is to log activities</br>
	 * in Dashboard Module.
	 * 
	 * @author              Bibhusrita.Nayak
	 * @param  templateName
	 * @param  typeSelect
	 * @throws Exception
	 */
	private void logActivity(Integer typeSelect, String entityName) throws Exception {

		Date				date				= new Date();
		Map<String, String>	requestParams		= new HashMap<>();
		UserDetailsVO		detailsVO			= userDetailsService.getUserDetails();
		String				masterModuleType	= Constants.Modules.DASHBOARD.getModuleName();
		String				activityTimestamp	= date.toString();
		requestParams.put("entityName", entityName);
		requestParams.put("masterModuleType", masterModuleType);
		requestParams.put("userName", detailsVO.getUserName());
		requestParams.put("message", "");
		requestParams.put("date", activityTimestamp);
		requestParams.put("action", Constants.Action.OPEN.getAction());
		if (typeSelect == Constants.Changetype.CUSTOM.getChangeTypeInt()) {
			requestParams.put("typeSelect", Constants.Changetype.CUSTOM.getChangetype());
		} else {
			requestParams.put("typeSelect", Constants.Changetype.SYSTEM.getChangetype());
		}
		activitylog.activitylog(requestParams);
	}

	@PostMapping(value = "/sdb")
	@ResponseBody
	public String saveDashboard(@RequestBody DashboardVO dashboardVO,
			@RequestHeader(value = "user-id", required = false) String userId) throws Exception {
		
			dashboardCrudService.deleteAllDashletFromDashboard(dashboardVO);
			dashboardCrudService.deleteAllDashboardRoles(dashboardVO);
		return dashboardCrudService.saveDashboardDetails(dashboardVO, userId, Constant.MASTER_SOURCE_VERSION_TYPE);
	}
	
	@GetMapping(value = "/dds",produces = MediaType.APPLICATION_JSON_VALUE)
	public List<DashletVO> getdashboarddashlet(@RequestParam("dashboardId") String dashboardId) throws Exception {
		Dashboard			dashboard	= new Dashboard();
		if (!StringUtils.isBlank(dashboardId)) {
			dashboard = dashboardCrudService.findDashboardByDashboardId(dashboardId);
			List<DashletVO> dashDashboard = dashboardCrudService.getDashletVOFromDashboard(dashboard);
			return dashDashboard;
		}
		return null;
	}

	@PostMapping(value = "/sdbv")
	public void saveDashboardByVersion(HttpServletRequest a_httpServletRequest,
			HttpServletResponse a_httpServletResponse) throws Exception {
		String			modifiedContent	= a_httpServletRequest.getParameter("modifiedContent");
		ObjectMapper	objectMapper	= new ObjectMapper();
		DashboardVO		dashboardVO		= objectMapper.readValue(modifiedContent, DashboardVO.class);
		dashboardCrudService.deleteAllDashletFromDashboard(dashboardVO);
		dashboardCrudService.deleteAllDashboardRoles(dashboardVO);
		dashboardCrudService.saveDashboardDetails(dashboardVO, null, Constant.REVISION_SOURCE_VERSION_TYPE);
	}

}
