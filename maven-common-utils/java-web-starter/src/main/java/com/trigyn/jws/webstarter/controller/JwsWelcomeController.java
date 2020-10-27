package com.trigyn.jws.webstarter.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.usermanagement.security.config.UserInformation;

@RestController
@RequestMapping("/cf")
public class JwsWelcomeController {

	
	@Autowired 
	private MenuService  menuService = null;
	
	@GetMapping()
	@ResponseBody
	public void welcome(HttpServletResponse httpServletResponse) throws Exception {
		 httpServletResponse.sendRedirect("/cf/login");
	}
	
	
	@GetMapping("/welcome")
	@ResponseBody
	public String welcomeUser(Map<String, Object> model) throws Exception {
		
		Map<String, Object> mapDetails =new HashMap<>();
		
		String  name = SecurityContextHolder.getContext().getAuthentication().getName();
		if(name!=null && !name.equalsIgnoreCase("anonymousUser")) {
			UserInformation userDetails = (UserInformation) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			mapDetails.put("loggedInUser", Boolean.TRUE);
			mapDetails.put("userName", userDetails.getFullName());
			
		}else {
			mapDetails.put("loggedInUser", Boolean.FALSE);
		}
		
		return menuService.getTemplateWithSiteLayout("jws-welcome",mapDetails);
	}
}
