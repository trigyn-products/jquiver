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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
import com.trigyn.jws.templating.service.DBTemplatingService;
import com.trigyn.jws.templating.utils.TemplatingUtils;
import com.trigyn.jws.templating.vo.TemplateVO;
import com.trigyn.jws.webstarter.service.DashboardCrudService;

@RestController
@RequestMapping("/cf")
public class DashboardCrudController {

	private final static Logger logger = LogManager.getLogger(DashboardCrudController.class);
	
    @Autowired
	private DBTemplatingService templatingService 		= null;

	@Autowired
	private TemplatingUtils templateEngine 				= null;

	@Autowired
	private DashboardCrudService dashboardCrudService 	= null;

	@Autowired
	private DashletService dashletServive 				= null;

	@Autowired
	private IUserDetailsService userDetails 			= null;
	
	@Autowired
	private PropertyMasterDAO propertyMasterDAO 		= null;
    
    
	@GetMapping(value = "/dlm", produces = MediaType.TEXT_HTML_VALUE)
	public String dashletMasterListing() throws Exception {
		TemplateVO templateVO = templatingService.getTemplateByName("dashlet-listing");
		Map<String,Object>  modelMap = new HashMap<>();
		String environment = propertyMasterDAO.findPropertyMasterValue("system", "system", "profile");
		modelMap.put("environment", environment);
		return templateEngine.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(), modelMap);
	}

	
	@GetMapping(value = "/dbm", produces = MediaType.TEXT_HTML_VALUE)
	public String dashboardMasterListing() throws Exception {
		TemplateVO templateVO = templatingService.getTemplateByName("dashboard-listing");
		return templateEngine.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(), new HashMap<>());
    }

    
	@PostMapping(value = "/aedb", produces = { MediaType.TEXT_HTML_VALUE })
	public String addEditDashboardDetails(@RequestParam(value = "dashboard-id") String dashboardId) throws Exception {
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
		TemplateVO templateVO = templatingService.getTemplateByName("dashboard-manage-details");
		return templateEngine.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(),
				templateMap);
	}

	
	@PostMapping(value = "/sdb", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Boolean> saveDashboard(@RequestBody DashboardVO dashboardVO,
			@RequestHeader(value = "user-id", required = true) String userId) throws Exception {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		try {
			dashboardCrudService.saveDashboardDetails(dashboardVO, userId);
			return new ResponseEntity<>(true, httpHeaders, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(false, httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
    
    
	@PostMapping(value = "/aedl", produces = {MediaType.TEXT_HTML_VALUE})
	public String createEditDashlet(@RequestParam("dashlet-id") String dashletId) throws Exception {
		Map<String, Object> templateMap 		= new HashMap<>();
		DashletVO dashletVO						= dashletServive.getDashletDetailsById(dashletId);
		Map<String, String> componentsMap		= dashletServive.findComponentTypes(Constants.COMPONENT_TYPE_CATEGORY);
		Map<String, String> contextDetailsMap	= dashboardCrudService.findContextDetails();
		templateMap.put("dashletVO", dashletVO);
		templateMap.put("componentMap", componentsMap);
		templateMap.put("contextDetailsMap", contextDetailsMap);
		TemplateVO templateVO = templatingService.getTemplateByName("dashlet-manage-details");
		return templateEngine.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(), templateMap);
	}
	

	
	@PostMapping(value = "/sdl", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Boolean> saveDashlet(@RequestHeader(value = "user-id", required = true) String userId
			, @RequestBody DashletVO dashletVO) throws Exception {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		try {
			dashboardCrudService.saveDashlet(userId, dashletVO);
			return new ResponseEntity<>(true, httpHeaders, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(false, httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
		}
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
