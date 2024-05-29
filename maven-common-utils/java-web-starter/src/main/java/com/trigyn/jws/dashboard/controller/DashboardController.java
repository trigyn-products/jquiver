package com.trigyn.jws.dashboard.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.dashboard.dao.DashletDAO;
import com.trigyn.jws.dashboard.service.DashboardService;
import com.trigyn.jws.dashboard.service.DashletService;
import com.trigyn.jws.dashboard.utility.Constants;
import com.trigyn.jws.dashboard.vo.DashboardDashletVO;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.utils.CustomStopException;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.templating.service.DBTemplatingService;
import com.trigyn.jws.templating.utils.TemplatingUtils;
import com.trigyn.jws.templating.vo.TemplateVO;
import com.trigyn.jws.usermanagement.security.config.Authorized;

@RestController
@RequestMapping(value = "/cf")
public class DashboardController {

	@Autowired
	private DashboardService	dashboardServiceImpl	= null;

	@Autowired
	private DashletService		dashletService			= null;

	@Autowired
	private DBTemplatingService	templatingService		= null;

	@Autowired
	private TemplatingUtils		templateEngine			= null;

	@Autowired
	private IUserDetailsService	userDetails				= null;
	
	@Autowired
	private DashletDAO 			dashletDAO 				= null;
	
	private final static Logger	logger					= LogManager.getLogger(DashboardController.class);

	@GetMapping(value = "/gdbc", produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<DashboardDashletVO> getDashletByContextId(@RequestHeader(value = "dashboardId", required = false) String dashboardId) throws NumberFormatException, Exception {
		return dashboardServiceImpl.getDashletsByContextId(dashboardId);
	}

	@PostMapping(value = "/dls", produces = MediaType.TEXT_HTML_VALUE)
	@ResponseBody
	@Authorized(moduleName = com.trigyn.jws.usermanagement.utils.Constants.DASHBOARD)
	public String dashlets(@RequestParam(value = "dashboardId") String dashboardId,
		@RequestHeader(value = "user-id", required = false) String userId, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
		List<String> roleIdList = new ArrayList<>();
		if (userId == null) {
			UserDetailsVO detailsVO = userDetails.getUserDetails();
			userId		= detailsVO.getUserId();
			roleIdList	= detailsVO.getRoleIdList();
		}
		return dashboardServiceImpl.getDashletUI(userId, false, dashboardId, roleIdList, true, httpServletRequest, httpServletResponse);
	}

	@PostMapping(value = "/sdc")
	public void saveDashletCoordinates(@RequestHeader(value = "user-id", required = false) String userId,
		@RequestParam(value = "dashboardId") String dashboardId, @RequestParam(value = "dashlets[]") String[] dashlets) throws Exception {
		if (userId == null) {
			UserDetailsVO detailsVO = userDetails.getUserDetails();
			userId = detailsVO.getUserId();
		}
		dashletService.saveDashletConfiguration(userId, dashlets, dashboardId);
	}
	
	@PostMapping(value = "/rdc")
	public String refreshDashletContent(@RequestHeader(value = "user-id", required = false) String userId,
		@RequestParam(value = "dashletId") String dashletId, @RequestParam(value = "paramArray[]") String[] paramArray,
		@RequestParam(value = "dashboardId") String dashboardId, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
		String properties = httpServletRequest.getParameter("properties");
		if (userId == null) {
			UserDetailsVO detailsVO = userDetails.getUserDetails();
			userId = detailsVO.getUserId();
		}
		return dashletService.refreshDashletContent(userId, dashletId, paramArray, dashboardId, httpServletRequest, httpServletResponse, properties);
	}
	
	@PostMapping(value = "/fvd")
	public Object fetchValidationData(HttpServletRequest httpServletRequest) throws Exception {
		logger.debug("Inside DashboardController.fetchValidationData");
		try {
			String type = httpServletRequest.getParameter("type");
				return dashletService.findComponentValidation(type);
		} catch (Exception exception) {
			logger.error("Error occured while fetching Validation Data", exception);
			return null;
		}
	}



	@PostMapping(value = "/oc")
	public String openDashletConfig(@RequestHeader(value = "user-id", required = false) String userId,
			@RequestParam(value = "dashboardId") String dashboardId,
			@RequestParam(value = "dashletId") String dashletId) throws Exception, CustomStopException {
		try {
			if (userId == null) {
				UserDetailsVO detailsVO = userDetails.getUserDetails();
				userId = detailsVO.getUserId();
			}
			Map<String, Object> templateDetails = dashletService.getDashletConfigDetails(userId, dashboardId,
					dashletId);
			
			TemplateVO configTemplateVO = templatingService.getTemplateByName("dashlet-configuration");
			return templateEngine.processTemplateContents(configTemplateVO.getTemplate(),
					configTemplateVO.getTemplateName(), templateDetails);
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in openDashletConfig.", custStopException);
			throw custStopException;
		}
	}

	@PostMapping(value = "/sc")
	public void saveConfiguration(@RequestBody MultiValueMap<String, String> formData, HttpServletRequest a_httpServletRequest)
			throws NumberFormatException, Exception {
		UserDetailsVO	detailsVO	= userDetails.getUserDetails();
		String			userId		= detailsVO.getUserId();
		String			dashboardId	= formData.getFirst("dashboardId");
		formData.remove("dashboardId");
		dashletService.saveConfiguration(formData, userId, dashboardId);
	}
	
}
