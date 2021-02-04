package com.trigyn.jws.resourcebundle.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@RestController
@RequestMapping("/cf")
public class ResourceBundleController {

	@Autowired
	private SessionLocaleResolver sessionLocaleResolver = null;

	@GetMapping(value = "/cl", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public boolean changeLanguage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return Boolean.TRUE;
	}
}
