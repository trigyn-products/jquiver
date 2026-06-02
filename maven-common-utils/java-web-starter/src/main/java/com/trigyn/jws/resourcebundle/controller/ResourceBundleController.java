package com.trigyn.jws.resourcebundle.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.trigyn.jws.resourcebundle.service.ResourceBundleService;
import com.trigyn.jws.webstarter.utils.RedissonQueryCacheManagerUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/cf")
public class ResourceBundleController {

	@Autowired
	private SessionLocaleResolver	sessionLocaleResolver	= null;

	@Autowired
	private ResourceBundleService	resourceBundleService	= null;

	@Autowired
	private RedissonQueryCacheManagerUtil cacheManager = null;
	
	@GetMapping(value = "/cl", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public boolean changeLanguage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String cacheName = "resourceBundleCache";
		cacheManager.invalidateRegion(cacheName);
		return Boolean.TRUE;
	}

	@PostMapping(value = "/getResourceBundleData")
	@ResponseBody
	public Object getResourceBundleData(HttpServletRequest request) throws Exception {
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
