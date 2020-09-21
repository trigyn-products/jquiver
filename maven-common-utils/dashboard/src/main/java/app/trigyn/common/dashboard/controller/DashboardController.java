package app.trigyn.common.dashboard.controller;

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

import app.trigyn.common.dashboard.entities.DashletProperties;
import app.trigyn.common.dashboard.service.DashboardService;
import app.trigyn.common.dashboard.service.DashletService;
import app.trigyn.common.dashboard.utility.Constants;
import app.trigyn.common.dashboard.vo.DashboardDashletVO;
import app.trigyn.common.dashboard.vo.DashboardLookupCategoryVO;
import app.trigyn.common.dbutils.spi.IUserDetailsService;
import app.trigyn.common.dbutils.vo.UserDetailsVO;
import app.trigyn.core.templating.service.DBTemplatingService;
import app.trigyn.core.templating.utils.TemplatingUtils;
import app.trigyn.core.templating.vo.TemplateVO;

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



	/**
	 * @param contextId
	 * @return
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	@GetMapping(value = "/gdbc", produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<DashboardDashletVO> getDashletByContextId(
			@RequestHeader(value = "context-id", required = true) String contextId,
			@RequestHeader(value = "dashboard-id", required = false) String dashboardId)
			throws NumberFormatException, Exception {
		return dashboardServiceImpl.getDashletsByContextId(contextId, dashboardId);
	}

	/**
	 * @param dashboardId
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/dls", produces = MediaType.TEXT_HTML_VALUE)
	@ResponseBody
	public String dashlets(@RequestParam(value = "dashboardId") String dashboardId,
			@RequestHeader(value = "user-id", required = false) String userId) throws Exception {
		if (userId == null) {
			UserDetailsVO detailsVO = userDetails.getUserDetails();
			userId = detailsVO.getUserId();
		}
		return dashletService.getDashletUI(userId, false, dashboardId);

	}

	/**
	 * @param userId
	 * @param dashboardId
	 * @param dashlets
	 * @throws Exception
	 */
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

	/**
	 * @param userId
	 * @param dashletId
	 * @param dashboardId
	 * @return
	 * @throws Exception
	 */
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