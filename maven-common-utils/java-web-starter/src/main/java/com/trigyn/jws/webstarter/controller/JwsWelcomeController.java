package com.trigyn.jws.webstarter.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.resourcebundle.service.ResourceBundleService;
import com.trigyn.jws.resourcebundle.vo.LanguageVO;
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.usermanagement.security.config.UserInformation;

@RestController
@RequestMapping("/cf")
public class JwsWelcomeController {

	private final static Logger		logger					= LogManager.getLogger(JwsWelcomeController.class);

	@Autowired
	private MenuService		menuService		= null;

	@Autowired
	private ServletContext	servletContext	= null;

	@Autowired
	private ResourceBundleService	resourceBundleService	= null;

	@GetMapping()
	@ResponseBody
	public void welcome(HttpServletResponse httpServletResponse) throws Exception {
		httpServletResponse.sendRedirect(servletContext.getContextPath() + "/cf/home");
	}

	@GetMapping("/welcome")
	@ResponseBody
	public String welcomeUser(Map<String, Object> model) throws Exception {

		Map<String, Object>	mapDetails	= new HashMap<>();

		String				name		= SecurityContextHolder.getContext().getAuthentication().getName();
		if (name != null && !name.equalsIgnoreCase("anonymousUser")) {
			UserInformation userDetails = (UserInformation) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			mapDetails.put("loggedInUser", Boolean.TRUE);
			mapDetails.put("userName", userDetails.getFullName());

		} else {
			mapDetails.put("loggedInUser", Boolean.FALSE);
		}

		return menuService.getTemplateWithSiteLayout("jws-welcome", mapDetails);
	}

	@PostMapping(value = "/gl", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<LanguageVO> getLanguages(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
		try {
			return  resourceBundleService.getLanguagesList();
		} catch (Exception a_exception) {
			logger.error("Error occured while loading languages.", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}

	}

}
