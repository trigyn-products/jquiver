package com.trigyn.jws.dashboard.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.dashboard.entities.DashletProperties;
import com.trigyn.jws.dashboard.service.DashboardService;
import com.trigyn.jws.dashboard.service.DashletService;
import com.trigyn.jws.dashboard.utility.Constants;
import com.trigyn.jws.dashboard.vo.DashboardDashletVO;
import com.trigyn.jws.dashboard.vo.DashboardLookupCategoryVO;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;

import com.trigyn.jws.templating.service.DBTemplatingService;
import com.trigyn.jws.templating.utils.TemplatingUtils;
import com.trigyn.jws.templating.vo.TemplateVO;
import com.trigyn.jws.usermanagement.security.config.Authorized;

@RestController
@RequestMapping(value = "/cf")
public class DashboardController {

	@Autowired
	private DashboardService dashboardServiceImpl 	= null;

	@Autowired
	private DashletService dashletService 			= null;

	@Autowired
	private DBTemplatingService templatingService	= null;

	@Autowired
	private TemplatingUtils templateEngine			= null;
	
	@Autowired
	private IUserDetailsService userDetails 		= null;



	
	@GetMapping(value = "/gdbc", produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<DashboardDashletVO> getDashletByContextId(
			@RequestHeader(value = "context-id", required = true) String contextId,
			@RequestHeader(value = "dashboardId", required = false) String dashboardId)
			throws NumberFormatException, Exception {
		return dashboardServiceImpl.getDashletsByContextId(contextId, dashboardId);
	}

	
	@PostMapping(value = "/dls", produces = MediaType.TEXT_HTML_VALUE)
	@ResponseBody
	@Authorized(moduleName = com.trigyn.jws.usermanagement.utils.Constants.DASHBOARD)
	public String dashlets(@RequestParam(value = "dashboardId") String dashboardId,
			@RequestHeader(value = "user-id", required = false) String userId) throws Exception {
		if (userId == null) {
			UserDetailsVO detailsVO = userDetails.getUserDetails();
			userId = detailsVO.getUserId();
		}
		return dashletService.getDashletUI(userId, false, dashboardId);
	}

	
	@PostMapping(value = "/sdc")
	public void saveDashletCoordinates(@RequestHeader(value = "user-id", required = false) String userId,
			@RequestParam(value = "dashboardId") String dashboardId,
			@RequestParam(value = "dashlets[]") String[] dashlets) throws Exception {
		if (userId == null) {
			UserDetailsVO detailsVO = userDetails.getUserDetails();
			userId = detailsVO.getUserId();
		}
		dashletService.saveDashletConfiguration(userId, dashlets, dashboardId);
	}

	@PostMapping(value = "/rdc")
	public String refreshDashletContent(@RequestHeader(value = "user-id", required = false) String userId, @RequestParam(value = "dashletId") String dashletId,
			@RequestParam(value = "paramArray[]") String[] paramArray) throws Exception {
		if (userId == null) {
			UserDetailsVO detailsVO = userDetails.getUserDetails();
			userId = detailsVO.getUserId();
		}
		return dashletService.refreshDashletContent(userId, dashletId, paramArray);
	}

	
	@PostMapping(value = "/oc")
	public String openDashletConfig(@RequestHeader(value = "user-id", required = false) String userId, 
			@RequestParam(value = "dashletId") String dashletId, @RequestParam(value = "dashboardId") String dashboardId) throws Exception {
		if (userId == null) {
			UserDetailsVO detailsVO = userDetails.getUserDetails();
			userId = detailsVO.getUserId();
		}
		List<DashletProperties> properties = dashletService.findDashletPropertyByDashletId(dashletId, false);
		Map<String, Object> templateDetails = new HashMap<>();
		templateDetails.put("properties", properties);
		templateDetails.put("dashboardId", dashboardId);
		TemplateVO configTemplateVO = templatingService.getTemplateByName("dashlet-configuration");
		if(Boolean.FALSE.equals(properties.isEmpty())) {
			Map<Object, Object> userPreferences 			= dashletService.getUserPreferences(userId, dashletId);
			List<DashboardLookupCategoryVO> lookupDetails 	= dashboardServiceImpl.getDashboardLookupDetails(Constants.COMPONENT_TYPE_CATEGORY);
			List<TemplateVO> templateVOs 					= dashboardServiceImpl.getComponentTemplates();
			Map<String, String> componentTemplateData 		= new HashMap<>();
			
			for (TemplateVO templateVO : templateVOs) {
				componentTemplateData.put(templateVO.getTemplateName(), templateVO.getTemplate());
			}
			templateDetails.put("userPreferences", userPreferences);
			templateDetails.put("components", lookupDetails);
			return templateEngine.processMultipleTemplateContents(configTemplateVO.getTemplate(), configTemplateVO.getTemplateName(), templateDetails, componentTemplateData);
		}
		return templateEngine.processTemplateContents(configTemplateVO.getTemplate(), configTemplateVO.getTemplateName(), templateDetails);
	}
	
}
