package com.trigyn.jws.webstarter.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.dbutils.repository.IModuleListingRepository;
import com.trigyn.jws.dbutils.repository.ModuleDAO;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.utils.Constant;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.templating.service.DBTemplatingService;
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.templating.vo.TemplateVO;
import com.trigyn.jws.usermanagement.repository.JwsRoleRepository;
import com.trigyn.jws.webstarter.controller.MasterModuleController;

@RestController
public class URLExceptionHandler implements ErrorController {

	@Autowired
	private DBTemplatingService			templateService				= null;

	@Autowired
	private IModuleListingRepository	iModuleListingRepository	= null;

	@Autowired
	private ModuleDAO					moduleDAO					= null;

	@Autowired
	private MenuService					menuService					= null;

	@Autowired
	private MasterModuleController		masterModuleController		= null;

	@Autowired
	private IUserDetailsService			userDetails					= null;

	@Autowired
	private JwsRoleRepository			jwsRoleRepository			= null;

	@RequestMapping("/error")
	public Object errorHandler(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws Exception {
		Object				status			= httpServletRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		Exception			exception		= (Exception) httpServletRequest
				.getAttribute("javax.servlet.error.exception");
		TemplateVO			templateVO		= templateService.getTemplateByName("error-page");
		Map<String, Object>	parameterMap	= new HashMap<String, Object>();
		if (status != null) {
			Integer statusCode = Integer.parseInt(status.toString());
			parameterMap.put("statusCode", statusCode);
			String url = httpServletRequest.getAttribute(RequestDispatcher.ERROR_REQUEST_URI).toString()
					.replace(httpServletRequest.getContextPath(), "");
			if (url.contains("webjars")) {
				return new ResponseEntity<String>("", HttpStatus.OK);
			}
			if (url.contains("/japi/")) {
				// mobile
				String		errorMessage	= httpServletRequest.getAttribute(RequestDispatcher.ERROR_MESSAGE)
						.toString();
				String		errorStatusCode	= httpServletRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)
						.toString();
				HttpStatus	httpStatus		= HttpStatus.INTERNAL_SERVER_ERROR;

				for (HttpStatus currentStatus : HttpStatus.values()) {
					if (currentStatus.value() == Integer.parseInt(errorStatusCode)) {
						httpStatus = currentStatus;
					}

				}
				return new ResponseEntity<String>(errorMessage, httpStatus);
			}
			if (statusCode == HttpStatus.NOT_FOUND.value()) {
				String fallbackTemplate = null;
				if ("/".equals(url)) {
					UserDetailsVO	userDetailsVO		= userDetails.getUserDetails();
					List<String>	roleIdList			= userDetailsVO.getRoleIdList();
					List<String>	roleIdPriorityList	= jwsRoleRepository.getRoleIdByPriority(roleIdList);
					List<String>	homePageURLList		= iModuleListingRepository
							.getModuleURLByRoleId(roleIdPriorityList, Constant.IS_HOME_PAGE);
					if (!CollectionUtils.isEmpty(homePageURLList)) {
						for (String homePageURL : homePageURLList) {
							if (StringUtils.isBlank(fallbackTemplate) && !StringUtils.isBlank(homePageURL)) {
								fallbackTemplate = masterModuleController.loadTemplate(httpServletRequest, homePageURL);
							}
						}
					}
				}
				if ("/".equals(url) && fallbackTemplate == null) {
					return menuService.getTemplateWithSiteLayout("home", new HashMap<String, Object>());
				} else if (fallbackTemplate == null) {
					return menuService.getTemplateWithSiteLayout(templateVO.getTemplateName(), parameterMap);
				} else {
					return fallbackTemplate;
				}
			}
			if (exception != null) {
				parameterMap.put("errorMessage", "<#noparse>" + exception.getCause() + "</#noparse>");
			} else {
				parameterMap.put("errorMessage", "<#noparse>"
						+ httpServletRequest.getAttribute(RequestDispatcher.ERROR_MESSAGE) + "</#noparse>");
			}

		}
		return menuService.getTemplateWithSiteLayout(templateVO.getTemplateName(), parameterMap);
	}

	@Override
	public String getErrorPath() {
		return null;
	}

}
