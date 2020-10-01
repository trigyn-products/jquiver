package com.trigyn.jws.webstarter.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.dashboard.entities.Dashboard;
import com.trigyn.jws.dashboard.entities.DashboardRoleAssociation;
import com.trigyn.jws.dashboard.service.DashletService;
import com.trigyn.jws.dashboard.utility.Constants;
import com.trigyn.jws.dashboard.vo.DashboardVO;
import com.trigyn.jws.dashboard.vo.DashletVO;
import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.vo.UserRoleVO;
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.webstarter.service.DashboardCrudService;

@RestController
@RequestMapping("/cf")
public class DashboardCrudController {

	private final static Logger logger = LogManager.getLogger(DashboardCrudController.class);
	
	@Autowired
	private DashboardCrudService dashboardCrudService 	= null;

	@Autowired
	private DashletService dashletServive 				= null;

	@Autowired
	private IUserDetailsService userDetails 			= null;
	
	@Autowired
	private PropertyMasterDAO propertyMasterDAO 		= null;
	
	@Autowired
	private MenuService menuService 					= null;
    
    
	@GetMapping(value = "/dlm", produces = MediaType.TEXT_HTML_VALUE)
	public String dashletMasterListing(){
		try{
			Map<String,Object>  modelMap = new HashMap<>();
			String environment = propertyMasterDAO.findPropertyMasterValue("system", "system", "profile");
			modelMap.put("environment", environment);
			return menuService.getTemplateWithSiteLayout("dashlet-listing", modelMap);
		} catch (Exception exception) {
			throw new RuntimeException(exception.getMessage());
		}
	}

	
	@GetMapping(value = "/dbm", produces = MediaType.TEXT_HTML_VALUE)
	public String dashboardMasterListing() {
		try{
			return menuService.getTemplateWithSiteLayout("dashboard-listing", new HashMap<>());
		} catch (Exception exception) {
			throw new RuntimeException(exception.getMessage());
		}
    }

    
	@PostMapping(value = "/aedb", produces = { MediaType.TEXT_HTML_VALUE })
	public String addEditDashboardDetails(@RequestParam(value = "dashboard-id") String dashboardId) {
		try{
			Map<String, Object> templateMap 	= new HashMap<>();
			Dashboard dashboard 				= new Dashboard();
			List<UserRoleVO> userRoleVOs 		= dashboardCrudService.getAllUserRoles();
			if (dashboardId != null && !dashboardId.isEmpty() && !dashboardId.equals("")) {
				dashboard = dashboardCrudService.findDashboardByDashboardId(dashboardId);
				List<DashboardRoleAssociation> dashletRoleAssociation = dashboardCrudService.findDashboardRoleByDashboardId(dashboardId);
				if (!CollectionUtils.isEmpty(dashletRoleAssociation)) {
					dashboard.setDashboardRoles(dashletRoleAssociation);
				}
			} else {
				dashboard.setDashboardRoles(new ArrayList<>());
			}
			
			templateMap.put("userRoleVOs", userRoleVOs);
			Map<String, String> contextDetails = dashboardCrudService.findContextDetails();
			templateMap.put("contextDetails", contextDetails);
			templateMap.put("dashboard", dashboard);
			return menuService.getTemplateWithSiteLayout("dashboard-manage-details", templateMap);
		} catch (Exception exception) {
			throw new RuntimeException(exception.getMessage());
		}
	}

	
	@PostMapping(value = "/sdb")
	@ResponseBody
	public String saveDashboard(@RequestBody DashboardVO dashboardVO,
			@RequestHeader(value = "user-id", required = true) String userId) throws Exception{
		dashboardCrudService.deleteAllDashletFromDashboard(dashboardVO);
		dashboardCrudService.deleteAllDashboardRoles(dashboardVO);
		return dashboardCrudService.saveDashboardDetails(dashboardVO, userId);
    }
    
    
	@PostMapping(value = "/aedl", produces = {MediaType.TEXT_HTML_VALUE})
	public String createEditDashlet(@RequestParam("dashlet-id") String dashletId) {
		try{
			Map<String, Object> templateMap 		= new HashMap<>();
			DashletVO dashletVO						= dashletServive.getDashletDetailsById(dashletId);
			Map<String, String> componentsMap		= dashletServive.findComponentTypes(Constants.COMPONENT_TYPE_CATEGORY);
			Map<String, String> contextDetailsMap	= dashboardCrudService.findContextDetails();
			templateMap.put("dashletVO", dashletVO);
			templateMap.put("componentMap", componentsMap);
			templateMap.put("contextDetailsMap", contextDetailsMap);
			return menuService.getTemplateWithSiteLayout("dashlet-manage-details", templateMap);
		} catch (Exception exception) {
			throw new RuntimeException(exception.getMessage());
		}
	}
	

	
	@PostMapping(value = "/sdl")
	@ResponseBody
	public String saveDashlet(@RequestHeader(value = "user-id", required = true) String userId
			, @RequestBody DashletVO dashletVO) throws Exception {
		dashboardCrudService.deleteAllDashletProperty(dashletVO);
		dashboardCrudService.deleteAllDashletRoles(dashletVO);
		return dashboardCrudService.saveDashlet(userId, dashletVO);
	}
    
	@PostMapping(value = "/ddl")
	public void downloadAllDashletsToLocalDirectory(HttpSession session, HttpServletRequest request) throws Exception {
		dashboardCrudService.downloadDashlets();
	}

	@PostMapping(value = "/udl")
	public void uploadAllDashletsToDB(HttpSession session, HttpServletRequest request) throws Exception {
		dashboardCrudService.uploadDashlets();
	}
	
}
