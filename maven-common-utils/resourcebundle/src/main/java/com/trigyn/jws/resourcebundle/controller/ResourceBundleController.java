package com.trigyn.jws.resourcebundle.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.trigyn.jws.resourcebundle.service.ResourceBundleService;

@RestController
@RequestMapping("/cf")
public class ResourceBundleController {

	@Autowired
	private SessionLocaleResolver	sessionLocaleResolver	= null;

	@Autowired
	private ResourceBundleService	resourceBundleService	= null;

	@GetMapping(value = "/cl", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public boolean changeLanguage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return Boolean.TRUE;
	}

	@RequestMapping(value = "/getResourceBundleData", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> getResourceBundleData(HttpServletRequest request) throws Exception {
		try {
			String			keyInitials	= request.getParameter("resourceKeys");
			List<String>	keyList		= new ArrayList<String>(Arrays.asList(keyInitials.split(",")));

			String			localeId	= sessionLocaleResolver.resolveLocale(request).toString();
			return resourceBundleService.getResourceBundleData(localeId, keyList);
		} catch (Throwable a_th) {
			a_th.printStackTrace();
			throw a_th;
		}
	}
}
