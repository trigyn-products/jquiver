package com.trigyn.jws.webstarter.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.dbutils.spi.PropertyMasterDetails;
import com.trigyn.jws.security.service.SecurityManagementService;

@RestController
@RequestMapping(value = "/cf")
public class JwsSecurityManagementController {

	@Autowired
	private SecurityManagementService	securityManagementService	= null;

	@Autowired
	private PropertyMasterDetails		propertyMasterDetails		= null;

	@GetMapping(value = "/scm")
	public String securityManagement() throws Exception {
		return securityManagementService.securityManagement();
	}

	@PostMapping(value = "/ddosc")
	public String loadDDOSConfiguration() throws Exception {
		return securityManagementService.loadDDOSConfiguration();
	}

	@PostMapping(value = "/sddosd")
	public void saveDDOSDetails(HttpServletRequest a_httpServletRequest) throws Exception {
		propertyMasterDetails.resetPropertyMasterDetails();
	}

}
