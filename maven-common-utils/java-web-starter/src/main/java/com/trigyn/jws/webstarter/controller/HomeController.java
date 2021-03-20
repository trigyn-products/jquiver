package com.trigyn.jws.webstarter.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.dbutils.repository.IModuleListingRepository;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.utils.Constant;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.usermanagement.repository.JwsRoleRepository;

@RestController
@RequestMapping("/cf")
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
	private MasterModuleController		masterModuleController		= null;

	@GetMapping(value = "/home", produces = MediaType.TEXT_HTML_VALUE)
	public String homePage(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws IOException {
		try {
			UserDetailsVO	userDetailsVO		= userDetails.getUserDetails();
			List<String>	roleNameList		= userDetailsVO.getRoleIdList();
			List<String>	roleIdPriorityList	= jwsRoleRepository.getRoleIdByPriority(roleNameList);
			List<String>	homePageURLList		= iModuleListingRepository.getModuleURLByRoleId(roleIdPriorityList,
					Constant.IS_HOME_PAGE);
			if (!CollectionUtils.isEmpty(homePageURLList)) {
				for (String homePageURL : homePageURLList) {
					if (!StringUtils.isBlank(homePageURL)) {
						return masterModuleController.loadTemplate(httpServletRequest, homePageURL);
					}
				}
			}
			return menuService.getTemplateWithSiteLayout("home", new HashMap<String, Object>());
		} catch (Exception a_exception) {
			logger.error("Error ", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}
	}

}
