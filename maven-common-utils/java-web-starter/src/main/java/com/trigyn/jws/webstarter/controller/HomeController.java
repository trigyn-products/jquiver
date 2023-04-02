package com.trigyn.jws.webstarter.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.dbutils.repository.IModuleListingRepository;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.utils.Constant;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.usermanagement.repository.JwsRoleRepository;
import com.trigyn.jws.usermanagement.security.config.ApplicationSecurityDetails;
import com.trigyn.jws.webstarter.service.MasterModuleService;

@RestController
@RequestMapping(value = { "/cf", "/", "" })
public class HomeController {

	private final static Logger			logger						= LogManager.getLogger(HomeController.class);

	@Autowired
	private MenuService					menuService					= null;

	@Autowired
	private IModuleListingRepository	iModuleListingRepository	= null;

	@Autowired
	private IUserDetailsService			userDetails					= null;

	@Autowired
	private JwsRoleRepository			jwsRoleRepository			= null;

	@Autowired
	private MasterModuleService			masterModuleService			= null;

	@Autowired
	private ApplicationSecurityDetails	applicationSecurityDetails	= null;

	@Autowired
	private JwsUserRegistrationController	jwsUserRegistrationController	= null;

	@RequestMapping(value = { "", "/", "/home" }, produces = MediaType.TEXT_HTML_VALUE)
	@ResponseBody
	public String homePage(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws IOException {
		try {
			UserDetailsVO	userDetailsVO		= userDetails.getUserDetails();
			List<String>	roleNameList		= userDetailsVO.getRoleIdList();
			List<String>	roleIdPriorityList	= jwsRoleRepository.getRoleIdByPriority(roleNameList);
			List<String>	homePageURLList		= iModuleListingRepository.getModuleURLByRoleId(roleIdPriorityList,
					Constant.IS_HOME_PAGE);
			Map<String, Object> authenticationDetails = applicationSecurityDetails.getAuthenticationDetails();
			
			if(roleNameList.size() >= 2 && roleNameList.contains("ADMIN") 
					&& roleNameList.contains("AUTHENTICATED")) {
				return menuService.getTemplateWithSiteLayout("control-panel", new HashMap<String, Object>());
			} else if((authenticationDetails != null && (boolean)authenticationDetails.get("isAuthenticationEnabled") == true) && 
					roleNameList.contains("anonymous") && CollectionUtils.isEmpty(homePageURLList) == true) {
				return jwsUserRegistrationController.userLoginPage(httpServletRequest, httpServletRequest.getSession(), httpServletResponse);
			} else {
				if (CollectionUtils.isEmpty(homePageURLList) == false) {
					for (String homePageURL : homePageURLList) {
						if (StringUtils.isBlank(homePageURL) == false) {
							return masterModuleService.loadTemplate(httpServletRequest, homePageURL, httpServletResponse);
						}
					}
				}
			}
			
			return menuService.getTemplateWithSiteLayout("control-panel", new HashMap<String, Object>());
		} catch (Exception a_exception) {
			logger.error("Error occured while loading home page.", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}
	}

}
