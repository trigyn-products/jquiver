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
import org.springframework.web.util.NestedServletException;

import com.trigyn.jws.dbutils.repository.IModuleListingRepository;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.utils.ApplicationContextUtils;
import com.trigyn.jws.dbutils.utils.Constant;
import com.trigyn.jws.dbutils.utils.CustomStopException;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.templating.service.DBTemplatingService;
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.templating.vo.TemplateVO;
import com.trigyn.jws.usermanagement.repository.JwsRoleRepository;
import com.trigyn.jws.webstarter.service.MasterModuleService;

@RestController
public class URLExceptionHandler implements ErrorController {

	@Autowired
	private DBTemplatingService			templateService				= null;

	@Autowired
	private IModuleListingRepository	iModuleListingRepository	= null;

	@Autowired
	private MasterModuleService			masterModuleService			= null;

	@Autowired
	private MenuService					menuService					= null;

	@Autowired
	private IUserDetailsService			userDetails					= null;

	@Autowired
	private JwsRoleRepository			jwsRoleRepository			= null;
	
	@RequestMapping("/error")
	public Object errorHandler(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
		Object				status			= httpServletRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		String				errorMessage1	= httpServletRequest.getAttribute(RequestDispatcher.ERROR_MESSAGE).toString();
		Exception			exception		= (Exception) httpServletRequest.getAttribute("javax.servlet.error.exception");
		boolean isCustomException = false;
		int customErrorCode = Integer.MIN_VALUE;
		String customErrorMessage = null;
		
		if(exception instanceof NestedServletException && exception.getCause() instanceof CustomStopException) {
			isCustomException = true;
			customErrorMessage = ((CustomStopException)exception.getCause()).getMessage();
			customErrorCode = ((CustomStopException)exception.getCause()).getStatusCode();
		}
		TemplateVO			templateVO		= templateService.getTemplateByName("error-page");
		Map<String, Object>	parameterMap	= new HashMap<String, Object>();
		if (status != null) {
			Integer statusCode ;
			if(isCustomException) {
				statusCode = customErrorCode;
				httpServletResponse.setStatus(statusCode);
			}
			else {
				statusCode = Integer.parseInt(status.toString());
			}
			
			parameterMap.put("statusCode", statusCode);
			parameterMap.put("isCustomException", isCustomException);
			
			String url = httpServletRequest.getAttribute(RequestDispatcher.ERROR_REQUEST_URI).toString()
					.replace(httpServletRequest.getContextPath(), "");
			if (url.contains("webjars")) {
				return new ResponseEntity<String>("", HttpStatus.OK);
			}
			if (url.contains("/japi/") || url.contains("/api/")) {
				isCustomException = true;
				// mobile
				String		errorMessage	= httpServletRequest.getAttribute(RequestDispatcher.ERROR_MESSAGE).toString();
				String		errorStatusCode	= httpServletRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE).toString();
				HttpStatus	httpStatus		= HttpStatus.INTERNAL_SERVER_ERROR;

				for (HttpStatus currentStatus : HttpStatus.values()) {
					if (currentStatus.value() == Integer.parseInt(errorStatusCode)) {
						httpStatus = currentStatus;
					}
				}
					return new ResponseEntity<String>(errorMessage, httpStatus);
			}
			
			if(httpServletRequest.getHeader("X-Requested-With") != null &&
					httpServletRequest.getHeader("X-Requested-With").equalsIgnoreCase("XMLHttpRequest")) {
				String		errorMessage	= httpServletRequest.getAttribute(RequestDispatcher.ERROR_MESSAGE).toString();
				String		errorStatusCode	= httpServletRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE).toString();
				HttpStatus hStatus = null;
				try {
					if(isCustomException) {
						hStatus = HttpStatus.valueOf(customErrorCode);
						errorMessage = customErrorMessage;
					} else {
						hStatus = HttpStatus.valueOf(Integer.parseInt(errorStatusCode));
					}
				} catch (Throwable a_th) {
					hStatus = HttpStatus.FAILED_DEPENDENCY;
					a_th.printStackTrace();
				}
				return new ResponseEntity<String>(errorMessage, hStatus);
			}
			
			if (statusCode == HttpStatus.NOT_FOUND.value()) {
				String fallbackTemplate = null;
				if ("/".equals(url)) {
					UserDetailsVO	userDetailsVO		= userDetails.getUserDetails();
					List<String>	roleIdList			= userDetailsVO.getRoleIdList();
					List<String>	roleIdPriorityList	= jwsRoleRepository.getRoleIdByPriority(roleIdList);
					List<String>	homePageURLList		= iModuleListingRepository.getModuleURLByRoleId(roleIdPriorityList,
							Constant.IS_HOME_PAGE);
					if(roleIdList.size() >= 2 && roleIdList.contains("ADMIN") 
							&& roleIdList.contains("AUTHENTICATED")) {
						return menuService.getTemplateWithSiteLayout("control-panel", new HashMap<String, Object>());
					} else {
						if (!CollectionUtils.isEmpty(homePageURLList)) {
							for (String homePageURL : homePageURLList) {
								if (StringUtils.isBlank(fallbackTemplate) && !StringUtils.isBlank(homePageURL)) {
									fallbackTemplate = masterModuleService.loadTemplate(httpServletRequest, homePageURL, httpServletResponse);
								}
							}
						}
					}
				}
				if ("/".equals(url) && fallbackTemplate == null) {
					return menuService.getTemplateWithSiteLayout("control-panel", new HashMap<String, Object>());
				} else if (fallbackTemplate == null) {
					if(isCustomException) {
						parameterMap.put("errorMessage", customErrorMessage);
					}
					return menuService.getTemplateWithSiteLayout(templateVO.getTemplateName(), parameterMap);
				} else {
					return fallbackTemplate;
				}
			}
			if(isCustomException) {
				parameterMap.put("errorMessage", customErrorMessage);
			}else if (exception != null) {
				parameterMap.put("errorMessage", exception.getCause());
			} else {
				parameterMap.put("errorMessage", httpServletRequest.getAttribute(RequestDispatcher.ERROR_MESSAGE) );
			}

		}
		return menuService.getTemplateWithSiteLayout(templateVO.getTemplateName(), parameterMap);
	}

	@Override
	public String getErrorPath() {
		return null;
	}

}
